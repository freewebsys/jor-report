package jatools.designer;

import jatools.swingx.SimpleTreeNode;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class DataTree extends JTree implements TreeSelectionListener {
    final static Color disabledForeground = UIManager.getColor("Label.disabledForeground");
    ArrayList listenerCache = new ArrayList();
    ActionListener dbclickAction;

    /**
     * Creates a new DataTree object.
     */
    public DataTree() {
        super();

        setCellRenderer(new _IconedTreeCellRenderer());

        this.addTreeSelectionListener(this);
        this.setRootVisible(false);
        this.setShowsRootHandles(true);
    }

    /**
     * Creates a new DataTree object.
     *
     * @param root DOCUMENT ME!
     */
    public DataTree(SimpleTreeNode root) {
        setRoot((SimpleTreeNode) root);

        setCellRenderer(new _IconedTreeCellRenderer());

        this.addTreeSelectionListener(this);

        this.setRootVisible(false);
    }

    /**
     * DOCUMENT ME!
     */
    public void expandAll() {
        TreeNode root = (TreeNode) getModel().getRoot();

        expandAll(new TreePath(root));
    }

    private void expandAll(TreePath parent) {
        TreeNode node = (TreeNode) parent.getLastPathComponent();

        if (node.getChildCount() >= 0) {
            for (Enumeration e = node.children(); e.hasMoreElements();) {
                TreeNode n = (TreeNode) e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(path);
            }
        }

        expandPath(parent);
    }

    /**
     * DOCUMENT ME!
     *
     * @param action DOCUMENT ME!
     */
    public void setDoubleClickAction(ActionListener action) {
        if (action != null) {
            dbclickAction = action;
            this.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() > 1) {
                            ActionEvent e1 = new ActionEvent(DataTree.this, 0, null);
                            dbclickAction.actionPerformed(e1);
                        }
                    }
                });
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] getUserObjects(int type) {
        ArrayList list = new ArrayList();

        SimpleTreeNode root = (SimpleTreeNode) getModel().getRoot();
        collectUserObject(root, type, list);

        return list.toArray();
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SimpleTreeNode[] getNodes(int type) {
        ArrayList list = new ArrayList();

        SimpleTreeNode root = (SimpleTreeNode) getModel().getRoot();
        collectNodes(root, type, list);

        return (SimpleTreeNode[]) list.toArray(new SimpleTreeNode[0]);
    }

    private void collectNodes(SimpleTreeNode root, int type, ArrayList list) {
        if (root.getType() == type) {
            list.add(root);
        }

        for (int i = 0; i < root.getChildCount(); i++) {
            SimpleTreeNode child = (SimpleTreeNode) root.getChildAt(i);
            collectNodes(child, type, list);
        }
    }

    private static void collectUserObject(SimpleTreeNode root, int type, ArrayList list) {
        if (root.getType() == type) {
            list.add(root.getUserObject());
        }

        for (int i = 0; i < root.getChildCount(); i++) {
            SimpleTreeNode child = (SimpleTreeNode) root.getChildAt(i);
            collectUserObject(child, type, list);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TreePath getTreePath(TreeNode node) {
        return new TreePath(getPathToRoot(node, 0));
    }

    protected TreeNode[] getPathToRoot(TreeNode aNode, int depth) {
        TreeNode[] retNodes;

        if (aNode == null) {
            if (depth == 0) {
                return null;
            } else {
                retNodes = new TreeNode[depth];
            }
        } else {
            depth++;

            if (aNode == getModel().getRoot()) {
                retNodes = new TreeNode[depth];
            } else {
                retNodes = getPathToRoot(aNode.getParent(), depth);
            }

            retNodes[retNodes.length - depth] = aNode;
        }

        return retNodes;
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void addChangeListener(ChangeListener lst) {
        listenerCache.add(lst);
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void removeChangeListener(ChangeListener lst) {
        listenerCache.remove(lst);
    }

    void fireChangeListener() {
        ChangeEvent e = new ChangeEvent(this);

        for (Iterator iter = listenerCache.iterator(); iter.hasNext();) {
            ChangeListener element = (ChangeListener) iter.next();

            element.stateChanged(e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param newModel DOCUMENT ME!
     */
    public void setModel(TreeModel newModel) {
        super.setModel(newModel);

        if (this.getRowCount() > 0) {
            this.expandRow(0);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void valueChanged(TreeSelectionEvent e) {
        fireChangeListener();
    }

    /**
     * DOCUMENT ME!
     *
     * @param root DOCUMENT ME!
     */
    public void setRoot(SimpleTreeNode root) {
        setModel(new DefaultTreeModel(root));
        setRootVisible(false);
    }

    public class _IconedTreeCellRenderer extends DefaultTreeCellRenderer {
        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected,
            boolean expanded, boolean leaf, int row, boolean hasFocus) {
            super.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, hasFocus);

            if (value instanceof SimpleTreeNode) {
                SimpleTreeNode node = (SimpleTreeNode) value;

                setIcon(node.getIcon());

                if (node.isDisabled()) {
                    this.selected = false;
                    setForeground(disabledForeground);
                }

                if (node.getTooltip() != null) {
                    this.setToolTipText(node.getTooltip());
                }
            }

            return this;
        }
    }
}
