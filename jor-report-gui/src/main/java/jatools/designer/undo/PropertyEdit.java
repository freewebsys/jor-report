package jatools.designer.undo;

import jatools.accessor.PropertyDescriptor;
import jatools.designer.peer.ComponentPeer;
import jatools.property.PropertyUtil;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class PropertyEdit extends AbstractUndoableEdit {
    ComponentPeer peer;
    String propertyName;
    Object oldValue;
    Object newValue;

    /**
     * Creates a new PropertyEdit object.
     *
     * @param peer DOCUMENT ME!
     * @param propertyName DOCUMENT ME!
     * @param oldValue DOCUMENT ME!
     * @param newValue DOCUMENT ME!
     */
    public PropertyEdit(ComponentPeer peer, String propertyName, Object oldValue, Object newValue) {
        this.peer = peer;
        this.propertyName = propertyName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws CannotRedoException DOCUMENT ME!
     */
    public void redo() throws CannotRedoException {
        super.redo();

        try {
            PropertyDescriptor des = PropertyUtil.getPropertyDescriptor(peer, propertyName);
            peer.setValue(propertyName, newValue, des.getPropertyType());
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

        try {
            PropertyDescriptor des = PropertyUtil.getPropertyDescriptor(peer, propertyName);

            peer.setValue(propertyName, oldValue, des.getPropertyType());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
