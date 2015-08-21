package jatools.data.reader;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
  */
public class ArrayCursor implements Cursor {
    int row = -1;
    private Object[] values;

    /**
     * Creates a new BasicCursor object.
     *
     * @param dataset DOCUMENT ME!
     */
    public ArrayCursor(Object[] values) {
        this.values = values;
    }

    /* (non-Javadoc)
         * @see jatools.data.reader.Cursor#getRow()
         */

    /* (non-Javadoc)
         * @see jatools.data.reader.Cursor#next()
         */

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
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     */
    public void rollback() {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasNext() {
        return row < (this.values.length - 1);
    }

    /**
     * DOCUMENT ME!
     */
    public void last() {
        row = this.values.length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isRowBased() {
        return false;
    }
}
