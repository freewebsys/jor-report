/*
 * Created on 2005-3-8
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jatools.viewer;

import jatools.designer.App;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

/**
 * @author java
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ed extends JDialog implements ActionListener {
    String type = "dhtml";
    boolean xok = false;

    ed() {
        super((Frame) null, App.messages.getString("res.1"), true);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.WEST;

        ButtonGroup group = new ButtonGroup();
        JRadioButton check = new JRadioButton("dhtml");
        panel.add(check, gbc);

        check.setSelected(true);

        check.addActionListener(this);
        group.add(check);

        check = new JRadioButton("pdf");
        panel.add(check, gbc);
        check.addActionListener(this);
        group.add(check);
        check = new JRadioButton("xls");
        panel.add(check, gbc);
        check.addActionListener(this);
        group.add(check);
        check = new JRadioButton("rtf");
        panel.add(check, gbc);
        check.addActionListener(this);
        group.add(check);
        check = new JRadioButton("csv");
        panel.add(check, gbc);
        check.addActionListener(this);
        group.add(check);
        check = new JRadioButton("ps");
        panel.add(check, gbc);
        check.addActionListener(this);
        group.add(check);

        panel.setBorder(BorderFactory.createTitledBorder(App.messages.getString("res.2")));
        ((JPanel) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel commandPanel = new JPanel(new BorderLayout());
        JPanel commandPanel0 = new JPanel(new GridBagLayout());
        JButton doneCommand = new JButton(App.messages.getString("res.3"));
        doneCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    xok = true;
                    hide();
                }
            });

        JButton cancelCommand = new JButton(App.messages.getString("res.4"));
        cancelCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    hide();
                }
            });

        gbc.insets = new Insets(5, 10, 5, 0);
        commandPanel0.add(doneCommand, gbc);
        commandPanel0.add(cancelCommand, gbc);
        commandPanel.add(commandPanel0, BorderLayout.SOUTH);

        ///
        this.getContentPane().add(panel, BorderLayout.CENTER);
        this.getContentPane().add(commandPanel, BorderLayout.EAST);
        setSize(200, 250);
        setLocationRelativeTo(null);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getType() {
        return type;
    }




    /**
     * DOCUMENT ME!
     *
     * @param e
     *            DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        JRadioButton b = (JRadioButton) e.getSource();
        type = b.getText();
    }
}
