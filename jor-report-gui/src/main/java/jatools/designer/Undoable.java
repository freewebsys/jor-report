package jatools.designer;

import javax.swing.undo.UndoableEdit;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public interface Undoable {
    void openEdit();

    void closeEdit();

    void appendEdit(UndoableEdit edit);

    /**
     * DOCUMENT ME!
     *
     * @param anEdit DOCUMENT ME!
     */
    public void addEdit(UndoableEdit anEdit);
}
