package jatools.designer.toolbox;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class BorderButton extends JLabel/**JButton*/ {
    final static int SHOW_STYLE = 1;
    final static int SHOW_THICKNESS = 2;
    final static int SHOW_NOTHING = 0;
    BorderIcon icon = new BorderIcon(60, 20);
    private int showtext;

    /**
     * Creates a new ColorLabel object.
     */
    public BorderButton(int showtext) {
        this.setFont(new Font("Dialog", 0, 10));
        this.setIcon(icon);
        this.setHorizontalAlignment(LEFT);
        this.setBorder( null);
       // this.setMargin(new Insets(0, 3, 0, 3));

        this.showtext = showtext;
        this.setThickness(1);
        this.setStyle("solid");
        
        this.setPreferredSize(new Dimension(120, 15));
        this.setMinimumSize(getPreferredSize());
        this.setMaximumSize(getPreferredSize());
        
    }

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     */
    public void setColor(Color color) {
        this.icon.setColor(color);
    }

    /**
     * DOCUMENT ME!
     *
     * @param style DOCUMENT ME!
     */
    public void setStyle(String style) {
        if (this.showtext == SHOW_STYLE) {
            this.setText(style);
        }

        this.icon.setStyle(style);
    }

    /**
     * DOCUMENT ME!
     *
     * @param thickness DOCUMENT ME!
     */
    public void setThickness(float thickness) {
        if (this.showtext == SHOW_THICKNESS) {
        	this.setText(thickness + ((thickness == (int) thickness) ? "px" : "pt"));
        }

        this.icon.setThickness(thickness);
    }
}
