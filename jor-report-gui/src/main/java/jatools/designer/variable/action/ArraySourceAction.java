package jatools.designer.variable.action;

import jatools.designer.variable.ArraySourceDialog;
import jatools.designer.variable.TreeNodeValue;
import jatools.designer.variable.NodeSourceTree;

import jatools.dom.src.ArrayNodeSource;
import jatools.dom.src.NodeSource;

import java.awt.Component;
import java.awt.event.ActionEvent;

import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: jatools</p>
 * @author Jiang
 * @version 1.0
 */
public class ArraySourceAction extends AbstractAction {
    public static final int ADD = 0;
    public static final int DELETE = 1;
    public static final int MODIFY = 2;
    private int type;
    private Component c;
    private DefaultMutableTreeNode defaultMutableTreeNode;
    private NodeSource nodeSource;

    /**
     * Creates a new ArraySourceAction object.
     *
     * @param name DOCUMENT ME!
     * @param c DOCUMENT ME!
     * @param defaultMutableTreeNode DOCUMENT ME!
     * @param type DOCUMENT ME!
     */
    public ArraySourceAction(String name, Component c,
        DefaultMutableTreeNode defaultMutableTreeNode, int type) {
        super(name);
        this.c = c;
        this.defaultMutableTreeNode = defaultMutableTreeNode;

        TreeNodeValue nodeValue = (TreeNodeValue) defaultMutableTreeNode.getUserObject();
        this.nodeSource = nodeValue.getNodeSource();
        this.type = type;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        switch (type) {
        case ADD:
            addAction();

            break;

        case DELETE:
            deleteAction();

            break;

        case MODIFY:
            modifyAction();

            break;
        }
    }

    private void addAction() {
        ArrayNodeSource source = ArraySourceDialog.getSource(null, c);

        if (source != null) {
            nodeSource.add(source);
            updateTree(defaultMutableTreeNode, ADD);

            if (c instanceof NodeSourceTree) {
                ((NodeSourceTree) c).selectNodeSource(source);
            }
        }
    }

    private void deleteAction() {
        //        int option = JOptionPane.showConfirmDialog(c, "删除之后不能恢复，确定删除？", "提示",
        //                      JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        //              if (option == JOptionPane.OK_OPTION) {
        ArrayList brother = nodeSource.getParent().getChildren();
        nodeSource.getParent().remove(brother.indexOf(nodeSource));
        updateTree(defaultMutableTreeNode, DELETE);

        //         }
    }

    private void modifyAction() {
        ArrayNodeSource source = ArraySourceDialog.getSource((ArrayNodeSource) nodeSource, c);

        if (source != null) {
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
