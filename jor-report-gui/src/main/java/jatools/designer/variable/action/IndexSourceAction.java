package jatools.designer.variable.action;

import jatools.designer.Main;
import jatools.designer.variable.TreeNodeValue;
import jatools.designer.variable.NodeSourceTree;
import jatools.designer.variable.dialog.IndexDialog;

import jatools.dom.src.DatasetNodeSource;
import jatools.dom.src.GroupNodeSource;
import jatools.dom.src.IndexNodeSource;
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
 * @version $Revision: 1.9 $
  */
public class IndexSourceAction extends AbstractAction {
    public static final int ADD = 0;
    public static final int DELETE = 1;
    public static final int MODIFY = 2;
    public static final int DEFINE_COLUMN = 3;
    private int type;
    private Component c;
    private NodeSource nodeSource;
    private DefaultMutableTreeNode defaultMutableTreeNode;

    /**
     * Creates a new IndexSourceAction object.
     *
     * @param name DOCUMENT ME!
     * @param c DOCUMENT ME!
     * @param defaultMutableTreeNode DOCUMENT ME!
     * @param type DOCUMENT ME!
     */
    public IndexSourceAction(String name, Component c,
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
            addIndex();

            break;

        case DELETE:
            deleteIndex();

            break;

        case MODIFY:
            modifyIndex();

            break;
        }
    }

    private void addIndex() {
        IndexDialog d = new IndexDialog((DatasetNodeSource) nodeSource, null);
        d.setVisible(true);

        IndexNodeSource indexNodeSource = d.createNodeSource();

        if (indexNodeSource != null) {
            DatasetNodeSource dataSrc = (DatasetNodeSource) nodeSource;

            if (dataSrc.getIndexFields() == null) {
                int option = JOptionPane.showConfirmDialog(Main.getInstance(),
                        "本数据集节点不存在任何索引,建议您将本索引加入到数据集节点,是否加入?", "提示", JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                if (option == JOptionPane.YES_OPTION) {
                    dataSrc.setIndexFields(indexNodeSource.getIndexFields());
                    updateTree(defaultMutableTreeNode, MODIFY);

                    return;
                }
            }

            nodeSource.add(indexNodeSource);
            updateTree(defaultMutableTreeNode, ADD);

            if (c instanceof NodeSourceTree) {
                ((NodeSourceTree) c).selectNodeSource(indexNodeSource);
            }
        }
    }

    private void deleteIndex() {
        //        int option = JOptionPane.showConfirmDialog(c, "删除之后不能恢复，确定删除？", "提示",
        //                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        //
        //        if (option == JOptionPane.OK_OPTION) {
        ArrayList brother = nodeSource.getParent().getChildren();
        nodeSource.getParent().remove(brother.indexOf(nodeSource));
        updateTree(defaultMutableTreeNode, DELETE);

        //        }
    }

    private void modifyIndex() {
        IndexDialog d = new IndexDialog((DatasetNodeSource) nodeSource.getParent(),
                (IndexNodeSource) nodeSource);
        d.setVisible(true);

        if (d.isSuccess()) {
            updateTree(defaultMutableTreeNode, MODIFY);
        }
    }

    private boolean containOwnGroup(NodeSource ns) {
        boolean hasGroup = false;
        ArrayList children = ns.getChildren();

        for (int i = 0; i < children.size(); i++) {
            NodeSource _ns = (NodeSource) children.get(i);

            if (_ns instanceof GroupNodeSource) {
                hasGroup = true;

                break;
            } else if (_ns instanceof DatasetNodeSource) {
                hasGroup = false;

                break;
            } else {
                hasGroup = containOwnGroup(_ns);
            }
        }

        return hasGroup;
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
