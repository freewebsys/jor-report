package jatools.engine;

import jatools.core.view.CompoundView;
import jatools.engine.printer.ReportPrinter;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public interface PrinterListener {
    /**
     * DOCUMENT ME!
     *
     * @param printer DOCUMENT ME!
     * @param page DOCUMENT ME!
     */
    public void pageAdded(ReportPrinter printer, CompoundView page);
}
