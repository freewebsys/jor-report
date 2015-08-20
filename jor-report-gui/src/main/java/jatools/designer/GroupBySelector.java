package jatools.designer;

import jatools.data.reader.DatasetReader;
import jatools.dataset.Column;
import jatools.designer.wizard.BuilderContext;
import jatools.designer.wizard.util.CustomGroup;
import jatools.swingx.CustomTable;
import jatools.swingx.SimpleTreeNode;
import jatools.swingx.SwingUtil;
import jatools.util.Util;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class GroupBySelector extends JPanel implements ChangeListener, ListSelectionListener,
    TableModelListener {
    final static String ASC = App.messages.getString("res.121");
    final static String DESC = App.messages.getString("res.122");
    final static String ORIGINAL = App.messages.getString("res.123");
    JButton selectCommand;
    JButton unselectCommand;
    JButton unselectAllCommand;
    JButton downCommand;
    JButton upCommand;
    CustomTable table;
    DataTree sourceTree;
    Column hitField;
    DatasetReader reader;

    /**
     * Creates a new GroupBySelector object.
     */
    public GroupBySelector() {
        super(new GridBagLayout());

        JPanel sourcePanel = getSourceTreePanel();
        JPanel commandPanel = getCommandPanel();
        JPanel targetPanel = getTargetPanel();

        SwingUtil.setSize(sourcePanel, new Dimension(200, 200));
        SwingUtil.setSize(commandPanel, new Dimension(60, 200));
        SwingUtil.setSize(targetPanel, new Dimension(250, 200));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = gbc.BOTH;

        add(sourcePanel, gbc);
        add(commandPanel, gbc);

        add(targetPanel, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        add(Box.createHorizontalGlue(), gbc);
        gbc.weightx = 0;
        gbc.weighty = 1.0;
        add(Box.createVerticalGlue(), gbc);

        SwingUtil.setBorder6(this);
    }

    void treeNextRow() {
        int index = sourceTree.getSelectionRows()[0];

        if (index < (sourceTree.getRowCount() - 1)) {
            sourceTree.setSelectionRow(sourceTree.getSelectionRows()[0] + 1);
        } else if (index == (sourceTree.getRowCount() - 1)) {
            refreshSelectedNode();
        }
    }

    private JPanel getTargetPanel() {
        table = new CustomTable(new String[] { App.messages.getString("res.108"), App.messages.getString("res.124") });

        downCommand = new JButton(Util.getIcon("/jatools/icons/download.gif"));
        upCommand = new JButton(Util.getIcon("/jatools/icons/up.gif"));

        upCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    table.upRow();
                }
            });

        downCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    table.downRow();
                }
            });

        upCommand.setFocusPainted(false);
        upCommand.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
        downCommand.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
        downCommand.setFocusPainted(false);

        table.getSelectionModel().addListSelectionListener(this);

        table.getModel().addTableModelListener(this);

        JComboBox sortTypeChooser = new JComboBox(new String[] { ASC, DESC, ORIGINAL });
        table.getColumn(App.messages.getString("res.124")).setCellEditor(new DefaultCellEditor(sortTypeChooser));

        table.setEditable(1, true);

        JPanel result = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        result.add(new JLabel(App.messages.getString("res.110")), gbc);
        gbc.weightx = 0;
        result.add(upCommand, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        result.add(downCommand, gbc);

        gbc.weighty = 1.0;
        result.add(new JScrollPane(table), gbc);

        return result;
    }

    private JPanel getCommandPanel() {
        selectCommand = new JButton(">");
        unselectCommand = new JButton("<<");
        unselectAllCommand = new JButton("<");

        unselectAllCommand.setMargin(new Insets(0, 0, 0, 0));
        unselectCommand.setEnabled(false);
        unselectAllCommand.setEnabled(false);

        SwingUtil.setSize(selectCommand, new Dimension(50, 27));
        SwingUtil.setSize(unselectCommand, selectCommand.getPreferredSize());
        SwingUtil.setSize(unselectAllCommand, selectCommand.getPreferredSize());

        selectCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (hitField != null) {
                        table.addRow(new Object[] { hitField, ASC }, true);
                        treeNextRow();
                    }
                }
            });
        unselectCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int row = table.getSelectedRow();
                    table.removeRow(row, true);
                }
            });

        unselectAllCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    table.removeAllRows();
                }
            });

        JPanel result = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets.top = 23;

        result.add(selectCommand, gbc);
        gbc.insets.top = 1;
        result.add(unselectCommand, gbc);
        result.add(unselectAllCommand, gbc);

        gbc.weighty = 1.0;
        result.add(Box.createVerticalGlue(), gbc);

        return result;
    }

    private JPanel getSourceTreePanel() {
        sourceTree = new DataTree();
        sourceTree.addChangeListener(this);

        JPanel result = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        result.add(new JLabel(App.messages.getString("res.111")), gbc);
        gbc.weighty = 1.0;
        result.add(new JScrollPane(sourceTree), gbc);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ArrayList getSelectedRows() {
        return table.cloneRows();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == sourceTree) {
            TreePath path = sourceTree.getSelectionPath();

            if (path != null) {
                SimpleTreeNode node = (SimpleTreeNode) path.getLastPathComponent();

                boolean hitReader = node.getUserObject() instanceof Column && !node.isDisabled();
                selectCommand.setEnabled(hitReader);

                if (hitReader) {
                    hitField = (Column) ((SimpleTreeNode) sourceTree.getSelectionPath()
                                                                    .getLastPathComponent()).getUserObject();
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param reader DOCUMENT ME!
     */
    public void setReader(DatasetReader reader) {
        if (this.reader != reader) {
            table.removeAllRows();

            if (reader == null) {
                sourceTree.setModel(new DefaultTreeModel(null));
            } else {
                sourceTree.setModel(new DefaultTreeModel(DataTreeUtil.asTree(reader)));

                if (sourceTree.getRowCount() > 0) {
                    sourceTree.expandRow(0);
                    sourceTree.setSelectionRow(1);
                }
            }
        }

        this.reader = reader;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void valueChanged(ListSelectionEvent e) {
        upCommand.setEnabled(table.canUp());
        downCommand.setEnabled(table.canDown());
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void tableChanged(TableModelEvent e) {
        unselectCommand.setEnabled(table.getRowCount() > 0);
        unselectAllCommand.setEnabled(unselectCommand.isEnabled());

        SimpleTreeNode node = (SimpleTreeNode) sourceTree.getModel().getRoot();
        disabledNodes(node);

        refreshSelectedNode();
        sourceTree.repaint();
    }

    void refreshSelectedNode() {
        int[] selectedRows = sourceTree.getSelectionRows();

        sourceTree.setSelectionRow(0);
        sourceTree.setSelectionRows(selectedRows);
    }

    void disabledNodes(SimpleTreeNode node) {
        Object userObject = node.getUserObject();
        boolean found = false;

        for (int i = 0; i < table.getRowCount(); i++) {
            if (table.getValueAt(i, 0) == userObject) {
                found = true;

                break;
            }
        }

        node.setDisabled(found);

        for (int i = 0; i < node.getChildCount(); i++) {
            disabledNodes((SimpleTreeNode) node.getChildAt(i));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     */
    public void apply(BuilderContext context) {
        CustomGroup[] fields = new CustomGroup[table.getRowCount()];

        for (int i = 0; i < fields.length; i++) {
            int order = 0;

            if (table.getValueAt(i, 1) == ASC) {
                order = 0;
            } else if (table.getValueAt(i, 1) == DESC) {
                order = 1;
            } else {
                order = 2;
            }

            Column field = (Column) table.getValueAt(i, 0);
            fields[i] = new CustomGroup(field.getName(), order);
        }

        context.setValue(BuilderContext.GROUP_ITEMS, fields);
    }
}
