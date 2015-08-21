/*
 * Created on 2004-2-1
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package jatools.designer.wizard.util;


/**
 *
 *  @author zhou
 *
 *  To change the template for this generated type comment go to
 *  Window - Preferences - Java - Code Generation - Code and Comments
 *
 */
public interface Group {
    /**
     * DOCUMENT ME!
     */
    public static final int ACS = 0;

    /**
     * DOCUMENT ME!
     */
    public static final int DESC = 1;

    /**
     * @return Returns the groupBy.
     */
    public abstract String getGroupBy();

    /**
     * @return Returns the orderBy.
     */
    public abstract int getOrder();
}
