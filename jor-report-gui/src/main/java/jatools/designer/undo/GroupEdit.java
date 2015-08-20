package jatools.designer.undo;

import java.util.Iterator;

import javax.swing.undo.CompoundEdit;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class GroupEdit extends CompoundEdit {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator iterator() {
        return (Iterator) this.edits.iterator();
    }
}
