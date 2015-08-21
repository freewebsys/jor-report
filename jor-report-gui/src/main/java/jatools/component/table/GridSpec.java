package jatools.component.table;

import jatools.component.Size;
import jatools.designer.App;

import java.awt.Rectangle;
import java.util.Arrays;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class GridSpec {
    private final Size rows = new Size();
    private final Size columns = new Size();

    /**
    * DOCUMENT ME!
    *
    * @param column DOCUMENT ME!
    * @param width DOCUMENT ME!
    */
    public void setColumnWidth(int column, int width) {
        if ((column < 0) || (column >= columns.length())) {
            throw new IllegalArgumentException(App.messages.getString("res.638") + column);
        }

        columns.setSize(column, width);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRowCount() {
        return this.rows.length();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumnCount() {
        return this.columns.length();
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public void setRowHeight(int row, int height) {
        if ((row < 0) || (row >= rows.length())) {
            throw new IllegalArgumentException(App.messages.getString("res.639") + row);
        }

        rows.setSize(row, height);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRowHeight(int row) {
        if ((row < 0) || (row >= rows.length())) {
            throw new IllegalArgumentException(App.messages.getString("res.639") + row);
        }

        return rows.getSize(row);
    }

    /**
     * DOCUMENT ME!
     *
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumnWidth(int column) {
        if ((column < 0) || (column >= columns.length())) {
            throw new IllegalArgumentException(App.messages.getString("res.638") + column);
        }

        return columns.getSize(column);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getTotalWidth() {
        return columns.getPosition(columns.length());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getTotalHeight() {
        return rows.getPosition(rows.length());
    }

    /**
     * DOCUMENT ME!
     *
     * @param col DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumnX(int col) {
        return columns.getPosition(col);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRowY(int row) {
        return rows.getPosition(row);
    }

    /**
     * DOCUMENT ME!
     *
     * @param cell DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Rectangle getBounds(Cell cell) {
        int x = this.getColumnX(cell.column);
        int y = this.getRowY(cell.row);

        int width = this.columns.getSize(cell.column, cell.colSpan);
        int height = this.rows.getSize(cell.row, cell.rowSpan);

        return new Rectangle(x, y, width, height);
    }

    /**
     * DOCUMENT ME!
     *
     * @param col DOCUMENT ME!
     * @param span DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWidth(int col, int span) {
        return this.columns.getSize(col, span);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param span DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHeight(int row, int span) {
        return this.rows.getSize(row, span);
    }

    //    public void setLayoutType(int layoutType) {

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     */
    public void setRowCount(int i) {
        int[] sizes = new int[i];
        Arrays.fill(sizes, 20);
        this.rows.setSizes(sizes);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     */
    public void setColumnCount(int i) {
        int[] sizes = new int[i];
        Arrays.fill(sizes, 20);
        this.columns.setSizes(sizes);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public void insertRow(int row, int height) {
        this.rows.insertEntries(row, 1, height);
    }

    /**
     * DOCUMENT ME!
     *
     * @param col DOCUMENT ME!
     * @param width DOCUMENT ME!
     */
    public void insertColumn(int col, int width) {
        this.columns.insertEntries(col, 1, width);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     */
    public void removeRow(int row) {
        this.rows.removeEntries(row, 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param col DOCUMENT ME!
     */
    public void removeColumn(int col) {
        this.columns.removeEntries(col, 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getRowHeights() {
        return this.rows.getSizes();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getColumnWidths() {
        return this.columns.getSizes();
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getY(int row) {
        return this.rows.getPosition(row);
    }

    /**
     * DOCUMENT ME!
     *
     * @param col DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getX(int col) {
        return this.columns.getPosition(col);
    }

    /**
     * DOCUMENT ME!
     *
     * @param rowHeights DOCUMENT ME!
     */
    public void setRowHeights(int[] rowHeights) {
        this.rows.setSizes(rowHeights);
    }

    /**
     * DOCUMENT ME!
     *
     * @param widths DOCUMENT ME!
     */
    public void setColumnsWidths(int[] widths) {
        this.columns.setSizes(widths);
    }
}
