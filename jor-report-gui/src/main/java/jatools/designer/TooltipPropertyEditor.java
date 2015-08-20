package jatools.designer;

import jatools.swingx.Chooser;
import jatools.swingx.CommandPanel;
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
public class TooltipPropertyEditor extends JDialog implements Chooser {
    TemplateTextField tooltipField = new TemplateTextField();
    boolean done;
    private String result;

    /**
     * Creates a new TooltipPropertyEditor object.
     */
    public TooltipPropertyEditor() {
        super(Main.getInstance(), App.messages.getString("res.203"), true);

        JPanel p = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);

        gbc.anchor = GridBagConstraints.WEST;

        gbc.weightx = 0;

        gbc.gridwidth = 1;
        p.add(new JLabel(App.messages.getString("res.204")), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        p.add(tooltipField, gbc);
        gbc.weightx = 0;
        gbc.weighty = 100;
        p.add(Box.createVerticalGlue(), gbc);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        getContentPane().add(p);

        ActionListener oklistener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    done = true;
                    result = getTooltip();

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

        getContentPane().add(commandPanel, BorderLayout.SOUTH);
        setSize(new Dimension(430, 250));
    }

    protected String getTooltip() {
        String tooltip = this.tooltipField.getText();

        if ((tooltip != null) && (tooltip.trim().length() == 0)) {
            return null;
        } else {
            return tooltip;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     */
    public void setValue(Object value) {
        String tooltip = (String) value;

        this.tooltipField.setText(tooltip);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue() {
        return result;
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
        this.setValue(value);
        this.setLocationRelativeTo(owner);
        show();

        return this.done;
    }
}
