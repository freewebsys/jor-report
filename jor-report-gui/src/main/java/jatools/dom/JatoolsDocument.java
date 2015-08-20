package jatools.dom;

import jatools.dataset.Dataset;
import jatools.dataset.RangeRowSet;
import jatools.dataset.RowSet;

import jatools.dom.src.DocumentNodeSource;
import jatools.dom.src.RootNodeSource;

import jatools.engine.script.DebugOff;
import jatools.engine.script.KeyStack;
import jatools.engine.script.Script;

import org.w3c.dom.Attr;
import org.w3c.dom.CDATASection;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMConfiguration;
import org.w3c.dom.DOMException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;

import bsh.CallStack;
import bsh.Interpreter;
import bsh.UtilEvalError;

import java.util.HashMap;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.6 $
 */
public class JatoolsDocument extends DatasetBasedNode implements Document, Root {
    /****************************************************************
     * Properties
     */

    /**
     * Static constant that holds the string identification for the result-set
     * property; The constant is used to identify the properties in property
     * vetoable change events.
     */
    protected static final String _propertyDataset = "Dataset";

    /**
     * Static constant that holds the string identification for the result-set
     * element property; The constant is used to identify the properties in
     * property / vetoable change events.
     */
    protected static final String _propertyDatasetElement = "DatasetElement";

    /**
     * Static constant that holds the string identification for the result-set
     * dumb-array property; The constant is used to identify the properties in
     * property / vetoable change events.
     */
    protected static final String _propertyDatasetDumbArray = "DatasetDumbArray";

    /****************************************************************
     * Constants
     */
    public final static short NOT_IMPL_ERROR_CODE = DOMException.NO_MODIFICATION_ALLOWED_ERR;

    // private DatasetBasedNode[] _children = null;
    private Script script;
    private RangeRowSet index;
    private HashMap elementCache;

    /**
     * Constructs a new result-set document
     */
    public JatoolsDocument(RootNodeSource rootSrc, Script script) {
        this.script = script;
        this.setSource(new DocumentNodeSource(rootSrc));
    }

    /**
     * Returns the result-set property
     *
     * @return The result-set reference.
     */
    public synchronized Dataset getDataset() {
        return null;
    }

    // protected synchronized DatasetBasedNode[] getDatasetDumbArray() {
    // return _children;
    // }

    /**
     * DOCUMENT ME!
     *
     * @param name
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException
     *             DOCUMENT ME!
     */
    public Attr createAttribute(String name) throws DOMException {
        throw new DOMException(DatasetNode.NOT_IMPL_ERROR_CODE, DatasetNode.NOT_IMPL_ERROR_MSG);
    }

    // /**
    // * DOCUMENT ME!
    // *
    // * @param root DOCUMENT ME!
    // */
    // public void setRoot(Element root) {
    // this.children = new ElementBase[] {
    // (ElementBase) root
    // };
    // }

    /**
     * @see org.w3c.dom.Document#createAttributeNS(java.lang.String,
     *      java.lang.String)
     */
    public Attr createAttributeNS(String namespaceURI, String qualifiedName)
        throws DOMException {
        throw new DOMException(DatasetNode.NOT_IMPL_ERROR_CODE, DatasetNode.NOT_IMPL_ERROR_MSG);
    }

    /**
     * @see org.w3c.dom.Document#createCDATASection(java.lang.String)
     */
    public CDATASection createCDATASection(String data)
        throws DOMException {
        throw new DOMException(DatasetNode.NOT_IMPL_ERROR_CODE, DatasetNode.NOT_IMPL_ERROR_MSG);
    }

    /**
     * @see org.w3c.dom.Document#createComment(java.lang.String)
     */
    public Comment createComment(String data) {
        throw new DOMException(DatasetNode.NOT_IMPL_ERROR_CODE, DatasetNode.NOT_IMPL_ERROR_MSG);
    }

    /**
     * @see org.w3c.dom.Document#createDocumentFragment()
     */
    public DocumentFragment createDocumentFragment() {
        throw new DOMException(DatasetNode.NOT_IMPL_ERROR_CODE, DatasetNode.NOT_IMPL_ERROR_MSG);
    }

    /**
     * @see org.w3c.dom.Document#createElement(java.lang.String)
     */
    public Element createElement(String tagName) throws DOMException {
        throw new DOMException(DatasetNode.NOT_IMPL_ERROR_CODE, DatasetNode.NOT_IMPL_ERROR_MSG);
    }

    /**
     * @see org.w3c.dom.Document#createElementNS(java.lang.String,
     *      java.lang.String)
     */
    public Element createElementNS(String namespaceURI, String qualifiedName)
        throws DOMException {
        throw new DOMException(DatasetNode.NOT_IMPL_ERROR_CODE, DatasetNode.NOT_IMPL_ERROR_MSG);
    }

    /**
     * @see org.w3c.dom.Document#createEntityReference(java.lang.String)
     */
    public EntityReference createEntityReference(String name)
        throws DOMException {
        throw new DOMException(DatasetNode.NOT_IMPL_ERROR_CODE, DatasetNode.NOT_IMPL_ERROR_MSG);
    }

    /**
     * @see org.w3c.dom.Document#createProcessingInstruction(java.lang.String,
     *      java.lang.String)
     */
    public ProcessingInstruction createProcessingInstruction(String target, String data)
        throws DOMException {
        throw new DOMException(DatasetNode.NOT_IMPL_ERROR_CODE, DatasetNode.NOT_IMPL_ERROR_MSG);
    }

    /**
     * @see org.w3c.dom.Document#createTextNode(java.lang.String)
     */
    public Text createTextNode(String data) {
        throw new DOMException(DatasetNode.NOT_IMPL_ERROR_CODE, DatasetNode.NOT_IMPL_ERROR_MSG);
    }

