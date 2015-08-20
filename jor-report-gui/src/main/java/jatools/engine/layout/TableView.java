package jatools.engine.layout;

import jatools.component.Size;

import jatools.component.table.Cell;

import jatools.core.view.AbstractView;
import jatools.core.view.CompoundView;
import jatools.core.view.DisplayStyle;
import jatools.core.view.View;

import java.awt.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class TableView extends CompoundView {
    static final long serialVersionUID = 2007071601220L;
    private Size rows;
    private Size columns;
    private ArrayList rowIDS = new ArrayList();
    private CellStore cellstore;
    private boolean insertable;
    private boolean useCell2;
    private DisplayStyle[] styles;

    /**
     * Creates a new TableView object.
     *
     * @param rows DOCUMENT ME!
     * @param columns DOCUMENT ME!
     */
    public TableView(int[] rows, int[] columns, String name) {
        this.rows = new Size(rows);
        this.columns = new Size(columns);
        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isLeftOf(int y) {
        return y > (this.bounds.x + columns.getPosition(columns.length()));
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void add(View e) {
        super.add(e);

        AbstractView view = (AbstractView) e;

        if (view.isAutoSize()) {
            Cell cel = view.getCell();
            int h = this.getRows().getSize(cel.row, cel.rowSpan);
            int w = this.getColumns().getSize(cel.column, cel.colSpan);
            int ph = view.getPreferedHeight(w);

            if (ph > h) {
                int prefered = (this.getRows().getSize(cel.row) + ph) - h;
                this.getRows().setSize(cel.row, prefered);
            }

            int pw = view.getPreferedWidth(h);

            if (pw > w) {
                int prefered = (this.getColumns().getSize(cel.column) + pw) - w;
                this.getColumns().setSize(cel.column, prefered);
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void doLayout() {
        this.doLayout(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param force DOCUMENT ME!
     */
    public void doLayout(boolean force) {
        if (force || this.layoutdirty) {
            Iterator it = this.elementCache.iterator();

            while (it.hasNext()) {
                AbstractView view = (AbstractView) it.next();
                Cell cell = view.getCell();
                int x = columns.getPosition(cell.column);

                int y = rows.getPosition(cell.row);
                int w = columns.getSize(cell.column, cell.colSpan);
                int h = rows.getSize(cell.row, cell.rowSpan);

                view.setBounds(new Rectangle(x, y, w, h));
            }

            int w = columns.getSize(0, columns.length());
            int h = rows.getSize(0, rows.length());
            getBounds().width = w;
            getBounds().height = h;

            this.layoutdirty = false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Size getColumns() {
        return columns;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isLayoutChildrenRequired() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param col DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractView getViewOver(int row, int col) {
        return getCellstore().getView(row, col);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param col DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractView getView(int row, int col) {
        AbstractView v = getCellstore().getView(row, col);

        if ((v != null) && (v.getCell().column == col) && (v.getCell().row == row)) {
            return v;
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Size getRows() {
        return rows;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CellStore getCellstore() {
        if (cellstore == null) {
            cellstore = new CellStore();
        }

        return cellstore;
    }

    /**
     * DOCUMENT ME!
     *
     * @param id DOCUMENT ME!
     */
    public void addRowID(Object id) {
        this.rowIDS.add(id);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getRowID(int i) {
        if (i < this.rowIDS.size()) {
            return this.rowIDS.get(i);
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isInsertable() {
        return insertable;
    }

    /**
     * DOCUMENT ME!
     *
     * @param insertable DOCUMENT ME!
     */
    public void setInsertable(boolean insertable) {
        this.insertable = insertable;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRowCount() {
        return rows.length();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumnCount() {
        return columns.length();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DisplayStyle[] getStyles() {
        // TODO Auto-generated method stub
        return this.styles;
    }

    /**
     * DOCUMENT ME!
     *
     * @param styles DOCUMENT ME!
     */
    public void setStyles(DisplayStyle[] styles) {
        this.styles = styles;
    }

    class CellStore {
        AbstractView[][] views;

        CellStore() {
            init();
        }

        AbstractView getView(int row, int col) {
            return views[row][col];
        }

        private void init() {
            views = new AbstractView[rows.length()][columns.length()];

            Iterator it = elementCache.iterator();

            while (it.hasNext()) {
                AbstractView view = (AbstractView) it.next();
                Cell cel = view.getCell();

                if (cel != null) {
                    for (int j = cel.row; j <= cel.row2(); j++) {
                        for (int k = cel.column; k <= cel.column2(); k++) {
                            views[j][k] = view;
                        }
                    }
                }
            }
        }
    }
}
