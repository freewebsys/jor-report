package jatools.designer.action;

import jatools.component.Component;

import jatools.designer.App;
import jatools.designer.ClipBoard;
import jatools.designer.ReportPanel;
import jatools.designer.SelectionState;

import jatools.designer.layer.table.TableEditKit;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class EditCopyAction extends EditClipAction {
    /**
     * Creates a new EditCopyAction object.
     */
    public EditCopyAction() {
        super(null, App.messages.getString("res.128"), getIcon("/jatools/icons/copy.gif"),
            getIcon("/jatools/icons/copy2.gif"));
        setStroke(ctrl(KeyEvent.VK_C));
    }

    /**
     * Creates a new EditCopyAction object.
     *
     * @param reportPanel
     *            DOCUMENT ME!
     */
    public EditCopyAction(ReportPanel reportPanel) {
        super(reportPanel, App.messages.getString("res.128"), getIcon("/jatools/icons/copy.gif"),
            getIcon("/jatools/icons/copy2.gif"));
        setStroke(ctrl(KeyEvent.VK_C));
    }

    /**
     * DOCUMENT ME!
     *
     * @param e
     *            DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        ReportPanel pagePanel = getReportPanel();
        TableEditKit kit = pagePanel.getTableEditKit();

        if (kit != null) {
            kit.copy(null, false);
        } else {
            int count = pagePanel.getSelectionCount();

            if (count == 0) {
                return;
            }

            Component[] selection = new Component[count];

            for (int i = 0; i < selection.length; i++) {
                selection[i] = pagePanel.getSelection(i).getComponent();
            }

            try {
                ClipBoard.getDefaultClipBoard().setContents(selection);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param state
     *            DOCUMENT ME!
     */
    public void enabled(SelectionState state) {
        setEnabled((state.getCount() > 0) && state.isCopiable());
    }
}
