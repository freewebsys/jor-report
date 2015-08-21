package jatools.engine.printer;

import jatools.component.ColumnPanel;
import jatools.component.Component;
import jatools.component.Panel;
import jatools.component.table.Cell;
import jatools.component.table.CellStore;
import jatools.core.view.AbstractView;
import jatools.core.view.View;
import jatools.data.reader.Cursor;
import jatools.engine.Printer;
import jatools.engine.css.PrintStyle;
import jatools.engine.layout.PowerTablePrinterLayout;
import jatools.engine.layout.PrinterLayout;
import jatools.engine.layout.TablePrinterLayout;
import jatools.engine.printer.table.PrintablePanelBuilder;
import jatools.engine.script.Context;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ColumnPanelPrinter extends AbstractPrinter {
    private static final int TOPMOST = 1;
    private static final int UNKNOWN = 0;
    private static final int OVERRIDE = 0;
    private Cursor cursor;
    protected Printer[] childPrinters;

    private int layer;

    
    private boolean newPrint;
    private Repeater repeater;

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */

    //    public View print(Context context) throws Exception {
    //        ColumnPanel panel = (ColumnPanel) getComponent();
    //
    //        newPrint = cursor == null;
    //
    //        if (cursor == null) {
    //            cursor = createCursor(context, panel, 1);
    //        }
    //
    //        printChildren(context);
    //
    //        return null;
    //    }
    public View print(Context context) throws Exception {
    	this.doBeforePrint(context.getScript());
        ColumnPanel panel = (ColumnPanel) getComponent();

        newPrint = cursor == null;

        if (cursor == null) {
            cursor = createCursor(context, panel, 1);
        }

        PrinterLayout layout = context.getLayout();

        boolean first = true;
        while (next(context, layout,first))
        {
        	first = false;
        }
            ;

        done = !cursor.hasNext();
        this.doAfterPrint(context.getScript());
        return null;
    }
    protected void setPrintStyle(PrintStyle printStyle) {
        super.setPrintStyle(printStyle);

        if ((printStyle == null) || (printStyle.getRepeatRule() == null)) {
            repeater = new Repeater(1);
        } else {
            repeater = new Repeater(printStyle.getRepeatRule());
        }
    }
    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     * @param layout DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public boolean next(Context context, PrinterLayout layout,boolean first)
        throws Exception {
        if (!cursor.hasNext()) {
            return false;
        }
        if (!first) {
            this.doBeforePrint(context.getScript());
        }
        cursor.save();
        cursor.next();
        cursor.open();

        Component c = getComponent();

        boolean childrenDone = printChildren(context, layout);

        cursor.close();

        if (!childrenDone) {
            cursor.rollback();
        }

        repeater.col++;

        boolean result = false;
        if (/*(repeater.col < repeater.getMaxCol()) &&*/ childrenDone) {
         
                result = true;
//            }

            // else
            
        }

        
        repeater.col = 0;
        result = result && cursor.hasNext() ;
        
        if (result) {
            this.doAfterPrint(context.getScript());
        }

        return result;
    }

    protected Printer[] prepareChildPrinters(Context context) {
        PrintablePanelBuilder builder = new PrintablePanelBuilder(getComponent(),
                getComponent().getCell());

        Panel[] panels;
        panels = builder.buildColumnList();

        Printer[] printers = new Printer[panels.length];

        for (int i = 0; i < printers.length; i++) {
            printers[i] = context.getPrinter(panels[i]);
        }

        return printers;
    }

    //    private void printChildren(Context context) throws Exception {
    //        Panel panel = (Panel) getComponent();
    //        TablePrinterLayout tableLayout = (TablePrinterLayout) context.getLayout();
    //
    //        while (cursor.hasNext()) {
    //            cursor.save();
    //            cursor.next();
    //            cursor.open();
    //
    //            boolean d = true;
    //
    //            if (isTopMost()) {
    //                d = printCell(context, tableLayout, (ColumnPanel) panel);
    //            } else {
    //                if (childPrinters == null) {
    //                    childPrinters = prepareChildPrinters(context);
    //                }
    //
    //                for (int i = 0; i < childPrinters.length; i++) {
    //                    Printer printer = childPrinters[i];
    //
    //                    if (newPrint) {
    //                        printer.reset();
    //                    }
    //
    //                    if (!printer.isDone(context)) {
    //                        printer.print(context);
    //
    //                        if (!printer.isDone(context)) {
    //                            d = false;
    //
    //                            break;
    //                        }
    //                    }
    //                }
    //
    //                newPrint = d;
    //            }
    //
    //            cursor.close();
    //
    //            if (!d) {
    //                cursor.rollback();
    //
    //                break;
    //            }
    //        }
    //
    //        done = !cursor.hasNext();
    //    }
    private boolean printChildren(Context context, PrinterLayout tableLayout)
        throws Exception {
        Panel panel = (Panel) getComponent();

        boolean d = true;

        if (isTopMost()) {
            d = printCell(context, (TablePrinterLayout) tableLayout, (ColumnPanel) panel);
        } else {
            if (childPrinters == null) {
                childPrinters = prepareChildPrinters(context);
            }

            for (int i = 0; i < childPrinters.length; i++) {
                Printer printer = childPrinters[i];

                if (newPrint) {
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

    private boolean isTopMost() {
        if (layer == UNKNOWN) {
            layer = TOPMOST;

            int c = getComponent().getChildCount();

            for (int i = 0; i < c; i++) {
                if (getComponent().getChild(i) instanceof ColumnPanel) {
                    layer = OVERRIDE;

                    break;
                }
            }
        }

        return layer == TOPMOST;
    }

    private boolean printCell(Context context, TablePrinterLayout tableLayout, ColumnPanel panel)
        throws Exception {
        boolean done = true;

        if (!isVisible(context.getScript())) {
            return true;
        }

        CellStore cellstore = panel.getRootTable().getCellstore();
        Cell cell = panel.getCell();

        int offrow = 0;

        if (tableLayout instanceof PowerTablePrinterLayout) {
            PowerTablePrinterLayout powerLayout = (PowerTablePrinterLayout) tableLayout;

            if (powerLayout.isCrossBodyPrint()) {
                cell = (Cell) cell.clone();
                cell.row = powerLayout.getCrossBodyRow0();
                cell.rowSpan = powerLayout.getCrossBodyRow1() - cell.row + 1;
                offrow = powerLayout.getOffRow();
            }
        }

        for (int col = cell.column; col < (cell.column + cell.colSpan); col++) {
            int lastCol = tableLayout.addColumn(tableLayout.getColumnWidth(col));

            for (int row = cell.row; row <= cell.row2(); row++) {
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
                    Cell acell = (Cell) c.getCell().clone();
                    view.setCell(acell);

                    int col0 = acell.column;
                    int col1 = acell.column2();

                    if (col0 < cell.column) {
                        col0 = cell.column;
                    }

                    if (col1 > cell.column2()) {
                        col1 = cell.column2();
                    }

                    acell.column = col0;
                    acell.colSpan = col1 - acell.column + 1;
                    acell.column = lastCol;
                    acell.row += offrow;

                    v = (AbstractView) printer.unitView(context.getScript() ,v, tableLayout, false);

                    if (v != null) {
                        tableLayout.add(v);
                    }
                }

                if (!printer.isDone(context)) {
                    done = false;
                }
            }
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
