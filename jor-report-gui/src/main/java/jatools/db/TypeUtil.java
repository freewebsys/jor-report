package jatools.db;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class TypeUtil {
    public static int SQL_ID = 0;
    public static int SHORT_NAME = 1;
    public static int CLASS_NAME = 2;
    public static int CLASS = 3;
    static Object[][] types = new Object[][] {
            { new Integer(Column.INTEGER), "int", "java.lang.Integer", java.lang.Integer.class },
            { new Integer(Column.DOUBLE), "double", "java.lang.Double", java.lang.Double.class },
            { new Integer(Column.VARCHAR), "string", "java.lang.String", java.lang.String.class },
            { new Integer(Column.DATE), "date", "java.sql.Date", java.sql.Date.class },
            { new Integer(Column.TIME), "time", "java.sql.Time", java.sql.Time.class },
            {
                new Integer(Column.TIMESTAMP), "timestamp", "java.sql.Timestamp",
                java.sql.Timestamp.class
            },
            {
                new Integer(Column.DECIMAL), "decimal", "java.math.BigDecimal",
                java.math.BigDecimal.class
            },
            { new Integer(Column.BIT), "boolean", "java.lang.Boolean", java.lang.Boolean.class },
            { new Integer(Column.TINYINT), "short", "java.lang.Short", java.lang.Short.class },
            { new Integer(Column.BIGINT), "long", "java.lang.Long", java.lang.Long.class },
            { new Integer(Column.REAL), "float", "java.lang.Float", java.lang.Float.class }
        };
    static Map shortNames;
    static Map classNames;
    static Map sqlIds;
    static Map classes;

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getShortName(Object key) {
        if (shortNames == null) {
            shortNames = createMaps(SHORT_NAME);
        }

        return (String) shortNames.get(key);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getSqlID(Object key) {
        if (sqlIds == null) {
            sqlIds = createMaps(SQL_ID);
        }

        return ((Integer) sqlIds.get(key)).intValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Class getClass(Object key) {
        if (classes == null) {
            classes = createMaps(CLASS);
        }

        return (Class) classes.get(key);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getClassName(Object key) {
        if (classNames == null) {
            classNames = createMaps(CLASS_NAME);
        }

        return (String) classNames.get(key);
    }

    private static Map createMaps(int keyColumn) {
        Map m = new HashMap();

        for (int i = 0; i < types.length; i++) {
            for (int j = 0; j < 4; j++) {
                if (j != keyColumn) {
                    m.put(types[i][j], types[i][keyColumn]);
                }
            }
        }

        return m;
    }

    /**
     * DOCUMENT ME!
     *
     * @param expr DOCUMENT ME!
     * @param sqlID DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     */
    public static Object valueOf(String expr, int sqlID)
        throws SQLException {
    	
        return Column.convertString(expr, sqlID);
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     * @param sqlID DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     */
    public static String stringOf(Object obj, int sqlID)
        throws SQLException {
        return Column.createString(obj, sqlID);
    }

    /**
     * DOCUMENT ME!
     *
     * @param nameid DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object[] getSupportedTypes(int nameid) {
        Object[] typs = new Object[types.length];

        for (int i = 0; i < typs.length; i++) {
            typs[i] = types[i][nameid];
        }

        return typs;
    }
}
