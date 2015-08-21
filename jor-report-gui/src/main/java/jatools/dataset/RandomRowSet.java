package jatools.dataset;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class RandomRowSet implements RowSet {
    private Dataset data;
    public Key key;
    public int[] rows;

    /**
     * Creates a new Index object.
     *
     * @param key DOCUMENT ME!
     * @param rows DOCUMENT ME!
     */
    public RandomRowSet(Dataset data, Key key, int[] rows) {
        this.data = data;
        this.key = key;
        this.rows = rows;
    }

    /**
     * DOCUMENT ME!
     *
     * @param data DOCUMENT ME!
     * @param col DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] valuesAt(int col) {
        Object[] result = new Object[rows.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = data.getValueAt(rows[i], col);
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param data DOCUMENT ME!
     * @param col DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object valueAt(int col) {
        return data.getValueAt(rows[0], col);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Key key() {
        return this.key;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int rowAt(int i) {
        return rows[i];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int length() {
        return rows.length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dataset getDataset() {
        return data;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Row getRow(int i) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Row[] toArray() {
        Row[] result = new Row[length()];

        for (int i = 0; i < result.length; i++) {
            result[i] = this.getDataset().getReferenceToRow(this.rows[i]);
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int firstRow() {
        return ((this.rows != null) && (this.rows.length > 0)) ? this.rows[0] : (-1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param col DOCUMENT ME!
     * @param def DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object valueAt(int col, boolean def) {
        Object result = valueAt(col);

        if (def && (result == null)) {
            return this.getDataset().getDefaultValue(col);
        } else {
            return result;
        }
    }
}
