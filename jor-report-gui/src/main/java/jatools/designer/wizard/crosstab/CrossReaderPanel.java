package jatools.designer.wizard.crosstab;

import jatools.data.reader.DatasetReader;
import jatools.dataset.Column;
import jatools.designer.App;
import jatools.designer.DataTree;
import jatools.designer.DataTreeUtil;
import jatools.designer.wizard.BuilderContext;
import jatools.designer.wizard.CustomSummary;
import jatools.dom.Group;
import jatools.swingx.CustomTable;
import jatools.swingx.SimpleTreeNode;
import jatools.util.Util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.DefaultCellEditor;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;



/**
 * changed 2007-12-27
 * @author   java9
 */
public class CrossReaderPanel extends JPanel implements ListSelectionListener, ChangeListener {
    ArrayList listeners = new ArrayList();
    Dimension textSize = new Dimension(200, 103);
    Dimension labelSize = new Dimension(25, 25);
    Dimension buttonSize = new Dimension(20, 20);
    DataTree dataTree;
    CustomTable calcTable;
    CustomTable columnTable;
    CustomTable rowTable;
    JButton cdeleteButton;
    JButton rdeleteButton;
    JButton adeleteButton;
    JButton upColCommand;
    JButton downColCommand;
    JButton upRowCommand;
    JButton downRowCommand;
    JButton rowButton;
    JButton columnButton;
    JButton aggregationButton;
    private DatasetReader reader;
    private Column hitField;
    private JCheckBox chkFlowlayout;
    private JCheckBox chkTopSummary;
    private JCheckBox chkLeftSummary;

