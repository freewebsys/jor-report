package jatools.designer.variable.dialog;

import jatools.data.interval.date.DateIntervalColumn;
import jatools.data.interval.formula.FormulaIntervalColumn;
import jatools.data.reader.udc.UserDefinedColumn;
import jatools.dataset.Column;
import jatools.designer.Main;
import jatools.swingx.CustomTable;
import jatools.swingx.SortableTable;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class UserDefinedColumnDialog extends JDialog implements ListSelectionListener {
    SortableTable sortTable;
    JButton editButton;
    JButton upButton;
    JButton downButton;
    JButton deleteButton;
    private Column[] sysCols;
    private boolean done;
    JPopupMenu menu;

    /**
     * Creates a new UserDefinedColumnDialog object.
     *
     * @param sysCols DOCUMENT ME!
     * @param userCols DOCUMENT ME!
     */
    public UserDefinedColumnDialog(Frame f, Column[] sysCols, UserDefinedColumn[] userCols) {
        super(f, "自定义字段", true);

        JPanel topPanel = getCenterPanel();
        getContentPane().add(topPanel, BorderLayout.CENTER);
        this.setSize(new Dimension(300, 320));

        addUserColumns(userCols);
        this.sysCols = sysCols;
        this.setLocationRelativeTo(f);
        valueChanged(null);
    }

    private void addUserColumns(UserDefinedColumn[] userCols2) {
        CustomTable table = (CustomTable) this.sortTable.getTable();

        for (int i = 0; i < userCols2.length; i++) {
            table.addRow(new Object[] {
                    userCols2[i],
                    getColumnType(userCols2[i])
                }, false);
        }
    }

    private String getColumnType(UserDefinedColumn ufc) {
        if (ufc instanceof DateIntervalColumn) {
            return "日期";
        } else if (ufc instanceof FormulaIntervalColumn) {
            return "公式";
        } else {
            return null;
        }
    }

    protected void cancel() {
        hide();
    }

    protected void done() {
        done = true;
        hide();
    }

    private JPanel getCenterPanel() {
    	
    	
    	
        JPanel formulsPanel = new JPanel(new BorderLayout());

        CustomTable table = new CustomTable(new String[] {
                    "名称",
                    "类型"
                });

        table.getColumnModel().getColumn(0).setPreferredWidth(125);
        table.getColumnModel().getColumn(1).setPreferredWidth(70);
        formulsPanel.add(this.sortTable = new SortableTable(table, 0), BorderLayout.CENTER);

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
                    Component c = (Component) e.getSource();
                    getPopupMenu().show(c, 0, c.getHeight());
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
        p.add(b = new JButton("确定"), gbc);
        b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    done();
                }
            });

        gbc.insets.bottom = 20;
        p.add(b = new JButton("取消"), gbc);
        b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cancel();
                }
            });
        p.setPreferredSize(new Dimension(80, 0));
        formulsPanel.add(p, BorderLayout.EAST);
        formulsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

       
        return formulsPanel;
    }

    JPopupMenu getPopupMenu() {
        if (this.menu == null) {
            this.menu = new JPopupMenu();

            JMenuItem m = new JMenuItem("日期区间字段");
            m.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        newDateGroupColumn();
                    }
                });

            menu.add(m);

            m = new JMenuItem("公式区间字段");
            m.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        newIntervalColumn();
                    }
                });
            menu.add(m);
        }

        return this.menu;
    }

    protected void newIntervalColumn() {
        FormulaIntevalColumnDialog d = new FormulaIntevalColumnDialog(Main.getInstance(), "新建公式区间字段");
        d.show();

        if (d.getFormulaIntervalColumn() != null) {
            FormulaIntervalColumn col = d.getFormulaIntervalColumn();

            CustomTable table = (CustomTable) this.sortTable.getTable();

            table.addRow(new Object[] {
                    col,
                    getColumnType(col)
                }, true);
        }
    }

    protected void edit() {
        CustomTable table = (CustomTable) this.sortTable.getTable();
        int row = table.getSelectedRow();

        if (row != -1) {
            Object sel =  table.getValueAt(row, 0);

            if (sel instanceof DateIntervalColumn) {
                editDateColumn(table, row, (DateIntervalColumn) sel);
            } else {
            	editFormulaColumn(table,row,(FormulaIntervalColumn) sel);
            }
        }
    }

    private void editDateColumn(CustomTable table, int row, DateIntervalColumn date) {
        DateIntervalDialog d = new DateIntervalDialog(Main.getInstance(), "编辑日期区间字段", this.sysCols);
        d.setDateGroupColumn(date);

        d.show();

        if (d.getDateGroupColumn() != null) {
            DateIntervalColumn col = d.getDateGroupColumn();
            table.setValueAt(col, row, 0);
        }
    }
    
    private void editFormulaColumn(CustomTable table, int row, FormulaIntervalColumn formula) {
    	FormulaIntevalColumnDialog d = new FormulaIntevalColumnDialog(Main.getInstance(), "编辑公式区间字段");
        d.setFormulaIntervalColumn (formula);

        d.show();

        if (d.getFormulaIntervalColumn() != null) {
        	FormulaIntervalColumn col = d.getFormulaIntervalColumn();
            table.setValueAt(col, row, 0);
        }
    }

    protected void newDateGroupColumn() {
        DateIntervalDialog d = new DateIntervalDialog(Main.getInstance(), "新建日期区间字段", this.sysCols);
        d.show();

        if (d.getDateGroupColumn() != null) {
            DateIntervalColumn col = d.getDateGroupColumn();
            CustomTable table = (CustomTable) this.sortTable.getTable();

            table.addRow(new Object[] {
                    col,
                    getColumnType(col)
                }, true);
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
     */
    public ArrayList getUserColumns() {
        CustomTable table = (CustomTable) this.sortTable.getTable();

        if (table.getRowCount() == 0) {
            return null;
        } else {
            ArrayList result = new ArrayList();

            for (int i = 0; i < table.getRowCount(); i++) {
                result.add(table.getValueAt(i, 0));
            }

            return result;
        }
    }
}
