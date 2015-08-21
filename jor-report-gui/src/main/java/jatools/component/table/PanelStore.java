package jatools.component.table;

import jatools.component.Component;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class PanelStore {
    TableBase grid;
    Component[][] comps;
    private boolean inline;
    private boolean excludeInlines;

    /**
     * Creates a new CellStore object.
     *
     * @param grid DOCUMENT ME!
     */
    public PanelStore(TableBase grid) {
        this.grid = grid;
    }

    /**
     * DOCUMENT ME!
     */
    public void excludeInlines() {
        this.excludeInlines = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param col DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Cell getCellOver(int row, int col) {
        Component c = getComponentOver(row, col);

        if (c != null) {
            return c.getCell();
        } else {
            return null;
        }
    }

    private Component[][] getComponents() {
        if (this.comps == null) {
            this.comps = new Component[this.grid.getRowCount()][this.grid.getColumnCount()];
            populateComponent(this.grid, comps);
        }

        return comps;
    }

    private void populateComponent(Component p, Component[][] to) {
        if (p instanceof GridComponent) {
            if (((GridComponent) p).isInline()) {
                if (this.excludeInlines) {
                    return;
                }

                this.inline = true;
            }

            Cell cel = p.getCell();

            if (cel != null) {
                for (int j = cel.row; j <= cel.row2(); j++) {
                    for (int k = cel.column; k <= cel.column2(); k++) {
                        to[j][k] = p;
                    }
                }
            }

            for (int i = 0; i < p.getChildCount(); i++) {
                Component c = p.getChild(i);
                populateComponent(c, to);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getComponentOver(int row, int column) {
        Component c = this.getComponents()[row][column];

        if (c != null) {
            return c;
        } else {
            return grid;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param cell DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CellStore createFilledStore(Cell cell) {
        getComponents();

        if (!inline || !isInline(cell)) {
            return this.grid.getCellstore();
        }

        Component[][] _comps = new Component[this.grid.getRowCount()][this.grid.getColumnCount()];

        for (int row = cell.row; row <= cell.row2(); row++) {
            for (int col = cell.column; col <= cell.column2(); col++) {
                _comps[row][col] = this.grid.getCellstore().getComponent(row, col);
            }
        }

        boolean filled = false;

        for (int row = cell.row; row <= cell.row2(); row++) {
            for (int col = cell.column; col <= cell.column2(); col++) {
                Component panel = this.getComponent(row, col);

                if (panel instanceof FillRowPanel) {
                    FillRowPanel fillpanel = (FillRowPanel) panel;
                    Cell fillcell = fillpanel.getCell();

                    int fillrows = 0;
                    int fillRow = fillcell.row;

                    while (((fillRow + fillcell.rowSpan) <= (cell.row + cell.rowSpan)) &&
                            ((fillpanel.getLimitRows() == 0) ||
                            (fillrows < fillpanel.getLimitRows()))) {
                        for (int _row = 0; _row < fillcell.rowSpan; _row++) {
                            for (int _col = 0; _col < fillcell.colSpan; _col++) {
                                _comps[fillRow + _row][fillcell.column + _col] = null;
                            }
                        }

                        _comps[fillRow][fillcell.column] = fillpanel;
                        fillpanel.setLastFilledRow(fillRow);
                        fillRow += fillcell.rowSpan;
                        fillrows++;
                    }

                    filled = true;
                }
            }
        }

        return filled ? new CellStore(this.grid, _comps) : this.grid.getCellstore();
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param col DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getComponent(int row, int col) {
        Component c = this.getComponents()[row][col];

        if ((c != null) && (c.getCell().row == row) && (c.getCell().column == col)) {
            return c;
        } else {
            return null;
        }
    }

    private boolean isInline(Cell cell) {
        return this.inline;
    }
}
