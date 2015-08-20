package jatools.data.reader;

import java.util.Stack;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class CursorManager {
    Stack cursors = new Stack();

    /**
     * DOCUMENT ME!
     *
     * @param cursor DOCUMENT ME!
     */
    public void push(Cursor cursor) {
        cursors.push(cursor);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Cursor pop() {
        return (Cursor) cursors.pop();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Cursor getCursor() {
        return (Cursor) cursors.peek();
    }
}
