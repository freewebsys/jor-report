package jatools.designer.action;

import jatools.component.Component;
import jatools.designer.App;
import jatools.designer.ClipBoard;
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
public class EditCutAction extends EditClipAction {
    /**
     * Creates a new EditCutAction object.
     */
    public EditCutAction() {
        super(null, App.messages.getString("res.125"), getIcon("/jatools/icons/cut.gif"),
            getIcon("/jatools/icons/cut2.gif"));
        setStroke(ctrl(KeyEvent.VK_X));
    }

    /**
     * Creates a new EditCutAction object.
     *
     * @param panel DOCUMENT ME!
     */
    public EditCutAction(ReportPanel panel) {
        super(panel, App.messages.getString("res.125"), getIcon("/jatools/icons/cut.gif"),
            getIcon("/jatools/icons/cut2.gif"));
        setStroke(ctrl(KeyEvent.VK_X));
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
            kit.cut();
        } else {
            int count = panel.getSelectionCount();

            if (count == 0) {
                return;
            }

            Component[] selection = new Component[count];

            for (int i = 0; i < selection.length; i++) {
                selection[i] = panel.getSelection(i).getComponent();
            }

            try {
                ClipBoard.getDefaultClipBoard().setContents(selection);

                CompoundEdit edit = new GroupEdit();

                if (delete(panel, (ComponentPeer[]) panel.getSelection(), edit)) {
                    panel.addEdit(edit);

                    getEditor().repaint();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param state DOCUMENT ME!
     */
    public void enabled(SelectionState state) {
        setEnabled((state.getCount() > 0) && state.isRemoveable());
    }
}
