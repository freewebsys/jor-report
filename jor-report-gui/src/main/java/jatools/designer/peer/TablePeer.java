/*
 * Author: John.
 *
 * 杭州杰创软件 All Copyrights Reserved.
 */
package jatools.designer.peer;

import jatools.component.Component;

import jatools.component.table.Cell;
import jatools.component.table.CellStore;
import jatools.component.table.TableBase;

import jatools.designer.ReportPanel;

import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;

import java.util.Iterator;

import javax.swing.SwingUtilities;


/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.4 $
 * @author $author$
 */
public class TablePeer extends ComponentPeer {
    /**
     * DOCUMENT ME!
     */
    public static final int NULL = 0;

    /**
     * DOCUMENT ME!
     */
    public static final int _SOUTH_EAST = 1;

    /**
     * DOCUMENT ME!
     */
    public static final int _SOUTH = 2;

    /**
     * DOCUMENT ME!
     */
    public static final int _EAST = 3;
    public static final int _EDAGE = 4;
    private static Rectangle testRect = new Rectangle();
    private boolean focused;
    private Rectangle selection;
    private Rectangle selectionLocation;
    private boolean gridVisible = true;

    /**
     * Creates a new TablePeer object.
     *
     * @param owner
     *            DOCUMENT ME!
     * @param target
     *            DOCUMENT ME!
     */
    public TablePeer(ReportPanel owner, Component target) {
        super(owner, target);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Rectangle getSelectionLocation() {
        if (this.selectionLocation == null) {
            TableBase grid = (TableBase) getComponent();
            Rectangle bounds = getSelection();

            selectionLocation = grid.getBounds(bounds.y, bounds.x, bounds.width, bounds.height);
        }

        return selectionLocation;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b
     *            DOCUMENT ME!
     */
    public void setFocused(boolean b) {
        this.focused = b;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isFocused() {
        return focused;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.jatools.designer.ZComponentPeer#isAcceptableChild(com.jatools.component
     * .ZComponent)
     */

    /**
     * DOCUMENT ME!
     *
     * @param child
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isAcceptableChild(Component child) {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param child
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isAcceptableDropedChild(Component child) {
        return (this.getComponent().getCell() == null) && !(child instanceof TableBase);
    }

    /**
     * 检测mouse在选择框的位置,在东面中间位置的,在南面中间位置,
     *
     * @param x
     *            DOCUMENT ME!
     * @param y
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int hitSelection(int x, int y) {
        Rectangle sel = getSelectionLocation();

        // 1
        int x0 = sel.x + (sel.width / 2);
        int y0 = sel.y + sel.height;

        testRect.setRect(x0 - 3, y0 - 3, 6, 6);

        if (testRect.contains(x, y)) {
            return _SOUTH;
        }

        // 2
        x0 = sel.x + sel.width;
        y0 = sel.y + (sel.height / 2);
        testRect.setRect(x0 - 3, y0 - 3, 6, 6);

        if (testRect.contains(x, y)) {
            return _EAST;
        }

        // 3
        x0 = sel.x + sel.width;
        y0 = sel.y + sel.height;
        testRect.setRect(x0 - 3, y0 - 3, 7, 7);

        if (testRect.contains(x, y)) {
            return _SOUTH_EAST;
        }

        // 检查是否在边框上，
        int result = NULL;
        sel.grow(2, 2);

        if (sel.contains(x, y)) {
            sel.grow(-4, -4);

            if (!sel.contains(x, y)) {
                result = _EDAGE;
            }

            sel.grow(4, 4);
        }

        sel.grow(-2, -2);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isResizable() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param selection
     *            DOCUMENT ME!
     */
    public void setSelection(Rectangle selection) {
        /**
         * @param selection
         */
        setSelection(selection, true);
    }

    /**
     * @param selection
     */
    public void setSelection(Rectangle selection, boolean invokelater) {
        /**
         * @param selection
         */
        _setSelection(selection);

        if (invokelater) {
            SwingUtilities.invokeLater(new _SelectionNotifier());
        } else {
            selectChildren();
        }
    }

    /**
    *
    */
    public void refreshSelection() {
        setSelection(getSelection());
    }

    /**
    *
    */
    private void selectChildren() {
        // if (!((ZTable) this.getTarget()).isFocused()) {
        // return;
        // }
        this.getOwner().unselectAll();

        Rectangle selection = getSelection();
        Iterator it = ((TableBase) getComponent()).getCellstore().iterator();

        while (it.hasNext()) {
            Component c = (Component) it.next();

            Cell cell = (Cell) c.getCell();

            if (selection.contains(cell.column, cell.row)) {
                this.getOwner().select(c);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param selection
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    /**
     * DOCUMENT ME!
     *
     * @param loc
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int hitHot(Point loc) {
        return ComponentPeer.NOT_HIT;
    }

    /**
     * DOCUMENT ME!
     *
     * @param focusedBoxes
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getFocusedBoxes(Point[] focusedBoxes) {
        // if (getComponent().getParent() instanceof GridBase) {
        // return 0;
        // }
        Point pointCache = focusedBoxes[0];
        Insets is = this.getComponent().getPadding();
        pointCache.setLocation(-is.left, -is.top);
        getOwner().childPointAsScreenPoint(this, pointCache);

        int x1 = pointCache.x + 2;
        int y1 = pointCache.y + 2;
        int x2 = (x1 + getWidth()) - 5;
        int y2 = (y1 + getHeight()) - 5;

        /*
         * p1(x1,y1) p2 p3 +-----------+----------+ | | p8+ + cx,cy + p4 | |
         * +-----------+----------+ p5(x2,y2) p7 p6
         */
        focusedBoxes[0].setLocation(x1, y1); // p1

        focusedBoxes[1].setLocation(x2, y1); // p3

        focusedBoxes[2].setLocation(x2, y2); // p5

        focusedBoxes[3].setLocation(x1, y2); // p7

        return 4;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */

    /**
     * DOCUMENT ME!
     *
     * @param selection
     *            DOCUMENT ME!
     */
    public void _setSelection(Rectangle selection) {
        this.selection = growAsNeeded(selection);
        this.selectionLocation = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param sel DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isMergedCellCut(Rectangle sel) {
        return !sel.clone().equals(growAsNeeded(sel));
    }

    private Rectangle growAsNeeded(Rectangle selection) {
        Iterator children = ((TableBase) getComponent()).getCellstore().iterator();

        while (children.hasNext()) {
            Cell cell = (Cell) ((Component) children.next()).getCell();
            Rectangle r = new Rectangle(cell.column, cell.row, cell.colSpan, cell.rowSpan);

            if (selection.intersects(r)) {
                selection.add(r);
            }
        }

        return selection;
    }

    private boolean hasIntersectMergedCells(Cell srcCell, Cell toCell) {
        CellStore store = ((TableBase) getComponent()).getCellstore();

        for (int row = toCell.row; row <= toCell.row2(); row++) {
            for (int col = toCell.column; col <= toCell.column2(); col++) {
                Component c = store.getComponentOver(row, col);

                // 如果有一个组件，在目标区域外面，且这个组件不在源区域内，则认为不可移动
                if ((c == null) ||
                        (!toCell.contains(c.getCell()) && !srcCell.contains(c.getCell()))) {
                    // 如果
                    c = store.getComponent(row, col);

                    if ((c == null) || c.getCell().contains(toCell)) {
                        continue;
                    } else {

                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param srcCell DOCUMENT ME!
     * @param toRow DOCUMENT ME!
     * @param toCol DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canMove(Cell srcCell, int toRow, int toCol) {
        Cell toCell = new Cell(toRow, toCol, srcCell.colSpan, srcCell.rowSpan);

        return !hasIntersectMergedCells(srcCell, toCell);
    }

    /**
     * DOCUMENT ME!
     *
     * @param srcCell DOCUMENT ME!
     * @param w DOCUMENT ME!
     * @param h DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canGrow(Cell srcCell, int w, int h) {
        Cell toCell = new Cell(srcCell.row, srcCell.column, w, h);

        if (srcCell.contains(toCell)) {
            return true;
        }

        CellStore store = ((TableBase) getComponent()).getCellstore();

        Component leftTop = store.getComponent(srcCell.row, srcCell.column);

        for (int row = toCell.row; row <= toCell.row2(); row++) {
            for (int col = toCell.column; col <= toCell.column2(); col++) {
                Component c = store.getComponentOver(row, col);

                // 如果有一个组件，在目标区域外面，且这个组件不在源区域内，则认为不可移动
                if ((c != null) && (c != leftTop) && (c != null) && !toCell.contains(c.getCell())) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isFixedHeight() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Rectangle getSelection() {
        return selection;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Cell getFocusedCell() {
        return new Cell(selection.y, selection.x);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isGridVisible() {
        return gridVisible;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isHandlerVisible() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param gridVisible
     *            DOCUMENT ME!
     */
    public void setGridVisible(boolean gridVisible) {
        this.gridVisible = gridVisible;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Cell getNearestCellInSelection(int x, int y) {
        Rectangle sel = getSelectionLocation();

        if (x < (sel.x + 3)) {
            x = sel.x + 1;
        } else if (x > ((sel.x + sel.width) - 3)) {
            x = (sel.x + sel.width) - 1;
        }

        if (y < (sel.y + 3)) {
            y = sel.y + 1;
        } else if (y > ((sel.y + sel.height) - 3)) {
            y = (sel.y + sel.height) - 1;
        }

        TableBase grid = (TableBase) getComponent();

        return grid.getGrowStandardCellAt(x, y);
    }

    class _SelectionNotifier implements Runnable {
        public void run() {
            selectChildren();
        }
    }
}
