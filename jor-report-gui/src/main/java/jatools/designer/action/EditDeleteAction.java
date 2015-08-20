package jatools.designer.action;

import jatools.designer.App;
import jatools.designer.ReportPanel;
import jatools.designer.SelectionState;
import jatools.designer.layer.table.TableEditKit;
import jatools.designer.peer.ComponentPeer;
import jatools.designer.undo.GroupEdit;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.undo.CompoundEdit;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class EditDeleteAction extends EditClipAction {
    /**
     * Creates a new EditDeleteAction object.
     */
    public EditDeleteAction() {
        super(null, App.messages.getString("res.96"), getIcon("/jatools/icons/delete.gif"),
            getIcon("/jatools/icons/delete2.gif"));
        setStroke(key(KeyEvent.VK_DELETE));
    }

    /**
     * Creates a new EditDeleteAction object.
     *
     * @param panel DOCUMENT ME!
     */
    public EditDeleteAction(ReportPanel panel) {
        super(panel, App.messages.getString("res.96"), getIcon("/jatools/icons/delete.gif"),
            getIcon("/jatools/icons/delete2.gif"));
        setStroke(key(KeyEvent.VK_DELETE));
    }

    /**
     * DOCUMENT ME!
     *
     * @param state DOCUMENT ME!
     */
    public void enabled(SelectionState state) {
        setEnabled((state.getCount() > 0) && state.isRemoveable());
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        ReportPanel panel = this.getReportPanel();
        ReportPanel pagePanel = getReportPanel();
        TableEditKit kit = pagePanel.getTableEditKit();

        if (kit != null) {
            kit.delete();
        } else {
            CompoundEdit edit = new GroupEdit();

            ComponentPeer[] peers = (ComponentPeer[]) panel.getSelection();

            for (int i = 0; i < peers.length; i++) {
                if (!peers[i].isRemoveable()) {
                    return;
                }
            }

            if (delete(panel, peers, edit)) {
                panel.addEdit(edit);

                this.getEditor().repaint();
            }
        }
    }
}
