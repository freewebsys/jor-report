package jatools.designer.toolbox;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class ComboThicknessChooser extends DropDownComponent {
    /**
     * Creates a new ComboColorChooser object.
     *
     * @param enabler DOCUMENT ME!
     */
    public ComboThicknessChooser() {
        super(new BorderLabel(BorderLabel.SHOW_THICKNESS), true);

        ((BorderLabel) active_comp).addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    actionPerformed(null);
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getThickness() {
        return ((BorderIcon) ((BorderLabel) active_comp).getIcon()).getThickness();
    }

    /**
     * DOCUMENT ME!
     *
     * @param thickness DOCUMENT ME!
     */
    public void setThickness(float thickness) {
        ((BorderIcon) ((BorderLabel) active_comp).getIcon()).setThickness(thickness);
    }

    protected JComponent createPopupComponent() {
        ThicknessSelectionPanel panel = new ThicknessSelectionPanel();
        panel.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent e) {
                    if ("lastThickness".equals(e.getPropertyName())) {
                        hidePopup();
                        ((BorderLabel) active_comp).setThickness2(((Float) e.getNewValue()).floatValue());
                    }
                }
            });

        return panel;
    }
}


class ThicknessSelectionPanel extends JPanel {
    /**
     * Creates a new ColorSelectionPanel object.
     */
    public ThicknessSelectionPanel() {
        JPanel p = new JPanel(new GridBagLayout());

        /*   ActionListener listener = new ActionListener() {
                   public void actionPerformed(ActionEvent evt) {
                       BorderIcon icon = (BorderIcon) ((BorderButton) evt.getSource()).getIcon();
                       selectThickness(icon.getThickness());
                   }
               };*/
        MouseListener listener2 = new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    // TODO Auto-generated method stub
                    BorderIcon icon = (BorderIcon) ((BorderButton) e.getSource()).getIcon();
                    selectThickness(icon.getThickness());
                }

                public void mouseEntered(MouseEvent e) {
                    // TODO Auto-generated method stub
                    JLabel thicknessLabel = (JLabel) e.getSource();
                    //thicknessLabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 1), BorderFactory.createEtchedBorder()));
                    thicknessLabel.setBorder(BorderFactory.createEtchedBorder());

                    //thicknessLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
                }

                public void mouseExited(MouseEvent e) {
                    // TODO Auto-generated method stub
                    JLabel thicknessLabel = (JLabel) e.getSource();
                    thicknessLabel.setBorder(BorderFactory.createEmptyBorder());
                }
            };

        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 100;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.REMAINDER;

        for (int i = 0; i < 9; i++) {
            BorderButton button = new BorderButton(BorderButton.SHOW_THICKNESS);
            button.setThickness((i == 0) ? 0.5f : i);
            // button.addActionListener(listener);
            button.addMouseListener(listener2);
            p.add(button, c);
        }

        add(p);
    }

    /**
     * DOCUMENT ME!
     *
     * @param newColor DOCUMENT ME!
     */
    public void selectThickness(float thickness) {
    	//float thick = thickness == 0? 0.5f:thickness;
        firePropertyChange("lastThickness", null, new Float(thickness));
    }

    /**
     * DOCUMENT ME!
     *
     * @param style DOCUMENT ME!
     */

    //    public void setStyle(String style) {
    //        for (int i = 0; i < this.getComponentCount(); i++) {
    //        	BorderButton c = (BorderButton) this.getComponent(i);
    //            BorderIcon icon = (BorderIcon)c.getIcon();
    //            if (style.equals(icon.getThickness() )) {
    //                c.requestFocus();
    //            }
    //        }
    //    }
}
