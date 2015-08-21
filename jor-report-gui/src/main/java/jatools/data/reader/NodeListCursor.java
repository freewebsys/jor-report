package jatools.data.reader;

import jatools.dom.DatasetBasedNode;
import jatools.dom.RowNode;
import jatools.engine.script.Context;

import org.w3c.dom.NodeList;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class NodeListCursor implements Cursor {
    Context context;
    int row = -1;
    NodeList e;

    /**
     * Creates a new BasicCursor object.
     *
     * @param dataset DOCUMENT ME!
     */
    public NodeListCursor(NodeList e, Context context) {
        this.e = e;
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
        if (e.item(row) instanceof DatasetBasedNode) {
            ((DatasetBasedNode) e.item(row)).open(context.getScript());
        }

        context.getScript().getNodeStack(context.getScript().getStackType()).push(e.item(row));
    }

    /**
     * DOCUMENT ME!
     */
    public void close() {
        if (e.item(row) instanceof DatasetBasedNode) {
            ((DatasetBasedNode) e.item(row)).close(context.getScript());
        }

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
        return row < (e.getLength() - 1);
    }

    /**
     * DOCUMENT ME!
     */
    public void last() {
        row = e.getLength();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //    private NodeLocator getLocator() {
    //        NodeLocator loc = null;
    //
    //        if ((e.getLength() > 0) && e.item(0) instanceof DatasetBasedElement) {
    //            loc = ((DatasetBasedElement) e.item(0)).getDatasetRoot().getLocator();
    //        }
    //
    //        return loc;
    //    }
    public boolean isRowBased() {
        return (this.e != null) && (e.getLength() > 0) && this.e.item(0) instanceof RowNode;
    }
}
