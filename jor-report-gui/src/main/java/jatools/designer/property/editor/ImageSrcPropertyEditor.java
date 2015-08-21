package jatools.designer.property.editor;

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
 * @version $Revision: 1.2 $
  */
public class ImageSrcPropertyEditor extends JDialog implements Chooser {
    final static String[] typeValues = {
            "builtin",
            "url",
            "file",
            "classpath",
            "field"
        };
    final static String[] typePropmts = {
            App.messages.getString("res.308"),
            App.messages.getString("res.309"),
            App.messages.getString("res.310"),
            App.messages.getString("res.311"),
            App.messages.getString("res.312")
        };
    JComboBox typeCombo = null;
    JTextField pathText = null;
    JButton moreButton = null;
    private boolean done;
    private String base64;
    private String result;

    /**
     * Creates a new BackgroundImageEditor object.
     */
    public ImageSrcPropertyEditor(boolean clear) {
        super(Main.getInstance(), App.messages.getString("res.356"), true);
        pathText = new JTextField(13);

        typeCombo = new JComboBox(typePropmts);
        typeCombo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    enabledMoreButton();
                }
            });

        SwingUtil.setSize(typeCombo, new Dimension(100, 23));

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
        //   label.setPreferredSize(new Dimension(30, 23));
        //        gbc.gridwidth = 1;
        //        p.add(new JLabel(), gbc);
        //        gbc.gridwidth = GridBagConstraints.REMAINDER;
        
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
        gbc.weighty = 100;
        p.add(Box.createVerticalGlue(), gbc);

        p.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.getContentPane().add(p);

        ActionListener oklistener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    done = true;
                    result = getImageSrc();

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

        if (clear) {
            commandPanel.addComponent(App.messages.getString("res.23"),
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        done = true;
                        hide();
                    }
                });
        }

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

                        // scratch array to allow reading in blocks instead of bytes...
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
     * @param args DOCUMENT ME!
     */

    /**
     * DOCUMENT ME!
     *
     * \"[^\"]*\"
     *
     * @param owner DOCUMENT ME!
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean showChooser(JComponent owner, Object value) {
        this.done = false;
        this.result = null;
        this.setImageSrc((String) value);
        this.setLocationRelativeTo(owner);
        show();

        return this.done;
    }

    private void setImageSrc(String src) {
        this.pathText.setText(null);
        this.typeCombo.setSelectedIndex(0);

        if (src != null) {
            String type = src.substring(0, src.indexOf(":"));
            int index = ArrayUtils.indexOf(typeValues, type);
            this.typeCombo.setSelectedIndex(index);

            if (index == 0) {
                this.pathText.setText(App.messages.getString("res.330"));
                this.base64 = src.substring(Image.SOURCE_BUILT_IN.length() + 1);
            } else {
                this.pathText.setText(src.substring(src.indexOf(":") + 1));
            }
        }
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
    public String getImageSrc() {
        String imageSrc = null;

        if (this.typeCombo.getSelectedIndex() == 0) {
            imageSrc = base64;
        } else if (typeCombo.getSelectedIndex() != -1) {
            imageSrc = pathText.getText();
        }

        if ((imageSrc != null) && (imageSrc.trim().length() > 0)) {
            StringBuffer css = new StringBuffer();

            css.append(typeValues[this.typeCombo.getSelectedIndex()]);
            css.append(":");

            css.append(imageSrc);

            return css.toString();
        } else {
            return null;
        }
    }
}
