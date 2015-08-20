package jatools.engine.script;

import jatools.dom.NodeStack;

import org.w3c.dom.Node;

import bsh.ValueAlways;




/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class AxisNode implements ValueAlways {
    private NodeStack nodeStack;

    /**
     * Creates a new AxisNode object.
     *
     * @param nodeStack DOCUMENT ME!
     */
    public AxisNode(NodeStack nodeStack) {
        this.nodeStack = nodeStack;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object value() {
        Node node = this.nodeStack.getNode();

        return node;
    }
}
