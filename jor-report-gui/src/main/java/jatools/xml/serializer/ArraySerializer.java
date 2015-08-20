package jatools.xml.serializer;

import jatools.util.Util;
import jatools.xml.XmlSerializer;
import jatools.xml.XmlSerializerFactory;

import java.lang.reflect.Array;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class ArraySerializer extends ContainerSerializer {
    private static Logger logger = Logger.getLogger("ArraySerializer");


    private Class getItemClass(Object[] array) {
        if (array.length == 0) {
            return null;
        }

        Class itemClass = array[0].getClass();

        for (int i = 1; i < array.length; i++) {
            if (!array[i].getClass().equals(itemClass)) {
                return null;
            }
        }
        return itemClass;
    }

    public Object read(Element e, Class type) {
        int len = Integer.parseInt(e.getAttribute(LENGTH));
        Object result = Array.newInstance(type.getComponentType(), len);
        Class itemClass = null;
        String className = e.getAttribute(ITEM_CLASS);

        try {
            if (!className.equals("")) {
                itemClass = Class.forName(className);
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
                Class nowClass = (itemClass != null) ? itemClass
                                                     : getClass(itemNode);

                XmlSerializer delegate = XmlSerializerFactory.createInstance(nowClass);
                Array.set(result, i, delegate.read(itemNode, nowClass));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        return result;
    }

    public void write(Element e, Object object) {
        Object[] list = (Object[]) object;
        Class itemClass = getItemClass(list);

        if (itemClass != null) {
            e.setAttribute(ITEM_CLASS, itemClass.getName());
        }

        e.setAttribute(LENGTH, list.length + "");

        for (int i = 0; i < list.length; i++) {
            Element fieldNode = createFieldNode(e, "Item" + i);
            Object item = list[i];

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
                    "fail to save " + "fail to item " +
                    item.getClass().getName());
            }
        }
    }
    
   
}
