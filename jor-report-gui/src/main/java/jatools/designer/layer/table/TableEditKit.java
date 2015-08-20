package jatools.designer.layer.table;

import jatools.component.Component;
import jatools.component.ComponentConstants;
import jatools.component.Label;

import jatools.component.table.Cell;
import jatools.component.table.CellStore;
import jatools.component.table.FillRowPanel;
import jatools.component.table.GridComponent;
import jatools.component.table.PanelStore;
import jatools.component.table.Table;
import jatools.component.table.TableBase;

import jatools.designer.App;
import jatools.designer.ClipBoard;
import jatools.designer.InplaceEditor;
import jatools.designer.Main;
import jatools.designer.ReportPanel;

import jatools.designer.action.NewTable;
import jatools.designer.action.ReportAction;

import jatools.designer.componenteditor.ComponentEditor;
import jatools.designer.componenteditor.ComponentEditorFactory;

import jatools.designer.layer.LayerContainer;
import jatools.designer.layer.painter.SelectionFramePainter;
import jatools.designer.layer.utils.MoveWorker;

import jatools.designer.peer.ComponentPeer;
import jatools.designer.peer.ComponentPeerFactory;
import jatools.designer.peer.TablePeer;

import jatools.designer.undo.AddEdit;
import jatools.designer.undo.GroupEdit;
import jatools.designer.undo.PropertyEdit;
import jatools.designer.undo.TablePropertyEdit;

import jatools.swingx.MessageBox;
import jatools.swingx.SpinEditor;

