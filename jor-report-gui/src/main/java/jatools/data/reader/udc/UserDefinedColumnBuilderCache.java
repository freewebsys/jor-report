package jatools.data.reader.udc;

import jatools.data.interval.date.DateIntervalColumn;
import jatools.data.interval.date.DateIntervalColumnBuilder;
import jatools.data.interval.formula.FormulaIntervalColumn;
import jatools.data.interval.formula.FormulaIntervalColumnBuilder;

import java.util.HashMap;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class UserDefinedColumnBuilderCache {
    static Map caches;

    /**
     * DOCUMENT ME!
     *
     * @param col DOCUMENT ME!
     */
    public static UserColumnBuilder getInstance(Object col) {
        if (caches == null) {
            registerBuilders();
        }

        return (UserColumnBuilder) caches.get(col.getClass());
    }

    /**
     * DOCUMENT ME!
     *
     * @param cls DOCUMENT ME!
     * @param shortName DOCUMENT ME!
     */
    private static void registerBuilders() {
        caches = new HashMap();
        caches.put(DateIntervalColumn.class, new DateIntervalColumnBuilder());
        caches.put(FormulaIntervalColumn.class, new FormulaIntervalColumnBuilder());
    }
}
