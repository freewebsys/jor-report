package jatools.designer.property.editor;




import jatools.component.BackgroundImageStyle;
import jatools.component.Image;
import jatools.designer.App;
import jatools.designer.Main;
import jatools.swingx.Chooser;
import jatools.swingx.CommandPanel;
import jatools.swingx.ImageFileChooser;
import jatools.swingx.SwingUtil;
import jatools.swingx.TitledSeparator;
import jatools.util.Base64Util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.lang.ArrayUtils;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class BackgroundImagePropertyEditor extends JDialog implements Chooser {
    final static String[] typeValues = { "builtin", "url", "file", "classpath", "field" };
    final static String[] typePropmts = { App.messages.getString("res.308"), App.messages.getString("res.309"), App.messages.getString("res.310"), App.messages.getString("res.311"), App.messages.getString("res.312") };
    final static String[] alignValues = { "0%", "50%", "100%" };
    final static String[] halignPrompts = { App.messages.getString("res.313"), App.messages.getString("res.314"), App.messages.getString("res.315") };
    final static String[] valignPrompts = { App.messages.getString("res.316"), App.messages.getString("res.314"), App.messages.getString("res.317") };
    final static String[] repeatPrompts = { App.messages.getString("res.318"), App.messages.getString("res.319"), App.messages.getString("res.320"), App.messages.getString("res.321") };
    final static String[] repeatValues = { "no-repeat", "repeat", "repeat-x", "repeat-y" };
    JComboBox typeCombo = null;
    JTextField pathText = null;
    JComboBox hCombo = null;
    JComboBox vCombo = null;
    JComboBox repeatCombo = null;
    JButton moreButton = null;
    private boolean done;
    private String base64;
    private String result;

    /**
     * Creates a new BackgroundImagePropertyEditor object.
     */
    public BackgroundImagePropertyEditor() {
        super(Main.getInstance(), App.messages.getString("res.322"), true);
        pathText = new JTextField(13);

        typeCombo = new JComboBox(typePropmts);
        typeCombo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    enabledMoreButton();
                }
            });

        hCombo = new JComboBox(halignPrompts);
        vCombo = new JComboBox(valignPrompts);
        repeatCombo = new JComboBox(repeatPrompts);

        SwingUtil.setSize(typeCombo, new Dimension(100, 23));

        Dimension combosize = new Dimension(80, 23);
        SwingUtil.setSize(hCombo, combosize);
        SwingUtil.setSize(vCombo, combosize);
        SwingUtil.setSize(repeatCombo, combosize);

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        p.add(new TitledSeparator(App.messages.getString("res.323")), gbc);
        gbc.gridwidth = 1;

        JLabel label = new JLabel(App.messages.getString("res.324"));
        label.setPreferredSize(new Dimension(30, 23));
        p.add(label, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.NONE;
        p.add(typeCombo, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridwidth = 1;
        p.add(new JLabel(App.messages.getString("res.325")), gbc);
        gbc.weightx = 100;

        p.add(this.pathText, gbc);
        gbc.weightx = 0;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        moreButton = new JButton("..");
        moreButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    chooseImageFile();
                }
            });
        SwingUtil.setSize(moreButton, new Dimension(23, 23));

        p.add(moreButton, gbc);
        p.add(Box.createVerticalStrut(20), gbc);
        p.add(new TitledSeparator(App.messages.getString("res.326")), gbc);
        gbc.gridwidth = 1;
        p.add(new JLabel(App.messages.getString("res.327")), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.NONE;
        p.add(hCombo, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridwidth = 1;
        p.add(new JLabel(App.messages.getString("res.328")), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.NONE;
        p.add(vCombo, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        p.add(Box.createVerticalStrut(20), gbc);

        gbc.gridwidth = 1;
        p.add(new JLabel(App.messages.getString("res.329")), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.NONE;
        p.add(repeatCombo, gbc);

        gbc.weighty = 100;
        p.add(Box.createVerticalGlue(), gbc);
        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.getContentPane().add(p);

        ActionListener oklistener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    done = true;
                    result = getBackgroundImage();

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
        setSize(new Dimension(490, 300));
    }

    private void enabledMoreButton() {
        boolean more = (typeCombo.getSelectedIndex() == 0) || (typeCombo.getSelectedIndex() == 2);
        moreButton.setEnabled(more);
        this.base64 = null;
        this.pathText.setText(null);
    }

    protected void chooseImageFile() {
        ImageFileChooser chooser = ImageFileChooser.getSharedInstance(this);

        if (chooser.show()) {
            File f = chooser.getSelectedFile();

            if (f != null) {
                try {
                    if (this.typeCombo.getSelectedIndex() == 0) {
                        FileInputStream in = new FileInputStream(f);

                        byte[] scratch = new byte[in.available()];

                        in.read(scratch);

                        ImageIcon ii = new ImageIcon(f.getAbsolutePath());

                        if (ii != null) {
                            base64 = Base64Util.encode(scratch);

                            pathText.setText(App.messages.getString("res.330"));
                        }
                    } else {
                        pathText.setText(f.getAbsolutePath());
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
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
        this.setBackgroundImageStyle((String) value);
        this.setLocationRelativeTo(owner);
        show();

        return this.done;
    }

    private void setBackgroundImageStyle(String text) {
        this.pathText.setText(null);
        this.typeCombo.setSelectedIndex(0);
        this.hCombo.setSelectedIndex(0);
        this.vCombo.setSelectedIndex(0);
        this.repeatCombo.setSelectedIndex(0);

        if (text != null) {
            BackgroundImageStyle css = new BackgroundImageStyle(text);

            String src = css.getImageFileSrc();

            if (src != null) {
                String type = src.substring(0, src.indexOf(":"));
                int index = ArrayUtils.indexOf(this.typeValues, type);
                this.typeCombo.setSelectedIndex(index);

                if (index == 0) {
                    this.pathText.setText(App.messages.getString("res.330"));
                    this.base64 = src.substring(Image.SOURCE_BUILT_IN.length() + 1);
                } else {
                    this.pathText.setText(src.substring(src.indexOf(":") + 1));
                }
            }

            if (css.getX() != null) {
                this.hCombo.setSelectedIndex(getAlignIndex(css.getX().floatValue()));
            }

            if (css.getY() != null) {
                this.vCombo.setSelectedIndex(getAlignIndex(css.getY().floatValue()));
            }

            if (!css.isRepeatx() && !css.isRepeaty()) {
                this.repeatCombo.setSelectedIndex(0);
            } else if (css.isRepeatx() && css.isRepeaty()) {
                this.repeatCombo.setSelectedIndex(1);
            } else if (css.isRepeatx() && !css.isRepeaty()) {
                this.repeatCombo.setSelectedIndex(2);
            } else if (!css.isRepeatx() && css.isRepeaty()) {
                this.repeatCombo.setSelectedIndex(3);
            }
        }
    }

    private int getAlignIndex(float f) {
        if (f == 0.0) {
            return 0;
        } else if (f == 50) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        new BackgroundImagePropertyEditor().show();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue() {
        return this.result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getBackgroundImage() {
        String imageSrc = null;

        if (this.typeCombo.getSelectedIndex() == 0) {
            imageSrc = base64;
        } else {
            imageSrc = pathText.getText();
        }

        if ((imageSrc != null) && (imageSrc.trim().length() > 0)) {
            StringBuffer css = new StringBuffer();
            css.append(BackgroundImageStyle.BACKGROUND_IMAGE + ":");
            css.append(typeValues[this.typeCombo.getSelectedIndex()]);
            css.append(":");

            css.append(imageSrc + ";");

            String position = alignValues[hCombo.getSelectedIndex()] + " " +
                alignValues[vCombo.getSelectedIndex()];
            css.append(BackgroundImageStyle.BACKGROUND_POSITION + ":" + position + ";");

            css.append(BackgroundImageStyle.BACKGROUND_REPEAT + ":" +
                repeatValues[this.repeatCombo.getSelectedIndex()]);

            return css.toString();
        } else {
            return null;
        }
    }
}
