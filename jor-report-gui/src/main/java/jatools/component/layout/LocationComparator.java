package jatools.component.layout;

import jatools.component.Component;

import java.util.Comparator;

public class LocationComparator implements Comparator {
	static LocationComparator instance;
	 public static Comparator getInstance() {
	        if (instance == null) {
	            instance = new LocationComparator();
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
    public int compare(Object o1, Object o2) {
        Component c1 = (Component) o1;
        Component c2 = (Component) o2;
        int y1 = c1.getY();
        int y2 = c2.getY();

        if (y1 > y2) {
            return 1;
        } else if (y1 < y2) {
            return -1;
        } else {
            int x1 = c1.getX();
            int x2 = c2.getX();

            if (x1 > x2) {
                return 1;
            } else if (x1 < x2) {
                return -1;
            } else {
                return 0;
            }
        }
    }
}
