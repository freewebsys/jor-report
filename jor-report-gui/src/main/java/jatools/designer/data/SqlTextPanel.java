package jatools.designer.data;

import jatools.swingx.wizard.WizardCellEditor;

import java.awt.Component;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.text.StyledEditorKit;



/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.3 $
 * @author $author$
 */
public class SqlTextPanel extends JScrollPane implements WizardCellEditor {
    String lastText;
    JEditorPane editor = new JEditorPane();

    /**
     * Creates a new ZSQLTextPane object.
     */
    public SqlTextPanel() {
        this.getViewport().setView(editor);
        editor.setEditorKit(new StyledEditorKit());
        editor.setDocument(new SQLDocument());
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isChanged() {
        return editor.getText().equals(lastText);
    }

    /**
     * DOCUMENT ME!
     */
    public void applyChange() {
        lastText = editor.getText();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void checkAvailable() throws Exception {
        if (editor.getText().trim().equals("")) { //
            throw new Exception("text is empty!"); //
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public Object getContent() throws Exception {
        return editor.getText();
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    public void setText(String text) {
        editor.setText(text);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getEditorComponent() {
        return this;
    }
}
