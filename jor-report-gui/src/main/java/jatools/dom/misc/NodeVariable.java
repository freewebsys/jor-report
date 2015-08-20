package jatools.dom.misc;

import jatools.dom.field.NodeProvider;
import bsh.ValueAlways;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class NodeVariable implements ValueAlways {
    private NodeProvider provider;

    /**
     * Creates a new NodeVariable object.
     *
     * @param n DOCUMENT ME!
     */
    public NodeVariable(NodeProvider n) {
        this.provider = n;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object value() {
        return provider.getNode();
    }
}
