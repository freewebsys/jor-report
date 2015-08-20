package jatools.designer.variable;

import java.util.ArrayList;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ArrayUtil {
    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ArrayList toVector(Object[] o) {
        if (o != null) {
            ArrayList v = new ArrayList();

            for (int i = 0; i < o.length; i++) {
                v.add(o[i]);
            }

            return v;
        }

        return null;
    }
}
