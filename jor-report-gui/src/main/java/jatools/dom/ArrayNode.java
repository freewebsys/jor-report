package jatools.dom;

import bsh.CallStack;
import bsh.Interpreter;
import bsh.NameSpace;
import bsh.Primitive;
import bsh.UtilEvalError;

import jatools.dataset.Dataset;
import jatools.dataset.Key;

import jatools.engine.script.DebugOff;
import jatools.engine.script.Script;

import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.TypeInfo;
import org.w3c.dom.UserDataHandler;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.10 $
  */
public class ArrayNode extends ElementBase implements NodeList {
    private String _tag;
    private Object key;
    private ElementBase _parent;

    /**
     * Creates a new ArrayNode object.
     *
     * @param parent DOCUMENT ME!
     * @param tag DOCUMENT ME!
     * @param key DOCUMENT ME!
     */
    public ArrayNode(ElementBase parent, String tag, Object key) {
        _parent = parent;
        _tag = tag;
        this.key = key;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public String getChildElementsLocalName() {
        return null;
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
            return new Key(new Object[] { value() });
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public Document getOwnerDocument() {
        return this._parent.getOwnerDocument();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public String getNamespaceURI() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public String getPrefix() {
        return this._parent.getPrefix();
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
    public Object getClientProperty(String prop, CallStack callstack, Interpreter interpreter) {
        return Primitive.VOID;
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
    @DebugOff
    public Object getUserData() {
        return this.key;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return key + "";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object value() {
        return key;
    }

    /**
     * DOCUMENT ME!
     */
    public void clear() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param script DOCUMENT ME!
     */
    public void open(Script script) {
        NameSpace name = new NameSpace("");
        script.pushNameSpace(name);

        try {
            name.setLocalVariable("$" + getTagName(), new NodeValue(this));
            name.setLocalVariable(getLocalName(), this);

            //
            //            
            //            name.setLocalVariable(getLocalName(), this.key, false);
            //       name.setLocalVariable("$", this);
        } catch (UtilEvalError e) {
            e.printStackTrace();
        }

        //        String expr = this.getSource().getKeyExpression();
        //
        //        if (expr != null) {
        //            expr = "new Object[]{" + expr + "};";
        //
        //            Object[] vals = (Object[]) script.eval(expr);
        //            script.getKeyStack(script.getStackType()).push(new Key(vals));
        //        } else {
        script.getKeyStack(script.getStackType()).push(getKey());

        //        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param script DOCUMENT ME!
     */
    public void close(Script script) {
        script.getKeyStack(script.getStackType()).pop();
        script.popNameSpace();
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg0 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAttribute(String arg0) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg0 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Attr getAttributeNode(String arg0) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NodeList getElementsByTagName(String name) {
        if (name.endsWith(getChildElementsLocalName())) {
            return this;
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg0 DOCUMENT ME!
     * @param arg1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAttributeNS(String arg0, String arg1) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg0 DOCUMENT ME!
     * @param arg1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Attr getAttributeNodeNS(String arg0, String arg1) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param namespaceURI DOCUMENT ME!
     * @param localName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NodeList getElementsByTagNameNS(String namespaceURI, String localName) {
        if (namespaceURI.equals(getNamespaceURI()) &&
                (localName.equals(getChildElementsLocalName()))) {
            return this;
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg0 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasAttribute(String arg0) {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg0 DOCUMENT ME!
     * @param arg1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasAttributeNS(String arg0, String arg1) {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public String getNodeValue() throws DOMException {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node getParentNode() {
        return this._parent;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public NodeList getChildNodes() {
        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node getFirstChild() {
        if (getLength() <= 0) {
            return null;
        }

        return item(0);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node getLastChild() {
        if (getLength() <= 0) {
            return null;
        }

        return item(getLength() - 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node item(int index) {
        return getChildElements()[index];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLength() {
        return getChildElements().length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node getNextSibling() {
        Element[] siblings = _parent.getChildElements();
        int index = this.getPosition();

        if ((index + 1) < siblings.length) {
            return siblings[index + 1];
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node getPreviousSibling() {
        Element[] siblings = _parent.getChildElements();
        int index = this.getPosition();

        if (index > 0) {
            return siblings[index - 1];
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public NamedNodeMap getAttributes() {
        return EMPTY;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasChildNodes() {
        return (getLength() > 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg0 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node cloneNode(boolean arg0) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasAttributes() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public TypeInfo getSchemaTypeInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param isId DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void setIdAttribute(String name, boolean isId)
        throws DOMException {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     *
     * @param namespaceURI DOCUMENT ME!
     * @param localName DOCUMENT ME!
     * @param isId DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void setIdAttributeNS(String namespaceURI, String localName, boolean isId)
        throws DOMException {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     *
     * @param idAttr DOCUMENT ME!
     * @param isId DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void setIdAttributeNode(Attr idAttr, boolean isId)
        throws DOMException {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     *
     * @param other DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public short compareDocumentPosition(Node other) throws DOMException {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public String getBaseURI() {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param feature DOCUMENT ME!
     * @param version DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getFeature(String feature, String version) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    @DebugOff
    public String getTextContent() throws DOMException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public Object getUserData(String key) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param namespaceURI DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDefaultNamespace(String namespaceURI) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param arg DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEqualNode(Node arg) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param other DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSameNode(Node other) {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param prefix DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String lookupNamespaceURI(String prefix) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param namespaceURI DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String lookupPrefix(String namespaceURI) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param textContent DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void setTextContent(String textContent) throws DOMException {
        // TODO Auto-generated method stub
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param data DOCUMENT ME!
     * @param handler DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object setUserData(String key, Object data, UserDataHandler handler) {
        // TODO Auto-generated method stub
        return null;
    }
}
