package jatools.designer.toolbox;

import jatools.core.view.Border;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.Icon;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class BorderIcon implements Icon {
    Border border;
    Color color = Color.BLACK;
    float thickness = 1;
    String style = "solid";
    private int width;
    private int height;

    /**
     * Creates a new ColorLabel object.
     */
    public BorderIcon(int w, int h) {
        this.width = w;
        this.height = h;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getColor() {
        return color;
    }

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     */
    public void setColor(Color color) {
        this.color = color;
        this.border = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getStyle() {
        return style;
    }

    /**
     * DOCUMENT ME!
     *
     * @param style DOCUMENT ME!
     */
    public void setStyle(String style) {
        this.style = style;
        this.border = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getThickness() {
        return thickness;
    }

    /**
     * DOCUMENT ME!
     *
     * @param thickness DOCUMENT ME!
     */
    public void setThickness(float thickness) {
        this.thickness = thickness;
        this.border = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getIconHeight() {
        return height;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getIconWidth() {
        return width;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param g DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void paintIcon(Component c, Graphics g, int x, int y) {
        if (border == null) {
        	
        	
            border = new Border("border-top:" + this.getThickness() + " " + this.getStyle() +
                    " black");
        }

        border.paint((Graphics2D) g,
            new Rectangle(x, y + (this.getIconHeight() / 2), this.getIconWidth() - 10, 1));
    }
}
