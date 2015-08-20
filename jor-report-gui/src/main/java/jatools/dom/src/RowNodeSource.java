package jatools.dom.src;

import jatools.accessor.PropertyDescriptor;

import jatools.component.ComponentConstants;

import jatools.dom.DatasetBasedNode;
import jatools.dom.RowList;
import jatools.dom.RowNode;

import jatools.engine.script.Script;

import org.w3c.dom.Node;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.6 $
  */
public class RowNodeSource extends NodeSource {
    /**
     * DOCUMENT ME!
     */
    public static final String TAG_NAME = "Row";

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTagName() {
        return TAG_NAME;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param script DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node create(Node parent, Script script) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param script DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node[] createNodeList(Node parent, Script script) {
        DatasetBasedNode n = (DatasetBasedNode) parent;
        RowList list = n.getRowList();
        RowNode[] rows = new RowNode[list.length()];

        for (int i = 0; i < rows.length; i++) {
            rows[i] = new RowNode(n, list.rowAt(i));
            //     rows[i].setKeyExpression(this.getKeyExpression());
            rows[i].setSource(this);
        }

        return rows;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] {
            ComponentConstants._CHILDREN,
            new PropertyDescriptor("KeyExpression", String.class, PropertyDescriptor.SERIALIZABLE)
        };
    }
}
