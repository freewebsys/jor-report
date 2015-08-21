package jatools.xml;

import jatools.engine.ProtectClass;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 *  DOCUMENT ME!
 * 
 *  @version $Revision: 1.2 $
 *  @author $author$
 * 
 */

public interface XmlSerializer extends ProtectClass {
	/**
	 * DOCUMENT ME!
	 *
	 * @param doc DOCUMENT ME!
	 * @param tag DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public abstract Node writeAsNode(Document doc, String tag);

	/**
	 * DOCUMENT ME!
	 *
	 * @param e DOCUMENT ME!
	 * @param object DOCUMENT ME!
	 */
	public abstract void write(Element e, Object object);

	/**
	 * DOCUMENT ME!
	 *
	 * @param node DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public abstract Object readFromNode(Node node);

	/**
	 * DOCUMENT ME!
	 *
	 * @param e DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public abstract Object read(Element e, Class type);

	/**
	 * DOCUMENT ME!
	 *
	 * @param e DOCUMENT ME!
	 *
	 * @return DOCUMENT ME!
	 */
	public abstract Object read(Element e);
}
