package jatools.component.table;

import jatools.component.Component;

import jatools.designer.App;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class HeaderTable extends Table {
    private String[] groupNames;
    private int[] groupColumns;

    /**
     * Creates a new HeaderTable object.
     */
    public HeaderTable() {
        this.gridSpec = null;
    }

    static HeaderTable create(Table table) {
        HeaderTable header = new HeaderTable();
        header.setChildren(table.getChildren());
        header.setNodePath(table.getNodePath());

        return header;
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
    * @param from DOCUMENT ME!
    * @param to DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public int getRowHeight(int from, int to) {
        throw new UnsupportedOperationException(App.messages.getString("res.640"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRowHeight(int row) {
        throw new UnsupportedOperationException(App.messages.getString("res.640"));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getRowHeights() {
        throw new UnsupportedOperationException(App.messages.getString("res.641"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRowY(int row) {
        throw new UnsupportedOperationException(App.messages.getString("res.641"));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getTotalRowHeights() {
        throw new UnsupportedOperationException(App.messages.getString("res.641"));
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
        throw new UnsupportedOperationException(App.messages.getString("res.641"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     * @param index DOCUMENT ME!
     */
    public void insert(Component child, int index) {
        super.insert(child, index);
        notifyRootTable();
    }

    /**
    * DOCUMENT ME!
    *
    * @param child DOCUMENT ME!
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
}
