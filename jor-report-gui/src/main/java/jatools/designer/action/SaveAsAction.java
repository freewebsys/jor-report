package jatools.designer.action;


import jatools.ReportDocument;
import jatools.designer.App;
import jatools.designer.Main;
import jatools.designer.chooser.ReportChooser;

import java.awt.event.ActionEvent;
import java.io.File;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class SaveAsAction extends ReportAction {
    /**
     * Creates a new SaveAsAction object.
     */
    public SaveAsAction() {
        super(App.messages.getString("res.599"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param e1 DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e1) {
        ReportDocument doc = getEditor().getDocument();

        if (doc != null) {
            ReportChooser chooser = ReportChooser.getInstance();
            File docFile = ReportDocument.getCachedFile(doc);

            if (docFile != null) {
                chooser.setSelectedFile(docFile);
            }

            chooser.setDocument(doc);
            chooser.showSaveAsDialog();

            Main.getInstance().save(doc, ReportDocument.getCachedFile(doc));
        }
    }
}
