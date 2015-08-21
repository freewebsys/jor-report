package jatools.data.reader;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public interface Cursor {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasNext();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public void next();

    /**
     * DOCUMENT ME!
     */
    public void open();

    /**
     * DOCUMENT ME!
     */
    public void close();

    /**
     * DOCUMENT ME!
     */
    public void save();

    /**
     * DOCUMENT ME!
     */
    public void rollback();

    /**
     * DOCUMENT ME!
     */
    public void last();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isRowBased();
}
