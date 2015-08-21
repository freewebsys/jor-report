package jatools.designer.action;

import jatools.designer.App;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.undo.CannotUndoException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class UndoAction extends ReportAction {
   

    /**
     * Creates a new UndoAction object.
     */
    public UndoAction() {
        super(App.messages.getString("res.602"), getIcon("/jatools/icons/undo.gif"),
            getIcon("/jatools/icons/undo2.gif"));
        setStroke(ctrl(KeyEvent.VK_Z));
        this.setEnabled(false);
    }

    /**
     * Creates a new UndoAction object.
     *
     * @param kit DOCUMENT ME!
     */
   

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        try {
            this.getEditor().getUndoManager().undo();
        } catch (CannotUndoException ex) {
        }
    }
}
