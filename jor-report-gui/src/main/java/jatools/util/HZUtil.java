/*
 * Created on 2004-3-19
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jatools.util;

import jatools.engine.System2;
import jatools.engine.script.ReportContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.jaxen.JaxenException;
import org.jaxen.XPath;
import org.jaxen.dom.DOMXPath;

import bsh.Interpreter;



/**
 *
 * @author zhou
 *
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 *
 */
public class HZUtil {
    static String[] HanDigiStr = new String[] {
            "零",
            "壹",
            "贰",
            "叁",
            "肆",
            "伍", // //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
            "陆",
            "柒",
            "捌", // //$NON-NLS-2$ //$NON-NLS-3$
            "玖" // //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
        };
    static String[] HanDiviStr = new String[] {
            "",
            "拾",
            "佰",
            "仟",
            "万",
            "拾",
            "佰",
            "仟",
            "亿",
            "拾",
            "佰",
            "仟",
            "万",
            "拾", // //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$ //$NON-NLS-13$ //$NON-NLS-14$
            "佰",
            "仟",
            "亿",
            "拾",
            "佰",
            "仟",
            "万",
            "拾",
            "佰", // //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
            "仟" // //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$
        };
    static SimpleDateFormat format;

    /**
     * DOCUMENT ME!
     *
     * @param NumStr
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    static String PositiveIntegerToHanStr(String NumStr) { // 输入字符串必须正整数，只允许前导空格(必须右对齐)，不宜有前导零

        String RMBStr = ""; //
        boolean lastzero = false;
        boolean hasvalue = false; // 亿、万进位前有数值标记
        int len;
        int n;
        len = NumStr.length();

        if (len > 15) {
            return "数值过大!"; //
        }

        for (int i = len - 1; i >= 0; i--) {
            if (NumStr.charAt(len - i - 1) == ' ') {
                continue;
            }

            n = NumStr.charAt(len - i - 1) - '0';

            if ((n < 0) || (n > 9)) {
                return "输入含非数字字符!"; //
            }

            if (n != 0) {
                if (lastzero) {
                    RMBStr += HanDigiStr[0]; // 若干零后若跟非零值，只显示一个零
                }

                if (!((n == 1) && ((i % 4) == 1) && (i == (len - 1)))) { // 十进位处于第一位不发壹音
                    RMBStr += HanDigiStr[n];
                }

                RMBStr += HanDiviStr[i]; // 非零值后加进位，个位为空
                hasvalue = true; // 置万进位前有值标记
            } else {
                if (((i % 8) == 0) || (((i % 8) == 4) && hasvalue)) { // 亿万之间必须有非零值方显示万
                    RMBStr += HanDiviStr[i]; // “亿”或“万”
                }
            }

            if ((i % 8) == 0) {
                hasvalue = false; // 万进位前有值标记逢亿复位
            }

            lastzero = (n == 0) && ((i % 4) != 0);
        }

        if (RMBStr.length() == 0) {
            return HanDigiStr[0]; // 输入空字符或"0"，返回"零"
        }

        return RMBStr;
    }

    /**
     * DOCUMENT ME!
     *
     * @param it DOCUMENT ME!
     * @param cols DOCUMENT ME!
     */
    public static void DEBUG_VARS(Interpreter it, int cols) {
        ReportContext.DEBUG_VARS(it, cols);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Date toDate(String d) {
        return toDate(d, System2.getProperty("dayformat"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     * @param path DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object selectNode(Object n, String path) {
        try {
            XPath xpath = new DOMXPath(path);
            Object result = xpath.selectSingleNode(n);

            return result;
        } catch (JaxenException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String utf8(String text) {
        if (text == null) {
            text = "";
        }

        try {
            return URLEncoder.encode(text, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // MYDO Auto-generated catch block
            e.printStackTrace();
        }

        return "";
    }

   



    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Date toDate(String d, String f) {
        if (format == null) {
            format = new SimpleDateFormat();
        }

        format.applyPattern(f);

        try {
            return format.parse(d);
        } catch (ParseException e) {
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param conn
     *            DOCUMENT ME!
     * @param sql
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object query(Object conn, String sql) {
        if (conn != null) {
            try {
                Connection connection = (Connection) conn;
                Statement stmt = connection.createStatement();
                ResultSet r = stmt.executeQuery(sql);

                if (r.next()) {
                    return r.getString(1);
                } else {
                    return null;
                }
            } catch (SQLException e) {
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getProperty(String prop) {
        return System2.getProperty(prop);
    }

    /**
     * DOCUMENT ME!
     *
     * @param url
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String http(String url) {
        try {
            URL _url = new URL(url);
            URLConnection conn = _url.openConnection();
            conn.connect();

            InputStream is = conn.getInputStream();

            byte[] tempbuf = new byte[1024];
            int readnum = 0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while ((readnum = is.read(tempbuf)) != -1) {
                baos.write(tempbuf, 0, readnum);
            }

            is.close();

            byte[] bytes = baos.toByteArray();

            baos.close();

            return new String(bytes);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ""; //
    }

    /**
     * DOCUMENT ME!
     *
     * @param val
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toRmbString(double val) {
        String SignStr = ""; //
        String TailStr = ""; //
        long fraction;
        long integer;
        int jiao;
        int fen;

        if (val < 0) {
            val = -val;
            SignStr = "负"; //
        }

        if ((val > 99999999999999.999) || (val < -99999999999999.999)) {
            return "数值位数过大!"; //
        }

        // 四舍五入到分
        long temp = Math.round(val * 100);
        integer = temp / 100;
        fraction = temp % 100;
        jiao = (int) fraction / 10;
        fen = (int) fraction % 10;

        if ((jiao == 0) && (fen == 0)) {
            TailStr = "整"; //
        } else {
            TailStr = HanDigiStr[jiao];

            if (jiao != 0) {
                TailStr += "角"; //
            }

            if ((integer == 0) && (jiao == 0)) { // 零元后不写零几分
                TailStr = ""; //
            }

            if (fen != 0) {
                TailStr += (HanDigiStr[fen] + "分"); //
            }
        }

        // 下一行可用于非正规金融场合，0.03只显示“叁分”而不是“零元叁分”
        // if( !integer ) return SignStr+TailStr;
        return "￥" + SignStr + //
        PositiveIntegerToHanStr(String.valueOf(integer)) + "元" + TailStr; //
    }

    /**
     * DOCUMENT ME!
     *
     * @param val
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toHZDay(int val) {
        String results = PositiveIntegerToHanStr(val + ""); //

        if (((val <= 10) && (val > 0)) || (val == 30) || (val == 20)) {
            return HanDigiStr[0] + results;
        } else if ((val > 10) && (val < 20)) {
            return HanDigiStr[1] + results;
        } else {
            return results;
        }
    }

    /*
     * 票据的出票日期必须使用中文大写。为防止变造票据的出票日期，在填写月、日时，月为壹、贰和壹拾的， 日为壹至玖和壹拾、贰拾和叁拾的，应在其前加"零"；
     * 日为拾壹至拾玖的，应在其前加"壹"。 如1月15日，应写成零壹月壹拾伍日。再如10月20日，应写成零壹拾月零贰拾日。
     */

    /**
     * DOCUMENT ME!
     *
     * @param val DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toHZMonth(int val) {
        String results = PositiveIntegerToHanStr(val + ""); //

        if ((val == 1) || (val == 2) || (val == 10)) {
            results = HanDigiStr[0] + results;
        }

        return results;
    }

    /**
     * DOCUMENT ME!
     *
     * @param val
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toHZ(int val) {
        String str = val + ""; //
        String results = ""; //

        for (int i = 0; i < str.length(); i++) {
            results += HanDigiStr[str.charAt(i) - '0'];
        }

        return results;
    }

    /**
     * DOCUMENT ME!
     *
     * @param it DOCUMENT ME!
     * @param col DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String clobString(Interpreter it, String col) {
        //        Variable.DATAITEM_ENABLED = false;
        //
        //        try {
        //            Object field = it.get(col);
        //            Variable.DATAITEM_ENABLED = true;
        //
        //            Cursor cursor = null;
        //            int fieldIndex = 0;
        //
        //            if (field instanceof ZField) {
        //                ZField fld = (ZField) field;
        //                cursor = fld.getCursor();
        //                fieldIndex = fld.getFieldIndex();
        //            } else if (field instanceof ZSubField) {
        //                ZSubField fld = (ZSubField) field;
        //                cursor = fld.getCursor();
        //                fieldIndex = fld.getFieldIndex();
        //            }
        //
        //            if (cursor != null) {
        //                Dataset rows = cursor.getRows();
        //
        //                try {
        //                    Reader reader = rows.getReader(rows.getRow(cursor.getRow()).index,
        //                            fieldIndex);
        //
        //                    BufferedReader br = new BufferedReader(reader);
        //
        //                    StringBuffer sb = new StringBuffer();
        //                    String line = null;
        //
        //                    while ((line = br.readLine()) != null) {
        //                        sb.append(line);
        //                        sb.append("\n");
        //                    }
        //
        //                    return sb.toString();
        //                } catch (IOException e1) {
        //                }
        //            }
        //        } catch (EvalError e) {
        //            // TODO Auto-generated catch block
        //            // e.printStackTrace();
        //        }
        //
        //        Variable.DATAITEM_ENABLED = true;
        //
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param it DOCUMENT ME!
     * @param text DOCUMENT ME!
     */
//    public static void parseText(Interpreter it, Text text) {
//        text.forceText(it);
//    }

    /**
     * DOCUMENT ME!
     *
     * @param aDate DOCUMENT ME!
     * @param nDays DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Date beforeDay(Date aDate, int nDays) {
        Calendar cal2 = Calendar.getInstance();

        cal2.setTime(aDate);
        cal2.add(Calendar.DATE, -nDays);

        return cal2.getTime();
    }

    /**
     * DOCUMENT ME!
     *
     * @param aDate DOCUMENT ME!
     * @param nDays DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Date beforeYear(Date aDate, int nDays) {
        Calendar cal2 = Calendar.getInstance();

        cal2.setTime(aDate);
        cal2.add(Calendar.YEAR, -nDays);

        return cal2.getTime();
    }

    /**
     * DOCUMENT ME!
     *
     * @param aDate DOCUMENT ME!
     * @param nDays DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Date beforeMonth(Date aDate, int nDays) {
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(aDate);
        cal2.add(Calendar.MONDAY, -nDays);

        return cal2.getTime();
    }

    // public static void clobInsert(String infile) throws Exception

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        System.out.print(utf8("中国人"));
    }
}
