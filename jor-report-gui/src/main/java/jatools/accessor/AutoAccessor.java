package jatools.accessor;

import jatools.xml.serializer.XmlReadListener;
import jatools.xml.serializer.XmlWriteListener;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class AutoAccessor implements PropertyAccessor {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        Field[] fields = this.getClass().getDeclaredFields();
        ArrayList properties = new ArrayList();

        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];

            try {
                int m = field.getModifiers();

                if (!Modifier.isTransient(m) && !Modifier.isStatic(m)) {
                    String f = field.getName();
                    f = f.substring(0, 1).toUpperCase() + f.substring(1);

                    properties.add(new PropertyDescriptor(f, field.getType()));
                }
            } catch (Exception ex) {
            	
                ex.printStackTrace();
            }
        }

        return (PropertyDescriptor[]) properties.toArray(new PropertyDescriptor[0]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public XmlWriteListener getSaveListener() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public XmlReadListener getLoadListener() {
        return null;
    }
}
