package jatools.dataset;

import jatools.dom.RowList;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public interface RowSet extends RowList {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Key key();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int firstRow();

    /**
     * DOCUMENT ME!
     *
     * @param data DOCUMENT ME!
     * @param col DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] valuesAt(int col);

    /**
     * DOCUMENT ME!
     *
     * @param data DOCUMENT ME!
     * @param col DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object valueAt(int col);
    
    public Object valueAt(int col,boolean def);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Row[] toArray();
}
