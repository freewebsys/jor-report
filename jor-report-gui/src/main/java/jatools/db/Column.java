package jatools.db;

import jatools.designer.App;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.HashMap;
import java.util.Hashtable;


class Column {
    private static Hashtable hTypes;
    private static HashMap lTypes;
    public final static int BIT = Types.BIT;
    public final static int TINYINT = Types.TINYINT;
    public final static int BIGINT = Types.BIGINT;
    public final static int LONGVARBINARY = Types.LONGVARBINARY;
    public final static int VARBINARY = Types.VARBINARY;
    public final static int BINARY = Types.BINARY;
    public final static int LONGVARCHAR = Types.LONGVARCHAR;
    public final static int CHAR = Types.CHAR;
    public final static int NUMERIC = Types.NUMERIC;
    public final static int DECIMAL = Types.DECIMAL;
    public final static int INTEGER = Types.INTEGER;
    public final static int SMALLINT = Types.SMALLINT;
    public final static int FLOAT = Types.FLOAT;
    public final static int REAL = Types.REAL;
    public final static int DOUBLE = Types.DOUBLE;
    public final static int VARCHAR = Types.VARCHAR;
    public final static int DATE = Types.DATE;
    public final static int TIME = Types.TIME;
    public final static int TIMESTAMP = Types.TIMESTAMP;
    public final static int OTHER = Types.OTHER;
    public final static int NULL = Types.NULL;
    public final static int VARCHAR_IGNORECASE = 100;
    public final static int[] TYPES = {
            BIT, TINYINT, BIGINT, LONGVARBINARY, VARBINARY, BINARY, LONGVARCHAR, CHAR, NUMERIC,
            DECIMAL, INTEGER, SMALLINT, FLOAT, REAL, DOUBLE, VARCHAR, DATE, TIME, TIMESTAMP, OTHER
        };

    static {
        hTypes = new Hashtable();
        addTypes(INTEGER, "INTEGER", "int", "java.lang.Integer");
        addType(INTEGER, "INT");
        addTypes(DOUBLE, "DOUBLE", "double", "java.lang.Double");
        addType(FLOAT, "FLOAT");
        addTypes(VARCHAR, "VARCHAR", "java.lang.String", null);
        addTypes(CHAR, "CHAR", "CHARACTER", null);
        addType(LONGVARCHAR, "LONGVARCHAR");

        addType(VARCHAR_IGNORECASE, "VARCHAR_IGNORECASE");
        addTypes(DATE, "DATE", "java.sql.Date", null);
        addTypes(TIME, "TIME", "java.sql.Time", null);

        addTypes(TIMESTAMP, "TIMESTAMP", "java.sql.Timestamp", "DATETIME");
        addTypes(DECIMAL, "DECIMAL", "java.math.BigDecimal", null);
        addType(NUMERIC, "NUMERIC");
        addTypes(BIT, "BIT", "java.lang.Boolean", "boolean");
        addTypes(TINYINT, "TINYINT", "java.lang.Short", "short");
        addType(SMALLINT, "SMALLINT");
        addTypes(BIGINT, "BIGINT", "java.lang.Long", "long");
        addTypes(REAL, "REAL", "java.lang.Float", "float");
        addTypes(BINARY, "BINARY", "byte[]", null);
        addType(VARBINARY, "VARBINARY");
        addType(LONGVARBINARY, "LONGVARBINARY");
        addTypes(OTHER, "OTHER", "java.lang.Object", "OBJECT");

        lTypes = new HashMap();

        lTypes.put(new Integer(INTEGER), "java.lang.Integer");
        lTypes.put(new Integer(DOUBLE), "java.lang.Double");

        lTypes.put(new Integer(FLOAT), "java.lang.Float");
        lTypes.put(new Integer(VARCHAR), "java.lang.String");
        lTypes.put(new Integer(CHAR), "java.lang.String");
        lTypes.put(new Integer(LONGVARCHAR), "java.lang.String");
        lTypes.put(new Integer(VARCHAR_IGNORECASE), "java.lang.String");
        lTypes.put(new Integer(DATE), "java.sql.Date");
        lTypes.put(new Integer(TIME), "java.sql.Time");
        lTypes.put(new Integer(TIMESTAMP), "java.sql.Timestamp");
        lTypes.put(new Integer(DECIMAL), "java.math.BigDecimal");
        lTypes.put(new Integer(NUMERIC), "java.lang.Double");
        lTypes.put(new Integer(BIT), "java.lang.Boolean");
        lTypes.put(new Integer(TINYINT), "java.lang.Short");
        lTypes.put(new Integer(SMALLINT), "java.lang.Short");
        lTypes.put(new Integer(BIGINT), "java.lang.Long");
        lTypes.put(new Integer(REAL), "java.lang.Float");
        lTypes.put(new Integer(BINARY), "byte[]");
        lTypes.put(new Integer(VARBINARY), "byte[]");
        lTypes.put(new Integer(LONGVARBINARY), "byte[]");
        lTypes.put(new Integer(OTHER), "java.lang.Object");
    }

