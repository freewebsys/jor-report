package jatools.dom.sort;

import jatools.dataset.RowsService;
import jatools.dom.ArrayNode;

import java.util.Comparator;

public class ArrayNodeComparator implements Comparator {
    boolean asc;

    ArrayNodeComparator(String command) {
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
    	ArrayNode row1 = (ArrayNode) o1;
    	ArrayNode row2 = (ArrayNode) o2;

        int value = RowsService.compareComparables((Comparable) row1.value(),
                (Comparable) row2.value());

        if (value != 0) {
            return asc ? value : (-value);
        }

        return 0;
    }
}
