package jatools.classtree;

import jatools.util.Util;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.7 $
  */
public class ImportScriptsPanel extends JPanel {
    static final Icon RUN_ICON = Util.getIcon("/jatools/icons/run.gif");
    private HashMap selectedMap = new HashMap();
    private ArrayList selectedMethods = new ArrayList();
    public JTree tree;
    private JPopupMenu popup;
    private Action findAction;
    
    JScrollPane scrollPane;
    /**
     * DOCUMENT ME!
     */
    public boolean done;

    /**
     * Creates a new ImportScriptsPanel object.
     */
    public ImportScriptsPanel() {
        this(null);
    }

    /**
     * Creates a new MethodTreePanel object.
     *
     * @param f DOCUMENT ME!
     * @param myscripts DOCUMENT ME!
     */
    public ImportScriptsPanel(ArrayList myscripts) {
        super(new BorderLayout());

        tree = createTree();
//        popup=new JPopupMenu();
//        findAction=new _findAction();
//        popup.add(findAction);       
//       MouseListener popListener=new PopupListener(popup);
        //lazy load
        MouseListener popListener=new PopupListener();
        tree.addMouseListener(popListener);
        scrollPane=new JScrollPane(tree);
        add(scrollPane);

        if ((myscripts != null) && !myscripts.isEmpty()) {
            setScripts(myscripts);
        }
    }

    private void setScripts(ArrayList myscripts) {
        for (int i = 0; i < myscripts.size(); i++) {
            String methods = (String) myscripts.get(i);
            String[] parts = methods.split(";");
            AbstractNode root = (AbstractNode) tree.getModel().getRoot();

            if (root != null) {
                AbstractNode jar = findChild(root, parts[0]);

                if (jar != null) {
                    AbstractNode cls = findChild(jar, parts[1]);

                    if (cls != null) {
                        AbstractNode method = findChild(cls, parts[2]);

                        if (method != null) {
                            tree.expandPath(new TreePath(cls.getPath()));
                        }
                    }
                }
            }
        }
    }

    private AbstractNode findChild(AbstractNode parent, String str) {
        if (!parent.isExplored()) {
            parent.explore();
        }

        for (int i = 0; i < parent.getChildCount(); i++) {
            AbstractNode node = (AbstractNode) parent.getChildAt(i);

            if (node.toString().equals(str)) {
                return node;
            }
        }

        return null;
    }
public void findClassNode(String classNode)
{
	String[] fullName=classNode.split(";");
	String className=fullName[0];
	String jarName=fullName[1];
	//System.out.println("className: "+className+"  jarName: "+jarName);
	DefaultMutableTreeNode root = (DefaultMutableTreeNode) this.tree.getModel().getRoot();
	//System.out.println("root.getChildCount()"+root.getChildCount());
	
	
	for(int i=0;i<root.getChildCount();i++)
	{
		JarFileNode jarNode=(JarFileNode) root.getChildAt(i);
		if(jarNode.toString().equals(jarName))
		{
		//data loaded dynamic
			jarNode.explore();
			for(int j=0;i<jarNode.getChildCount();j++)
			{
				ClassNode cln=(ClassNode) jarNode.getChildAt(j);
				
				if(className.equals(cln.getUserObject()))
				{
					TreePath p=new TreePath(cln.getPath());
					this.tree.setSelectionPath(p);					
					this.tree.expandPath(p);
					showScrollBarToRightPosition(p);
					this.tree.requestFocus();					
					break;
				}
				
			}
			
		}
	}
	
	
}
  public JPopupMenu getPopupMenu()
  {
	  if(popup==null)
	  {
		    popup=new JPopupMenu();
	        findAction=new _findAction();
	        popup.add(findAction); 
	  }
	return popup;
	
  }

private void  showScrollBarToRightPosition(TreePath path){
final Rectangle rec = tree.getPathBounds(path);
   scrollPane.setAutoscrolls(true);
	Runnable doScroll = new Runnable() {
		public void run() {
			scrollPane.getVerticalScrollBar().setValue(rec.y-60);
		}
	};
	javax.swing.SwingUtilities.invokeLater(doScroll);
	doScroll = null;
	scrollPane.getVerticalScrollBar().repaint();
}

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    JTree createTree() {
        JTree _tree = new JTree(createTreeModel("lib"));
        _tree.setToggleClickCount(0);

        _tree.setRowHeight(17);

        _tree.setCellRenderer(new NodeRenderer());
        _tree.putClientProperty("JTree.lineStyle", "Angled");

        _tree.addMouseListener(new NodeSelectionListener(_tree));
        _tree.addTreeExpansionListener(new TreeExpansionListener() {
                public void treeCollapsed(TreeExpansionEvent event) {
                    // TODO 自动生成方法存根
                }

                public void treeExpanded(TreeExpansionEvent event) {
                    TreePath path = event.getPath();
                    AbstractNode node = (AbstractNode) path.getLastPathComponent();

                    if (!node.isExplored()) {
                        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                        node.explore();
                        model.nodeStructureChanged(node);
                    }
                }
            });

        return _tree;
    }

