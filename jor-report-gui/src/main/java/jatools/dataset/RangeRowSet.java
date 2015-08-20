package jatools.dataset;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class RangeRowSet implements RowSet {
    public Key key;
    public int from;
    public int to;
    private Dataset data;

    /**
     * Creates a new Index object.
     *
     * @param key DOCUMENT ME!
     * @param from DOCUMENT ME!
     * @param to DOCUMENT ME!
     */
    public RangeRowSet(Dataset data, Key key, int from, int to) {
        this.data = data;
        this.key = key;
        this.from = from;
        this.to = to;
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
        return data.getValuesAt(this.from, this.to, col);
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
        return data.getValueAt(this.from, col);
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
     * @return DOCUMENT ME!
     */
    public int rowAt(int i) {
        return i + from;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int length() {
        return to - from + 1;
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
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Row[] toArray() {
        Row[] rows = new Row[this.length()];

        for (int i = from; i <= to; i++) {
            rows[i - from] = this.getDataset().getReferenceToRow(i);
        }

        return rows;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int firstRow() {
        return this.from;
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
