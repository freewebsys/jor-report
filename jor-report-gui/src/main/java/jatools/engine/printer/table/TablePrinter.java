package jatools.engine.printer.table;

import jatools.component.Panel;

import jatools.component.table.Cell;
import jatools.component.table.Table;

import jatools.core.view.View;

import jatools.engine.CellFinder;
import jatools.engine.Printer;

import jatools.engine.css.CSSValue;
import jatools.engine.css.rule.CrossTabRule;

import jatools.engine.layout.PrinterLayout;
import jatools.engine.layout.TablePrinterLayout;
import jatools.engine.layout.TableView;
import jatools.engine.layout.TableViewSplitter;

import jatools.engine.printer.AbstractContainerPrinter;

import jatools.engine.script.Context;
import jatools.engine.script.Script;

import java.awt.Insets;
import java.awt.Rectangle;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class TablePrinter extends AbstractContainerPrinter {
    private static CrossTabRule EMPTY = new CrossTabRule();
    protected CellFinder cellFinder;
    protected PrinterLayout layout;
    private TableView lastView;
    private TableView topHeader;
    private CrossTabRule crossRule;
    private CrossTabRule defaultRule;
    boolean done;

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public View print(Context context) throws Exception {
        int type = ((Table) this.getComponent()).isRightFlow() ? 1 : 0;
        type = context.getScript().setStackType(type);

        if (this.crossRule == null) {
            this.crossRule = getCrossTabRule(context.getScript());
        }

        View view = null;

        if ((this.crossRule == EMPTY) ||
                ((this.crossRule.headerColumns == -1) && (this.crossRule.headerRows == -1))) {
            view = super.print(context);
        } else {
            TablePrinterLayout tableLayout = (TablePrinterLayout) this.createLayout(context);

            if (this.lastView == null) {
                //  this.topHeader = null;
                this.lastView = (TableView) super.print(context);

                if (this.crossRule.is(crossRule.topHeaderVisible, "everypage", true) &&
                        (this.crossRule.headerRows > 0)) {
                    TableViewSplitter splitter = new TableViewSplitter(this.lastView, crossRule);
                    this.topHeader = splitter.copyRowsBefore(crossRule.headerRows);
                }
            }

            this.lastView = tableLayout.calcLastView(this.crossRule, this.lastView);
            view = tableLayout.getRootView();
            ((TableView) view).doLayout(true);
        }

        // tableLayout.doLayout();
        context.getScript().setStackType(type);

        return view;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param script DOCUMENT ME!
     */
    private CrossTabRule getDefaultRule() {
        if (defaultRule == null) {
            this.defaultRule = new CrossTabRule();
            defaultRule.leftHeaderVisible = CSSValue.TRUE;
            defaultRule.topHeaderVisible = CSSValue.TRUE;
            defaultRule.pageWrap = CSSValue.TRUE;
        }

        return defaultRule;
    }

    CrossTabRule getCrossTabRule(Script script) {
        CrossTabRule rule = this.getPrintStyle().getCrossTabRule();

        if (rule != null) {
            rule.reset(script);

            return rule;
        } else {
            return getDefaultRule();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     * @param layout DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void print(Context context, PrinterLayout layout)
        throws Exception {
        this.layout = layout;
        print(context);
    }

    protected Printer[] prepareChildPrinters(Context context) {
        Table t = (Table) this.getComponent();
        Cell cell = t.getCell();

        if (cell == null) {
            cell = new Cell(0, 0, t.getColumnCount(), t.getRowCount());
        }

        PrintablePanelBuilder builder = new PrintablePanelBuilder(t, cell);
        Panel[] panels;

        if (t.isRightFlow()) {
            panels = builder.buildColumnList();
        } else {
            panels = builder.buildRowList();
        }

        Printer[] printers = new Printer[panels.length];

        for (int i = 0; i < printers.length; i++) {
            printers[i] = context.getPrinter(panels[i]);
        }

        return printers;
    }

    protected PrinterLayout createLayout(Context context) {
        if (layout != null) {
            return layout;
        }

        Table t = (Table) this.getComponent();

        TableView view = null;

        if ((this.topHeader != null)) {
            TableViewSplitter splitter = new TableViewSplitter(this.topHeader, crossRule);
            view = splitter.copyRowsBefore(-1);
        } else {
            if (t.isRightFlow()) {
                view = new TableView(t.getRowHeights(), new int[0], t.getName());
            } else {
                view = new TableView(new int[0], t.getColumnWidths(), t.getName());
            }
        }

        view.setBackColor(t.getBackColor());
        view.setBounds(t.getBounds());
        view.setBorder(t.getBorder());

        setBackgroundImageStyle(context.getScript(), view);

        if (cellFinder == null) {
            cellFinder = CellFinder.create((Table) this.getComponent());
        }

        Insets is = this.getComponent().getPadding();
        Rectangle imageable = this.getComponent().getBounds();
        imageable.x = is.left;
        imageable.y = is.top;

        Rectangle parent = context.getLayout().getImageable();
        imageable.height = context.getLayout().getMaxBottom(getComponent()) - imageable.y -
            is.bottom;

        imageable.width = context.getLayout().getMaxRight(this.getComponent()) - is.left -
            is.right;

        TablePrinterLayout layout = new TablePrinterLayout(this, imageable, view, cellFinder,
                ((Table) this.getComponent()).isRightFlow());

        return layout;
    }

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDone(Context context) {
        return done || (super.isDone(context) && (this.lastView == null));
    }

    /**
     * DOCUMENT ME!
     *
     * @param remainings DOCUMENT ME!
     */

    //    public void setRemainingView2(TableView remainings) {
    //        this.remainingView = remainings;
    //    }
}
