package jatools.component.table;

import jatools.component.Component;

import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class CellStore {
    TableBase grid;
    Component[][] comps;

    /**
     * Creates a new CellStore object.
     *
     * @param grid DOCUMENT ME!
     */
    public CellStore(TableBase grid) {
        this.grid = grid;
    }

    /**
     * Creates a new CellStore object.
     *
     * @param grid DOCUMENT ME!
     */
    public CellStore(TableBase grid, Component[][] comps) {
        this.grid = grid;
        this.comps = comps;
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

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param col DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component clear(int row, int col) {
        Component c = this.getComponents()[row][col];
        getComponents()[row][col] = null;

        if ((c != null) && (c.getCell().row == row) && (c.getCell().column == col)) {
            return c;
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator iterator() {
        return new Children();
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

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param col DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Cell getCell(int row, int col) {
        Component c = getComponent(row, col);

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
            for (int i = 0; i < p.getChildCount(); i++) {
                Component c = p.getChild(i);

                if (c instanceof GridComponent) {
                    this.populateComponent(c, to);
                } else {
                    Cell cel = c.getCell();

                    if (cel != null) {
                        for (int j = cel.row; j <= cel.row2(); j++) {
                            for (int k = cel.column; k <= cel.column2(); k++) {
                                if ((j >= to.length) || (k >= to[j].length)) {
                                    System.out.println();
                                }

                                to[j][k] = c;
                            }
                        }
                    }
                }
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
        return this.getComponents()[row][column];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        for (int row = 0; row < grid.getRowCount(); row++) {
            for (int col = 0; col < grid.getColumnCount(); col++) {
                Component c = getComponent(row, col);

                if (c != null) {
                    System.out.print("row=" + row + "col=" + col + "[" + c.getCell().toString() +
                        "]");
                } else {
                    System.out.print("row=" + row + "col=" + col + "                    ");
                }
            }

            //            System.out.println();
        }

        return null;
    }

    class Children implements Iterator {
        int row;
        int col;
        Component _next;

        public void remove() {
        }

        public boolean hasNext() {
            for (; row < grid.getRowCount(); row++) {
                for (; col < grid.getColumnCount(); col++) {
                    Component c = getComponent(row, col);

                    if (c != null) {
                        col++;

                        if (col >= grid.getColumnCount()) {
                            col = 0;
                            row++;
                        }

                        _next = c;

                        return true;
                    }
                }

                col = 0;
            }

            return false;
        }

        public Object next() {
            return _next;
        }
    }
}
