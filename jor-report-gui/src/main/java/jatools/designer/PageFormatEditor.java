package jatools.designer;

import jatools.swingx.Chooser;

import java.awt.print.PageFormat;
import java.awt.print.PrinterJob;

import javax.swing.JComponent;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PageFormatEditor implements Chooser {
    PageFormat value;
    boolean exitOK = false;

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean showChooser(JComponent owner, Object value) {
        this.value = (PageFormat) value;

        PrinterJob printJob = PrinterJob.getPrinterJob();

        PageFormat now = printJob.pageDialog(this.value);

        if (now == value) {
            return false;
        } else {
            value = now;

            return true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue() {
        return value;
    }
}
