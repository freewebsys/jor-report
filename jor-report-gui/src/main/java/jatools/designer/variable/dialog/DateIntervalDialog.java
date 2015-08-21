package jatools.designer.variable.dialog;

import jatools.data.interval.date.DateIntervalColumn;
import jatools.dataset.Column;
import jatools.designer.Global;
import jatools.designer.JatoolsException;
import jatools.swingx.CommandPanel;
import jatools.swingx.GridBagConstraints2;
import jatools.swingx.MessageBox;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;

import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class DateIntervalDialog extends JDialog {
    /**
     * DOCUMENT ME!
     */

    final static String[] SUPPORT_FUNCTIONS = {
            "年",
            "半年",
            "季",
            "月",
            "周",
            "天",
            "半天",
            "小时",
            "分钟",
            "秒"
        };
    private JTextField nameText = new JTextField();
    private JComboBox fieldCombo;
    private JComboBox funcCombo;
    private DateIntervalColumn dateColumn;
    private boolean done;

    DateIntervalDialog(Frame owner, String title, Column[] columns) {
        super(owner, title, true);

        JPanel topPanel = new JPanel(new GridBagLayout());

        GridBagConstraints2 gbc = new GridBagConstraints2(topPanel);
        gbc.insets = Global.GBC_INSETS;

        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.WEST;

        topPanel.add(new JLabel("名称:"), gbc);

        fieldCombo = new JComboBox(columns);
        fieldCombo.setEditable(false);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        //     gbc.add( nameText,100);
        topPanel.add(nameText, gbc);
        gbc.gridwidth = 1;
        topPanel.add(new JLabel("源字段:"), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        topPanel.add(fieldCombo, gbc);

        // 计算方法
        funcCombo = new JComboBox(SUPPORT_FUNCTIONS);
        funcCombo.setEditable(false);

        gbc.weightx = 0.0;
        gbc.gridwidth = 1;
        topPanel.add(new JLabel("分组依据:"), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.add(funcCombo, 80);

        //    topPanel.add(funcCombo, gbc);
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
        setSize(new Dimension(340, 200));
        this.setLocationRelativeTo(owner);
    }

    protected void canel() {
        hide();
    }

    private void done() {
        try {
            dateColumn = this.createGroupColumn();
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

    DateIntervalColumn getDateGroupColumn() {
        return this.dateColumn;
    }

    void setDateGroupColumn(DateIntervalColumn g) {
        this.nameText.setText(g.getName());

        int i = this.getColumnIndex(g.getSrcField());

        if (i > -1) {
            this.fieldCombo.setSelectedIndex(i);
        }

        this.funcCombo.setSelectedIndex(g.getType());
    }

    DateIntervalColumn createGroupColumn() throws JatoolsException {
        String name = this.nameText.getText();

        if ((name == null) || (name.trim().length() == 0)) {
            throw new JatoolsException("名称不能为空.", this.nameText);
        }

        Column col = (Column) this.fieldCombo.getSelectedItem();

        if (col == null) {
            throw new JatoolsException("请选择源字段.", this.fieldCombo);
        } else if (!isDateColumn(col)) {
            throw new JatoolsException("源字段 [" + col.getName() + "] 不是日期型.", this.fieldCombo);
        }

        String srcField = col.getName();
        int type = this.funcCombo.getSelectedIndex();

        return new DateIntervalColumn(name, srcField, type);
    }

    private boolean isDateColumn(Column col) {
        return ((col.getColumnClass() == null) ||
        Date.class.isAssignableFrom(col.getColumnClass()));
    }

    int getColumnIndex(String colName) {
        int c = this.fieldCombo.getItemCount();

        for (int i = 0; i < c; i++) {
            Column col = (Column) this.fieldCombo.getItemAt(i);

            if (col.getName().equals(colName)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        System.out.println(Date.class.isAssignableFrom(java.sql.Date.class));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDone() {
        return done;
    }
}
