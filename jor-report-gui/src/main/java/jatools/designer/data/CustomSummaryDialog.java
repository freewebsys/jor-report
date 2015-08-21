package jatools.designer.data;

import jatools.data.sum.Sum;
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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class CustomSummaryDialog extends JDialog {
    Dimension textSize = new Dimension(155, 25);
    Dimension buttonSize = new Dimension(70, 25);
    Dimension labelSize = new Dimension(130, 25);
    private JComboBox byCombo = new JComboBox();
    private JComboBox fieldCombo = new JComboBox();
    private JComboBox funcCombo = new JComboBox();

    /**
     * Creates a new CustomSummaryDialog object.
     *
     * @param owner DOCUMENT ME!
     */
    public CustomSummaryDialog(Frame owner) {
        super(owner, App.messages.getString("res.530"), true);

        fieldCombo.setEditable(false);
        byCombo.setEditable(false);
        funcCombo.setEditable(false);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(1, 10, 1, 10));

        JPanel topPanel = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10, 10, 10, 10);
        topPanel.add(new JLabel(App.messages.getString("res.197")), gbc);

        gbc.gridwidth = GridBagConstraints.REMAINDER;

        topPanel.add(byCombo, gbc);

        gbc.gridwidth = 1;

        topPanel.add(new JLabel(App.messages.getString("res.531")), gbc);

        gbc.gridwidth = GridBagConstraints.REMAINDER;

        fieldCombo = new JComboBox();

        topPanel.add(fieldCombo, gbc);
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        topPanel.add(new JLabel(App.messages.getString("res.532")), gbc);

        gbc.weightx = 100;

        topPanel.add(funcCombo, gbc);

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
                    hide();
                }
            });

        buttonPanel.add(cancelButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);
        panel.add(topPanel, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        setSize(new Dimension(330, 250));
        this.setLocationRelativeTo(owner);
    }

    private void done() {
        try {
            hide();
        } catch (Exception e1) {
            JOptionPane.showConfirmDialog(getParent(), e1.getMessage(), App.messages.getString("res.26"),
                JOptionPane.OK_OPTION, JOptionPane.ERROR_MESSAGE);
            e1.printStackTrace();
        }
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
}
