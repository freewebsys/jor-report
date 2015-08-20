package jatools.designer.variable.action;

import jatools.designer.Main;

import jatools.designer.variable.NodeSourceTree;
import jatools.designer.variable.TreeNodeValue;
import jatools.designer.variable.dialog.CrossIndexDialog;

import jatools.dom.src.CrossIndexNodeSource;
import jatools.dom.src.DatasetNodeSource;
import jatools.dom.src.NodeSource;

import java.awt.Component;
import java.awt.event.ActionEvent;

import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class CrossSourceAction extends AbstractAction {
    public static final int ADD = 0;
    public static final int DELETE = 1;
    public static final int MODIFY = 2;
    public static final int DEFINE_COLUMN = 3;
    private int type;
    private Component c;
    private NodeSource nodeSource;
    private DefaultMutableTreeNode defaultMutableTreeNode;

    /**
     * Creates a new CrossSourceAction object.
     *
     * @param name DOCUMENT ME!
     * @param c DOCUMENT ME!
     * @param defaultMutableTreeNode DOCUMENT ME!
     * @param type DOCUMENT ME!
     */
    public CrossSourceAction(String name, Component c,
        DefaultMutableTreeNode defaultMutableTreeNode, int type) {
        super(name);
        this.type = type;
        this.c = c;
        this.defaultMutableTreeNode = defaultMutableTreeNode;

        TreeNodeValue nodeValue = (TreeNodeValue) defaultMutableTreeNode.getUserObject();
        this.nodeSource = nodeValue.getNodeSource();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        switch (type) {
        case ADD:
            addCross();

            break;

        case DELETE:
            delCross();

            break;

        case MODIFY:
            modifyCross();

            break;
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void addCross() {
        if (this.nodeSource instanceof DatasetNodeSource) {
            CrossIndexDialog d = new CrossIndexDialog((DatasetNodeSource) nodeSource, null);
            d.setVisible(true);

            CrossIndexNodeSource crossIndexNodeSource = d.createNodeSource();

            if (crossIndexNodeSource != null) {
                DatasetNodeSource dataSrc = (DatasetNodeSource) nodeSource;

                if (!dataSrc.hasIndex()) {
                    if ((crossIndexNodeSource.getTagName() == null) ||
                            (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(
                                Main.getInstance(), "本数据集节点不存在任何索引,建议您将本索引加入到数据集节点,是否加入?", "提示",
                                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE))) {
                        dataSrc.setIndexFields(crossIndexNodeSource.getIndexFields());
                        dataSrc.setIndexFields2(crossIndexNodeSource.getIndexFields2());
                        updateTree(defaultMutableTreeNode, MODIFY);

                        return;
                    }
                }

                nodeSource.add(crossIndexNodeSource);
                updateTree(defaultMutableTreeNode, ADD);

                if (c instanceof NodeSourceTree) {
                    ((NodeSourceTree) c).selectNodeSource(crossIndexNodeSource);
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void delCross() {
        ArrayList brother = nodeSource.getParent().getChildren();
        nodeSource.getParent().remove(brother.indexOf(nodeSource));
        updateTree(defaultMutableTreeNode, DELETE);
    }

    /**
     * DOCUMENT ME!
     */
    public void modifyCross() {
        CrossIndexDialog d = new CrossIndexDialog((DatasetNodeSource) nodeSource.getParent(),
                (CrossIndexNodeSource) nodeSource);
        d.setVisible(true);

        if (d.isSuccess()) {
            updateTree(defaultMutableTreeNode, MODIFY);
        }
    }

    private void updateTree(DefaultMutableTreeNode defaultMutableTreeNode, int type) {
        if (c instanceof NodeSourceTree) {
            NodeSourceTree tree = (NodeSourceTree) c;

            switch (type) {
            case ADD:
                tree.updateTreeAfterNodeAdded2(defaultMutableTreeNode);

                break;

            case MODIFY:
                tree.updateTreeAfterNodeModified(defaultMutableTreeNode, nodeSource);

                break;

            case DELETE:
                tree.updateTreeAfterNodeDeleted(defaultMutableTreeNode);

                break;
            }
        }
    }
}
