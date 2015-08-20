package jatools.dom;

import jatools.dom.field.NodeProvider;
import bsh.ValueAlways;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class NodeValue implements ValueAlways {
    NodeProvider e;
    private ElementBase node;

    /**
     * Creates a new NodeValue object.
     *
     * @param e DOCUMENT ME!
     */
    public NodeValue(NodeProvider e) {
        this.e = e;
    }

    /**
     * Creates a new NodeValue object.
     *
     * @param node DOCUMENT ME!
     */
    public NodeValue(ElementBase node) {
        this.node = node;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object value() {
        ElementBase e1 = null;

        if (this.node != null) {
            e1 = this.node;
        } else {
            e1 = (ElementBase) this.e.getNode();
        }

        return e1.value();
    }
}
