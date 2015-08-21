package jatools.dom.sort;

import java.util.Comparator;

public class ArrayNodeComparatorFactory implements NodeComparatorFactory {
    /**
     * DOCUMENT ME!
     *
     * @param command DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Comparator create(String command) {
        return new ArrayNodeComparator(command);
    }
}
