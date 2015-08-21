package jatools.designer;


import jatools.data.reader.DatasetReader;
import jatools.data.sum.Sum;
import jatools.dataset.Column;
import jatools.designer.data.NameChecker;
import jatools.designer.wizard.CustomSummary;
import jatools.swingx.CommandPanel;
import jatools.swingx.SortableTable;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class SummaryFieldDialog extends JDialog {
    JTextField nameText = new JTextField();
    NameChecker checker;
    private JComboBox fieldCombo;
    private JComboBox funcCombo;
    private SortableTable groupFieldsTable;
    DatasetReader reader;
    private JCheckBox byParentChecker;

    /**
     * Creates a new SummaryFieldDialog object.
     *
     * @param owner DOCUMENT ME!
     * @param title DOCUMENT ME!
     * @param reader DOCUMENT ME!
     */
    public SummaryFieldDialog(Frame owner, String title, DatasetReader reader) {
        super(owner, title, true);
        this.reader = reader;

        JPanel topPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = Global.GBC_INSETS;

        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.WEST;

        topPanel.add(new JLabel(App.messages.getString("res.90")), gbc);

        Column[] columns = null;

        if (this.reader != null) {
            columns = ReaderUtil.getColumns(this.reader);
        } else {
            columns = new Column[0];
        }

        fieldCombo = new JComboBox(columns);
        fieldCombo.setEditable(false);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        topPanel.add(nameText, gbc);
        gbc.gridwidth = 1;
        topPanel.add(new JLabel(App.messages.getString("res.189")), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        topPanel.add(fieldCombo, gbc);

        funcCombo = new JComboBox(CustomSummary.SUPPORT_FUNCTIONS);
        funcCombo.setEditable(false);

        gbc.weightx = 0.0;
        gbc.gridwidth = 1;
        topPanel.add(new JLabel(App.messages.getString("res.190")), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        topPanel.add(funcCombo, gbc);

        this.groupFieldsTable = new SortableTable(this.createGroupFieldsTable(columns),
                SortableTable.ALL);

        topPanel.add(Box.createVerticalStrut(20), gbc);
        gbc.gridwidth = 1;
        gbc.weighty = 1.0;

        JLabel label = new JLabel(App.messages.getString("res.191"));
        label.setVerticalAlignment(JLabel.TOP);
        topPanel.add(label, gbc);

        gbc.gridwidth = GridBagConstraints.REMAINDER;

        topPanel.add(this.groupFieldsTable, gbc);
        this.groupFieldsTable.addNewActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addGroupField();
                }
            });
        gbc.weighty = 0.0;
        gbc.gridx = 1;
        this.byParentChecker = new JCheckBox(App.messages.getString("res.192"));
        this.byParentChecker.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setLocateByParent();
                }
            });
        topPanel.add(byParentChecker, gbc);
        gbc.gridx = GridBagConstraints.RELATIVE;

        topPanel.add(Box.createVerticalStrut(20), gbc);

        ActionListener lok = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    done();
                }
            };

        ActionListener cok = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    canel();
                }
            };

        CommandPanel cp = CommandPanel.createPanel(CommandPanel.OK, lok, CommandPanel.CANCEL, cok);
        getContentPane().add(topPanel, BorderLayout.CENTER);
        getContentPane().add(cp, BorderLayout.SOUTH);

        pack();
        setSize(new Dimension(340, 370));
        this.setLocationRelativeTo(owner);
    }

    protected void setLocateByParent() {
        JTable t = this.groupFieldsTable.getTable();

        if (this.byParentChecker.isSelected()) {
            for (int i = 0; i < t.getRowCount(); i++) {
                Object field = t.getValueAt(i, 0);

                if (field != null) {
                    t.setValueAt(this.reader.getName() + "." + field, i, 1);
                }
            }
        }

        t.repaint();
    }

    protected void addGroupField() {
        DefaultTableModel model = (DefaultTableModel) groupFieldsTable.getTable().getModel();
        model.addRow(new Object[2]);
        groupFieldsTable.setSelectedRow(model.getRowCount() - 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param calc DOCUMENT ME!
     * @param groupBy DOCUMENT ME!
     * @param fields DOCUMENT ME!
     * @param func DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Sum start(Sum calc, String[] groupBy, String[] fields, String[] func) {
        return null;
    }

    private JTable createGroupFieldsTable(Object[] columns) {
        JTable table = new JTable(new DefaultTableModel(new String[] { App.messages.getString("res.193"), App.messages.getString("res.194") }, 0) {
                    public boolean isCellEditable(int row, int column) {
                        return (column == 0) || !byParentChecker.isSelected();
                    }

                    public void setValueAt(Object aValue, int row, int column) {
                        super.setValueAt(aValue, row, column);

                        if ((column == 0) && (getValueAt(row, 1) == null)) {
                            setValueAt(reader.getName() + "." + aValue, row, 1);
                        }
                    }
                });

        table.getColumnModel().getColumn(0)
             .setCellEditor(new DefaultCellEditor(new JComboBox(columns)));

        table.getColumnModel().getColumn(1).setCellRenderer(new _DisableTableCellRenderer());

        return table;
    }

    private Sum createSummary() throws Exception {
        return null;
    }

    protected void done() {
    }

    protected void canel() {
        hide();
    }

    class _DisableTableCellRenderer extends DefaultTableCellRenderer {
        public static final String PROPERTY_VALUE = "value";

        public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
            boolean disabled = (byParentChecker.isSelected() && (column == 1));

            if (disabled) {
                isSelected = false;
            }

            JLabel l = (JLabel) super.getTableCellRendererComponent(table, value, isSelected,
                    hasFocus, row, column);

            l.setEnabled(!disabled);

            return l;
        }
    }
}
