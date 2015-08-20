package jatools.dom.src.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class GetterReflect {
    static Map cached = new HashMap();

    /**
     * DOCUMENT ME!
     *
     * @param cls DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String[] getFields(String clss) {
        String[] result = (String[]) cached.get(clss);

        if (result == null) {
            Class cls;

            try {
                cls = Class.forName(clss);

                ArrayList results = new ArrayList();
                Field[] fields = cls.getFields();

                for (int i = 0; i < fields.length; i++) {
                    results.add(fields[i].getName());
                }

                Method[] methods = cls.getMethods();

                for (int i = 0; i < methods.length; i++) {
                    if (isGetter(methods[i])) {
                        String name = getShortName(methods[i].getName());

                        if (!results.contains(name)) {
                            results.add(name);
                        }
                    }
                }

                result = (String[]) results.toArray(new String[0]);
                cached.put(cls, result);
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return result;
    }

    //	public static void printGettersSetters(Class aClass){
    /**
     * DOCUMENT ME!
     *
     * @param method DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isGetter(Method method) {
        if (method.getParameterTypes().length != 0) {
            return false;
        }

        if (method.getName().startsWith("is") && Boolean.TYPE.equals(method.getReturnType())) {
            return true;
        }

        if (!method.getName().startsWith("getClass") && method.getName().startsWith("get") && !void.class.equals(method.getReturnType())) {
            return true;
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param method DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getShortName(String name) {
        int start = name.startsWith("is") ? 2 : 3;

        return name.substring(start, start + 1).toLowerCase() + name.substring(start + 1);
    }
}
