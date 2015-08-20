package jatools.dom.src;

import jatools.accessor.PropertyDescriptor;
import jatools.component.ComponentConstants;
import jatools.dataset.CrossIndexView;
import jatools.dom.CrossIndexNode;
import jatools.dom.DatasetBasedNode;
import jatools.engine.script.Script;

import org.w3c.dom.Node;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class CrossIndexNodeSource extends IndexNodeSource {
    String[] indexFields2;

    /**
     * Creates a new DatasetNodeSource object.
     *
     * @param reader DOCUMENT ME!
     */
    public CrossIndexNodeSource(String tag, String[] indexFields, String[] indexFields2) {
        super(tag, indexFields);
        this.indexFields2 = indexFields2;
    }

    /**
     * Creates a new DatasetNodeSource object.
     */
    public CrossIndexNodeSource() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param groups DOCUMENT ME!
     */
    public void layoutGroup(String[] groups) {
        this.indexFields = new String[groups.length];
        System.arraycopy(groups,0,this.indexFields,0,groups.length);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] {
            ComponentConstants._CHILDREN,
            new PropertyDescriptor("IndexFields", String[].class, PropertyDescriptor.SERIALIZABLE),
            new PropertyDescriptor("IndexFields2", String[].class, PropertyDescriptor.SERIALIZABLE),
            new PropertyDescriptor("TagName", String.class, PropertyDescriptor.SERIALIZABLE),
            new PropertyDescriptor("KeyExpression", String.class, PropertyDescriptor.SERIALIZABLE)
        };
    }

    /**
    * DOCUMENT ME!
    *
    * @param script DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public Node create(Node parent, Script script) {
        DatasetBasedNode n = (DatasetBasedNode) parent;
        int[] cols = n.getDataset().getRowInfo().getIndexByColumnName(this.indexFields);
        int[] cols2 = n.getDataset().getRowInfo().getIndexByColumnName(this.indexFields2);

        CrossIndexView crossView = new CrossIndexView(n.getRowSet(), cols, cols2);
        CrossIndexNode result = new CrossIndexNode(n, this.getTagName(), crossView);
        result.setSource(this);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String[] getIndexFields2() {
        return indexFields2;
    }

    /**
     * DOCUMENT ME!
     *
     * @param indexFields DOCUMENT ME!
     */
    public void setIndexFields2(String[] indexFields2) {
        this.indexFields2 = indexFields2;
    }
}
