package jatools.dataset;

import java.util.Comparator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class RowFieldComparator implements Comparator {
    int col;

    /**
     * Creates a new RowFieldComparator object.
     *
     * @param col DOCUMENT ME!
     */
    public RowFieldComparator(int col) {
        this.col = col;
    }

    /**
    * DOCUMENT ME!
    *
    * @param o1 DOCUMENT ME!
    * @param o2 DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public int compare(Object o1, Object o2) {
        Row row1 = (Row) o1;
        Row row2 = (Row) o2;

        return RowsService.compareComparables((Comparable) row1.getValueAt(col),
            (Comparable) row2.getValueAt(col));
    }
}
