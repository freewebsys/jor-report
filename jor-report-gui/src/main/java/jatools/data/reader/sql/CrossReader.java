package jatools.data.reader.sql;

import jatools.accessor.PropertyDescriptor;
import jatools.data.reader.AbstractDatasetReader;
import jatools.data.reader.DatasetReader;
import jatools.dataset.Column;
import jatools.dataset.Dataset;
import jatools.dataset.DatasetException;
import jatools.dataset.Row;
import jatools.dataset.RowMeta;
import jatools.engine.script.Script;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class CrossReader extends AbstractDatasetReader {
    DatasetReader cache;
    int _row = 0;
    private Dataset _rows;

    /**
     * Creates a new CrossReader object.
     *
     * @param cache DOCUMENT ME!
     */
    public CrossReader(DatasetReader cache) {
        this.cache = cache;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getType() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param script DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DatasetException DOCUMENT ME!
     */
    public RowMeta readStart(Script script,boolean withdata) throws DatasetException {
        _rows = cache.read(script, -1);

        Column[] infos = new Column[_rows.getRowCount()];

        for (int i = 0; i < _rows.getRowCount(); i++) {
            infos[i] = new Column("#" + i);
        }

        _row = 0;

        return new RowMeta(infos);
    }

    /**
     * DOCUMENT ME!
     *
     * @param rowInfo DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DatasetException DOCUMENT ME!
     */
    public Row readRow(RowMeta rowInfo) throws DatasetException {
        if (_row < _rows.getColumnCount()) {
            Object[] values = _rows.getValuesAt(0, _rows.getRowCount() - 1, _row);
            _row++;

            return new Row(values);
        } else {
            return Row.NO_MORE_ROWS;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DatasetException DOCUMENT ME!
     */
    public int readEnd() throws DatasetException {
        return _row;
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
     * @param script DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dataset load(Script script) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param script DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RowMeta getRowMeta(Script script) {
        // TODO Auto-generated method stub
        return null;
    }
}
