package jatools.dom;

import bsh.CallStack;
import bsh.Interpreter;
import bsh.Primitive;

import jatools.dataset.Dataset;
import jatools.dataset.Key;
import jatools.dataset.RangeRowSet;
import jatools.dataset.RowBuffer;
import jatools.dataset.RowSet;

import jatools.dom.field.RowField;

import jatools.dom.src.RowNodeSource;

import org.w3c.dom.Document;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.10 $
  */
public class RowNode extends DatasetBasedNode {
    protected static final String _propertyDatasetElement = "DatasetElement";
    protected static final String _propertyRow = "row";
    private int _row = 0;
    Key key;
    private RangeRowSet index;
    private RowBuffer buffer;

    /**
     * Creates a new RowNode object.
     *
     * @param parent DOCUMENT ME!
     * @param row DOCUMENT ME!
     */
    public RowNode(DatasetBasedNode parent, int row) {
        setParent(parent);
        setRow(row);
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     */
    public void setParent(DatasetBasedNode parent) {
        _parent = parent;
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
    public /*synchronized*/ int getRow() {
        return (_row);
    }

    private /*synchronized*/ void setRow(int newRow) {
        _row = newRow;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getLocalName() {
        return RowNodeSource.TAG_NAME;
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
    public Document getOwnerDocument() {
        return _parent.getOwnerDocument();
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
        Dataset data = getDataset();

        if (data != null) {
            int col = data.getColumnIndex(prop);

            if (col > -1) {
                return this.getDatasetRoot().getRowField(col).setNode(this);
            }
        }

        return Primitive.VOID;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPrefix() {
        return _parent.getPrefix();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNew() {
        return (this.getRow() == 0) && this.getDataset().isPaddingFirst();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getChildElementsLocalName() {
        return "Cell";
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
    public Dataset getDataset() {
        return getDatasetRoot().getDataset();
    }

    /**
     * DOCUMENT ME!
     *
     * @param col DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValueAt(int col) {
        return this.getDataset().getValueAt(this._row, col);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RowSet getRowSet() {
        if (this.index == null) {
            this.index = new RangeRowSet(this.getDataset(), null, this._row, this._row);
        }

        return this.index;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RowBuffer getBuffer() {
        return buffer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param buffer DOCUMENT ME!
     */
    public void setBuffer(RowBuffer buffer) {
        this.buffer = buffer;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //    public int getAction() {
    //        return buffer.getAction();
    //    }
    //
    //    /**
    //     * DOCUMENT ME!
    //     *
    //     * @return DOCUMENT ME!
    //     */
    //    public boolean isDeleted() {
    //        return buffer.getAction() == RowBuffer.DELETED;
    //    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RowNode getMaster() {
        return (RowNode) (this.getParentNode().getParentNode());
    }
}
