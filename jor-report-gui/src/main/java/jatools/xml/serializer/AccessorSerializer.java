package jatools.xml.serializer;

import jatools.accessor.PropertyDescriptor;
import jatools.property.PropertyUtil;
import jatools.util.Util;
import jatools.xml.XmlSerializer;
import jatools.xml.XmlSerializerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class AccessorSerializer extends AbstractSerializer {
    private static Logger logger = Logger.getLogger("ZAccessorElement");
    static Map cachedInstance = new HashMap();

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object read(Element e, Class type) {
        Object accessor = null;

        try {
            accessor = (Object) type.newInstance();
        } catch (Exception ex) {
            Util.debug(logger, "no default contructor for " + type.getName());
        }

        return load1(e, accessor);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     * @param accessor DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object load1(Element e, Object accessor) {
        NodeList nodeList = getFieldNodeList(e);

        for (int i = 0; i < nodeList.getLength(); i++) {
            Object item = nodeList.item(i);

            if (!(item instanceof Element)) {
                continue;
            }

            Element fieldNode = (Element) item;
            String fieldName = fieldNode.getNodeName();
            
            PropertyDescriptor des = PropertyUtil.getPropertyDescriptor(accessor, fieldName);

            
//            if(des.getPropertyName().equals("ColumnWidths"))
//            {
//            	System.out.println();
//            }

            try {
                Class type = accessor.getClass();
                type = des.isResolvable() ? ClassResolvable.class : des.getPropertyType();

                XmlSerializer delegate = XmlSerializerFactory.createInstance(type);

                Object propValue = delegate.read(fieldNode, type);

                Util.setValue(accessor, des.getPropertyName(), propValue, des.getPropertyType());
            } catch (Exception ex) {
                Util.debug(logger,
                    "could not load value for " + accessor.getClass().getName() + "." +
                    fieldName);
            }
        }

        return accessor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     * @param object DOCUMENT ME!
     */
    public void write(Element e, Object object) {
        Object defObject = cachedInstance.get(object.getClass());

        if (defObject == null) {
            try {
                defObject = object.getClass().newInstance();
                cachedInstance.put(object.getClass(), defObject);
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            }
        }

        ArrayList descriptors = PropertyUtil.getPropertyDescriptors(object);
        XmlWriteListener listener = null;

        if (object instanceof ListenToXmlWrite) {
            listener = ((ListenToXmlWrite) object).getXmlWriteListener();

            if (listener != null) {
                listener.beforeWrite();
            }
        }

        for (int i = 0; i < descriptors.size(); i++) {
            PropertyDescriptor des = (PropertyDescriptor) descriptors.get(i);
            if(des.isReadonly())
            	continue;
            
            String propName = des.getPropertyName();

            if (!des.isSerializable()) {
                continue;
            }

            if ((listener != null) && !listener.isWritable(propName)) {
                continue;
            }

            try {
                //System.out.println(object+propName);
                Object propValue = Util.getValue(object, propName,
                        des.getPropertyType() == Boolean.TYPE);

                if ((propValue == null) || propValue.equals("")) {
                    continue;
                }

                if (!des.isNodefault()) {
                    Object defValue = Util.getValue(defObject, propName,
                            des.getPropertyType() == Boolean.TYPE);

                    if (propValue.equals(defValue)) {
                        continue;
                    }
                }

                appendPropertyNode(e, des, propName, propValue);
            } catch (Exception ex) {
                ex.printStackTrace();
                Util.debug(logger, "fail to build field " + propName);
            }
        }

        if (listener != null) {
            listener.afterWrite();
        }
    }

    protected void appendPropertyNode(Element e, PropertyDescriptor des, String propName,
        Object propValue) throws Exception {
        Class type = des.getPropertyType();

        if (des.isResolvable()) {
            propValue = new ClassResolvable(propValue);
            type = ClassResolvable.class;
        }

        XmlSerializer delegate = XmlSerializerFactory.createInstance(type);
        Element fieldNode = createFieldNode(e, propName);

        delegate.write(fieldNode, propValue);
        e.appendChild(fieldNode);
    }
}
