package jatools.component;

import jatools.component.layout.LayoutManager;
import jatools.component.table.Cell;
import jatools.component.table.GridComponent;
import jatools.component.table.Table;
import jatools.component.table.TableBase;

import java.awt.Insets;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class ColumnPanel extends Panel implements GridComponent {
    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     */
    public void add(Component child) {
        super.add(child);

        notifyRootTable();
    }
    
    public boolean isInline()
    {
    	return false;
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
     * @return DOCUMENT ME!
     */
    public TableBase getRootTable() {
        Component c = this;

        while (c != null) {
            if (c instanceof TableBase && (c.getCell() == null)) {
                return (TableBase) c;
            }

            c = c.getParent();
        }

        return null;
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

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     */
    public void add(Component child, int row, int column) {
        add(child, row, column, 1, 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public void add(Component child, int row, int column, int width, int height) {
        child.setCell(new Cell(row, column, width, height));
        add(child);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param newheight DOCUMENT ME!
     */
    public void insertRow(int row, int newheight) {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     */
    public void removeRow(int row) {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public LayoutManager getLayout() {
        return Table.GRID_LAYOUT;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TableBase getNearestTable() {
        Component c = this;

        while (c != null) {
            if (c instanceof TableBase) {
                return (TableBase) c;
            }

            c = c.getParent();
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Insets getPadding() {
        return NULL_INSETS;
    }
}
