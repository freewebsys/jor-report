package jatools.designer.data;

import jatools.swingx.SimpleTreeNode;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JDialog;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class IconTree extends JTree implements TreeSelectionListener {
    private final static Color disabledForeground = UIManager.getColor("Label.disabledForeground");
    private ArrayList listenerCache = new ArrayList();
    private ActionListener doubleClickAction;
    private ActionListener popupListener;
    public boolean isSingleClicked = false;

    /**
     * Creates a new IconTree object.
     */
    public IconTree() {
        setCellRenderer(new _IconedTreeCellRenderer());

        addTreeSelectionListener(this);

        addMouseListener(new MouseAdapter() {
                public void mouseReleased(MouseEvent e) {
                    if (e.isPopupTrigger() && (popupListener != null)) {
                        popupListener.actionPerformed(new ActionEvent(e, 0, null));
                    }
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @param popupListener DOCUMENT ME!
     */
    public void setPopupActionListener(ActionListener popupListener) {
        this.popupListener = popupListener;
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
     * @param action DOCUMENT ME!
     */
    public void setDoubleClickAction(ActionListener action) {
        this.doubleClickAction = action;

        if (doubleClickAction != null) {
            addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        if (e.getClickCount() > 1) {
                            TreePath path = getPathForLocation(e.getX(), e.getY());

                            if (path != null) {
                                doubleClickAction.actionPerformed(new ActionEvent(IconTree.this, 0,
                                        null));
                            }
                        }
                    }
                });
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSelectedType() {
        TreePath path = getSelectionPath();

        return (path == null) ? (-1) : ((SimpleTreeNode) path.getLastPathComponent()).getType();
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
     * @param root DOCUMENT ME!
     */
    public void setRoot(SimpleTreeNode root) {
        setRoot(root, true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param root DOCUMENT ME!
     * @param expand DOCUMENT ME!
     */
    public void setRoot(SimpleTreeNode root, boolean expand) {
        setModel(new DefaultTreeModel(root));

        expandAll();
    }

    protected void expandAll() {
        for (int i = 0; i < getRowCount(); i++) {
            expandRow(i);
        }
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
     * @param userObject DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean selectObject(Object userObject) {
        DefaultTreeModel model = (DefaultTreeModel) getModel();
        SimpleTreeNode node = find((SimpleTreeNode) model.getRoot(), userObject);

        boolean found = false;

        if (node != null) {
            TreePath tp = getTreePath(node);
            setSelectionPath(tp);

            found = true;
        }

        return found;
    }

    protected TreePath getTreePath(TreeNode node) {
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

    protected SimpleTreeNode find(SimpleTreeNode fromNode, Object uo) {
        if (fromNode.getUserObject().equals(uo)) {
            return fromNode;
        } else {
            for (int i = 0; i < fromNode.getChildCount(); i++) {
                SimpleTreeNode node = find((SimpleTreeNode) fromNode.getChildAt(i), uo);

                if (node != null) {
                    return node;
                }
            }

            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param helper DOCUMENT ME!
     */
    public void setTooltipHelper(TooltipHelper helper) {
        _IconedTreeCellRenderer renderer = (_IconedTreeCellRenderer) getCellRenderer();
        renderer.setTooltipHelper(helper);

        if (helper != null) {
            ToolTipManager.sharedInstance().registerComponent(this);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getSelectedObject() {
        TreePath path = getSelectionPath();

        if (path == null) {
            return null;
        }

        SimpleTreeNode node = (SimpleTreeNode) path.getLastPathComponent();

        if (node == null) {
            return null;
        } else {
            return node.getUserObject();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        JDialog d = new JDialog();
        d.getContentPane().add(new IconTree());
        d.pack();
        d.show();
    }

    public class _IconedTreeCellRenderer extends DefaultTreeCellRenderer {
        TooltipHelper tipHelper;

        public void setTooltipHelper(TooltipHelper tipHelper) {
            this.tipHelper = tipHelper;
        }

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

                if (tipHelper != null) {
                    setToolTipText(tipHelper.getToolTipText(value));
                }
            }

            return this;
        }
    }
}
