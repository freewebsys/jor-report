package jatools.xml.serializer;

import jatools.xml.XmlBase;
import jatools.xml.XmlSerializer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * 
 * DOCUMENT ME!
 * 
 * @version $Revision: 1.1 $
 * @author $author$
 * 
 */

public abstract class AbstractSerializer implements XmlSerializer {
	


	/**
	 * DOCUMENT ME!
	 * 
	 * @param e
	 *            DOCUMENT ME!
	 * @param object
	 *            DOCUMENT ME!
	 */
	protected static void setAttribute(Element e, String name, Object object) {
		e.setAttribute(name, object.toString());
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param e
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	protected static String getAttribute(Element e, String name) {
		return e.getAttribute(name);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param doc
	 *            DOCUMENT ME!
	 * @param tag
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	final public Node writeAsNode(Document doc, String tag) {
		return null;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param e
	 *            DOCUMENT ME!
	 * @param object
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	protected Element createObjectHeader(Element e, Object object) {
		Element objectNode = getDocument(e).createElement(
				XmlBase.OBJECT_NODE_TAG);
		objectNode.setAttribute(XmlBase.CLASS_ATTRIBUTE_TAG, object.getClass()
				.getName());

		Element fieldsNode = getDocument(e).createElement(
				XmlBase.FIELDS_NODE_TAG);
		objectNode.appendChild(fieldsNode);
		e.appendChild(objectNode);

		return fieldsNode;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param e
	 *            DOCUMENT ME!
	 * @param object
	 *            DOCUMENT ME!
	 */
	/**
	 * DOCUMENT ME!
	 * 
	 * @param e
	 *            DOCUMENT ME!
	 * @param name
	 *            DOCUMENT ME!
	 * @param text
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	protected Element createTextFieldNode(Element e, String name, String text) {
		Element fieldNode = createFieldNode(e, name);

		Node textNode = getDocument(e).createTextNode(text);

		fieldNode.appendChild(textNode);

		return fieldNode;
	}

	protected void appendText(Element e, String text) {
		Node textNode = getDocument(e).createTextNode(text);
		e.appendChild(textNode);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param e
	 *            DOCUMENT ME!
	 * @param name
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	protected Element createFieldNode(Element e, String name) {
		Element fieldNode = getDocument(e).createElement(name);

		return fieldNode;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param node
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	protected Document getDocument(Node node) {
		return (node instanceof Document) ? (Document) node : node
				.getOwnerDocument();
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param e
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	protected NodeList getFieldNodeList(Element e) {
		try {
			return e.getChildNodes();
		} catch (Exception ex) {
			ex.printStackTrace();

			return null;
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param e
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	protected String getText(Element e) {
		if (e.getFirstChild() != null)
			return e.getFirstChild().getNodeValue();
		else
			return null;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param e
	 *            DOCUMENT ME!
	 * @param text
	 *            DOCUMENT ME!
	 */
	protected void setText(Element e, String text) {
		Node textNode = getDocument(e).createTextNode(text);
		e.appendChild(textNode);
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param e
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	protected int intValue(Element e) {
		return Integer.parseInt(getText(e));
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param e
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	protected float floatValue(Element e) {
		return Float.parseFloat(getText(e));
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param e
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	protected double doubleValue(Element e) {
		return Double.parseDouble(getText(e));
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param e
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	protected long longValue(Element e) {
		return Long.parseLong(getText(e));
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param e
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Object read(Element e) {
		return null;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param node
	 *            DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
	public Object readFromNode(Node node) {
		return null;
	}
}
