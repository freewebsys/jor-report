package jatools.designer;

import jatools.ReportDocument;
import jatools.engine.System2;
import jatools.util.Util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class DefaultReportsTreePanel extends JScrollPane {
    private static final String EXAMPLES_ROOT = System2.getProperty("examples.root");
    private JPopupMenu jpopupMenu;
    private JFileTree tree;

    /**
     * Creates a new DefaultReportsTreePanel object.
     */
    public DefaultReportsTreePanel() {
        this.setViewportView(this.getDefaultReportsTree());
    }

    private JTree getDefaultReportsTree() {
        JMenuItem jmenuItem = new JMenuItem(App.messages.getString("res.92"));
        jmenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String path = getSelectedPath();

                    if (path != null) {
                        openReport(path,true);
                    }
                }
            });
        jpopupMenu = new JPopupMenu();
        jpopupMenu.add(jmenuItem);

        tree = new JFileTree(new File(EXAMPLES_ROOT));
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) tree.getCellRenderer();
        renderer.setLeafIcon(Util.getIcon("/jatools/icons/tmp.gif"));
        renderer.setOpenIcon(Util.getIcon("/jatools/icons/fldopen.gif"));
        renderer.setClosedIcon(Util.getIcon("/jatools/icons/fldclose.gif"));

        tree.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    int mods = e.getModifiers();

                    String path = getSelectedPath();

                    if (path != null) {
                        if (e.getClickCount() == 2) {
                            openReport(path,(mods & InputEvent.SHIFT_MASK) == 0); // 如果按了 shift,则不要切换到property panel
                        } else if ((mods & InputEvent.BUTTON3_MASK) != 0) {
                            jpopupMenu.show(tree, e.getX(), e.getY());
                        }
                    }
                }
            });

        return tree;
    }

    private String getSelectedPath() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

        if ((node != null) && node.isLeaf()) {
            return node.getUserObject().toString();
        } else {
            return null;
        }
    }

    private void openReport(String path,boolean selectPropertyPanel) {
        try {
            ReportDocument doc = ReportDocument.load(new File(path));
            Main.getInstance()
                .createEditor(doc, ReportDocument.getCachedFile(doc).getName(),
                ReportDocument.getCachedFile(doc).getAbsolutePath(),selectPropertyPanel);
            App.getMruManager().open(path);
            
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
