package jatools.dom;

import jatools.dataset.Dataset;
import jatools.dataset.Row;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public interface RowList {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int rowAt(int i);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int length();

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Row getRow(int i);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dataset getDataset();
}
