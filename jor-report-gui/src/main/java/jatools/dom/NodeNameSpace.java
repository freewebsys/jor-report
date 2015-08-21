package jatools.dom;

import jatools.dom.field.NodeProvider;

import java.util.Stack;

import org.w3c.dom.Node;

import bsh.NameSpace;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class NodeNameSpace extends NameSpace implements NodeProvider {
    Stack stack = new Stack();

    /**
     * Creates a new NodeNameSpace object.
     */
    public NodeNameSpace() {
        super("");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node getNode() {
        return (Node) stack.peek();
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     */
    public void push(Node node) {
        stack.push(node);
    }

    /**
     * DOCUMENT ME!
     */
    public void pop() {
        stack.pop();
    }
}
