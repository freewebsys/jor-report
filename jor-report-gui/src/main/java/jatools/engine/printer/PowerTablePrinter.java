package jatools.engine.printer;


import jatools.component.Component;
import jatools.component.Panel;
import jatools.component.table.Cell;
import jatools.component.table.CellStore;
import jatools.component.table.PowerTable;
import jatools.component.table.RowPanel;
import jatools.component.table.Table;
import jatools.component.table.TableBase;
import jatools.core.view.AbstractView;
import jatools.core.view.CompoundView;
import jatools.engine.CellFinder;
import jatools.engine.Printer;
import jatools.engine.css.CSSValue;
import jatools.engine.css.rule.CrossTabRule;
import jatools.engine.layout.PowerTablePrinterLayout;
import jatools.engine.layout.PrinterLayout;
import jatools.engine.layout.TablePrinterLayout;
import jatools.engine.layout.TableView;
import jatools.engine.printer.table.PrintablePanelBuilder;
import jatools.engine.printer.table.TablePrinter;
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
public class PowerTablePrinter extends AbstractContainerPrinter {
    private static CrossTabRule defaultRule;
    static TableView lastTableView = null;
    CellFinder cellFinder;

    protected Printer[] childPrinters;
    private TablePrinter headerPrinter;
    private TableView lastView;
    int printCount;
    private int[] lastColumns;
    boolean done;

    protected PrinterLayout createLayout(Context context) {
        TableView view = createView(context);
        PowerTablePrinterLayout layout = (PowerTablePrinterLayout) createLayout(context, view);

        return layout;
    }

    protected void preparePrinters(Context context) {
    }

    protected void resetPrinters() {
        printCount = 0;
    }

