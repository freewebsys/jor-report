package jatools.designer.action;


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
public abstract class SpaceAction extends ReportAction {
    public static int H_EQUAL = 1;
    public static int H_DEC = 2;
    public static int H_INC = 3;
    public static int V_EQUAL = 4;
    public static int V_DEC = 5;
    public static int V_INC = 6;

    /**
     * Creates a new SpaceAction object.
     *
     * @param name DOCUMENT ME!
     * @param icon DOCUMENT ME!
     */
    public SpaceAction(String name, Icon icon) {
        super(name, icon);
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

            Point delta = getDelta(targetRect, peerRect);

            peer.move(delta.x, delta.y);
        }

        getEditor().getUndoManager().addEdit(compoundEdit);

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
        setEnabled((state.getCount() > 1) && state.isSameParent());
    }
}
