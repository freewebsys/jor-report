package jatools.designer.layer.table;

import jatools.accessor.PropertyDescriptor;

import jatools.component.ColumnPanel;
import jatools.component.Component;
import jatools.component.ComponentConstants;
import jatools.component.Panel;

import jatools.component.table.Cell;
import jatools.component.table.CellStore;
import jatools.component.table.FillRowPanel;
import jatools.component.table.GridComponent;
import jatools.component.table.HeaderTable;
import jatools.component.table.PanelStore;
import jatools.component.table.RowPanel;
import jatools.component.table.Table;
import jatools.component.table.TableBase;

import jatools.designer.App;
import jatools.designer.Main;
import jatools.designer.ReportPanel;

import jatools.designer.layer.AbstractLayer;
import jatools.designer.layer.Layer;
import jatools.designer.layer.painter.Painter;

import jatools.designer.peer.ComponentPeer;
import jatools.designer.peer.TablePeer;

import jatools.designer.property.editor.NodePathPropertyEditor;

import jatools.designer.undo.DeleteEdit;
import jatools.designer.undo.GroupEdit;
import jatools.designer.undo.ParentChangeEdit;
import jatools.designer.undo.PropertyEdit;
import jatools.designer.undo.TablePropertyEdit;

import jatools.engine.printer.NodePathTarget;

import jatools.swingx.MessageBox;

import jatools.util.CursorUtil;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;

