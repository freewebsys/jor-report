package jatools.dom.src;

import jatools.accessor.PropertyAccessor;

import jatools.component.table.GLayout;

import jatools.dom.ElementBase;

import jatools.engine.script.DebugOff;
import jatools.engine.script.Script;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
 */
public abstract class NodeSource implements PropertyAccessor, GLayout {
    NodeSource parent;
    ArrayList children = new ArrayList();
    String keyExpression;
    private String beforeLoad;
    private String afterLoad;
    protected String beforeCreate;
    protected String afterCreate;
    private ElementBase latestParentNode;



    /**
     * DOCUMENT ME!
     *
     * @param groups
     *            DOCUMENT ME!
     */
    public void layoutGroup(String[] groups) {
        NodeSource parent = this.getParent();

        NodeSource[] srcs = new NodeSource[groups.length];

        for (int i = 0; i < srcs.length; i++) {
            srcs[i] = parent.getNodeSourceByTagName(groups[i]);
        }

        parent.remove(this);

        NodeSource src = parent;

        for (int i = 0; i < srcs.length; i++) {
            NodeSource p = srcs[i].getParent();

            if (p != null) {
                p.remove(srcs[i]);
            }

            src.add(srcs[i]);
            src = srcs[i];
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return null;
    }

    protected void doBeforeCreate(Script script) {
        if ((this.beforeCreate != null) && (script != null)) {
            if (script != null) {
                script.set("me", this);
            }

            script.eval(beforeCreate);
        }
    }

    protected void doAfterCreate(Script script, Node node) {
        if ((this.afterCreate != null) && (script != null)) {
            script.set("me", this);
            script.set("node", node);
            script.eval(afterCreate);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public boolean isNodeListSource() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tag
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NodeSource tag(String tag) {
        return this.getNodeSourceByTagName(tag);
    }

    /**
     * DOCUMENT ME!
     *
     * @param tag
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NodeSource getNodeSourceByTagName(String tag) {
        Iterator it = this.children.iterator();

        while (it.hasNext()) {
            NodeSource src = (NodeSource) it.next();

            if (tag.equals(src.getTagName())) {
                return src;
            } else {
                NodeSource result = src.getNodeSourceByTagName(tag);

                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tag
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NodeSource getNodeSourceByPath(String tag) {
        if ((tag != null) || (tag.length() == 0)) {
            NodeSource context = this;
            String prefix = null;

            if (tag.startsWith(prefix = "/root/") || tag.equals(prefix = "/root") ||
                    tag.startsWith(prefix = "/")) {
                context = this.getRootNodeSource();
                tag = tag.substring(prefix.length());
            }

            return context.getNodeSourceByRelativePath(tag);
        }

        return this;
    }

    private NodeSource getRootNodeSource() {
        NodeSource src = this;

        while (!(src instanceof RootNodeSource) && src.getParent() != null) {
            src = src.getParent();
        }

        return src;
    }

    private NodeSource getNodeSourceByRelativePath(String tag) {
        String[] tags = tag.split("/");
        NodeSource select = this;

        for (int i = 0; i < tags.length; i++) {
            select = select.getChildByTagName(tags[i]);

            if (select == null) {
                return null;
            }
        }

        return select;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract String getTagName();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NodeSource getParent() {
        return this.parent;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NodeSource[] getChildSources() {
        return (NodeSource[]) this.children.toArray(new NodeSource[0]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param src
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NodeSource add(NodeSource src) {
        this.children.add(src);
        src.parent = this;

        return src;
    }

    /**
     * DOCUMENT ME!
     *
     * @param src
     *            DOCUMENT ME!
     */
    public void remove(int src) {
        this.children.remove(src);
    }

    /**
     * DOCUMENT ME!
     *
     * @param src
     *            DOCUMENT ME!
     */
    public void remove(NodeSource src) {
        this.children.remove(src);
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent
     *            DOCUMENT ME!
     * @param script
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Node create(Node parent, Script script);

    /**
     * DOCUMENT ME!
     *
     * @param parent
     *            DOCUMENT ME!
     * @param script
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Node[] createNodeList(Node parent, Script script);

    /**
     * DOCUMENT ME!
     *
     * @param children
     *            DOCUMENT ME!
     */
    public void setChildren(ArrayList children) {
        this.children = children;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public ArrayList getChildren() {
        return children;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public String getKeyExpression() {
        return keyExpression;
    }

    /**
     * DOCUMENT ME!
     *
     * @param keyExpression
     *            DOCUMENT ME!
     */
    public void setKeyExpression(String keyExpression) {
        this.keyExpression = keyExpression;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent2
     *            DOCUMENT ME!
     */
    public void setParent(NodeSource parent2) {
        this.parent = parent2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFullPath() {
        String result = "";
        NodeSource src = this;

        while ((src != null) && (src.getTagName() != null)) {
            result = "/" + src.getTagName() + result;
            src = src.parent;
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tag
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NodeSource getChildByTagName(String tag) {
        if (this.children != null) {
            Iterator it = this.children.iterator();

            while (it.hasNext()) {
                NodeSource result = (NodeSource) it.next();

                if (tag.equals(result.getTagName())) {
                    return result;
                }
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public String getAfterLoad() {
        return afterLoad;
    }

    /**
     * DOCUMENT ME!
     *
     * @param afterLoad
     *            DOCUMENT ME!
     */
    @DebugOff
    public void setAfterLoad(String afterLoad) {
        this.afterLoad = afterLoad;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public String getBeforeLoad() {
        return beforeLoad;
    }

    /**
     * DOCUMENT ME!
     *
     * @param beforeLoad
     *            DOCUMENT ME!
     */
    @DebugOff
    public void setBeforeLoad(String beforeLoad) {
        this.beforeLoad = beforeLoad;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public String getAfterCreate() {
        return afterCreate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param afterCreate
     *            DOCUMENT ME!
     */
    public void setAfterCreate(String afterCreate) {
        this.afterCreate = afterCreate;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public String getBeforeCreate() {
        return beforeCreate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param beforeCreate
     *            DOCUMENT ME!
     */
    public void setBeforeCreate(String beforeCreate) {
        this.beforeCreate = beforeCreate;
    }

    /**
     * DOCUMENT ME!
     *
     * @param node
     *            DOCUMENT ME!
     */
    public void setLatestParentNode(ElementBase node) {
        this.latestParentNode = node;
    }

    /**
     * DOCUMENT ME!
     */
    public void refresh() {
        this.latestParentNode.resetChildrenFor(this);
    }
}