import jatools.util.CursorUtil;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import java.beans.PropertyChangeEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.undo.CompoundEdit;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class TableEditKit implements ActionListener, KeyEventDispatcher {
    private static int DEFAULT_ROW_HEIGHT = 20;
    private static int DEFAULT_COLUMN_WIDTH = 60;
    static int INSIDE = 0;
    static int EDGE = 1;
    static int OUTSIDE = 2;
    private final static String BATCH = "BATCH";
    final protected static int STATE_IDLE = 0;
    private final static int STATE_SELECTING = 1;
    private final static int STATE_RESIZING = 2;
    private final static int STATE_CELL_MOVING = 3;
    private final static int STATE_CELL_MERGING = 4;
    private final static int TABLE_DRAG = 203;
    final static int SUBSTATE_IDLE = 0;
    private final static int SUBSTATE_ROW_RESIZE_READY = 1;
    private final static int SUBSTATE_COLUMN_RESIZE_READY = 2;
    private final static int SUBSTATE_CELL_MERGE_READY = 3;
    private final static int SUBSTATE_CELL_MOVE_READY = 4;
    private final static int SUBSTATE_PRE_TABLE_DRAG = 5;
    private final static int SUBSTATE_DRAG_READY = 6;
    private final static int SUBSTATE_ROW_RESIZING = 7;
    private final static int SUBSTATE_COLUMN_RESIZING = 8;
    private final static int SUBSTATE_CELL_SELECTED = 8;
    private final static int VERTICAL = 1;
    private final static int HORIZONTAL = 0;
    final protected static String INSERT_ROW_BEFORE = "insert.row.before";
    final protected static String INSERT_ROW_AFTER = "insert.row.after";
    private final static String INSERT_COL_AFTER = "insert.col.after";
    private final static String INSERT_COL_BEFORE = "insert.col.before";
    private final static String INSERT_ROW_BEFORE_N = "insert.row.before.n";
    private final static String INSERT_ROW_AFTER_N = "insert.row.after.n";
    private final static String INSERT_COL_AFTER_N = "insert.col.after.n";
    private final static String INSERT_COL_BEFORE_N = "insert.col.before.n";
    final protected static String REMOVE_ROW = "remove.row";
    private final static String REMOVE_COL = "remove.col";
    private final static String UNITE_CELL = "unite.cell";
    private final static String SHOW_GRID = "rim.property";
    private final static String UN_UNITE_CELL = "table.cancel";
    final protected static String RESIZE_ROW_HEIGHTS = "resize.row.heights";
    private final static String RESIZE_COL_WIDTHS = "remove.col.widths";
    private Cursor cursor = CursorUtil.CELL_SELECT_CURSOR;
    _State info = new _State();
    private RubberLine vertResizeBar;
    private RubberLine horzResizeBar;
    private RubberRect cellsRubber;
    private _ColumnSizer colResizer = new _ColumnSizer();
    private _RowSizer rowResizer = new _RowSizer();
    private _CellMerger cellMerger = new _CellMerger();
    private _CellMover cellMover = new _CellMover();
    public TablePeer tablePeer;
    private Point off = new Point();
    private InplaceEditor editor = new InplaceEditor();
    private jatools.designer.layer.utils.MoveWorker worker;
    private JPopupMenu ppm;
    private MenuProvider menuProvider;

    /**
     *
     */
    public TableEditKit() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                            .addKeyEventDispatcher(this);
    }

    void setMenuProvider(MenuProvider menuProvider) {
        this.menuProvider = menuProvider;
    }

    JPopupMenu getPopup() {
        if (ppm == null) {
            ppm = createPopupMenu();
        }

        return ppm;
    }

    void setTablePeer(TablePeer gridPeer, int offx, int offy) {
        if (tablePeer != null) {
            gridPeer.setFocused(false);
        }

        this.tablePeer = gridPeer;
        info.mainState = STATE_IDLE;
        info.subState = SUBSTATE_IDLE;
        off.setLocation(offx, offy);

        if (gridPeer != null) {
            gridPeer.setFocused(true);
        }

        if (ppm != null) {
            ppm.setVisible(false);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier
     *            DOCUMENT ME!
     * @param x
     *            DOCUMENT ME!
     * @param y
     *            DOCUMENT ME!
     * @param deltaX
     *            DOCUMENT ME!
     * @param deltaY
     *            DOCUMENT ME!
     */
    public void mouseDragged(int modifier, int x, int y, int deltaX, int deltaY) {
        boolean redraw = true;

        if (info.mainState == STATE_RESIZING) {
            doResize(x, y);
        } else if (info.mainState == STATE_SELECTING) {
            doSelect(x, y);
        } else if (info.mainState == STATE_CELL_MOVING) {
            doCellMove(x, y);
        } else if (info.mainState == STATE_CELL_MERGING) {
            doCellMerge(x, y);
        } else if (info.mainState == STATE_IDLE) {
            if ((info.subState == SUBSTATE_ROW_RESIZE_READY) ||
                    (info.subState == SUBSTATE_COLUMN_RESIZE_READY)) {
                startResize(x, y);
            } else if (info.subState == SUBSTATE_CELL_MOVE_READY) {
                startCellMove(x, y);
            } else if (info.subState == SUBSTATE_CELL_MERGE_READY) {
                startCellMerge(x, y);
            } else if (info.subState != SUBSTATE_DRAG_READY) {
                startSelect(x, y);
            }
        } else if (info.mainState == TABLE_DRAG) {
            worker.move(deltaX, deltaY);
            redraw = false;
        }

        info.lastMousePosition.setLocation(x, y);

        if (redraw) {
            tablePeer.getOwner().repaint();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier
     *            DOCUMENT ME!
     * @param x
     *            DOCUMENT ME!
     * @param y
     *            DOCUMENT ME!
     * @param deltaX
     *            DOCUMENT ME!
     * @param deltaY
     *            DOCUMENT ME!
     */
    public void mouseMoved(int modifier, int x, int y, int deltaX, int deltaY) {
        try {
            if (info.mainState == STATE_IDLE) {
                Object ho = hitResizer(x, y);

                if (ho instanceof _RowSizer) {
                    cursor = CursorUtil.RESIZE_ROW_CURSOR;

                    _RowSizer row = (_RowSizer) ho;
                    info.minPoint.y = row.top;
                    info.startRow = row.index;
                    info.subState = SUBSTATE_ROW_RESIZE_READY;
                } else if (ho instanceof _ColumnSizer) {
                    cursor = CursorUtil.RESIZE_COL_CURSOR;

                    _ColumnSizer column = (_ColumnSizer) ho;
                    info.minPoint.x = column.left;

                    info.startCol = column.index;
                    info.subState = SUBSTATE_COLUMN_RESIZE_READY;
                } else if (ho instanceof _CellMerger) {
                    cursor = CursorUtil.CELL_MERGE_CURSOR;

                    _CellMerger merger = (_CellMerger) ho;
                    // info.minPoint.x = column.left;

                    // info.startCol = column.index;
                    info.subState = SUBSTATE_CELL_MERGE_READY;
                } else if (ho instanceof _CellMover) {
                    cursor = CursorUtil.DEFAULT_CURSOR;

                    // _CellMerger merger = (_CellMerger) ho;
                    // info.minPoint.x = column.left;

                    // info.startCol = column.index;
                    info.subState = SUBSTATE_CELL_MOVE_READY;
                } else {
                    int area = hitArea(x, y);

                    if (area == EDGE) {
                        cursor = CursorUtil.MOVE_CURSOR;
                        info.subState = SUBSTATE_PRE_TABLE_DRAG;
                    } else if (area == INSIDE) {
                        cursor = CursorUtil.CELL_SELECT_CURSOR;
                        info.subState = SUBSTATE_IDLE;
                    } else {
                        cursor = CursorUtil.DEFAULT_CURSOR;
                        info.subState = SUBSTATE_IDLE;
                    }
                }
            } else {
                endDrag(modifier);
            }

            info.lastMousePosition.x = x;
            info.lastMousePosition.y = y;
        } catch (Exception ex) {
        }
    }

    protected Object hitResizer(int x, int y) {
        TableBase table = (TableBase) tablePeer.getComponent();

        int hit = tablePeer.hitSelection(x, y);

        if (hit == TablePeer.NULL) {
            return null;
        }

        Rectangle sel = tablePeer.getSelection();

        if (hit == TablePeer._EAST) {
            int lastColumn = (sel.x + sel.width) - 1;

            return this.colResizer.init(lastColumn,
                getColumnFrom(table, lastColumn));
        } else if (hit == TablePeer._SOUTH) {
            int lastRow = (sel.y + sel.height) - 1;

            return this.rowResizer.init(lastRow, getRowFrom(table, lastRow));
        } else if (hit == TablePeer._SOUTH_EAST) {
            return this.cellMerger.init(0, 0, 0);
        } else if (hit == TablePeer._EDAGE) {
            return this.cellMover.init(0, x, y);
        }

        return null;
    }

    protected static int getRowFrom(TableBase grid, int row) {
        return grid.getRowY(row);
    }

    protected static int getColumnFrom(TableBase grid, int column) {
        return grid.getColumnX(column);
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier
     *            DOCUMENT ME!
     * @param x
     *            DOCUMENT ME!
     * @param y
     *            DOCUMENT ME!
     */
    public void mouseReleased(int modifier, int x, int y) {
        endDrag(modifier);
    }

    void doResize(int x, int y) {
        if (info.subState == SUBSTATE_COLUMN_RESIZING) {
            if (x == info.lastMousePosition.x) {
                return;
            }

            int x1 = Math.max(x, info.minPoint.x);
            vertResizeBar.moveTo(x1, 0);
        } else if (info.subState == SUBSTATE_ROW_RESIZING) {
            if (y == info.lastMousePosition.y) {
                return;
            }

            int y1 = Math.max(y, info.minPoint.y);
            horzResizeBar.moveTo(0, y1);
        }
    }

    void doSelect(int x, int y) {
        Rectangle selection = new Rectangle();

        try {
            Cell cid = hitCell(getTable(), x, y);

            if ((info.lastSelCell.x == cid.column) &&
                    (info.lastSelCell.y == cid.row)) {
                return;
            }

            info.lastSelCell.x = cid.column;
            info.lastSelCell.y = cid.row;
            selection.setRect(info.startCol, info.startRow, cid.column, cid.row);
            selection.x = Math.min(info.startCol, cid.column);
            selection.y = Math.min(info.startRow, cid.row);
            selection.width = Math.abs(cid.column - info.startCol) + 1;
            selection.height = Math.abs(cid.row - info.startRow) + 1;

            setSelection(selection);
        } catch (Exception e) {
        }

        ;
    }

    void doCellMove(int x, int y) {
        try {
            Cell cell = getTable().getValidStandardCellAt(x, y);

            if ((cell.row != info.lastRow) || (cell.column != info.lastColumn)) {
                Rectangle sel = (Rectangle) tablePeer.getSelection().clone();
                sel.translate(cell.column - info.startCol,
                    cell.row - info.startRow);
                cellsRubber.moveTo(getTable()
                                       .getBounds(sel.y, sel.x, sel.width,
                        sel.height));
                cellMover.x = sel.x;
                cellMover.y = sel.y;

                info.lastRow = cell.row;
                info.lastColumn = cell.column;
            }
        } catch (Exception e) {
        }
    }

    void doCellMerge(int x, int y) {
        try {
            x = Math.min(getTable().getWidth() - 1, x);
            y = Math.min(getTable().getHeight() - 1, y);

            Cell cell = getTable().getGrowStandardCellAt(x, y);

            if ((cell.row != info.lastRow) || (cell.column != info.lastColumn)) {
                if (((cell.row - info.startRow) >= 0) &&
                        ((cell.column - info.startCol) >= 0)) {
                    cellsRubber.moveTo(getTable()
                                           .getBounds(info.startRow,
                            info.startCol, cell.column - info.startCol + 1,
                            cell.row - info.startRow + 1));
                    cellMerger.x = cell.column;
                    cellMerger.y = cell.row;
                }

                info.lastRow = cell.row;
                info.lastColumn = cell.column;
            }
        } catch (Exception e) {
        }
    }

    void setSelection(Rectangle selection) {
        tablePeer.setSelection(selection);
    }

    void endDrag(int modifier) {
        if (info.mainState != STATE_IDLE) {
            if (info.mainState == STATE_SELECTING) {
                endSelect();
            } else if (info.mainState == STATE_RESIZING) {
                endResize();
            } else if (info.mainState == STATE_CELL_MOVING) {
                endCellMove((modifier & MouseEvent.CTRL_MASK) != 0);
            } else if (info.mainState == STATE_CELL_MERGING) {
                endCellMerge();
            } else if (info.mainState == TABLE_DRAG) {
                endMove();
            }
        }

        info.mainState = STATE_IDLE;
        info.subState = SUBSTATE_IDLE;
    }

    private void endCellMerge() {
        Cell sel = new Cell((Rectangle) tablePeer.getSelection().clone());

        if ((sel.row2() != cellMerger.y) || (sel.column2() != cellMerger.x)) {
            Rectangle target = new Rectangle(info.startCol, info.startRow,
                    cellMerger.x - info.startCol + 1,
                    cellMerger.y - info.startRow + 1);

            if (tablePeer.canGrow(sel, target.width, target.height)) {
                GroupEdit edit = new GroupEdit();

                try {
                    this.ununitCell(edit, target);
                    getTablePeer().getSelection().setBounds(target);
                    this.unitCell(edit);
                    BlankCellLoader.load(tablePeer, edit);
                    edit.addEdit(new TablePropertyEdit(tablePeer));
                    tablePeer.getOwner().addEdit(edit);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else {
                MessageBox.error(tablePeer.getOwner(), "不能对全并单元格作部分修改!");
            }
        }

        this.cellsRubber = null;
        info.lastRow = info.lastColumn = -1;
        cellMerger.x = cellMerger.y = -1;
        tablePeer.refreshSelection();

        _repaint();
    }

    private void endCellMove(boolean ctrl) {
        Cell srcCell = new Cell((Rectangle) tablePeer.getSelection());

        if (tablePeer.canMove(srcCell, cellMover.y, cellMover.x)) {
            moveCells((Rectangle) tablePeer.getSelection().clone(),
                cellMover.y, cellMover.x, ctrl);

            tablePeer.getSelection().setLocation(cellMover.x, cellMover.y);
        } else {
            MessageBox.error(tablePeer.getOwner(), "不能对全并单元格作部分修改!");
        }

        this.cellsRubber = null;
        info.lastRow = info.lastColumn = -1;
        tablePeer.refreshSelection();
        tablePeer.getOwner().repaint();
    }

    private void endMove() {
        ReportPanel owner = tablePeer.getOwner();
        GroupEdit edit = new GroupEdit();

        if (worker != null) {
            try {
                worker.close(edit);

                owner.addEdit(edit);

                MoveWorker.validParent(getTable());
                off.setLocation(0, 0);
                owner.childPointAsScreenPoint(tablePeer, off);
            } catch (Exception e) {
                e.printStackTrace();

                MessageBox.error(owner, e.getMessage());
                owner.getUndoManager().undo();
                owner.getUndoManager().discardEdit(edit);
            }
        }

        tablePeer.getOwner().repaint();
    }

    private static int[] getColumnWidths(TableBase t) {
        int[] cols = new int[t.getColumnCount()];

        for (int i = 0; i < cols.length; i++) {
            cols[i] = t.getColumnWidth(i);
        }

        return cols;
    }

    private void endResize() {
        int s;

        TableBase table = (TableBase) tablePeer.getComponent();

        try {
            Rectangle selection = getResizeSelction();

            if (info.subState == SUBSTATE_COLUMN_RESIZING) {
                s = info.lastMousePosition.x - info.minPoint.x;

                int[] oldVal = getColumnWidths(table);

                if (((selection.x + selection.width) - 1) == info.startCol) {
                    for (int i = selection.x;
                            i < (selection.x + selection.width); i++)
                        table.setColumnWidth(i, s);
                } else {
                    table.setColumnWidth(info.startCol, s);
                }

                int[] newVal = getColumnWidths(table);

                PropertyEdit edit = new TablePropertyEdit(tablePeer,
                        ComponentConstants.PROPERTY_COLUMN_WIDTHS, oldVal,
                        newVal);
                vertResizeBar = null;

                tablePeer.getOwner().addEdit(edit);
            } else if (info.subState == SUBSTATE_ROW_RESIZING) {
                s = info.lastMousePosition.y - info.minPoint.y;

                int[] oldVal = table.getRowHeights();

                if (((selection.y + selection.height) - 1) == info.startRow) {
                    for (int i = selection.y;
                            i < (selection.y + selection.height); i++) {
                        table.setRowHeight(i, s);
                    }
                } else {
                    int dy = s - table.getRowHeight(info.startRow);
                    table.setRowHeight(info.startRow, s);

                    if (tablePeer.isFixedHeight() &&
                            (info.startRow < (table.getRowCount() - 1))) {
                        table.setRowHeight(info.startRow + 1,
                            table.getRowHeight(info.startRow + 1) - dy);
                    }
                }

                int[] newVal = table.getRowHeights();

                PropertyEdit edit = new TablePropertyEdit(tablePeer,
                        ComponentConstants.PROPERTY_ROW_HEIGHTS, oldVal, newVal);
                tablePeer.getOwner().addEdit(edit);

                horzResizeBar = null;
            }

            table.invalid();
            this._repaint();
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    protected Rectangle getResizeSelction() {
        return tablePeer.getSelection();
    }

    private void endSelect() {
        info.mainState = STATE_IDLE;
    }

    private void startResize(int x, int y) {
        info.mainState = STATE_RESIZING;
        info.subState = (info.subState == SUBSTATE_ROW_RESIZE_READY)
            ? SUBSTATE_ROW_RESIZING : SUBSTATE_COLUMN_RESIZING;

        if (info.subState == SUBSTATE_COLUMN_RESIZING) {
            x = Math.max(x, info.minPoint.x);
            vertResizeBar = new RubberLine(VERTICAL, x, off,
                    this.getTablePeer().getOwner().getPage().getHeight());
        } else {
            y = Math.max(y, info.minPoint.y);
            horzResizeBar = new RubberLine(HORIZONTAL, y, off,
                    this.getTablePeer().getOwner().getPage().getWidth());
        }
    }

    private Cell startSelect(int x, int y) {
        try {
            Cell cell = hitCell(getTable(), x, y);

            cell = leadingCell(getTable(), cell);

            Rectangle selection = new Rectangle();

            selection.setRect(cell.column, cell.row, 1, 1);
            info.subState = SUBSTATE_CELL_SELECTED;

            info.startRow = selection.y;
            info.startCol = selection.x;
            info.lastSelCell.x = info.startCol;
            info.lastSelCell.y = info.startRow;
            info.mainState = STATE_SELECTING;

            setSelection((Rectangle) selection.clone());

            if (this.cursor != CursorUtil.CELL_SELECT_CURSOR) {
                this.cursor = CursorUtil.CELL_SELECT_CURSOR;
            }

            return cell;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Cell startCellMove(int x, int y) {
        try {
            Cell cell = getTablePeer()
                            .getNearestCellInSelection(this.cellMover.x,
                    this.cellMover.y);

            info.mainState = STATE_CELL_MOVING;
            info.startRow = cell.row;
            info.startCol = cell.column;

            cellsRubber = new RubberRect();
            cellsRubber.moveTo(tablePeer.getSelectionLocation());

            return cell;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Cell startCellMerge(int x, int y) {
        try {
            Cell br = new Cell(getTablePeer().getSelection());

            Cell cell = getTable().getCellstore()
                            .getCell(br.row2(), br.column2());

            if (cell == null) {
                cell = new Cell(br.row, br.column);
            }

            info.mainState = STATE_CELL_MERGING;
            info.startRow = cell.row;
            info.startCol = cell.column;

            cellsRubber = new RubberRect();
            cellsRubber.moveTo(this.getTable().getBounds(cell));

            return cell;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param grid
     *            DOCUMENT ME!
     * @param x
     *            DOCUMENT ME!
     * @param y
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Cell hitCell(TableBase grid, int x, int y) {
        return grid.getCellAt(x, y);
    }

    /**
     * DOCUMENT ME!
     *
     * @param table
     *            DOCUMENT ME!
     * @param cell
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Cell leadingCell(TableBase table, Cell cell) {
        Cell c = table.getCellstore().getCellOver(cell.row, cell.column);

        if ((c != null) && c.contains(cell.row, cell.column)) {
            return c;
        } else {
            return cell;
        }
    }

    private Component getParentCandidateForSelecton(Component c, Cell sel) {
        // 找到了部件的所有父对象
        for (int i = 0; i < c.getChildCount(); i++) {
            Component child = c.getChild(i);

            if (!(child instanceof GridComponent)) {
                continue;
            }

            Cell cell = (Cell) child.getCell().clone();

            if (cell.contains(sel)) { // 子部件包括所选区域

                return getParentCandidateForSelecton(child, sel);
            }
        }

        return c;
    }

    private boolean isFillRowExists(Rectangle sel) {
        TableBase t = this.getTable();
        CellStore store = t.getCellstore();

        for (int row = sel.y; row < (sel.height + sel.y); row++) {
            for (int col = sel.x; col < (sel.width + sel.x); col++) {
                Component c = store.getComponent(row, col);

                if (c != null) {
                    while ((c = c.getParent()) != null) {
                        if (c instanceof FillRowPanel) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Cursor getCursor() {
        return cursor;
    }

    protected JPopupMenu createPopupMenu() {
        JPopupMenu pop = new JPopupMenu();

        ReportPanel panel = tablePeer.getOwner();

        JMenu menu = new JMenu(App.messages.getString("res.408"));

        Action[] inserts = Main.getInstance().getNewableActions();

        for (int i = 1; i < inserts.length; i++) {
            if (!(inserts[i] instanceof NewTable)) {
                menu.add(new _InsertItem((ReportAction) inserts[i]));
            }
        }

        if (!panel.simple) {
            pop.add(menu);
        }

        pop.addSeparator();

        _PopAction mergeAction = new _PopAction(App.messages.getString(
                    "res.409"));
        mergeAction.putValue(Action.ACTION_COMMAND_KEY, UNITE_CELL);
        pop.add(mergeAction);

        _PopAction unmergeAction = new _PopAction(App.messages.getString(
                    "res.410"));
        unmergeAction.putValue(Action.ACTION_COMMAND_KEY, UN_UNITE_CELL);
        pop.add(unmergeAction);

        pop.addSeparator();

        AbstractAction action = null;

        menu = new JMenu(App.messages.getString("res.411"));
        pop.add(menu);

        action = new _PopAction(App.messages.getString("res.412"));
        action.putValue(Action.ACTION_COMMAND_KEY, INSERT_ROW_BEFORE);
        menu.add(action);

        action = new _PopAction(App.messages.getString("res.413"));
        action.putValue(Action.ACTION_COMMAND_KEY, INSERT_ROW_AFTER);
        menu.add(action);

        pop.add(menu);

        menu = new JMenu(App.messages.getString("res.414"));
        pop.add(menu);

        action = new _PopAction(App.messages.getString("res.415"));
        action.putValue(Action.ACTION_COMMAND_KEY, INSERT_COL_BEFORE);
        menu.add(action);

        action = new _PopAction(App.messages.getString("res.416"));
        action.putValue(Action.ACTION_COMMAND_KEY, INSERT_COL_AFTER);
        menu.add(action);

        menu = new JMenu(App.messages.getString("res.417"));
        pop.add(menu);

        JMenu tmp0 = new JMenu(App.messages.getString("res.412"));
        menu.add(tmp0);

        JMenu tmp1 = new JMenu(App.messages.getString("res.413"));

        menu.add(tmp1);

        for (int i = 3; i < 11; i++) {
            action = new _PopAction("" + i);
            action.putValue(Action.ACTION_COMMAND_KEY, INSERT_ROW_BEFORE_N);

            JMenuItem mi = new JMenuItem(action);
            mi.putClientProperty(BATCH, new Integer(i));

            tmp0.add(mi);

            action = new _PopAction("" + i);
            action.putValue(Action.ACTION_COMMAND_KEY, INSERT_ROW_AFTER_N);
            mi = new JMenuItem(action);
            mi.putClientProperty(BATCH, new Integer(i));

            tmp1.add(mi);
        }

        menu = new JMenu(App.messages.getString("res.418"));
        pop.add(menu);

        tmp0 = new JMenu(App.messages.getString("res.415"));
        menu.add(tmp0);
        tmp1 = new JMenu(App.messages.getString("res.416"));

        menu.add(tmp1);

        for (int i = 3; i < 11; i++) {
            action = new _PopAction("" + i);
            action.putValue(Action.ACTION_COMMAND_KEY, INSERT_COL_BEFORE_N);

            JMenuItem mi = new JMenuItem(action);
            mi.putClientProperty(BATCH, new Integer(i));

            tmp0.add(mi);

            action = new _PopAction("" + i);
            action.putValue(Action.ACTION_COMMAND_KEY, INSERT_COL_AFTER_N);
            mi = new JMenuItem(action);
            mi.putClientProperty(BATCH, new Integer(i));

            tmp1.add(mi);
        }

        action = new _PopAction(App.messages.getString("res.419"));
        action.putValue(Action.ACTION_COMMAND_KEY, REMOVE_ROW);
        pop.add(action);

        action = new _PopAction(App.messages.getString("res.420"));
        action.putValue(Action.ACTION_COMMAND_KEY, REMOVE_COL);
        pop.add(action);

        action = new _PopAction(App.messages.getString("res.421"));
        action.putValue(Action.ACTION_COMMAND_KEY, RESIZE_ROW_HEIGHTS);

        if (!panel.simple) {
            pop.addSeparator();
            pop.add(action);
        }

        action = new _PopAction(App.messages.getString("res.422"));
        action.putValue(Action.ACTION_COMMAND_KEY, RESIZE_COL_WIDTHS);

        if (!panel.simple) {
            pop.add(action);
        }

        pop.addSeparator();

        if (this.menuProvider != null) {
            Action[] as = this.menuProvider.getActions();

            for (Action a : as) {
                pop.add(a);
            }
        }

        action = new _PopAction(App.messages.getString("res.423"));
        action.putValue(Action.ACTION_COMMAND_KEY, SHOW_GRID);

        JCheckBoxMenuItem gridVisible = new JCheckBoxMenuItem(action);
        gridVisible.setSelected(true);

        if (!panel.simple) {
            pop.addSeparator();
            pop.add(gridVisible);
        }

        return pop;
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier
     *            DOCUMENT ME!
     * @param x
     *            DOCUMENT ME!
     * @param y
     *            DOCUMENT ME!
     */
    public void mousePressed(int modifier, int x, int y) {
        JPopupMenu ppp = getPopup();

        if ((MouseEvent.META_MASK & modifier) != 0) {
            JComponent panel = tablePeer.getOwner().getGroupPanel();
            ppp.show(panel,
                (int) (tablePeer.getOwner().getScale() * (x + off.x)),
                (int) (tablePeer.getOwner().getScale() * (y + off.y)));

            return;
        } else {
            if (ppp.isVisible()) {
                ppp.setVisible(false);
            }
        }

        if (info.subState == SUBSTATE_PRE_TABLE_DRAG) {
            info.mainState = TABLE_DRAG;
            info.subState = SUBSTATE_IDLE;

            SelectionFramePainter paintLayer = tablePeer.getOwner()
                                                        .getSelectedFrameLayer();

            worker = new MoveWorker(tablePeer.getOwner(), true,
                    ComponentPeer.CENTER, paintLayer,
                    new ComponentPeer[] { tablePeer });

            paintLayer.setSelectedFrames(worker.getFrames());
        } else if ((info.subState == SUBSTATE_IDLE) &&
                ((MouseEvent.META_MASK & modifier) == 0)) {
            startSelect(x, y);
            endSelect();
        }
    }

    int hitArea(int x, int y) {
        TableBase table = getTable();
        int tw = table.getWidth();
        int th = table.getHeight();

        Rectangle outer = new Rectangle(-4, -4, tw + 8, th + 8);

        if (!outer.contains(x, y)) {
            return OUTSIDE;
        }

        Rectangle inner = new Rectangle(2, 2, tw - 2, th - 2);

        if (inner.contains(x, y)) {
            return INSIDE;
        } else {
            return EDGE;
        }
    }

    void _repaint() {
        tablePeer.setSelection(tablePeer.getSelection(), false);

        tablePeer.getComponent().invalid();
        tablePeer.getOwner().repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @param edit
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean unitCell(GroupEdit edit) {
        TableBase t = getTable();

        Rectangle sel = (Rectangle) tablePeer.getSelection().clone();

        TablePropertyListener lst = new TablePropertyListener(tablePeer);
        t.addPropertyChangeListener(lst);

        boolean merged = t.mergeCell(sel.y, sel.x, sel.width, sel.height);
        t.removePropertyChangeListener(lst);

        if (merged) {
            lst.applyEdit(edit);
        }

        return merged;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r
     *            DOCUMENT ME!
     * @param c
     *            DOCUMENT ME!
     * @param w
     *            DOCUMENT ME!
     * @param h
     *            DOCUMENT ME!
     * @param prepareClass
     *            DOCUMENT ME!
     * @param edit
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean insertChildren(int r, int c, int w, int h,
        Class prepareClass, GroupEdit edit) {
        TablePropertyListener lst = new TablePropertyListener(tablePeer);

        TableBase table = (TableBase) tablePeer.getComponent();
        CellStore store = table.getCellstore();
        PanelStore panelstore = table.getPanelstore();

        for (int i = r; i < (r + h); i++) {
            for (int j = c; j < (c + w); j++) {
                Component com = store.getComponentOver(i, j);
                Cell newCell;

                if (com == null) {
                    newCell = new Cell(i, j);
                } else {
                    if ((com.getCell().row == i) &&
                            (com.getCell().column == j)) {
                        newCell = (Cell) com.getCell().clone();

                        Component p = com.getParent();

                        com.getParent().remove(com);

                        PropertyChangeEvent e = new PropertyChangeEvent(p,
                                ComponentConstants.PROPERTY_DELETE, com, null);

                        lst.propertyChange(e);
                    } else {
                        continue;
                    }
                }

                try {
                    Component child = (Component) prepareClass.newInstance();
                    child.setCell(newCell);

                    Component p = panelstore.getComponentOver(r, c);
                    p.add(child);

                    PropertyChangeEvent e = new PropertyChangeEvent(p,
                            ComponentConstants.PROPERTY_ADD, child, null);

                    lst.propertyChange(e);
                } catch (InstantiationException e1) {
                    e1.printStackTrace();
                } catch (IllegalAccessException e1) {
                    e1.printStackTrace();
                }
            }
        }

        lst.applyEdit(edit);

        return true;
    }

    /**
     * DOCUMENT ME!
     */
    public void cut() {
        copy(null, false);

        try {
            Rectangle r = tablePeer.getSelection();
            CompoundEdit edit = new GroupEdit();
            delete(new Cell(r), edit);
            edit.addEdit(new TablePropertyEdit(tablePeer));
            tablePeer.getOwner().addEdit(edit);
            tablePeer.setSelection(r);
            _repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param from DOCUMENT ME!
     * @param r DOCUMENT ME!
     * @param c DOCUMENT ME!
     * @param ctrl DOCUMENT ME!
     */
    public void moveCells(Rectangle from, int r, int c, boolean ctrl) {
        ClipBoard cp = new ClipBoard();

        copy(cp, true);

        try {
            GroupEdit edit = new GroupEdit();
            Cell region = new Cell(from);

            if (ctrl) {
                region = region.moveTo(r, c).getIntersection(new Cell(from));
            }

            delete(region, edit);
            tablePeer.getSelection().setLocation(c, r);
            _paste(edit, cp);
            BlankCellLoader.load(tablePeer, edit);
            edit.addEdit(new TablePropertyEdit(tablePeer));
            tablePeer.getOwner().addEdit(edit);
            tablePeer.setSelection(from);
            _repaint();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void copy(ClipBoard cp, boolean tableParentAlways) {
        if (cp == null) {
            cp = ClipBoard.getDefaultClipBoard();
        }

        final String PARENT = "old.parent";
        TableBase t = (TableBase) tablePeer.getComponent();
        CellStore store = t.getCellstore();
        Rectangle r = tablePeer.getSelection();

        Component first = store.getComponent(r.y, r.x);

        if (!tableParentAlways && (first != null) &&
                first.getCell().equals(new Cell(r))) {
            Cell save = first.getCell();
            first.setCell(null);

            try {
                cp.setContents(new Component[] { first });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                first.setCell(save);
            }
        } else {
            ArrayList sels = new ArrayList();

            for (int i = r.y; i < (r.y + r.height); i++) {
                for (int j = r.x; j < (r.x + r.width); j++) {
                    Component com = store.getComponent(i, j);

                    if (com != null) {
                        sels.add(com);
                        com.setClientProperty(PARENT, com.getParent());
                        com.getCell().column -= r.x;
                        com.getCell().row -= r.y;
                    }
                }
            }

            int[] columns = new int[r.width];

            for (int i = 0; i < columns.length; i++) {
                columns[i] = t.getColumnWidth(r.x + i);
            }

            int[] rows = new int[r.height];

            for (int i = 0; i < rows.length; i++) {
                rows[i] = t.getRowHeight(r.y + i);
            }

            Table newtable = new Table(rows, columns);
            newtable.setChildren(sels);

            Component[] selection = new Component[] { newtable };

            try {
                cp.setContents(selection);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Iterator it = sels.iterator();

                while (it.hasNext()) {
                    Component c = (Component) it.next();
                    c.setParent((Component) c.getClientProperty(PARENT));
                    c.getCell().column += r.x;
                    c.getCell().row += r.y;
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void delete() {
        CompoundEdit edit = new GroupEdit();
        delete(new Cell(tablePeer.getSelection()), edit);
        edit.addEdit(new TablePropertyEdit(tablePeer));
        tablePeer.getOwner().addEdit(edit);
        tablePeer.setSelection(tablePeer.getSelection());
        _repaint();
    }

    /**
     * DOCUMENT ME!
     */
    public void paste() {
        CompoundEdit edit = new GroupEdit();
        edit = _paste(edit, null);

        if (edit != null) {
            edit.addEdit(new TablePropertyEdit(tablePeer));
            tablePeer.getOwner().addEdit(edit);

            _repaint();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public CompoundEdit _paste(CompoundEdit edit, ClipBoard cp) {
        if (cp == null) {
            cp = ClipBoard.getDefaultClipBoard();
        }

        int left = Integer.MAX_VALUE;
        int top = Integer.MAX_VALUE;
        int bottom = 0;
        int right = 0;

        TableBase t = (TableBase) tablePeer.getComponent();
        Rectangle sel = tablePeer.getSelection();

        try {
            Component[] children = cp.getContents();

            if (children.length != 1) {
                return null;
            }

            if (children[0] instanceof TableBase) {
                children = (Component[]) ((TableBase) children[0]).getChildren()
                                          .toArray(new Component[0]);
            } else {
                int row = sel.y;
                int column = sel.x;
                children[0].setCell(leadingCell(t, new Cell(row, column)));
            }

            if (edit == null) {
                edit = new GroupEdit();
            }

            for (int i = 0; i < children.length; i++) {
                Component comp = children[i];
                Cell cell = (Cell) comp.getCell();

                if (cell.column < left) {
                    left = cell.column;
                }

                if (cell.row < top) {
                    top = cell.row;
                }

                if (((cell.row + cell.rowSpan) - 1) > bottom) {
                    bottom = (cell.row + cell.rowSpan) - 1;
                }

                if (((cell.column + cell.colSpan) - 1) > right) {
                    right = (cell.column + cell.colSpan) - 1;
                }
            }

            int offcol = sel.x - left;
            int offrow = sel.y - top;

            Rectangle selection = (Rectangle) sel.clone();
            selection.width = right - left + 1;
            selection.height = bottom - top + 1;

            int less = (selection.width + selection.x) - t.getColumnCount();

            if (less > 0) {
                selection.width -= less;
            }

            less = (selection.height + selection.y) - t.getRowCount();

            if (less > 0) {
                selection.height -= less;
            }

            Cell intercell = new Cell(selection);
            delete(intercell, edit);

            PanelStore panelstore = t.getPanelstore();
            TablePropertyListener lst = new TablePropertyListener(tablePeer);

            for (int i = 0; i < children.length; i++) {
                Component comp = children[i];

                Cell cell = (Cell) comp.getCell();
                cell.column += offcol;
                cell.row += offrow;

                if (!intercell.contains(cell.row, cell.column)) {
                    continue;
                }

                if (cell.row2() > intercell.row2()) {
                    cell.rowSpan -= (cell.row2() - intercell.row2());
                }

                if (cell.column2() > intercell.column2()) {
                    cell.colSpan -= (cell.column2() - intercell.column2());
                }

                Component p = panelstore.getComponentOver(cell.row, cell.column);
                p.add(comp);

                PropertyChangeEvent e = new PropertyChangeEvent(p,
                        ComponentConstants.PROPERTY_ADD, comp, null);

                lst.propertyChange(e);
            }

            lst.applyEdit(edit);

            tablePeer.setSelection(selection);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return edit;
    }

    private void delete(Cell cell, CompoundEdit edit) {
        TablePropertyListener lst = new TablePropertyListener(tablePeer);

        CellStore store = ((TableBase) tablePeer.getComponent()).getCellstore();

        for (int i = cell.row; i <= cell.row2(); i++) {
            for (int j = cell.column; j <= cell.column2(); j++) {
                Component com = store.getComponentOver(i, j);

                if (com != null) {
                    Cell oldCell = com.getCell();
                    Cell newCell = null;

                    if (cell.contains(com.getCell())) {
                        continue;
                    }

                    if (cell.contains(cell.row, com.getCell().column2())) {
                        newCell = (Cell) oldCell.clone();
                        newCell.colSpan = cell.column - newCell.column;
                    } else if (cell.contains(cell.row, com.getCell().column)) {
                        newCell = (Cell) oldCell.clone();
                        newCell.column = cell.column2() + 1;
                        newCell.colSpan = -newCell.column + 1 +
                            oldCell.column2();
                    }

                    if (newCell != null) {
                        com.setCell(newCell);

                        lst.propertyChange(new PropertyChangeEvent(com,
                                ComponentConstants.PROPERTY_CELL, oldCell,
                                newCell));
                    }
                }
            }
        }

        for (int i = cell.row; i <= cell.row2(); i++) {
            for (int j = cell.column; j <= cell.column2(); j++) {
                Component com = store.getComponent(i, j);

                if (com != null) {
                    Component p = com.getParent();
                    com.getParent().remove(com);

                    PropertyChangeEvent e = new PropertyChangeEvent(p,
                            ComponentConstants.PROPERTY_DELETE, com, null);

                    lst.propertyChange(e);
                }
            }
        }

        lst.applyEdit(edit);
    }

    /**
     * DOCUMENT ME!
     *
     * @param edit
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean ununitCell(GroupEdit edit, Rectangle sel) {
        TableBase t = getTable();

        TablePropertyListener lst = new TablePropertyListener(tablePeer);
        t.addPropertyChangeListener(lst);

        boolean unmerged = t.unmergeCell(sel.y, sel.x, sel.width, sel.height);
        t.removePropertyChangeListener(lst);

        if (unmerged) {
            lst.applyEdit(edit);
        }

        return unmerged;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g
     *            DOCUMENT ME!
     */
    public void paint(Graphics g) {
        if (vertResizeBar != null) {
            vertResizeBar.paint((Graphics2D) g);
        } else if (horzResizeBar != null) {
            horzResizeBar.paint((Graphics2D) g);
        } else if (cellsRubber != null) {
            cellsRubber.paint((Graphics2D) g);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier
     *            DOCUMENT ME!
     * @param x
     *            DOCUMENT ME!
     * @param y
     *            DOCUMENT ME!
     */
    public void mouseDoublePressed(int modifier, int x, int y) {
        Cell cell = hitCell(getTable(), x, y);

        if ((MouseEvent.META_MASK & modifier) == 0) {
            createLabelAt(cell, (InputEvent.CTRL_MASK & modifier) != 0);
        }
    }

    private void createLabelAt(Cell cell, boolean ctrl) {
        cell = leadingCell(getTable(), cell);

        Component hit = getTable().getCellstore()
                            .getComponent(cell.row, cell.column);

        if (hit == null) {
            hit = new Label();

            Rectangle b = getTable()
                              .getBounds(cell.row, cell.column, cell.colSpan,
                    cell.rowSpan);
            hit.setBounds(b);
            hit.setCell(cell);
            getTable().add(hit);

            ComponentPeer peer = ComponentPeerFactory.createPeer(tablePeer.getOwner(),
                    hit);
            tablePeer.add(peer);

            tablePeer.getOwner().addEdit(new AddEdit(peer, true));
        }

        ComponentPeer peer = tablePeer.getOwner().getComponentPeer(hit);
        ComponentEditor editor = ComponentEditorFactory.createEditor(peer, ctrl);

        if (editor != null) {
            editor.show(peer);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e
     *            DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
    }

    protected TableBase getTable() {
        return (tablePeer == null) ? null : (TableBase) tablePeer.getComponent();
    }

    private void nextFocusedCell(boolean z /* 从左到右从上到下 */) {
        TableBase grid = getTable();

        Cell focusedCell = (Cell) tablePeer.getFocusedCell();

        focusedCell = leadingCell(grid, focusedCell);

        if (z) {
            int row = focusedCell.row;
            int col = focusedCell.column + focusedCell.colSpan;

            if (col >= grid.getColumnCount()) {
                col = 0;
                row = row + 1;

                if (row == grid.getRowCount()) {
                    return;
                }
            }

            setSelection(new Rectangle(col, row, 1, 1));
        } else {
            int row = focusedCell.row + focusedCell.rowSpan;
            int col = focusedCell.column;

            if (row >= grid.getRowCount()) {
                row = 0;
                col = col + 1;

                if (col == grid.getColumnCount()) {
                    return;
                }
            }

            setSelection(new Rectangle(col, row, 1, 1));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean keyTyped(KeyEvent e) {
        if ((e.getModifiers() &
                (KeyEvent.ALT_MASK | KeyEvent.SHIFT_MASK | KeyEvent.CTRL_MASK)) != 0) {
            return false;
        }

        switch (e.getKeyChar()) {
        case KeyEvent.VK_TAB:
        case KeyEvent.VK_ENTER:
            nextFocusedCell(e.getKeyChar() == KeyEvent.VK_TAB);
            tablePeer.getOwner().repaint();

            break;

        case KeyEvent.VK_LEFT:
        case KeyEvent.VK_RIGHT:
        case KeyEvent.VK_DOWN:
        case KeyEvent.VK_UP:
            moveFocusedCell(e.getKeyChar());
            tablePeer.getOwner().repaint();

            break;

        case KeyEvent.VK_ESCAPE:
        case KeyEvent.VK_DELETE:
            break;

        default:
            e.consume();

            Cell cell = new Cell(tablePeer.getFocusedCell().row,
                    tablePeer.getFocusedCell().column, 1, 1);

            createLabelAt(cell, false);
            sendKey(editor, e.getKeyCode(), e.getKeyChar());

            break;
        }

        return true;
    }

    private void moveFocusedCell(int chr) {
        TableBase grid = getTable();

        Cell focusedCell = (Cell) tablePeer.getFocusedCell();

        focusedCell = leadingCell(grid, focusedCell);

        int row = focusedCell.row;
        int col = focusedCell.column;

        if (chr == KeyEvent.VK_LEFT) {
            col--;
        } else if (chr == KeyEvent.VK_RIGHT) {
            col++;
        } else if (chr == KeyEvent.VK_DOWN) {
            row++;
        } else {
            row--;
        }

        row = Math.max(Math.min(grid.getRowCount() - 1, row), 0);
        col = Math.max(Math.min(grid.getColumnCount() - 1, col), 0);

        setSelection(new Rectangle(col, row, 1, 1));
    }

    /**
     * DOCUMENT ME!
     *
     * @param comp
     *            DOCUMENT ME!
     * @param vk_code
     *            DOCUMENT ME!
     * @param keyChar
     *            DOCUMENT ME!
     */
    public static void sendKey(java.awt.Component comp, int vk_code,
        char keyChar) {
        KeyEvent k = new KeyEvent(comp, KeyEvent.KEY_TYPED,
                new Date().getTime(), 0, KeyEvent.VK_UNDEFINED, keyChar);
        comp.dispatchEvent(k);
    }

    private static Cell[] getCells(TableBase t, Rectangle r) {
        ArrayList cells = new ArrayList();

        for (int row = r.y; row < (r.y + r.height); row++) {
            for (int col = r.x; col < (r.x + r.width); col++) {
                Cell cell = new Cell(row, col);
                cell = leadingCell(t, cell);

                if (!cells.contains(cell)) {
                    cells.add(cell);
                }
            }
        }

        return (Cell[]) cells.toArray(new Cell[0]);
    }

    private void insertRow(int row, int h, CompoundEdit edit, boolean topside) {
        TableBase t = (TableBase) tablePeer.getComponent();

        TablePropertyListener lst = new TablePropertyListener(tablePeer);
        t.addPropertyChangeListener(lst);

        if (topside) {
            t.insertRowBefore(row, h);
        } else {
            t.insertRowAfter(row, h);
        }

        t.removePropertyChangeListener(lst);

        lst.applyEdit(edit);
    }

    private void insertColumn(int col, int w, CompoundEdit edit,
        boolean leftside) {
        TableBase t = (TableBase) tablePeer.getComponent();

        TablePropertyListener lst = new TablePropertyListener(tablePeer);
        t.addPropertyChangeListener(lst);

        if (leftside) {
            t.insertColumnBefore(col, w);
        } else {
            t.insertColumnAfter(col, w);
        }

        t.removePropertyChangeListener(lst);

        lst.applyEdit(edit);
    }

    boolean removeColumn(int col, int count, GroupEdit edit) {
        TableBase t = (TableBase) getTable();

        if (t.getColumnCount() == count) {
            MessageBox.error(tablePeer.getOwner().getTopLevelAncestor(),
                App.messages.getString("res.424"));

            return false;
        }

        TablePropertyListener lst = new TablePropertyListener(tablePeer);
        t.addPropertyChangeListener(lst);

        t.removeColumn(col, count);
        t.removePropertyChangeListener(lst);

        lst.applyEdit(edit);

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param row
     *            DOCUMENT ME!
     * @param count
     *            DOCUMENT ME!
     * @param edit
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean removeRow(int row, int count, CompoundEdit edit) {
        TableBase t = (TableBase) tablePeer.getComponent();

        if ((row < 0) || (row >= t.getRowCount())) {
            throw new java.lang.ArrayIndexOutOfBoundsException("No such row " +
                row);
        }

        if (t.getRowCount() == count) {
            MessageBox.error(tablePeer.getOwner().getTopLevelAncestor(),
                App.messages.getString("res.425"));

            return false;
        }

        TablePropertyListener lst = new TablePropertyListener(tablePeer);
        t.addPropertyChangeListener(lst);

        t.removeRow(row, count);

        t.removePropertyChangeListener(lst);

        lst.applyEdit(edit);

        return true;
    }

    Point getOff() {
        return off;
    }

    boolean resizeColumnWidths(GroupEdit edit) {
        TableBase table = getTable();

        int[] oldVal = getColumnWidths(table);

        Rectangle selection = tablePeer.getSelection();

        int width = table.getColumnWidth(selection.x);

        SizeDialog input = new SizeDialog((Frame) tablePeer.getOwner()
                                                           .getTopLevelAncestor(),
                App.messages.getString("res.422"),
                App.messages.getString("res.426"), width);
        input.show();

        if (!input.xok) {
            return false;
        } else {
            for (int i = selection.x; i < (selection.x + selection.width);
                    i++)
                table.setColumnWidth(i, input.getValue());

            int[] newVal = getColumnWidths(table);

            edit.addEdit(new TablePropertyEdit(tablePeer,
                    ComponentConstants.PROPERTY_COLUMN_WIDTHS, oldVal, newVal));

            return true;
        }
    }

    boolean resizeRowHeights(GroupEdit edit) {
        TableBase table = getTable();

        int[] oldVal = table.getRowHeights();

        Rectangle selection = tablePeer.getSelection();

        int height = table.getRowHeight(selection.y);

        SizeDialog input = new SizeDialog((Frame) tablePeer.getOwner()
                                                           .getTopLevelAncestor(),
                App.messages.getString("res.421"),
                App.messages.getString("res.427"), height);

        input.show();

        if (!input.xok) {
            return false;
        } else {
            for (int i = selection.y; i < (selection.y + selection.height);
                    i++)
                table.setRowHeight(i, input.getValue());

            int[] newVal = table.getRowHeights();

            edit.addEdit(new TablePropertyEdit(tablePeer,
                    ComponentConstants.PROPERTY_ROW_HEIGHTS, oldVal, newVal));

            return true;
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void hidePopup() {
        if (ppm.isVisible()) {
            ppm.setVisible(false);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TablePeer getTablePeer() {
        return tablePeer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b
     *            DOCUMENT ME!
     */
    public void setFocused(boolean b) {
        if (tablePeer != null) {
            tablePeer.setFocused(false);
        }
    }

    boolean isSystemCaptured(int chr) {
        switch (chr) {
        case KeyEvent.VK_TAB:
        case KeyEvent.VK_LEFT:
        case KeyEvent.VK_RIGHT:
        case KeyEvent.VK_DOWN:
        case KeyEvent.VK_UP:
            return true;
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean dispatchKeyEvent(KeyEvent e) {
        if ((this.tablePeer != null) && this.tablePeer.isFocused() &&
                tablePeer.getOwner().isActive() &&
                e.getComponent() instanceof LayerContainer &&
                isSystemCaptured(e.getKeyCode())) {
            if (e.getKeyCode() == KeyEvent.VK_TAB) {
                if (e.getID() == KeyEvent.KEY_TYPED) {
                    nextFocusedCell(e.getKeyChar() == KeyEvent.VK_TAB);
                    tablePeer.getOwner().repaint();
                }
            } else if (e.getID() == KeyEvent.KEY_PRESSED) {
                moveFocusedCell(e.getKeyCode());
                tablePeer.getOwner().repaint();
            }

            return true;
        }

        return false;
    }

    class _InsertItem extends JMenuItem implements ActionListener {
        Class prepareClass;

        public _InsertItem(ReportAction action) {
            super((String) action.getValue(Action.NAME),
                (Icon) action.getValue(Action.SMALL_ICON));
            this.addActionListener(this);
            this.prepareClass = (Class) action.getValue(ReportAction.CLASS);
        }

        public void actionPerformed(ActionEvent e) {
            if (prepareClass == null) {
                return;
            }

            GroupEdit edit = new GroupEdit();
            edit.addEdit(new TablePropertyEdit(tablePeer));

            ReportPanel owner = tablePeer.getOwner();

            Rectangle r = tablePeer.getSelection();
            insertChildren(r.y, r.x, r.width, r.height, prepareClass, edit);
            setSelection(tablePeer.getSelection());
            edit.addEdit(new TablePropertyEdit(tablePeer));
            owner.addEdit(edit);

            _repaint();
        }
    }

    class _PopAction extends AbstractAction {
        _PopAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            TableBase table = getTable();
            Rectangle selection = tablePeer.getSelection();

            boolean done = true;

            GroupEdit edit = new GroupEdit();
            edit.addEdit(new TablePropertyEdit(tablePeer));

            if (e.getActionCommand() == INSERT_ROW_BEFORE) {
                insertRow(selection.y, DEFAULT_ROW_HEIGHT, edit, true);

                BlankCellLoader.load(tablePeer, edit);
                setSelection(selection);
            } else if (e.getActionCommand() == INSERT_ROW_BEFORE_N) {
                int batch = ((Integer) ((JComponent) e.getSource()).getClientProperty(BATCH)).intValue();

                for (int i = 0; i < batch; i++) {
                    insertRow(selection.y, DEFAULT_ROW_HEIGHT, edit, true);

                    BlankCellLoader.load(tablePeer, edit);
                }

                selection.height = batch;
                setSelection(selection);
            } else if (e.getActionCommand() == INSERT_ROW_AFTER_N) {
                int batch = ((Integer) ((JComponent) e.getSource()).getClientProperty(BATCH)).intValue();

                for (int i = 0; i < batch; i++) {
                    insertRow(selection.y, DEFAULT_ROW_HEIGHT, edit, false);
                    BlankCellLoader.load(tablePeer, edit);
                }

                selection.y++;
                selection.height = batch;
                setSelection(selection);
            } else if (e.getActionCommand() == INSERT_ROW_AFTER) {
                insertRow(selection.y, DEFAULT_ROW_HEIGHT, edit, false);
                BlankCellLoader.load(tablePeer, edit);

                selection.y++;
                setSelection(selection);
            } else if (e.getActionCommand() == INSERT_COL_BEFORE) {
                insertColumn(selection.x, DEFAULT_COLUMN_WIDTH, edit, true);
                BlankCellLoader.load(tablePeer, edit);
                setSelection(selection);
            } else if (e.getActionCommand() == INSERT_COL_BEFORE_N) {
                int batch = ((Integer) ((JComponent) e.getSource()).getClientProperty(BATCH)).intValue();

                for (int i = 0; i < batch; i++) {
                    insertColumn(selection.x, DEFAULT_COLUMN_WIDTH, edit, true);
                }

                BlankCellLoader.load(tablePeer, edit);
                selection.width = batch;

                setSelection(selection);
            } else if (e.getActionCommand() == INSERT_COL_AFTER) {
                insertColumn(selection.x, DEFAULT_COLUMN_WIDTH, edit, false);
                BlankCellLoader.load(tablePeer, edit);
                selection.x++;
                setSelection(selection);
            } else if (e.getActionCommand() == INSERT_COL_AFTER_N) {
                int batch = ((Integer) ((JComponent) e.getSource()).getClientProperty(BATCH)).intValue();

                for (int i = 0; i < batch; i++) {
                    insertColumn(selection.x, DEFAULT_COLUMN_WIDTH, edit, false);
                }

                BlankCellLoader.load(tablePeer, edit);
                selection.x++;
                selection.width = batch;
                setSelection(selection);
            } else if (e.getActionCommand() == REMOVE_ROW) {
                done = removeRow(selection.y, selection.height, edit);

                if (done) {
                    selection.height = 1;

                    if (selection.y == table.getRowCount()) {
                        selection.y--;
                    }

                    BlankCellLoader.load(tablePeer, edit);

                    setSelection(selection);
                }
            } else if (e.getActionCommand() == SHOW_GRID) {
                JCheckBoxMenuItem gv = (JCheckBoxMenuItem) e.getSource();
                tablePeer.setGridVisible(gv.isSelected());

                tablePeer.getOwner().repaint();
                done = false;
            } else if (e.getActionCommand() == REMOVE_COL) {
                done = removeColumn(selection.x, selection.width, edit);

                if (done) {
                    selection.width = 1;

                    if (selection.x == table.getColumnCount()) {
                        selection.x--;
                    }

                    BlankCellLoader.load(tablePeer, edit);
                    setSelection(selection);
                }
            } else if (e.getActionCommand() == UNITE_CELL) {
                done = unitCell(edit);
            } else if (e.getActionCommand() == UN_UNITE_CELL) {
                done = ununitCell(edit,
                        (Rectangle) tablePeer.getSelection().clone());
                BlankCellLoader.load(tablePeer, edit);
                setSelection(selection);
            } else if (e.getActionCommand() == RESIZE_ROW_HEIGHTS) {
                done = resizeRowHeights(edit);
            } else if (e.getActionCommand() == RESIZE_COL_WIDTHS) {
                done = resizeColumnWidths(edit);
            }

            if (done) {
                edit.addEdit(new TablePropertyEdit(tablePeer));
                tablePeer.getOwner().addEdit(edit);
                _repaint();
            }
        }
    }

    class _State {
        public int lastRow;
        public int lastColumn;
        public int mainState;
        public int subState;
        public Rectangle lastResizeLine = new Rectangle();
        public int startRow;
        public int startCol;
        public Point minPoint = new Point();
        public Point lastSelCell = new Point();
        public Point lastMousePosition = new Point();
    }

    class _RowSizer {
        int index;
        int top;

        _RowSizer init(int index, int top) {
            this.index = index;
            this.top = top;

            return this;
        }
    }

    class _ColumnSizer {
        int index;
        int left;

        _ColumnSizer init(int index, int left) {
            this.index = index;
            this.left = left;

            return this;
        }
    }

    class _CellMerger {
        int index;
        int x;
        int y;

        _CellMerger init(int index, int x, int y) {
            this.index = index;
            this.x = x;
            this.y = y;

            return this;
        }
    }

    class _CellMover {
        int index;
        int x;
        int y;

        _CellMover init(int index, int x, int y) {
            this.index = index;
            this.x = x;
            this.y = y;

            return this;
        }
    }
}


class SizeDialog extends JDialog {
    SpinEditor editor;
    boolean xok;

    SizeDialog(Frame owner, String caption, String label, int value) {
        super(owner, caption, true);

        JPanel center = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        center.add(new JLabel(label), gbc);
        editor = new SpinEditor(new Integer(value), new Integer(0),
                new Integer(500), new Integer(1));
        editor.setPreferredSize(new Dimension(90, 20));
        center.add(editor, gbc);

        JPanel commandPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton doneCommand = new JButton(App.messages.getString("res.3"));
        JButton cancelCommand = new JButton(App.messages.getString("res.4"));
        doneCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    xok = true;
                    hide();
                }
            });
        cancelCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    hide();
                }
            });

        commandPanel.add(doneCommand);
        commandPanel.add(cancelCommand);

        getContentPane().add(center, BorderLayout.CENTER);
        getContentPane().add(commandPanel, BorderLayout.SOUTH);

        this.setSize(new Dimension(300, 130));

        this.setLocationRelativeTo(owner);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getValue() {
        return editor.intValue();
    }
}
