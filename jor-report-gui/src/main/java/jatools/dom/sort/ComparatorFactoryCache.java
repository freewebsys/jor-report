package jatools.dom.sort;

import jatools.designer.App;
import jatools.dom.src.ArrayNodeSource;
import jatools.dom.src.GroupNodeSource;
import jatools.dom.src.RowNodeSource;

import java.util.HashMap;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class ComparatorFactoryCache {
    static Map painters = new HashMap();

    static {
        registerPainter(RowNodeSource.class, new RowNodeComparatorFactory());
        registerPainter(GroupNodeSource.class, new GroupNodeComparatorFactory());
        registerPainter(ArrayNodeSource.class, new ArrayNodeComparatorFactory());
    }

    /**
     * DOCUMENT ME!
     *
     * @param cls DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static NodeComparatorFactory getInstance(Class cls) {
        NodeComparatorFactory p = (NodeComparatorFactory) painters.get(cls);

        if (p == null) {
            throw new IllegalArgumentException(App.messages.getString("res.68") + cls.getName());
        }

        return p;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param painter DOCUMENT ME!
     */
    public static void registerPainter(Class c, NodeComparatorFactory painter) {
        painters.put(c, painter);
    }
}
