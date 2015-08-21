/*
 *   Author: John.
 *
 *   杭州杰创软件   All Copyrights Reserved.
 */
package jatools.component.table;

import jatools.accessor.PropertyAccessor;
import jatools.accessor.PropertyDescriptor;

import jatools.designer.App;

import org.w3c.dom.Element;

import java.awt.Rectangle;


/**
 * @author java9
 */
public class Cell implements Comparable, PropertyAccessor, Cloneable {
    // public ZComponent component;

    /**
     * DOCUMENT ME!
     */
    public int row;

    /**
     * DOCUMENT ME!
     */
    public int column;

    /**
     * DOCUMENT ME!
     */
    public int colSpan = 1;

    /**
     * DOCUMENT ME!
     */
    public int rowSpan = 1;

    /**
     * Creates a new ZCell object.
     *
     * @param component
     *            DOCUMENT ME!
     * @param constraint
     *            DOCUMENT ME!
     */
    public Cell(int row, int col) {
        this.row = row;
        this.column = col;
    }

    /**
     * Creates a new ZCell object.
     *
     * @param row
     *            DOCUMENT ME!
     * @param col
     *            DOCUMENT ME!
     * @param width
     *            DOCUMENT ME!
     * @param height
     *            DOCUMENT ME!
     */
    public Cell(int row, int col, int width, int height) {
        this.row = row;
        this.column = col;
        this.colSpan = width;
        this.rowSpan = height;
    }

    /**
     * Creates a new ZCell object.
     *
     * @param component
     *            DOCUMENT ME!
     */
    public Cell() {
        this(-1, -1);
    }

