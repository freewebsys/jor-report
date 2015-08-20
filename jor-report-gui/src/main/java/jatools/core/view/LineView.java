package jatools.core.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;


/**
 * @author   java9
 */
public class LineView extends AbstractView {
    static final long serialVersionUID = 20030716005L;

    /**
     * DOCUMENT ME!
     */
    public static Object[] storedLinePatterns = new Object[6];

    static {
        float j = 1.1f;

        for (int i = 1; i < storedLinePatterns.length; i++, j += 1.0f) {
            float[] dash = {
                    j
                };
            storedLinePatterns[i] = dash;
        }
    }

    private Color foreColor;
    int linePattern;
    float lineSize = 1.0f;
    private Rectangle clip;

    /**
     * DOCUMENT ME!
     * @return   DOCUMENT ME!
     * @uml.property   name="foreColor"
     */
    public Color getForeColor() {
        return foreColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     */
    public void setClip(Rectangle r) {
        this.clip = r;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x0 DOCUMENT ME!
     * @param y0 DOCUMENT ME!
     * @param x1 DOCUMENT ME!
     * @param y1 DOCUMENT ME!
     */
    public void set(int x0, int y0, int x1, int y1) {
        setBounds(new Rectangle(x0, y0, x1 - x0, y1 - y0));
    }

    /**
     * DOCUMENT ME!
     * @param foreColor   DOCUMENT ME!
     * @uml.property   name="foreColor"
     */
    public void setForeColor(Color foreColor) {
        this.foreColor = foreColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics2D g) {
        Color color = getForeColor();

        Graphics2D copy = (Graphics2D) g.create();

        if (clip != null) {
        	copy.clip(clip);
        }

        copy.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        copy.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
            RenderingHints.VALUE_FRACTIONALMETRICS_ON);

        if (color != null) {
            copy.setColor(color);
            setStroke(copy);

            Rectangle b = getBounds();
            copy.drawLine(b.x, b.y, b.x + b.width, b.y + b.height);
        }

        copy.dispose();
    }

    /**
     * DOCUMENT ME!
     * @param linePattern   DOCUMENT ME!
     * @uml.property   name="linePattern"
     */
    public void setLinePattern(int linePattern) {
        this.linePattern = linePattern;
    }

    /**
     * DOCUMENT ME!
     * @return   DOCUMENT ME!
     * @uml.property   name="linePattern"
     */
    public int getLinePattern() {
        return linePattern;
    }

    /**
     * DOCUMENT ME!
     * @param lineSize   DOCUMENT ME!
     * @uml.property   name="lineSize"
     */
    public void setLineSize(float lineSize) {
        this.lineSize = lineSize;
    }

    /**
     * DOCUMENT ME!
     * @return   DOCUMENT ME!
     * @uml.property   name="lineSize"
     */
    public float getLineSize() {
        return lineSize;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int normalizedSize() {
        return (int) Math.max(lineSize, 1.0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g
     *            DOCUMENT ME!
     */
    protected void setStroke(Graphics2D g) {
        float lineSize = getLineSize();

        float[] dash = (float[]) storedLinePatterns[getLinePattern()];

        BasicStroke stroke;

        if (dash == null) {
            stroke = new BasicStroke(lineSize);
        } else {
            stroke = new BasicStroke(lineSize, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER,
                    10.0f, dash, 0.0f);
        }

        g.setStroke(stroke);
    }
}
