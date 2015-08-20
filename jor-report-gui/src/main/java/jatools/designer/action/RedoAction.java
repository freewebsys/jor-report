package jatools.designer.action;


import jatools.designer.App;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.undo.CannotRedoException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class RedoAction extends ReportAction {
    /**
     * Creates a new RedoAction object.
     */
    public RedoAction() {
        super(App.messages.getString("res.595"), getIcon("/jatools/icons/redo.gif"), getIcon("/jatools/icons/redo2.gif"));
        setStroke(ctrl(KeyEvent.VK_Y));
        setEnabled(false);
    }



    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        try {
            this.getEditor().getUndoManager().redo();
        } catch (CannotRedoException ex) {
        }
    }
}
