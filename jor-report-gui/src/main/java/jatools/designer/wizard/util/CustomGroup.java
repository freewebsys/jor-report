/*
 * Created on 2004-1-3
 *
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package jatools.designer.wizard.util;



/**
 * @author   java9
 */
public class CustomGroup implements Group {
    String groupBy;
    int orderBy; // acsend /descend= 0/1

    /**
     * @param groupBy
     * @param orderBy
     */
    public CustomGroup(String groupBy, int orderBy) {
        this.groupBy = groupBy;
        this.orderBy = orderBy;
    }

    /**
     * @return   Returns the groupBy.
     * @uml.property   name="groupBy"
     */
    public String getGroupBy() {
        return groupBy;
    }

    /**
     * @return Returns the orderBy.
     */
    public int getOrder() {
        return orderBy;
    }
}
