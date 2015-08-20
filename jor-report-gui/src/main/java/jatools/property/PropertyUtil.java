package jatools.property;


import jatools.accessor.PropertyAccessor;
import jatools.accessor.PropertyDescriptor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PropertyUtil {
    private static Map registersCache = new HashMap();
    private static Map descriptorsCache = new HashMap();
    private static Map editableDescriptorsCache = new HashMap();

    /**
     * DOCUMENT ME!
     *
     * @param comp DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ArrayList getPropertyDescriptors(Object comp) {
        PropertyRegister register = (PropertyRegister) registersCache.get(comp.getClass());

        if (register == null) {
            register = registerProperties(comp.getClass(),
                    ((PropertyAccessor) comp).getRegistrableProperties());
        }

        return register.getPropertyDescriptors();
    }

    /**
     * DOCUMENT ME!
     *
     * @param cls DOCUMENT ME!
     * @param des DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static PropertyRegister registerProperties(Class cls, PropertyDescriptor[] des) {
        PropertyRegister register = new PropertyRegister();
        register.addPropertyDescriptors(des);
        registersCache.put(cls, register);

        return register;
    }

    /**
     * DOCUMENT ME!
     *
     * @param comp DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ArrayList getEditablePropertyDescriptors(Object comp) {
        ArrayList descriptors = (ArrayList) editableDescriptorsCache.get(comp.getClass());

        if (descriptors == null) {
            descriptors = (ArrayList) getPropertyDescriptors(comp).clone();

            for (int i = descriptors.size() - 1; i >= 0; i--) {
                PropertyDescriptor descriptor = (PropertyDescriptor) descriptors.get(i);

                if (!descriptor.isEditable()) {
                    descriptors.remove(descriptor);
                }
            }
        }

        return descriptors;
    }

    /**
     * DOCUMENT ME!
     *
     * @param accessor DOCUMENT ME!
     * @param propertyName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static PropertyDescriptor getPropertyDescriptor(Object accessor, String propertyName) {
        String key = propertyName + "." + accessor.getClass().getName();
        PropertyDescriptor descriptor = (PropertyDescriptor) descriptorsCache.get(key);

        if (descriptor == null) {
            ArrayList descriptors = getPropertyDescriptors(accessor);

            for (int i = 0; i < descriptors.size(); i++) {
                PropertyDescriptor des = (PropertyDescriptor) descriptors.get(i);
                descriptorsCache.put(des.getPropertyName() + "." + accessor.getClass().getName(),
                    des);
            }

            descriptor = (PropertyDescriptor) descriptorsCache.get(key);
        }

        return descriptor;
    }
}
