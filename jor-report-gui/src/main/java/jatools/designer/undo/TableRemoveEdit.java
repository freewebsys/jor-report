/*
 * Created on 2004-5-30
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jatools.designer.undo;

import jatools.designer.peer.TablePeer;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;



/**
 * @author zhou
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TableRemoveEdit extends AbstractUndoableEdit {
    TablePeer peer;
    boolean rowRemove;
    int index;
    int size ;
    
    
    
    

    public TableRemoveEdit(TablePeer peer,
                          boolean rowRemove,
                          int index) {
        
        this.peer = peer;
        this.rowRemove = rowRemove;
        this.index = index;
    }
    
    public static TableRemoveEdit work(TablePeer peer,
            boolean rowRemove,
            int index)
    {
        
        return null;
    }
    

    /**
     * DOCUMENT ME!
     *
     * @throws CannotRedoException DOCUMENT ME!
     */
    public void redo() throws CannotRedoException {
        super.redo();

        try {
        
        } catch (Exception ex) {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws CannotUndoException DOCUMENT ME!
     */
    public void undo() throws CannotUndoException {
        super.undo();

     
    }
}
