package jatools.data.reader;

import jatools.accessor.PropertyDescriptor;
import jatools.accessor.ProtectPublic;

import jatools.data.reader.udc.UserColumnBuilder;
import jatools.data.reader.udc.UserDefinedColumnBuilderCache;

import jatools.dataset.Dataset;
import jatools.dataset.DatasetException;
import jatools.dataset.FilterSet;
import jatools.dataset.Row;
import jatools.dataset.RowMeta;
import jatools.dataset.StreamService;

import jatools.engine.script.Script;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public abstract class AbstractDatasetReader implements DatasetReader, ProtectPublic {
    static int CURSOR = 1;
    static int NO_CURSOR = 2;
    static int UNKNOWN = 0;
    protected String type;
    protected String name;
    protected String description;
    protected ArrayList indexs;
    protected ArrayList userDefinedColumns;
    protected FilterSet filterSet;

    /**
     * DOCUMENT ME!
     *
     * @param field DOCUMENT ME!
     * @param strVals DOCUMENT ME!
     */
    public void select(String field, String[] strVals) {
        if (filterSet == null) {
            filterSet = new FilterSet();
        }

        filterSet.select(field, strVals, true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param field DOCUMENT ME!
     * @param strVals DOCUMENT ME!
     */
    public void unselect(String field, String[] strVals) {
        if (filterSet == null) {
            filterSet = new FilterSet();
        }

        filterSet.select(field, strVals, false);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator indexs() {
        return (indexs == null) ? null : indexs.iterator();
    }

    /**
     * DOCUMENT ME!
     *
     * @param script DOCUMENT ME!
     * @param requestRows DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DatasetException DOCUMENT ME!
     */
    public Dataset read(Script script, int requestRows)
        throws DatasetException {
        return doRead(script, requestRows);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param paraProvider DOCUMENT ME!
     * @param withdata DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DatasetException DOCUMENT ME!
     */
    public abstract RowMeta readStart(Script paraProvider, boolean withdata)
        throws DatasetException;

    /**
     * DOCUMENT ME!
     *
     * @param rowInfo DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DatasetException DOCUMENT ME!
     */
    public abstract Row readRow(RowMeta rowInfo) throws DatasetException;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DatasetException DOCUMENT ME!
     */
    public abstract int readEnd() throws DatasetException;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public StreamService getStreamService() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    abstract public Object clone();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDescription() {
        return description;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    abstract public String getType();

    /**
     * DOCUMENT ME!
     *
     * @param string DOCUMENT ME!
     */
    public void setDescription(String string) {
        description = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @param string DOCUMENT ME!
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return getName();
    }

    protected Dataset doRead(Script script, int requestRows)
        throws DatasetException {
        int count = 0;

        RowMeta rowInfo = readStart(script, requestRows != 0);
        Dataset ds = new Dataset(rowInfo);

        if (requestRows == -1) {
            requestRows = Integer.MAX_VALUE;
        }

        if (filterSet != null) {
            filterSet.initDataset(ds);
        }

        Row row;

        while ((count < requestRows) && ((row = readRow(rowInfo)) != Row.NO_MORE_ROWS)) {
            if ((filterSet == null) || filterSet.accept(row)) {
                ds.addRow(row);
                count++;
            }
        }

        readEnd();

        if (this.userDefinedColumns != null) {
            addUserColumns(ds, this.userDefinedColumns, script);
        }

        ds.setReaderSrc(this);

        return ds;
    }

    protected void addUserColumns(Dataset data, ArrayList cols, Script script) {
        Iterator it = cols.iterator();

        while (it.hasNext()) {
            Object col = it.next();
            UserColumnBuilder f = UserDefinedColumnBuilderCache.getInstance(col);
            f.build(data, col, script);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param indexs DOCUMENT ME!
     */
    public void setIndexs(ArrayList indexs) {
        this.indexs = indexs;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ArrayList getUserDefinedColumns() {
        return userDefinedColumns;
    }

    /**
     * DOCUMENT ME!
     *
     * @param userDefinedColumns DOCUMENT ME!
     */
    public void setUserDefinedColumns(ArrayList userDefinedColumns) {
        this.userDefinedColumns = userDefinedColumns;
    }
}
