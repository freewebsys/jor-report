package jatools.dom.field;

import jatools.dom.GroupNode;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class GroupField extends AbstractValuesField {
    private int col;
    GroupNode node;

    /**
     * Creates a new NodeField object.
     *
     * @param col
     *            DOCUMENT ME!
     * @param nodestack
     *            DOCUMENT ME!
     */
    public GroupField(int col, GroupNode node) {
        this.col = col;
        this.node = node;
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
     * @return DOCUMENT ME!
     */
    public Object[] values() {
        Object vals = node.valuesAt(col);

        return (Object[]) ((vals == null) ? EMPTY_ARRAY : vals);
    }
}
