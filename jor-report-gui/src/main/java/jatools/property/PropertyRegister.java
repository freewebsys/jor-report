package jatools.property;


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
public class PropertyRegister {
    private static Map distinctDescriptorsCache = new HashMap();
    private ArrayList propertyDescriptors = new ArrayList();
   
     void addPropertyDescriptors(PropertyDescriptor[] entries) {
        for (int i = 0; i < entries.length; i++) {
            PropertyDescriptor target = (PropertyDescriptor) distinctDescriptorsCache.get(entries[i].getPropertyName());

            if (target == null) {
                distinctDescriptorsCache.put(entries[i].getPropertyName(), entries[i]);
                target = entries[i];
            }

            propertyDescriptors.add(target);
        }
    }

    ArrayList getPropertyDescriptors() {
        return propertyDescriptors;
    }
}
