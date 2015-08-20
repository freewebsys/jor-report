package jatools.designer.variable.dialog;

import jatools.resources.Messages;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class IndexFieldSelectPanel extends JPanel implements ActionListener {
    private boolean crossIndex;
    private JList leftList;
    private JList rowList;
    private JList columnList;
    private JScrollPane columnListScroll;
    private DefaultListModel leftModel;
    private DefaultListModel rowModel;
    private DefaultListModel columnModel;
    private JTabbedPane rightTab;

    /**
     * Creates a new IndexFieldSelectPanel object.
     */
    public IndexFieldSelectPanel() {
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.WEST;

        gbc.weightx = 0.5;
        gbc.weighty = 1.0;

        leftList = new JList();
        leftList.setCellRenderer(new MyCellRenderer());

        JTabbedPane leftTab = new JTabbedPane();
        leftTab.addTab(Messages.getString("res.270"), new JScrollPane(leftList));

        leftTab.setPreferredSize(new Dimension(150, 200));

        add(leftTab, gbc);
        gbc.weightx = 0.0;
        add(controlPanel(), gbc);
        gbc.weightx = 0.5;
        rowList = new JList();

        rightTab = new JTabbedPane();
        rightTab.addTab(Messages.getString("res.279"), new JScrollPane(rowList));
        columnList = new JList();
        columnListScroll = new JScrollPane(columnList);

        rightTab.setPreferredSize(new Dimension(150, 200));

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(rightTab, gbc);
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void requiredCheck() throws Exception {
        if (this.rowModel.getSize() == 0) {
            String rowIndexTitle = (this.isCrossIndex()) ? "行索引字段" : "索引字段";
            this.rightTab.setSelectedIndex(0);
            throw new Exception(rowIndexTitle + "不能为空!");
        }

        if ((this.columnModel.getSize() == 0) && this.isCrossIndex()) {
            this.rightTab.setSelectedIndex(1);
            throw new Exception("列索引字段不能为空!");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param fields DOCUMENT ME!
     * @param selectedFields DOCUMENT ME!
     * @param selectedFields2 DOCUMENT ME!
     */
    public void init(String[] fields, String[] selectedFields, String[] selectedFields2) {
        leftModel = new DefaultListModel();

        for (int i = 0; i < fields.length; i++) {
            leftModel.addElement(new FieldItem(fields[i]));
        }

        leftList.setModel(leftModel);

        rowModel = new DefaultListModel();

        if (selectedFields != null) {
            for (int i = 0; i < selectedFields.length; i++) {
                rowModel.addElement(selectedFields[i]);
            }
        }

        rowList.setModel(rowModel);

        columnModel = new DefaultListModel();

        if (selectedFields2 != null) {
            for (int i = 0; i < selectedFields2.length; i++) {
                columnModel.addElement(selectedFields2[i]);
            }
        }

        columnList.setModel(columnModel);

        this.setCrossIndex((selectedFields2 != null) && (selectedFields2.length > 0));
    }

    private JPanel controlPanel() {
        JButton add = new JButton(">");
        add.setActionCommand("add");
        add.addActionListener(this);

        JButton del = new JButton("<");
        del.setActionCommand("del");
        del.addActionListener(this);

        JPanel p = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        p.add(add, gbc);
        p.add(del, gbc);

        return p;
    }

    private JList getSelectedList() {
        if (rightTab.getSelectedIndex() == 0) {
            return this.rowList;
        } else {
            return this.columnList;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        JList list = getSelectedList();
        DefaultListModel model = (DefaultListModel) list.getModel();

        if (e.getActionCommand().equals("add")) {
            FieldItem l = (FieldItem) leftList.getSelectedValue();

            if ((l != null) && !l.selected) {
                model.addElement(l.name);
                this.enableSrcFields();
            }
        } else if (e.getActionCommand().equals("addall")) {
            model.removeAllElements();

            for (int i = 0; i < leftModel.getSize(); i++) {
                model.addElement(leftModel.get(i));
            }
        } else if (e.getActionCommand().equals("del")) {
            String l = (String) list.getSelectedValue();

            if (l != null) {
                model.removeElement(l);
            }

            this.enableSrcFields();
        } else if (e.getActionCommand().equals("delall")) {
            model.removeAllElements();
        }
    }

    private void enableSrcFields() {
        for (int i = 0; i < this.leftModel.getSize(); i++) {
            FieldItem item = (FieldItem) this.leftModel.get(i);
            item.selected = false;
        }

        FieldItem target = new FieldItem();

        for (int i = 0; i < this.rowModel.getSize(); i++) {
            String item = (String) this.rowModel.get(i);
            target.name = item;

            int sel = this.leftModel.indexOf(target);

            if (sel > -1) {
                ((FieldItem) this.leftModel.get(sel)).selected = true;
            }
        }

        if (this.isCrossIndex()) {
            for (int i = 0; i < this.columnModel.getSize(); i++) {
                String item = (String) this.columnModel.get(i);
                target.name = item;

                int sel = this.leftModel.indexOf(target);

                if (sel > -1) {
                    ((FieldItem) this.leftModel.get(sel)).selected = true;
                }
            }
        }

        leftList.repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCrossIndex() {
        return this.rightTab.getTabCount() == 2;
    }

    /**
     * DOCUMENT ME!
     *
     * @param crossIndex DOCUMENT ME!
     */
    public void setCrossIndex(boolean crossIndex) {
        if (crossIndex) {
            if (this.rightTab.getTabCount() == 1) {
                this.rightTab.addTab("列索引字段", columnListScroll);
                this.rightTab.setTitleAt(0, "行索引字段");
            }
        } else {
            if (this.rightTab.getTabCount() > 1) {
                this.rightTab.removeTabAt(1);
                this.rightTab.setTitleAt(0, "索引字段");
            }
        }

        this.enableSrcFields();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String[] getIndexFields() {
        String[] result = new String[this.rowModel.getSize()];

        for (int i = 0; i < result.length; i++) {
            result[i] = (String) this.rowModel.get(i);
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String[] getIndexFields2() {
        if (this.isCrossIndex()) {
            String[] result = new String[this.columnModel.getSize()];

            for (int i = 0; i < result.length; i++) {
                result[i] = (String) this.columnModel.get(i);
            }

            return result;
        } else {
            return null;
        }
    }

    class MyCellRenderer extends JLabel implements ListCellRenderer {
        public MyCellRenderer() {
            setOpaque(true);
        }

        public Component getListCellRendererComponent(JList list, Object value, int index,
            boolean isSelected, boolean cellHasFocus) {
            setText(value.toString());

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            if (value instanceof FieldItem && ((FieldItem) value).selected) {
                setForeground(Color.gray);
            }

            return this;
        }
    }

    class FieldItem {
        boolean selected;
        String name;

        FieldItem(String name) {
            this.name = name;
        }

        public FieldItem() {
            // TODO Auto-generated constructor stub
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }

            FieldItem other = (FieldItem) obj;

            return name.equals(other.name);
        }

        public String toString() {
            return this.name;
        }
    }
}
