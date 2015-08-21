package jatools.data.reader;

import jatools.dataset.Dataset;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class DatasetCursor implements Cursor {
    int savePoint;
    int row = -1;
    Dataset dataset;

    /**
     * Creates a new BasicCursor object.
     *
     * @param dataset DOCUMENT ME!
     */
    public DatasetCursor(Dataset ds) {
        this.dataset = ds;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public void next() {
        row++;
    }

    /**
     * DOCUMENT ME!
     */
    public void open() {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     */
    public void close() {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     */
    public void save() {
        this.savePoint = row;
    }

    /**
     * DOCUMENT ME!
     */
    public void rollback() {
        this.row = this.savePoint;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasNext() {
        return row < (this.dataset.getRowCount() - 1);
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
     */
    public void last() {
        row = this.dataset.getRowCount();
    }
    
    public void end()
    {
    	row = this.dataset.getRowCount()-1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dataset getDataset() {
        return dataset;
    }

    /**
     * DOCUMENT ME!
     *
     * @param fieldIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue(int col) {
        return this.dataset.getValueAt(this.getRow(), col);
    }

  

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isRowBased() {
        return false;
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        row = -1;
    }

	public void setRow(int i) {
		this.row = i;
		
	}
}
