/**
 *
 */
package jatools.dom.src;

import jatools.accessor.PropertyDescriptor;

import jatools.component.ComponentConstants;

import jatools.dataset.GroupView;
import jatools.dataset.RowSet;

import jatools.dom.DatasetBasedNode;
import jatools.dom.Group;
import jatools.dom.GroupNode;

import jatools.engine.script.Script;

import org.w3c.dom.Node;


/**
 * @author java9
 *
 */
public class GroupNodeSource extends NodeSource {
    Group group;

    /**
     * Creates a new GroupNodeSource object.
     *
     * @param group DOCUMENT ME!
     */
    public GroupNodeSource(Group group) {
        this.group = group;
    }

    /**
    *
    */
    public GroupNodeSource() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] {
            ComponentConstants._CHILDREN,
            new PropertyDescriptor("Group", Group.class, PropertyDescriptor.SERIALIZABLE),
            new PropertyDescriptor("KeyExpression", String.class, PropertyDescriptor.SERIALIZABLE)
        };
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Group getGroup() {
        return group;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTagName() {
        return group.getField();
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param script DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node create(Node parent, Script script) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param script DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node[] createNodeList(Node parent, Script script) {
        DatasetBasedNode n = (DatasetBasedNode) parent;
        Object[] parentKeys = n.getKey().elements;
        RowSet[] rowsets = new GroupView(n.getRowSet(), this.group).getRowSets();
        Node[] result = new Node[rowsets.length];

        for (int i = 0; i < rowsets.length; i++) {
            Object[] newkeys = new Object[parentKeys.length + 1];
            System.arraycopy(parentKeys, 0, newkeys, 0, parentKeys.length);
            newkeys[parentKeys.length] = rowsets[i].key().elements[0];

            rowsets[i].key().elements = newkeys;

            GroupNode groupNode = new GroupNode(n, this.getTagName(), rowsets[i]);
            groupNode.setSource(this);
            result[i] = groupNode;
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param group DOCUMENT ME!
     */
    public void setGroup(Group group) {
        this.group = group;
    }
}
