package jatools.designer.layer.table;


import jatools.designer.App;
import jatools.swingx.CommandPanel;
import jatools.swingx.SpinEditor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class TableSpecDialog extends JDialog {
    public boolean exitOk;
    SpinEditor rowEditor;
    SpinEditor colEditor;

    /**
     * Creates a new TableSpecDialog object.
     *
     * @param owner DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param col DOCUMENT ME!
     */
    public TableSpecDialog(Frame owner, int row, int col) {
        super(owner, App.messages.getString("res.431"), true);

        rowEditor = new SpinEditor(new Integer(row), new Integer(1), new Integer(100),
                new Integer(1));
        colEditor = new SpinEditor(new Integer(col), new Integer(1), new Integer(100),
                new Integer(1));

        rowEditor.setPreferredSize(new Dimension(100, 23));
        colEditor.setPreferredSize(rowEditor.getPreferredSize());

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        panel.add(new JLabel(App.messages.getString("res.432")), gbc);
        panel.add(rowEditor, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 100;
        panel.add(Box.createHorizontalGlue(), gbc);
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        panel.add(new JLabel(App.messages.getString("res.433")), gbc);

        panel.add(colEditor, gbc);

        ActionListener oklistener = new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    exitOk = true;
                    hide();
                }
            };

        ActionListener canellistener = new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    exitOk = false;
                    hide();
                }
            };

        JPanel commandsPane = CommandPanel.createPanel(oklistener, canellistener);
        this.getContentPane().add(panel, BorderLayout.CENTER);
        this.getContentPane().add(commandsPane, BorderLayout.SOUTH);
        pack();

        setSize(250, 180);
        this.setLocationRelativeTo(owner);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRowCount() {
        return ((Integer) rowEditor.getValue()).intValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumnCount() {
        return ((Integer) colEditor.getValue()).intValue();
    }
}
