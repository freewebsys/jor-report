package jatools.designer.variable.action;

import jatools.designer.variable.TreeNodeValue;
import jatools.designer.variable.NodeSourceTree;
import jatools.dom.src.NodeSource;
import jatools.dom.src.RowNodeSource;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.tree.DefaultMutableTreeNode;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
  */
public class RowSourceAction extends AbstractAction {
    public static final int ADD_ROW = 1;
    public static final int DELETE_ROW = 2;
    private Component c;
    private NodeSource nodeSource;
    private int type;
    private DefaultMutableTreeNode defaultMutableTreeNode;

    /**
     * Creates a new RowSourceAction object.
     *
     * @param name DOCUMENT ME!
     * @param c DOCUMENT ME!
     * @param defaultMutableTreeNode DOCUMENT ME!
     * @param type DOCUMENT ME!
     */
    public RowSourceAction(String name, Component c, DefaultMutableTreeNode defaultMutableTreeNode,
        int type) {
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
        case ADD_ROW:
            addRowAction();

            break;

        case DELETE_ROW:
            deleteRowAction();

            break;
        }
    }

    private void addRowAction() {
        RowNodeSource rowNodeSource = new RowNodeSource();
        nodeSource.add(rowNodeSource);
        updateTree(defaultMutableTreeNode, ADD_ROW);
        if (c instanceof NodeSourceTree) 
            ((NodeSourceTree) c).selectNodeSource(rowNodeSource);
    }

    private void deleteRowAction() {
//        ArrayList v = nodeSource.getChildren();
//        NodeSource target = null;
//        DefaultMutableTreeNode targetRowNode = null;
//
//        for (int i = 0; i < v.size(); i++) {
//            NodeSource ns = (NodeSource) v.get(i);
//
//            if (ns.getTagName().equals("Row")) {
//                target = ns;
//
//                break;
//            }
//        }
//
//        Enumeration nodes = defaultMutableTreeNode.children();
//
//        while (nodes.hasMoreElements()) {
//            DefaultMutableTreeNode node = (DefaultMutableTreeNode) nodes.nextElement();
//            TreeNodeValue value = (TreeNodeValue) node.getUserObject();
//
//            if (value.getNodeSource() instanceof RowNodeSource) {
//                targetRowNode = node;
//
//                break;
//            }
//        }
//
//        if (target != null) {
//            nodeSource.remove(v.indexOf(target));
//
//            if (targetRowNode != null) {
//                updateTree(targetRowNode, DELETE_ROW);
//            }
//        }
        
    
                ArrayList brother=nodeSource.getParent().getChildren();
                nodeSource.getParent().remove(brother.indexOf(nodeSource));
                updateTree(defaultMutableTreeNode,DELETE_ROW);

        
    }

    private void updateTree(DefaultMutableTreeNode defaultMutableTreeNode, int type) {
        if (c instanceof NodeSourceTree) {
            NodeSourceTree tree = (NodeSourceTree) c;

            switch (type) {
            case ADD_ROW:
                tree.updateTreeAfterNodeAdded2(defaultMutableTreeNode);

                break;

            case DELETE_ROW:
                tree.updateTreeAfterNodeDeleted(defaultMutableTreeNode);

                break;
            }
        }
    }
}
