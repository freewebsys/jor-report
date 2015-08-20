package jatools.util;

import java.io.UnsupportedEncodingException;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class StringUtil {
    static String reserved = "~!@#$&*()=:/,;?+'";
    static String format(String fmt, String[] s1) {
        StringBuffer b = new StringBuffer();
        String[] fmts = fmt.split("#");

        int i = 0;

        for (i = 0; i < s1.length; i++) {
            b.append(fmts[i]);
            b.append(s1[i]);
        }

        if (i == (fmts.length - 1)) {
            b.append(fmts[i]);
        }

        return b.toString();
    }
    
    public static String encodeURI(String url) {
        if (url != null) {
            StringBuffer result = new StringBuffer();

            for (int i = 0; i < url.length(); i++) {
                String ch = url.charAt(i) + "";

                if (reserved.indexOf(ch) > -1) {
                    result.append(ch);
                } else {
                    try {
                        result.append(java.net.URLEncoder.encode(ch, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

            return result.toString();
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param fmt DOCUMENT ME!
     * @param s1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String format(String fmt, String s1) {
        return format(fmt, new String[] {
                s1
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @param fmt DOCUMENT ME!
     * @param s1 DOCUMENT ME!
     * @param s2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String format(String fmt, String s1, String s2) {
        return format(fmt, new String[] {
                s1,
                s2
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @param fmt DOCUMENT ME!
     * @param s1 DOCUMENT ME!
     * @param s2 DOCUMENT ME!
     * @param s3 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String format(String fmt, String s1, String s2, String s3) {
        return format(fmt, new String[] {
                s1,
                s2,
                s3
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @param fmt DOCUMENT ME!
     * @param s1 DOCUMENT ME!
     * @param s2 DOCUMENT ME!
     * @param s3 DOCUMENT ME!
     * @param s4 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String format(String fmt, String s1, String s2, String s3, String s4) {
        return format(fmt, new String[] {
                s1,
                s2,
                s3,
                s4
            });
    }

  
    /**
     * DOCUMENT ME!
     *
     * @param str DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int[] toIntArray(String str) {
        if (str != null) {
            String[] strings = str.split(",");
            int[] results = new int[strings.length];

            for (int i = 0; i < results.length; i++) {
                results[i] = Integer.parseInt(strings[i]);
            }

            return results;
        } else {
            return null;
        }
    }
}
