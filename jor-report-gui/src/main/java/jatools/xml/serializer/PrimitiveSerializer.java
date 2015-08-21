package jatools.xml.serializer;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;


public class PrimitiveSerializer extends AbstractSerializer {
    static Map ctorCache = new HashMap();

    static {
        try {
            ctorCache.put(Integer.TYPE, Integer.class.getConstructor(new Class[] {
                        String.class
                    }));
            ctorCache.put(Boolean.TYPE, Boolean.class.getConstructor(new Class[] {
                        String.class
                    }));
            ctorCache.put(Float.TYPE, Float.class.getConstructor(new Class[] {
                        String.class
                    }));
            ctorCache.put(Double.TYPE, Double.class.getConstructor(new Class[] {
                        String.class
                    }));
        } catch (NoSuchMethodException ex) {
        } catch (SecurityException ex) {
        }
    }

    public void write(Element e, Object object) {
        setText(e, object.toString());
    }

    public Object read(Element e, Class type) {
        try {
            if (type == String.class) {
                return getText(e);
            }

            Constructor creator = (Constructor) ctorCache.get(type);

            return creator.newInstance(new Object[] {
                    getText(e)
                });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
