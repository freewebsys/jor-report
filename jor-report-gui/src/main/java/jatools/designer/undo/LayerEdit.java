package jatools.designer.undo;

import jatools.designer.peer.ComponentPeer;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;



/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.1 $
 * @author $author$
 */
public class LayerEdit extends AbstractUndoableEdit {
    ComponentPeer peer;
    int oldIndex;
    int newIndex;

    /**
    * Creates a new ZRemoveEdit object.
    *
    * @param source DOCUMENT ME!
    * @param index DOCUMENT ME!
    */
    public LayerEdit(ComponentPeer peer,
                       int oldIndex,
                       int newIndex) {
        this.peer = peer;
        this.oldIndex = oldIndex;
        this.newIndex = newIndex;
    }

    /**
    * DOCUMENT ME!
    *
    * @throws CannotRedoException DOCUMENT ME!
    */
    public void redo() throws CannotRedoException {
        super.redo();

        ComponentPeer parent = peer.getParent();

        if (parent != null) {
            parent.remove(peer);
            parent.insert(peer, newIndex);
        }
    }

    /**
    * DOCUMENT ME!
    *
    * @throws CannotUndoException DOCUMENT ME!
    */
    public void undo() throws CannotUndoException {
        super.undo();

        ComponentPeer parent = peer.getParent();

        if (parent != null) {
            parent.remove(peer);
            parent.insert(peer, oldIndex);
        }
    }
}
