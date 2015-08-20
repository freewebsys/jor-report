/*
 * Created on 2004-5-29
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jatools.designer.undo;

import jatools.designer.peer.ComponentPeer;
import jatools.designer.peer.TablePeer;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;



/**
 * @author zhou
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class TablePropertyEdit extends PropertyEdit {
    /**
     * @param peer
     * @param propertyName
     * @param oldValue
     * @param newValue
     */
    public TablePropertyEdit(ComponentPeer peer, String propertyName, Object oldValue,
        Object newValue) {
        super(peer, propertyName, oldValue, newValue);

    }

    public TablePropertyEdit(ComponentPeer peer) {
        this(peer, null, null, null);
       
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.undo.UndoableEdit#redo()
     */
    public void redo() throws CannotRedoException {
        // TODO Auto-generated method stub
        if (propertyName != null) {
            super.redo();
        } else {
            ((TablePeer) peer).refreshSelection();
        }

        ((TablePeer) peer).getComponent().invalid();
      
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.undo.UndoableEdit#undo()
     */
    public void undo() throws CannotUndoException {
        // TODO Auto-generated method stub
        if (propertyName != null) {
            super.undo();
        } else {
            ((TablePeer) peer).refreshSelection();
        }

        ((TablePeer) peer).getComponent().invalid();
        
    }
}
