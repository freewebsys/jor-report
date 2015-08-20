package jatools.xml.serializer;

import java.awt.Rectangle;

import javax.swing.JTree;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 *  DOCUMENT ME!
 * 
 *  @version $Revision: 1.2 $ZRectangleNodeDelegate
 *  @author $author$
 * 
 */

public class RectangleSerializer extends AbstractSerializer {
	private static final String X_TAG = "x"; //
	private static final String Y_TAG = "y"; //
	private static final String WIDTH_TAG = "width"; //
	private static final String HEIGHT_TAG = "height"; //

	JTree tree = new JTree();

	/**
	 * DOCUMENT ME!
	 *
	 * @param e DOCUMENT ME!
	 * @param type DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public Object read(Element e, Class type) {

		Rectangle r = new Rectangle();
		NodeList nodeList = getFieldNodeList(e);

		for (int i = 0; i < nodeList.getLength(); i++) {
			Element fieldNode = (Element) nodeList.item(i);
			String fieldName = fieldNode.getNodeName();

			if (fieldName.equals(X_TAG)) {
				r.x = intValue(fieldNode);
			} else if (fieldName.equals(Y_TAG)) {
				r.y = intValue(fieldNode);
			} else if (fieldName.equals(WIDTH_TAG)) {
				r.width = intValue(fieldNode);
			} else if (fieldName.equals(HEIGHT_TAG)) {
				r.height = intValue(fieldNode);
			}
		}

		return r;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param doc DOCUMENT ME!
	 * @param tag DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public void write(Element e, Object object) {
		Rectangle r = (Rectangle) object;
		Node node = this.createTextFieldNode(e, X_TAG, String.valueOf(r.x));
		e.appendChild(node);
		node = this.createTextFieldNode(e, Y_TAG, String.valueOf(r.y));
		e.appendChild(node);
		node = this.createTextFieldNode(e, WIDTH_TAG, String.valueOf(r.width));
		e.appendChild(node);
		node = this
			.createTextFieldNode(e, HEIGHT_TAG, String.valueOf(r.height));
		e.appendChild(node);
	}
}
