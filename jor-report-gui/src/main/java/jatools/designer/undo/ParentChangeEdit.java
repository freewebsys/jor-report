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
public class ParentChangeEdit extends AbstractUndoableEdit {
    ComponentPeer peer;
    ComponentPeer oldParent;
    ComponentPeer newParent;
    int oldIndex;
    int newIndex;

    /**
    * Creates a new ZParentChangeEdit object.
    *
    * @param source DOCUMENT ME!
    */
    public ParentChangeEdit(ComponentPeer peer,
                             ComponentPeer oldParent,
                             int oldIndex) {
        this.peer = peer;
        this.oldParent = oldParent;

        this.newParent = peer.getParent();
        this.oldIndex = oldIndex;
        this.newIndex = newParent.indexOf(peer);
    }

    /**
    * DOCUMENT ME!
    *
    * @throws CannotRedoException DOCUMENT ME!
    */
    public void redo() throws CannotRedoException {
        super.redo();
        oldParent.remove(peer);
        newParent.insert(peer, newIndex);
    }

    /**
    * DOCUMENT ME!
    *
    * @throws CannotUndoException DOCUMENT ME!
    */
    public void undo() throws CannotUndoException {
        super.undo();
        newParent.remove(peer);
        oldParent.insert(peer, oldIndex);
    }
}
