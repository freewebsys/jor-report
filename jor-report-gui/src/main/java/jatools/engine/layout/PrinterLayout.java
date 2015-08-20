package jatools.engine.layout;

import jatools.component.Component;
import jatools.core.view.CompoundView;
import jatools.core.view.View;
import jatools.engine.Printer;
import jatools.engine.printer.AbstractContainerPrinter;
import jatools.engine.script.Context;

import java.awt.Rectangle;

/**
 * DOCUMENT ME!
 * 
 * @author $author$
 * @version $Revision$
 */
public interface PrinterLayout {
	/**
	 * DOCUMENT ME!
	 * 
	 * @param view
	 *            DOCUMENT ME!
	 */
	public void add(View view);

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public CompoundView getRootView();

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Rectangle getImageable();

	/**
	 * DOCUMENT ME!
	 * 
	 * @param c
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public int getMaxBottom(Component c);

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean isFooterSupported();

	/**
	 * DOCUMENT ME!
	 * 
	 * @param context
	 *            TODO
	 * @param printer
	 */
	public void beforePrint(Context context, AbstractContainerPrinter printer);

	/**
	 * DOCUMENT ME!
	 * 
	 * @param printer
	 */
	public void initPrint(AbstractContainerPrinter printer);

	/**
	 * DOCUMENT ME!
	 * 
	 * @param printer
	 *            DOCUMENT ME!
	 */
	public void afterPrint(AbstractContainerPrinter printer);

	/**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public void doLayout();

	/**
	 * DOCUMENT ME!
	 * 
	 * @param c
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean contains(Component c);

	/**
	 * DOCUMENT ME!
	 * 
	 * @param c
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean containsY(Component c, int off);

	/**
	 * DOCUMENT ME!
	 * 
	 * @param c
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public boolean containsX(Component c, int off);

	/**
	 * DOCUMENT ME!
	 * 
	 * @param component
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public int getMaxRight(Component component);

	/**
	 * DOCUMENT ME!
	 * 
	 * @param component
	 *            DOCUMENT ME!
	 */
	public void reserveFooterSpace(Component component);

	public Printer getPrinter();

	/**
	 * DOCUMENT ME!
	 */
	public void restoreFooterSpace();
}
