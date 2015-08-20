package jatools.designer.undo;

import jatools.designer.peer.ComponentPeer;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;



public class ResizeEdit extends AbstractUndoableEdit {
    ComponentPeer peer;
    int widthDelta;
    int heightDelta;

    public ResizeEdit(ComponentPeer peer,
                       int widthDelta,
                       int heightDelta) {
        this.peer = peer;
        this.widthDelta = widthDelta;
        this.heightDelta = heightDelta;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws CannotRedoException DOCUMENT ME!
     */
    public void redo() throws CannotRedoException {
        super.redo();

        int width = peer.getWidth();
        int height = peer.getHeight();

        peer.setWidth(width + widthDelta);
        peer.setHeight(height + heightDelta);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws CannotUndoException DOCUMENT ME!
     */
    public void undo() throws CannotUndoException {
        super.undo();

        int width = peer.getWidth();
        int height = peer.getHeight();

        peer.setWidth(width - widthDelta);
        peer.setHeight(height - heightDelta);
    }
}
