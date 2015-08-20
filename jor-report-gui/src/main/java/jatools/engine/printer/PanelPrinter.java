package jatools.engine.printer;

import jatools.component.Page;
import jatools.component.Panel;

import jatools.component.layout.PrinterLocationComparator;

import jatools.core.view.CompoundView;

import jatools.engine.layout.FreePrinterLayout;
import jatools.engine.layout.PrinterLayout;

import jatools.engine.script.Context;
import jatools.engine.script.Script;

import java.awt.Insets;
import java.awt.Rectangle;

import java.util.Arrays;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class PanelPrinter extends AbstractContainerPrinter {

    protected void initPrinters() {
        Arrays.sort(childPrinters, PrinterLocationComparator.getInstance());
    }

    protected PrinterLayout createLayout(Context context) {
        Rectangle r = new Rectangle(this.getComponent().getBounds());

        CompoundView view = new CompoundView();

        view.setBackColor(this.getComponent().getBackColor());
        view.setBounds(r);
        view.setPadding(this.getComponent().getPadding());
        setBackgroundImageStyle(context.getScript(), view);
        view.setBorder(this.getComponent().getBorder());

        Insets is = this.getComponent().getPadding();
        Rectangle imageable = this.getComponent().getBounds();
        imageable.x = is.left;
        imageable.y = is.top;

        imageable.height = context.getLayout().getMaxBottom(this.getComponent()) - is.top -
            is.bottom;
        imageable.width = context.getLayout().getMaxRight(this.getComponent()) - is.left -
            is.right;

        // view.setImageable((Rectangle) imageable.clone());
        return new FreePrinterLayout(this, imageable, view);
    }

    // private void loadBackgroundImage(BackgroundImageCSS css) {
    /**
     * DOCUMENT ME!
     *
     * @param script
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEveryPagePrint(Script script) {
        Panel p = (Panel) this.getComponent();

        if ((p.getType() == Page.FOOTER) || (p.getType() == Page.HEADER)) {
            return true;
        } else {
            return super.isEveryPagePrint(script);
        }
    }
}

