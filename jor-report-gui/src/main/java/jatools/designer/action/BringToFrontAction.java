package jatools.designer.action;

import jatools.component.Component;
import jatools.component.Page;
import jatools.designer.App;
import jatools.designer.ReportEditor;
import jatools.designer.SelectionState;
import jatools.designer.peer.ComponentPeer;
import jatools.designer.undo.LayerEdit;

import java.awt.event.ActionEvent;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class BringToFrontAction extends ReportAction {
    /**
     * Creates a new BringToFrontAction object.
     */
    public BringToFrontAction() {
        super(App.messages.getString("res.578"), getIcon("/jatools/icons/tofront.gif"),
            getIcon("/jatools/icons/tofront2.gif"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        ReportEditor editor = getEditor();
        ComponentPeer[] peers = (ComponentPeer[]) editor.getReportPanel().getSelection();

        if (peers.length != 1) {
            return;
        }

        ComponentPeer peer = peers[0];
        Component target = peer.getComponent();

        if (target instanceof Page) {
            return;
        }

        ComponentPeer parent = peer.getParent();

        int oldIndex = parent.indexOf(peer);

        if (parent.remove(peer)) {
            parent.add(peer);

            int newIndex = parent.getChildCount() - 1;

            if (newIndex != oldIndex) {
                getEditor().getUndoManager().addEdit(new LayerEdit(peer, oldIndex, newIndex));
                getEditor().repaint();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param state DOCUMENT ME!
     */
    public void enabled(SelectionState state) {
        setEnabled((state.getCount() == 1) && state.isMoveable());
    }
}