    protected Printer[] prepareChildPrinters(Context context) {
        Table t = (Table) this.getComponent();
        PrintablePanelBuilder builder = new PrintablePanelBuilder(t,
                new Cell(0, 0, t.getColumnCount(), t.getRowCount()));

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

    protected PrinterLayout popLayout(Context context) {
        PowerTablePrinterLayout layout = (PowerTablePrinterLayout) super.popLayout(context);
        layout.unlockLayout();

        return layout;
    }

    protected void pushLayout(Context context, PrinterLayout layout) {
        super.pushLayout(context, layout);
        ((PowerTablePrinterLayout) layout).lockLayout();
    }

    protected PrinterLayout createLayout(Context context, CompoundView view) {
        if (cellFinder == null) {
            cellFinder = CellFinder.create((TableBase) this.getComponent());
        }

        Insets is = this.getComponent().getPadding();
        Rectangle imageable = this.getComponent().getBounds();
        imageable.x = is.left;
        imageable.y = is.top;

        Rectangle parent = context.getLayout().getImageable();

        imageable.height = context.getLayout().getMaxBottom(getComponent()) - imageable.y -
            is.bottom;
        imageable.width = parent.width - imageable.x - is.right;

        TablePrinterLayout layout = new PowerTablePrinterLayout(this, imageable, view, cellFinder,
                true, this.lastView);

        return layout;
    }

    protected TableView createView(Context context) {
        PowerTable t = (PowerTable) this.getComponent();
        TableView view = new TableView(new int[0], new int[0], t.getName());

        view.setBackColor(t.getBackColor());
        view.setBounds(t.getBounds());
        view.setBorder(t.getBorder());

        setBackgroundImageStyle(context.getScript(), view);

        return view;
    }

    protected boolean printChildren(Context context, PrinterLayout layout)
        throws Exception {
        PowerTablePrinterLayout powerLayout = (PowerTablePrinterLayout) layout;

        if (this.lastView == null) {
            context.pushLayout(layout);

            CrossTabRule rule = getCrossTabRule(context.getScript());

            if ((printCount == 0) || rule.is(rule.topHeaderVisible, "everypage", true)) {
                printHeader(context, layout);

                TableView v = (TableView) layout.getRootView();
                lastColumns = v.getColumns().getSizes();
            } else {
                TableView v = (TableView) layout.getRootView();

                for (int i = 0; i < lastColumns.length; i++) {
                    powerLayout.addColumn(lastColumns[i]);
                }
            }

            printCount++;

            done = printBody(context, layout);
            context.popLayout();

            powerLayout.calcLastView(getCrossTabRule(context.getScript()),
                (TableView) powerLayout.getRootView());
        } else {
            powerLayout.calcLastView(getCrossTabRule(context.getScript()),
                (TableView) powerLayout.getRootView());
        }

        this.lastView = powerLayout.getLastView();

        return done;
    }

    private static CrossTabRule getDefaultRule() {
        if (defaultRule == null) {
            defaultRule = new CrossTabRule();
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

    private boolean printBody(Context context, PrinterLayout layout)
        throws Exception {
        PowerTable pt = (PowerTable) getComponent();
        Table leftHeader = pt.getLeftHeader();
        TablePrinter printer = (TablePrinter) context.getPrinter(leftHeader);
        printer.print(context, layout);

        return printer.isDone(context);
    }

    private void printHeader(Context context, PrinterLayout layout)
        throws Exception {
        PowerTable pt = (PowerTable) getComponent();
        Table topHeader = pt.getTopHeader();

        if (topHeader != null) {
            TablePrinterLayout tableLayout = (TablePrinterLayout) layout;

            for (int i = 0; i < topHeader.getRowCount(); i++) {
                tableLayout.addRow(pt.getRowHeight(i));
            }

            printUpperLeftRect(pt, context, tableLayout);

            headerPrinter = (TablePrinter) context.getPrinter(topHeader);
            headerPrinter.print(context, layout);
        }
    }

    private void printUpperLeftRect(PowerTable pt, Context context, TablePrinterLayout tableLayout)
        throws Exception {
        Table topHeader = pt.getTopHeader();
        Table leftHeader = pt.getLeftHeader();
        CellStore cellstore = pt.getCellstore();
        Cell cell = new Cell(0, 0, leftHeader.getColumnCount(), topHeader.getRowCount());

        if (leftHeader != null) {
            for (int col = 0; col < cell.colSpan; col++) {
                tableLayout.addColumn(pt.getColumnWidth(col));

                for (int row = 0; row < cell.rowSpan; row++) {
                    Component c = cellstore.getComponentOver(row, col);

                    if (c == null) {
                        continue;
                    }

                    if (c.getCell().isMerged()) {
                        if ((col > cell.column) && (cellstore.getComponentOver(row, col - 1) == c)) {
                            continue;
                        }

                        if ((row > cell.row) && (cellstore.getComponentOver(row - 1, col) == c)) {
                            continue;
                        }
                    }

                    Printer printer = context.getPrinter(c);
                    AbstractView v = (AbstractView) printer.print(context);

                    if (v != null) {
                        AbstractView view = (AbstractView) v;
                        view.setCell((Cell) c.getCell().clone());

                        Cell b = view.getCell();

                        int row0 = b.row;
                        int row1 = b.row2();

                        if (row0 < cell.row) {
                            row0 = cell.row;
                        }

                        if (row1 > cell.row2()) {
                            row1 = cell.row2();
                        }

                        b.row = row0;
                        b.rowSpan = row1 - b.row + 1;
                        v = (AbstractView) printer.unitView(context.getScript(), v, tableLayout,
                                false);

                        if (v != null) {
                            tableLayout.add(v);
                        }
                    }
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDone(Context context) {
        return/* done ||*/ ((lastView == null) && super.isDone(context));
    }

    /**
     * DOCUMENT ME!
     *
     * @param panel DOCUMENT ME!
     * @param context DOCUMENT ME!
     * @param layout DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void printCellsLeadBy(RowPanel panel, Context context, PowerTablePrinterLayout layout)
        throws Exception {
        Cell cell = panel.getCell();
        layout.locate(cell.row, cell.column2());
        layout.lockColumn();
        layout.setCrossBody(cell.row, cell.row2());
        headerPrinter.print(context, layout);

        layout.clearCrossBody();
        layout.unlockColumn();
    }
}
