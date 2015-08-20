package jatools.designer.variable.dialog;

import jatools.data.interval.formula.FormulaIntervalColumn;
import jatools.data.interval.formula.IntervalFormula;
import jatools.designer.JatoolsException;
import jatools.designer.Main;
import jatools.swingx.CommandPanel;
import jatools.swingx.CustomTable;
import jatools.swingx.MessageBox;
import jatools.swingx.SortableTable;
import jatools.swingx.TitledSeparator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class FormulaIntevalColumnDialog extends JDialog implements ListSelectionListener {
    JTextField nameText = new JTextField();
    SortableTable sortTable;
    JButton editButton;
    JButton upButton;
    JButton downButton;
    JButton deleteButton;
    private boolean done;
    private FormulaIntervalColumn formulaInterval;

    /**
     * Creates a new UserDefinedColumnDialog object.
     *
     * @param sysCols DOCUMENT ME!
     * @param userCols DOCUMENT ME!
     */
    public FormulaIntevalColumnDialog(Frame f, String title) {
        super(f, title, true);

        JPanel topPanel = getCenterPanel();
        getContentPane().add(topPanel, BorderLayout.CENTER);

        //    topPanel.add(funcCombo, gbc);
        ActionListener lok = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    done();
                }
            };

        ActionListener cok = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cancel();
                }
            };

        CommandPanel cp = CommandPanel.createPanel(CommandPanel.OK, lok, CommandPanel.CANCEL, cok);
        getContentPane().add(cp, BorderLayout.SOUTH);
        //   SwingUtil.setBorder6((JComponent) getContentPane());
        this.setSize(new Dimension(350, 400));

        this.setLocationRelativeTo(f);
        valueChanged(null);
    }

    void setFormulaIntervalColumn(FormulaIntervalColumn c) {
    	this.nameText.setText( c.getName() );
        CustomTable table = (CustomTable) this.sortTable.getTable();
        ArrayList v = c.getFormulas();
        Iterator it = v.iterator();

        while (it.hasNext()) {
            IntervalFormula f = (IntervalFormula) it.next();

            table.addRow(new Object[] {
                    f.getExpression(),
                    f.getAs()
                }, false);
        }
        
        
    }

    protected void cancel() {
        hide();
    }

    protected void done() {
        try {
            this.formulaInterval = this.createFormulaIntervalColumn();
            done = true;
            hide();
        } catch (JatoolsException e) {
            MessageBox.error(this, e.getMessage());

            JComponent c = (JComponent) e.getSource();

            if (c != null) {
                c.requestFocus();
            }
        }
    }

    private JPanel getCenterPanel() {
        JPanel result = new JPanel(new BorderLayout());

        CustomTable table = new CustomTable(new String[] {
                    "区间条件",
                    "区间值"
                });

        table.getColumnModel().getColumn(0).setPreferredWidth(180);
        table.getColumnModel().getColumn(1).setPreferredWidth(80);
        result.add(this.sortTable = new SortableTable(table, 0), BorderLayout.CENTER);

        table.getSelectionModel().addListSelectionListener(this);

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets.top = 10;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JButton b = null;

        p.add(b = new JButton("新增"), gbc);
        b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    newIntervalFormula();
                }
            });

        gbc.insets.top = 0;
        p.add(b = editButton = new JButton("编辑"), gbc);
        b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    edit();
                }
            });
        p.add(b = deleteButton = new JButton("删除"), gbc);
        b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    sortTable.delete();
                }
            });

        gbc.insets.top = 10;
        p.add(b = upButton = new JButton("上移"), gbc);
        b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    sortTable.up();
                }
            });
        gbc.insets.top = 0;
        p.add(b = downButton = new JButton("下移"), gbc);
        b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    sortTable.down();
                }
            });

        gbc.weighty = 1;
        p.add(Box.createVerticalGlue(), gbc);
        gbc.weighty = 0;

        p.setPreferredSize(new Dimension(80, 0));
        result.add(p, BorderLayout.EAST);

        // result.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        JPanel re = new JPanel(new GridBagLayout());
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets.left = 5;
        gbc.insets.right = 5;
        gbc.insets.top = 6;
        re.add(new JLabel("名称:  "), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        re.add(nameText, gbc);
        gbc.weightx = 0.0;
        gbc.insets.top = 15;
        re.add(new TitledSeparator("区间公式:"), gbc);
        gbc.insets.left = 0;
        gbc.insets.right = 0;
        gbc.insets.top = 0;
        gbc.weighty = 1.0;
        gbc.insets.bottom = 5;
        re.add(result, gbc);

        return re;
    }

    protected void newIntervalFormula() {
        IntervalFormulaDialog d = new IntervalFormulaDialog(Main.getInstance(), "新建区间公式");
        d.show();

        if (d.getIntervalFormula() != null) {
            IntervalFormula f = d.getIntervalFormula();

            CustomTable table = (CustomTable) this.sortTable.getTable();
            table.addRow(new Object[] {
                    f.getExpression(),
                    f.getAs()
                }, true);
        }
    }

    protected void edit() {
        CustomTable table = (CustomTable) this.sortTable.getTable();
        int row = table.getSelectedRow();

        if (row != -1) {
            String exp = (String) table.getValueAt(row, 0);
            String as = (String) table.getValueAt(row, 1);

            IntervalFormulaDialog d = new IntervalFormulaDialog(Main.getInstance(), "编辑区间公式");
            d.setIntervalFormula(new IntervalFormula(exp, as));

            d.show();

            if (d.getIntervalFormula() != null) {
                IntervalFormula f = d.getIntervalFormula();
                table.setValueAt(f.getExpression(), row, 0);
                table.setValueAt(f.getAs(), row, 1);
            }
        }
    }

    /**
    * DOCUMENT ME!
    *
    * @param args DOCUMENT ME!
    */

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void valueChanged(ListSelectionEvent e) {
        upButton.setEnabled(sortTable.canUp());
        downButton.setEnabled(sortTable.canDown());
        deleteButton.setEnabled(sortTable.canDelete());
        editButton.setEnabled(deleteButton.isEnabled());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDone() {
        return done;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     * @throws JatoolsException
     */
    public FormulaIntervalColumn createFormulaIntervalColumn()
        throws JatoolsException {
        String name = this.nameText.getText();

        if ((name == null) || (name.trim().length() == 0)) {
            throw new JatoolsException("名称不能为空.", this.nameText);
        }

        CustomTable table = (CustomTable) this.sortTable.getTable();

        if (table.getRowCount() == 0) {
            throw new JatoolsException("公式不能为空.", table);
        } else {
            ArrayList formulas = new ArrayList();

            for (int i = 0; i < table.getRowCount(); i++) {
                String exp = (String) table.getValueAt(i, 0);
                String as = (String) table.getValueAt(i, 1);

                formulas.add(new IntervalFormula(exp, as));
            }

            return new FormulaIntervalColumn(name, formulas);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public FormulaIntervalColumn getFormulaIntervalColumn() {
        return this.formulaInterval;
    }
}
