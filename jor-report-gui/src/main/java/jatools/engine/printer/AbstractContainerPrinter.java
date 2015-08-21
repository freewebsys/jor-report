package jatools.engine.printer;

import jatools.component.Component;
import jatools.core.view.AbstractView;
import jatools.core.view.CompoundView;
import jatools.core.view.View;
import jatools.data.reader.Cursor;
import jatools.engine.Printer;
import jatools.engine.css.PrintStyle;
import jatools.engine.layout.FreePrinterLayout;
import jatools.engine.layout.PrinterLayout;
import jatools.engine.script.Context;

import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Map;


/**
 * 打印规则 1. 没有预留打印问题 2. 子件打印过的,不再打印 3. 第二次打印,从未打印子件,开始
 *
 * @author $author$
 * @version $Revision$
 */
public abstract class AbstractContainerPrinter extends AbstractPrinter {
    protected Printer[] childPrinters;
    protected Cursor cursor;

    // protected boolean newPrint;
    private Map properties;
    private RepeatStore repeatstore;
    private Repeater repeater;

    /**
     * DOCUMENT ME!
     *
     * @param prop
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getProperty(String prop) {
        if (properties == null) {
            return null;
        } else {
            return properties.get(prop);
        }
    }

    protected boolean isFixedBounds() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param context
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDone(Context context) {
        return (cursor != null) && !cursor.hasNext();
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        super.reset();
        cursor = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop
     *            DOCUMENT ME!
     * @param val
     *            DOCUMENT ME!
     */
    public void setProperty(String prop, Object val) {
        if (properties == null) {
            properties = new HashMap();
            properties.put(prop, val);
        }
    }

    protected void resetPrinters() {
        for (int i = 0; i < childPrinters.length; i++) {
            childPrinters[i].reset();
        }
    }

    protected boolean printChildren(Context context, PrinterLayout layout)
        throws Exception {
        layout.beforePrint(context, this);

        boolean free = (layout instanceof FreePrinterLayout);
        Object firstFooter = null;

        // for (int i = 0; i < childPrinters.length; i++) {
        if (layout.isFooterSupported()) {
            for (int i = childPrinters.length - 1; i >= 0; i--) {
                Printer printer = childPrinters[i];

                if (printer.isEveryPagePrint(context.getScript())) {
                    layout.reserveFooterSpace(printer.getComponent());
                    firstFooter = printer;
                } else {
                    break;
                }
            }
        }

        boolean prevPrintersDone = true;
        int growDelta = 0;

        for (int i = 0; i < childPrinters.length; i++) {
            Printer printer = childPrinters[i];

            int y0 = printer.getComponent().getY();

            if (free) {
                printer.getComponent().setY(y0 + growDelta);
            }

            if (printer == firstFooter) {
                layout.restoreFooterSpace();
            }

            boolean everyPagePrint = printer.isEveryPagePrint(context.getScript());
            boolean _done = printer.isDone(context);

            boolean print = false;

            if (everyPagePrint) {
                if (_done) {
                    printer.reset();
                }

                print = true;
            } else {
                if (prevPrintersDone && !_done) {
                    print = true;
                }
            }

            if (print) {
                View view = printer.print(context);

                if (free && view instanceof AbstractView) {
                    Rectangle r = ((AbstractView) view).getBounds();
                    growDelta += ((r.height - printer.getComponent().getHeight() + r.y) -
                    printer.getComponent().getY());
                }

                if (!printer.isDone(context)) {
                    layout.add(view);
                    prevPrintersDone = false;
                } else {
                    layout.add(view);

                    if ((i < (childPrinters.length - 1)) &&
                            printer.isForceBreak2(context.getScript())) {
                        prevPrintersDone = false;

                        this.setForcedBreak(true);

                        break;
                    }
                }

                // 打印一次后，取消标志
                this.setForcedBreak(false);
            } else if (free) {
                growDelta -= printer.getComponent().getHeight();
            }

            if (free) {
                printer.getComponent().setY(y0);
            }
        }

        layout.afterPrint(this);

        return prevPrintersDone;
    }

