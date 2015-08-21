package jatools.dom;

import jatools.dataset.Dataset;
import jatools.dataset.Key;
import jatools.dataset.RowSet;
import jatools.dom.field.GroupField;

import org.apache.commons.lang.StringUtils;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import bsh.CallStack;
import bsh.Interpreter;
import bsh.Primitive;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.7 $
 */
public class GroupNode extends DatasetBasedNode {
    private String _tag;
    String childLocalName;
    FieldMap f;

    // GroupView indexSet;
    private RowSet rowset;

    /**
     * Creates a new GroupElement object.
     *
     * @param parent
     *            DOCUMENT ME!
     * @param tag
     *            DOCUMENT ME!
     * @param keys
     *            DOCUMENT ME!
     */
    public GroupNode(DatasetBasedNode parent, String tag, RowSet index) {
        _parent = parent;
        _tag = tag;
        this.rowset = index;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parentKeys
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    // public Object[] getSubGroupKeys(Object[] parentKeys) {
    // return this.indexSet.getSubGroupKeys(parentKeys);
    // }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dataset getDataset() {
        return this.getDatasetRoot().getDataset();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getChildElementsLocalName() {
        return null;
    }

    /**
     * DOCUMENT ME!
     */
    public void clear() {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Document getOwnerDocument() {
        return this._parent.getOwnerDocument();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getNamespaceURI() {
        return null; // this._parent.getNamespaceURI();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPrefix() {
        return this._parent.getPrefix();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getLocalName() {
        return _tag;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getUserData() {
        return this.rowset.key().lastValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NamedNodeMap getAttributes() {
        if (f == null) {
            f = new FieldMap();
        }

        return f;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Key getKey() {
        Key key = super.getKey();

        if (key != Dataset.ANY) {
            return key;
        } else {
            return this.rowset.key();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object value() {
        return this.getUserData();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return StringUtils.join(this.getKey().elements, ",");
    }

    // public String toString()
    // {
    // return "aa";
    // }

    // /**
    // * DOCUMENT ME!
    // *
    // * @param indexSet DOCUMENT ME!
    // */
    // public void setIndexSet(GroupView indexSet) {
    // this.indexSet = indexSet;
    // }
    //
    // /**
    // * DOCUMENT ME!
    // *
    // * @return DOCUMENT ME!
    // */
    // public GroupView getIndexSet() {
    // return this.indexSet;
    // }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RowSet getRowSet() {
        return this.rowset;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRowCount() {
        return this.rowset.length();
    }

    class FieldMap implements NamedNodeMap {
        FieldAttr fa;

        /**
         * DOCUMENT ME!
         *
         * @param arg0
         *            DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Node getNamedItem(String arg0) {
            if (fa == null) {
                fa = new FieldAttr(GroupNode.this);
            }

            return fa;
        }

        /**
         * DOCUMENT ME!
         *
         * @param arg0
         *            DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws DOMException
         *             DOCUMENT ME!
         */
        public Node setNamedItem(Node arg0) throws DOMException {
            return null;
        }

        /**
         * DOCUMENT ME!
         *
         * @param arg0
         *            DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws DOMException
         *             DOCUMENT ME!
         */
        public Node removeNamedItem(String arg0) throws DOMException {
            // TODO Auto-generated method stub
            return null;
        }

        /**
         * DOCUMENT ME!
         *
         * @param arg0
         *            DOCUMENT ME!
         * @param arg1
         *            DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         */
        public Node getNamedItemNS(String arg0, String arg1) {
            return getNamedItem(arg0);
        }

        /**
         * DOCUMENT ME!
         *
         * @param arg0
         *            DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws DOMException
         *             DOCUMENT ME!
         */
        public Node setNamedItemNS(Node arg0) throws DOMException {
            // TODO Auto-generated method stub
            return null;
        }

        /**
         * DOCUMENT ME!
         *
         * @param arg0
         *            DOCUMENT ME!
         * @param arg1
         *            DOCUMENT ME!
         *
         * @return DOCUMENT ME!
         *
         * @throws DOMException
         *             DOCUMENT ME!
         */
        public Node removeNamedItemNS(String arg0, String arg1)
            throws DOMException {
            return null;
        }

        public Node item(int i) {
            return getNamedItem(null);
        }

        public int getLength() {
            return 1;
        }
    }
    
    public Object getProperty(String prop, CallStack callstack, Interpreter interpreter) {
        Dataset data = getDataset();

        if (data != null) {
            int col = data.getColumnIndex(prop);

            if (col > -1) {
                return new GroupField(col, this);
            }
        }

        return Primitive.VOID;
    }
}
