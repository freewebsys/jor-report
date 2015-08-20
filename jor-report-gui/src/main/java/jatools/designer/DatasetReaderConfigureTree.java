package jatools.designer;

import jatools.data.reader.DatasetReader;

import jatools.designer.config.DatasetReaderList;

import jatools.designer.data.DatasetPreviewer;
import jatools.designer.data.DatasetReaderFactory;
import jatools.designer.data.NameChecker;
import jatools.designer.data.SimpleDatasetReaderFactory;

import jatools.swingx.SimpleTreeNode;

import jatools.util.Util;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;
import javax.swing.tree.DefaultTreeModel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class DatasetReaderConfigureTree extends DataTree implements NameChecker {
    private static final long serialVersionUID = 1L;
    private static final int NEW_JDBC = 0;
    private static final int EDIT = 4;
    private static final int DELETE = 5;
    private static final int PREVIEW = 6;
    final static Icon wizardIcon = Util.getIcon("/jatools/icons/newdataset.gif");
    private Action[] commands;
    DatasetReaderList proxyContainer = App.getConfiguration();
    private JPopupMenu newMenu;
    private JPopupMenu commandMenu;

    /**
     * Creates a new DatasetReaderConfigureTree object.
     */
    public DatasetReaderConfigureTree() {
        super(DataTreeUtil.asTree(App.getConfiguration()));

        MouseListener popListener = new PopupListener();
        this.addMouseListener(popListener);
        this.setShowsRootHandles(true);
      
        

        ToolTipManager.sharedInstance().registerComponent(this);
    }

    JPopupMenu getCommandMenu() {
        if (commandMenu == null) {
            commandMenu = new JPopupMenu();

            JMenu menu = new JMenu(App.messages.getString("res.93"));
            menu.add(this.getCommands()[NEW_JDBC]);

            commandMenu.add(menu);

            commandMenu.add(this.getCommands()[EDIT]);
            commandMenu.add(this.getCommands()[DELETE]);
            commandMenu.add(this.getCommands()[PREVIEW]);
        }

        return commandMenu;
    }

    Action[] getCommands() {
        if (this.commands == null) {
            this.commands = new Action[7];
            this.commands[NEW_JDBC] = new Command(App.messages.getString("res.94"), NEW_JDBC);

            this.commands[EDIT] = new Command(App.messages.getString("res.95"), EDIT);
            this.commands[DELETE] = new Command(App.messages.getString("res.96"), DELETE);
            this.commands[PREVIEW] = new Command(App.messages.getString("res.97"), PREVIEW);
        }

        return this.commands;
    }

    JPopupMenu getNewMenu() {
        if (newMenu == null) {
            newMenu = new JPopupMenu();
            newMenu.add(this.getCommands()[NEW_JDBC]);
        }

        return newMenu;
    }

    /**
     * DOCUMENT ME!
     *
     * @param root DOCUMENT ME!
     */
    public void setRoot(SimpleTreeNode root) {
        super.setRoot(root);

        addWizardNode(root);
    }

    /**
     * DOCUMENT ME!
     *
     * @param root DOCUMENT ME!
     */
    public void addWizardNode(SimpleTreeNode root) {
        SimpleTreeNode node = new SimpleTreeNode(App.messages.getString("res.98"), wizardIcon, 999);
        ((DefaultTreeModel) getModel()).insertNodeInto(node, root, root.getChildCount());
    }

    /**
     * DOCUMENT ME!
     *
     * @param source DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void check(String source) throws Exception {
        if ((source == null) || source.toString().trim().equals("")) {
            throw new Exception(App.messages.getString("res.99"));
        }

        String name = ((String) source).trim();

        int count = proxyContainer.getCount();

        for (int i = 0; i < count; i++) {
            if (name.equals(proxyContainer.getDatasetReader(i).getName())) {
                throw new Exception(App.messages.getString("res.100"));
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     */
    public void add(int type) {
        DatasetReaderFactory proxyFactory = SimpleDatasetReaderFactory.getIntance(type);

        if (proxyFactory != null) {
            DatasetReader proxy = proxyFactory.create(this, this);

            if (proxy != null) {
                proxyContainer.add(proxy);

                int row = this.getSelectionRows()[0];
                SimpleTreeNode newNode = DataTreeUtil.createSqlNode(proxy);
                SimpleTreeNode rootNode = (SimpleTreeNode) (SimpleTreeNode) this.getModel().getRoot();
                ((DefaultTreeModel) this.getModel()).insertNodeInto(newNode, rootNode, row);
                setSelectionRow(row);
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

    /**
     * DOCUMENT ME!
     */
    public void delete() {
        int result = JOptionPane.showConfirmDialog(null, App.messages.getString("res.101"),
                App.messages.getString("res.96"), JOptionPane.YES_NO_OPTION);

        if (result != JOptionPane.YES_OPTION) {
            return;
        }

        SimpleTreeNode selected = (SimpleTreeNode) this.getSelectionModel().getSelectionPath()
                                                       .getLastPathComponent();
        DefaultTreeModel model = (DefaultTreeModel) this.getModel();
        DatasetReader reader = (DatasetReader) selected.getUserObject();
        proxyContainer.remove(reader);
        model.removeNodeFromParent(selected);
    }

    /**
     * DOCUMENT ME!
     */
    public void edit() {
        SimpleTreeNode selected = (SimpleTreeNode) getSelectionPath().getLastPathComponent();

        if ((selected != null) && selected.getUserObject() instanceof DatasetReader) {
            DatasetReader reader = (DatasetReader) selected.getUserObject();

            DatasetReaderFactory proxyFactory = SimpleDatasetReaderFactory.getIntance(reader.getType());
            proxyFactory.edit(reader, this, this);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void preview() {
        SimpleTreeNode node = (SimpleTreeNode) this.getLastSelectedPathComponent();

        if (node.getUserObject() instanceof DatasetReader) {
            DatasetPreviewer previewer = new DatasetPreviewer(Main.getInstance());
            previewer.setReader((DatasetReader) node.getUserObject());
            previewer.show();
        }
    }

    class PopupListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            int row = DatasetReaderConfigureTree.this.getRowForLocation(e.getX(), e.getY());

            if (row < 0) {
                return;
            }

            DatasetReaderConfigureTree.this.setSelectionRow(row);

            SimpleTreeNode node = (SimpleTreeNode) DatasetReaderConfigureTree.this.getPathForLocation(e.getX(),
                    e.getY()).getLastPathComponent();

            if (node.getType() == 999) {
                Rectangle b = getPathBounds(getSelectionPath());
                getNewMenu().show(e.getComponent(), e.getX(), b.y + b.height);
            }
        }

        public void mouseReleased(MouseEvent e) {
            showPopupMenu(e);
        }

        private void showPopupMenu(MouseEvent e) {
            if (DatasetReaderConfigureTree.this.getPathForLocation(e.getX(), e.getY()) == null) {
                return;
            }

            SimpleTreeNode node = (SimpleTreeNode) DatasetReaderConfigureTree.this.getPathForLocation(e.getX(),
                    e.getY()).getLastPathComponent();

            boolean b = (node.getUserObject() instanceof DatasetReader);
            getCommands()[DELETE].setEnabled(b);
            getCommands()[EDIT].setEnabled(b);
            getCommands()[PREVIEW].setEnabled(b);

            if (e.isPopupTrigger() && (node.getType() != 999)) {
                Rectangle bo = getPathBounds(getSelectionPath());
                getCommandMenu().show(e.getComponent(), e.getX(), bo.y + bo.height);
            }
        }
    }

    private class Command extends AbstractAction {
        int id;

        Command(String prompt, int id) {
            super(prompt);
            this.id = id;
        }

        public void actionPerformed(ActionEvent e) {
            switch (id) {
            case NEW_JDBC:
                add(id);

                break;

            case EDIT:
                edit();

                break;

            case DELETE:
                delete();

                break;

            case PREVIEW:
                preview();

                break;
            }
        }
    }
}
