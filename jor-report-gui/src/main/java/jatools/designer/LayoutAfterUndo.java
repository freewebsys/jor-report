package jatools.designer;

import jatools.designer.undo.GroupEdit;
import jatools.designer.undo.LayoutEdit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.undo.UndoableEdit;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class LayoutAfterUndo implements ActionListener {
    ReportPanel owner;
    boolean required = false;

    /**
     * Creates a new LayoutAfterUndo object.
     *
     * @param owner DOCUMENT ME!
     */
    public LayoutAfterUndo(ReportPanel owner) {
        this.owner = owner;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof ReportEditor) {
            int id = e.getID();

            if (id == UndoManager.ADD_UNDO) {
                UndoableEdit edit = ((ReportEditor) e.getSource()).getUndoManager().lastEdit();

                hookListener(edit);
            } else if ((id == UndoManager.AFTER_REDO) || (id == UndoManager.AFTER_UNDO)) {
                doLayout();
            } else if ((id == UndoManager.BEFORE_REDO) || (id == UndoManager.BEFORE_UNDO)) {
                this.clear();
            }
        } else if (e.getSource() instanceof LayoutEdit) {
            required = true;
        }
    }

    private void hookListener(UndoableEdit edit) {
        if (edit instanceof LayoutEdit) {
            ((LayoutEdit) edit).addActionListener(this);
        } else if (edit instanceof GroupEdit) {
            Iterator it = ((GroupEdit) edit).iterator();

            while (it.hasNext())
                hookListener((UndoableEdit) it.next());
        }
    }

    private void doLayout() {
    }

    private void clear() {
        this.required = false;
    }
}
