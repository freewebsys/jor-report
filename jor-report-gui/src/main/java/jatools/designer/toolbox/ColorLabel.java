package jatools.designer.toolbox;

import jatools.swingx.ColorIcon;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JLabel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class ColorLabel extends JLabel {
    public static Color EMPTY = new Color(1, 1, 1);
    ColorIcon icon = new ColorIcon(10, 10);

    /**
     * Creates a new ColorLabel object.
     */
    public ColorLabel() {
        this.setIcon(icon);
        this.setFont(new Font("Dialog", 0, 10));
    }

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     */
    public void setColor(Color color) {
        if (color == EMPTY) {
            setIcon(null);
            setText(null);
            this.icon.setColor(color);
        } else {
            this.setIcon(this.icon);
            this.icon.setColor(color);

            if (color != null) {
                setText("RGB[" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() +
                    "]"); // //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
            } else {
                setText(null);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getColor() {
        return icon.getColor();
    }
}
