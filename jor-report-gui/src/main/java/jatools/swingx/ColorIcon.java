package jatools.swingx;

import jatools.util.Util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ColorIcon implements Icon {
    final static Icon TRANSPARENT = Util.getIcon("/jatools/icons/transparent.gif");
    int width;
    int height;
    Color color;

    /**
     * Creates a new ColorIcon object.
     *
     * @param d DOCUMENT ME!
     */
    public ColorIcon(Dimension d) {
        this(d.width, d.height);
    }

    /**
     * Creates a new ColorIcon object.
     *
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public ColorIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param g1 DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void paintIcon(Component c, Graphics g1, int x, int y) {
        Graphics g = g1.create();

        if (color != null) {
            g.setColor(color);
            g.fillRect(x, y, getIconWidth(), getIconHeight());
        } else {
            TRANSPARENT.paintIcon(c, g1, x, y);
        }

        g.setColor(Color.black);
        g.drawRect(x, y, getIconWidth(), getIconHeight());
        g.dispose();
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
     * @return DOCUMENT ME!
     */
    public int getIconHeight() {
        return this.height;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getIconWidth() {
        return this.width;
    }
}
