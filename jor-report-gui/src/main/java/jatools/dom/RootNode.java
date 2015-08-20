package jatools.dom;

import jatools.dataset.Dataset;
import jatools.dataset.RangeRowSet;
import jatools.dataset.RowSet;

import jatools.dom.src.RootNodeSource;

import jatools.engine.script.DebugOff;
import jatools.engine.script.KeyStack;
import jatools.engine.script.Script;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import bsh.CallStack;
import bsh.Interpreter;
import bsh.UtilEvalError;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.6 $
  */
public class RootNode extends DatasetBasedNode {
    /****************************************************************
     * Properties
     */

    /**
     * Static constant that holds the string identification for the result-set property;
     * The constant is used to identify the properties in property / vetoable change events.
     */
    private static final String _propertyDataset = "Dataset";

    /**
     * Static constant that holds the string identification for the result-set element property;
     * The constant is used to identify the properties in property / vetoable change events.
     */
    protected static final String _propertyDatasetElement = "DatasetElement";

    /**
     * Static constant that holds the string identification for the result-set dumb-array property;
     * The constant is used to identify the properties in property / vetoable change events.
     */
    protected static final String _propertyDatasetDumbArray = "DatasetDumbArray";

    /****************************************************************
     * Constants
     */
    public final static short NOT_IMPL_ERROR_CODE = DOMException.NO_MODIFICATION_ALLOWED_ERR;
    private RowSet index;

    //    //    private DatasetBasedNode[] _children = null;
    //    private Script script;

    /**
     * Constructs a new result-set document
     */
    public RootNode(Node p) {
        this._parent = (DatasetBasedNode) p;
    }

    /**
     * Returns the result-set property
     * @return        The result-set reference.
     */
    @DebugOff
    public synchronized Dataset getDataset() {
        return null;
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
     * @see org.w3c.dom.Node#getNodeType()
     */

    //    public short getNodeType() {
    //        return Node.DOCUMENT_NODE;
    //    }

    /**
     * @see org.w3c.dom.Node#getLocalName()
     */
    public String getLocalName() {
        return "root";
    }

    /**
     * @see org.w3c.dom.Node#getNamespaceURI()
     */
    @DebugOff
    public String getNamespaceURI() {
        return null;
    }

    /**
     * @see org.w3c.dom.Node#getPrefix()
     */
    @DebugOff
    public String getPrefix() {
        return null;
    }

    /**
     * @see org.w3c.dom.Node#getPreviousSibling()
     */
    public Node getPreviousSibling() {
        return null;
    }

    /****************************************************************
     * ArrayBasedElement impl.
     */

    /**
     * @see com.mercury.topaz.tdm.adapters.dbAdapter.DatasetBasedNode#getChildElementsLocalName()
     */
    @DebugOff
    public String getChildElementsLocalName() {
        return null;
    }

    /**
     * @see com.manspace.DatasetDOMWrapper.DatasetBasedNode#clearElements()
     */
    public void clear() {
        //        if (_DatasetElement != null) {
        //            _DatasetElement.clear();
        //        }
        //
        //        setDatasetDumbArray(null);
        //        setDatasetElement(null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        try {
            //            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            //
            //            SqlReader r = (SqlReader) TestDataset.getReader("select * from country");
            //            Dataset ds = r.read(null, -1);
            //            Document document = new DatasetElement(ds, new Group[] {
            //                        new Group("CONTINENT", 0)
            //                    });
            //
            //            TransformerFactory tFactory = TransformerFactory.newInstance();
            //            Transformer transformer = tFactory.newTransformer();
            //            DOMSource source = new DOMSource(document);
            //            StreamResult result = new StreamResult(new FileOutputStream("d:/x.xml"));
            //            transformer.transform(source, result);
            System.out.println(new Object[0].equals(new Object[0]));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node getNextSibling() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    //    public int getRowTo() {
    //        return 1; //_Dataset.getRowCount() - 1;
    //    }
    //
    //    /**
    //     * DOCUMENT ME!
    //     *
    //     * @return DOCUMENT ME!
    //     */
    //    public int getRowFrom() {
    //        // TODO Auto-generated method stub
    //        return 0;
    //    }

    //    /**
    //     * DOCUMENT ME!
    //     *
    //     * @param de DOCUMENT ME!
    //     */
    //    public void setDatasetElement(DatasetNode de) {
    //        _children = new DatasetBasedNode[] {
    //                de
    //            };
    //    }

    /**
     * DOCUMENT ME!
     *
     * @param script DOCUMENT ME!
     */
    public void open(Script script) {
        script.getKeyStack(KeyStack.ROW).push(getKey());
    }

    /**
     * DOCUMENT ME!
     *
     * @param script DOCUMENT ME!
     */
    public void close(Script script) {
        script.getKeyStack(KeyStack.ROW).pop();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public Document getOwnerDocument() {
        return _parent.getOwnerDocument();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public boolean isCached() {
        return ((RootNodeSource) this.getSource()).isCached();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public RowSet getRowSet() {
        if (this.index == null) {
            this.index = new RangeRowSet(this.getDataset(), getKey(), 0,
                    this.getDataset().getRowCount() - 1);
        }

        return this.index;
    }

	public Object getProperty(String prop, CallStack callstack,
			Interpreter interpreter) throws UtilEvalError {
		// TODO Auto-generated method stub
		return null;
	}
}
