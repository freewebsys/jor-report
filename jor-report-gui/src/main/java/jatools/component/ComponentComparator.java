/*
 *   Author: John.
 *
 *   杭州杰创软件   All Copyrights Reserved.
 */


package jatools.component;


import java.util.Comparator;


/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.1 $
 * @author $author$
 */
 final public class ComponentComparator implements Comparator {
    private static ComponentComparator instance;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Comparator getInstance() {
        if (instance == null) {
            instance = new ComponentComparator();
        }

        return instance;
    }

    /**
     * DOCUMENT ME!
     *
     * @param o1 DOCUMENT ME!
     * @param o2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compare(Object o1,
                       Object o2) {
        Component comp1 = (Component) o1;
        Component comp2 = (Component) o2;

        int index1 = calculateIndex(comp1);
        int index2 = calculateIndex(comp2);

        return index1 - index2;
    }

    /**
     * DOCUMENT ME!
     *
     * @param comp DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int calculateIndex(Component comp) {
        int index = 0;

        Component parent = comp.getParent();

        if (parent != null) {
            index = parent.indexOf(comp);
        }

        index += comp.getLayer();

        return index;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        return false;
    }
}
