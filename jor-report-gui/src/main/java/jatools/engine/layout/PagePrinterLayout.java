package jatools.engine.layout;

import jatools.component.Component;
import jatools.core.view.CompoundView;
import jatools.engine.Printer;

import java.awt.Rectangle;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PagePrinterLayout extends FreePrinterLayout {
    boolean onepage;

    /**
     * Creates a new PagePrinterLayout object.
     *
     * @param printer DOCUMENT ME!
     * @param imageable DOCUMENT ME!
     * @param rootView DOCUMENT ME!
     */
    public PagePrinterLayout(Printer printer, Rectangle imageable, CompoundView rootView,
        boolean onepage) {
        super(printer, imageable, rootView);
        this.onepage = onepage;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMaxBottom(Component c) {
        
        return onepage ? this.getImageable().height : c.getHeight();
    }
}
