package jatools.engine.layout;

import jatools.component.table.PowerTableBase;
import jatools.component.table.RowPanel;
import jatools.core.view.CompoundView;
import jatools.engine.CellFinder;
import jatools.engine.Printer;
import jatools.engine.css.rule.CrossTabRule;
import jatools.engine.printer.PowerTablePrinter;
import jatools.engine.script.Context;

import java.awt.Rectangle;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PowerTablePrinterLayout extends TablePrinterLayout {
    private boolean columnLocked;
    private int lastRow;
    private int lastCol;
    private int crossBodyRow0 = -1;
    private int crossBodyRow1 = -1;
    private int offRow;
    private boolean layoutlocked;

    /**
     * Creates a new PowerTablePrinterLayout object.
     *
     * @param imageable DOCUMENT ME!
     * @param rootView DOCUMENT ME!
     * @param cellFinder DOCUMENT ME!
     * @param rightFlow DOCUMENT ME!
     */
    public PowerTablePrinterLayout(Printer printer, Rectangle imageable, CompoundView rootView,
        CellFinder cellFinder, boolean rightFlow, TableView lastView) {
        super(printer, imageable, rootView, cellFinder, rightFlow);
        this.lastView = lastView;
    }

    /**
     * DOCUMENT ME!
     */
    public void doLayout() {
        if (!this.layoutlocked) {
            super.doLayout();
        }
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public boolean isCrossBodyPrint() {
        return crossBodyRow0 > -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param row0 DOCUMENT ME!
     * @param row1 DOCUMENT ME!
     */
    public void setCrossBody(int row0, int row1) {
        this.crossBodyRow0 = row0;
        this.crossBodyRow1 = row1;
    }

    /**
     * DOCUMENT ME!
     */
    public void clearCrossBody() {
        this.crossBodyRow0 = this.crossBodyRow1 = -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param col DOCUMENT ME!
     */
    public void locate(int row, int col) {
        this.lastRow = row;
        this.lastCol = col;
    }

    /**
     * DOCUMENT ME!
     */
    public void lockColumn() {
        this.columnLocked = true;
    }

    /**
     * DOCUMENT ME!
     */
    public void lockLayout() {
        this.layoutlocked = true;
    }

    /**
     * DOCUMENT ME!
     */
    public void unlockLayout() {
        this.layoutlocked = false;
    }

    /**
     * DOCUMENT ME!
     */
    public void unlockColumn() {
        this.columnLocked = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param w DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int addColumn(int w) {
        if (!columnLocked) {
            return super.addColumn(w);
        } else {
            this.lastCol++;

            return lastCol;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param panel DOCUMENT ME!
     * @param context DOCUMENT ME!
     * @param layout DOCUMENT ME!
     * @throws Exception
     */
    public void printPerformed(RowPanel panel, Context context, PowerTablePrinterLayout layout)
        throws Exception {
        TableView grid = (TableView) this.rootView;
        offRow = (grid.getRows().length() - panel.getCell().rowSpan) - panel.getCell().row;
        ((PowerTablePrinter) getPrinter()).printCellsLeadBy(panel, context, layout);
        offRow = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getCrossBodyRow0() {
        return crossBodyRow0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getCrossBodyRow1() {
        return crossBodyRow1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getOffRow() {
        return offRow;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLeftHeaderColumns(CrossTabRule rule) {
        return ((PowerTableBase) this.getPrinter().getComponent()).getLeftHeaderColumns();
    }

    /**
     * DOCUMENT ME!
     * @param rule
     */
}
