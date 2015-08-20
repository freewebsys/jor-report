package jatools.designer.undo;

import jatools.designer.peer.ComponentPeer;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;



public class AddEdit extends AbstractLayoutEdit /*implements ActionListener*/ {
    /**
     * DOCUMENT ME!
     */
//    public static final String UNDO_ACTION = "undo.action";
//
//    /**
//     * DOCUMENT ME!
//     */
//    public static final String REDO_ACTION = "redo.action";
    ComponentPeer childPeer;
//    ActionListener listener;
    private int index;

    /**
     * Creates a new AddEdit object.
     *
     * @param childPeer DOCUMENT ME!
     * @param autoUnselect DOCUMENT ME!
     */
    public AddEdit(ComponentPeer childPeer, boolean autoUnselect) {
        this(childPeer, childPeer.getParent().indexOf(childPeer), autoUnselect);
    }

    /**
     * Creates a new AddEdit object.
     *
     * @param childPeer DOCUMENT ME!
     * @param index DOCUMENT ME!
     * @param autoUnselect DOCUMENT ME!
     */
    public AddEdit(ComponentPeer childPeer, int index, boolean autoUnselect) {
        this.childPeer = childPeer;

//        if (autoUnselect) {
//            this.listener = this;
//        }

        this.index = index;
    }



    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
//    public void actionPerformed(ActionEvent e) {
//        if (e.getActionCommand() == AddEdit.UNDO_ACTION) {
//            ComponentPeer peer = (ComponentPeer) e.getSource();
//
//            if (peer.getOwner().isSelected(peer)) {
//                peer.getOwner().unselect(peer);
//            }
//        }
//    }

    /**
     * DOCUMENT ME!
     *
     * @throws CannotRedoException DOCUMENT ME!
     */
    public void redo() throws CannotRedoException {
        super.redo();
        childPeer.getParent().insert(childPeer, index);
       this.fireListener(REDO);
    }

//    private void fireAction(String command) {
//        if (listener == null) {
//            return;
//        }
//
//        ActionEvent e = new ActionEvent(childPeer, -1, command);
//        listener.actionPerformed(e);
//    }

    /**
     * DOCUMENT ME!
     *
     * @throws CannotUndoException DOCUMENT ME!
     */
    public void undo() throws CannotUndoException {
        super.undo();

//        if (childPeer instanceof ZSectionPeer) {
//            ((ZReportPeer) childPeer.getParent()).remove(childPeer, true);
//        } else {
            childPeer.getParent().remove(childPeer);
//        }

        this.fireListener(UNDO);
    }
}
