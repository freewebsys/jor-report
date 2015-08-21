package jatools.engine.script;

import java.util.Date;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class SystemScript {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int lastYear() {
        return lastYear(new Date());
    }

    /**
     * DOCUMENT ME!
     *
     * @param date DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int lastYear(Date date) {
        return date.getYear() + 1899;
    }

    /**
     * DOCUMENT ME!
     *
     * @param date DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int nextYear(Date date) {
        return date.getYear() + 1901;
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object sum(Object arr) {
        if (arr instanceof Object[]) {
            return jatools.math.Math.sum((Object[]) arr);
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object avg(Object arr) {
        if (arr instanceof Object[]) {
            return jatools.math.Math.avg((Object[]) arr);
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object max(Object arr) {
        if (arr instanceof Object[]) {
            return jatools.math.Math.max((Object[]) arr);
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object min(Object arr) {
        if (arr instanceof Object[]) {
            return jatools.math.Math.min((Object[]) arr);
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param arr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object count(Object arr) {
        if (arr instanceof Object[]) {
            return new Integer(((Object[]) arr).length);
        } else {
            return null;
        }
    }
}