    /**
     * DOCUMENT ME!
     *
     * @param context
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    public View print(Context context) throws Exception {
        this.doBeforePrint(context.getScript());

        if (repeatstore == null) {
            repeatstore = new RepeatStore();
        }

        preparePrinters(context);

        Rectangle oldbounds = null;

        if (!isFixedBounds()) {
            oldbounds = getComponent().getBounds();
        }

        if ((cursor == null) || !cursor.hasNext()) {
            cursor = createCursor(context, this.getComponent(), context.getScript().getStackType());

            if ((oldbounds != null) && context.getLayout().getPrinter().isForcedBreak()) {
                resetLocation(context.getScript());
            }

            resetPrinters();
        } else if (oldbounds != null) {
            resetLocation(context.getScript());
        }

        repeatstore.reset();
        repeater.reset2(getComponent(), context.getScript());

        boolean first = true;

        while (next(context, first)) {
            first = false;
        }

        if (oldbounds != null) {
            this.getComponent().setBounds(oldbounds);
        }

        AbstractView result = repeatstore.getResultView();

        if (result != null) {
            this.setLayoutRule(result, context.getScript());
        }

        this.doAfterPrint(context.getScript());

        if (repeater.isOverhidden() && cursor.hasNext()) {
            cursor.last();
        }

        return result;
    }

    protected void preparePrinters(Context context) {
        if (childPrinters == null) {
            childPrinters = prepareChildPrinters(context);

            initPrinters();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param context
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    private boolean next(Context context, boolean first)
        throws Exception {
        if (!cursor.hasNext()) {
            return false;
        }

        if (!first) {
            this.doBeforePrint(context.getScript());
        }

        cursor.save();
        cursor.next();

        boolean more = false;
        cursor.open();

        if (isVisible(context.getScript())) {
            Component c = getComponent();

            PrinterLayout layout = createLayout(context);

            pushLayout(context, layout);

            boolean childrenDone = printChildren(context, layout);

            popLayout(context);

            layout.doLayout();

            CompoundView view = layout.getRootView();

            repeatstore.add(layout.getRootView());

            if (childrenDone) {
                resetPrinters();
            }

            cursor.close();

            if (!childrenDone) {
                cursor.rollback();
            }

            Rectangle b = view.getBounds();
            repeater.maxwidth = Math.max(repeater.maxwidth, b.width);
            repeater.maxheight = Math.max(repeater.maxheight, b.height);

            if (repeater.isRowFirst()) {
                repeater.col++;

                boolean shiftrow = true;

                if ((repeater.col < repeater.getMaxCol()) /* && childrenDone */) {
                    if (layout.containsX(c, b.width + repeater.getGapX())) {
                        c.setX(c.getX() + b.width + repeater.getGapX());

                        shiftrow = false;
                        more = true;
                    }

                    // else
                }

                if (shiftrow) {
                    repeater.col = 0;
                    repeater.row++;

                    if (repeater.row < repeater.getMaxRow()) {
                        if (layout.containsY(c, repeater.maxheight + repeater.getGapY())) {
                            c.setY(c.getY() + repeater.maxheight + repeater.getGapY());
                            repeater.maxheight = 0;

                            c.setX(repeater.x0);

                            more = true;
                        } else {
                            more = false;
                        }
                    } else {
                        more = false;
                    }
                }
            } else {
                repeater.row++;

                boolean shiftcol = true;

                if ((repeater.row < repeater.getMaxRow()) /* && childrenDone */) {
                    if (layout.containsY(c, b.height + repeater.getGapY())) {
                        c.setY(c.getY() + b.height + repeater.getGapY());

                        shiftcol = false;
                        more = true;
                    }

                    // else
                }

                if (shiftcol) {
                    repeater.row = 0;
                    repeater.col++;

                    if (repeater.col < repeater.getMaxCol()) {
                        if (layout.containsX(c, repeater.maxwidth + repeater.getGapX())) {
                            c.setX(c.getX() + repeater.maxwidth + repeater.getGapX());
                            repeater.maxwidth = 0;

                            c.setY(repeater.y0);

                            more = true;
                        } else {
                            more = false;
                        }
                    } else {
                        more = false;
                    }
                }
            }
        } else {
            cursor.close();
            more = cursor.hasNext();
        }

        more = more && cursor.hasNext();

        if (more) {
            this.doAfterPrint(context.getScript());
        }

        return more;
    }

    protected PrinterLayout popLayout(Context context) {
        return (PrinterLayout) context.popLayout();
    }

    protected void pushLayout(Context context, PrinterLayout layout) {
        context.pushLayout(layout);
    }

    protected void setPrintStyle(PrintStyle printStyle) {
        super.setPrintStyle(printStyle);

        if ((printStyle == null) || (printStyle.getRepeatRule() == null)) {
            repeater = new Repeater(1);
        } else {
            repeater = new Repeater(printStyle.getRepeatRule());
        }
    }

    protected void initPrinters() {
    }

    protected abstract PrinterLayout createLayout(Context context);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Printer[] getChildPrinters() {
        return childPrinters;
    }
}
