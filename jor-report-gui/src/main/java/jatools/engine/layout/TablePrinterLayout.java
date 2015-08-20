package jatools.engine.layout;

import jatools.component.Component;
import jatools.component.Size;

import jatools.component.table.Cell;
import jatools.component.table.TableBase;

import jatools.core.view.CompoundView;
import jatools.core.view.View;

import jatools.engine.CellFinder;
import jatools.engine.Printer;

import jatools.engine.css.rule.CrossTabRule;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class TablePrinterLayout extends AbstractPrinterLayout {
    /**
     * DOCUMENT ME!
     */
    public final static int FAIL = -1;
    private CellFinder cellFinder;
    private UnitedLevelManager unitedLevelManager;
    private boolean rightFlow;
    private int footerSpace;
    private InsertableGroup currentInsertableGroup;
    protected TableView lastView;

    /**
     * Creates a new TableFiller object.
     *
     * @param remainder DOCUMENT ME!
     * @param imageable DOCUMENT ME!
     * @param rootView DOCUMENT ME!
     * @param cellFinder DOCUMENT ME!
     */
    public TablePrinterLayout(Printer printer, Rectangle imageable, CompoundView rootView,
        CellFinder cellFinder, boolean rightFlow) {
        super(printer, imageable, rootView);
        this.cellFinder = cellFinder;
        this.rightFlow = rightFlow;
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     */
    public void add(View view) {
        if (view != null) {
            this.rootView.add(view);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void openRowGroup(int rowGroupID) {
        this.currentInsertableGroup = new InsertableGroup(rowGroupID);

        TableView grid = (TableView) this.rootView;
        grid.setInsertable(true);
    }

    /**
     * DOCUMENT ME!
     */
    public void closeRowGroup() {
        this.currentInsertableGroup = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param h DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int addRow(int h) {
        return addRow(h, false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param h DOCUMENT ME!
     * @param forced DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int addRow(int h, boolean forced) {
        TableView grid = (TableView) this.rootView;

        if (!forced &&
                ((grid.getRows().getPosition(grid.getRows().length()) + h) > this.imageable.height)) {
            return FAIL;
        } else {
            int[] old = grid.getRows().getSizes();
            int[] s = new int[old.length + 1];
            System.arraycopy(old, 0, s, 0, old.length);
            s[old.length] = h;
            grid.getRows().setSizes(s);

            Object group = null;

            if (this.currentInsertableGroup != null) {
                if (!this.currentInsertableGroup.added) {
                    group = this.currentInsertableGroup;
                    this.currentInsertableGroup.added = true;
                }

                this.currentInsertableGroup.rowSpan++;
            }

            grid.addRowID(group);

            return s.length - 1;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumnWidth(int c) {
        return cellFinder.getColumnWidth(c);
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getTableRowHeight(int r) {
        return cellFinder.getRowHeight(r);
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getViewRowHeight(int r) {
        return ((TableView) this.rootView).getRows().getSize(r);
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     * @param h DOCUMENT ME!
     */
    public void setViewRowHeight(int r, int h) {
        ((TableView) this.rootView).getRows().setSize(r, h);
    }

    /**
     * DOCUMENT ME!
     *
     * @param w DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int addColumn(int w) {
        TableView grid = (TableView) this.rootView;
        int[] old = grid.getColumns().getSizes();
        int[] s = new int[old.length + 1];
        System.arraycopy(old, 0, s, 0, old.length);
        s[old.length] = w;
        grid.getColumns().setSizes(s);

        return s.length - 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public UnitedLevelManager getUnitedLevelManager() {
        if (unitedLevelManager == null) {
            unitedLevelManager = new UnitedLevelManager();
        }

        return unitedLevelManager;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isRightFlow() {
        return rightFlow;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rightFlow DOCUMENT ME!
     */
    public void setRightFlow(boolean rightFlow) {
        this.rightFlow = rightFlow;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMaxBottom(Component c) {
        TableView grid = (TableView) this.rootView;

        return this.getImageable().height /*- c.getY() */ -
        grid.getRows().getPosition(grid.getRows().length() - 1);
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
        int right = c.getPadding().left + getImageable().width + c.getPadding().right;

        return right >= (c.getWidth() + off);
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param off DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean containsY(Component c, int off) {
        Cell cell = c.getCell();

        if (cell != null) {
            TableView grid = (TableView) this.rootView;
            TableBase table = (TableBase) getPrinter().getComponent();
            int h = table.getHeight(cell.row, cell.rowSpan);

            return (grid.getRows().getPosition(grid.getRows().length()) + h) <= getImageable().height;
        } else {
            int h = c.getPadding().top + getImageable().height + c.getPadding().bottom;

            return h >= (c.getHeight() + off);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void doLayout() {
        TableView grid = (TableView) this.rootView;
        Size rows = grid.getRows();

        rootView.doLayout();
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(Component c) {
        return true;
    }

    //    /**
    /**
     * DOCUMENT ME!
     *
     * @param component DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMaxRight(Component c) {
        return this.getImageable().width - c.getX();
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void reserveFooterSpace(Component c) {
        Cell cell = c.getCell();
        TableBase table = (TableBase) getPrinter().getComponent();
        int h = table.getHeight(cell.row, cell.rowSpan);
        footerSpace += h;

        this.getImageable().height -= h;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isFooterSupported() {
        return true;
    }

    /**
     * DOCUMENT ME!
     */
    public void restoreFooterSpace() {
        this.getImageable().height += footerSpace;
        footerSpace = 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rule DOCUMENT ME!
     * @return
     */
    public TableView calcLastView(CrossTabRule rule, TableView root) {
        TableView view = lastView;

        if (lastView == null) {
            view = root;
        }

        Rectangle im = getImageable();

        if (!view.isLeftOf(im.x + im.width) && rule.isTrue(rule.pageWrap, true)) {
            lastView = view;

            TableViewSplitter splitter = new TableViewSplitter(view, rule);

            view = splitter.selectColumn(im, getLeftHeaderColumns(rule));
        } else {
            lastView = null;
        }

        this.rootView = view;

        return lastView;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLeftHeaderColumns(CrossTabRule rule) {
        return rule.headerColumns;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TableView getLastView() {
        return lastView;
    }
}
