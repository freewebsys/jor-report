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
 *  DOCUMENT ME!
 *
 *  @version $Revision: 1.3 $
 *  @author $author$
 *
 */
public class ListSerializer extends ContainerSerializer {
    private static Logger logger = Logger.getLogger("ZListElement"); //

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     * @param value DOCUMENT ME!
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
     * @param e DOCUMENT ME!
     * @param value DOCUMENT ME!
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

        List list = null;

        try {
            list = (List) type.newInstance();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        }

        Class itemClass = null;

        String className = e.getAttribute(ListSerializer.ITEM_CLASS);

        try {
            if (!className.equals("")) { //
                itemClass = getClass(e, ListSerializer.ITEM_CLASS);
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        NodeList nodeList = e.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Object item = nodeList.item(i);

            if (!(item instanceof Element)) {
                continue;
            }

            Element itemNode = (Element) item;

            try {
                Class nowClass = (itemClass != null) ? itemClass : getClass(itemNode);

                XmlSerializer delegate = XmlSerializerFactory.createInstance(nowClass);
                list.add(delegate.read(itemNode, nowClass));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return list;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     * @param object DOCUMENT ME!
     */
    public void write(Element e, Object object) {
        /*
         <Field NAME="Children" ITEM_CLASS="java.util.Integer">
         <Field NAME="Item1">1997</Field>
         <Field NAME="Item2">69</Field>
         <Field NAME="Item3">31</Field>
         <Field NAME="Item4">30</Field>
         <Field NAME="Item5">2</Field>
         <Field NAME="Item6">15</Field>
         </Fields>
         */

        
        List list = (List) object;
        Class itemClass = getItemClass(list);

        if (itemClass != null) {
            setClass(e, list.get(0), ListSerializer.ITEM_CLASS);
        }

        for (int i = 0; i < list.size(); i++) {
            Element fieldNode = createFieldNode(e, "Item" + i); //
            Object item = list.get(i);

            try {
                XmlSerializer delegate = XmlSerializerFactory.createInstance(item.getClass());
                delegate.write(fieldNode, item);

                if (itemClass == null) {
                    setClass(fieldNode, item);
                }

                e.appendChild(fieldNode);
            } catch (Exception ex) {
                Util.debug(logger, ex);
                Util.debug(logger,
                    "fail to save " + "fail to item " + // //$NON-NLS-2$
                    item.getClass().getName());
            }
        }
    }
}
