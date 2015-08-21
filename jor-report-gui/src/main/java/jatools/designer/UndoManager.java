package jatools.designer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class UndoManager extends javax.swing.undo.UndoManager {
    /**
     * DOCUMENT ME!
     */
    public static final int AFTER_UNDO = 1;

    /**
     * DOCUMENT ME!
     */
    public static final int AFTER_REDO = 2;

    /**
     * DOCUMENT ME!
     */
    public static final int ADD_UNDO = 3;

    /**
     * DOCUMENT ME!
     */
    public static final int BEFORE_REDO = 4;

    /**
     * DOCUMENT ME!
     */
    public static final int BEFORE_UNDO = 5;

    /**
     * DOCUMENT ME!
     */
    public static final int DISCARD_EDITS = 6;
    ReportPanel reportPanel;
    ArrayList listeners;
    private boolean dirty;

    /**
     * Creates a new UndoManager object.
     *
     * @param reportPanel DOCUMENT ME!
     */
    public UndoManager(ReportPanel reportPanel) {
        this.reportPanel = reportPanel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param anEdit DOCUMENT ME!
     */
    public void discardEdit(UndoableEdit anEdit) {
        int index = edits.indexOf(anEdit);

        if (index >= 0) {
            this.trimEdits(index, index);
        }

        fireListener(DISCARD_EDITS);
    }

    /**
     * DOCUMENT ME!
     *
     * @param listener DOCUMENT ME!
     */
    public void addListener(ActionListener listener) {
        if (this.listeners == null) {
            this.listeners = new ArrayList();
        }

        this.listeners.add(listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @param action DOCUMENT ME!
     */
    public void fireListener(int action) {
        if (this.listeners != null) {
            ActionEvent e = new ActionEvent(reportPanel, action, null);
            Iterator it = this.listeners.iterator();

            while (it.hasNext()) {
                ActionListener al = (ActionListener) it.next();
                al.actionPerformed(e);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param anEdit DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public synchronized boolean addEdit(UndoableEdit anEdit) {
        if (anEdit instanceof CompoundEdit) {
            ((CompoundEdit) anEdit).end();
        }

        boolean result = super.addEdit(anEdit);
        fireListener(ADD_UNDO);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws CannotRedoException DOCUMENT ME!
     */
    public void redo() throws CannotRedoException {
        fireListener(BEFORE_REDO);
        super.redo();
        fireListener(AFTER_REDO);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws CannotUndoException DOCUMENT ME!
     */
    public void undo() throws CannotUndoException {
        fireListener(BEFORE_UNDO);
        super.undo();
        fireListener(AFTER_UNDO);
    }

    /**
     * DOCUMENT ME!
     */
    public void clear() {
        super.discardAllEdits();
        dirty = false;
        fireListener(DISCARD_EDITS);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public UndoableEdit lastEdit() {
        return super.lastEdit();
    }

    /**
     * DOCUMENT ME!
     *
     * @param enabler DOCUMENT ME!
     */
    public void removeListener(ActionListener listener) {
        if (this.listeners != null) {
            this.listeners.remove(listener);
        }
    }
}
