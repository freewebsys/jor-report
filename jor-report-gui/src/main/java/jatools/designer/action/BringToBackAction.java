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
public class BringToBackAction extends ReportAction {
    /**
     * Creates a new BringToBackAction object.
     */
    public BringToBackAction() {
        super(App.messages.getString("res.577"), getIcon("/jatools/icons/toback.gif"),
            getIcon("/jatools/icons/toback2.gif"));
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
            parent.insert(peer, 0);

            int newIndex = parent.indexOf(peer);

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
