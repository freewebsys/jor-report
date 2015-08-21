package jatools.designer.variable.action;

import jatools.data.reader.AbstractDatasetReader;
import jatools.data.reader.DatasetReader;
import jatools.data.reader.udc.UserDefinedColumn;

import jatools.dataset.Column;
import jatools.dataset.DatasetException;
import jatools.dataset.UserColumn;

import jatools.designer.Main;

import jatools.designer.data.DatasetPreviewer;
import jatools.designer.data.DatasetReaderFactory;
import jatools.designer.data.SimpleDatasetReaderFactory;

import jatools.designer.variable.DatasetTreeNodeValue;
import jatools.designer.variable.TreeNodeValue;
import jatools.designer.variable.NodeSourceTree;
import jatools.designer.variable.dialog.IndexEditor;
import jatools.designer.variable.dialog.UserDefinedColumnDialog;

import jatools.dom.src.DatasetNodeSource;
import jatools.dom.src.GroupNodeSource;
import jatools.dom.src.NodeSource;

import jatools.engine.script.ReportContext;

import jatools.swingx.MessageBox;

import java.awt.Component;
import java.awt.event.ActionEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import javax.swing.AbstractAction;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.18 $
 */
public class DataSourceAction extends AbstractAction {
    public static final int ADD_JDBC = 0;
    public static final int ADD_CSV = 1;
    public static final int DEFINE_COLUMN = 4;
    public static final int EDIT_INDEX = 12;
    public static final int DELETE = 7;
    public static final int MODIFY = 8;
    public static final int PREVIEW = 10;
    public static final int UPDATE_OPTIONS = 11;
    private int type;
    private Component c;
    private NodeSource nodeSource;
    private DefaultMutableTreeNode defaultMutableTreeNode;

    /**
     * Creates a new DataSourceAction object.
     *
     * @param name DOCUMENT ME!
     * @param c DOCUMENT ME!
     * @param defaultMutableTreeNode DOCUMENT ME!
     * @param type DOCUMENT ME!
     */
    public DataSourceAction(String name, Component c,
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
        case ADD_JDBC:
        case ADD_CSV:
            add(type);

            break;

        case DELETE:
            deleteAction();

            break;

        case MODIFY:
            modifyAction();

            break;

        case PREVIEW:
            preview();

            break;

        case DEFINE_COLUMN:
            defineColumnAction((DatasetNodeSource) this.nodeSource, this.defaultMutableTreeNode,
                this.c);

            break;

        case EDIT_INDEX:
            editIndex((DatasetNodeSource) this.nodeSource, this.defaultMutableTreeNode, this.c);

            break;
        }
    }

    private void editIndex(DatasetNodeSource nodeSource2,
        DefaultMutableTreeNode defaultMutableTreeNode2, Component c2) {
        IndexEditor editor = new IndexEditor(nodeSource2);
        editor.setVisible(true);

        if (c instanceof NodeSourceTree && editor.isSuccess()) {
            invalidate();
            updateTree(defaultMutableTreeNode, MODIFY);
        }
    }

    void defineColumnAction(DatasetNodeSource nodeSource,
        DefaultMutableTreeNode defaultMutableTreeNode, Component c) {
        DatasetNodeSource dns = (DatasetNodeSource) nodeSource;
        AbstractDatasetReader reader = (AbstractDatasetReader) dns.getReader();

        ArrayList cols = reader.getUserDefinedColumns();
        UserDefinedColumn[] userCols = null;

        if ((cols != null) && !cols.isEmpty()) {
            userCols = (UserDefinedColumn[]) cols.toArray(new UserDefinedColumn[0]);
        } else {
            userCols = new UserDefinedColumn[0];
        }

        ArrayList srcCols = null;

        try {
            srcCols = new ArrayList(Arrays.asList(reader.read(ReportContext.getDefaultContext(), 0)
                                                        .getRowInfo().getColumns()));
        } catch (DatasetException e) {
            srcCols = new ArrayList();
            e.printStackTrace();
        }

        Iterator it = srcCols.iterator();

        while (it.hasNext()) {
            if (it.next() instanceof UserColumn) {
                it.remove();
            }
        }

        Column[] sysCols = (Column[]) srcCols.toArray(new Column[0]);

        UserDefinedColumnDialog d = new UserDefinedColumnDialog(Main.getInstance(), sysCols,
                userCols);
        d.show();

        if (d.isDone()) {
            reader.setUserDefinedColumns(d.getUserColumns());

            if (c instanceof NodeSourceTree) {
                invalidate();
                updateTree(defaultMutableTreeNode, MODIFY);
            }
        }
    }

    void invalidate() {
        ((DatasetTreeNodeValue) this.defaultMutableTreeNode.getUserObject()).invalidate();
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     */
    public void add(int type) {
        DatasetReaderFactory proxyFactory = SimpleDatasetReaderFactory.getIntance(type);

        if (proxyFactory != null) {
            DatasetReader proxy = proxyFactory.create(Main.getInstance(), (NodeSourceTree) c);

            if (proxy != null) {
                String tagName = proxy.getName();
                DatasetNodeSource datasetNodeSource = new DatasetNodeSource(tagName, proxy);
                nodeSource.add(datasetNodeSource);
                updateTree(defaultMutableTreeNode, type);

                if (c instanceof NodeSourceTree) {
                    ((NodeSourceTree) c).selectNodeSource(datasetNodeSource);
                }
            }
        }
    }

    void preview() {
        try {
            DatasetPreviewer preview = new DatasetPreviewer(Main.getInstance());
            preview.setLocationRelativeTo(Main.getInstance());
            preview.setReader(((DatasetNodeSource) nodeSource).getReader());
            preview.show();
        } catch (Exception ex) {
            ex.printStackTrace();

            MessageBox.error(Main.getInstance(), ex.getMessage());
        }
    }

    private void deleteAction() {
        //                JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        //
        //        if (option == JOptionPane.OK_OPTION) {
        ArrayList brother = nodeSource.getParent().getChildren();
        nodeSource.getParent().remove(brother.indexOf(nodeSource));
        updateTree(defaultMutableTreeNode, DELETE);

        //        }
    }

    private void modifyAction() {
        //        if (containOwnGroup(nodeSource)) {

        //                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
        //
        //            if (option != JOptionPane.OK_OPTION) {
        //                return;
        //            }
        //        }
        DatasetNodeSource dns = (DatasetNodeSource) nodeSource;
        DatasetReader proxy = dns.getReader();
        DatasetReaderFactory proxyFactory = SimpleDatasetReaderFactory.getIntance(proxy.getType());

        if (proxyFactory.edit(proxy, Main.getInstance(), (NodeSourceTree) c)) {
            invalidate();
            dns.setTagName(proxy.getName());
            updateTree(defaultMutableTreeNode, MODIFY);
        }
    }

    private void updateTree(DefaultMutableTreeNode defaultMutableTreeNode, int type) {
        if (c instanceof NodeSourceTree) {
            NodeSourceTree tree = (NodeSourceTree) c;

            switch (type) {
            case ADD_JDBC:
            case ADD_CSV:
                tree.updateTreeAfterNodeAdded2(defaultMutableTreeNode);

                break;

            case MODIFY:
            case EDIT_INDEX:
                tree.updateTreeAfterNodeModified(defaultMutableTreeNode, nodeSource);

                break;

            case DELETE:
                tree.updateTreeAfterNodeDeleted(defaultMutableTreeNode);

                break;
            }
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
}
