package jatools.designer;

import jatools.component.Page;
import jatools.designer.property.editor.BorderPanel;
import jatools.designer.wizard.BuilderContext;
import jatools.swingx.CommandPanel;
import jatools.swingx.SpinEditor;
import jatools.swingx.TitledSeparator;
import jatools.util.Util;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Paper;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PageSetupPanel extends JPanel {
    public static final int INCH = Toolkit.getDefaultToolkit().getScreenResolution();
    public static final double DOTS_PER_MM = (double) INCH / (double) 25.4;
    public static final double DEFAULT_MARGIN = 4.3d;
    public static Object[][] sizes = {
            { "Letter", new Dimension(216, 279) },
            { "Legal", new Dimension(216, 356) },
            { "Executive", new Dimension(184, 267) },
            { "A2", new Dimension(420, 594) },
            { "A3", new Dimension(297, 420) },
            { "A4", new Dimension(210, 297) },
            { "B4", new Dimension(250, 354) },
            { "A5", new Dimension(148, 210) },
            { "B5 (JIS)", new Dimension(182, 257) },
            { "Folio", new Dimension(216, 330) },
            { App.messages.getString("res.141"), new Dimension(105, 241) },
            { App.messages.getString("res.142"), new Dimension(110, 220) },
            { App.messages.getString("res.143"), new Dimension(162, 229) },
            { App.messages.getString("res.144"), new Dimension(114, 162) },
            { App.messages.getString("res.145"), new Dimension(176, 250) },
            { "Envelope Monarch", new Dimension(98, 191) },
            { "A6", new Dimension(105, 148) },
            { App.messages.getString("res.146"), new Dimension(0, 0) }
        };
    private JPanel iconPanel;
    private CardLayout card;
    SpinEditor widthText = new SpinEditor(800.0);
    SpinEditor heightText = new SpinEditor(700.0);
    SpinEditor topText = new SpinEditor(DEFAULT_MARGIN);
    SpinEditor leftText = new SpinEditor(DEFAULT_MARGIN);
    SpinEditor rightText = new SpinEditor(DEFAULT_MARGIN);
    SpinEditor bottomText = new SpinEditor(DEFAULT_MARGIN);
    JRadioButton portraitButton = new JRadioButton(App.messages.getString("res.147"));
    JRadioButton landscapeButton = new JRadioButton(App.messages.getString("res.148"));
    PageFormat format;
    JDialog dialog;
    JComboBox typeComboBox;
    JCheckBox headButton = new JCheckBox(App.messages.getString("res.149"));
    JCheckBox tailButton = new JCheckBox(App.messages.getString("res.150"));

    /**
     * Creates a new PageSetupPanel object.
     */
    public PageSetupPanel() {
        super(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        gbc.weightx = 1;
        gbc.anchor = gbc.WEST;

        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(new TitledSeparator(App.messages.getString("res.151")), gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 20, 15, 2);
        add(getPaperPanel(), gbc);
        gbc.insets = new Insets(0, 2, 0, 2);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(new TitledSeparator(App.messages.getString("res.152")), gbc);
        gbc.fill = GridBagConstraints.NONE;
        gbc.insets = new Insets(0, 20, 15, 2);
        add(getOrentationPanel(), gbc);
        gbc.insets = new Insets(0, 2, 0, 2);

        gbc.fill = GridBagConstraints.HORIZONTAL;

        add(new TitledSeparator(App.messages.getString("res.153")), gbc);
        gbc.insets = new Insets(0, 20, 15, 2);
        gbc.fill = GridBagConstraints.NONE;
        add(getMarginPanel(), gbc);

        gbc.insets = new Insets(0, 2, 0, 2);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(new TitledSeparator(App.messages.getString("res.154")), gbc);
        gbc.insets = new Insets(0, 20, 15, 2);
        gbc.fill = GridBagConstraints.NONE;
        add(getHeadTailPanel(), gbc);
    }

    private JPanel getPaperPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = gbc.HORIZONTAL;
        gbc.insets = new Insets(2, 0, 2, 0);

        typeComboBox = new JComboBox();

        for (int i = 0; i < sizes.length; i++) {
            typeComboBox.addItem(sizes[i][0]);
        }

        p.add(new JLabel(App.messages.getString("res.155")), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 100;
        gbc.anchor = GridBagConstraints.WEST;
        p.add(typeComboBox, gbc);
        gbc.weightx = 0;
        gbc.gridwidth = 1;
        p.add(Box.createGlue(), gbc);
        p.add(widthText);
        widthText.setPreferredSize(new Dimension(50, 23));
        gbc.gridx = GridBagConstraints.RELATIVE;

        p.add(new JLabel("X"), gbc);
        heightText.setPreferredSize(new Dimension(50, 23));
        p.add(heightText, gbc);

        p.add(new JLabel("mm"), gbc);
        typeComboBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JComboBox cbb = (JComboBox) e.getSource();
                    Dimension d = (Dimension) sizes[cbb.getSelectedIndex()][1];
                    widthText.setValue(d.width);
                    heightText.setValue(d.height);
                }
            });

        typeComboBox.setSelectedItem("A4");

        return p;
    }

    private JPanel getOrentationPanel() {
        card = new CardLayout();
        iconPanel = new JPanel(card);
        iconPanel.add("portrait", new JLabel(Util.getIcon("/jatools/icons/portrait.gif")));
        iconPanel.add("landscape", new JLabel(Util.getIcon("/jatools/icons/landscape.gif")));

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 20);

        ButtonGroup group = new ButtonGroup();
        group.add(portraitButton);
        group.add(landscapeButton);

        portraitButton.setSelected(true);
        p.add(iconPanel, gbc);
        p.add(portraitButton, gbc);
        p.add(landscapeButton, gbc);

        portraitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    card.show(iconPanel, "portrait");
                }
            });

        landscapeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    card.show(iconPanel, "landscape");
                }
            });

        return p;
    }

    private JPanel getHeadTailPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 20);
        p.add(headButton, gbc);
        p.add(tailButton, gbc);

        return p;
    }

    private JPanel getMarginPanel() {
        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel(App.messages.getString("res.156")));
        topPanel.add(topText);
        topPanel.add(new JLabel("mm"));

        JPanel leftPanel = new JPanel();
        leftPanel.add(new JLabel(App.messages.getString("res.157")));
        leftPanel.add(leftText);
        leftPanel.add(new JLabel("mm"));

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new JLabel(App.messages.getString("res.158")));
        bottomPanel.add(bottomText);
        bottomPanel.add(new JLabel("mm"));

        JPanel rightPanel = new JPanel();
        rightPanel.add(new JLabel(App.messages.getString("res.159")));
        rightPanel.add(rightText);
        rightPanel.add(new JLabel("mm"));

        topText.setPreferredSize(new Dimension(45, 23));
        leftText.setPreferredSize(new Dimension(45, 23));
        bottomText.setPreferredSize(new Dimension(45, 23));
        rightText.setPreferredSize(new Dimension(45, 23));

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 1;

        p.add(topPanel, gbc);

        gbc.gridy = 1;
        gbc.gridx = GridBagConstraints.RELATIVE;
        gbc.gridwidth = 1;

        p.add(leftPanel, gbc);

        BorderPanel borderPanel = new BorderPanel();
        borderPanel.setPreferredSize(new Dimension(70, 80));

        p.add(borderPanel, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        p.add(rightPanel, gbc);
        gbc.gridy = 2;
        gbc.gridx = GridBagConstraints.RELATIVE;
        p.add(bottomPanel, gbc);

        return p;
    }

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     * @param f DOCUMENT ME!
     * @param page DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PageFormat showDialog(Frame owner, PageFormat f, Page page) {
        if (f != null) {
            setFormat(f);
        }

        if (page != null) {
            headButton.setSelected(page.getHeader() != null);
            tailButton.setSelected(page.getFooter() != null);
        }

        dialog = new JDialog(owner, App.messages.getString("res.160"), true);

        dialog.getContentPane().add(this, BorderLayout.CENTER);

        ActionListener oklistener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    format = createFormat();
                    dialog.hide();
                }
            };

        ActionListener canellistener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dialog.hide();
                }
            };

        CommandPanel panel = CommandPanel.createPanel(oklistener, canellistener);

        dialog.getContentPane().add(panel, BorderLayout.SOUTH);

        dialog.setSize(440, 480);
        dialog.setLocationRelativeTo(owner);

        dialog.show();

        return format;
    }

    private void setFormat(PageFormat f) {
        Paper p = f.getPaper();

        int left = toCms(p.getImageableX());
        int top = toCms(p.getImageableY());
        int right = toCms(p.getWidth() - p.getImageableWidth() - p.getImageableX());
        int bottom = toCms(p.getHeight() - p.getImageableHeight() - p.getImageableY());

        widthText.setValue(toCms(f.getWidth()));
        heightText.setValue(toCms(f.getHeight()));

        leftText.setValue(left);
        topText.setValue(top);
        rightText.setValue(right);
        bottomText.setValue(bottom);

        int pi = indexOfPage((int) p.getWidth(), (int) p.getHeight());

        if (pi != -1) {
            typeComboBox.setSelectedIndex(pi);
        } else {
            ((Dimension) sizes[sizes.length - 1][1]).setSize((int) toCms(p.getWidth()),
                (int) toCms(p.getHeight()));

            typeComboBox.setSelectedIndex(sizes.length - 1);
        }

        if (f.getOrientation() == PageFormat.PORTRAIT) {
            portraitButton.setSelected(true);
        } else {
            landscapeButton.setSelected(true);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     */
    public void apply(BuilderContext context) {
        PageFormat format = createFormat();
        context.setValue(BuilderContext.PAGE_FORMAT, format);
    }

    private PageFormat createFormat() {
        UIPageFormat f = new UIPageFormat();

        f.portrait = portraitButton.isSelected();
        f.width = widthText.doubleValue();
        f.height = heightText.doubleValue();
        f.top = topText.doubleValue();

        f.left = leftText.doubleValue();
        f.bottom = bottomText.doubleValue();
        f.right = rightText.doubleValue();

        return f.toAwtFormat();
    }

    static int toDots(double mm) {
        try {
            return (int) ((DOTS_PER_MM * mm));
        } catch (NumberFormatException e) {
        }

        return 0;
    }

    int indexOfPage(int width, int height) {
        for (int i = 0; i < sizes.length; i++) {
            Dimension size = (Dimension) sizes[i][1];

            if ((toDots(size.width) == width) && (toDots(size.height) == height)) {
                return i;
            }
        }

        return -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param dots DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int toCms(double dots) {
        return (int) Math.round(dots / DOTS_PER_MM);
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        System.out.println(toDots(1000));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasHeader() {
        return headButton.isSelected();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasFooter() {
        return tailButton.isSelected();
    }
}
