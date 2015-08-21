package jatools.designer.variable;

import jatools.classtree.AbstractNode;
import jatools.classtree.ClassNode;
import jatools.classtree.LibFileNode;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;



public class BeanTree extends JTree {
   public BeanTree(){
	   super();
	   LibFileNode rootNode = new LibFileNode("lib");
       rootNode.explore();
       DefaultTreeModel model=new DefaultTreeModel(rootNode);
       this.setModel(model);
   }
   public void expandPath(TreePath path){
	   AbstractNode node = (AbstractNode) path.getLastPathComponent();

       if (!node.isExplored()) {
           DefaultTreeModel model = (DefaultTreeModel) getModel();
           node.explore();
           model.nodeStructureChanged(node);
       }
       if(node instanceof ClassNode ){
        return;
       }
       super.expandPath(path);
   }
}
