package jatools.designer.variable.popup;

import jatools.designer.App;

import jatools.designer.variable.SourceType;
import jatools.designer.variable.TreeNodeValue;
import jatools.designer.variable.action.ArraySourceAction;
import jatools.designer.variable.action.CrossSourceAction;
import jatools.designer.variable.action.DataSourceAction;
import jatools.designer.variable.action.GroupSourceAction;
import jatools.designer.variable.action.IndexSourceAction;
import jatools.designer.variable.action.RowSourceAction;

import jatools.dom.src.DatasetNodeSource;
import jatools.dom.src.GroupNodeSource;
import jatools.dom.src.NodeSource;
import jatools.dom.src.RowNodeSource;

import java.awt.Component;

import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class NodeSourceTreePopup extends JPopupMenu implements SourceType {
    private JMenu addDatasetMenu;
    private JMenuItem addJdbcItem;
    private JMenuItem addCsvItem;
    private JMenuItem dataPreviewItem;
    private JMenuItem delDatasetItem;
    private JMenuItem modifyDatasetItem;
    private JMenuItem addIndexItem;
    private JMenuItem delIndexItem;
    private JMenuItem modifyIndexItem;
    private JMenuItem defineColumnItem;
    private JMenuItem editIndexItem;
    private JMenuItem addCrossItem;
    private JMenuItem delCrossItem;
    private JMenuItem modifyCrossItem;
    private JMenuItem addRowItem;
    private JMenuItem delRowItem;
    private JMenuItem addGroupItem;
    private JMenuItem delGroupItem;
    private JMenuItem modifyGroupItem;
    private JMenuItem addArrayItem;
    private JMenuItem delArrayItem;
    private JMenuItem modifyArrayItem;
    private NodeSource nodeSource;
    private Component c;
    private JMenu addView;

    /**
     * Creates a new SourcePopup object.
     *
     * @param c DOCUMENT ME!
     * @param defaultMutableTreeNode DOCUMENT ME!
     * @param popupType DOCUMENT ME!
     */
    public NodeSourceTreePopup(Component c, DefaultMutableTreeNode defaultMutableTreeNode,
        int popupType) {
        this.c = c;

        TreeNodeValue nodeValue = (TreeNodeValue) defaultMutableTreeNode.getUserObject();
        this.nodeSource = nodeValue.getNodeSource();
        addDatasetMenu = new JMenu(App.messages.getString("res.261"));
        addJdbcItem = new JMenuItem(new DataSourceAction(App.messages.getString("res.262"), c,
                    defaultMutableTreeNode, DataSourceAction.ADD_JDBC));
        addCsvItem = new JMenuItem(new DataSourceAction("CSV数据集", c, defaultMutableTreeNode,
                    DataSourceAction.ADD_CSV));

        defineColumnItem = new JMenuItem(new DataSourceAction("自定义字段", c, defaultMutableTreeNode,
                    DataSourceAction.DEFINE_COLUMN));

        editIndexItem = new JMenuItem(new DataSourceAction("定义索引", c, defaultMutableTreeNode,
                    DataSourceAction.EDIT_INDEX));

        addDatasetMenu.add(addJdbcItem);
        addDatasetMenu.add(addCsvItem);

        delDatasetItem = new JMenuItem(new DataSourceAction(App.messages.getString("res.96"), c,
                    defaultMutableTreeNode, DataSourceAction.DELETE));

        modifyDatasetItem = new JMenuItem(new DataSourceAction(App.messages.getString("res.95"), c,
                    defaultMutableTreeNode, DataSourceAction.MODIFY));

        addView = new JMenu(App.messages.getString("res.263"));
        addIndexItem = new JMenuItem(new IndexSourceAction(App.messages.getString("res.264"), c,
                    defaultMutableTreeNode, IndexSourceAction.ADD));
        delIndexItem = new JMenuItem(new IndexSourceAction(App.messages.getString("res.96"), c,
                    defaultMutableTreeNode, IndexSourceAction.DELETE));
        modifyIndexItem = new JMenuItem(new IndexSourceAction("编辑", c, defaultMutableTreeNode,
                    IndexSourceAction.MODIFY));

        addCrossItem = new JMenuItem(new CrossSourceAction(App.messages.getString("res.265"), c,
                    defaultMutableTreeNode, CrossSourceAction.ADD));
        delCrossItem = new JMenuItem(new CrossSourceAction(App.messages.getString("res.96"), c,
                    defaultMutableTreeNode, CrossSourceAction.DELETE));
        modifyCrossItem = new JMenuItem(new CrossSourceAction(App.messages.getString("res.95"), c,
                    defaultMutableTreeNode, CrossSourceAction.MODIFY));

        addRowItem = new JMenuItem(new RowSourceAction(App.messages.getString("res.266"), c,
                    defaultMutableTreeNode, RowSourceAction.ADD_ROW));

        addRowItem.setEnabled(!hasRowChild());

        delRowItem = new JMenuItem(new RowSourceAction(App.messages.getString("res.96"), c,
                    defaultMutableTreeNode, RowSourceAction.DELETE_ROW));
        addArrayItem = new JMenuItem(new ArraySourceAction("添加java数组", c, defaultMutableTreeNode,
                    ArraySourceAction.ADD));
        delArrayItem = new JMenuItem(new ArraySourceAction("删除", c, defaultMutableTreeNode,
                    ArraySourceAction.DELETE));
        modifyArrayItem = new JMenuItem(new ArraySourceAction("编辑", c, defaultMutableTreeNode,
                    ArraySourceAction.MODIFY));
        addGroupItem = new JMenuItem(new GroupSourceAction(App.messages.getString("res.223"), c,
                    defaultMutableTreeNode, GroupSourceAction.ADD_GROUP));
        delGroupItem = new JMenuItem(new GroupSourceAction(App.messages.getString("res.96"), c,
                    defaultMutableTreeNode, GroupSourceAction.DELETE_GROUP));
        modifyGroupItem = new JMenuItem(new GroupSourceAction(App.messages.getString("res.95"), c,
                    defaultMutableTreeNode, GroupSourceAction.MODIFY_GROUP));

        dataPreviewItem = new JMenuItem(new DataSourceAction(App.messages.getString("res.187"), c,
                    defaultMutableTreeNode, DataSourceAction.PREVIEW));

        switch (popupType) {
        case SourceType.ROOT_NODE_SOURCE:
            configRootPopup();

            break;

        case SourceType.DATASET_NODE_SOURCE:
            configDatasetPopup();

            break;

        case SourceType.INDEX_DATASET_NODE_SOURCE:
            configIndexPopup();

            break;

        case SourceType.CROSS_DATASET_NODE_SOURCE:
            configCrossPopup();

            break;

        case SourceType.GROUP_NODE_SOURCE:
            configGroupPopup();

            break;

        case SourceType.ROW_NODE_SOURCE:
            configRowPopup();

            break;

        case SourceType.ARRAY_NODE_SOURCE:
            configArrayPopup();

            break;
        }
    }

    private void configRootPopup() {
        this.add(addDatasetMenu);
        this.add(addArrayItem);
    }

    private void configDatasetPopup() {
        this.add(dataPreviewItem);
        this.addSeparator();

        addView.add(addRowItem);
        addView.add(addIndexItem);
        addView.add(addCrossItem);
        addView.add(addGroupItem);
        this.add(addView);
        this.add(addDatasetMenu);
        this.add(addArrayItem);
        this.addSeparator();
        this.add(defineColumnItem);
        this.add(editIndexItem);

        this.addSeparator();
        this.add(modifyDatasetItem);
        this.add(delDatasetItem);
    }

    private void configIndexPopup() {
        addOrDelRow();

        this.addSeparator();

        this.add(modifyIndexItem);
        this.add(delIndexItem);

        addRowItem.setEnabled(!childrenContainGroupORRow());
        addGroupItem.setEnabled(!childrenContainGroupORRow());
    }

    private void configCrossPopup() {
        addOrDelRow();

        this.addSeparator();

        this.add(modifyCrossItem);
        this.add(delCrossItem);

        addRowItem.setEnabled(!childrenContainGroupORRow());
        addGroupItem.setEnabled(!childrenContainGroupORRow());
    }

    private void configGroupPopup() {
        addView.add(addRowItem);
        addView.add(addIndexItem);
        addView.add(addCrossItem);
        addView.add(addGroupItem);
        this.add(addView);
        this.add(addDatasetMenu);
        this.add(addArrayItem);
        this.addSeparator();
        this.add(modifyGroupItem);
        this.add(delGroupItem);
    }

    private void configRowPopup() {
        this.add(addDatasetMenu);
        this.add(addArrayItem);
        this.addSeparator();
        this.add(delRowItem);
    }

    private void configArrayPopup() {
        this.add(addDatasetMenu);

        this.add(addArrayItem);

        this.addSeparator();
        this.add(modifyArrayItem);
        this.add(delArrayItem);

        addRowItem.setEnabled(!childrenContainGroupORRow());
        addGroupItem.setEnabled(!childrenContainGroupORRow());
    }

    private void addOrDelRow() {
        ArrayList v = nodeSource.getChildren();
        NodeSource target = null;

        for (int i = 0; i < v.size(); i++) {
            NodeSource ns = (NodeSource) v.get(i);

            if (ns.getTagName().equals("Row")) {
                target = ns;

                break;
            }
        }

        if (target != null) {
            add(delRowItem);
        } else {
            add(addRowItem);
        }
    }

    private boolean hasRowChild() {
        ArrayList v = nodeSource.getChildren();

        for (int i = 0; i < v.size(); i++) {
            NodeSource ns = (NodeSource) v.get(i);

            if (ns.getTagName().equals("Row")) {
                return true;
            }
        }

        return false;
    }

    private boolean childrenContainGroupORRow() {
        ArrayList v = nodeSource.getChildren();

        for (int i = 0; i < v.size(); i++) {
            NodeSource ns = (NodeSource) v.get(i);

            if (ns instanceof RowNodeSource || ns instanceof GroupNodeSource) {
                return true;
            }
        }

        return false;
    }

    private DatasetNodeSource getDatasetSource(NodeSource nodeSource) {
        if (nodeSource instanceof DatasetNodeSource) {
            return (DatasetNodeSource) nodeSource;
        } else {
            return getDatasetSource(nodeSource.getParent());
        }
    }
}
