package jatools.designer.undo;

import jatools.designer.peer.ComponentPeer;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;



public class DeleteEdit extends AbstractLayoutEdit {
    ComponentPeer peer;
    int index;

    /**
     * Creates a new DeleteEdit object.
     *
     * @param peer DOCUMENT ME!
     * @param index DOCUMENT ME!
     */
    public DeleteEdit(ComponentPeer peer, int index) {
        this.peer = peer;
        this.index = index;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws CannotRedoException DOCUMENT ME!
     */
    public void redo() throws CannotRedoException {
        super.redo();
        peer.getParent().remove(peer);

        this.fireListener(REDO);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws CannotUndoException DOCUMENT ME!
     */
    public void undo() throws CannotUndoException {
        super.undo();
        peer.getParent().insert(peer, index);
        this.fireListener(UNDO);
    }
}
