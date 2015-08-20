package jatools.component.layout;

import jatools.component.Component;
import jatools.engine.Printer;

import java.util.Comparator;

public class PrinterLocationComparator extends LocationComparator {
	static PrinterLocationComparator instance2;

	public static Comparator getInstance() {
		if (instance2 == null) {
			instance2 = new PrinterLocationComparator();
		}

		return instance2;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param o1
	 *            DOCUMENT ME!
	 * @param o2
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public int compare(Object o1, Object o2) {
		Component c1 = ((Printer) o1).getComponent();
		Component c2 = ((Printer) o2).getComponent();
		return super.compare(c1, c2);
	}
}
