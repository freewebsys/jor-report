package jatools.dom.src;

import jatools.accessor.PropertyDescriptor;

import jatools.component.ComponentConstants;

import jatools.dataset.Dataset;
import jatools.dataset.IndexView;

import jatools.dom.DatasetBasedNode;
import jatools.dom.IndexNode;

import jatools.engine.script.Script;

import org.w3c.dom.Node;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class IndexNodeSource extends NodeSource {
    String[] indexFields;
    private String tag;

    /**
     * Creates a new DatasetNodeSource object.
     *
     * @param reader DOCUMENT ME!
     */
    public IndexNodeSource(String tag, String[] indexFields) {
        this.tag = tag;
        this.indexFields = indexFields;
    }

    /**
     * Creates a new DatasetNodeSource object.
     */
    public IndexNodeSource() {
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
        Dataset ds = ((DatasetBasedNode) parent).getDataset();

        if ((this.indexFields != null) && (indexFields.length > 0)) {
            IndexView indexView = new IndexView(ds, ((DatasetBasedNode) parent).getRowSet(),
                    ds.getRowInfo().getIndexByColumnName(this.indexFields));
            IndexNode result = new IndexNode((DatasetBasedNode) parent, this.getTagName(), indexView);
            result.setSource(this);

            return result;
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String[] getIndexFields() {
        return indexFields;
    }

    /**
     * DOCUMENT ME!
     *
     * @param indexFields DOCUMENT ME!
     */
    public void setIndexFields(String[] indexFields) {
        this.indexFields = indexFields;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTagName() {
        return this.tag;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public void setTagName(String tag) {
        this.tag = tag;
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
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNodeListSource() {
        return false;
    }
}
