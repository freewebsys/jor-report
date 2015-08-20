package jatools.dataset;

import jatools.designer.App;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class RowMeta {
    private Column[] fieldInfos;
    Map name2Index = new HashMap();

    /**
     * Creates a new ZRowInfo object.
     *
     * @param fieldInfos
     *            DOCUMENT ME!
     */
    public RowMeta(Column[] fieldInfos) {
        this.fieldInfos = fieldInfos;
        refreshCache();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Column[] getColumns() {
        return this.fieldInfos;
    }
    
    

    /**
     * DOCUMENT ME!
     */
    private void refreshCache() {
        if (fieldInfos != null) {
            for (int i = 0; i < fieldInfos.length; i++) {
                if (fieldInfos[i].getName() != null) {
                    name2Index.put(fieldInfos[i].getName(), new Integer(i));
                }

                name2Index.put("#" + i, new Integer(i));
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param i
     *            DOCUMENT ME!
     * @param name
     *            DOCUMENT ME!
     */
    public void setColumnName(int i, String name) {
        getColumnInfo(i).setName(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param field
     *            DOCUMENT ME!
     */
    public void addColumn(Column field) {
        Column[] infos = new Column[fieldInfos.length + 1];
        System.arraycopy(fieldInfos, 0, infos, 0, fieldInfos.length);
        infos[infos.length - 1] = field;
        fieldInfos = infos;
        refreshCache();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        Column[] infos = new Column[fieldInfos.length];

        for (int i = 0; i < infos.length; i++) {
            infos[i] = (Column) fieldInfos[i].clone();
        }

        return new RowMeta(infos);
    }

    /**
     * DOCUMENT ME!
     *
     * @param columnName
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ZDataSetException
     *             DOCUMENT ME!
     */
    public final int getIndexByColumnName(String columnName) {
        // Future implementation
        Integer i = (Integer) name2Index.get(columnName);

        if (i != null) {
            return i.intValue();
        } else {
            return -1;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param columnName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getIndexByColumnName(String[] columnName) {
        // Future implementation
        int[] columns = new int[columnName.length];

        for (int i = 0; i < columnName.length; i++) {
            columns[i] = getIndexByColumnName(columnName[i]);
        }

        return columns;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getColumnName(int i) {
        return getColumnInfo(i).getName();
    }

    /**
     * DOCUMENT ME!
     *
     * @param i
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Column getColumnInfo(int i) {
        int count = fieldInfos.length;

        if ((i < 0) || (i > (count - 1))) {
            throw new ArrayIndexOutOfBoundsException(App.messages.getString("res.607") +
                (count - 1) + App.messages.getString("res.608")); // //$NON-NLS-2$
        }

        return fieldInfos[i];
    }

    /**
     * DOCUMENT ME!
     *
     * @param i
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getColumnClassName(int i) {
        return getColumnInfo(i).getClassName();
    }

    /**
     * DOCUMENT ME!
     *
     * @param i
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ClassNotFoundException
     *             DOCUMENT ME!
     */
    public Class getColumnClass(int i) throws ClassNotFoundException {
        return fieldInfos[i].getColumnClass();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumnCount() {
        return fieldInfos.length;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rowInfo
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(RowMeta rowInfo) {
        int count = fieldInfos.length;

        if (count != rowInfo.getColumnCount()) {
            return false;
        }

        for (int i = 0; i < count; i++) {
            if (!getColumnClassName(i).equals(rowInfo.getColumnClassName(i))) {
                return false;
            }
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ZRowInfo:"); //
        buffer.append(" { "); //

        for (int i0 = 0; (fieldInfos != null) && (i0 < fieldInfos.length); i0++) {
            buffer.append(" fieldInfos[" + i0 + "]: "); // //$NON-NLS-2$
            buffer.append(fieldInfos[i0]);
        }

        buffer.append(" } "); //
        buffer.append("]"); //

        return buffer.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param k DOCUMENT ME!
     */
    public void remove(int k) {
        if ((k < 0) || (k > (fieldInfos.length - 1))) {
            throw new ArrayIndexOutOfBoundsException(App.messages.getString("res.609") + k);
        } else {
            ArrayList v = new ArrayList(Arrays.asList(fieldInfos));
            v.remove(k);
            fieldInfos = (Column[]) v.toArray(new Column[0]);

            refreshCache();
        }
    }
}
