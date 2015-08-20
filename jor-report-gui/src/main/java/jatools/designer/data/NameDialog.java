package jatools.designer.data;


import jatools.designer.App;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class NameDialog extends JDialog {
    private JTextField nameField;
    private NameChecker checker;
    private String variableName;

    /**
     * Creates a new NameDialog object.
     *
     * @param owner DOCUMENT ME!
     */
    public NameDialog(Frame owner) {
        super(owner, App.messages.getString("res.525"), true);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(1, 10, 1, 10));

        JPanel topPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(25, 10, 10, 10);
        topPanel.add(new JLabel(App.messages.getString("res.90")), gbc);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 100;

        nameField = new JTextField();
        topPanel.add(nameField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton okButton = new JButton(App.messages.getString("res.3"));

        okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    done();
                }
            });

        buttonPanel.add(okButton);

        JButton cancelButton = new JButton(App.messages.getString("res.4"));
        cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    variableName = null;
                    hide();
                }
            });

        buttonPanel.add(cancelButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(topPanel, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        setSize(new Dimension(330, 150));
        this.setLocationRelativeTo(owner);
    }

    private void done() {
        try {
            checker.check(nameField.getText());
            variableName = nameField.getText();

            hide();
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(getParent(), e1.getMessage(), App.messages.getString("res.26"),
                JOptionPane.ERROR_MESSAGE);

            nameField.requestFocus();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param checker DOCUMENT ME!
     * @param def DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String start(NameChecker checker, String def) {
        this.checker = checker;
        nameField.setText(def);
        nameField.selectAll();

        show();

        return variableName;
    }
}
