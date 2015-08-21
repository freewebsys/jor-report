package jatools.xml.serializer;

import jatools.util.Util;

import java.util.Iterator;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class PropertiesSerializer extends AbstractSerializer {
	private static Logger logger = Logger.getLogger("ZMapElement"); //

	/**
	 * DOCUMENT ME!
	 * 
	 * @param e
	 *            DOCUMENT ME!
	 * @param value
	 *            DOCUMENT ME!
	 */
	public Object read(Element e, Class type) {

		Properties props = new Properties();

		NodeList nodeList = e.getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			Element itemNode = (Element) nodeList.item(i);

			try {

				String key = itemNode.getNodeName();

				String value = getText(itemNode);
				if (key != null && value != null)

					props.setProperty(key, value);

			} catch (Exception ex) {
				Util.debug(logger, ex);
			}
		}

		return props;
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param e
	 *            DOCUMENT ME!
	 * @param object
	 *            DOCUMENT ME!
	 */
	public void write(Element e, Object object) {

		
		Properties m = (Properties) object;

		Iterator it = m.keySet().iterator();
		while (it.hasNext()) {
			String key = (String) it.next();
			String value = m.getProperty(key);

			if (key != null && value != null) {

				try {
					Element fieldNode = createFieldNode(e, key);
					this.setText(fieldNode, value);

					e.appendChild(fieldNode);
				} catch (Exception ex) {
					ex.printStackTrace();

					Util
							.debug(
									logger,
									"fail to save" + "fail to item " + key); // //$NON-NLS-2$
				}

			}
		}
	}

}
