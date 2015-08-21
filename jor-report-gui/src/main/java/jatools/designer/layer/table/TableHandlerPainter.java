package jatools.designer.layer.table;

import jatools.component.ColumnPanel;
import jatools.component.Component;
import jatools.component.Label;

import jatools.component.painter.LabelPainter;

import jatools.component.table.Cell;
import jatools.component.table.GridComponent;
import jatools.component.table.PowerTable;
import jatools.component.table.RowPanel;
import jatools.component.table.Table;
import jatools.component.table.TableBase;

import jatools.core.view.Border;

import jatools.designer.peer.TablePeer;

import jatools.engine.printer.NodePathTarget;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D.Float;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class TableHandlerPainter {
    public static TableHandlerPainter SHARED = new TableHandlerPainter();
    private static Color SECTION_COLOR = new Color(240, 240, 240);
    private static LabelPainter labelPainter;
    private static final int staticRowHeaderWidth = 50;
    private List<Handler2> handlers;
    Component focused;
    private TablePeer tablePeer;
    private Rectangle area0;
    private Rectangle area1;
    private Table table0;
    private Table table1;
    private PathTip[] topTips;
    private PathTip[] bottomTips;
    private PathTip[] leftTips;
    private PathTip[] rightTips;
    private List inlineGrids;
    private boolean active = false;
    Point start;

    private void paintLeftHandler(Graphics2D g, Rectangle r, Component c, boolean first) {
        if (first) {
            g.setColor(SECTION_COLOR);
            g.fillRect(r.x - r.width - 1, r.y - 1, r.width + 1, r.height + 3);
        }

        int padding = first ? 0 : 2;
        Float[] lines = new Float[3];

        lines[0] = new Float(r.x - r.width, r.y + padding, r.x - r.width + 2, r.y + padding);
        lines[1] = new Float(r.x - r.width, (r.y + r.height) - padding, r.x - r.width + 2,
                (r.y + r.height) - padding);
        lines[2] = new Float(r.x - r.width, r.y + padding, r.x - r.width, (r.y + r.height) -
                padding);

        Handler2 handler = new Handler2(lines, c);
        Color color = (focused == c) ? Color.RED : new Color(0, 153, 0);

        g.setColor(color);
        handler.paint(g);

        handlers.add(handler);

        if ((focused == c) && (c instanceof RowPanel)) {
            Point p1 = new Point(r.x - r.width + 1, r.y + padding);
            MovePicker movePicker = new MovePicker(p1);
            movePicker.paint(g);

            Point p2 = new Point(r.x - r.width + 1, (r.y + r.height) - padding);
            movePicker = new MovePicker(p2);
            movePicker.paint(g);
        }
    }

    Iterator iterator() {
        if ((handlers != null) && !handlers.isEmpty()) {
            return handlers.iterator();
        } else {
            return null;
        }
    }

    private void paintInlineHandler(Graphics2D g2, Component c) {
        Cell cell = c.getCell();
        GridComponent gc = (GridComponent) c;
        TableBase t = gc.getRootTable();

        Rectangle r = new Rectangle();

        r.x = t.getColumnX(cell.column) - 4;
        r.width = 0;

        r.y = t.getRowY(cell.row);
        r.height = t.getRowHeight(cell.row, cell.row2());

        if (c instanceof NodePathTarget) {
            NodePathTarget tg = (NodePathTarget) c;

            // int i = topTips.length - level;
            //
            // if (topTips[i] == null) {
            // topTips[i] = new PathTip(cell.row, -r.width + r.x, r.y
            // + r.height, tg.getNodePath());
            // } else if (topTips[i].index > cell.row) {
            // if ((bottomTips[i] == null)
            // || (bottomTips[i].index < topTips[i].index)) {
            // bottomTips[i] = topTips[i];
            // }
            //
            // topTips[i] = new PathTip(cell.row, -r.width + r.x, r.y
            // + r.height, tg.getNodePath());
            // } else if ((bottomTips[i] == null)
            // || (bottomTips[i].index < cell.row)) {
            // bottomTips[i] = new PathTip(cell.row, -r.width + r.x, r.y
            // + r.height, tg.getNodePath());
            // }
        }

        paintLeftHandler(g2, r, c, false);
        r.x = t.getColumnX(cell.column2() + 1) + 2;
        r.width = 0;

        paintRightHandler(g2, r, c);
    }

    private void paintRightHandler(Graphics2D g, Rectangle r, Component c) {
        int padding = 2;
        Float[] lines = new Float[3];

        lines[0] = new Float(r.x + r.width, r.y + padding, r.x + r.width + 2, r.y + padding);
        lines[1] = new Float(r.x + r.width, (r.y + r.height) - padding, r.x - r.width + 2,
                (r.y + r.height) - padding);
        lines[2] = new Float(r.x + r.width + 2, r.y + padding, r.x + r.width + 2,
                (r.y + r.height) - padding);

        Handler2 handler = new Handler2(lines, c);
        Color color = (focused == c) ? Color.RED : new Color(0, 153, 0);

        g.setColor(color);
        handler.paint(g);

        handlers.add(handler);

        // // 提示拖动
        if (focused == c) {
            Point p1 = new Point(r.x - r.width + 1, r.y + padding);
            MovePicker movePicker = new MovePicker(p1);
            movePicker.paint(g);

            Point p2 = new Point(r.x - r.width + 1, (r.y + r.height) - padding);
            movePicker = new MovePicker(p2);
            movePicker.paint(g);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param peer
     *            DOCUMENT ME!
     * @param g2
     *            DOCUMENT ME!
     */
    public void paint(TablePeer peer, Graphics2D g2) {
        handlers = null;
        active = false;
        area0 = null;
        area1 = null;
        table0 = null;
        table1 = null;
        inlineGrids = null;

        if (!peer.isFocused() || !peer.isHandlerVisible()) {
            return;
        }

        handlers = new ArrayList();

        start = new Point();
        peer.getOwner().childPointAsScreenPoint(peer, start);

        Component tablebase = peer.getComponent();

        if (tablebase instanceof PowerTable) {
            PowerTable power = (PowerTable) tablebase;

            paint(power.getLeftHeader(), g2);
            paint(power.getTopHeader(), g2);
        } else if (tablebase instanceof Table) {
            paint((Table) tablebase, g2);
        }

        this.tablePeer = peer;
        this.active = true;
    }

    private void paint(Table drawing, Graphics2D g2) {
        int level = getLevels(drawing, 1);
        Cell cell = drawing.getCell();

        g2.translate(start.x, start.y);

        if (cell == null) {
            cell = new Cell(0, 0, drawing.getColumnCount(), drawing.getRowCount());
        }

        TableBase t = drawing.getRootTable();

        if (drawing.isRightFlow()) {
            leftTips = new PathTip[level];
            rightTips = new PathTip[level];

            paintTopHandler(g2, drawing, cell, level);
            g2.setColor(new Color(200, 200, 200));

            int base = -10;

            for (int i = leftTips.length - 1; i >= 0; i--) {
                PathTip tip = leftTips[i];

                if ((tip != null) && (tip.tip != null)) {
                }
            }

            int r = drawing.getRootTable().getWidth() + 10;
            base = -1;

            for (int i = 0; i < rightTips.length; i++) {
                PathTip tip = rightTips[i];

                if ((tip != null) && (tip.tip != null)) {
                    if (base == -1) {
                        base = tip.y;
                    }

                    g2.drawLine(r - 3, tip.y, r, tip.y);
                    g2.drawLine(r, tip.y, r + 10, base);
                    g2.drawLine(r + 10, base, r + 13, base);

                    g2.drawString(tip.tip, r + 16, base + 5);

                    base += 14;
                }
            }

            table1 = drawing;
        } else {
            table0 = drawing;
            topTips = new PathTip[level];
            bottomTips = new PathTip[level];

            paintLeftHandler(g2, drawing, cell, level);
            g2.setColor(new Color(200, 200, 200));

            int base = -10;

            for (int i = topTips.length - 1; i >= 0; i--) {
                PathTip tip = topTips[i];

                if ((tip != null) && (tip.tip != null)) {
                    tip.y = t.getRowY(tip.index);
                    g2.drawLine(tip.x, tip.y - 4, tip.x, base);
                    g2.drawLine(tip.x, base, 10, base);
                    g2.drawString(tip.tip, 13, base + 5);
                    base -= 14;
                }
            }

            base = drawing.getRootTable().getTotalRowHeights() + 10;

            for (int i = bottomTips.length - 1; i >= 0; i--) {
                PathTip tip = bottomTips[i];

                if ((tip != null) && (tip.tip != null)) {
                    g2.drawLine(tip.x, tip.y + 4, tip.x, base);
                    g2.drawLine(tip.x, base, 10, base);
                    g2.drawString(tip.tip, 13, base + 5);

                    base += 14;
                }
            }
        }

        if ((this.inlineGrids != null) && !this.inlineGrids.isEmpty()) {
            paintInlineHandlers(g2);
        }

        g2.translate(-start.x, -start.y);
    }

    private void paintInlineHandlers(Graphics2D g2) {
        Iterator it = this.inlineGrids.iterator();

        while (it.hasNext()) {
            paintInlineHandler(g2, (Component) it.next());
        }
    }

    private void paintCell(Graphics2D g, Label label, boolean autoAdd) {
        label.setFont(new Font("Arial", 0, 10));
        label.setHorizontalAlignment(Label.CENTER);
        labelPainter = new LabelPainter();

        if (label.equals(this.focused)) {
            label.setBackColor(Color.red);
        } else {
            label.setBackColor(new Color(192, 200, 192));
        }

        label.setBorder(new Border(1, Color.LIGHT_GRAY));
        labelPainter.paint(g, label);
        g.draw3DRect(label.getBounds().x, label.getBounds().y, label.getBounds().width + 1,
            label.getBounds().height + 1, true);
    }

    private void paintTopHandler(Graphics2D g2, Component c, Cell cell, int level) {
        GridComponent gc = (GridComponent) c;
        TableBase t = gc.getRootTable();

        Rectangle r = new Rectangle();

        r.y = -2;
        r.height = 6 * (level - 1);

        r.x = t.getColumnX(cell.column);
        r.width = t.getColumnWidth(cell.column, cell.column2());

        if (c instanceof NodePathTarget) {
            NodePathTarget tg = (NodePathTarget) c;

            int i = leftTips.length - level;
            int padding = (area1 == null) ? 0 : 2;

            if (rightTips[i] == null) {
                rightTips[i] = new PathTip(cell.column, (r.x + r.width) - padding, r.y - r.height,
                        tg.getNodePath());
            } else if (rightTips[i].index < cell.column) {
                if ((leftTips[i] == null) || (leftTips[i].index > rightTips[i].index)) {
                    leftTips[i] = rightTips[i];
                }

                rightTips[i] = new PathTip(cell.column, (r.x + r.width) - padding, r.y - r.height,
                        tg.getNodePath());
            } else if ((leftTips[i] == null) || (leftTips[i].index > cell.column)) {
                leftTips[i] = new PathTip(cell.column, (r.x + r.width) - padding, r.y - r.height,
                        tg.getNodePath());
            }
        }

        if (area1 == null) {
            area1 = (Rectangle) r.clone();
            area1.grow(1, 1);
            area1.y -= r.height;
            paintTopHandler(g2, r, c, true);
        } else {
            paintTopHandler(g2, r, c, false);
        }

        if (level > 1) {
            for (int i = 0; i < c.getChildCount(); i++) {
                Component child = c.getChild(i);

                if (child instanceof ColumnPanel) {
                    paintTopHandler(g2, child, child.getCell(), level - 1);
                }
            }
        }

        // System.out.println();
    }

    private void paintTopHandler(Graphics2D g, Rectangle r, Component c, boolean first) {
        g.setColor(SECTION_COLOR);
        g.fillRect(r.x - 1, r.y - r.height - 1, r.width + 3, r.height + 1);

        int padding = first ? 0 : 2;
        Float[] lines = new Float[3];
        lines[0] = new Float(r.x + padding, r.y - r.height, r.x + padding, r.y - r.height + 2);
        lines[1] = new Float((r.x + r.width) - padding, r.y - r.height, (r.x + r.width) - padding,
                r.y - r.height + 2);
        lines[2] = new Float(r.x + padding, r.y - r.height, (r.x + r.width) - padding,
                r.y - r.height);

        Handler2 handler = new Handler2(lines, c);
        Color color = (focused == c) ? Color.RED : new Color(0, 153, 0);

        g.setColor(color);
        handler.paint(g);
        handlers.add(handler);

        if ((focused == c) && (c instanceof ColumnPanel)) {
            Point p1 = new Point(r.x + padding, r.y - r.height + 3);
            MovePicker movePicker = new MovePicker(p1);
            movePicker.paint(g);

            Point p2 = new Point((r.x + r.width) - padding, r.y - r.height + 3);
            movePicker = new MovePicker(p2);
            movePicker.paint(g);
        }
    }

    private int getLevels(Component c, int level) {
        int maxLevel = ++level;

        for (int i = 0; i < c.getChildCount(); i++) {
            Component child = c.getChild(i);

            if (child instanceof GridComponent) {
                if (((GridComponent) child).isInline()) {
                    if (this.inlineGrids == null) {
                        this.inlineGrids = new ArrayList();
                    }

                    this.inlineGrids.add(child);
                } else {
                    int l = getLevels(child, level);

                    if (l > maxLevel) {
                        maxLevel = l;
                    }
                }
            }
        }

        if (maxLevel > level) {
            level = maxLevel;
        }

        return (level);
    }

    private void paintLeftHandler(Graphics2D g2, Component c, Cell cell, int level) {
        GridComponent gc = (GridComponent) c;
        TableBase t = gc.getRootTable();

        Rectangle r = new Rectangle();

        r.x = -2;
        r.width = 6 * (level - 1);

        r.y = t.getRowY(cell.row);
        r.height = t.getRowHeight(cell.row, cell.row2());

        if (c instanceof NodePathTarget) {
            NodePathTarget tg = (NodePathTarget) c;

            int i = topTips.length - level;

            if (topTips[i] == null) {
                topTips[i] = new PathTip(cell.row, -r.width + r.x, r.y + r.height, tg.getNodePath());
            } else if (topTips[i].index > cell.row) {
                if ((bottomTips[i] == null) || (bottomTips[i].index < topTips[i].index)) {
                    bottomTips[i] = topTips[i];
                }

                topTips[i] = new PathTip(cell.row, -r.width + r.x, r.y + r.height, tg.getNodePath());
            } else if ((bottomTips[i] == null) || (bottomTips[i].index < cell.row)) {
                bottomTips[i] = new PathTip(cell.row, -r.width + r.x, r.y + r.height,
                        tg.getNodePath());
            }
        }

        if (area0 == null) {
            area0 = (Rectangle) r.clone();
            area0.grow(1, 1);
            area0.x -= r.width;
            paintLeftHandler(g2, r, c, true);
        } else {
            paintLeftHandler(g2, r, c, false);
        }

        if (level > 1) {
            for (int i = 0; i < c.getChildCount(); i++) {
                Component child = c.getChild(i);

                if (child.getClass() == RowPanel.class) {
                    paintLeftHandler(g2, child, child.getCell(), level - 1);
                }
            }
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
     * @return DOCUMENT ME!
     */
    public boolean isActive() {
        return active;
    }

    /**
     * DOCUMENT ME!
     *
     * @param active
     *            DOCUMENT ME!
     */
    public void setActive(boolean active) {
        this.active = active;
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
    public Table hitTable(int x, int y) {
        if ((area0 != null) && area0.contains(x, y)) {
            return table0;
        } else if ((area1 != null) && area1.contains(x, y)) {
            return table1;
        } else {
            return null;
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
    public boolean hitStaticHeader(int x, int y) {
        if ((area0 != null) && area0.contains(x, y)) {
            return true;
        } else if ((area1 != null) && area1.contains(x, y)) {
            return true;
        } else {
            return false;
        }
    }

    class PathTip {
        int x;
        int y;
        String tip;
        int index;

        public PathTip(int index, int x, int y, String tip) {
            this.index = index;
            this.x = x;
            this.y = y;

            this.tip = tip;
        }
    }
}


class MovePicker {
    Point center;

    MovePicker(Point center) {
        this.center = center;
    }

    void paint(Graphics2D g2d) {
        g2d.setColor(Color.black);
        g2d.draw3DRect(center.x - 2, center.y - 2, 4, 4, true);
        g2d.setColor(Color.decode("#EEAD0E"));
        g2d.fill3DRect(center.x - 1, center.y - 1, 3, 3, true);
    }
}