    /**
     * Creates a new Cell object.
     *
     * @param r
     *            DOCUMENT ME!
     */
    public Cell(Rectangle r) {
        this.row = r.y;
        this.column = r.x;
        this.colSpan = r.width;
        this.rowSpan = r.height;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int row2() {
        return (row + this.rowSpan) - 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param cell
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(Cell cell) {
        return contains(cell.row, cell.column) && contains(cell.row2(), cell.column2());
    }

    /**
     * DOCUMENT ME!
     *
     * @param _row
     *            DOCUMENT ME!
     * @param _column
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(int _row, int _column) {
        return (_row >= this.row) && (_row < (this.row + this.rowSpan)) &&
        (_column >= this.column) && (_column < (this.column + this.colSpan));
    }

    /**
     * DOCUMENT ME!
     *
     * @param r0
     *            DOCUMENT ME!
     * @param r1
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean betweenRows(int r0, int r1) {
        return (this.row >= r0) && (((this.row + this.rowSpan) - 1) <= r1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param c0
     *            DOCUMENT ME!
     * @param c1
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean betweenColumns(int c0, int c1) {
        return (this.column >= c0) && (((this.column + this.colSpan) - 1) <= c1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param r0
     *            DOCUMENT ME!
     * @param r1
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean intersectRows(int r0, int r1) {
        return ((row >= r0) && (row <= r1)) ||
        ((((row + rowSpan) - 1) >= r0) && (((row + rowSpan) - 1) <= r1)) ||
        ((row < r0) && (((row + rowSpan) - 1) > r1));
    }

    /**
     * DOCUMENT ME!
     *
     * @param r0
     *            DOCUMENT ME!
     * @param r1
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean intersectColumns(int r0, int r1) {
        return ((column >= r0) && (column <= r1)) ||
        ((((column + colSpan) - 1) >= r0) && (((column + colSpan) - 1) <= r1)) ||
        ((column < r0) && (((column + colSpan) - 1) > r1));
    }

    /**
     * DOCUMENT ME!
     *
     * @param cell
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean intersect(Cell cell) {
        return intersectColumns(cell.column, (cell.colSpan + cell.column) - 1) ||
        intersectRows(cell.row, (cell.rowSpan + cell.row) - 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param cell DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Cell getIntersection(Cell cell) {
        int r0 = Math.max(this.row, cell.row);
        int r1 = Math.min(this.row2(), cell.row2());
        int c0 = Math.max(this.column, cell.column);
        int c1 = Math.min(this.column2(), cell.column2());

        return new Cell(r0, c0, c1 - c0 + 1, r1 - r0 + 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isMerged() {
        return (colSpan > 1) || (rowSpan > 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] {
            new PropertyDescriptor("Row", Integer.TYPE, //
                PropertyDescriptor.SERIALIZABLE), //
            new PropertyDescriptor("Col", Integer.TYPE, //
                PropertyDescriptor.SERIALIZABLE), //
            new PropertyDescriptor("RowSpan", Integer.TYPE, //
                PropertyDescriptor.SERIALIZABLE), //
            new PropertyDescriptor("ColSpan", Integer.TYPE, //
                PropertyDescriptor.SERIALIZABLE)
        };
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        try {
            Object cloned = super.clone();

            return cloned;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();

            return null;
        }
    }

    /**
     * 对比与另一个 ZCell 相等， 或其中的 component 成员与另一个 _Component 相等
     *
     * @param object
     *            另一个ZCell 或 _Component
     *
     * @return true/false 相等/不等
     */
    public boolean equals(Object object) {
        boolean equal = false;

        if (object instanceof Cell) {
            Cell that = (Cell) object;

            equal = ((that.row == row) && (that.column == column) && (that.colSpan == colSpan) &&
                (that.rowSpan == rowSpan));
        }

        return equal;
    }

    /**
     * DOCUMENT ME!
     *
     * @param o
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compareTo(Object o) {
        if (!(o instanceof Cell)) {
            throw new IllegalArgumentException(App.messages.getString("res.637")); //
        }

        Cell that = (Cell) o;

        if (row > that.row) {
            return 1;
        } else if (row < that.row) {
            return -1;
        } else {
            if (this.column > that.column) {
                return 1;
            } else if (this.column < that.column) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getCol() {
        return column;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     * @uml.property name="colSpan"
     */
    public int getColSpan() {
        return colSpan;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     * @uml.property name="row"
     */
    public int getRow() {
        return row;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     * @uml.property name="rowSpan"
     */
    public int getRowSpan() {
        return rowSpan;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "[r=" + row + ",c=" + column + ",h=" + rowSpan + ",w=" + colSpan + "]"; // //$NON-NLS-2$ //$NON-NLS-3$
    }

    /**
     * DOCUMENT ME!
     *
     * @param rowSpan
     *            DOCUMENT ME!
     * @uml.property name="rowSpan"
     */
    public void setRowSpan(int rowSpan) {
        this.rowSpan = rowSpan;
    }

    /**
     * DOCUMENT ME!
     *
     * @param row
     *            DOCUMENT ME!
     * @uml.property name="row"
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * DOCUMENT ME!
     *
     * @param colSpan
     *            DOCUMENT ME!
     * @uml.property name="colSpan"
     */
    public void setColSpan(int colSpan) {
        this.colSpan = colSpan;
    }

    /**
     * DOCUMENT ME!
     *
     * @param col
     *            DOCUMENT ME!
     */
    public void setCol(int col) {
        this.column = col;
    }

    /**
     * @return Returns the column.
     * @uml.property name="column"
     */
    public int getColumn() {
        return column;
    }

    /**
     * @param column
     *            The column to set.
     * @uml.property name="column"
     */
    public void setColumn(int column) {
        this.column = column;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * com.jatools.core.accessor.ZPropertyAccessor#onLoad(org.w3c.dom.Element)
     */

    /**
     * DOCUMENT ME!
     *
     * @param e
     *            DOCUMENT ME!
     */
    public void onLoad(Element e) {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEmpty() {
        return (colSpan < 1) || (rowSpan < 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param cell
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Cell copyTo(Cell cell) {
        cell.row = row;
        cell.column = column;
        cell.rowSpan = rowSpan;
        cell.colSpan = colSpan;

        return cell;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Rectangle toRect() {
        return new Rectangle(column, row, colSpan, rowSpan);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int column2() {
        return (column + this.colSpan) - 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param index
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toColumnId(int index) {
        String result = new String();
        int k = (--index) % 26;

        while ((index = index / 26) != 0) {
            result = String.valueOf((char) (k + 'A')) + result;
            k = index % 26;
        }

        result = String.valueOf((char) (k + 'A')) + result;

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toStringId() {
        return toColumnId(column + 1) + (this.row + 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param text
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Cell toCell(String text) {
        Cell cell = new Cell();

        String x = "";

        for (int i = text.length() - 1; i >= 0; i--) {
            if (Character.isDigit(text.charAt(i))) {
                x = text.charAt(i) + x;
            } else {
                break;
            }
        }

        if (x.length() > 0) {
            cell.row = (Integer.valueOf(x)).intValue() - 1;
        }

        x = text.substring(0, text.length() - x.length());

        if (x.length() > 0) {
            cell.column = toColumn(x) - 1;
        }

        return cell;
    }

    /**
     * DOCUMENT ME!
     *
     * @param strcol
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int toColumn(String strcol) {
        int result = 0;
        int carry = 1;

        for (int i = strcol.length() - 1; i >= 0; i--) {
            result += ((strcol.charAt(i) - 'A' + 1) * carry);
            carry *= 26;
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param row
     *            DOCUMENT ME!
     * @param col
     *            DOCUMENT ME!
     */
    public Cell translate(int row, int col) {
        this.row += row;
        this.column += col;

        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Cell moveTo(int r, int c) {
        this.row = r;
        this.column = c;

        return this;
    }
}
