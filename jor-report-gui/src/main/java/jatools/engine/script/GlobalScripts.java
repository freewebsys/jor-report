package jatools.engine.script;

import bsh.Interpreter;
import bsh.Primitive;

import jatools.accessor.ProtectPublic;

import jatools.designer.App;

import jatools.engine.ImportFunctions;
import jatools.engine.System2;

import jatools.engine.script.debug.ScriptDebugger;

import jatools.util.HZUtil;

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;

import java.net.URLEncoder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class GlobalScripts implements ProtectPublic {
    /**
     * DOCUMENT ME!
     *
     * @param time1 DOCUMENT ME!
     * @param time2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getDays(Date d) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);

        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * DOCUMENT ME!
     *
     * @param keys DOCUMENT ME!
     * @param values DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Map toMap(String keys, String values) {
        HashMap result = new HashMap();

        String[] ks = keys.split(",");
        String[] vs = values.split(",");

        for (int i = 0; i < vs.length; i++) {
            result.put(ks[i], new Integer(vs[i]));
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String quoted(String src) {
        String[] a = src.split(",");

        for (int i = 0; i < a.length; i++) {
            a[i] = String.format("'%s'", a[i]);
        }

        return StringUtils.join(a, ',');
    }

    /**
     * DOCUMENT ME!
     *
     * @param con DOCUMENT ME!
     * @param sql DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object query(Connection con, String sql) {
        Object result = null;

        PreparedStatement p = null;
        ResultSet rs = null;

        try {
            p = con.prepareStatement(sql);
            rs = p.executeQuery();

            if (rs.next()) {
                result = rs.getObject(1);
            }

            rs.close();
            p.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param year DOCUMENT ME!
     * @param month DOCUMENT ME!
     * @param day DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DATE, day);

        return cal.getTime();
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double abs(double a) {
        return Math.abs(a);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double asin(double a) {
        return Math.asin(a);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double acos(double a) {
        return Math.acos(a);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double atan(double a) {
        return Math.atan(a);
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toUrl(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return s;
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double atan2(double a, double b) {
        return Math.atan2(a, b);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double ceil(double a) {
        return Math.ceil(a);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double cos(double a) {
        return Math.cos(a);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double exp(double a) {
        return Math.exp(a);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double floor(double a) {
        return Math.floor(a);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double log(double a) {
        return Math.log(a);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double max(double a, double b) {
        return Math.max(a, b);
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        System.out.println(quoted("m0001,m0002,m0003"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double min(double a, double b) {
        return Math.min(a, b);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double max(double a, double b, double c) {
        return Math.max(c, Math.max(a, b));
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double min(double a, double b, double c) {
        return Math.min(c, Math.min(a, b));
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double pow(double a, double b) {
        return Math.pow(a, b);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double random() {
        return Math.random();
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double rint(double a) {
        return Math.rint(a);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double sin(double a) {
        return Math.sin(a);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double sqrt(double a) {
        return Math.sqrt(a);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double tan(double a) {
        return Math.tan(a);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double toDegrees(double a) {
        return Math.toDegrees(a);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static double toRadians(double a) {
        return Math.toRadians(a);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static long round(double a) {
        return Math.round(a);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int length(String a) {
        return a.length();
    }

    /**
     * DOCUMENT ME!
     *
     * @param str DOCUMENT ME!
     * @param a DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public char charAt(String str, int a) {
        return str.charAt(a);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean startsWith(String a, String b) {
        return a.startsWith(b);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean endsWith(String a, String b) {
        return a.endsWith(b);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int indexOf(String a, String b) {
        return a.indexOf(b);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int lastIndexOf(String a, String b) {
        return a.lastIndexOf(b);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String substring(String a, int b) {
        return a.substring(b);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String substring(String a, int b, int c) {
        return a.substring(b, c);
    }

    /**
     * DOCUMENT ME!
     *
     * @param a DOCUMENT ME!
     * @param b DOCUMENT ME!
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String replaceAll(String a, String b, String c) {
        return a.replaceAll(b, c);
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String[] split(String s) {
        return s.split(s);
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toLowerCase(String s) {
        return s.toLowerCase();
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toUpperCase(String s) {
        return s.toUpperCase();
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getYear(Date d) {
        return d.getYear() + 1900;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getMonth(Date d) {
        return d.getMonth() + 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getDate(Date d) {
        return d.getDate();
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getDay(Date d) {
        return d.getDay();
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getHours(Date d) {
        return d.getHours();
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getMinutes(Date d) {
        return d.getMinutes();
    }

    /**
     * DOCUMENT ME!
     *
     * @param val DOCUMENT ME!
     * @param val2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object iif(Object val, Object val2) {
        if ((val == Primitive.VOID_OBJECT) || (val == null)) {
            return val2;
        } else {
            return val;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param val DOCUMENT ME!
     * @param val2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object ifNull(Object val, Object val2) {
        return iif(val, val2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param val DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean has(Object val) {
        if (val instanceof String) {
            return val.toString().length() > 0;
        } else {
            return ((val != Primitive.VOID_OBJECT) && (val != null));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getSeconds(Date d) {
        return d.getSeconds();
    }

    /**
     * DOCUMENT ME!
     *
     * @param d1 DOCUMENT ME!
     * @param d2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean before(Date d1, Date d2) {
        return d2.before(d1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d1 DOCUMENT ME!
     * @param d2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean after(Date d1, Date d2) {
        return d2.before(d1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toHZYear(int d) {
        return HZUtil.toHZ(d);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toHZMonth(int d) {
        return HZUtil.toHZMonth(d);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int thisYear() {
        return getYear(new Date());
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toHZDay(int d) {
        return HZUtil.toHZDay(d);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toRmbString(double d) {
        return HZUtil.toRmbString(d);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String format(double d, String s) {
        DecimalFormat df = new DecimalFormat(s);

        return df.format(d);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String format(Date d, String s) {
        SimpleDateFormat sdf = new SimpleDateFormat(s);

        return sdf.format(d);
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String p(String s) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param s DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String clobString(String s) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return "Script";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ResultSet getResultSet() {
        Connection con = null;
        PreparedStatement p = null;
        ResultSet rs = null;

        try {
            Class.forName("org.hsqldb.jdbcDriver");
            con = DriverManager.getConnection("jdbc:hsqldb:./demosdb/mydb", "sa", null);
            p = con.prepareStatement("SELECT * FROM 订单查询");
            rs = p.executeQuery();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static TableModel getTableModel() {
        DefaultTableModel result = new DefaultTableModel(0, 4);
        result.addRow(new Object[] {
                App.messages.getString("res.51"), new Integer(16), App.messages.getString("res.52"),
                new Integer(90)
            });
        result.addRow(new Object[] {
                App.messages.getString("res.53"), new Integer(17), App.messages.getString("res.54"),
                new Integer(99)
            });
        result.addRow(new Object[] {
                App.messages.getString("res.55"), new Integer(18), App.messages.getString("res.54"),
                new Integer(85)
            });

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param it DOCUMENT ME!
     * @param cls DOCUMENT ME!
     */
    public static void importFunctions(Interpreter it, Class cls) {
        ImportFunctions.importFunctions(it, cls);
    }

    /**
     * DOCUMENT ME!
     *
     * @param it DOCUMENT ME!
     */
    public static void debug(Interpreter it) {
        ScriptDebugger.debug(it);
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String property(String name) {
        return System2.getProperty(name);
    }
}