    private DefaultTreeModel createTreeModel(String name) {
        LibFileNode rootNode = new LibFileNode(name);
        rootNode.explore();

        return new DefaultTreeModel(rootNode);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public HashMap getNodesMap() {
        addNodes((AbstractNode) tree.getModel().getRoot());

        return selectedMap;
    }

    private void addNodes(AbstractNode root) {
        if (root.getChildCount() >= 0) {
            for (Enumeration e = root.children(); e.hasMoreElements();) {
                AbstractNode n = (AbstractNode) e.nextElement();
                addNodes(n);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
    }

    class NodeSelectionListener extends MouseAdapter {
        JTree tree1;

        NodeSelectionListener(JTree tree) {
            this.tree1 = tree;
        }

        public void mouseClicked(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();
            int row = tree1.getRowForLocation(x, y);
            TreePath path = tree1.getPathForRow(row);

            // TreePath path = tree.getSelectionPath();
            if (path != null) {
                AbstractNode node = (AbstractNode) path.getLastPathComponent();

                node.explore();

                tree1.expandPath(path);

                ((DefaultTreeModel) tree1.getModel()).nodeChanged(node);

                // I need revalidate if node is root. but why?
                if (row == 0) {
                    tree1.revalidate();
                    tree1.repaint();
                }

                /*
                 * HashMap m = getNodesMap(); GJApp.launch(new
                 * MethodTreePanel(m), "JTree Test Explorer", 300, 300, 450,
                 * 400);
                 */

                /*
                 * ArrayList v = getSelectedMethods(); for (Iterator iter =
                 * v.iterator(); iter.hasNext();) {
                 * System.out.println(iter.next()); }
                 */
            }
        }
    }
    
    ///////////0130
    class PopupListener extends MouseAdapter {
		//        JPopupMenu popupMenu;
		//        PopupListener(JPopupMenu popupMenu) {
		//            this.popupMenu = popupMenu;
		//
		//        }
		PopupListener() {
		}

		public void mousePressed(MouseEvent e) {
			int row = tree.getRowForLocation(e.getX(), e.getY());
			if (row < 0) {
				return;
			}
			tree.setSelectionRow(row);
		}

		public void mouseReleased(MouseEvent e) {
			showPopupMenu(e);
		}

		private void showPopupMenu(MouseEvent e) {
			if (tree.getPathForLocation(e.getX(), e.getY()) == null) {
				return;
			}

			if (e.isPopupTrigger()) {//如果当前事件与鼠标事件相关，则弹出菜单
				// popupMenu.show(e.getComponent(),e.getX(), e.getY());
				getPopupMenu().show(e.getComponent(), e.getX(), e.getY());
			}
		}
	}

	class _findAction extends AbstractAction {
		_findAction() {
			super("查找类名");
		}

		public void actionPerformed(ActionEvent e) {
			showfindClass();
		}

		public void showfindClass() {
			FindClassDialog fc = new FindClassDialog(
					((javax.swing.JDialog) (javax.swing.SwingUtilities
							.getRoot(ImportScriptsPanel.this))), "查找类型");
			fc.setLocationRelativeTo(((JDialog) (SwingUtilities.getRoot(ImportScriptsPanel.this))));
			fc.pack();
			fc.setVisible(true);
			if (fc.isDone()) {
				if (fc.getClassName() != null) {
//					System.out.println("fc.getClassName(): "
//							+ fc.getClassName());
					findClassNode(fc.getClassName());
				}
			}
		}
	}
}
