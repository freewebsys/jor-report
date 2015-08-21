package jatools.designer.action;



import jatools.ReportDocument;
import jatools.designer.App;
import jatools.designer.Main;
import jatools.designer.chooser.ReportChooser;
import jatools.swingx.MessageBox;
import jatools.util.Util;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class SaveAction extends ReportAction {
    /**
     * Creates a new SaveAction object.
     */
    public SaveAction() {
        super(App.messages.getString("res.566"), getIcon("/jatools/icons/save.gif"), getIcon("/jatools/icons/save2.gif"));
        setStroke(ctrl(KeyEvent.VK_S));
    }

    /**
     * DOCUMENT ME!
     *
     * @param ae DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent ae) {
        ReportDocument doc = getEditor().getDocument();

        if (doc != null) {
            ReportChooser chooser = ReportChooser.getInstance();
            File docFile = ReportDocument.getCachedFile(doc);

            if (docFile == null) {
                chooser.setSelectedFile(null);
                chooser.setDocument(doc);
                chooser.showSaveDialog();
                docFile = ReportDocument.getCachedFile(doc);
            } else {
                try {
                    ReportDocument.save(doc, docFile, false);
                } catch (Exception e) {
                    MessageBox.error(Main.getInstance(), App.messages.getString("res.568") + Util.toString(e));
                    Util.debug("ZSaveAction", e);
                }
            }

            Main.getInstance().save(doc, docFile);
        }
    }
}
