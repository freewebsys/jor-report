package jatools.designer.toolbox;

import jatools.designer.App;
import jatools.swingx.ColorIcon;
import jatools.util.Util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class ColorSelectionPanel extends JPanel {
    private JButton transparent;

    

    /**
     * Creates a new ColorSelectionPanel object.
     */
    public ColorSelectionPanel(boolean enabledtransparent, boolean empty) {
        setLayout(new GridBagLayout());

        ActionListener color_listener = new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    select(((JButton) evt.getSource()).getBackground());
                }
            };

        MouseListener color_listener2 = new MouseListener() {
                public void mouseClicked(MouseEvent e) {
                    // TODO Auto-generated method stub
                    select(((JLabel) e.getSource()).getBackground());
                }

                public void mouseEntered(MouseEvent e) {
                    // TODO Auto-generated method stub
                    JLabel colorlabel = (JLabel) e.getSource();
                    //colorlabel.setBorder(BorderFactory.createLineBorder(Color.black, 1));
                    colorlabel.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.WHITE, 1),
                            BorderFactory.createEtchedBorder()));

                    //colorlabel.setBorder(BorderFactory.createEtchedBorder());
                }

                public void mouseExited(MouseEvent e) {
                    // TODO Auto-generated method stub
                    JLabel colorlabel = (JLabel) e.getSource();
                    //colorlabel.setBorder(BorderFactory.createLineBorder(Color.white, 1));
                    colorlabel.setBorder(BorderFactory.createRaisedBevelBorder());
                }

                public void mousePressed(MouseEvent e) {
                    // TODO Auto-generated method stub
                }

                public void mouseReleased(MouseEvent e) {
                    // TODO Auto-generated method stub
                }
            };

        Color[] colors = new Color[12];
        colors[0] = Color.white;
        colors[1] = Color.black;
        colors[2] = Color.blue;
        colors[3] = Color.cyan;
        colors[4] = Color.gray;
        colors[5] = Color.green;
        colors[6] = Color.lightGray;
        colors[7] = Color.magenta;
        colors[8] = Color.orange;
        colors[9] = Color.pink;
        colors[10] = Color.red;
        colors[11] = Color.yellow;

        int[] values = new int[] {
                0,
                128,
                192,
                255
            };

        int index = 0;
        Color[] colors8x8 = new Color[64];

        for (int r = 0; r < values.length; r++) {
            for (int g = 0; g < values.length; g++) {
                for (int b = 0; b < values.length; b++) {
                    colors8x8[index] = new Color(values[r], values[g], values[b]);
                    index++;
                }
            }
        }

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.insets = new Insets(1, 1, 1, 1);

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                gbc.gridx = j;
                gbc.gridy = i;

                JLabel button = new ColorButton(colors8x8[j + (i * 8)]);
                button.setOpaque(true);
                //                button.setForeground(colors8x8[j + (i * 8)]);
                p.add(button, gbc);
                button.addMouseListener(color_listener2);
            }
        }

        gbc = new GridBagConstraints();
        gbc.gridwidth = gbc.REMAINDER;
        gbc.insets = new Insets(0, 1, 1, 1);
        gbc.fill = gbc.BOTH;
        gbc.weightx = 10;
        add(p, gbc);

        p = new JPanel(new GridBagLayout());

        transparent = new JButton(App.messages.getString("res.13"), new ColorIcon(10, 10));
        transparent.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    select(null);
                }
            });

        Dimension dim = new Dimension(50, 22);
        transparent.setSize(dim);
        transparent.setPreferredSize(dim);
        transparent.setMinimumSize(dim);
        transparent.setMargin(new Insets(0, 0, 0, 0));
        transparent.setVerticalTextPosition(JButton.CENTER);
        transparent.setDisabledIcon(Util.getIcon("/jatools/icons/transparentdisabled.gif"));
        transparent.setFont(new Font("DialogInput", 0, 12));

        transparent.setEnabled(enabledtransparent);
        gbc.gridwidth = 1;
        p.add(transparent, gbc);

        JButton more = new JButton(App.messages.getString("res.286"));
        gbc.gridwidth = gbc.REMAINDER;
        p.add(more, gbc);
        dim = new Dimension(50, 22);
        more.setSize(dim);
        more.setPreferredSize(dim);
        more.setMinimumSize(dim);
        more.setMargin(new Insets(0, 0, 0, 0));
        more.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    moreSelect();
                }
            });

        
        if (empty) {
            p.add(new JSeparator(), gbc);

            JButton empty2 = new JButton(App.messages.getString("res.287"));
            p.add(empty2, gbc);
            dim = new Dimension(100, 22);
            empty2.setSize(dim);
            empty2.setPreferredSize(dim);
            empty2.setMinimumSize(dim);
            empty2.setMargin(new Insets(0, 0, 0, 0));
            empty2.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent evt) {
                    	select(ColorLabel.EMPTY);
                    }
                });
        }

        add(p, gbc);
    }

    /**
     * Creates a new ColorSelectionPanel object.
     */
    public void setTransparentEnabled(boolean enabled) {
        this.transparent.setEnabled(enabled);
    }

    void moreSelect() {
        DropDownComponent ddc = (DropDownComponent) this.getClientProperty("ddc");
        ddc.locked = true;

        Color color = JColorChooser.showDialog(this.getTopLevelAncestor(), App.messages.getString("res.12"), null);
        ddc.locked = false;

        if (color != null) {
            select(color);
        } else {
            cancelSelect();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param newColor DOCUMENT ME!
     */
    public void select(Color newColor) {
        firePropertyChange(DropDownComponent.NEW_VALUE, null, newColor);
    }

    /**
     * DOCUMENT ME!
     */
    public void cancelSelect() {
        firePropertyChange(DropDownComponent.CANCEL_VALUE, null, Color.RED);
    }
}
