package jatools.dom;

import jatools.dataset.Dataset;
import jatools.dataset.IndexView;
import jatools.dataset.RowSet;
import jatools.dom.field.IndexField;
import jatools.engine.script.KeyStack;

import org.w3c.dom.Document;

import bsh.CallStack;
import bsh.Interpreter;
import bsh.Primitive;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
  */
public class IndexNode extends DatasetBasedNode {
    private String tag;
    private IndexView indexView;

    /**
     * Creates a new GroupElement object.
     *
     * @param parent DOCUMENT ME!
     * @param tag DOCUMENT ME!
     * @param keys DOCUMENT ME!
     */
    public IndexNode(DatasetBasedNode parent, String tag, IndexView indexView) {
        _parent = parent;
        this.tag = tag;
        this.indexView = indexView;
    }

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
    public String toString() {
        return null;
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
        return null; //this._parent.getNamespaceURI();
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
        return tag;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //    public NamedNodeMap getAttributes() {
    //       
    //
    //        return null;
    //    }

    //    /**
    //     * DOCUMENT ME!
    //     *
    //     * @param indexSet DOCUMENT ME!
    //     */
    //    public void setIndexSet(GroupView indexSet) {
    //        this.indexSet = indexSet;
    //    }
    //
    //    /**
    //     * DOCUMENT ME!
    //     *
    //     * @return DOCUMENT ME!
    //     */
    //    public GroupView getIndexSet() {
    //        return this.indexSet;
    //    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RowSet getRowSet() {
        return this.getDataset().getRowSet();
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     * @param callstack DOCUMENT ME!
     * @param interpreter DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getProperty(String prop, CallStack callstack, Interpreter interpreter) {
        int col = getDataset().getColumnIndex(prop);

        if (col > -1) {
            KeyStack keyStack = this.getRoot().getScript()
                                    .getKeyStack(this.getRoot().getScript().getStackType());

            return new IndexField(this.indexView, col, keyStack);
        } else {
            return Primitive.VOID;
        }
    }
}
