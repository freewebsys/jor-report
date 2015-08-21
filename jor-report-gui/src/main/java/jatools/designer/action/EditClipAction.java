package jatools.designer.action;

import jatools.designer.ReportPanel;
import jatools.designer.peer.ComponentPeer;
import jatools.designer.undo.DeleteEdit;

import javax.swing.Icon;
import javax.swing.undo.CompoundEdit;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public abstract class EditClipAction extends ReportAction {
    ReportPanel reportPanel;

    /**
     * Creates a new EditClipAction object.
     *
     * @param panel DOCUMENT ME!
     * @param text DOCUMENT ME!
     * @param icon DOCUMENT ME!
     * @param icon2 DOCUMENT ME!
     */
    public EditClipAction(ReportPanel panel, String text, Icon icon, Icon icon2) {
        super(text, icon, icon2);
        this.reportPanel = panel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ReportPanel getReportPanel() {
        return (reportPanel != null) ? reportPanel : getEditor().getReportPanel();
    }

    boolean delete(ReportPanel owner, ComponentPeer[] peers, CompoundEdit edit) {
        if (peers.length == 0) {
            return false;
        }

        for (int i = 0; i < peers.length; i++) {
            ComponentPeer peer = peers[i];

            if (owner.isSelected(peer.getComponent())) {
                owner.unselect(peer.getComponent());
            }

            int index = peer.getParent().indexOf(peer);

            if (peer.getParent().remove(peer)) {
                edit.addEdit(new DeleteEdit(peer, index));
            } else {
                owner.select(peer.getComponent());
            }
        }

        return true;
    }
}
