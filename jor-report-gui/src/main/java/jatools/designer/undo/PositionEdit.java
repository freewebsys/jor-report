package jatools.designer.undo;

import jatools.designer.peer.ComponentPeer;

import java.awt.Rectangle;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;



public class PositionEdit extends AbstractLayoutEdit implements LayoutEdit {
    Rectangle oldVal;
    Rectangle newVal;
    private ComponentPeer peer;

    /**
     * Creates a new PositionEdit object.
     *
     * @param peer DOCUMENT ME!
     * @param oldVal DOCUMENT ME!
     * @param newVal DOCUMENT ME!
     */
    public PositionEdit(ComponentPeer peer, Rectangle oldVal, Rectangle newVal) {
        this.peer = peer;
        this.oldVal = oldVal;
        this.newVal = newVal;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws CannotRedoException DOCUMENT ME!
     */
    public void redo() throws CannotRedoException {
        super.redo();

        peer.getComponent().setBounds(newVal);
        fireListener(REDO);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws CannotUndoException DOCUMENT ME!
     */
    public void undo() throws CannotUndoException {
        super.undo();

        peer.getComponent().setBounds(oldVal);

        fireListener(UNDO);
    }
}
