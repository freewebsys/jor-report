package jatools.designer.action;

import jatools.ReportDocument;
import jatools.designer.App;
import jatools.designer.EditorTabPanel;
import jatools.designer.Main;
import jatools.designer.ReportEditor;
import jatools.designer.chooser.ReportChooser;
import jatools.swingx.MessageBox;

import java.awt.event.ActionEvent;
import java.io.File;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class CloseAction extends ReportAction {
    /**
     * Creates a new CloseAction object.
     */
    public CloseAction() {
        this(App.messages.getString("res.164"));
    }

    /**
     * Creates a new CloseAction object.
     *
     * @param text DOCUMENT ME!
     */
    public CloseAction(String text) {
        super(text);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        ReportEditor editor = Main.getInstance().getActiveEditor();

        boolean closed = true;

        if (editor != null) {
            closed = closeQuery(editor);
        }

        if (closed) {
            EditorTabPanel tabPanel = Main.getInstance().getEditorPanel().getTabbedPanel();
            tabPanel.removeTabAt(tabPanel.indexOfComponent(editor));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param editor DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean closeQuery(ReportEditor editor) {
        if (!editor.isDirty()) {
            return true;
        }

        int answer = MessageBox.show(Main.getInstance(), App.messages.getString("res.437"), App.messages.getString("res.579"),
                MessageBox.YES_NO_CANCEL);

        if (answer == MessageBox.YES) {
            ReportDocument doc = editor.getDocument();

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
}
