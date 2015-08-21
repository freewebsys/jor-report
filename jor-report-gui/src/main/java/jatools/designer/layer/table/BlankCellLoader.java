package jatools.designer.layer.table;


import jatools.component.Component;
import jatools.component.Label;
import jatools.component.table.Cell;
import jatools.component.table.PanelStore;
import jatools.component.table.TableBase;
import jatools.designer.peer.ComponentPeer;
import jatools.designer.peer.ComponentPeerFactory;
import jatools.designer.peer.TablePeer;
import jatools.designer.undo.AddEdit;

import java.util.ArrayList;

import javax.swing.undo.CompoundEdit;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class BlankCellLoader {
    /**
     * DOCUMENT ME!
     *
     * @param peer DOCUMENT ME!
     * @param edit DOCUMENT ME!
     */
    public static void load(TablePeer peer, CompoundEdit edit) {
        Component[] newChildren = getEmptyChildren((TableBase) peer.getComponent());

        TableBase g = (TableBase) peer.getComponent();
        PanelStore liststore = g.getPanelstore();

        for (int i = 0; i < newChildren.length; i++) {
            Component c = newChildren[i];
            ComponentPeer childPeer = ComponentPeerFactory.createPeer(peer.getOwner(), c);

            Component p = liststore.getComponentOver(c.getCell().row, c.getCell().column);
            ComponentPeer parentPeer = peer.getOwner().getComponentPeer(p);

            parentPeer.add(childPeer);

            if (edit != null) {
                edit.addEdit(new AddEdit(childPeer, false));
            }
        }
    }

    private static Component[] getEmptyChildren(TableBase table) {
        ArrayList empties = new ArrayList();

        for (int row = 0; row < table.getRowCount(); row++) {
            for (int col = 0; col < table.getColumnCount(); col++) {
                if (table.getCellstore().getCellOver(row, col) == null) {
                    Label label = new Label();

                    label.setCell(new Cell(row, col));
                    empties.add(label);
                }
            }
        }

        return (Component[]) empties.toArray(new Component[0]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param table DOCUMENT ME!
     */
    public static void load(TableBase table) {
        Component[] children = getEmptyChildren(table);
        PanelStore liststore = table.getPanelstore();

        for (int i = 0; i < children.length; i++) {
            Component c = children[i];

            Component p = liststore.getComponentOver(c.getCell().row, c.getCell().column);

            p.add(c);
        }
    }
}
