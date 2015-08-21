package jatools.designer.variable.action;

import jatools.designer.variable.TreeNodeValue;
import jatools.designer.variable.NodeSourceTree;
import jatools.designer.variable.dialog.GroupSourceDialog;
import jatools.dom.src.GroupNodeSource;
import jatools.dom.src.NodeSource;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.tree.DefaultMutableTreeNode;


public class GroupSourceAction extends AbstractAction{
    private Component c;
    private NodeSource nodeSource;
    private int type;
    public static final int ADD_GROUP=1;
    public static final int DELETE_GROUP=2;
    public static final int MODIFY_GROUP=3;
    private DefaultMutableTreeNode defaultMutableTreeNode;
    public GroupSourceAction(String name,Component c,DefaultMutableTreeNode defaultMutableTreeNode,int type) {
        super(name);
        this.type=type;
        this.c=c;
        this.defaultMutableTreeNode=defaultMutableTreeNode;
        TreeNodeValue nodeValue=(TreeNodeValue)defaultMutableTreeNode.getUserObject();
        this.nodeSource=nodeValue.getNodeSource();
    }

    public void actionPerformed(ActionEvent e) {
        switch(type){
        case ADD_GROUP:
            addGroupAction();
            break;
        case DELETE_GROUP:
            deleteGroupAction();
            break;
        case MODIFY_GROUP:
            modifyGroupAction();
            break;
        }


    }
    private void addGroupAction(){
       GroupNodeSource source=GroupSourceDialog.getNodeSource(nodeSource,null,c);
       if(source!=null){
           nodeSource.add(source);
           updateTree(defaultMutableTreeNode,ADD_GROUP);
           if (c instanceof NodeSourceTree) 
               ((NodeSourceTree) c).selectNodeSource(source);
       }

    }
    private void deleteGroupAction(){
//        int option=JOptionPane.showConfirmDialog(c,"删除之后不能恢复，确定删除？","提示",
//                JOptionPane.OK_CANCEL_OPTION,JOptionPane.WARNING_MESSAGE);
//        if(option==JOptionPane.OK_OPTION){
            ArrayList brother=nodeSource.getParent().getChildren();
            nodeSource.getParent().remove(brother.indexOf(nodeSource));
            updateTree(defaultMutableTreeNode,DELETE_GROUP);
//         }
        }
    private void modifyGroupAction(){
        GroupNodeSource source=GroupSourceDialog.
                               getNodeSource(nodeSource.getParent(),(GroupNodeSource)nodeSource,c);
        if(source!=null){
        updateTree(defaultMutableTreeNode,MODIFY_GROUP);
        }
    }
    private void updateTree(DefaultMutableTreeNode defaultMutableTreeNode,int type){
        if (c instanceof NodeSourceTree) {
       NodeSourceTree tree = (NodeSourceTree) c;
       switch(type){
         case ADD_GROUP:
           tree.updateTreeAfterNodeAdded2(defaultMutableTreeNode);
           break;
          case MODIFY_GROUP:
            tree.updateTreeAfterNodeModified(defaultMutableTreeNode,nodeSource);
            break;
          case DELETE_GROUP:
            tree.updateTreeAfterNodeDeleted(defaultMutableTreeNode);
            break;
       }
       }
    }

}