    String sName;
    int iType;
    private boolean bNullable;
    private boolean bIdentity;

    Column(String name, boolean nullable, int type, boolean identity) {
        sName = name;
        bNullable = nullable;
        iType = type;
        bIdentity = identity;
    }

    static String className(int i) {
        return (String) lTypes.get(new Integer(i));
    }

    private static void addTypes(int type, String name, String n2, String n3) {
        addType(type, name);
        addType(type, n2);
        addType(type, n3);
    }

    private static void addType(int type, String name) {
        if (name != null) {
            hTypes.put(name, new Integer(type));
        }
    }

    boolean isIdentity() {
        return bIdentity;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     */
    public static int getTypeNr(String type) throws SQLException {
        Integer i = (Integer) hTypes.get(type);

        return i.intValue();
    }

    static String getType(int type) throws SQLException {
        switch (type) {
        case NULL:
            return "NULL";

        case INTEGER:
            return "INTEGER";

        case DOUBLE:
            return "DOUBLE";

        case VARCHAR_IGNORECASE:
            return "VARCHAR_IGNORECASE";

        case VARCHAR:
            return "VARCHAR";

        case CHAR:
            return "CHAR";

        case LONGVARCHAR:
            return "LONGVARCHAR";

        case DATE:
            return "DATE";

        case TIME:
            return "TIME";

        case DECIMAL:
            return "DECIMAL";

        case BIT:
            return "BIT";

        case TINYINT:
            return "TINYINT";

        case SMALLINT:
            return "SMALLINT";

        case BIGINT:
            return "BIGINT";

        case REAL:
            return "REAL";

        case FLOAT:
            return "FLOAT";

        case NUMERIC:
            return "NUMERIC";

        case TIMESTAMP:
            return "TIMESTAMP";

        case BINARY:
            return "BINARY";

        case VARBINARY:
            return "VARBINARY";

        case LONGVARBINARY:
            return "LONGVARBINARY";

        case OTHER:
            return "OBJECT";

        default:
            throw new SQLException(App.messages.getString("res.603") + type);
        }
    }

    boolean isNullable() {
        return bNullable;
    }

    static Object concat(Object a, Object b) throws SQLException {
        if (a == null) {
            return b;
        } else if (b == null) {
            return a;
        }

        return convertObject(a) + convertObject(b);
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     */
    public static Object convertString(String s, int type)
        throws SQLException {
        if (s == null || "null".equals(s)) {
            return null;
        }

        switch (type) {
        case NULL:
            return null;

        case INTEGER:
            return new Integer(s);

        case FLOAT:
        case DOUBLE:
            return new Double(s);

        case VARCHAR_IGNORECASE:
        case VARCHAR:
        case CHAR:
        case LONGVARCHAR:
            return s;

        case DATE:
            return java.sql.Date.valueOf(s);

        case TIME:
            return Time.valueOf(s);

        case TIMESTAMP:
            return Timestamp.valueOf(s);

        case NUMERIC:
        case DECIMAL:
            return new BigDecimal(s.trim());

        case BIT:
            return new Boolean(s);

        case TINYINT:
        case SMALLINT:
            return new Short(s);

        case BIGINT:
            return new Long(s);

        case REAL:
            return new Float(s);

        case BINARY:
        case VARBINARY:
        case LONGVARBINARY:
        case OTHER:
            return s.getBytes();

        default:
            throw new SQLException(App.messages.getString("res.603") + type);
        }
    }

    static String convertObject(Object o) {
        if (o == null) {
            return null;
        }

        return o.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     */
    public static Object convertObject(Object o, int type)
        throws SQLException {
        if (o == null) {
            return null;
        }

        return convertString(o.toString(), type);
    }

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     */
    public static String createString(Object o, int type)
        throws SQLException {
        if (o == null) {
            return "NULL";
        }

        switch (type) {
        case Column.NULL:
            return "NULL";

        case Column.BINARY:
        case Column.VARBINARY:
        case Column.LONGVARBINARY:
        case Column.DATE:
        case Column.TIME:
        case Column.TIMESTAMP:
        case Column.OTHER:
            return "'" + o.toString() + "'";

        case Column.VARCHAR_IGNORECASE:
        case Column.VARCHAR:
        case Column.CHAR:
        case Column.LONGVARCHAR:
            return createString((String) o);

        default:
            return o.toString();
        }
    }

    static String createString(String s) {
        StringBuffer b = new StringBuffer().append('\'');

        if (s != null) {
            for (int i = 0, len = s.length(); i < len; i++) {
                char c = s.charAt(i);

                if (c == '\'') {
                    b.append(c);
                }

                b.append(c);
            }
        }

        return b.append('\'').toString();
    }
}
