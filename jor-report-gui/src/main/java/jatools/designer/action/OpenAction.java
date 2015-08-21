package jatools.designer.action;



import jatools.ReportDocument;
import jatools.designer.App;
import jatools.designer.Main;
import jatools.designer.chooser.ReportChooser;
import jatools.swingx.MessageBox;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class OpenAction extends ReportAction {
    /**
     * Creates a new OpenAction object.
     */
    public OpenAction() {
        super(App.messages.getString("res.92"), getIcon("/jatools/icons/open.gif"));
        setStroke(ctrl(KeyEvent.VK_O));
    }

    /**
     * Creates a new OpenAction object.
     *
     * @param text DOCUMENT ME!
     */
    public OpenAction(String text) {
        super(text);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        ReportChooser chooser = ReportChooser.getInstance();

        if (chooser.showDialog(App.messages.getString("res.92"), ReportChooser.SHOW_OPEN)) {
            ReportDocument doc = chooser.getDocument();
            Main.getInstance()
                .createEditor(doc, ReportDocument.getCachedFile(doc).getName(),
                ReportDocument.getCachedFile(doc).getAbsolutePath(),true);

//            System.out.println(ReportDocument.getCachedFile(doc).getAbsolutePath());
        }
    }

    protected boolean saveAs() {
        int answer = MessageBox.show(getEditor(), App.messages.getString("res.437"), App.messages.getString("res.588"), MessageBox.YES_NO_CANCEL);

        if (answer == MessageBox.YES) {
            ReportDocument doc = getEditor().getDocument();

            if (doc != null) {
                ReportChooser chooser = ReportChooser.getInstance();
                File docFile = ReportDocument.getCachedFile(doc);

                if (docFile != null) {
                    chooser.setSelectedFile(docFile);
                }

                chooser.setDocument(doc);
                chooser.showSaveDialog();
            }
        }

        return answer != MessageBox.CANCEL;
    }

    protected ReportDocument getDocument1() {
        ReportChooser chooser = ReportChooser.getInstance();

        if (chooser.showDialog(App.messages.getString("res.92"), ReportChooser.SHOW_OPEN)) {
            return chooser.getDocument();
        } else {
            return null;
        }
    }
}
