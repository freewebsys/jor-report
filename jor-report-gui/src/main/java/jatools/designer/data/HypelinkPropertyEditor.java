package jatools.designer.data;



import jatools.designer.App;
import jatools.designer.Main;
import jatools.swingx.Chooser;
import jatools.swingx.CommandPanel;
import jatools.swingx.SwingUtil;
import jatools.swingx.TemplateTextField;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class HypelinkPropertyEditor extends JDialog implements Chooser {
    TemplateTextField urlField = new TemplateTextField();
    boolean done;
    private Hyperlink result;
    JComboBox targetCombo = new JComboBox(new Object[] { "_blank", "_parent", "_self", "_top" });

    /**
     * Creates a new HypelinkPropertyEditor object.
     */
    public HypelinkPropertyEditor() {
        super(Main.getInstance(), App.messages.getString("res.546"), true);

        JPanel p = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);

        gbc.anchor = GridBagConstraints.WEST;

        gbc.weightx = 0;

        gbc.gridwidth = 1;
        p.add(new JLabel(App.messages.getString("res.547")), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        p.add(urlField, gbc);
        gbc.weightx = 0;

        gbc.gridwidth = 1;
        p.add(new JLabel(App.messages.getString("res.548")), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.NONE;
        p.add(targetCombo, gbc);
        SwingUtil.setSize(targetCombo, new Dimension(80, 23));
        gbc.weighty = 100;
        p.add(Box.createVerticalGlue(), gbc);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        getContentPane().add(p);

        ActionListener oklistener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    done = true;
                    result = getHyperlink();

                    hide();
                }
            };

        ActionListener cancellistener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    done = false;
                    hide();
                }
            };

        CommandPanel commandPanel = CommandPanel.createPanel(oklistener, cancellistener);

        commandPanel.addComponent(App.messages.getString("res.23"),
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    done = true;
                    hide();
                }
            });

        this.getContentPane().add(commandPanel, BorderLayout.SOUTH);

        setSize(new Dimension(430, 250));
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     */
    public void setHyperlink(Hyperlink f) {
        if (f == null) {
            f = new Hyperlink();
        }

        this.urlField.setText(f.getUrl());
        this.targetCombo.setSelectedItem((f.getTarget() == null) ? "_blank" : f.getTarget());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue() {
        return this.result;
    }

    private Hyperlink getHyperlink() {
        String url = this.urlField.getText();

        if ((url != null) && (url.trim().length() == 0)) {
            return null;
        } else {
            return new Hyperlink(url, (String) this.targetCombo.getSelectedItem());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean showChooser(JComponent owner, Object value) {
        this.done = false;
        this.result = null;
        this.setHyperlink((Hyperlink) value);
        this.setLocationRelativeTo(owner);
        show();

        return this.done;
    }
}
