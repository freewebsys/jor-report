package jatools.swingx;

import jatools.designer.App;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class CommandPanel extends JPanel {
    /**
     * DOCUMENT ME!
     */
    public static final String OK = App.messages.getString("res.3");

    /**
     * DOCUMENT ME!
     */
    public static final String CANCEL = App.messages.getString("res.4");
    JSeparator separator;
    JPanel buttonsPanel;

    /**
     * Creates a new CommandPanel object.
     */
    private CommandPanel(boolean sep, int layout) {
        super(new BorderLayout());
        buttonsPanel = new JPanel(new FlowLayout(layout));

        if (sep) {
            separator = new JSeparator();
            add(separator, BorderLayout.NORTH);
        }

        add(buttonsPanel, BorderLayout.CENTER);
    }

    private CommandPanel(boolean sep) {
        this(sep, FlowLayout.RIGHT);
    }

    /**
     * DOCUMENT ME!
     *
     * @param title1 DOCUMENT ME!
     * @param l1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static CommandPanel createPanel() {
        CommandPanel p = new CommandPanel(true);

        return p;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hasSeparator DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static CommandPanel createPanel(boolean hasSeparator) {
        CommandPanel p = new CommandPanel(hasSeparator);

        return p;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hasSeparator DOCUMENT ME!
     * @param layout DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static CommandPanel createPanel(boolean hasSeparator, int layout) {
        CommandPanel p = new CommandPanel(hasSeparator, layout);

        return p;
    }

    /**
     * DOCUMENT ME!
     *
     * @param title1 DOCUMENT ME!
     * @param l1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static CommandPanel createPanel(String title1, ActionListener l1) {
        CommandPanel p = new CommandPanel(true);

        p.addComponent(title1, l1);

        return p;
    }

    /**
     * DOCUMENT ME!
     *
     * @param title1 DOCUMENT ME!
     * @param l1 DOCUMENT ME!
     * @param title2 DOCUMENT ME!
     * @param l2 DOCUMENT ME!
     * @param title3 DOCUMENT ME!
     * @param l3 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static CommandPanel createPanel(String title1, ActionListener l1, String title2,
        ActionListener l2, String title3, ActionListener l3) {
        CommandPanel p = new CommandPanel(true);
        p.addComponent(title1, l1);
        p.addComponent(title2, l2);
        p.addComponent(title3, l3);

        return p;
    }

    /**
     * DOCUMENT ME!
     *
     * @param title DOCUMENT ME!
     * @param l DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JButton addComponent(String title, ActionListener l) {
        JButton b = new JButton(title);
        b.addActionListener(l);

        buttonsPanel.add(b);

        return b;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JButton addComponent(JButton b) {
        buttonsPanel.add(b);

        return b;
    }

    /**
     * DOCUMENT ME!
     */
    public void addComponents(JComponent... buttons) {
        if (buttons != null) {
            for (int i = 0; i < buttons.length; i++) {
                buttonsPanel.add(buttons[i]);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     */
    public void addSeparator(int width) {
        buttonsPanel.add(Box.createHorizontalStrut(width));
    }

    /**
     * DOCUMENT ME!
     *
     * @param l1 DOCUMENT ME!
     * @param l2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static CommandPanel createPanel(ActionListener l1, ActionListener l2) {
        CommandPanel p = new CommandPanel(true);
        p.addComponent(OK, l1);
        p.addComponent(CANCEL, l2);

        return p;
    }

    /**
     * DOCUMENT ME!
     *
     * @param title1 DOCUMENT ME!
     * @param l1 DOCUMENT ME!
     * @param title2 DOCUMENT ME!
     * @param l2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static CommandPanel createPanel(String title1, ActionListener l1, String title2,
        ActionListener l2) {
        CommandPanel p = new CommandPanel(true);
        p.addComponent(title1, l1);
        p.addComponent(title2, l2);

        return p;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param gap DOCUMENT ME!
     */
    public void insert(JComponent c, int gap) {
        if (gap > 0) {
            buttonsPanel.add(Box.createHorizontalStrut(gap), 0);
        }

        buttonsPanel.add(c, 0);
    }

    /**
     * DOCUMENT ME!
     */
    public void removeSeparator() {
        remove(this.separator);
    }

    /**
     * DOCUMENT ME!
     *
     * @param title1 DOCUMENT ME!
     * @param l1 DOCUMENT ME!
     * @param title2 DOCUMENT ME!
     * @param l2 DOCUMENT ME!
     * @param title3 DOCUMENT ME!
     * @param l3 DOCUMENT ME!
     * @param title4 DOCUMENT ME!
     * @param l4 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static CommandPanel createPanel(String title1, ActionListener l1, String title2,
        ActionListener l2, String title3, ActionListener l3, String title4, ActionListener l4) {
        CommandPanel p = new CommandPanel(true);
        p.addComponent(title1, l1);
        p.addComponent(title2, l2);
        p.addComponent(title3, l3);
        p.addComponent(title4, l4);

        return p;
    }
}
