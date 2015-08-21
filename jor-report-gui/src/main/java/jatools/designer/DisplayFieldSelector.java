package jatools.designer;

import jatools.data.reader.DatasetReader;

import jatools.dataset.Column;

import jatools.designer.wizard.BuilderContext;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
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
public class DisplayFieldSelector extends JPanel implements ListDataListener, ChangeListener,
    ListSelectionListener, TableModelListener {
    public static final int FIELD = 1;
    JButton selectCommand;
    JButton unselectCommand;
    JButton unselectAllCommand;
    JButton downCommand;
    JButton upCommand;
    CustomTable table;
    DataTree sourceTree;
    private String alias;
    Column hitField;
    DatasetReader reader;
    private JButton selectAllCommand;
    ArrayList listenerCache = new ArrayList();
    /**
     * Creates a new DisplayFieldSelector object.
     */
    public DisplayFieldSelector() {
        super(new GridBagLayout());

        JPanel sourcePanel = getSourceTreePanel();
        JPanel commandPanel = getCommandPanel();
        JPanel targetPanel = getTargetPanel();

        SwingUtil.setSize(sourcePanel, new Dimension(200, 200));
        SwingUtil.setSize(commandPanel, new Dimension(60, 200));
        SwingUtil.setSize(targetPanel, new Dimension(250, 200));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

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

    /**
     * DOCUMENT ME!
     */
    public void selectAll() {
        table.removeAllRows();

        SimpleTreeNode[] fields = sourceTree.getNodes(FIELD);

        if (fields.length > 0) {
            for (int i = 0; i < fields.length; i++) {
                if (!table.exists(0, fields[i].getUserObject())) {
                    table.addRow(new Object[] { fields[i].getUserObject(), null }, true);
                }
            }

            table.setSelectedRow(0);
        }
    }

    void treeNextRow() {
        int index = sourceTree.getSelectionRows()[0];

        if (index < (sourceTree.getRowCount() - 1)) {
            sourceTree.setSelectionRow(sourceTree.getSelectionRows()[0] + 1);
        }
    }
    
    void fireChangeListener() {
        ChangeEvent e = new ChangeEvent(this);

        for (Iterator iter = listenerCache.iterator(); iter.hasNext();) {
            ChangeListener element = (ChangeListener) iter.next();

            element.stateChanged(e);
        }
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
                    SimpleTreeNode stn = (SimpleTreeNode) sourceTree.getSelectionPath()
                                                                    .getLastPathComponent();

                    hitField = (Column) stn.getUserObject();

                    // alias = (String) stn.getProperty(SimpleTreeNode.ALIAS);
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void contentsChanged(ListDataEvent e) {
        enableButtons();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void intervalAdded(ListDataEvent e) {
        enableButtons();
    }

    /**
     * DOCUMENT ME!
     */
    public void enableButtons() {
        upCommand.setEnabled(table.canUp());
        downCommand.setEnabled(table.canDown());
        

    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void intervalRemoved(ListDataEvent e) {
        enableButtons();
    }

    /**
     * DOCUMENT ME!
     *
     * @param reader DOCUMENT ME!
     */
    public void setReader(DatasetReader reader) {
        if (this.reader != reader) {
            this.reader = reader;

            table.removeAllRows();

            if (reader == null) {
                sourceTree.setModel(new DefaultTreeModel(null));
            } else {
                sourceTree.setModel(new DefaultTreeModel(DataTreeUtil.asTree(reader)));
            }

            if (sourceTree.getRowCount() > 0) {
                sourceTree.expandRow(0);
                sourceTree.setSelectionRow(1);
            }
        }
    }

    private JPanel getTargetPanel() {
        table = new CustomTable(new String[] {
                    App.messages.getString("res.108"), App.messages.getString("res.109")
                });
        table.setEditable(1, true);

        downCommand = new JButton(Util.getIcon("/jatools/icons/download.gif"));
        upCommand = new JButton(Util.getIcon("/jatools/icons/up.gif"));

        upCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    table.upRow();
                }
            });

        upCommand.setFocusPainted(false);
        upCommand.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
        downCommand.setBorder(BorderFactory.createEmptyBorder(0, 1, 0, 1));
        downCommand.setFocusPainted(false);

        downCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    table.downRow();
                }
            });

        table.getSelectionModel().addListSelectionListener(this);

        table.getModel().addTableModelListener(this);

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
        selectAllCommand = new JButton(">>");
        selectCommand = new JButton(">");
        unselectCommand = new JButton("<");
        unselectAllCommand = new JButton("<<");

        selectAllCommand.setMargin(new Insets(0, 0, 0, 0));
        unselectAllCommand.setMargin(new Insets(0, 0, 0, 0));
        unselectCommand.setEnabled(false);
        unselectAllCommand.setEnabled(false);

        SwingUtil.setSize(selectAllCommand, new Dimension(50, 27));
        SwingUtil.setSize(selectCommand, selectAllCommand.getPreferredSize());
        SwingUtil.setSize(unselectCommand, selectAllCommand.getPreferredSize());
        SwingUtil.setSize(unselectAllCommand, selectAllCommand.getPreferredSize());

        selectAllCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    selectAll();
                }
            });

        selectCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (hitField != null) {
                        table.addRow(new Object[] { hitField, alias }, false);
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

        result.add(selectAllCommand, gbc);
        gbc.insets.top = 1;
        result.add(selectCommand, gbc);
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
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        showGUI();
    }

    /**
     * DOCUMENT ME!
     */
    public static void showGUI() {
        try {
            JFrame frame = new JFrame();
            DisplayFieldSelector inst = new DisplayFieldSelector();
            frame.setContentPane(inst);
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        selectAllCommand.setEnabled(table.getRowCount() < sourceTree.getUserObjects(FIELD).length);

        SimpleTreeNode node = (SimpleTreeNode) sourceTree.getModel().getRoot();
        disabledNodes(node);

        refreshSelectedNode();
        sourceTree.repaint();
        
        fireChangeListener();
    }
    
    public void addChangeListener(ChangeListener lst) {
        listenerCache.add(lst);
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void removeChangeListener(ChangeListener lst) {
        listenerCache.remove(lst);
    }
    

    void refreshSelectedNode() {
        int[] selectedRows = sourceTree.getSelectionRows();

        sourceTree.setSelectionRow(0);
        sourceTree.setSelectionRows(selectedRows);
    }

    void disabledNodes(SimpleTreeNode node) {
        Object userObject = node.getUserObject();
        boolean found = table.exists(0, userObject);

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
        String[] fields = new String[table.getRowCount()];

        Map aliasLooker = new HashMap();

        for (int i = 0; i < fields.length; i++) {
            String field = ((Column) table.getValueAt(i, 0)).getName();
            fields[i] = field;

            Object alias = table.getValueAt(i, 1);

            if ((alias != null) && !alias.toString().trim().equals("")) {
                aliasLooker.put(field, alias.toString());
            } else {
                aliasLooker.put(field, "");
            }
        }

        context.setValue(BuilderContext.DISPLAY_ITEMS, fields);
        context.setValue(BuilderContext.ALIAS_LOOKER, aliasLooker);
    }

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     * @param fieldKey DOCUMENT ME!
     * @param aliasLookerKey DOCUMENT ME!
     */
    public void apply(BuilderContext context, String fieldKey, String aliasLookerKey) {
        String[] fields = new String[table.getRowCount()];

        Map aliasLooker = new HashMap();

        for (int i = 0; i < fields.length; i++) {
            String field = ((Column) table.getValueAt(i, 0)).getName();
            fields[i] = field;

            Object alias = table.getValueAt(i, 1);

            if ((alias != null) && !alias.toString().trim().equals("")) {
                aliasLooker.put(field, alias.toString());
            } else {
                aliasLooker.put(field, "");
            }
        }

        context.setValue(fieldKey, fields);
        context.setValue(aliasLookerKey, aliasLooker);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSelected() {
        return this.table.getRowCount() > 0;
    }
}
