package jatools.engine.layout;

import jatools.component.Component;
import jatools.core.view.AbstractView;
import jatools.core.view.CompoundView;
import jatools.core.view.TransformView;
import jatools.core.view.View;
import jatools.engine.Printer;
import jatools.engine.printer.AbstractContainerPrinter;
import jatools.engine.script.Context;

import java.awt.Insets;
import java.awt.Rectangle;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class FreePrinterLayout extends AbstractPrinterLayout {
    private static final String BROKEN_PRINTER = "broken.printer";
    final static int EVERY_PAGE_COPY = 0;
    final static int ROW_COPY = 1;
    final static int WRAPPED_ROW_COPY = 2;
    final static int COLUMN_COPY = 3;
    final static int WRAPPED_COLUMN_COPY = 4;
    PrintableArea printableArea;

    
    int x0;
    int y0;
    int dx;
    int dy;
    int offy = 0;

    /**
    * Creates a new FreePrinterLayout object.
    *
    * @param imageable DOCUMENT ME!
    * @param rootView DOCUMENT ME!
    */
    public FreePrinterLayout(Printer printer, Rectangle imageable, CompoundView rootView) {
        super(printer, imageable, rootView);

        Insets padding = printer.getComponent().getPadding();
        printableArea = new PrintableArea(imageable.width + padding.right + padding.left,
                imageable.height + padding.top + padding.bottom);
    }

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //    public boolean copy(Context context) {
    //        Rectangle mincopy = getMinCopyBounds();
    //        printableArea.moveTo(printableArea.getX(),
    //            printableArea.getY() + rootView.getBounds().height + copyRule.gapy);
    //
    
    //        if (printableArea.canBottomAppend(copyRule.gapy + mincopy.height)) {
    //            imageable.height -= (rootView.getBounds().height - copyRule.gapy);
    //
    //            //  imageable.y += (rootView.getBounds().height - copyRule.gapy);
    //            CompoundView newView = ((AbstractContainerPrinter) this.getPrinter()).createView(context);
    //            newView.getBounds().setLocation(printableArea.getX(), printableArea.getY());
    //
    
    //
    
    //            //            if (resultView == null) {
    //            //                resultView = newView;
    //            //            } else {
    //            if (resultView == null) {
    
    //                resultView = new CompoundView();
    //                resultView.add(rootView);
    //                resultView.setBounds((Rectangle) rootView.getBounds().clone());
    //                rootView.getBounds().setLocation(0, 0);
    //            }
    //
    //            //            }
    //            //    Rectangle r = resultView.getBounds().union(rootView.getBounds());
    //            //   resultView.setBounds(r);
    //            resultView.add(newView);
    //            rootView = newView;
    //
    //            dx = dy = offy = 0;
    //
    //            return true;
    //        } else {
    //            return false;
    //        }
    //    }

    /**
    * DOCUMENT ME!
    *
    * @param view DOCUMENT ME!
    */
    public void add(View view) {
        if (view != null) {
            
            AbstractView av = (AbstractView) view;

            if (av.isAutoSize()) {
                av.autoResize();
            }

            av.getBounds().x += (dx);
            av.getBounds().y += (dy);

            int bottom = av.getBounds().height + av.getBounds().y;

            if (bottom > rootView.getBounds().height) {
                rootView.getBounds().height = bottom + rootView.getPadding().top +
                    rootView.getPadding().bottom;
            }

            //            int right = av.getBounds().width + av.getBounds().x;
            //
            //            if (right > rootView.getBounds().width) {
            //                rootView.getBounds().width = right + rootView.getPadding().left +
            //                    rootView.getPadding().right;
            //            }
            this.rootView.add(view);
        }
    }

    /**
     * DOCUMENT ME!
     * @param printer DOCUMENT ME!
     */
    public void beforePrint(Context context, AbstractContainerPrinter printer) {
        //        offy = 0;
        //
        //        Printer brokenPrinter = (Printer) printer.getProperty(BROKEN_PRINTER);
        //
        //        if (brokenPrinter != null) {
        
        //            offy = brokenPrinter.getComponent().getY();
        
        //            getImageable().height -= offy;
        //            getRootView().add(new TransformView(0, -offy));
        //            printer.setProperty(BROKEN_PRINTER, null);
        //        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMaxBottom(Component c) {
        return this.getImageable().height - c.getY();
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMaxRight(Component c) {
        return this.getImageable().width - c.getX();
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void translate(int x, int y) {
        this.dx += x;
        this.dy += y;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isFooterSupported() {
        return false;
    }

    /**
     * DOCUMENT ME!
     */
    public void afterPrint(AbstractContainerPrinter printer) {
        if (offy != 0) {
            getRootView().add(new TransformView(0, offy));
        }

        // translate(0, getRootView().getBounds().height);
    }

    /**
     * DOCUMENT ME!
     *
     * @param printer DOCUMENT ME!
     */
    public void initPrint(AbstractContainerPrinter printer) {
        //        Printer[] printers = printer.getChildPrinters();
        //        Arrays.sort(printers, comparator);
    }

    /**
     * DOCUMENT ME!
     *
     * @param printer DOCUMENT ME!
     * @param printer2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //    public boolean interruptPrint(AbstractContainerPrinter printer, Printer printer2) {
    //        //  printer.setProperty(BROKEN_PRINTER, printer2);
    //        return true;
    //    }

    /**
     * DOCUMENT ME!
     */
    public void doLayout() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(Component c) {
        int bottom = c.getPadding().top + /* c.getY() +*/
            getImageable().height + c.getPadding().bottom;

        return bottom >= (c.getHeight() /*+ c.getY()*/);
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean containsX(Component c, int off) {
        int right = c.getPadding().left + getImageable().width + c.getPadding().right;

        return right >= (c.getWidth() + off);
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean containsY(Component c, int off) {
        int h = c.getPadding().top + getImageable().height + c.getPadding().bottom;

        return h >= (c.getHeight() + off);
    }

    /**
     * DOCUMENT ME!
     *
     * @param component DOCUMENT ME!
     */
    public void reserveFooterSpace(Component component) {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     */
    public void restoreFooterSpace() {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //    public boolean isInsideCopySupported() {
    //        return copyRule.rule != EVERY_PAGE_COPY;
    //    }
    //
    //    class _CopyRule {
    //        private int rule;
    //        private int gapx;
    //        private int gapy = 5;
    //    }

    /**
     * DOCUMENT ME!
     *
     * @param offx DOCUMENT ME!
     * @param offy DOCUMENT ME!
     */
}
