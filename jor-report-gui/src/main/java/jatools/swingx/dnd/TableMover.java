package jatools.swingx.dnd;

import jatools.designer.App;

import java.awt.Component;

import javax.swing.JTable;


/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.3 $
 * @author $author$
 */
public class TableMover implements Moveable {
    JTable table;

    /**
     * Creates a new ZTableMover object.
     *
     * @param table DOCUMENT ME!
     */
    public TableMover(JTable table) {
        if (table == null) {
            throw new NullPointerException(App.messages.getString("res.40")); //
        }

        if (!(table.getModel() instanceof Selectable)) {
            throw new IllegalArgumentException(
                    App.messages.getString("res.41")); //
        }

        this.table = table;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canDrop(JatoolsDragSource obj) {
        return true;
    }

    /**
     * DOCUMENT ME!
     */
    public void complete(boolean whole) {
    }

    /**
     * DOCUMENT ME!
     */
    public void cancel() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getObject() {
        int row = table.getSelectedRow();

        return ((Selectable) table.getModel()).getSelection(row);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] getAllObject() {
        Object[] all = new Object[table.getRowCount()];

        for (int i = 0; i < all.length; i++) {
            all[i] = ((Selectable) table.getModel()).getSelection(i);
        }

        return all;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getSourceComponent() {
        return table;
    }

    /**
     * DOCUMENT ME!
     *
     * @param source DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean drop(JatoolsDragSource source) {
        source.complete(false);

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param source DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean dropAll(JatoolsDragSource source) {
        source.complete(true);

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getTargetComponent() {
        return table;
    }
}
