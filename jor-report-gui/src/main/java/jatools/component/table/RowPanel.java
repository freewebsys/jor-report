package jatools.component.table;

import jatools.accessor.PropertyDescriptor;

import jatools.component.Component;
import jatools.component.ComponentConstants;
import jatools.component.Panel;

import jatools.component.layout.LayoutManager;

import java.awt.Insets;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class RowPanel extends Panel implements GridComponent {
    //private boolean insertable;

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     */
    public void add(Component child) {
        super.add(child);

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
     * @return DOCUMENT ME!
     */
    public Insets getPadding() {
        return NULL_INSETS;
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
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] {
            ComponentConstants._NAME, //
            ComponentConstants._TYPE,
            
            ComponentConstants._Y, //
            ComponentConstants._X, //
            ComponentConstants._WIDTH, //
            ComponentConstants._HEIGHT, //
            ComponentConstants._CELL, ComponentConstants._PRINT_STYLE, ComponentConstants._CHILDREN,
            ComponentConstants._NODE_PATH,
            
            ComponentConstants._INIT_PRINT, ComponentConstants._AFTERPRINT,
            ComponentConstants._BEFOREPRINT2
        };
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

    //	public boolean isInsertable() {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isInline() {
        // TODO Auto-generated method stub
        return false;
    }
}
