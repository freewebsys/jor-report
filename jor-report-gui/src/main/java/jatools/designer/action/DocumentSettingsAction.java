package jatools.designer.action;

import jatools.ReportDocument;
import jatools.designer.App;
import jatools.designer.DocumentSettingsDialog;

import java.awt.event.ActionEvent;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class DocumentSettingsAction extends ReportAction {
    /**
     * Creates a new DocumentSettingsAction object.
     */
    public DocumentSettingsAction() {
        super(App.messages.getString("res.580"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        ReportDocument doc = getEditor().getDocument();
        DocumentSettingsDialog d = new DocumentSettingsDialog(doc);

        d.show();
    }
}
