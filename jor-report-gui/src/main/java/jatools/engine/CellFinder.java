package jatools.engine;

import jatools.component.Component;
import jatools.component.table.Cell;
import jatools.component.table.GridComponent;
import jatools.component.table.TableBase;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class CellFinder {
    int[] rows;
    int[] columns;
    Component[][] children;

    private CellFinder(Component[][] children, int[] rows, int[] columns) {
        this.children = children;
        this.rows = rows;
        this.columns = columns;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRowHeight(int r) {
        return rows[r];
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumnWidth(int c) {
        return columns[c];
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param col DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component find(int row, int col) {
        return this.children[row][col];
    }

    /**
     * DOCUMENT ME!
     *
     * @param t DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static CellFinder create(TableBase t) {
        Component[][] children = new Component[t.getRowCount()][t.getColumnCount()];

        int c = t.getChildCount();

        for (int i = 0; i < c; i++) {
            Component child = t.getChild(i);
            Cell cell = child.getCell();

            if ((cell != null) && (!(child instanceof GridComponent))) {
            	if(cell.row >= children.length || cell.column >= children[cell.row].length)
            		System.out.println();
                children[cell.row][cell.column] = child;
            }
        }

        return new CellFinder(children, t.getRowHeights(), t.getColumnWidths());
    }
}
