/*
 * Created on 2003-12-31
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package jatools.xml.serializer;

import jatools.util.Util;
import jatools.xml.XmlSerializer;
import jatools.xml.XmlSerializerFactory;

import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 *  @author zhou
 *  
 *  To change the template for this generated type comment go to Window -
 *  Preferences - Java - Code Generation - Code and Comments
 * 
 */

public class CollectionSerializer extends ContainerSerializer {
	private static Logger logger = Logger.getLogger("ZCollectionElemnt"); //

	

	

	private static final String ITEM_CLASS = "ItemClass"; //

	/**
	 * DOCUMENT ME!
	 * 
	 * @param e
	 *            DOCUMENT ME!
	 * @param value
	 *            DOCUMENT ME!
	 */
	private Class getItemClass(List list) {
		if (list.isEmpty()) {
			return null;
		}

		Class itemClass = list.get(0).getClass();

		for (int i = 1; i < list.size(); i++) {
			if (!list.get(i).getClass().equals(itemClass)) {
				return null;
			}
		}

		return itemClass;
	}

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
			try {
				Util.debug(
					logger,
					"class not found:" + getClass(e).getName() );
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} //
		}

		if (type == null) {
			Util.debug(logger, "null element type"); // //$NON-NLS-2$

			return null;
		}

		List list = null;

		try {
			list = (List) type.newInstance();
		} catch (Exception ex) {

			Util.debug(logger, ex);

		}

		Class itemClass = null;

		String className = e.getAttribute(CollectionSerializer.ITEM_CLASS);

		try {
			if (!className.equals("")) { //
				itemClass = Class.forName(className);
			}
		} catch (ClassNotFoundException ex) {
			Util.debug(logger, "class not found :" + className); //
		}

		NodeList nodeList = e.getChildNodes();

		for (int i = 0; i < nodeList.getLength(); i++) {
			Element itemNode = (Element) nodeList.item(i);

			try {
				Class nowClass = (itemClass != null) ? itemClass :getClass(itemNode);

				XmlSerializer delegate = XmlSerializerFactory.createInstance(nowClass);
				list.add(delegate.read(itemNode, nowClass));
			} catch (Exception ex) {
				Util.debug(logger, "can not load child ,index is :" + i); //
			}
		}

		return list;
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
		 * <Field NAME="Children" ITEM_CLASS="java.util.Integer">
		 * <Field NAME="Item1"> 1997 </Field><Field NAME="Item2"> 69 </Field>
		 * <Field NAME="Item3"> 31 </Field><Field NAME="Item4"> 30 </Field>
		 * <Field NAME="Item5"> 2 </Field><Field NAME="Item6"> 15 </Field>
		 * </Fields>
		 */

		
		List list = (List) object;
		Class itemClass = getItemClass(list);

		if (itemClass != null) {
			e.setAttribute(ITEM_CLASS, itemClass.getName());
		}

		for (int i = 0; i < list.size(); i++) {
			Element fieldNode = createFieldNode(e, "Item" + i); //
			Object item = list.get(i);

			try {
				XmlSerializer delegate = XmlSerializerFactory.createInstance(item
					.getClass());
				delegate.write(fieldNode, item);

				if (itemClass == null) {
					setClass(fieldNode, item);
				}

				e.appendChild(fieldNode);
			} catch (Exception ex) {
				Util.debug(
					logger,
					"fail to save item " + item.getClass().getName()); //
			}
		}
	}

	/**
	 * DOCUMENT ME!
	 * 
	 * @param argv
	 *            DOCUMENT ME!
	 */

}
