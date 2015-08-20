package jatools.engine.printer;

import jatools.component.Component;

import java.util.Comparator;


public class CellComparator implements Comparator {
    public int compare(Object o1, Object o2) {
        Component c1 = (Component) o1;
        Component c2 = (Component) o2;

        return c1.getCell().compareTo(c2.getCell());
    }
    
    public static Comparator SHARE = new CellComparator();
    
}
