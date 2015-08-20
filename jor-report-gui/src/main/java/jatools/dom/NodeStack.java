package jatools.dom;

import bsh.NameSpace;

import org.w3c.dom.Node;

import java.util.Stack;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class NodeStack {
    NameSpace namespace;
    Stack nodes = new Stack();

    /**
     * Creates a new NodeStack object.
     *
     * @param ns DOCUMENT ME!
     */
    public NodeStack(NameSpace ns) {
        this.namespace = ns;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node getNode() {
        return (Node) nodes.peek();
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     */
    public void push(Node node) {
        this.nodes.push(node);

        //        if (node != null) {
        //            setNodeVariable(node);
        //        }
    }

    //    private void setNodeVariable(Node node) {
    //        if (node.getLocalName() != null) {
    //            try {
    //                String varName = "$" + node.getLocalName();
    //                Object x = this.namespace.getLocalVariable(varName);
    //
    //                if (x instanceof NodeVariable) {
    //                    NodeVariable var = (NodeVariable) x;
    //                    var.push(node);
    //                } else {
    //                    this.namespace.setVariable(varName, new NodeVariable(node), false, false);
    //                }
    //            } catch (UtilEvalError e) {
    //                e.printStackTrace();
    //            }
    //        }
    //    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node getRoot() {
        return (Node) this.nodes.firstElement();
    }

    /**
     * DOCUMENT ME!
     */
    public void pop() {
        Object o = this.nodes.pop();

        //        if (o != null) {
        //            Node node = (Node) o;
        //
        //            if (node.getLocalName() != null) {
        //                String varName = "$" + node.getLocalName();
        //
        //                Object x = this.namespace.getLocalVariable(varName);
        //
        //                if (x instanceof NodeVariable) {
        //                    NodeVariable var = (NodeVariable) x;
        //                    var.pop();
        //                }
        //            }
        //        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node get(int i) {
        if ((i >= 0) && (i < this.nodes.size())) {
            return (Node) this.nodes.get(i);
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int size() {
        return nodes.size();
    }
}
