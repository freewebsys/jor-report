package jatools.designer.action;

import jatools.designer.ReportEditor;
import jatools.designer.SelectionState;
import jatools.designer.peer.ComponentPeer;
import jatools.designer.undo.GroupEdit;
import jatools.designer.undo.ResizeEdit;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import javax.swing.Icon;
import javax.swing.undo.CompoundEdit;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public abstract class ResizeAction extends ReportAction {
    /**
     * Creates a new ResizeAction object.
     *
     * @param owner DOCUMENT ME!
     * @param name DOCUMENT ME!
     * @param icon DOCUMENT ME!
     */
    public ResizeAction(String name, Icon icon,Icon icon2) {
        super( name, icon,icon2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param state DOCUMENT ME!
     */
    public void enabled(SelectionState state) {
        setEnabled((state.getCount() > 1) && state.isResizable() );
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
        CompoundEdit compoundEdit = new GroupEdit();

        for (int i = 1; i < peers.length; i++) {
            ComponentPeer peer = peers[i];

            Rectangle peerRect = peer.getBounds();

            Point delta = getSizeDelta(targetRect, peerRect);

            peer.setWidth(peerRect.width + delta.x);
            peer.setHeight(peerRect.height + delta.y);
            compoundEdit.addEdit(new ResizeEdit(peer, delta.x, delta.y));
        }

        getEditor().getUndoManager() .addEdit(compoundEdit);
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
    public abstract Point getSizeDelta(Rectangle targetRect, Rectangle peerRect);
}
