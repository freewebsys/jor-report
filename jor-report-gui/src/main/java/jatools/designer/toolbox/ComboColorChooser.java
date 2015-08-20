package jatools.designer.toolbox;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JDialog;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class ComboColorChooser extends DropDownComponent {
	boolean transparent ;
	boolean empty;
    /**
     * Creates a new ComboColorChooser object.
     *
     * @param enabler DOCUMENT ME!
     */
    public ComboColorChooser(boolean transparent) {
        super(new ColorLabel(), true);

        ((ColorLabel) active_comp).setColor(Color.black);
        ((ColorLabel) active_comp).addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    actionPerformed(null);
                }
            });
        
        this.transparent = transparent;
        
    }

    protected JComponent createPopupComponent() {
        ColorSelectionPanel panel = new ColorSelectionPanel(transparent,empty);

        panel.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent e) {
                    if (NEW_VALUE == e.getPropertyName()) {
                        ((ColorLabel) active_comp).setColor((Color) e.getNewValue());
                        hidePopup();
                        fireActionListener(0);
                    } else if (CANCEL_VALUE == e.getPropertyName()) {
                        hidePopup();
                        fireActionListener(0);
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
    public Color getColor() {
        return ((ColorLabel) active_comp).getColor();
    }

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     */
    public void setColor(Color color) {
        ((ColorLabel) active_comp).setColor(color);
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        JDialog d = new JDialog();
        d.getContentPane().add(new ComboColorChooser(false));
        d.pack();
        d.show();
    }

	public void setEmpty(boolean empty) {
		this.empty = empty;
	}
}
