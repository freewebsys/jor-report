package jatools.designer.action;



import jatools.ReportDocument;
import jatools.designer.Main;
import jatools.swingx.MessageBox;

import java.awt.event.ActionEvent;
import java.io.File;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class OpenMruAction extends OpenAction {
    /**
     * Creates a new OpenMruAction object.
     */
    public OpenMruAction() {
        super(null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        ReportDocument doc = getDocument(e.getActionCommand());

        if (doc != null) {
            Main.getInstance()
                .createEditor(doc, ReportDocument.getCachedFile(doc).getName(),
                ReportDocument.getCachedFile(doc).getAbsolutePath(),true);
        }
    }

    protected ReportDocument getDocument(String f) {
        ReportDocument doc = null;
        File file = new File(f);

        if (file != null) {
            try {
                doc = ReportDocument.load(file);
            } catch (Exception e1) {
                MessageBox.error(Main.getInstance().getActiveEditor(), e1.getMessage());
            }
        }

        return doc;
    }
}
