package jatools.dom.field;

import jatools.dom.DatasetNode;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class DatasetField extends AbstractValuesField {
    private int col;
    DatasetNode node;

    /**
     * Creates a new NodeField object.
     *
     * @param col
     *            DOCUMENT ME!
     * @param nodestack
     *            DOCUMENT ME!
     */
    public DatasetField(int col, DatasetNode node) {
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
