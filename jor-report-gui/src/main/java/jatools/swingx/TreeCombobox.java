package jatools.swingx;


import jatools.util.Util;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.AbstractListModel;
import javax.swing.ComboBoxModel;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTree;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;


public class TreeCombobox extends JComboBox {
    static final int OFFSET = 16;
    static Border emptyBorder = new EmptyBorder(0, 0, 0, 0);

    public TreeCombobox(TreeModel aTreeModel) {
        super();
        setModel(new TreeToListModel(aTreeModel));
        setRenderer(new ListEntryRenderer());
    }

    public static void main(String[] args) {
        JDialog d = new JDialog();

        d.getContentPane().add(new TreeCombobox(new JTree().getModel()));
        d.show();
    }

    class TreeToListModel extends AbstractListModel implements ComboBoxModel, TreeModelListener {
        TreeModel source;
        boolean invalid = true;
        Object currentValue;
        ArrayList cache = new ArrayList();

        public TreeToListModel(TreeModel aTreeModel) {
            source = aTreeModel;
            aTreeModel.addTreeModelListener(this);
            setRenderer(new ListEntryRenderer());
        }

        public void setSelectedItem(Object anObject) {
            currentValue = anObject;
            fireContentsChanged(this, -1, -1);
        }

        public Object getSelectedItem() {
            return currentValue;
        }

        public int getSize() {
            validate();

            return cache.size();
        }

        public Object getElementAt(int index) {
            return cache.get(index);
        }

        public void treeNodesChanged(TreeModelEvent e) {
            invalid = true;
        }

        public void treeNodesInserted(TreeModelEvent e) {
            invalid = true;
        }

        public void treeNodesRemoved(TreeModelEvent e) {
            invalid = true;
        }

        public void treeStructureChanged(TreeModelEvent e) {
            invalid = true;
        }

        void validate() {
            if (invalid) {
                cache = new ArrayList();
                cacheTree(source.getRoot(), 0);

                if (cache.size() > 0) {
                    currentValue = cache.get(0);
                }

                invalid = false;
                fireContentsChanged(this, 0, 0);
            }
        }

        void cacheTree(Object anObject, int level) {
            if (source.isLeaf(anObject)) {
                addListEntry(anObject, level, false);
            } else {
                int c = source.getChildCount(anObject);
                int i;
                Object child;

                addListEntry(anObject, level, true);
                level++;

                for (i = 0; i < c; i++) {
                    child = source.getChild(anObject, i);
                    cacheTree(child, level);
                }

                level--;
            }
        }

        void addListEntry(Object anObject, int level, boolean isNode) {
            cache.add(new ListEntry(anObject, level, isNode));
        }
    }

    class ListEntry {
        Object object;
        int level;
        boolean isNode;

        public ListEntry(Object anObject, int aLevel, boolean isNode) {
            object = anObject;
            level = aLevel;
            this.isNode = isNode;
        }

        public Object object() {
            return object;
        }

        public int level() {
            return level;
        }

        public boolean isNode() {
            return isNode;
        }
    }

    class ListEntryRenderer extends JLabel implements ListCellRenderer {
        Icon leafIcon = Util.getIcon("/jatools/icons/new.gif");
        Icon nodeIcon = leafIcon;

        public ListEntryRenderer() {
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList listbox, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {
            ListEntry listEntry = (ListEntry) value;

            if (listEntry != null) {
                Border border;
                setText(listEntry.object().toString());
                setIcon(listEntry.isNode() ? nodeIcon : leafIcon);

                if (index != -1) {
                    border = new EmptyBorder(0, OFFSET * listEntry.level(), 0, 0);
                } else {
                    border = emptyBorder;
                }

                if (UIManager.getLookAndFeel().getName().equals("CDE/Motif")) {
                    if (index == -1) {
                        setOpaque(false);
                    } else {
                        setOpaque(true);
                    }
                } else {
                    setOpaque(true);
                }

                setBorder(border);

                if (isSelected) {
                    setBackground(UIManager.getColor("ComboBox.selectionBackground"));
                    setForeground(UIManager.getColor("ComboBox.selectionForeground"));
                } else {
                    setBackground(UIManager.getColor("ComboBox.background"));
                    setForeground(UIManager.getColor("ComboBox.foreground"));
                }
            } else {
                setText("");
            }

            return this;
        }
    }
}
