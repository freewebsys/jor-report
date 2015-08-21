package jatools.dom.field;

import bsh.Interpreter;

import jatools.dom.RowNode;

import jatools.engine.InterpreterAware;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class RowField extends AbstractValueField implements InterpreterAware {
    private int col;
    RowNode node;
    private Interpreter it;

    /**
     * Creates a new NodeField object.
     *
     * @param col DOCUMENT ME!
     * @param nodestack DOCUMENT ME!
     */
    public RowField(int col, RowNode node) {
        this.col = col;
        this.node = node;
    }

    /**
     * Creates a new RowField object.
     *
     * @param col DOCUMENT ME!
     */
    public RowField(int col) {
        this.col = col;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RowNode getNode() {
        return node;
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     */
    public RowField setNode(RowNode node) {
        this.node = node;

        return this;
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
        it.getRoot().setValue2(this);

        return node.valueAt(col);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object pre() {
        RowNode n = (RowNode) node.getPreviousSibling();

        if (n != null) {
            return new RowField(getColumn(), n);
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param it DOCUMENT ME!
     */
    public void setInterpreter(Interpreter it) {
        this.it = it;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object next() {
        RowNode n = (RowNode) node.getNextSibling();

        if (n != null) {
            return new RowField(getColumn(), n);
        } else {
            return null;
        }
    }
}
