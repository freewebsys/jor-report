package jatools.designer;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class JFileTree extends JTree {
    public static final boolean DIRECTORY_AND_FILE = true;
    public static final boolean DIRECTORY_NO_FILE = false;
    private static final String DESKTOP_EN = "Desktop";
    private static final String DESKTOP_ZH = App.messages.getString("res.129");
    private static final String DISK_EN = "Disk";
    private JFileTreeNode systemNode = null;
    private JFileTreeNode rootNode;
    private DefaultTreeModel jFileTreeModel;
    private boolean model;

    /**
     * Creates a new JFileTree object.
     *
     * @throws FileNotFoundException DOCUMENT ME!
     */
    public JFileTree() throws FileNotFoundException {
        this(DIRECTORY_AND_FILE);
    }

    /**
     * Creates a new JFileTree object.
     *
     * @param model DOCUMENT ME!
     */
    public JFileTree(boolean model) {
        this(null, model);
    }

    /**
     * Creates a new JFileTree object.
     *
     * @param file DOCUMENT ME!
     */
    public JFileTree(File file) {
        this(file, DIRECTORY_AND_FILE);
    }

    /**
     * Creates a new JFileTree object.
     *
     * @param file DOCUMENT ME!
     * @param model DOCUMENT ME!
     */
    public JFileTree(File file, boolean model) {
        this.model = model;
        putClientProperty("JTree.lineStyle", "Angled");

        if ((file == null) || !file.exists()) {
            file = new File(System.getProperty("user.home") + File.separator + DESKTOP_EN);

            if (!file.exists()) {
                file = new File(System.getProperty("user.home") + File.separator + DESKTOP_ZH);
            }

            rootNode = systemNode = new JFileTreeNode(file);
        } else {
            rootNode = new JFileTreeNode(file);
        }

        rootNode.expand();
        jFileTreeModel = new DefaultTreeModel(rootNode);
        setModel(jFileTreeModel);
        addTreeExpansionListener(new JTreeExpansionListener());
    }

    /**
     * DOCUMENT ME!
     *
     * @param path DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPathName(TreePath path) {
        Object o = path.getLastPathComponent();

        if (o instanceof JFileTreeNode) {
            return ((JFileTreeNode) o).file.getAbsolutePath();
        }

        return null;
    }

    protected class JFileTreeNode extends DefaultMutableTreeNode {
        protected File file;
        protected boolean isDirectory;

        public JFileTreeNode(File file) {
            this.file = file;
            isDirectory = file.isDirectory();
            setUserObject(file);
        }

        public boolean isLeaf() {
            if (file == null) {
                return false;
            }

            return !isDirectory;
        }

        public String toString() {
            if (file.getParentFile() == null) {
                return DISK_EN + "(" + file.getPath().substring(0, 2) + ")";
            } else {
                return file.getName();
            }
        }

        public boolean getAllowsChildren() {
            return isDirectory;
        }

        public boolean expand() {
            this.removeAllChildren();

            if (this.equals(systemNode)) {
                File[] roots = File.listRoots();

                for (int i = 0; i < roots.length; i++) {
                    if (roots[i].exists()) {
                        this.add(new JFileTreeNode(roots[i]));
                    }
                }
            }

            File[] files = file.listFiles();

            if (files == null) {
                return false;
            }

            for (int i = 0; i < files.length; i++) {
                File f = files[i];

                if (f.isDirectory() || model) {
                    this.add(new JFileTreeNode(f));
                }
            }

            return true;
        }
    }

    protected class JTreeExpansionListener implements TreeExpansionListener {
        public void treeExpanded(TreeExpansionEvent e) {
            JFileTreeNode fileNode = (JFileTreeNode) e.getPath().getLastPathComponent();

            if (fileNode != null) {
                new FileNodeExpansion(fileNode).run();
            }
        }

        public void treeCollapsed(TreeExpansionEvent e) {
        }
    }

    protected class FileNodeExpansion {
        private JFileTreeNode node;

        public FileNodeExpansion(JFileTreeNode node) {
            this.node = node;
        }

        protected void run() {
            boolean done = node.expand();

            if (done) {
                jFileTreeModel.reload(node);
            }
        }
    }
}
