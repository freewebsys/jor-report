package jatools.engine.printer;

import jatools.component.Component;
import jatools.component.Panel;

import jatools.component.table.Cell;
import jatools.component.table.CellStore;
import jatools.component.table.FillRowPanel;
import jatools.component.table.RowPanel;

import jatools.core.view.AbstractView;
import jatools.core.view.TablePartView;
import jatools.core.view.View;

import jatools.data.reader.Cursor;
import jatools.data.reader.PaddingCursor;

import jatools.engine.Printer;

import jatools.engine.css.PrintStyle;

import jatools.engine.layout.PowerTablePrinterLayout;
import jatools.engine.layout.PrinterLayout;
import jatools.engine.layout.TablePrinterLayout;

import jatools.engine.printer.table.PrintablePanelBuilder;

import jatools.engine.script.Context;

import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class RowPanelPrinter extends AbstractPrinter {
    private static final int TOPMOST = 1;
    private static final int UNKNOWN = 0;
    private static final int OVERRIDE = 0;
    private Cursor cursor;
    protected Printer[] childPrinters;
    private int layer;
    private boolean footer;
    private boolean newPrint;
    private Repeater repeater;
    private boolean childrenDone;
    private boolean insertable;

    /**
     * Creates a new RowPanelPrinter object.
     */
    public RowPanelPrinter() {
    }

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
        this.doBeforePrint(context.getScript());

        RowPanel panel = (RowPanel) getComponent();

        newPrint = (cursor == null) || !cursor.hasNext() || this.childrenDone;

        if (cursor == null) {
            cursor = createCursor(context, panel, 0);
        }

        PrinterLayout layout = context.getLayout();
        repeater.reset2(getComponent(), context.getScript());

        boolean first = true;

        while (next(context, layout, first)) {
            first = false;
        }

        if (repeater.isOverhidden() && cursor.hasNext()) {
            cursor.last();
        }

        done = !cursor.hasNext();
        this.doAfterPrint(context.getScript());

        return null;
    }

    protected Printer[] prepareChildPrinters(Context context) {
        PrintablePanelBuilder builder = new PrintablePanelBuilder(getComponent(),
                getComponent().getCell());

        Panel[] panels;
        panels = builder.buildRowList();

        Printer[] printers = new Printer[panels.length];

        for (int i = 0; i < printers.length; i++) {
            printers[i] = context.getPrinter(panels[i]);
        }

        return printers;
    }

    private boolean printChildren(Context context, PrinterLayout tableLayout)
        throws Exception {
        Panel panel = (Panel) getComponent();

        boolean d = true;

        if (isTopMost()) {
            d = printCell(context, (TablePrinterLayout) tableLayout, (RowPanel) panel);
        } else {
            if (childPrinters == null) {
                childPrinters = prepareChildPrinters(context);
            }

            if (newPrint) {
                for (int i = 0; i < childPrinters.length; i++) {
                    childPrinters[i].reset();
                }
            }

            for (int i = 0; i < childPrinters.length; i++) {
                Printer printer = childPrinters[i];

                boolean everyPagePrint = printer.isEveryPagePrint(context.getScript());

                if (everyPagePrint) {
                    printer.reset();
                }

                if (!printer.isDone(context)) {
                    printer.print(context);

                    if (!printer.isDone(context)) {
                        d = false;

                        break;
                    }
                }
            }

            newPrint = d;
        }

        return d;
    }

    private boolean next(Context context, PrinterLayout layout, boolean first)
        throws Exception {
        if (!cursor.hasNext()) {
            return false;
        }

        if (!first) {
            this.doBeforePrint(context.getScript());
        }

        boolean result = false;

        cursor.save();
        cursor.next();
        cursor.open();

        Component c = getComponent();

        if (this.insertable) {
            ((TablePrinterLayout) layout).openRowGroup(this.getLocalID());

            childrenDone = printChildren(context, layout);
            ((TablePrinterLayout) layout).closeRowGroup();
        } else {
            childrenDone = printChildren(context, layout);
        }

        cursor.close();

        if (!childrenDone) {
            cursor.rollback();
            repeater.row = 0;

            result = false;
        } else {
            repeater.row++;

            if ((repeater.row < repeater.getMaxRow()) && layout.containsY(c, 0)) {
                result = true;

                int modCount = repeater.getModCount();

                if ((modCount > -1) && !cursor.hasNext() &&
                        (!(this.cursor instanceof PaddingCursor))) {
                    int padding = modCount - (repeater.row % modCount);

                    if ((padding > 0)) {
                        this.cursor = new PaddingCursor(padding, context);
                    }
                }
            } else {
                repeater.row = 0;

                result = false;
            }
        }

        result = result && cursor.hasNext();

        if (result) {
            this.doAfterPrint(context.getScript());
        }

        return result;
    }

    protected void setPrintStyle(PrintStyle printStyle) {
        super.setPrintStyle(printStyle);

        if ((printStyle == null) || (printStyle.getRepeatRule() == null)) {
            repeater = new Repeater(1);
        } else {
            repeater = new Repeater(printStyle.getRepeatRule());
        }
    }

    private boolean isTopMost() {
        if (layer == UNKNOWN) {
            layer = TOPMOST;

            int c = getComponent().getChildCount();

            for (int i = 0; i < c; i++) {
                if (getComponent().getChild(i).getClass() == RowPanel.class) {
                    layer = OVERRIDE;

                    break;
                }
            }
        }

        return layer == TOPMOST;
    }

    private boolean printCell(Context context, TablePrinterLayout tableLayout, RowPanel panel)
        throws Exception {
        boolean done = true;

        if (!isVisible(context.getScript())) {
            return true;
        }

        Cell cell = panel.getCell();
        CellStore cellstore = panel.getRootTable().getPanelstore().createFilledStore(cell); // getCellstore();

        for (int row = cell.row; row < (cell.row + cell.rowSpan); row++) {
            int lastRow = tableLayout.addRow(tableLayout.getTableRowHeight(row), footer);

            if (lastRow == TablePrinterLayout.FAIL) {
                return false;
            }

            for (int col = cell.column; col < (cell.column + cell.colSpan); col++) {
                Component c = cellstore.getComponentOver(row, col);

                if (c == null) {
                    continue;
                }

                if (!(c instanceof FillRowPanel) && c.getCell().isMerged()) {
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
                    AbstractView view = null;

                    Iterator tablepart = null;

                    if (v instanceof TablePartView) {
                        tablepart = ((TablePartView) v).getElements().iterator();

                        if (tablepart.hasNext()) {
                            view = (AbstractView) tablepart.next();
                        }
                    } else {
                        view = (AbstractView) v;
                        view.setCell((Cell) c.getCell().clone());
                    }

                    while (view != null) {
                        if (tableLayout.getViewRowHeight(lastRow) < c.getCell().rowSpan) {
                            tableLayout.setViewRowHeight(lastRow, c.getCell().rowSpan);
                        }

                        Cell acell = view.getCell();

                        int row0 = acell.row;
                        int row1 = acell.row2();

                        if (row0 < cell.row) {
                            row0 = cell.row;
                        }

                        if (row1 > cell.row2()) {
                            row1 = cell.row2();
                        }

                        acell.row = row0;
                        acell.rowSpan = row1 - acell.row + 1;
                        acell.row = lastRow;

                        view = (AbstractView) printer.unitView(context.getScript(), view,
                                tableLayout, true);

                        if (view != null) {
                            tableLayout.add(view);
                        }

                        if ((tablepart != null) && tablepart.hasNext()) {
                            view = (AbstractView) tablepart.next();
                        } else {
                            break;
                        }
                    }
                }

                if (!printer.isDone(context)) {
                    done = false;
                }
            }
        }

        if (done && tableLayout instanceof PowerTablePrinterLayout) {
            ((PowerTablePrinterLayout) tableLayout).printPerformed(panel, context,
                (PowerTablePrinterLayout) tableLayout);
        }

        return done;
    }

    /**
     * DOCUMENT ME!
     */
    public void reset() {
        super.reset();
        cursor = null;
    }
}
