package jatools.dom.field;

import bsh.Interpreter;

import jatools.dom.DatasetBasedNode;
import jatools.engine.InterpreterAware;

import org.w3c.dom.Element;
import org.w3c.dom.Node;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class NodeField extends AbstractField implements InterpreterAware {
    private int col;
    NodeProvider nodestack;
    private Interpreter it;

    /**
     * Creates a new NodeField object.
     *
     * @param col DOCUMENT ME!
     * @param nodestack DOCUMENT ME!
     */
    public NodeField(int col, NodeProvider nodestack) {
        this.col = col;
        this.nodestack = nodestack;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumn() {
        return col;
    }

    /**
     * DOCUMENT ME!
     *
     * @param it DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object value() {
        Node node = this.getNode();

        if (node instanceof DatasetBasedNode) {
            DatasetBasedNode de = (DatasetBasedNode) node;

            it.getRoot().setValue2(this);

            return de.valueAt(col);
        } else if (node instanceof Element) {
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node getNode() {
        return this.nodestack.getNode();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] values() {
        Node node = this.getNode();

        if (node instanceof DatasetBasedNode) {
            DatasetBasedNode de = (DatasetBasedNode) node;

            return de.valuesAt(col);
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object pre() {
        Node n = (DatasetBasedNode) getNode().getPreviousSibling();

        if (n != null) {
            return new FixedNodeField(getColumn(), n);
        } else {
            return null;
        }
    }
    public void setInterpreter(Interpreter it) {
        this.it = it;
    }
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object next() {
        Node n = getNode().getNextSibling();

        if (n != null) {
            return new FixedNodeField(getColumn(), n);
        } else {
            return null;
        }
    }
}
