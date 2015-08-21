package jatools.xml.serializer;

import jatools.util.Map;
import jatools.util.Util;
import jatools.xml.XmlBase;
import jatools.xml.XmlSerializer;
import jatools.xml.XmlSerializerFactory;

import java.util.Iterator;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * 
 * DOCUMENT ME!
 * 
 * @version $Revision: 1.3 $
 * @author $author$
 * 
 */

public class MapSerializer extends ContainerSerializer {
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
		try {
			if (type == null) {
				type = ContainerSerializer.getClass(e);
			}
		} catch (ClassNotFoundException ex) {
			Util.debug(logger, ex);
		}

		if (type == null) {

			Util.debug(logger, "null element type"); //

			return null;
		}

		Map m = null;
		try {
			m = (Map) type.newInstance();
		} catch (Exception e1) {
			Util.debug(logger, e1);
		}
		NodeList nodeList = e.getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			Element itemNode = (Element) nodeList.item(i);

			try {
				Class nowClass = getClass(itemNode);

				XmlSerializer delegate = XmlSerializerFactory.createInstance(nowClass);

				m.put(itemNode.getAttribute(XmlBase.NAME_ATTRIBUTE_TAG),
						delegate.read(itemNode, nowClass));
			} catch (Exception ex) {
				Util.debug(logger, ex);
			}
		}

		return m;
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

		/*
		 * <Field NAME="Children" ITEM_CLASS="java.util.Integer"> <Item1
		 * ITEM_CLASS> 1997 </Field><Field NAME="Item2"> 69 </Field> <Field
		 * NAME="Item3"> 31 </Field><Field NAME="Item4"> 30 </Field> <Field
		 * NAME="Item5"> 2 </Field><Field NAME="Item6"> 15 </Field> </Fields>
		 */

		
		Map m = (Map) object;

		int i = 0;
		for (Iterator iter = m.names(); iter.hasNext(); i++) {
			String name = (String) iter.next();

			Element fieldNode = createFieldNode(e, ITEM + i);
			Object item = m.get(name);
			if (item != null) {

				try {

					//
					XmlSerializer delegate = XmlSerializerFactory.createInstance(item
							.getClass());
					delegate.write(fieldNode, item);
					//
					setClass(fieldNode, item);

					fieldNode.setAttribute(XmlBase.NAME_ATTRIBUTE_TAG, name);
					//
					e.appendChild(fieldNode);
				} catch (Exception ex) {
					
			//		ex.printStackTrace() ;

					Util
							.debug(
									logger,
									"fail to save" + "fail to item " + item.getClass().getName()); // //$NON-NLS-2$
				}
			}
		}
	}

}
