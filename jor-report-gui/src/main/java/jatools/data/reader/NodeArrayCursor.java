package jatools.data.reader;

import jatools.dom.ElementBase;
import jatools.dom.RowNode;
import jatools.engine.script.Context;

import org.w3c.dom.Node;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class NodeArrayCursor implements Cursor {
    Context context;
    int row = -1;
    Node[] nodes;
    private int save;

    /**
     * Creates a new BasicCursor object.
     *
     * @param dataset DOCUMENT ME!
     */
    public NodeArrayCursor(Node[] nodes, Context context) {
        this.nodes = nodes;
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
        context.getScript().getNodeStack(context.getScript().getStackType()).push(nodes[row]);

        if (nodes[row] instanceof ElementBase) {
            ((ElementBase) nodes[row]).open(context.getScript());
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void close() {
        if (nodes[row] instanceof ElementBase) {
            ((ElementBase) nodes[row]).close(context.getScript());
        }

        context.getScript().getNodeStack(context.getScript().getStackType()).pop();
    }

    /**
     * DOCUMENT ME!
     */
    public void save() {
        this.save = this.row;
    }

    /**
     * DOCUMENT ME!
     */
    public void rollback() {
        this.row = this.save;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasNext() {
        return row < (nodes.length - 1);
    }

    /**
     * DOCUMENT ME!
     */
    public void last() {
        row = nodes.length;
    }

    //    /**
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isRowBased() {
        return (this.nodes != null) && (this.nodes.length > 0) && this.nodes[0] instanceof RowNode;
    }
}
