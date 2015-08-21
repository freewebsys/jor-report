/*
 *   Author: John.
 *
 *   杭州杰创软件   All Copyrights Reserved.
 */


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
public class MoveEdit extends AbstractUndoableEdit {
    ComponentPeer peer;
    int deltaX;
    int deltaY;

    public MoveEdit(ComponentPeer peer,
                     int deltaX,
                     int deltaY) {
        this.peer = peer;
        this.deltaX = deltaX;
        this.deltaY = deltaY;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws CannotRedoException DOCUMENT ME!
     */
    public void redo() throws CannotRedoException {
        super.redo();
        peer.move(deltaX, deltaY);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws CannotUndoException DOCUMENT ME!
     */
    public void undo() throws CannotUndoException {
        super.undo();

        peer.move(-deltaX, -deltaY);
    }
}
