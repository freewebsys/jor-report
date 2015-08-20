package jatools.dom.field;

import org.w3c.dom.Node;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class FixedNodeField extends NodeField {
    Node _node;

    /**
     * Creates a new FixedNodeField object.
     *
     * @param col DOCUMENT ME!
     * @param n DOCUMENT ME!
     */
    public FixedNodeField(int col, Node n) {
        super(col, null);

        _node = n;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node getNode() {
        return _node;
    }
}
