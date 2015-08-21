package jatools.designer.wizard.crosstab;

import jatools.designer.App;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author Jiang Dehua
 * @version 1.0
 */
public class OrderBy {
    public static final String ASC = App.messages.getString("res.121");
    public static final String DESC = App.messages.getString("res.122");
    public static final String ORIGINAL = App.messages.getString("res.123");
    String orderby;

    /**
     * Creates a new OrderBy object.
     *
     * @param orderby DOCUMENT ME!
     */
    public OrderBy(String orderby) {
        super();
        this.orderby = orderby;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getOrderby() {
        return orderby;
    }

    /**
     * DOCUMENT ME!
     *
     * @param orderBy DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int getIntOrder(String orderBy) {
        if (orderBy.equals(ASC)) {
            return jatools.dom.Group.ASCEND;
        } else if (orderBy.equals(DESC)) {
            return jatools.dom.Group.DESEND;
        } else {
            return jatools.dom.Group.ORIGINAL;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param orderby DOCUMENT ME!
     */
    public void setOrderby(String orderby) {
        this.orderby = orderby;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return orderby;
    }
}