import java.beans.PropertyChangeEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.undo.CompoundEdit;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class TablePanelEditLayer extends AbstractLayer implements Painter, FocusRequest,
    MenuProvider {
    private final static String INSERT_LIST_BY_SELECT = "insert.by.select";
    private final static String DELETE_LIST = "delete";
    private final static String DELETE_LIST_ALL = "delete.all";
    private final static String CREATE_FILL_ROW = "create.fillrow";
    private final static String DELETE_FILL_ROW = "delete.fillrow";
    static int VERTICAL = 1;
    static int HORIZONTAL = 0;
    static BasicStroke RESIZE_STROKE = new BasicStroke(1, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER, 1.0f, new float[] { 1.0f, 1.0f }, 0.0f);
    private static int LEFT = -1;
    private static int RIGHT = 1;
    private static int UP = -1;
    private static int DOWN = 1;
    private static int FIXED = 0;
    private Handler2 selectedHandler = null;
    private _Line vertResizeBar;
    private _Line horzResizeBar;
    private RubberData rubber = new RubberData();
    private ReportPanel owner;
    TableHandlerPainter painter;
    private JPopupMenu pop;
    private Point start;
    private Table hot;
    private _PopAction deleteAll = new _PopAction(App.messages.getString("res.428"));
    private JMenuItem deleteAllItem = new JMenuItem(deleteAll);
    private Point selectedPoint;
    private boolean requestingFocus;

    /**
     * Creates a new TablePanelEditLayer object.
     *
     * @param owner
     *            DOCUMENT ME!
     */
    public TablePanelEditLayer(ReportPanel owner) {
        this.owner = owner;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasTooltip() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g
     *            DOCUMENT ME!
     */
    public void paint(Graphics2D g) {
        TablePeer tablePeer = owner.getActiveTable();

        if (tablePeer != null) {
            if (painter == null) {
                painter = new TableHandlerPainter();
            }

            painter.paint(tablePeer, g);
        }

        if (vertResizeBar != null) {
            vertResizeBar.paint(g);
        }

        if (horzResizeBar != null) {
            horzResizeBar.paint(g);
        }

        if ((this.rubber != null) && this.rubber.active) {
            g.translate(start.x, start.y);
            this.rubber.paint(g);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x
     *            DOCUMENT ME!
     * @param y
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTooltip(int x, int y) {
        if ((this.painter != null) && this.painter.isActive()) {
            start = painter.start;

            Iterator it = painter.iterator();

            if (it != null) {
                x -= start.x;
                y -= start.y;

                if (painter.hitTable(x, y) != null) {
                    while (it.hasNext()) {
                        Handler2 handler = (Handler2) it.next();

                        if (handler.hit(x, y) && handler.c instanceof NodePathTarget) {
                            return ((NodePathTarget) handler.c).getNodePath();
                        }
                    }
                }
            }
        }

        return NO_TOOLTIP;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int wakenBy() {
        return Layer.WAKEN_BY_MOUSE_PRESSED;
    }

    /**
     * DOCUMENT ME!
     *
     * @param waker
     *            DOCUMENT ME!
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
     *
     * @return DOCUMENT ME!
     */
    public boolean tryWake(int waker, int modifier, int x, int y, int deltaX, int deltaY) {
        if ((this.painter != null) && this.painter.isActive()) {
            start = painter.start;

            x -= start.x;
            y -= start.y;

            if (this.requestingFocus || (painter.hitTable(x, y) != null) ||
                    painter.hitStaticHeader(x + start.x, y + start.y)) {
                pressed(modifier, x, y);
                this.painter.getTablePeer().setFocused(true);

                this.requestingFocus = false;

                return true;
            }
        }

        return false;
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
        selectedPoint = new Point(x, y);
        selectedHandler = null;
        horzResizeBar = null;
        vertResizeBar = null;
        x -= start.x;
        y -= start.y;

        if (isWaken()) {
            if ((painter.hitTable(x, y) == null) && !this.request(x, y) &&
                    !painter.hitStaticHeader(x + start.x, y + start.y)) {
                this.painter.getTablePeer().setFocused(false);
                this.requestingFocus = false;
                painter.focused = null;
                this.fireActionPerformed(ACTION_DONE);
                owner.getGroupPanel().mousePressed(modifier, x + start.x, y + start.y);

                return;
            }
        }

        pressed(modifier, x, y);
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
        if ((this.painter != null) && this.painter.isActive()) {
            if (selectedHandler != null) {
                if (selectedHandler.hit(x - painter.start.x, y - painter.start.y)) {
                    setCursor(CursorUtil.MOVE_CURSOR);
                } else {
                    setCursor(CursorUtil.DEFAULT_CURSOR);
                }
            }
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
        if (selectedHandler != null) {
            Point startPoint = new Point(0, 0);
            startPoint = owner.childPointAsScreenPoint(painter.getTablePeer(), startPoint);

            Component c = selectedHandler.c;

            if (c instanceof HeaderTable) {
                return;
            }

            TableBase tt = (TableBase) painter.getTablePeer().getComponent();

            // start = painter.start;

            // 取得 FillRowPanel的 cell;
            // cell.rowSpan = y 所在行 -
            // 取得空间
            // 取得

            // System.out.println(tt.getStandardCellAt(x, y));
            // Rectangle area = tt.getBounds(fp.getCell());
            // int x0 = tt.getColumnX(fp.getCell().column);
            // int y0 = tt.getRowY(fp.getCell().row);
            //
            // if (y < area.y) {
            // area.height = 1;
            // // lastRow
            // } else {
            // Cell cell = tt.getStandardCellAt(0, y);
            // area.height = tt.getHeight(fp.getCell().row, cell.row
            // - fp.getCell().row + 1);
            //
            // }
            if (selectedPoint != null) {
                if (selectedHandler.c instanceof FillRowPanel) {
                    x -= start.x;
                    y -= start.y;

                    FillRowPanel fp = (FillRowPanel) selectedHandler.c;

                    if (!this.rubber.active) {
                        Rectangle rc = tt.getBounds(fp.getCell());

                        int vType = (y < (rc.y + (rc.height / 2))) ? UP : DOWN;
                        Cell valid = new Cell(0, 0, tt.getColumnCount(), tt.getRowCount());

                        if (vType == UP) {
                            valid.row = 0;
                            valid.rowSpan = fp.getCell().row2() + 1;
                        }

                        int hType = (x < (rc.x + (rc.width / 2))) ? LEFT : RIGHT;

                        if (hType == LEFT) {
                            valid.column = 0;
                            valid.colSpan = fp.getCell().column2() + 1;
                        }

                        this.rubber.activate(fp.getCell(), valid, vType, hType);
                    }

                    Cell cell = tt.getGrowStandardCellAt(x, y);
                    this.rubber.setRow1(cell.row);
                    this.rubber.setColumn1(cell.column);

                    owner.repaint();
                } else if (selectedHandler.c instanceof RowPanel) {
                    RowPanel cp = (RowPanel) selectedHandler.c;
                    Panel topBrotherRowPanel = StaticPanelFactory.getStaticRowHeaderTopBrother(cp,
                            tt);
                    Panel downBrotherRowPanel = StaticPanelFactory.getStaticRowHeaderBottomBrother(cp,
                            tt);
                    Component parentPanel = StaticPanelFactory.getParentComponent(cp);
                    ArrayList childPanels = StaticPanelFactory.getChildPanels(cp);

                    int start_y = 0;
                    int static_y = 0;
                    int real_rowPanel_y1 = StaticPanelFactory.getPanelPoint1(tt, cp).y +
                        startPoint.y;
                    int real_rowPanel_y2 = StaticPanelFactory.getPanelPoint2(tt, cp).y +
                        startPoint.y;

                    if (Math.abs(real_rowPanel_y2 - selectedPoint.getY()) <= Math.abs(real_rowPanel_y1 -
                                selectedPoint.getY())) {
                        start_y = real_rowPanel_y2;
                        static_y = real_rowPanel_y1;
                    } else {
                        start_y = real_rowPanel_y1;
                        static_y = real_rowPanel_y2;
                    }

                    int end_y = y;
                    int change = end_y - start_y;

                    int stop_y = 0;
                    int nearetRowValue = StaticPanelFactory.getNearestRowValue(tt, y -
                            startPoint.y);

                    if ((start_y == real_rowPanel_y2) && (change > 0)) {
                        stop_y = StaticPanelFactory.getMaxY_To_Drag(tt, cp, downBrotherRowPanel,
                                parentPanel, childPanels, true) + startPoint.y;

                        if (end_y >= stop_y) {
                            horzResizeBar = new _Line(HORIZONTAL, start_y, stop_y, static_y, true);
                        } else if (end_y < (start_y + 10)) {
                            horzResizeBar = new _Line(HORIZONTAL, start_y, start_y, static_y, true);
                        } else {
                            nearetRowValue = StaticPanelFactory.getNearestRowValue(tt,
                                    y - startPoint.y) + startPoint.y;
                            horzResizeBar = new _Line(HORIZONTAL, start_y, nearetRowValue,
                                    static_y, true);
                        }
                    } else if ((start_y == real_rowPanel_y2) && (change < 0)) {
                        stop_y = StaticPanelFactory.getMinY_To_Drag(tt, cp, topBrotherRowPanel,
                                parentPanel, childPanels, false) + startPoint.y;

                        if (end_y <= stop_y) {
                            horzResizeBar = new _Line(HORIZONTAL, start_y, stop_y, static_y, true);
                        } else if (end_y > (start_y - 10)) {
                            horzResizeBar = new _Line(HORIZONTAL, start_y, start_y, static_y, true);
                        } else {
                            nearetRowValue = StaticPanelFactory.getNearestRowValue(tt,
                                    y - startPoint.y) + startPoint.y;
                            horzResizeBar = new _Line(HORIZONTAL, start_y, nearetRowValue,
                                    static_y, true);
                        }
                    }

                    if ((start_y == real_rowPanel_y1) && (change > 0)) {
                        stop_y = StaticPanelFactory.getMaxY_To_Drag(tt, cp, downBrotherRowPanel,
                                parentPanel, childPanels, false) + startPoint.y;

                        if (end_y >= stop_y) {
                            horzResizeBar = new _Line(HORIZONTAL, start_y, stop_y, static_y, true);
                        } else if (end_y < (start_y + 10)) {
                            horzResizeBar = new _Line(HORIZONTAL, start_y, start_y, static_y, true);
                        } else {
                            nearetRowValue = StaticPanelFactory.getNearestRowValue(tt,
                                    y - startPoint.y) + startPoint.y;
                            horzResizeBar = new _Line(HORIZONTAL, start_y, nearetRowValue,
                                    static_y, true);
                        }
                    } else if ((start_y == real_rowPanel_y1) && (change < 0)) {
                        stop_y = StaticPanelFactory.getMinY_To_Drag(tt, cp, topBrotherRowPanel,
                                parentPanel, childPanels, true) + startPoint.y;

                        if (end_y <= stop_y) {
                            horzResizeBar = new _Line(HORIZONTAL, start_y, stop_y, static_y, true);
                        } else if (end_y > (start_y - 10)) {
                            horzResizeBar = new _Line(HORIZONTAL, start_y, start_y, static_y, true);
                        } else {
                            nearetRowValue = StaticPanelFactory.getNearestRowValue(tt,
                                    y - startPoint.y) + startPoint.y;
                            horzResizeBar = new _Line(HORIZONTAL, start_y, nearetRowValue,
                                    static_y, true);
                        }
                    }

                    owner.repaint();
                } else if (selectedHandler.c instanceof ColumnPanel) {
                    ColumnPanel cp = (ColumnPanel) selectedHandler.c;
                    Panel leftBrotherRowPanel = StaticPanelFactory.getStaticColumnHeaderLeftBrother(cp,
                            tt);
                    Panel rightBrotherRowPanel = StaticPanelFactory.getStaticColumnHeaderRightBrother(cp,
                            tt);

                    Component parentPanel = StaticPanelFactory.getParentComponent(cp);
                    ArrayList childPanels = StaticPanelFactory.getChildPanels(cp);

                    int start_x = 0;
                    int static_x = 0;
                    int real_colPanel_x1 = StaticPanelFactory.getPanelPoint1(tt, cp).x +
                        startPoint.x;
                    int real_colPanel_x2 = StaticPanelFactory.getPanelPoint2(tt, cp).x +
                        startPoint.x;

                    if (Math.abs(real_colPanel_x2 - selectedPoint.getX()) <= Math.abs(real_colPanel_x1 -
                                selectedPoint.getX())) {
                        start_x = real_colPanel_x2;
                        static_x = real_colPanel_x1;
                    } else {
                        start_x = real_colPanel_x1;
                        static_x = real_colPanel_x2;
                    }

                    int end_x = x;
                    int change = end_x - start_x;

                    int stop_x = 0;
                    int nearetColValue = StaticPanelFactory.getNearestColumnValue(tt,
                            x - startPoint.x);

                    if ((start_x == real_colPanel_x2) && (change > 0)) {
                        stop_x = StaticPanelFactory.getMaxX_To_Drag(tt, cp, rightBrotherRowPanel,
                                parentPanel, childPanels, true) + startPoint.x;

                        if (end_x >= stop_x) {
                            vertResizeBar = new _Line(VERTICAL, start_x, stop_x, static_x, true);
                        } else if (end_x < (start_x + 10)) {
                            vertResizeBar = new _Line(VERTICAL, start_x, start_x, static_x, true);
                        } else {
                            nearetColValue = StaticPanelFactory.getNearestColumnValue(tt,
                                    x - startPoint.x) + startPoint.x;
                            vertResizeBar = new _Line(VERTICAL, start_x, nearetColValue, static_x,
                                    true);
                        }
                    } else if ((start_x == real_colPanel_x2) && (change < 0)) {
                        stop_x = StaticPanelFactory.getMinX_To_Drag(tt, cp, leftBrotherRowPanel,
                                parentPanel, childPanels, false) + startPoint.x;

                        if (end_x <= stop_x) {
                            vertResizeBar = new _Line(VERTICAL, start_x, stop_x, static_x, true);
                        } else if (end_x > (start_x - 10)) {
                            vertResizeBar = new _Line(VERTICAL, start_x, start_x, static_x, true);
                        } else {
                            nearetColValue = StaticPanelFactory.getNearestColumnValue(tt,
                                    x - startPoint.x) + startPoint.x;
                            vertResizeBar = new _Line(VERTICAL, start_x, nearetColValue, static_x,
                                    true);
                        }
                    }

                    if ((start_x == real_colPanel_x1) && (change > 0)) {
                        stop_x = StaticPanelFactory.getMaxX_To_Drag(tt, cp, rightBrotherRowPanel,
                                parentPanel, childPanels, false) + startPoint.x;

                        if (end_x >= stop_x) {
                            vertResizeBar = new _Line(VERTICAL, start_x, stop_x, static_x, true);
                        } else if (end_x < (start_x + 10)) {
                            vertResizeBar = new _Line(VERTICAL, start_x, start_x, static_x, true);
                        } else {
                            nearetColValue = StaticPanelFactory.getNearestColumnValue(tt,
                                    x - startPoint.x) + startPoint.x;
                            vertResizeBar = new _Line(VERTICAL, start_x, nearetColValue, static_x,
                                    true);
                        }
                    } else if ((start_x == real_colPanel_x1) && (change < 0)) {
                        stop_x = StaticPanelFactory.getMinX_To_Drag(tt, cp, leftBrotherRowPanel,
                                parentPanel, childPanels, true) + startPoint.x;

                        if (end_x <= stop_x) {
                            vertResizeBar = new _Line(VERTICAL, start_x, stop_x, static_x, true);
                        } else if (end_x > (start_x - 10)) {
                            vertResizeBar = new _Line(VERTICAL, start_x, start_x, static_x, true);
                        } else {
                            nearetColValue = StaticPanelFactory.getNearestRowValue(tt,
                                    y - startPoint.y) + startPoint.y;
                            vertResizeBar = new _Line(VERTICAL, start_x, nearetColValue, static_x,
                                    true);
                        }
                    }

                    owner.repaint();
                }
            }
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
    public void mouseReleased(int modifier, int x, int y) {
        if (selectedHandler != null) {
            Point startPoint = new Point(0, 0);
            startPoint = owner.childPointAsScreenPoint(painter.getTablePeer(), startPoint);

            TableBase tt = (TableBase) painter.getTablePeer().getComponent();

            if (horzResizeBar != null) {
                CompoundEdit edit = new CompoundEdit();
                TablePropertyEdit tableEdit = new TablePropertyEdit(painter.getTablePeer());
                edit.addEdit(tableEdit);

                int start_y = horzResizeBar.startPos;
                int end_y = horzResizeBar.stopedPos;
                int static_y = horzResizeBar.staticPos;
                RowPanel row = (RowPanel) selectedHandler.c;

                if ((start_y == end_y) || !horzResizeBar.ready) {
                    horzResizeBar = null;

                    return;
                }

                if (static_y == end_y) {
                    if (row.getParent() != null) {
                        ArrayList v = row.getChildren();

                        int oldRowIndex = owner.getComponentPeer(row.getParent())
                                               .indexOf(owner.getComponentPeer(row));

                        ArrayList childVector = new ArrayList();

                        for (int i = 0; i < v.size(); i++) {
                            childVector.add(v.get(i));
                        }

                        Iterator it = childVector.iterator();

                        while (it.hasNext()) {
                            Component c = (Component) it.next();
                            ComponentPeer peer = owner.getComponentPeer(c);
                            ComponentPeer oldParentPeer = owner.getComponentPeer(c.getParent());
                            int oldIndex = oldParentPeer.indexOf(peer);

                            oldParentPeer.remove(peer);
                            owner.getComponentPeer(row.getParent()).add(peer);

                            ParentChangeEdit parentChangeEdit1 = new ParentChangeEdit(peer,
                                    oldParentPeer, oldIndex);
                            edit.addEdit(parentChangeEdit1);
                        }

                        ComponentPeer ownerPeer = owner.getComponentPeer(row);
                        owner.getComponentPeer(row.getParent()).remove(ownerPeer);

                        DeleteEdit delEdit = new DeleteEdit(ownerPeer, oldRowIndex);
                        edit.addEdit(delEdit);

                        tableEdit = new TablePropertyEdit(painter.getTablePeer());
                        edit.addEdit(tableEdit);
                        owner.addEdit(edit);
                    }
                } else {
                    Cell oldCell = row.getCell();
                    Cell newCell = null;
                    newCell = StaticPanelFactory.getNewCell(tt, oldCell, startPoint, end_y, static_y);

                    if (tt.getBounds(newCell).height > tt.getBounds(oldCell).height) {
                        Component parent = row.getParent();
                        ArrayList brothers = parent.getChildren();
                        ArrayList otherChild = new ArrayList();

                        for (int i = 0; i < brothers.size(); i++) {
                            Component c = (Component) brothers.get(i);

                            if (!c.equals(row)) {
                                if (newCell.contains(c.getCell())) {
                                    otherChild.add(c);
                                }
                            }
                        }

                        for (int i = 0; i < otherChild.size(); i++) {
                            Component c = (Component) otherChild.get(i);
                            ComponentPeer peer = owner.getComponentPeer(c);
                            int oldIndex = peer.getParent().indexOf(peer);
                            ComponentPeer oldParentPeer = peer.getParent();

                            oldParentPeer.remove(peer);
                            owner.getComponentPeer(row).add(peer);

                            ParentChangeEdit parentChangeEdit1 = new ParentChangeEdit(peer,
                                    oldParentPeer, oldIndex);
                            edit.addEdit(parentChangeEdit1);
                        }
                    } else {
                        ArrayList children = row.getChildren();
                        ArrayList otherChild = new ArrayList();

                        for (int i = 0; i < children.size(); i++) {
                            Component c = (Component) children.get(i);

                            if (!newCell.contains(c.getCell())) {
                                otherChild.add(c);
                            }
                        }

                        for (int i = 0; i < otherChild.size(); i++) {
                            Component c = (Component) otherChild.get(i);

                            ComponentPeer peer = owner.getComponentPeer(c);
                            int oldIndex = peer.getParent().indexOf(peer);
                            ComponentPeer oldParentPeer = peer.getParent();

                            oldParentPeer.remove(peer);
                            owner.getComponentPeer(row.getParent()).add(peer);

                            ParentChangeEdit parentChangeEdit1 = new ParentChangeEdit(peer,
                                    oldParentPeer, oldIndex);
                            edit.addEdit(parentChangeEdit1);
                        }
                    }

                    row.setCell(newCell);

                    PropertyEdit cellEdit = new PropertyEdit(owner.getComponentPeer(row),
                            ComponentConstants.PROPERTY_CELL, oldCell, newCell);
                    edit.addEdit(cellEdit);

                    tableEdit = new TablePropertyEdit(painter.getTablePeer());
                    edit.addEdit(tableEdit);
                    owner.addEdit(edit);
                }
            }

            if (vertResizeBar != null) {
                CompoundEdit edit = new CompoundEdit();

                TablePropertyEdit tableEdit = new TablePropertyEdit(painter.getTablePeer());
                edit.addEdit(tableEdit);

                int start_x = vertResizeBar.startPos;
                int end_x = vertResizeBar.stopedPos;
                int static_x = vertResizeBar.staticPos;
                ColumnPanel column = (ColumnPanel) selectedHandler.c;

                if ((start_x == end_x) || !vertResizeBar.ready) {
                    vertResizeBar = null;

                    return;
                }

                if (static_x == end_x) {
                    if (column.getParent() != null) {
                        ArrayList v = column.getChildren();

                        int oldColumnIndex = owner.getComponentPeer(column.getParent())
                                                  .indexOf(owner.getComponentPeer(column));
                        ArrayList childVector = new ArrayList();

                        for (int i = 0; i < v.size(); i++) {
                            childVector.add(v.get(i));
                        }

                        Iterator it = childVector.iterator();

                        while (it.hasNext()) {
                            Component c = (Component) it.next();
                            ComponentPeer peer = owner.getComponentPeer(c);
                            ComponentPeer oldParentPeer = owner.getComponentPeer(c.getParent());
                            int oldIndex = oldParentPeer.indexOf(peer);

                            oldParentPeer.remove(peer);
                            owner.getComponentPeer(column.getParent()).add(peer);

                            ParentChangeEdit parentChangeEdit1 = new ParentChangeEdit(peer,
                                    oldParentPeer, oldIndex);
                            edit.addEdit(parentChangeEdit1);
                        }

                        ComponentPeer ownerPeer = owner.getComponentPeer(column);
                        owner.getComponentPeer(column.getParent()).remove(ownerPeer);

                        DeleteEdit delEdit = new DeleteEdit(ownerPeer, oldColumnIndex);
                        edit.addEdit(delEdit);

                        tableEdit = new TablePropertyEdit(painter.getTablePeer());
                        edit.addEdit(tableEdit);
                        owner.addEdit(edit);
                    }
                } else {
                    Cell oldCell = column.getCell();
                    Cell newCell = null;
                    newCell = StaticPanelFactory.getNewCell_X(tt, oldCell, startPoint, end_x,
                            static_x);

                    if (tt.getBounds(newCell).width > tt.getBounds(oldCell).width) {
                        Component parent = column.getParent();
                        int count = parent.getChildCount();
                        ArrayList brothers = parent.getChildren();
                        ArrayList otherChild = new ArrayList();

                        for (int i = 0; i < count; i++) {
                            Component c = (Component) brothers.get(i);

                            if (!c.equals(column)) {
                                if (newCell.contains(c.getCell())) {
                                    otherChild.add(c);
                                }
                            }
                        }

                        for (int i = 0; i < otherChild.size(); i++) {
                            Component c = (Component) otherChild.get(i);
                            ComponentPeer peer = owner.getComponentPeer(c);
                            int oldIndex = peer.getParent().indexOf(peer);
                            ComponentPeer oldParentPeer = peer.getParent();

                            oldParentPeer.remove(peer);
                            owner.getComponentPeer(column).add(peer);

                            ParentChangeEdit parentChangeEdit1 = new ParentChangeEdit(peer,
                                    oldParentPeer, oldIndex);

                            edit.addEdit(parentChangeEdit1);
                        }
                    } else {
                        ArrayList children = column.getChildren();
                        ArrayList otherChild = new ArrayList();

                        for (int i = 0; i < children.size(); i++) {
                            Component c = (Component) children.get(i);

                            if (!newCell.contains(c.getCell())) {
                                otherChild.add(c);
                            }
                        }

                        for (int i = 0; i < otherChild.size(); i++) {
                            Component c = (Component) otherChild.get(i);

                            ComponentPeer peer = owner.getComponentPeer(c);
                            int oldIndex = peer.getParent().indexOf(peer);
                            ComponentPeer oldParentPeer = peer.getParent();

                            oldParentPeer.remove(peer);
                            owner.getComponentPeer(column.getParent()).add(peer);

                            ParentChangeEdit parentChangeEdit1 = new ParentChangeEdit(peer,
                                    oldParentPeer, oldIndex);
                            edit.addEdit(parentChangeEdit1);
                        }
                    }

                    column.setCell(newCell);

                    PropertyEdit cellEdit = new PropertyEdit(owner.getComponentPeer(column),
                            ComponentConstants.PROPERTY_CELL, oldCell, newCell);
                    edit.addEdit(cellEdit);

                    tableEdit = new TablePropertyEdit(painter.getTablePeer());
                    edit.addEdit(tableEdit);
                    owner.addEdit(edit);
                }
            }

            if (this.rubber.active) {
                updateFillRowPanel((FillRowPanel) this.selectedHandler.c,
                    this.rubber.getCurrentCell());
            }

            this.rubber.active = false;
            vertResizeBar = null;
            horzResizeBar = null;
            tt.validate();
            _repaint(painter.getTablePeer());
        }
    }

    private boolean createFillRowPanel(Cell newCell, CompoundEdit edit) {
        TablePeer tablePeer = painter.getTablePeer();
        ReportPanel _owner = tablePeer.getOwner();

        List<FillRowPanel> panels = getFillRowPanels((TableBase) tablePeer.getComponent(),
                newCell.toRect());

        if (!panels.isEmpty()) {
            if (MessageBox.show(_owner, "提示", "选择区域已经存在填充板,是否先删除?", MessageBox.OK_CANCEL) == MessageBox.CANCEL) {
                return false;
            }
        }

        deleteFillRowPanel(tablePeer, edit, panels);

        TableBase table = (TableBase) tablePeer.getComponent();

        CellStore cellstore = table.getCellstore();
        cellstore.getComponent(0, 0);

        TablePropertyListener lst = new TablePropertyListener(tablePeer);

        FillRowPanel fp = new FillRowPanel();

        Cell cell = (Cell) newCell.clone();

        fp.setCell(cell);

        Component p = table.getPanelstore().getComponentOver(cell.row, cell.column);
        p.add(fp);

        PropertyChangeEvent e = new PropertyChangeEvent(p, ComponentConstants.PROPERTY_ADD, fp, null);

        lst.propertyChange(e);

        for (int row = cell.row; row <= cell.row2(); row++) {
            for (int col = cell.column; col <= cell.column2(); col++) {
                Component child = cellstore.clear(row, col);

                if ((child == null) || (child.getParent() == fp)) {
                    continue;
                }

                p = child.getParent();
                p.remove(child);

                fp.add(child);

                e = new PropertyChangeEvent(fp, ComponentConstants.PROPERTY_PARENT_CHANGE, child, p);

                lst.propertyChange(e);
            }
        }

        lst.applyEdit(edit);

        return true;
    }

    private boolean deleteFillRowPanel(Cell newCell, CompoundEdit edit) {
        TablePeer tablePeer = painter.getTablePeer();

        List<FillRowPanel> panels = getFillRowPanels((TableBase) tablePeer.getComponent(),
                newCell.toRect());

        if (panels.isEmpty()) {
            return false;
        }

        deleteFillRowPanel(tablePeer, edit, panels);

        return true;
    }

    private void updateFillRowPanel(FillRowPanel column, Cell newCell) {
        if (!newCell.isEmpty()) {
            Rectangle sel = new Rectangle(newCell.column, newCell.row, newCell.colSpan,
                    newCell.rowSpan);

            if (painter.getTablePeer().isMergedCellCut(sel)) {
                MessageBox.error(painter.getTablePeer().getOwner(), "填充板区域不能覆盖部分合并单元格!");

                return;
            }
        }

        CompoundEdit edit = new CompoundEdit();

        TablePeer tablePeer = this.painter.getTablePeer();

        if (updateFillRowPanel(tablePeer, column, newCell, edit)) {
            edit.addEdit(new TablePropertyEdit(tablePeer));
            tablePeer.getOwner().addEdit(edit);
            _repaint(tablePeer);
        }
    }

    private boolean updateFillRowPanel(TablePeer table, FillRowPanel fp, Cell newCell,
        CompoundEdit edit) {
        ReportPanel _owner = table.getOwner();

        TablePropertyEdit tableEdit = new TablePropertyEdit(table);
        edit.addEdit(tableEdit);

        if (newCell.isEmpty()) {
            List<FillRowPanel> panels = new ArrayList<FillRowPanel>();
            panels.add(fp);
            deleteFillRowPanel(table, edit, panels);
        } else {
            List<FillRowPanel> panels = getFillRowPanels((TableBase) table.getComponent(),
                    newCell.toRect());
            panels.remove(fp);

            if (!panels.isEmpty()) {
                if (MessageBox.show(_owner, "提示", "选择区域已经存在填充板,是否先删除?", MessageBox.OK_CANCEL) == MessageBox.CANCEL) {
                    return false;
                }
            }

            deleteFillRowPanel(table, edit, panels);

            TableBase t = (TableBase) table.getComponent();

            CellStore cellstore = t.getCellstore();
            cellstore.getComponent(0, 0);

            TablePropertyListener lst = new TablePropertyListener(table);
            Cell cell = newCell;

            for (int row = cell.row; row <= cell.row2(); row++) {
                for (int col = cell.column; col <= cell.column2(); col++) {
                    Component child = cellstore.clear(row, col);

                    if ((child == null) || (child.getParent() == fp)) {
                        continue;
                    }

                    Component p = child.getParent();
                    p.remove(child);

                    fp.add(child);

                    PropertyChangeEvent e = new PropertyChangeEvent(fp,
                            ComponentConstants.PROPERTY_PARENT_CHANGE, child, p);

                    lst.propertyChange(e);
                }
            }

            Cell oldCell = fp.getCell();
            newCell = (Cell) newCell.clone();
            fp.setCell(newCell);

            lst.propertyChange(new PropertyChangeEvent(fp, ComponentConstants.PROPERTY_CELL,
                    oldCell, newCell));

            lst.applyEdit(edit);
        }

        tableEdit = new TablePropertyEdit(table);
        edit.addEdit(tableEdit);
        table.setSelection(newCell.toRect());

        return true;
    }

    private List<FillRowPanel> getFillRowPanels(TableBase table, Rectangle sel) {
        CellStore store = table.getCellstore();
        List<FillRowPanel> result = new ArrayList<FillRowPanel>();

        for (int row = sel.y; row < (sel.height + sel.y); row++) {
            for (int col = sel.x; col < (sel.width + sel.x); col++) {
                Component c = store.getComponent(row, col);

                if (c != null) {
                    while ((c = c.getParent()) != null) {
                        if (c instanceof FillRowPanel) {
                            if (!result.contains(c)) {
                                result.add((FillRowPanel) c);
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    private void deleteFillRowPanel(TablePeer table, CompoundEdit edit, List<FillRowPanel> panels) {
        TableBase t = (TableBase) table.getComponent();

        CellStore cellstore = t.getCellstore();
        cellstore.getComponent(0, 0);

        PanelStore panelstore = t.getPanelstore();
        panelstore.excludeInlines();
        panelstore.getComponent(0, 0);

        TablePropertyListener lst = new TablePropertyListener(table);

        for (FillRowPanel fp : panels) {
            Cell cell = fp.getCell();

            for (int row = cell.row; row <= cell.row2(); row++) {
                for (int col = cell.column; col <= cell.column2(); col++) {
                    Component child = cellstore.clear(row, col);

                    if (child == null) {
                        continue;
                    }

                    fp.remove(child);

                    Component p = panelstore.getComponentOver(row, col);

                    if (p == null) {
                        p = t;
                    }

                    p.add(child);

                    PropertyChangeEvent e = new PropertyChangeEvent(p,
                            ComponentConstants.PROPERTY_PARENT_CHANGE, child, fp);

                    lst.propertyChange(e);
                }
            }

            Component p = fp.getParent();
            p.remove(fp);

            PropertyChangeEvent e = new PropertyChangeEvent(p, ComponentConstants.PROPERTY_DELETE,
                    fp, null);
            lst.propertyChange(e);
        }

        lst.applyEdit(edit);
    }

    void _repaint(TablePeer tablePeer) {
        tablePeer.setSelection(tablePeer.getSelection(), false);
        tablePeer.getComponent().invalid();
        tablePeer.getOwner().repaint();
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
        if (painter.focused instanceof NodePathTarget) {
            PropertyDescriptor[] props = painter.focused.getRegistrableProperties();
            boolean path = false;

            if (props != null) {
                for (int i = 0; i < props.length; i++) {
                    if (props[i] == ComponentConstants._NODE_PATH) {
                        path = true;

                        break;
                    }
                }
            }

            if (path) {
                NodePathPropertyEditor editor = new NodePathPropertyEditor();
                String oldVal = ((NodePathTarget) painter.focused).getNodePath();

                if (editor.showChooser((JComponent) Main.getInstance().getContentPane(), oldVal)) {
                    String newVal = (String) editor.getValue();

                    ((NodePathTarget) painter.focused).setNodePath(newVal);

                    this.owner.addEdit(new PropertyEdit(this.owner.getComponentPeer(painter.focused),
                            ComponentConstants.PROPERTY_NODE_PATH, oldVal, newVal));
                }
            }
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
        if ((e.getModifiers() & (KeyEvent.ALT_MASK | KeyEvent.SHIFT_MASK | KeyEvent.CTRL_MASK)) != 0) {
            return false;
        }

        switch (e.getKeyChar()) {
        case KeyEvent.VK_ENTER:
            owner.repaint();

            break;

        case KeyEvent.VK_ESCAPE:
        case KeyEvent.VK_DELETE:
            break;

        default:
            e.consume();

            break;
        }

        return true;
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
    public void pressed(int modifier, int x, int y) {
        hot = painter.hitTable(x, y);

        Iterator it = painter.iterator();

        if (it != null) {
            while (it.hasNext()) {
                Handler2 handler = (Handler2) it.next();

                if (handler.hit(x, y)) {
                    selectedHandler = handler;
                    painter.focused = handler.c;
                }
            }
        }

        JPopupMenu ppp = getPopupMenu();

        if ((MouseEvent.META_MASK & modifier) != 0) {
            JComponent panel = owner.getGroupPanel();

            if (pop.getComponentIndex(deleteAllItem) == -1) {
                pop.add(deleteAllItem);
            }

            if ((painter.focused != null) && (painter.focused instanceof Panel)) {
                ArrayList v = StaticPanelFactory.getChildPanels((Panel) painter.focused);

                if ((v == null) || v.isEmpty()) {
                    deleteAllItem.setEnabled(false);
                } else if ((v != null) && !v.isEmpty()) {
                    deleteAllItem.setEnabled(true);
                }
            }

            if (panel != null) {
                ppp.show(panel, x + start.x + 5, y + start.y + 5);
            }
        } else {
            if (ppp.isVisible()) {
                ppp.setVisible(false);
            }
        }

        owner.repaint();
    }

    /**
     * DOCUMENT ME!
     */
    public void sleep() {
        super.sleep();

        if ((pop != null) && pop.isVisible()) {
            pop.setVisible(false);
        }
    }

    protected JPopupMenu getPopupMenu() {
        if (pop == null) {
            pop = new JPopupMenu();

            _PopAction a = new _PopAction(App.messages.getString("res.429"));
            a.putValue(Action.ACTION_COMMAND_KEY, INSERT_LIST_BY_SELECT);
            pop.add(a);

            a = new _PopAction(App.messages.getString("res.430"));
            a.putValue(Action.ACTION_COMMAND_KEY, DELETE_LIST);
            pop.add(a);

            deleteAll.putValue(Action.ACTION_COMMAND_KEY, DELETE_LIST_ALL);
            deleteAllItem = new JMenuItem(deleteAll);
            pop.add(deleteAllItem);
        }

        return pop;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasPopupMenu() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c
     *            DOCUMENT ME!
     * @param edit
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean deletePanel(Component c, GroupEdit edit) {
        if (c instanceof TableBase) {
            return false;
        }

        TablePeer tablePeer = this.painter.getTablePeer();
        TableBase t = (TableBase) tablePeer.getComponent();

        TablePropertyListener lst = new TablePropertyListener(tablePeer);

        Component[] removed = (Component[]) c.getChildren().toArray(new Component[0]);

        for (int i = 0; i < removed.length; i++) {
            Component child = removed[i];
            c.remove(child);
            c.getParent().add(child);

            PropertyChangeEvent e = new PropertyChangeEvent(c.getParent(),
                    ComponentConstants.PROPERTY_PARENT_CHANGE, child, c);

            lst.propertyChange(e);

            removed[i] = child;
        }

        Component p = c.getParent();
        p.remove(c);

        PropertyChangeEvent e = new PropertyChangeEvent(p, ComponentConstants.PROPERTY_DELETE, c,
                null);
        lst.propertyChange(e);
        lst.applyEdit(edit);

        return true;
    }

    private boolean deletePanelAll(Component c, GroupEdit edit) {
        ArrayList cv = new ArrayList();
        ArrayList v = c.getChildren();
        Iterator vIt = v.iterator();

        while (vIt.hasNext()) {
            cv.add(vIt.next());
        }

        boolean b = deletePanel(c, edit);

        if (b) {
            Iterator it = cv.iterator();

            while (it.hasNext()) {
                Component cc = (Component) it.next();

                if (cc instanceof Panel) {
                    boolean bb = deletePanelAll(cc, edit);

                    if (!bb) {
                        System.err.println("error to delete panel!" + cc.toString());

                        break;
                    }
                }
            }
        }

        return b;
    }

    private boolean insertPanelBySelection(GroupEdit edit) {
        TablePeer tablePeer = this.painter.getTablePeer();
        Table t = hot;

        Rectangle sel = tablePeer.getSelection();

        Cell cell = null;

        if (t.isRightFlow()) {
            cell = new Cell(0, sel.x, sel.width, 1);
        } else {
            cell = new Cell(sel.y, 0, 1, sel.height);
        }

        Component parent = this.getParentCandidateForSelecton(t, cell, !t.isRightFlow());

        if (parent == null) {
            return false;
        }

        Component newlist = null;

        if (t.isRightFlow()) {
            newlist = new ColumnPanel();
            newlist.setCell(new Cell(0, sel.x, sel.width, t.getRowCount()));
        } else {
            newlist = new RowPanel();
            newlist.setCell(new Cell(sel.y, 0, t.getColumnCount(), sel.height));
        }

        TablePropertyListener lst = new TablePropertyListener(tablePeer);

        parent.add(newlist);

        PropertyChangeEvent e = new PropertyChangeEvent(parent, ComponentConstants.PROPERTY_ADD,
                newlist, null);

        lst.propertyChange(e);

        for (int i = parent.getChildCount() - 1; i >= 0; i--) {
            Component child = parent.getChild(i);

            if ((child != newlist) &&
                    newlist.getCell().contains(child.getCell().row, child.getCell().column)) {
                parent.remove(child);
                newlist.add(child);

                e = new PropertyChangeEvent(newlist, ComponentConstants.PROPERTY_PARENT_CHANGE,
                        child, parent);

                lst.propertyChange(e);
            }
        }

        lst.applyEdit(edit);

        this.painter.focused = newlist;

        return true;
    }

    private Component getParentCandidateForSelecton(Component c, Cell sel, boolean rowlist) {
        Component candidate = c;

        for (int i = 0; i < c.getChildCount(); i++) {
            Component child = c.getChild(i);

            if (!(child instanceof GridComponent)) {
                continue;
            }

            Cell cell = (Cell) child.getCell().clone();

            if (rowlist) {
                cell.column = 0;
                cell.colSpan = 1;

                if (cell.contains(sel)) {
                    c = getParentCandidateForSelecton(child, sel, rowlist);

                    if (c != null) {
                        return c;
                    } else {
                        return candidate;
                    }
                } else if (sel.contains(cell)) {
                } else if ((sel.row > cell.row2()) || (cell.row > sel.row2())) {
                } else {
                    return null;
                }
            } else {
                cell.row = 0;
                cell.rowSpan = 1;

                if (cell.contains(sel)) {
                    c = getParentCandidateForSelecton(child, sel, rowlist);

                    if (c != null) {
                        return c;
                    } else {
                        return candidate;
                    }
                } else if (sel.contains(cell)) {
                } else if ((sel.column > cell.column2()) || (cell.column > sel.column2())) {
                } else {
                    return null;
                }
            }
        }

        return c;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean request(int x, int y) {
        if (painter != null) {
            Iterator it = painter.iterator();

            if (it != null) {
                while (it.hasNext()) {
                    Handler2 handler = (Handler2) it.next();

                    if (handler.hit(x, y)) {
                        this.requestingFocus = true;

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
     * @return DOCUMENT ME!
     */
    public Action[] getActions() {
        _PopAction a = new _PopAction("新增填充行");
        a.putValue(Action.ACTION_COMMAND_KEY, CREATE_FILL_ROW);

        _PopAction b = new _PopAction("删除填充行");
        b.putValue(Action.ACTION_COMMAND_KEY, DELETE_FILL_ROW);

        return new Action[] { a, b };
    }

    class RubberData extends RubberRect {
        int hType; // 1/-1 = 向右/向左;
        int vType; // 1/-1 = 向下/向上
        int row;
        int column;
        Cell srcCell;
        Rectangle rect;
        Cell validCell;
        Cell currentCell;
        boolean active;

        Cell getCurrentCell() {
            this.getRect();

            return currentCell;
        }

        void activate(Cell srcCell, Cell validCell, int vType, int hType) {
            this.srcCell = srcCell;
            this.validCell = validCell;
            this.vType = vType;
            this.hType = hType;

            this.active = true;
            this.row = this.column = -100;
        }

        void setRow1(int r1) {
            int r;

            if ((r1 < validCell.row) && (vType == DOWN)) {
                r = validCell.row - 1;
            } else if ((r1 > validCell.row2()) && (vType == UP)) {
                r = validCell.row2() + 1;
            } else {
                r = Math.max(validCell.row, Math.min(r1, validCell.row2()));
            }

            if (r != this.row) {
                this.row = r;
                this.rect = null;
            }
        }

        void setColumn1(int c1) {
            int c;

            if ((c1 < validCell.column) && (hType == RIGHT)) {
                c = validCell.column - 1;
            } else if ((c1 > validCell.column2()) && (hType == LEFT)) {
                c = validCell.column2() + 1;
            } else {
                c = Math.max(validCell.column, Math.min(c1, validCell.column2()));
            }

            if (c != this.column) {
                this.column = c;
                this.rect = null;
            }
        }

        protected Rectangle getRect() {
            if (this.rect == null) {
                Cell cell = (Cell) srcCell.clone();

                if (this.vType == DOWN) {
                    cell.rowSpan = row - cell.row + 1;
                } else if (this.vType == UP) {
                    int h = cell.row2() - row + 1;
                    cell.rowSpan = h;
                    cell.row = row;
                }

                if (this.hType == RIGHT) {
                    cell.colSpan = column - cell.column + 1;
                } else if (this.hType == LEFT) {
                    int w = cell.column2() - column + 1;
                    cell.colSpan = w;
                    cell.column = column;
                }

                TableBase t = (TableBase) painter.getTablePeer().getComponent();

                this.rect = t.getBounds(cell);

                if (this.rect.height == 0) {
                    this.rect.height = 2;
                } else if (this.rect.width == 0) {
                    this.rect.width = 2;
                }

                this.currentCell = cell;
            }

            return this.rect;
        }
    }

    class _PopAction extends AbstractAction {
        _PopAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            boolean done = false;
            GroupEdit edit = new GroupEdit();
            TablePeer tablePeer = painter.getTablePeer();

            if (e.getActionCommand() == DELETE_LIST) {
                if (painter.focused != null) {
                    done = deletePanel(painter.focused, edit);
                }
            } else if (e.getActionCommand() == INSERT_LIST_BY_SELECT) {
                done = insertPanelBySelection(edit);
            } else if (e.getActionCommand() == DELETE_LIST_ALL) {
                if (painter.focused != null) {
                    done = deletePanelAll(painter.focused, edit);
                }
            } else if (e.getActionCommand() == CREATE_FILL_ROW) {
                done = createFillRowPanel(new Cell(tablePeer.getSelection()), edit);
            } else if (e.getActionCommand() == DELETE_FILL_ROW) {
                done = deleteFillRowPanel(new Cell(tablePeer.getSelection()), edit);
            }

            if (done) {
                edit.addEdit(new TablePropertyEdit(tablePeer));
                tablePeer.getOwner().addEdit(edit);
                _repaint(tablePeer);
            }
        }
    }

    // class _Rubber extends Rect
    class _Line {
        int orientation;
        int startPos;
        int stopedPos;
        int staticPos;
        boolean ready = false;

        _Line(int orientation, int startPos, int stopedPos, int staticPos, boolean ready) {
            this.orientation = orientation;
            this.startPos = startPos;
            this.stopedPos = stopedPos;
            this.ready = ready;
            this.staticPos = staticPos;
        }

        public void paint(Graphics2D g2) {
            int x0 = 0;
            int x1 = 0;
            int y0 = 0;
            int y1 = 0;

            if (orientation == VERTICAL) {
                x0 = stopedPos;
                x1 = x0;
                y0 = 0;
                y1 = owner.getHeight();
            } else {
                x0 = 0;
                x1 = owner.getWidth();
                y0 = stopedPos;
                y1 = y0;
            }

            g2.setXORMode(Color.white);
            g2.setStroke(RESIZE_STROKE);
            g2.setColor(Color.DARK_GRAY);

            Line2D line = new Line2D.Float(x0, y0, x1, y1);
            g2.draw(line);
            g2.setPaintMode();
        }
    }
}
