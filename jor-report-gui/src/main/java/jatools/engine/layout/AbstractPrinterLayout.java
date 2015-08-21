package jatools.engine.layout;

import jatools.component.Component;
import jatools.core.view.CompoundView;
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
public abstract class AbstractPrinterLayout implements PrinterLayout {
    //    int x0 = 0;
    //    int y0 = 0;

    //  Rectangle remainder1;   // 
    protected CompoundView rootView;
    protected CompoundView resultView;

    
    
    
    // imageable.x = c.inset.left;
    // imageable.y = c.inset.top
    
    
    // 
    // imageable.width = c.width;
    Rectangle imageable; 
    Rectangle lastcopy;
    private Printer printer;

    protected AbstractPrinterLayout(Printer printer, Rectangle imageable, CompoundView rootView) {
        this.imageable = imageable;
        this.rootView = rootView;
        this.printer = printer;

        Insets padding = printer.getComponent().getPadding();
        PrintableArea printableArea = new PrintableArea(imageable.width + padding.right +
                padding.left, imageable.height + padding.top + padding.bottom);
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     */
    public void add(View view) {
        // TODO Auto-generated method stub
    }

    

    /**
     * 是否超出了容器的可用高度
     *
     * @param ymax DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean bottomContained(int ymax) {
        
        return imageable.contains(imageable.y + 1, ymax);
    }

    /**
    * DOCUMENT ME!
    *
    * @param c DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public final boolean contain(Component c) {
        return false;
    }

//    /**
//     * DOCUMENT ME!
//     *
//     * @param height DOCUMENT ME!
//     */
//    public void makeFooterReservation(int height) {
//        this.imageable.height -= height;
//    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CompoundView getRootView() {
        return (resultView != null) ? resultView : this.rootView;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Rectangle getImageable() {
        return this.imageable;
    }

    /**
     * DOCUMENT ME!
     *
     * @param printer DOCUMENT ME!
     */
    public void afterPrint(AbstractContainerPrinter printer) {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     * @param printer DOCUMENT ME!
     */
    public void beforePrint(Context context, AbstractContainerPrinter printer) {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     *
     * @param printer DOCUMENT ME!
     */
    public void initPrint(AbstractContainerPrinter printer) {
        // TODO Auto-generated method stub
    }

//    /**
//     * DOCUMENT ME!
//     *
//     * @param printer DOCUMENT ME!
//     * @param printer2 DOCUMENT ME!
//     *
//     * @return DOCUMENT ME!
//     */
//    public boolean interruptPrint(AbstractContainerPrinter printer, Printer printer2) {
//        // TODO Auto-generated method stub
//        return false;
//    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isFooterSupported() {
        // TODO Auto-generated method stub
        return false;
    }

//    /**
//     * DOCUMENT ME!
//     *
//     * @return DOCUMENT ME!
//     */
//    public Rectangle getLastCopyBounds() {
//        // TODO Auto-generated method stub
//        return null;
//    }

//    /**
//     * DOCUMENT ME!
//     *
//     * @return DOCUMENT ME!
//     */
//    public Rectangle getMinCopyBounds() {
//        return printer.getComponent().getBounds();
//    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Printer getPrinter() {
        return printer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean containsY(Component c,int off) {
        return contains(c);
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param off DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean containsX(Component c, int off) {
        return contains(c);
    }
}
