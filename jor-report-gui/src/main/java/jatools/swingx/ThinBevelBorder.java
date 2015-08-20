package jatools.swingx;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;


public class ThinBevelBorder implements Border {
    public static final int RAISED = 0;
    public static final int LOWERRED = 1;
    int style = RAISED;

    /**
     * Creates a new ZThinBevelBorder object.
     *
     * @param style DOCUMENT ME!
     */
    public ThinBevelBorder(int style) {
        this.style = style;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param g DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Color oldColor = g.getColor();
        g.setColor(Color.LIGHT_GRAY);
        g.draw3DRect(x, y, width - 1, height - 1, style == RAISED);
        g.setColor(oldColor);
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Insets getBorderInsets(Component c) {
        return new Insets(1, 1, 1, 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isBorderOpaque() {
        return false;
    }
}
