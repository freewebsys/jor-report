package jatools.designer.variable.dialog;

import jatools.data.reader.DatasetReader;
import jatools.dataset.Dataset;
import jatools.designer.App;
import jatools.dom.Group;
import jatools.dom.src.DatasetNodeSource;
import jatools.dom.src.GroupNodeSource;
import jatools.dom.src.NodeSource;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.6 $
  */
public class GroupSourceDialog extends JDialog implements ActionListener {
    private static NodeSource parent;
    private static GroupNodeSource current;
    private static boolean exitedOk;
    private JComboBox fieldsBox;
    private JComboBox ordersBox;
    private Component c;

    private GroupSourceDialog(NodeSource source, Component c) {
        super((Frame) javax.swing.SwingUtilities.getWindowAncestor(c));
        exitedOk = false;
        this.setModal(true);
        this.setTitle(App.messages.getString("res.275"));
        this.parent = source;
        this.c = c;
        initUI();
    }

    private void initUI() {
        this.setSize(new Dimension(260, 150));

        this.getContentPane().setLayout(new BorderLayout());

        if (c != null) {
            this.setLocationRelativeTo(c);
        } else {
            this.setLocation(250, 250);
        }

        JPanel center = new JPanel();
        JLabel label = new JLabel(App.messages.getString("res.276"));
        fieldsBox = new JComboBox();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        center.setLayout(new GridBagLayout());

        center.add(label, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 70;
        center.add(fieldsBox, gbc);
        gbc.weightx = 0;

        label = new JLabel(App.messages.getString("res.277"));
        ordersBox = new JComboBox();
        gbc.gridwidth = 1;
        center.add(label, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        center.add(ordersBox, gbc);

        JButton ok = new JButton(App.messages.getString("res.3"));
        JButton cancel = new JButton(App.messages.getString("res.4"));
        ok.setPreferredSize(new Dimension(78, 23));
        cancel.setPreferredSize(new Dimension(78, 23));
        ok.setActionCommand("ok");
        cancel.setActionCommand("cancel");
        ok.addActionListener(this);
        cancel.addActionListener(this);

        Box south = Box.createHorizontalBox();
        south.add(Box.createHorizontalGlue());
        south.add(ok);
        south.add(Box.createHorizontalStrut(10));
        south.add(cancel);
        south.add(Box.createHorizontalStrut(10));

        center.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        south.setBorder(BorderFactory.createEmptyBorder(10, 5, 5, 0));
        this.getContentPane().add(center, BorderLayout.CENTER);
        this.getContentPane().add(south, BorderLayout.SOUTH);
    }

    private void setCurrentGroupSource(GroupNodeSource groupSource) {
        this.current = groupSource;

        DefaultComboBoxModel filed_model = new DefaultComboBoxModel(getFields(parent));
        fieldsBox.setModel(filed_model);

        Object[] orders = {
                new Order(App.messages.getString("res.121"), Group.ASCEND),
                new Order(App.messages.getString("res.122"), Group.DESEND),
                new Order(App.messages.getString("res.123"), Group.ORIGINAL)
            };
        DefaultComboBoxModel orer_model = new DefaultComboBoxModel(orders);
        ordersBox.setModel(orer_model);

        if (current != null) {
            filed_model.setSelectedItem(current.getGroup().getField());
            orer_model.setSelectedItem(orer_model.getElementAt(current.getGroup().getOrder()));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param groupSource DOCUMENT ME!
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static GroupNodeSource getNodeSource(NodeSource parent, GroupNodeSource groupSource,
        Component c) {
        GroupSourceDialog dialog = new GroupSourceDialog(parent, c);
        dialog.setCurrentGroupSource(groupSource);
        dialog.setVisible(true);

        if (exitedOk) {
            return current;
        } else {
            return null;
        }
    }

    private String[] getFields(NodeSource parent) {
        if (parent instanceof DatasetNodeSource) {
            DatasetNodeSource dn = (DatasetNodeSource) parent;

            return getFields(dn.getReader());
        } else {
            return getFields(parent.getParent());
        }
    }

    private String[] getFields(DatasetReader reader) {
        if (reader != null) {
            return Dataset.getFieldNames(reader);
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("ok")) {
            if (current == null) {
                current = new GroupNodeSource();
            }

            current.setGroup(new Group(fieldsBox.getSelectedItem().toString(),
                    ordersBox.getSelectedIndex()));
            exitedOk = true;
            this.dispose();
        } else if (e.getActionCommand().equals("cancel")) {
            exitedOk = false;
            this.dispose();
        }
    }

    class Order {
        private String name;
        private int index;

        Order(String name, int index) {
            this.name = name;
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public String toString() {
            return name;
        }
    }
}
