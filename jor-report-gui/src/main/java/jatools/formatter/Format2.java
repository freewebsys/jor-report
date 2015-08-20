package jatools.formatter;

import jatools.accessor.PropertyAccessor;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: EZSoft.</p>
 * @author 周文军
 * @version 1.0
 */
public interface Format2 extends PropertyAccessor {
    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String format(Object o);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toExcel();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toPattern();
}
