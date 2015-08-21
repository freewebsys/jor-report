package jatools.data.reader;

import jatools.dom.PaddingNode;
import jatools.dom.RowNode;
import jatools.engine.script.Context;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class PaddingCursor implements Cursor {
    int row = -1;
    private int size;
    private Context context;

    /**
     * Creates a new PaddingCursor object.
     *
     * @param size DOCUMENT ME!
     * @param context DOCUMENT ME!
     */
    public PaddingCursor(int size, Context context) {
        this.size = size;
        this.context = context;
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public void next() {
        row++;
    }

    /**
     * DOCUMENT ME!
     */
    public void open() {
        context.getScript().getNodeStack(context.getScript().getStackType())
               .push(new PaddingNode(-1 * (1 + row)));
    }

    /**
     * DOCUMENT ME!
     */
    public void close() {
        context.getScript().getNodeStack(context.getScript().getStackType()).pop();
    }

    /**
     * DOCUMENT ME!
     */
    public void save() {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     */
    public void rollback() {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasNext() {
        return row < (size - 1);
    }

    /**
     * DOCUMENT ME!
     */
    public void last() {
        row = size;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isRowBased() {
        return context.getScript().getNodeStack(context.getScript().getStackType()).getNode() instanceof RowNode;
    }
}
