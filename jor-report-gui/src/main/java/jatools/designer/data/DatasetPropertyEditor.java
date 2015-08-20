package jatools.designer.data;



import jatools.designer.App;
import jatools.swingx.MessageBox;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
 * @version $Revision$
  */
public class DatasetPropertyEditor extends JDialog {
    private JTextField nameText;
    private JComboBox typeSelector;
    private boolean exitOK = false;
    private NameChecker nameChecker;

    /**
     * Creates a new DatasetPropertyEditor object.
     *
     * @param owner DOCUMENT ME!
     * @param name DOCUMENT ME!
     * @param type DOCUMENT ME!
     * @param types DOCUMENT ME!
     */
    public DatasetPropertyEditor(Frame owner, String name, Object type, Object[] types) {
        super(owner, App.messages.getString("res.541"));
        buildUI(name, type, types);
        setModal(true);

        pack();
        setLocationRelativeTo(owner);
    }

    /**
     * DOCUMENT ME!
     *
     * @param nameChecker DOCUMENT ME!
     */
    public void setNameChecker(NameChecker nameChecker) {
        this.nameChecker = nameChecker;
    }

    private void buildUI(String name, Object type, Object[] types) {
        nameText = new JTextField(name);

        typeSelector = new JComboBox(types);
        typeSelector.setSelectedItem(type);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;

        JPanel center = new JPanel(new GridBagLayout());

        center.add(new JLabel(App.messages.getString("res.90")), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 70;
        center.add(nameText, gbc);
        gbc.weightx = 0;

        gbc.gridwidth = 1;
        center.add(new JLabel(App.messages.getString("res.542")), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        center.add(typeSelector, gbc);
        typeSelector.setPreferredSize(new Dimension(200, 25));

        JPanel south = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton b = new JButton(App.messages.getString("res.352"));
        b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (validateInput()) {
                        exitOK = true;
                        setVisible(false);
                    }
                }
            });

        south.add(b);
        b = new JButton(App.messages.getString("res.4"));
        b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setVisible(false);
                }
            });
        south.add(b);
        center.setBorder(BorderFactory.createEmptyBorder(25, 25, 15, 25));
        south.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.getContentPane().add(center, BorderLayout.CENTER);
        this.getContentPane().add(south, BorderLayout.SOUTH);
    }

    private boolean validateInput() {
        JComponent focus = null;
        String error = null;

        String name = nameText.getText();

        try {
            if (nameChecker != null) {
                nameChecker.check(name);
            }
        } catch (Exception ex) {
            focus = nameText;
            nameText.selectAll();
            error = ex.getMessage();
        }

        if ((focus == null) && (typeSelector.getSelectedIndex() == -1)) {
            focus = typeSelector;
            error = App.messages.getString("res.543");
        }

        if (error != null) {
            MessageBox.error(this.getContentPane(), error);
            focus.requestFocus();

            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isExitOK() {
        return exitOK;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return nameText.getText();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getType() {
        return (String) typeSelector.getSelectedItem();
    }
}
