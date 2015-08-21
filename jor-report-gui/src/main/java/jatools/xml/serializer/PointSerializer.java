package jatools.xml.serializer;

import java.awt.Point;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 *  DOCUMENT ME!
 * 
 *  @version $Revision: 1.2 $
 *  @author $author$
 * 
 */

public class PointSerializer extends AbstractSerializer {
	private static final String X = "x"; //
	private static final String Y = "y"; //

	/**
	 * DOCUMENT ME!
	 *
	 * @param e DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public Object read(Element e, Class type) {
		Point p = new Point();
		NodeList nodeList = getFieldNodeList(e);

		for (int i = 0; i < nodeList.getLength(); i++) {
			Element fieldNode = (Element) nodeList.item(i);
			String fieldName = fieldNode.getNodeName();

			if (fieldName.equals(X)) {
				p.x = intValue(fieldNode);
			} else if (fieldName.equals(Y)) {
				p.y = intValue(fieldNode);
			}
		}

		return p;
	}

	/**
	 * DOCUMENT ME!
	 *
	 * @param e DOCUMENT ME!
	 * @param object DOCUMENT ME!
	 */
	public void write(Element e, Object object) {
		Point p = (Point) object;
		Node node = this.createTextFieldNode(e, X, String.valueOf(p.x));
		e.appendChild(node);

		node = this.createTextFieldNode(e, Y, String.valueOf(p.y));
		e.appendChild(node);
	}
}
