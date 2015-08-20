package jatools.formatter;

import jatools.accessor.PropertyDescriptor;


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
public class DecimalFormat implements Format2 {
    private static final String PATTERN = "Pattern"; //
    private static java.text.DecimalFormat format = new java.text.DecimalFormat();
    private String pattern;

    /**
     * Creates a new ZDecimalFormat object.
     *
     * @param pattern
     *            DOCUMENT ME!
     */
    public DecimalFormat(String pattern) {
        this.pattern = pattern;
    }

    /**
     * Creates a new ZDecimalFormat object.
     */
    public DecimalFormat() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        return pattern.hashCode();
    }

    /**
     * DOCUMENT ME!
     *
     * @param n
     *            DOCUMENT ME!
     * @param pattern
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String format(Number n, String pattern) {
        format.applyPattern(pattern);

        return format.format(n);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] {
            new PropertyDescriptor(PATTERN, String.class, PropertyDescriptor.SERIALIZABLE)
        };
    }

    /**
     * DOCUMENT ME!
     *
     * @param o
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String format(Object o) {
        format.applyPattern(pattern);

        try {
            return format.format(o);
        } catch (Exception ex) {
            // ex.printStackTrace();
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args
     *            DOCUMENT ME!
     */

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public java.text.DecimalFormat getFormat() {
        return format;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toPattern() {
        return pattern;
    }
    
    public String getPattern() {
        return pattern;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return this.toPattern();
    }

    /**
     * DOCUMENT ME!
     *
     * @param pattern
     *            DOCUMENT ME!
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        DecimalFormat inst = new DecimalFormat();
        inst.pattern = this.pattern;

        return inst;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toExcel() {
        // return null;
        return pattern;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (obj instanceof DecimalFormat) {
            DecimalFormat anthor = (DecimalFormat) obj;

            return anthor.pattern.equals(pattern);
        } else {
            return false;
        }
    }
}
