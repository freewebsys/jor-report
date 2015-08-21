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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class ComboStyleChooser extends DropDownComponent {
    /**
     * Creates a new ComboColorChooser object.
     *
     * @param enabler DOCUMENT ME!
     */
    public ComboStyleChooser() {
        super(new BorderLabel(BorderLabel.SHOW_STYLE), true);

        ((BorderLabel) active_comp).addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    actionPerformed(null);
                }
            });
    }

    protected JComponent createPopupComponent() {
        StyleSelectionPanel panel = new StyleSelectionPanel();
        panel.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent e) {
                    if ("lastStyle".equals(e.getPropertyName())) {
                        hidePopup();
                        ((BorderLabel) active_comp).setStyle((String) e.getNewValue());
                    }
                }
            });

        return panel;
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public String getStyle() {
        return ((BorderIcon) ((BorderLabel) active_comp).getIcon()).getStyle();
    }

    /**
     * DOCUMENT ME!
     *
     * @param style DOCUMENT ME!
     */
    public void setStyle(String style) {
        ((BorderIcon) ((BorderLabel) active_comp).getIcon()).setStyle(style);
    }
    public static void main(String[] args) {
        JDialog d = new JDialog();
        d.getContentPane().add(new ComboStyleChooser());
        d.pack();
        d.show();
    }
}


class StyleSelectionPanel extends JPanel {
    /**
     * Creates a new ColorSelectionPanel object.
     */
    public StyleSelectionPanel() {
        JPanel p = new JPanel(new GridBagLayout());

    /*    ActionListener listener = new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    BorderIcon icon = (BorderIcon) ((BorderButton) evt.getSource()).getIcon();
                    selectStyle(icon.getStyle());
                }
            };*/
        MouseListener listener2=new MouseAdapter()
        {

			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				  BorderIcon icon = (BorderIcon) ((BorderButton) e.getSource()).getIcon();
                  selectStyle(icon.getStyle());
			}

			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				JLabel stylelabel=(JLabel) e.getSource();
				//stylelabel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(Color.WHITE, 1), BorderFactory.createEtchedBorder()));
				stylelabel.setBorder(BorderFactory.createEtchedBorder());
			}

			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				JLabel stylelabel=(JLabel) e.getSource();
				stylelabel.setBorder(BorderFactory.createEmptyBorder());
			}
        	
        };
        BorderButton button = new BorderButton(BorderButton.SHOW_STYLE);
        button.setStyle("solid");
       // button.addActionListener(listener);
         button.addMouseListener(listener2);
        GridBagConstraints c = new GridBagConstraints();
        c.weightx = 100;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = GridBagConstraints.REMAINDER;

        p.add(button, c);

        button = new BorderButton(BorderButton.SHOW_STYLE);
        button.setStyle("dashed");
       // button.addActionListener(listener);
        button.addMouseListener(listener2);
        p.add(button, c);

        button = new BorderButton(BorderButton.SHOW_STYLE);
        button.setStyle("dotted");
        //button.addActionListener(listener);
        button.addMouseListener(listener2);
        p.add(button, c);

        add(p);
        
        
    }

    /**
     * DOCUMENT ME!
     *
     * @param newColor DOCUMENT ME!
     */
    public void selectStyle(String style) {
        firePropertyChange("lastStyle", null, style);
    }

    /**
     * DOCUMENT ME!
     *
     * @param style DOCUMENT ME!
     */
    public void setStyle(String style) {
        for (int i = 0; i < this.getComponentCount(); i++) {
            BorderButton c = (BorderButton) this.getComponent(i);
            BorderIcon icon = (BorderIcon) c.getIcon();

            if (style.equals(icon.getStyle())) {
                c.requestFocus();
            }
        }
    }
    
   
}