    /**
     * Creates a new ZCrossTabPanel object.
     *
     * @param colFields
     *            作为列表头的字段
     * @param rowFields
     *            作为行表头的字段
     * @param calcField
     *            被统计的字段
     * @param calcFunction
     *            计算函数 ,参考本类常量定义
     */
    public CrossReaderPanel() {
        
        JPanel west = new JPanel(new GridLayout(2, 1));
        JPanel leftTopPanel = new JPanel(new BorderLayout());
        leftTopPanel.setBorder(new EmptyBorder(20, 20, 20, 3));

        JLabel dataLabel = new JLabel(App.messages.getString("res.111"), JLabel.LEFT);
        leftTopPanel.add(dataLabel, BorderLayout.NORTH);
        dataTree = new DataTree(null);

        JScrollPane sp = new JScrollPane(dataTree);
        leftTopPanel.add(sp);
        west.add(leftTopPanel);

        
        JPanel leftBottomPanel = new JPanel(new BorderLayout());
        leftBottomPanel.setBorder(new EmptyBorder(20, 20, 0, 3));

        JPanel rowPanel = new JPanel(new BorderLayout());
        JLabel rowLabel = new JLabel(App.messages.getString("res.239"), JLabel.LEFT);
        rowPanel.add(rowLabel, BorderLayout.WEST);
        rdeleteButton = createReleteButton();

        JPanel commandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 2));

        rowPanel.add(commandPanel, BorderLayout.EAST);

        leftBottomPanel.add(rowPanel, BorderLayout.NORTH);

        rowTable = createRowTabel();
        leftBottomPanel.add(new JScrollPane(rowTable));
        west.add(leftBottomPanel);

        upRowCommand = new JButton(Util.getIcon("/jatools/icons/moveup.gif")); //
        downRowCommand = new JButton(Util.getIcon("/jatools/icons/movedown.gif")); //
        commandPanel.add(upRowCommand);
        commandPanel.add(downRowCommand);

        upRowCommand.setPreferredSize(rdeleteButton.getPreferredSize());
        downRowCommand.setPreferredSize(rdeleteButton.getPreferredSize());

        commandPanel.add(rdeleteButton);

        upRowCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    rowTable.upRow();
                }
            });
        downRowCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    rowTable.downRow();
                }
            });
        rowTable.getSelectionModel().addListSelectionListener(this);

        
        JPanel east = new JPanel(new GridLayout(2, 1));
        JPanel rightTopPanel = new JPanel(new BorderLayout());
        rightTopPanel.setBorder(new EmptyBorder(20, 3, 20, 20));

        
        JPanel columnPanel = new JPanel(new BorderLayout());
        JLabel columnLabel = new JLabel(App.messages.getString("res.240"), JLabel.LEFT);
        columnPanel.add(columnLabel, BorderLayout.WEST);
        cdeleteButton = createCdeleteButton();

        rightTopPanel.add(columnPanel, BorderLayout.NORTH);
        columnTable = createColumnTable();
        rightTopPanel.add(new JScrollPane(columnTable));
        east.add(rightTopPanel);

        commandPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 2));
        columnPanel.add(commandPanel, BorderLayout.EAST);

        upColCommand = new JButton(Util.getIcon("/jatools/icons/moveup.gif")); //
        downColCommand = new JButton(Util.getIcon("/jatools/icons/movedown.gif")); //
        upColCommand.setPreferredSize(rdeleteButton.getPreferredSize());
        downColCommand.setPreferredSize(rdeleteButton.getPreferredSize());
        commandPanel.add(upColCommand);
        commandPanel.add(downColCommand);
        commandPanel.add(cdeleteButton);

        upColCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    columnTable.upRow();
                }
            });

        downColCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    columnTable.downRow();
                }
            });
        columnTable.getSelectionModel().addListSelectionListener(this);

        
        JPanel rightBottomPanel = new JPanel(new BorderLayout());
        rightBottomPanel.setBorder(new EmptyBorder(20, 3, 0, 20));

        JPanel aggregationPanel = new JPanel(new BorderLayout());
        JLabel aggregationLabel = new JLabel(App.messages.getString("res.241"), JLabel.LEFT);
        aggregationPanel.add(aggregationLabel, BorderLayout.WEST);
        adeleteButton = createAdeleteButton();
        aggregationPanel.add(adeleteButton, BorderLayout.EAST);

        rightBottomPanel.add(aggregationPanel, BorderLayout.NORTH);
        calcTable = createcalcTable();
        rightBottomPanel.add(new JScrollPane(calcTable));
        east.add(rightBottomPanel);

        
        
        JPanel middle = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.gridwidth = 1;

        JLabel label = new JLabel();
        label.setPreferredSize(labelSize);

        // label.setVisible(false);
        middle.add(label, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        columnButton = createColumnButton();
        middle.add(columnButton, gbc);
        gbc.gridwidth = 1;

        rowButton = createRowButton();
        middle.add(rowButton, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        aggregationButton = createAggregationButton();
        middle.add(aggregationButton, gbc);

        JPanel dataPanel = new JPanel(new GridBagLayout());

        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        dataPanel.add(west, gbc);
        gbc.weightx = 0;
        dataPanel.add(middle, gbc);
        gbc.weightx = 1;
        dataPanel.add(east, gbc);

        this.setLayout(new BorderLayout());

        this.add(dataPanel, BorderLayout.CENTER);

        chkFlowlayout = new JCheckBox(App.messages.getString("res.242"));
        chkFlowlayout.setEnabled(false);
        chkTopSummary = new JCheckBox(App.messages.getString("res.243"));
        chkTopSummary.setSelected(false);
        chkLeftSummary = new JCheckBox(App.messages.getString("res.244"));
        chkLeftSummary.setSelected(false);

        JPanel tmp = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        tmp.add(chkFlowlayout);
        tmp.add(chkTopSummary);
        tmp.add(chkLeftSummary);

        tmp.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        this.add(tmp, BorderLayout.SOUTH);

        dataTree.addChangeListener(this);
    }

    /**
     * 创建行字段删除按钮
     * @return JButton
     */
    private JButton createReleteButton() {
        Icon rdeleteButtonIcon = Util.getIcon("/jatools/icons/cross.gif"); //
        rdeleteButton = new JButton(rdeleteButtonIcon);

        rdeleteButton.setPreferredSize(buttonSize);

        rdeleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (rowTable.getRowCount() > 0) {
                        rowTable.removeRow(rowTable.getSelectedRow(), true);
                    }
                }
            });
        rdeleteButton.setEnabled(false);

        return rdeleteButton;
    }

    /**
     * 创建行字段表
     * @return CustomTable
     */
    private CustomTable createRowTabel() {
        CustomTable table = new CustomTable(new String[] {
                    App.messages.getString("res.108"),
                    App.messages.getString("res.124")
                });

        table.getModel().addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent e) {
                    rdeleteButton.setEnabled(rowTable.getRowCount() > 0);
                }
            });

        table.setBackground(Color.white);

        JComboBox sortTypeChooser = new JComboBox(new OrderBy[] {
                    new OrderBy(OrderBy.ASC),
                    new OrderBy(OrderBy.DESC),
                    new OrderBy(OrderBy.ORIGINAL)
                });
        table.getColumn(App.messages.getString("res.124")).setCellEditor( //
            new DefaultCellEditor(sortTypeChooser));
        table.setEditable(1, true);

        return table;
    }

    /**
     * 列字段删除按钮
     * @return JButton
     */
    private JButton createCdeleteButton() {
        Icon cdeleteButtonIcon = Util.getIcon("/jatools/icons/cross.gif"); //
        cdeleteButton = new JButton(cdeleteButtonIcon);
        cdeleteButton.setEnabled(false);
        cdeleteButton.setPreferredSize(buttonSize);

        cdeleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (columnTable.getRowCount() > 0) {
                        columnTable.removeRow(columnTable.getSelectedRow(), true);
                    }
                }
            });

        return cdeleteButton;
    }

    /**
     * 创建列字段表
     * @return CustomTable
     */
    private CustomTable createColumnTable() {
        CustomTable table = new CustomTable(new String[] {
                    App.messages.getString("res.108"),
                    App.messages.getString("res.124")
                });

        table.getModel().addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent e) {
                    cdeleteButton.setEnabled(columnTable.getRowCount() > 0);
                }
            });
        table.setBackground(Color.white);

        JComboBox sortTypeChooser = new JComboBox(new OrderBy[] {
                    new OrderBy(OrderBy.ASC),
                    new OrderBy(OrderBy.DESC),
                    new OrderBy(OrderBy.ORIGINAL)
                });

        table.getColumn(App.messages.getString("res.124")).setCellEditor(new DefaultCellEditor(sortTypeChooser));
        table.setEditable(1, true);

        return table;
    }

    /**
     * 统计表删除记录按钮
     * @return JButton
     */
    private JButton createAdeleteButton() {
        Icon adeleteButtonIcon = Util.getIcon("/jatools/icons/cross.gif"); //
        adeleteButton = new JButton(adeleteButtonIcon);
        adeleteButton.setEnabled(false);
        adeleteButton.setPreferredSize(buttonSize);

        adeleteButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (calcTable.getRowCount() > 0) {
                        ((DefaultTableModel) calcTable.getModel()).removeRow(0);
                    }
                }
            });

        return adeleteButton;
    }

    /**
     * 创建统计项表
     * @return CustomTable
     */
    private CustomTable createcalcTable() {
        CustomTable table = new CustomTable(new String[] {
                    App.messages.getString("res.245"), // //$NON-NLS-2$
                App.messages.getString("res.246") //
                }); // //$NON-NLS-2$

        table.getModel().addTableModelListener(new TableModelListener() {
                public void tableChanged(TableModelEvent e) {
                    adeleteButton.setEnabled(calcTable.getRowCount() > 0);
                    chkFlowlayout.setEnabled(calcTable.getRowCount() > 1);
                }
            });

        table.setBackground(Color.white);

        JComboBox sortTypeChooser = new JComboBox(CustomSummary.SUPPORT_FUNCTIONS);
        table.getColumn(App.messages.getString("res.246")).setCellEditor( //
            new DefaultCellEditor(sortTypeChooser));
        table.setEditable(1, true);
        table.setEditable(0, true);

        return table;
    }

    /**
     * 列按钮
     * @return JButton
     */
    private JButton createColumnButton() {
        Icon columnButtonIcon = Util.getIcon("/jatools/icons/toeast.gif"); //
        columnButton = new JButton(columnButtonIcon);
        columnButton.setEnabled(false);
        columnButton.setPreferredSize(labelSize);

        columnButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    columnTable.addRow(new Object[] {
                            hitField.getName(),
                            OrderBy.ASC
                        }, true);
                }
            });

        return columnButton;
    }

    /**
     * 行按钮
     * @return JButton
     */
    private JButton createRowButton() {
        Icon rowButtonIcon = Util.getIcon("/jatools/icons/tosouth.gif"); //
        rowButton = new JButton(rowButtonIcon);
        rowButton.setEnabled(false);
        rowButton.setPreferredSize(labelSize);

        rowButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    rowTable.addRow(new Object[] {
                            hitField.getName(),
                            OrderBy.ASC
                        }, true);
                }
            });

        return rowButton;
    }

    /**
     * 添加统计项按钮
     * @return JButton
     */
    private JButton createAggregationButton() {
        Icon aggregationButtonIcon = Util.getIcon("/jatools/icons/tosoutheast.gif"); //
        aggregationButton = new JButton(aggregationButtonIcon);
        aggregationButton.setEnabled(false);
        aggregationButton.setPreferredSize(labelSize);

        aggregationButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    selectFunction();
                }
            });

        return aggregationButton;
    }

    private void selectFunction() {
        Class cls = hitField.getColumnClass();
        String fun = null;

        if ((cls != null) && (Number.class.isAssignableFrom(cls))) {
            fun = CustomSummary.SUPPORT_FUNCTIONS[1];
        } else {
            fun = CustomSummary.SUPPORT_FUNCTIONS[0];
        }

        calcTable.addRow(new Object[] {
                hitField.getName(),
                fun
            }, true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void addChangeListener(ChangeListener lst) {
        listeners.add(lst);
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void removeChangeListener(ChangeListener lst) {
        listeners.remove(lst);
    }

    void fireChangeListener() {
        ChangeEvent e = new ChangeEvent(this);

        for (Iterator i = listeners.iterator(); i.hasNext();) {
            ChangeListener lst = (ChangeListener) i.next();
            lst.stateChanged(e);
        }
    }

    /**
     * 取得列分组
     * @return ArrayList
     */
    private ArrayList getColumnFields() {
        ArrayList list = new ArrayList();

        if (columnTable.getRowCount() > 0) {
            for (int i = 0; i < columnTable.getRowCount(); i++) {
                Group group = new Group(columnTable.getValueAt(i, 0).toString(),
                        OrderBy.getIntOrder(columnTable.getValueAt(i, 1).toString()));
                list.add(group);
            }
        }

        return list;
    }

    /**
     * 取得行分组
     * @return ArrayList
     */
    private ArrayList getRowFields() {
        ArrayList list = new ArrayList();

        if (rowTable.getRowCount() > 0) {
            for (int i = 0; i < rowTable.getRowCount(); i++) {
                Group group = new Group(rowTable.getValueAt(i, 0).toString(),
                        OrderBy.getIntOrder(rowTable.getValueAt(i, 1).toString()));
                list.add(group);
            }
        }

        return list;
    }

    /**
     * 取得统计
     * @return ArrayList
     */
    private ArrayList getSums() {
        ArrayList list = new ArrayList();

        if (calcTable.getRowCount() > 0) {
            CustomSummary summary = null;

            for (int i = 0; i < calcTable.getRowCount(); i++) {
                String calcField = (String) calcTable.getValueAt(i, 0);
                String type = (String) calcTable.getValueAt(i, 1);
                summary = new CustomSummary(null, type, calcField, null, null);

                list.add(CustomSummary.asGroupCalc(summary));
            }
        }

        return list;
    }

    /**
     * 当数据集变化时候，调用此方法
     * @param reader DatasetReader
     */
    void setCachedReader(DatasetReader reader) {
        this.reader = reader;
        calcTable.removeAllRows();
        columnTable.removeAllRows();
        rowTable.removeAllRows();

        if (reader == null) {
            dataTree.setModel(new DefaultTreeModel(null));
        } else {
            dataTree.setModel(new DefaultTreeModel(DataTreeUtil.asTree(reader)));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param reader DOCUMENT ME!
     */
    public void setReader(DatasetReader reader) {
        if (reader != null) {
        } else {
            setCachedReader(null);
        }

        this.reader = reader;
    }

    /**
     * DOCUMENT ME!
     */
    public void enableButtons() {
        upRowCommand.setEnabled(rowTable.canUp());
        downRowCommand.setEnabled(rowTable.canDown());
        upColCommand.setEnabled(columnTable.canUp());
        downColCommand.setEnabled(columnTable.canDown());
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
     */

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void valueChanged(ListSelectionEvent e) {
        enableButtons();
    }

    /**
     * 点击完成后设置数据到context，以便后头取
     * @param context BuilderContext
     */
    public void apply(BuilderContext context) {
        context.setValue(CrossTabStyler.COLUMN_FIELDS, this.getColumnFields());
        context.setValue(CrossTabStyler.ROW_FIELDS, this.getRowFields());
        context.setValue(CrossTabStyler.SUMS, this.getSums());

        context.setValue(CrossTabStyler.ROW_FLOWLAYOUT,
            chkFlowlayout.isSelected() ? Boolean.TRUE : Boolean.FALSE);
        context.setValue(CrossTabStyler.COLUMN_SUM_LEFT,
            chkLeftSummary.isSelected() ? Boolean.TRUE : Boolean.FALSE);
        context.setValue(CrossTabStyler.ROW_SUM_TOP,
            chkTopSummary.isSelected() ? Boolean.TRUE : Boolean.FALSE);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == dataTree) {
            
            TreePath path = dataTree.getSelectionPath();

            if (path != null) {
                boolean hitReader = ((SimpleTreeNode) path.getLastPathComponent()).getUserObject() instanceof Column;

                rowButton.setEnabled(hitReader);
                columnButton.setEnabled(hitReader);
                aggregationButton.setEnabled(hitReader);

                if (hitReader) {
                    hitField = (Column) ((SimpleTreeNode) dataTree.getSelectionPath()
                                                                  .getLastPathComponent()).getUserObject();
                }
            }
        }
    }
}
