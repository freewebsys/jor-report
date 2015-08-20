package jatools.designer.property.editor;



import jatools.core.view.Border;
import jatools.core.view.BorderStyle;
import jatools.designer.App;
import jatools.designer.Main;
import jatools.designer.toolbox.ComboColorChooser;
import jatools.designer.toolbox.ComboStyleChooser;
import jatools.designer.toolbox.ComboThicknessChooser;
import jatools.swingx.Chooser;
import jatools.swingx.CommandPanel;
import jatools.swingx.Icon25x25Button;
import jatools.swingx.SwingUtil;
import jatools.util.Util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class BorderPropertyEditor extends JDialog implements Chooser {
    private final static int LEFT = 0;
    private final static int RIGHT = 1;
    private final static int TOP = 2;
    private final static int BOTTOM = 3;
    final BorderPanel borderPanel = new BorderPanel();
    final ComboStyleChooser styleChooser = new ComboStyleChooser();
    JToggleButton[] buttons = new JToggleButton[4];
    final ComboColorChooser colorChooser = new ComboColorChooser(false);
    final ComboThicknessChooser thicknessChooser = new ComboThicknessChooser();
    boolean done = false;
    Border result;

    /**
     * Creates a new BorderPropertyEditor object.
     */
    public BorderPropertyEditor() {
        super(Main.getInstance(), App.messages.getString("res.331"), true);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        JPanel centerPanel = new JPanel();

        thicknessChooser.setThickness(1);
        styleChooser.setStyle("solid");
        colorChooser.setColor(Color.black);

        JToggleButton topButton = buttons[TOP] = new JToggleButton(Util.getIcon(
                        "/jatools/icons/bordertop2.gif"));
        JToggleButton leftButton = buttons[LEFT] = new JToggleButton(Util.getIcon(
                        "/jatools/icons/borderleft2.gif"));
        JToggleButton bottomButton = buttons[BOTTOM] = new JToggleButton(Util.getIcon(
                        "/jatools/icons/borderbottom2.gif"));
        JToggleButton rightButton = buttons[RIGHT] = new JToggleButton(Util.getIcon(
                        "/jatools/icons/borderright2.gif"));
        JButton allButton = new Icon25x25Button(Util.getIcon("/jatools/icons/borderall2.gif"));
        allButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    allButton();
                }
            });

        JButton clearAllButton = new Icon25x25Button(Util.getIcon(
                    "/jatools/icons/borderclear2.gif"));

        clearAllButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    borderPanel.setBorder2(null);
                    borderPanel.repaint();
                }
            });

        borderPanel.setPreferredSize(new Dimension(100, 100));

        ActionListener al = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    hit(Integer.parseInt(e.getActionCommand()));
                }
            };

        topButton.setActionCommand(TOP + "");
        topButton.setPreferredSize(new Dimension(25, 25));
        topButton.addActionListener(al);
        leftButton.setActionCommand(LEFT + "");
        leftButton.setPreferredSize(new Dimension(25, 25));
        leftButton.addActionListener(al);

        bottomButton.setActionCommand(BOTTOM + "");
        bottomButton.setPreferredSize(new Dimension(25, 25));
        bottomButton.addActionListener(al);

        rightButton.setActionCommand(RIGHT + "");
        rightButton.setPreferredSize(new Dimension(25, 25));
        rightButton.addActionListener(al);

        centerPanel.setLayout(new GridBagLayout());

        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(1, 1, 1, 1);

        c.insets = new Insets(1, 2, 1, 2);

        borderPanel.setBackground(Color.WHITE);

        c.gridwidth = 3;
        centerPanel.add(Box.createHorizontalBox(), c);
        c.gridwidth = 1;

        centerPanel.add(topButton, c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        centerPanel.add(Box.createHorizontalBox(), c);
        c.gridwidth = 1;

        centerPanel.add(allButton, c);
        centerPanel.add(clearAllButton, c);

        c.insets = new Insets(1, 20, 1, 2);

        centerPanel.add(leftButton, c);
        c.insets = new Insets(1, 2, 1, 2);
        centerPanel.add(borderPanel, c);
        c.gridwidth = GridBagConstraints.REMAINDER;

        centerPanel.add(rightButton, c);

        c.gridx = 3;
        c.gridwidth = 1;

        centerPanel.add(bottomButton, c);

        topPanel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.insets = new Insets(1, 2, 1, 2);
        c.gridwidth = 1;
        c.gridheight = 1;
        topPanel.add(new JLabel(App.messages.getString("res.332")), c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        topPanel.add(thicknessChooser, c);
        c.gridwidth = 1;
        c.gridheight = 1;
        topPanel.add(new JLabel(App.messages.getString("res.172")), c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        topPanel.add(styleChooser, c);
        c.gridwidth = 1;
        c.gridheight = 1;
        topPanel.add(new JLabel(App.messages.getString("res.333")), c);
        c.gridwidth = GridBagConstraints.REMAINDER;
        topPanel.add(colorChooser, c);
        c.weighty = 100;
        topPanel.add(Box.createVerticalBox(), c);

        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        ActionListener oklistener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    done = true;

                    result = borderPanel.getBorder2();
                    hide();
                }
            };

        ActionListener cancellistener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    done = false;
                    hide();
                }
            };

        CommandPanel bottomPanel = CommandPanel.createPanel(oklistener, cancellistener);
        bottomPanel.addComponent(App.messages.getString("res.23"),
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    done = true;
                    hide();
                }
            });

        JPanel cPanel = new JPanel(new BorderLayout());
        cPanel.add(topPanel, "West");
        cPanel.add(centerPanel, "Center");

        contentPane.add(cPanel, "Center");
        contentPane.add(bottomPanel, "South");

        Dimension size = new Dimension(120, 25);
        SwingUtil.setSize(this.thicknessChooser, size);
        SwingUtil.setSize(this.styleChooser, size);

        SwingUtil.setSize(this.colorChooser, size);

        this.borderPanel.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals("border2")) {
                        validateButtons();
                    } else if (evt.getPropertyName().equals("hitside")) {
                        hit(((Integer) evt.getNewValue()).intValue());
                    }
                }
            });

        setSize(new Dimension(420, 300));
    }

    private void validateButtons() {
        BorderStyle[] styles = getStyles(borderPanel.getBorder2());

        for (int i = 0; i < styles.length; i++) {
            buttons[i].setSelected(styles[i] != null);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param side DOCUMENT ME!
     */
    public void hit(int side) {
        float thickness = thicknessChooser.getThickness();
        String style = styleChooser.getStyle();
        Color color = colorChooser.getColor();
        BorderStyle[] styles = getStyles(borderPanel.getBorder2());

        if (styles[side] == null) {
            styles[side] = new BorderStyle(thickness, style, color);
        } else {
            styles[side] = null;
        }

        Border newBorder = Border.create(styles[TOP], styles[LEFT], styles[BOTTOM], styles[RIGHT]);
        borderPanel.setBorder2(newBorder);
        borderPanel.repaint();
    }

    private void allButton() {
        float thickness = thicknessChooser.getThickness();
        String style = styleChooser.getStyle();
        Color color = colorChooser.getColor();
        BorderStyle _style = new BorderStyle(thickness, style, color);

        borderPanel.setBorder2(Border.create(_style, _style, _style, _style));
        borderPanel.repaint();
    }

    private BorderStyle[] getStyles(Border border) {
        BorderStyle[] styles = new BorderStyle[4];

        if (border != null) {
            styles[LEFT] = border.getLeftStyle();
            styles[RIGHT] = border.getRightStyle();
            styles[TOP] = border.getTopStyle();
            styles[BOTTOM] = border.getBottomStyle();
        }

        return styles;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
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
        this.borderPanel.setBorder2((Border) value);
        this.setLocationRelativeTo(owner);
        show();

        return this.done;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue() {
        return result;
    }
}
