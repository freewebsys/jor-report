package jatools.dom;

import jatools.accessor.AutoAccessor;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
  */
public class Group extends AutoAccessor {
    public static final int ASCEND = 0;
    public static final int DESEND = 1;
    public static final int ORIGINAL = 2;
    int order;
    String field;

    /**
     * Creates a new Group object.
     *
     * @param field DOCUMENT ME!
     * @param order DOCUMENT ME!
     */
    public Group(String field, int order) {
        this.field = field;
        this.order = order;
    }

    /**
     * Creates a new Group object.
     *
     * @param field DOCUMENT ME!
     */
    public Group(String field) {
        this.field = field;
    }

    /**
     * Creates a new Group object.
     */
    public Group() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getField() {
        return field;
    }

    /**
     * DOCUMENT ME!
     *
     * @param field DOCUMENT ME!
     */
    public void setField(String field) {
        this.field = field;
    }

    /**
     * DOCUMENT ME!
     *
     * @param order DOCUMENT ME!
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
       * DOCUMENT ME!
       *
       * @return DOCUMENT ME!
       */
    public int getOrder() {
        return order;
    }
}
