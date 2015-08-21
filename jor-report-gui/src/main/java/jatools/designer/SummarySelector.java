package jatools.designer;

import jatools.data.reader.DatasetReader;
import jatools.dataset.Column;
import jatools.designer.wizard.BuilderContext;
import jatools.designer.wizard.CustomSummary;
import jatools.swingx.CustomTable;
import jatools.swingx.MessageBox;
import jatools.swingx.SimpleTreeNode;
import jatools.swingx.SwingUtil;
import jatools.swingx.TitledSeparator;
import jatools.util.Util;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
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
public class SummarySelector extends JPanel implements ChangeListener, ListSelectionListener {
    public static final String GROUP_FOR_ALL = App.messages.getString("res.195");
    JButton selectCommand;
    JButton unselectCommand;
    JButton unselectAllCommand;
    JButton downCommand;
    JButton upCommand;
    Map functionCache = new HashMap();
    JComboBox groupByChooser;
    CustomTable table;
    DataTree sourceTree;
    Object lastGroupBy = GROUP_FOR_ALL;
    Column hitField;
    DatasetReader reader;

    /**
     * Creates a new SummarySelector object.
     */
    public SummarySelector() {
        super(new GridBagLayout());

        JPanel sourcePanel = getSourceTreePanel();
        JPanel commandPanel = getCommandPanel();
        JPanel targetPanel = getTargetPanel();

        SwingUtil.setSize(sourcePanel, new Dimension(200, 200));
        SwingUtil.setSize(commandPanel, new Dimension(60, 200));
        SwingUtil.setSize(targetPanel, new Dimension(250, 200));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.WEST;
        add(new TitledSeparator(App.messages.getString("res.196")), gbc);

        groupByChooser = new JComboBox();
        groupByChooser.addItemListener(new ItemListener() {
                public void itemStateChanged(ItemEvent e) {
                    changeGroupBy();
                }
            });

        groupByChooser.setPreferredSize(new Dimension(200, 23));

        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEADING));
        p.add(new JLabel(App.messages.getString("res.197")));
        p.add(groupByChooser);

        add(p, gbc);
        gbc.insets.top = 20;
        add(new TitledSeparator(App.messages.getString("res.198")), gbc);
        gbc.insets.top = 0;
        gbc.gridwidth = 1;

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

        setGroupBys(new Object[0]);
    }

    private JPanel getTargetPanel() {
        table = new CustomTable(new String[] { App.messages.getString("res.108"), App.messages.getString("res.199") });

        table.getSelectionModel().addListSelectionListener(this);

        table.getModel().addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent e) {
                    unselectCommand.setEnabled(table.getRowCount() > 0);
                    unselectAllCommand.setEnabled(unselectCommand.isEnabled());
                }
            });

        JComboBox sortTypeChooser = new JComboBox(new _FunctionModel(
                    CustomSummary.SUPPORT_FUNCTIONS));
        table.getColumn(App.messages.getString("res.199")).setCellEditor(new DefaultCellEditor(sortTypeChooser));

        table.setEditable(1, true);

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
        unselectCommand = new JButton("<");
        unselectAllCommand = new JButton("<<");

        unselectAllCommand.setMargin(new Insets(0, 0, 0, 0));
        unselectCommand.setEnabled(false);
        unselectAllCommand.setEnabled(false);

        SwingUtil.setSize(selectCommand, new Dimension(50, 27));
        SwingUtil.setSize(unselectCommand, selectCommand.getPreferredSize());
        SwingUtil.setSize(unselectAllCommand, selectCommand.getPreferredSize());

        selectCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    select();
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

    protected void select() {
        if (hitField != null) {
            Class cls = hitField.getColumnClass();
            String fun = null;

            if ((cls != null) && (Number.class.isAssignableFrom(cls))) {
                fun = CustomSummary.SUPPORT_FUNCTIONS[1];
            } else {
                fun = CustomSummary.SUPPORT_FUNCTIONS[0];
            }

            table.addRow(new Object[] { hitField, fun }, true);
            treeNextRow();
        }
    }

    private JPanel getSourceTreePanel() {
        sourceTree = new DataTree(null);
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

    protected void changeGroupBy() {
        if (lastGroupBy != null) {
            functionCache.put(lastGroupBy, table.cloneRows());
        }

        table.removeAllRows();

        lastGroupBy = groupByChooser.getSelectedItem();

        if (lastGroupBy != null) {
            ArrayList rowsData = (ArrayList) functionCache.get(lastGroupBy);

            if (rowsData != null) {
                table.loadRows(rowsData);
            }
        }

        groupByChooser.getSelectedItem();
    }

    /**
     * DOCUMENT ME!
     *
     * @param groupBys DOCUMENT ME!
     */
    public void setGroupBys(Object[] groupBys) {
        boolean equal = false;

        if (groupBys.length == (groupByChooser.getItemCount() - 1)) {
            equal = true;

            for (int i = 0; i < groupBys.length; i++) {
                if (!groupByChooser.getItemAt(i + 1).equals(groupBys[i])) {
                    equal = false;

                    break;
                }
            }
        }

        if (!equal) {
            functionCache.clear();
            groupByChooser.removeAllItems();
            groupByChooser.addItem(GROUP_FOR_ALL);

            for (int i = 0; i < groupBys.length; i++) {
                groupByChooser.addItem(groupBys[i]);
            }
        }
    }

    void treeNextRow() {
        int index = sourceTree.getSelectionRows()[0];

        if (index < (sourceTree.getRowCount() - 1)) {
            sourceTree.setSelectionRow(sourceTree.getSelectionRows()[0] + 1);
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
                boolean hitReader = ((SimpleTreeNode) path.getLastPathComponent()).getUserObject() instanceof Column;
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
     * @param context DOCUMENT ME!
     */
    public void apply(BuilderContext context) {
        if (lastGroupBy != null) {
            functionCache.put(lastGroupBy, table.cloneRows());
        }

        ArrayList fields = new ArrayList();

        Set keys = functionCache.keySet();

        for (Iterator iter = keys.iterator(); iter.hasNext();) {
            Object element = (Object) iter.next();

            Column groupField = (element == GROUP_FOR_ALL) ? null : ((Column) element);

            ArrayList rows = (ArrayList) functionCache.get(element);

            for (Iterator iterator = rows.iterator(); iterator.hasNext();) {
                ArrayList v = (ArrayList) iterator.next();

                Column calcField = (Column) v.get(0);
                String calcType = (String) v.get(1);

                if (calcType.equals(App.messages.getString("res.200"))) {
                    calcType = App.messages.getString("res.201");
                }

                CustomSummary summary = new CustomSummary(null,
                        (groupField == null) ? null : groupField.getName(), calcField.getName(),
                        calcType, null);
                fields.add(summary);
            }
        }

        context.setValue(BuilderContext.SUMMARY_ITEMS,
            (CustomSummary[]) fields.toArray(new CustomSummary[0]));
    }

    /**
	 * DOCUMENT ME!
	 *
	 * @param context DOCUMENT ME!
	 */
	public void apply2(BuilderContext context) {
	    if (lastGroupBy != null) {
	        functionCache.put(lastGroupBy, table.cloneRows());
	    }
	
	    Map haspMap = new HashMap();
	
	    Set keys = functionCache.keySet();
	
	    for (Iterator iter = keys.iterator(); iter.hasNext();) {
	        Object element = (Object) iter.next();
	        Column groupField = (element == GROUP_FOR_ALL) ? null : ((Column) element);
	        ArrayList rows = (ArrayList) functionCache.get(element);
	        ArrayList summaryVector = new ArrayList();
	
	        for (Iterator iterator = rows.iterator(); iterator.hasNext();) {
	            Vector v = (Vector) iterator.next();
	            Column calcField = (Column) v.get(0);
	            String calcType = (String) v.get(1);
	
	            if (calcType.equals(App.messages.getString("res.200"))) {
	                calcType = App.messages.getString("res.201");
	            }
	
	            CustomSummary summary = new CustomSummary(null,
	                    (groupField == null) ? null : groupField.getName(), calcField.getName(),
	                    calcType, null);
	            summaryVector.add(summary);
	        }
	
	        haspMap.put((groupField == null) ? null : groupField.getName(), summaryVector);
	    }
	
	    context.setValue(BuilderContext.SUMMARY_ITEMS, haspMap);
	}

    class _FunctionModel extends DefaultComboBoxModel {
        public _FunctionModel(Object[] items) {
            super(items);
        }

        public void setSelectedItem(Object anObject) {
            Column hitField = (Column) table.getValueAt(table.getSelectedRow(), 0);

            Class cls = hitField.getColumnClass();

            boolean numeric = ((anObject == CustomSummary.SUM) ||
                (anObject == CustomSummary.AVERAGE));

            if (numeric && (cls != null) && (!(Number.class.isAssignableFrom(cls)))) {
                MessageBox.error(SummarySelector.this, App.messages.getString("res.202"));

                return;
            }

            super.setSelectedItem(anObject);
        }
    }
}
