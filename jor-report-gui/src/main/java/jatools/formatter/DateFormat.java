package jatools.formatter;

import jatools.accessor.PropertyDescriptor;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company: EZSoft.
 * </p>
 *
 * @author 周文军
 * @version 1.0
 */
public class DateFormat implements Format2 {
    static final String TYPE = "FormatType"; //
    static final String STYLE = "FormatStyle"; //

    /**
     * DOCUMENT ME!
     */
    public static String DATE = "1"; //

    /**
     * DOCUMENT ME!
     */
    public static String TIME = "2"; //

    /**
     * DOCUMENT ME!
     */
    public static final int FULL = 0;

    /**
     * Constant for long style pattern.
     */
    public static final int LONG = 1;

    /**
     * Constant for medium style pattern.
     */
    public static final int MEDIUM = 2;

    /**
     * Constant for short style pattern.
     */
    public static final int SHORT = 3;

    /**
     * DOCUMENT ME!
     */
    public static Date NOW = new Date();
    static SimpleDateFormat dateFormat = new SimpleDateFormat();
    private String formatType = DATE;
    private int formatStyle;
    private String pattern;

    /**
     * Creates a new DateFormat object.
     *
     * @param type DOCUMENT ME!
     * @param style DOCUMENT ME!
     */
    public DateFormat(String type, int style) {
        this.formatType = type;
        this.formatStyle = style;
    }

    /**
     * Creates a new DateFormat object.
     *
     * @param pattern DOCUMENT ME!
     */
    public DateFormat(String pattern) {
        this.pattern = pattern;
    }

    /**
     * Creates a new DateFormat object.
     */
    public DateFormat() {
        this(DATE, java.text.DateFormat.DEFAULT);
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     * @param format DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String format(Date d, String format) {
        // format(d,"yyyy-MM-dd") -> 2004-01-28
        dateFormat.applyPattern(format);

        return dateFormat.format(d);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] {
            new PropertyDescriptor(TYPE, String.class, PropertyDescriptor.SERIALIZABLE),
            new PropertyDescriptor(STYLE, Integer.TYPE, PropertyDescriptor.SERIALIZABLE),
        };
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return format(NOW);
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String format(Object obj) {
        if (pattern != null) {
            return format((Date) obj, pattern);
        } else {
            Format f = (DATE.equals(formatType))
                ? java.text.DateFormat.getDateInstance(formatStyle)
                : java.text.DateFormat.getTimeInstance(formatStyle);

            return f.format(obj);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (obj instanceof DateFormat) {
            DateFormat that = (DateFormat) obj;

            if (that.pattern != null) {
                return that.pattern.equals(pattern);
            }

            if (pattern != null) {
                return pattern.equals(that.pattern);
            }

            return (equals(that.formatType, formatType) && (formatStyle == that.formatStyle));
        } else {
            return false;
        }
    }

    static boolean equals(Object one, Object another) {
        return (one == null) ? (another == null) : one.equals(another);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getFormatStyle() {
        return formatStyle;
    }

    /**
     * DOCUMENT ME!
     *
     * @param style DOCUMENT ME!
     */
    public void setFormatStyle(int style) {
        this.formatStyle = style;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFormatType() {
        return formatType;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     */
    public void setFormatType(String type) {
        this.formatType = type;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        DateFormat inst = new DateFormat();
        inst.formatType = this.formatType;
        inst.formatStyle = this.formatStyle;

        return inst;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toExcel() {
        String result = toPattern();

        if ((result != null) && (result.indexOf('\'') > -1)) {
            result = result.replaceAll("\'", "");
        }

        return result;

        // return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toPattern() {
        String result = null;

        if (pattern != null) {
            result = pattern;
        } else {
            Format f = (DATE.equals(formatType))
                ? java.text.DateFormat.getDateInstance(formatStyle)
                : java.text.DateFormat.getTimeInstance(formatStyle);

            result = (f instanceof SimpleDateFormat) ? ((SimpleDateFormat) f).toPattern() : null;
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        return toExcel().hashCode();
    }
}
