package jatools.designer.action;

import jatools.ReportDocument;
import jatools.designer.App;
import jatools.designer.Main;
import jatools.designer.ReportEditor;
import jatools.designer.chooser.ReportChooser;
import jatools.swingx.MessageBox;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.Iterator;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ExitAction extends ReportAction {
    /**
     * Creates a new ExitAction object.
     */
    public ExitAction() {
        super(App.messages.getString("res.581"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        WindowEvent we = new WindowEvent(Main.getInstance(), WindowEvent.WINDOW_CLOSING);
        Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(we);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean closeQuery() {
        Iterator it = Main.getInstance().getEditorPanel().iterator();

        boolean dirty = false;
        ReportEditor dirtyeditor = null;

        int count = 0;

        while (it.hasNext()) {
            ReportEditor editor = (ReportEditor) it.next();

            if (editor.isDirty()) {
                dirty = true;
                dirtyeditor = editor;
            }

            count++;
        }

        boolean closed = true;

        if (dirty) {
            if (count == 1) {
                closed = query(dirtyeditor);
            } else {
                int answer = MessageBox.show(Main.getInstance(), App.messages.getString("res.437"),
                        App.messages.getString("res.582"), MessageBox.YES_NO);

                if (answer == MessageBox.YES) {
                    closed = false;
                }
            }
        }

        return closed;
    }

    private boolean query(ReportEditor dirtyeditor) {
        int answer = MessageBox.show(Main.getInstance(), App.messages.getString("res.437"), App.messages.getString("res.579"),
                MessageBox.YES_NO_CANCEL);

        if (answer == MessageBox.YES) {
            ReportDocument doc = dirtyeditor.getDocument();

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
