package jatools.classtree;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.plaf.ColorUIResource;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

public class NodeRenderer extends JPanel implements TreeCellRenderer {
	protected JLabel check;

	DefaultTreeCellRenderer cellRenderer = new DefaultTreeCellRenderer();

	DefaultTreeCellRenderer checkRenderer = new DefaultTreeCellRenderer();

	public NodeRenderer() {
		setLayout(new BorderLayout());
		add(check = new JLabel(), BorderLayout.WEST);
		add(checkRenderer, BorderLayout.CENTER);
	}

	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean isSelected, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
         if(value instanceof AbstractNode){
		AbstractNode node = (AbstractNode) value;

		
			cellRenderer.getTreeCellRendererComponent(tree, value, isSelected,
					expanded, leaf, row, hasFocus);
			cellRenderer.setIcon(node.getIcon());
			return cellRenderer;
         }
         else{
        	 return cellRenderer.getTreeCellRendererComponent(tree, value, isSelected,
 					expanded, leaf, row, hasFocus);
         }

//		} else {
//			checkRenderer.getTreeCellRendererComponent(tree, value, isSelected,
//					expanded, leaf, row, hasFocus);
//			checkRenderer.setIcon(node.getIcon());
//			if (node.isGrayed()) {
//				check.setIcon(AbstractNode.GRAY_CHECK_ICON);
//			} else if (node.isSelected()) {
//				check.setIcon(AbstractNode.CHECKED_ICON);
//			} else
//				check.setIcon(AbstractNode.NO_CHECK_ICON);
//
//			node.getParent();
//
//			return this;
//
//		}

	}

	public void setBackground(Color color) {
		if (color instanceof ColorUIResource)
			color = null;
		super.setBackground(color);
	}

}
