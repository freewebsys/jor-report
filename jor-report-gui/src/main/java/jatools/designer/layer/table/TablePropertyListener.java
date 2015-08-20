package jatools.designer.layer.table;

import jatools.component.Component;
import jatools.component.ComponentConstants;

import jatools.designer.peer.ComponentPeer;
import jatools.designer.peer.ComponentPeerFactory;
import jatools.designer.peer.TablePeer;

import jatools.designer.undo.AddEdit;
import jatools.designer.undo.DeleteEdit;
import jatools.designer.undo.ParentChangeEdit;
import jatools.designer.undo.PropertyEdit;
import jatools.designer.undo.TablePropertyEdit;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class TablePropertyListener implements PropertyChangeListener {
    ArrayList changes = new ArrayList();
    TablePeer tablePeer;

    /**
     * Creates a new TablePropertyListener object.
     *
     * @param tablePeer DOCUMENT ME!
     */
    public TablePropertyListener(TablePeer tablePeer) {
        this.tablePeer = tablePeer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName() == ComponentConstants.PROPERTY_DELETE) {
            ComponentPeer parentPeer = tablePeer.getOwner()
                                                .getComponentPeer((Component) e.getSource());
            ComponentPeer childPeer = tablePeer.getOwner()
                                               .getComponentPeer((Component) e.getOldValue());
            int index = parentPeer.indexOf(childPeer);

            parentPeer.remove(childPeer);

            changes.add(new DeleteEdit(childPeer, index));
        } else if (e.getPropertyName() == ComponentConstants.PROPERTY_ADD) {
            ComponentPeer parentPeer = tablePeer.getOwner()
                                                .getComponentPeer((Component) e.getSource());

            ComponentPeer childPeer = tablePeer.getOwner()
                                               .getComponentPeer((Component) e.getOldValue());

            if (childPeer == null) {
                childPeer = ComponentPeerFactory.createPeer(tablePeer.getOwner(),
                        (Component) e.getOldValue());
            }

            parentPeer.add(childPeer);

            changes.add(new AddEdit(childPeer, true));
        } else if (e.getPropertyName() == ComponentConstants.PROPERTY_PARENT_CHANGE) {
            ComponentPeer parentPeer = tablePeer.getOwner()
                                                .getComponentPeer((Component) e.getSource());

            ComponentPeer childPeer = tablePeer.getOwner()
                                               .getComponentPeer((Component) e.getOldValue());

            ComponentPeer oldParent = tablePeer.getOwner()
                                               .getComponentPeer((Component) e.getNewValue());

            int oldIndex = oldParent.indexOf(childPeer);

            oldParent.remove(childPeer);
            parentPeer.add(childPeer);

            changes.add(new ParentChangeEdit(childPeer, oldParent, oldIndex));
        } else {
            Component c = (Component) e.getSource();
            ComponentPeer peer = tablePeer.getOwner().getComponentPeer(c);

            if (peer != null) {
                PropertyEdit pe = new PropertyEdit(peer, e.getPropertyName(), e.getOldValue(),
                        e.getNewValue());

                changes.add(pe);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param edit DOCUMENT ME!
     */
    public void applyEdit(CompoundEdit edit) {
        if (!changes.isEmpty()) {
            Iterator it = changes.iterator();

            while (it.hasNext()) {
                UndoableEdit pe = (UndoableEdit) it.next();
                edit.addEdit(pe);
            }

            PropertyEdit e = new TablePropertyEdit(tablePeer);
            edit.addEdit(e);
        }
    }
}
