package jatools.dom.sort;

import jatools.dataset.RowsService;
import jatools.dom.GroupNode;

import java.util.Comparator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class GroupNodeComparator implements Comparator {
    boolean asc;

    GroupNodeComparator(String command) {
        this.asc = "asc".equals(command.trim());
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
        GroupNode row1 = (GroupNode) o1;
        GroupNode row2 = (GroupNode) o2;

        int value = RowsService.compareComparables((Comparable) row1.value(),
                (Comparable) row2.value());

        if (value != 0) {
            return asc ? value : (-value);
        }

        return 0;
    }
}