    /**
     * @see org.w3c.dom.Document#importNode(org.w3c.dom.Node, boolean)
     */
    public Node importNode(Node importedNode, boolean deep)
        throws DOMException {
        throw new DOMException(DatasetNode.NOT_IMPL_ERROR_CODE, DatasetNode.NOT_IMPL_ERROR_MSG);
    }

    /**
     * @see org.w3c.dom.Document#getDoctype()
     */
    @DebugOff
    public DocumentType getDoctype() {
        return null;
    }

    /**
     * @see org.w3c.dom.Document#getDocumentElement()
     */
    @DebugOff
    public Element getDocumentElement() {
        return null; // getDatasetElement();
    }

    /**
     * @see org.w3c.dom.Document#getElementById(java.lang.String)
     */
    public Element getElementById(String elementId) {
        return null;
    }

    /**
     * @see org.w3c.dom.Document#getImplementation()
     */
    @DebugOff
    public DOMImplementation getImplementation() {
        return null;
    }

    /****************************************************************
     * Node impl.
     */

    /**
     * @see org.w3c.dom.Node#getNodeType()
     */
    @DebugOff
    public short getNodeType() {
        return Node.DOCUMENT_NODE;
    }

    /**
     * @see org.w3c.dom.Node#getLocalName()
     */
    @DebugOff
    public String getLocalName() {
        return null;
    }

    /**
     * @see org.w3c.dom.Node#getNamespaceURI()
     */
    @DebugOff
    public String getNamespaceURI() {
        return "http://dom-result-set.sf.net/result-set-uri/";
    }

    /**
     * @see org.w3c.dom.Node#getOwnerDocument()
     */
    @DebugOff
    public Document getOwnerDocument() {
        return this;
    }

    /**
     * @see org.w3c.dom.Node#getParentNode()
     */
    public Node getParentNode() {
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
        // if (_DatasetElement != null) {
        // _DatasetElement.clear();
        // }
        //
        // setDatasetDumbArray(null);
        // setDatasetElement(null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param args
     *            DOCUMENT ME!
     */
    public static void main(String[] args) {
        try {
            // DocumentBuilderFactory factory =
            // DocumentBuilderFactory.newInstance();
            //
            // SqlReader r = (SqlReader)
            // TestDataset.getReader("select * from country");
            // Dataset ds = r.read(null, -1);
            // Document document = new DatasetElement(ds, new Group[] {
            // new Group("CONTINENT", 0)
            // });
            //
            // TransformerFactory tFactory = TransformerFactory.newInstance();
            // Transformer transformer = tFactory.newTransformer();
            // DOMSource source = new DOMSource(document);
            // StreamResult result = new StreamResult(new
            // FileOutputStream("d:/x.xml"));
            // transformer.transform(source, result);
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

    // public int getRowTo() {
    // return 1; //_Dataset.getRowCount() - 1;
    // }
    //
    // /**
    // * DOCUMENT ME!
    // *
    // * @return DOCUMENT ME!
    // */
    // public int getRowFrom() {
    // // TODO Auto-generated method stub
    // return 0;
    // }

    // /**
    // * DOCUMENT ME!
    // *
    // * @param de DOCUMENT ME!
    // */
    // public void setDatasetElement(DatasetNode de) {
    // _children = new DatasetBasedNode[] {
    // de
    // };
    // }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public Script getScript() {
        return this.script;
    }

    /**
     * DOCUMENT ME!
     *
     * @param script
     *            DOCUMENT ME!
     */
    public void open(Script script) {
        script.getKeyStack(KeyStack.ROW).push(getKey());
    }

    /**
     * DOCUMENT ME!
     *
     * @param script
     *            DOCUMENT ME!
     */
    public void close(Script script) {
        script.getKeyStack(KeyStack.ROW).pop();
    }

    /**
     * DOCUMENT ME!
     *
     * @param script
     *            DOCUMENT ME!
     */
    public void setScript(Script script) {
        this.script = script;
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

    /**
     * DOCUMENT ME!
     *
     * @param node
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object registerElement(ElementBase node) {
        if (elementCache == null) {
            elementCache = new HashMap();
        }

        Object key = elementCache.size() + "";
        this.elementCache.put(key, node);

        return key;
    }

    /**
     * DOCUMENT ME!
     *
     * @param key
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getElement(Object key) {
        return this.elementCache.get(key);
    }

    /**
     * DOCUMENT ME!
     *
     * @param source DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public Node adoptNode(Node source) throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public String getDocumentURI() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public DOMConfiguration getDomConfig() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public String getInputEncoding() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public boolean getStrictErrorChecking() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public String getXmlEncoding() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public boolean getXmlStandalone() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public String getXmlVersion() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * DOCUMENT ME!
     */
    public void normalizeDocument() {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     * @param namespaceURI DOCUMENT ME!
     * @param qualifiedName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public Node renameNode(Node n, String namespaceURI, String qualifiedName)
        throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param documentURI DOCUMENT ME!
     */
    public void setDocumentURI(String documentURI) {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     *
     * @param strictErrorChecking DOCUMENT ME!
     */
    public void setStrictErrorChecking(boolean strictErrorChecking) {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     *
     * @param xmlStandalone DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void setXmlStandalone(boolean xmlStandalone)
        throws DOMException {
        // TODO Auto-generated method stub
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
     * @param xmlVersion DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void setXmlVersion(String xmlVersion) throws DOMException {
        // TODO Auto-generated method stub
    }

	public Object getProperty(String prop, CallStack callstack,
			Interpreter interpreter) throws UtilEvalError {
		
		return null;
	}
}
