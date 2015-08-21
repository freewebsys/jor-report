package jatools.engine.printer;

import jatools.component.Component;

import jatools.component.table.Cell;
import jatools.component.table.CellStore;
import jatools.component.table.FillRowPanel;
import jatools.component.table.TableBase;

import jatools.core.view.AbstractView;
import jatools.core.view.TablePartView;
import jatools.core.view.View;

import jatools.data.reader.Cursor;

import jatools.engine.Printer;

import jatools.engine.script.Context;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class FillRowPanelPrinter extends AbstractPrinter {
    private Cursor cursor;
    private Cell willPrintedArea;

    // private int last

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

        FillRowPanel panel = (FillRowPanel) getComponent();

        if (cursor == null) {
            cursor = createCursor(context, panel, 0);
        }

        if (willPrintedArea == null) {
            willPrintedArea = (Cell) panel.getCell().clone();
        }

        TablePartView result = new TablePartView();

        next(context, result, panel);

        if ((this.willPrintedArea.row == panel.getLastFilledRow()) && panel.isPrintContinued()) {
            done = !cursor.hasNext();
            this.willPrintedArea = null;
        } else {
            done = true;

            this.willPrintedArea.row += this.willPrintedArea.getRowSpan();
        }

        this.doAfterPrint(context.getScript());

        return result;
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
    private boolean next(Context context, TablePartView view, FillRowPanel panel)
        throws Exception {
        if (!cursor.hasNext()) {
            printChildren(context, view, panel.getRootTable(), this.willPrintedArea);

            return true;
        } else {
            this.doBeforePrint(context.getScript());

            boolean result = false;

            cursor.save();
            cursor.next();
            cursor.open();
            printChildren(context, view, panel.getRootTable(), panel.getCell());

            cursor.close();

            result = result && cursor.hasNext();

            if (result) {
                this.doAfterPrint(context.getScript());
            }

            return result;
        }
    }

    private boolean printChildren(Context context, TablePartView result, TableBase table, Cell cell)
        throws Exception {
        boolean done = true;

        if (!isVisible(context.getScript())) {
            return true;
        }

        CellStore cellstore = table.getCellstore();

        for (int row = cell.row; row < (cell.row + cell.rowSpan); row++) {
            for (int col = cell.column; col < (cell.column + cell.colSpan); col++) {
                Component c = cellstore.getComponent(row, col);

                if (c == null) {
                    continue;
                }

                Printer printer = context.getPrinter(c);
                AbstractView v = (AbstractView) printer.print(context);

                if (v != null) {
                    ((AbstractView) v).setCell((Cell) c.getCell().clone());
                    result.add(v);
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

        willPrintedArea = null;
    }
}
