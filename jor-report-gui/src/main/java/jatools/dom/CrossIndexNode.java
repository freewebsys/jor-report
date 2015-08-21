package jatools.dom;

import jatools.dataset.CrossIndexView;
import jatools.dataset.Dataset;
import jatools.dataset.RowSet;

import org.w3c.dom.Document;

import bsh.CallStack;
import bsh.Interpreter;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.6 $
  */
public class CrossIndexNode extends DatasetBasedNode {
    private String tag;
    private CrossIndexView indexView;
    private CrossFieldsCache cache;

    /**
     * Creates a new CrossIndexNode object.
     *
     * @param parent DOCUMENT ME!
     * @param tag DOCUMENT ME!
     * @param indexView DOCUMENT ME!
     */
    public CrossIndexNode(DatasetBasedNode parent, String tag, CrossIndexView indexView) {
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
    public String getChildElementsLocalName() {
        return null;
    }

    /**
     * DOCUMENT ME!
     */
    public void clear() {
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
        return null;
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
        if (this.cache == null) {
            this.cache = new CrossFieldsCache(this, this.indexView);
        }

        return this.cache.getProperty(prop);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CrossGroupList getGroups() {
        return new CrossGroupList(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CrossIndexView getIndexView() {
        return indexView;
    }
}
