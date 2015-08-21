package jatools.designer.action;

import jatools.component.Component;

import jatools.designer.ReportEditor;
import jatools.designer.SelectionState;

import jatools.designer.peer.ComponentPeer;

import jatools.designer.undo.GroupEdit;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.undo.CompoundEdit;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public abstract class AlignAction extends ReportAction {
    /**
     * Creates a new AlignAction object.
     *
     * @param name DOCUMENT ME!
     * @param icon DOCUMENT ME!
     * @param icon2 DOCUMENT ME!
     */
    public AlignAction(String name, Icon icon, Icon icon2) {
        super(name, icon, icon2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        ReportEditor editor = getEditor();

        if (editor.getReportPanel().getSelectionCount() <= 1) {
            return;
        }

        ComponentPeer[] peers = (ComponentPeer[]) editor.getReportPanel().getSelection();
        Rectangle targetRect = editor.getReportPanel().getSelection(0).getBounds();

        SimplePropertyListener propertyChangeListener = SimplePropertyListener.getInstance();

        propertyChangeListener.clear();

        Component.temppropertylistener.set(propertyChangeListener);

        for (int i = 1; i < peers.length; i++) {
            Component c = peers[i].getComponent();

            Rectangle peerRect = c.getBounds();
            Point delta = getDelta(targetRect, peerRect);
            c.setX(c.getX() + delta.x);
            c.setY(c.getY() + delta.y);
        }

        CompoundEdit edit = new GroupEdit();
        propertyChangeListener.applyEdit(edit);
        Component.temppropertylistener.set(null);
        getEditor().getUndoManager().addEdit(edit);

        editor.repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @param targetRect DOCUMENT ME!
     * @param peerRect DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Point getDelta(Rectangle targetRect, Rectangle peerRect);

    /**
     * DOCUMENT ME!
     *
     * @param state DOCUMENT ME!
     */
    public void enabled(SelectionState state) {
        setEnabled((state.getCount() > 1) && state.isMoveable());
    }
}
