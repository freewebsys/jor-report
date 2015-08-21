package jatools.engine.printer;

import jatools.PageFormat;
import jatools.component.Page;
import jatools.core.view.PageView;
import jatools.engine.ReportJob;
import jatools.engine.layout.PagePrinterLayout;
import jatools.engine.layout.PrinterLayout;
import jatools.engine.script.Context;
import jatools.util.Util;

import java.awt.Insets;
import java.awt.Rectangle;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PagePrinter extends AbstractContainerPrinter {
    PageFormat format;
    boolean onePagePrint;
    int oldHeight = 0;
    private PageView view;

    PagePrinter(Page p, Context context) {
        open(p, context.getScript());

        this.format = p.getPageFormat();

        onePagePrint = Util.boolValue(context.getScript().get(ReportJob.ALL_IN_ONE_PAGE), false);

        if (onePagePrint) {
            Insets is = p.getPadding();
            oldHeight = p.getHeight();
            format.setHeight(Integer.MAX_VALUE - is.top - is.top);
        }
    }

    protected PrinterLayout createLayout(Context context) {
        if (context.uo > 0) {
            view = new PageView();

            view.setPageFormat(format);
            view.setBackColor(getComponent().getBackColor());

            view.setBounds(getComponent().getBounds());

            Insets is = this.getComponent().getPadding();

            view.setPadding(is);
            setBackgroundImageStyle(context.getScript(), view);

            Rectangle imageable = this.getComponent().getBounds();
            imageable.x = is.left;
            imageable.y = is.top;
            imageable.height = this.getComponent().getHeight() - imageable.y - is.bottom;
            imageable.width = this.getComponent().getWidth() - imageable.x - is.right;

            return new PagePrinterLayout(this, imageable, view, onePagePrint);
        } else {
            return null;
        }
    }

    protected boolean isFixedBounds() {
        return true;
    }

    /**
     * DOCUMENT ME!
     */
    public void resetPageFormat() {
        if (this.onePagePrint) {
            view.autoAdjustHeight();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     * @param context DOCUMENT ME!
     */
    public void setNextPage(Page p, Context context) {
        open(p, context.getScript());
    }
}
