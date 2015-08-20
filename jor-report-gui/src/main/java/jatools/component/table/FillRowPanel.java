package jatools.component.table;

import jatools.accessor.PropertyDescriptor;

import jatools.component.Component;
import jatools.component.ComponentConstants;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class FillRowPanel extends RowPanel {
    private String[] groupNames;
    private int[] groupColumns;
    private int limitRows;
    private int lastFilledRow;
    private boolean printContinued=true;

    /**
     * Creates a new HeaderTable object.
     */
    public FillRowPanel() {
        //	this.gridSpec = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLastFilledRow() {
        return lastFilledRow;
    }

    /**
     * DOCUMENT ME!
     *
     * @param lastFilledRow DOCUMENT ME!
     */
    public void setLastFilledRow(int lastFilledRow) {
        this.lastFilledRow = lastFilledRow;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isPrintContinued() {
        return printContinued;
    }

    /**
     * DOCUMENT ME!
     *
     * @param printContinued DOCUMENT ME!
     */
    public void setPrintContinued(boolean printContinued) {
        this.printContinued = printContinued;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLimitRows() {
        return limitRows;
    }

    /**
     * DOCUMENT ME!
     *
     * @param maxRows DOCUMENT ME!
     */
    public void setLimitRows(int maxRows) {
        this.limitRows = maxRows;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] {
            ComponentConstants._NAME,
            
            ComponentConstants._TYPE,
            
            ComponentConstants._Y, //
            ComponentConstants._X, //
            ComponentConstants._WIDTH, //
            ComponentConstants._HEIGHT, //
            ComponentConstants._CELL, ComponentConstants._PRINT_STYLE,
            ComponentConstants._LIMIT_ROWS, ComponentConstants._PRINT_CONTINUED,
            ComponentConstants._CHILDREN, ComponentConstants._NODE_PATH,
            ComponentConstants._INIT_PRINT,
            ComponentConstants._AFTERPRINT, ComponentConstants._BEFOREPRINT2
        };
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumnCount() {
        return getCell().colSpan;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRowCount() {
        return getCell().rowSpan;
    }

    /**
     * DOCUMENT ME!
     *
     * @param from
     *            DOCUMENT ME!
     * @param to
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRowHeight(int from, int to) {
        throw new UnsupportedOperationException("不支持该操作,HeaderTable.getRowHeight");
    }

    /**
     * DOCUMENT ME!
     *
     * @param row
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRowHeight(int row) {
        throw new UnsupportedOperationException("不支持该操作,HeaderTable.getRowHeight");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getRowHeights() {
        throw new UnsupportedOperationException("不支持该操作,HeaderTable.getRowHeights");
    }

    /**
     * DOCUMENT ME!
     *
     * @param row
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRowY(int row) {
        throw new UnsupportedOperationException("不支持该操作,HeaderTable.getRowHeights");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getTotalRowHeights() {
        throw new UnsupportedOperationException("不支持该操作,HeaderTable.getRowHeights");
    }

    /**
     * DOCUMENT ME!
     *
     * @param col
     *            DOCUMENT ME!
     * @param span
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWidth(int col, int span) {
        throw new UnsupportedOperationException("不支持该操作,HeaderTable.getRowHeights");
    }

    /**
     * DOCUMENT ME!
     *
     * @param child
     *            DOCUMENT ME!
     * @param index
     *            DOCUMENT ME!
     */
    public void insert(Component child, int index) {
        super.insert(child, index);
        notifyRootTable();
    }

    /**
     * DOCUMENT ME!
     *
     * @param child
     *            DOCUMENT ME!
     */
    public void remove(Component child) {
        super.remove(child);
        notifyRootTable();
    }

    protected void notifyRootTable() {
        TableBase g = this.getRootTable();

        if (g != null) {
            g.setDirty();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     */
    public void add(Component child) {
        super.add(child);

        notifyRootTable();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getGroupColumns() {
        return groupColumns;
    }

    /**
     * DOCUMENT ME!
     *
     * @param groupColumns DOCUMENT ME!
     */
    public void setGroupColumns(int[] groupColumns) {
        this.groupColumns = groupColumns;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String[] getGroupNames() {
        return groupNames;
    }

    /**
     * DOCUMENT ME!
     *
     * @param groupNames DOCUMENT ME!
     */
    public void setGroupNames(String[] groupNames) {
        this.groupNames = groupNames;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isInline() {
        return true;
    }
}
