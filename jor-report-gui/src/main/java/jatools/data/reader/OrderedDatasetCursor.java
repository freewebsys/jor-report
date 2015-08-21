package jatools.data.reader;

import jatools.dataset.Dataset;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class OrderedDatasetCursor {
    int row = -1;
    String[] fields;
    boolean[] orders;
    Dataset dataset;
    int[] indexmaps;

    /**
     * Creates a new OrderedDatasetCursor object.
     *
     * @param dataset DOCUMENT ME!
     * @param fields DOCUMENT ME!
     * @param orders DOCUMENT ME!
     */
    public OrderedDatasetCursor(Dataset dataset, String[] fields, boolean[] orders) {
        this.dataset = dataset;
        this.fields = fields;
        this.orders = orders;
        init();
    }

    private void init() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param fieldIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue(int row, int fieldIndex) {
        return this.dataset.getValueAt(indexmaps[row], fieldIndex);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRow() {
        return row;
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean next() {
        if (isEof()) {
            return false;
        }

        row++;

        return !isEof();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEof() {
        return this.indexmaps.length == row;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object value() {
        return new Integer(this.getRow() + 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int last() {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dataset getDataset() {
        // TODO Auto-generated method stub
        return null;
    }
}
