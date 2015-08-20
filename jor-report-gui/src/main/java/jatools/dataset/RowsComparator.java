package jatools.dataset;

import jatools.designer.App;

import java.util.Comparator;


final class RowsComparator implements Comparator {
    private Comparator comparator;

    public RowsComparator(Comparator comparator) {
        if (comparator == null) {
            throw new NullPointerException(App.messages.getString("res.610")); //
        }

        this.comparator = comparator;
    }

    /**
     * DOCUMENT ME!
     *
     * @param object1 DOCUMENT ME!
     * @param object2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compare(Object object1,
                       Object object2) {
        return comparator.compare(object1, object2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param object DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object object) {
        return comparator.equals(object);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Comparator getComparator() {
        return comparator;
    }
}
