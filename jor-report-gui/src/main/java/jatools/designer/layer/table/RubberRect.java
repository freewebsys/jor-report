package jatools.designer.layer.table;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class RubberRect {
    private final static BasicStroke RESIZE_STROKE = new BasicStroke(3, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER, 1.0f, new float[] { 1.0f, 1.0f }, 0.0f);
    private Rectangle pos = new Rectangle();

    /**
     * Creates a new RubberLine object.
     *
     * @param orientation
     *            DOCUMENT ME!
     * @param pos
     *            DOCUMENT ME!
     * @param off
     *            DOCUMENT ME!
     * @param line_length
     *            DOCUMENT ME!
     */
    public RubberRect() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param x
     *            DOCUMENT ME!
     * @param y
     *            DOCUMENT ME!
     */
    public void moveTo(Rectangle newPos) {
        pos = newPos;
    }

    protected Rectangle getRect() {
        return pos;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g2
     *            DOCUMENT ME!
     */
    public void paint(Graphics2D g2) {
        Rectangle p = this.getRect();

        if (p == null) {
            return;
        }

        g2.setXORMode(Color.white);
        g2.setStroke(RESIZE_STROKE);
        g2.setColor(Color.DARK_GRAY);

        g2.drawRect(p.x, p.y, p.width, p.height);
        g2.setPaintMode();
    }
}
