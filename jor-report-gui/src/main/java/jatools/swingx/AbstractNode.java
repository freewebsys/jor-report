package jatools.swingx;

import jatools.util.Util;

import javax.swing.Icon;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public abstract class AbstractNode extends DefaultMutableTreeNode {
    static final Icon JAR_ICON = Util.getIcon("/jatools/icons/jarfile.gif");
    static final Icon CLASS_ICON = Util.getIcon("/jatools/icons/class.gif");
    static final Icon LIB_ICON = Util.getIcon("/jatools/icons/library.gif");
    static final Icon STATIC_ICON = Util.getIcon("/jatools/icons/static.gif");
    static final Icon PUBLIC_ICON = Util.getIcon("/jatools/icons/publicmethod.gif");
    public static final Icon CHECKED_ICON = Util.getIcon("/jatools/icons/checked.gif");
    public static final Icon NO_CHECK_ICON = Util.getIcon("/jatools/icons/nocheck.gif");
    static final int NOT_SELECTED = 0;
    static final int PARTLY_SELECTED = 1;
    static final int SELECTED = 2;
    static final int SELECT_DISABLE = -1;
    private boolean leaf = false;
    protected boolean explored = false;
    String memo;

    /**
     * Creates a new AbstractNode object.
     */
    private AbstractNode() {
        this(null);
    }

    /**
     * Creates a new AbstractNode object.
     *
     * @param userObject DOCUMENT ME!
     */
    public AbstractNode(Object userObject) {
        this(userObject, true, false);
    }

    /**
     * Creates a new AbstractNode object.
     *
     * @param userObject DOCUMENT ME!
     * @param allowsChildren DOCUMENT ME!
     * @param isSelected DOCUMENT ME!
     */
    public AbstractNode(Object userObject, boolean allowsChildren, boolean isSelected) {
        super(userObject, allowsChildren);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Icon getIcon();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isExplored() {
        return explored;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isLeaf() {
        return leaf;
    }

    protected void setLeaf(boolean yesno) {
        leaf = yesno;
    }

    /**
     * DOCUMENT ME!
     */
    public void explore() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getMemo() {
        return memo;
    }

    /**
     * DOCUMENT ME!
     *
     * @param memo DOCUMENT ME!
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }
}
