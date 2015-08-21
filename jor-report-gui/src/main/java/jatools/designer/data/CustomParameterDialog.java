package jatools.designer.data;





import jatools.data.Parameter;
import jatools.db.TypeUtil;
import jatools.designer.App;
import jatools.designer.Main;
import jatools.swingx.CommandPanel;
import jatools.swingx.GridBagConstraints2;
import jatools.swingx.SwingUtil;
import jatools.swingx.TemplateTextField;
import jatools.swingx.TypesCombobox;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
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
public class CustomParameterDialog extends JDialog {
    Dimension textSize = new Dimension(155, 25);
    Dimension buttonSize = new Dimension(70, 25);
    Dimension labelSize = new Dimension(130, 25);
    private JTextField nameField;
    private JComboBox typeCombo;
    private TemplateTextField defaultField;
    private Parameter parameter;
    private NameChecker checker;
    private String oldName;

    /**
     * Creates a new CustomParameterDialog object.
     */
    public CustomParameterDialog() {
        super(Main.getInstance(), App.messages.getString("res.525"), true);

        CommandPanel buttonPanel = CommandPanel.createPanel();

        JButton okButton = new JButton(App.messages.getString("res.3"));
        okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    done();
                }
            });

        buttonPanel.addComponent(okButton);

        JButton cancelButton = new JButton(App.messages.getString("res.4"));
        cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    hide();
                }
            });

        buttonPanel.addComponent(cancelButton);

        this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        this.getContentPane().add(getGeneralPanel(), BorderLayout.CENTER);
        SwingUtil.setBorder6((JComponent) this.getContentPane());

        pack();

        setSize(new Dimension(400, 250));
        this.setLocationRelativeTo(Main.getInstance());
    }

    private JPanel getGeneralPanel() {
        JPanel result = new JPanel(new GridBagLayout());

        GridBagConstraints2 gbc = new GridBagConstraints2(result);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        result.add(new JLabel(App.messages.getString("res.90")), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        gbc.weightx = 100;
        nameField = new JTextField();
        gbc.add(nameField, 120);
        gbc.weightx = 0;
        gbc.gridwidth = 1;

        result.add(new JLabel(App.messages.getString("res.526")), gbc);

        gbc.gridwidth = GridBagConstraints.REMAINDER;

        typeCombo = new TypesCombobox(TypeUtil.SHORT_NAME);

        typeCombo.setEditable(false);
        gbc.add(typeCombo, 120);

        gbc.gridwidth = 1;

        result.add(new JLabel(App.messages.getString("res.527")), gbc);

        defaultField = new TemplateTextField();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        result.add(defaultField, gbc);
        gbc.weighty = 100;

        result.add(Box.createVerticalGlue(), gbc);
        SwingUtil.setBorder6(result);

        return result;
    }

    private void done() {
        try {
            parameter = createParameter();
            hide();
        } catch (Exception e1) {
            JOptionPane.showMessageDialog(getParent(), e1.getMessage(), App.messages.getString("res.26"),
                JOptionPane.ERROR_MESSAGE);
            e1.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param checker DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Parameter start(NameChecker checker) {
        return start(null, checker);
    }

    /**
     * DOCUMENT ME!
     *
     * @param para DOCUMENT ME!
     * @param checker DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Parameter start(Parameter para, NameChecker checker) {
        this.checker = checker;

        if (para != null) {
            oldName = para.getName();
            nameField.setText(para.getName());
            typeCombo.setSelectedItem(TypeUtil.getShortName(para.getType1()));
            defaultField.setText(para.getDefaultValue());
        }

        show();

        return parameter;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     *
     * @throws HeadlessException DOCUMENT ME!
     */
    public static void main(String[] args) throws HeadlessException {
    }

    private Parameter createParameter() throws Exception {
        String name = nameField.getText();

        if (!((oldName != null) && oldName.equals(name))) {
            if (checker != null) {
                try {
                    checker.check(name);
                } catch (Exception e) {
                    nameField.requestFocus();
                    throw e;
                }
            }
        }

        String type = (String) typeCombo.getSelectedItem();

        if (isEmpty(type)) {
            typeCombo.requestFocus();
            throw new Exception(App.messages.getString("res.528"));
        }

        String def = defaultField.getText().trim();

        if (isEmpty(def)) {
            defaultField.requestFocus();
            throw new Exception(App.messages.getString("res.529"));
        }

        Parameter parameter = new Parameter(name, TypeUtil.getClassName(type), null, def);

        return parameter;
    }

    private boolean isEmpty(String text) {
        return (text == null) || text.equals("");
    }

    private Parameter getParameter() {
        return parameter;
    }
}
