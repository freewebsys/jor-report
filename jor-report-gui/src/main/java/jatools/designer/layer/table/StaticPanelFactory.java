package jatools.designer.layer.table;

import jatools.component.ColumnPanel;
import jatools.component.Component;
import jatools.component.Panel;

import jatools.component.table.Cell;
import jatools.component.table.HeaderTable;
import jatools.component.table.RowPanel;
import jatools.component.table.TableBase;

import java.awt.Point;
import java.awt.Rectangle;

import java.util.ArrayList;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class StaticPanelFactory {
    /**
     * DOCUMENT ME!
     *
     * @param panel DOCUMENT ME!
     * @param tt DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Panel getStaticColumnHeaderLeftBrother(Panel panel, TableBase tt) {
        Component c = panel.getParent();
        ArrayList v = c.getChildren();
        int column1 = 0;

        if (panel instanceof ColumnPanel) {
            ColumnPanel p = (ColumnPanel) panel;
            Cell cell = p.getCell();
            column1 = cell.getColumn();

            if (column1 == 0) {
                return null;
            } else {
                int _column2 = 0;
                Panel _panel = null;

                for (int i = 0; i < v.size(); i++) {
                    Component cc = (Component) v.get(i);

                    if (!cc.equals(panel) && cc instanceof ColumnPanel) {
                        if (cc.getCell().column2() <= column1) {
                            int _column2_ = cc.getCell().column2();

                            if (_column2_ >= _column2) {
                                _column2 = _column2_;
                                _panel = (Panel) cc;
                            }
                        }
                    }
                }

                return _panel;
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param panel DOCUMENT ME!
     * @param tt DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Panel getStaticColumnHeaderRightBrother(Panel panel, TableBase tt) {
        Component c = panel.getParent();
        ArrayList v = c.getChildren();
        int column2 = 0;

        if (panel instanceof ColumnPanel) {
            ColumnPanel p = (ColumnPanel) panel;
            Cell cell = p.getCell();
            column2 = cell.column2();

            if (column2 == tt.getColumnCount()) {
                return null;
            } else {
                int _column1 = tt.getColumnCount();
                Panel _panel = null;

                for (int i = 0; i < v.size(); i++) {
                    Component cc = (Component) v.get(i);

                    if (!cc.equals(panel) && cc instanceof ColumnPanel) {
                        if (cc.getCell().getColumn() >= column2) {
                            int _column1_ = cc.getCell().getColumn();

                            if (_column1_ <= _column1) {
                                _column1 = _column1_;
                                _panel = (Panel) cc;
                            }
                        }
                    }
                }

                return _panel;
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param panel DOCUMENT ME!
     * @param tt DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Panel getStaticRowHeaderTopBrother(Panel panel, TableBase tt) {
        Component c = panel.getParent();
        ArrayList v = c.getChildren();
        int row1 = 0;

        if (panel instanceof RowPanel) {
            RowPanel p = (RowPanel) panel;
            Cell cell = p.getCell();
            row1 = cell.getRow();

            if (row1 == 0) {
                return null;
            } else {
                int _row2 = 0;
                Panel _panel = null;

                for (int i = 0; i < v.size(); i++) {
                    Component cc = (Component) v.get(i);

                    if (!cc.equals(panel) && cc instanceof RowPanel) {
                        if (cc.getCell().row2() <= row1) {
                            int _row2_ = cc.getCell().row2();

                            if (_row2_ >= _row2) {
                                _row2 = _row2_;
                                _panel = (Panel) cc;
                            }
                        }
                    }
                }

                return _panel;
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param panel DOCUMENT ME!
     * @param tt DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Panel getStaticRowHeaderBottomBrother(Panel panel, TableBase tt) {
        Component c = panel.getParent();
        ArrayList v = c.getChildren();
        int row2 = 0;

        if (panel instanceof RowPanel) {
            RowPanel p = (RowPanel) panel;
            Cell cell = p.getCell();
            row2 = cell.row2();

            if (row2 == tt.getRowCount()) {
                return null;
            } else {
                int _row1 = tt.getRowCount();
                Panel _panel = null;

                for (int i = 0; i < v.size(); i++) {
                    Component cc = (Component) v.get(i);

                    if (!cc.equals(panel) && cc instanceof RowPanel) {
                        if (cc.getCell().getRow() >= row2) {
                            int _row1_ = cc.getCell().getRow();

                            if (_row1_ <= _row1) {
                                _row1 = _row1_;
                                _panel = (Panel) cc;
                            }
                        }
                    }
                }

                return _panel;
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param panel DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Component getParentComponent(Panel panel) {
        Component c = panel.getParent();

        if (c == null) {
            return null;
        } else if (c instanceof RowPanel || c instanceof ColumnPanel || c instanceof HeaderTable) {
            return c;
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param panel DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ArrayList getChildPanels(Panel panel) {
        if (panel.getChildCount() == 0) {
            return null;
        } else {
            ArrayList v = new ArrayList();
            ArrayList children = panel.getChildren();

            for (int i = 0; i < children.size(); i++) {
                Component c = (Component) children.get(i);

                if (c instanceof RowPanel || c instanceof ColumnPanel) {
                    v.add(c);
                }
            }

            return v;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param tt DOCUMENT ME!
     * @param panel DOCUMENT ME!
     * @param downBrother DOCUMENT ME!
     * @param parent DOCUMENT ME!
     * @param child DOCUMENT ME!
     * @param spread DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getMaxY_To_Drag(TableBase tt, Panel panel, Panel downBrother,
        Component parent, ArrayList child, boolean spread) {
        int result = 0;

        if (spread) {
            int y = tt.getBounds().height;
            int parentY = (parent == null) ? y : getPanelPoint2(tt, parent).y;
            int downY = (downBrother == null) ? parentY : getPanelPoint1(tt, downBrother).y;
            result = downY;
        } else {
            int y = getPanelPoint2(tt, panel).y;

            if (child == null) {
                result = y;
            } else {
                int minY = y;

                for (int i = 0; i < child.size(); i++) {
                    Panel cp = (Panel) child.get(i);

                    if (getPanelPoint1(tt, cp).y < minY) {
                        minY = getPanelPoint1(tt, cp).y;
                    }
                }

                result = minY;
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tt DOCUMENT ME!
     * @param panel DOCUMENT ME!
     * @param topBrother DOCUMENT ME!
     * @param parent DOCUMENT ME!
     * @param child DOCUMENT ME!
     * @param spread DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getMinY_To_Drag(TableBase tt, Panel panel, Panel topBrother,
        Component parent, ArrayList child, boolean spread) {
        int result = 0;

        if (spread) {
            int y = 0;
            int parentY = ((parent == null) ? y : getPanelPoint1(tt, parent).y);
            int downY = (topBrother == null) ? parentY : getPanelPoint2(tt, topBrother).y;
            result = downY;
        } else {
            int y = getPanelPoint1(tt, panel).y;

            if (child == null) {
                result = y;
            } else {
                int maxY = y;

                for (int i = 0; i < child.size(); i++) {
                    Panel cp = (Panel) child.get(i);

                    if (getPanelPoint2(tt, cp).y > maxY) {
                        maxY = getPanelPoint2(tt, cp).y;
                    }
                }

                result = maxY;
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tt DOCUMENT ME!
     * @param panel DOCUMENT ME!
     * @param rightBrother DOCUMENT ME!
     * @param parent DOCUMENT ME!
     * @param child DOCUMENT ME!
     * @param spread DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getMaxX_To_Drag(TableBase tt, Panel panel, Panel rightBrother,
        Component parent, ArrayList child, boolean spread) {
        int result = 0;

        if (spread) {
            int x = tt.getBounds().height;
            int parentX = (parent == null) ? x : getPanelPoint2(tt, parent).x;
            int downX = (rightBrother == null) ? parentX : getPanelPoint1(tt, rightBrother).x;
            result = downX;
        } else {
            int x = getPanelPoint2(tt, panel).x;

            if (child == null) {
                result = x;
            } else {
                int minX = x;

                for (int i = 0; i < child.size(); i++) {
                    Panel cp = (Panel) child.get(i);

                    if (getPanelPoint1(tt, cp).x < minX) {
                        minX = getPanelPoint1(tt, cp).x;
                    }
                }

                result = minX;
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tt DOCUMENT ME!
     * @param panel DOCUMENT ME!
     * @param topBrother DOCUMENT ME!
     * @param parent DOCUMENT ME!
     * @param child DOCUMENT ME!
     * @param spread DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getMinX_To_Drag(TableBase tt, Panel panel, Panel topBrother,
        Component parent, ArrayList child, boolean spread) {
        int result = 0;

        if (spread) {
            int x = 0;
            int parentX = ((parent == null) ? x : getPanelPoint1(tt, parent).x);
            int downX = (topBrother == null) ? parentX : getPanelPoint2(tt, topBrother).x;
            result = downX;
        } else {
            int x = getPanelPoint1(tt, panel).x;

            if (child == null) {
                result = x;
            } else {
                int maxX = x;

                for (int i = 0; i < child.size(); i++) {
                    Panel cp = (Panel) child.get(i);

                    if (getPanelPoint2(tt, cp).x > maxX) {
                        maxX = getPanelPoint2(tt, cp).x;
                    }
                }

                result = maxX;
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tt DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getNearestRowValue(TableBase tt, int y) {
        if (y < (tt.getRowHeight(0) / 2)) {
            return 0;
        } else {
            int rowCount = tt.getRowCount();
            int value = 0;

            for (int i = 0; i < rowCount; i++) {
                int v = tt.getRowY(i) + tt.getRowHeight(i);

                if (v > y) {
                    return v;
                }
            }

            return value;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param tt DOCUMENT ME!
     * @param x DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getNearestColumnValue(TableBase tt, int x) {
        if (x < (tt.getColumnWidth(0) / 2)) {
            return 0;
        } else {
            int colCount = tt.getColumnCount();
            int value = 0;

            for (int i = 0; i < colCount; i++) {
                int v = tt.getColumnX(i) + tt.getColumnWidth(i);

                if (v > x) {
                    return v;
                }
            }

            return value;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param tt DOCUMENT ME!
     * @param panel DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Point getPanelPoint1(TableBase tt, Component panel) {
        Point p = null;
        Cell c = panel.getCell();

        if (c == null) {
            return new Point(0, 0);
        }

        p = tt.getBounds(c).getLocation();

        return p;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tt DOCUMENT ME!
     * @param panel DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Point getPanelPoint2(TableBase tt, Component panel) {
        Point p = new Point();
        Cell c = panel.getCell();

        if (c == null) {
            p.setLocation(panel.getBounds().width, panel.getBounds().height);
        } else {
            Point temp = tt.getBounds(c).getLocation();
            p.setLocation(temp.x + tt.getBounds(c).width, temp.y + tt.getBounds(c).height);
        }

        return p;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tt DOCUMENT ME!
     * @param panel DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Rectangle getPanelBounds(TableBase tt, Panel panel) {
        return tt.getBounds(panel.getCell());
    }

    /**
     * DOCUMENT ME!
     *
     * @param tt DOCUMENT ME!
     * @param oldCell DOCUMENT ME!
     * @param start DOCUMENT ME!
     * @param startY DOCUMENT ME!
     * @param endY DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Cell getNewCell(TableBase tt, Cell oldCell, Point start, int startY, int endY) {
        Cell cell = new Cell();
        oldCell.copyTo(cell);

        int rowCount = tt.getRowCount();
        int value = start.y;
        int newRowStart = 0;
        int newRowEnd = 0;

        int min = (startY < endY) ? startY : endY;
        int max = (startY < endY) ? endY : startY;

        for (int i = 0; i < rowCount; i++) {
            value = value + tt.getRowHeight(i);

            if (value == min) {
                newRowStart = i + 1;

                break;
            }
        }

        value = start.y;

        for (int i = 0; i < rowCount; i++) {
            value = value + tt.getRowHeight(i);

            if (value == max) {
                newRowEnd = i;

                break;
            }
        }

        cell.setRow(newRowStart);
        cell.setRowSpan(newRowEnd - newRowStart + 1);

        return cell;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tt DOCUMENT ME!
     * @param oldCell DOCUMENT ME!
     * @param start DOCUMENT ME!
     * @param startX DOCUMENT ME!
     * @param endX DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Cell getNewCell_X(TableBase tt, Cell oldCell, Point start, int startX, int endX) {
        Cell cell = new Cell();
        oldCell.copyTo(cell);

        int colCount = tt.getColumnCount();
        int value = start.x;
        int newColStart = 0;
        int newColEnd = 0;

        int min = (startX < endX) ? startX : endX;
        int max = (startX < endX) ? endX : startX;

        for (int i = 0; i < colCount; i++) {
            value = value + tt.getColumnWidth(i);

            if (value == min) {
                newColStart = i + 1;

                break;
            }
        }

        value = start.x;

        for (int i = 0; i < colCount; i++) {
            value = value + tt.getColumnWidth(i);

            if (value == max) {
                newColEnd = i;

                break;
            }
        }

        cell.setColumn(newColStart);
        cell.setColSpan(newColEnd - newColStart + 1);

        return cell;
    }
}
