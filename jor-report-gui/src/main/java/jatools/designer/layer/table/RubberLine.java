package jatools.designer.layer.table;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Line2D;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class RubberLine {
    private final static BasicStroke RESIZE_STROKE = new BasicStroke(1, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER, 1.0f, new float[] { 1.0f, 1.0f }, 0.0f);
    final public static int VERTICAL = 1;
    final public static int HORIZONTAL = 0;
    private int orientation;
    private Point pos = new Point(-1, -1);
    private Point off;
    private int line_length;

    /**
     * Creates a new RubberLine object.
     *
     * @param orientation DOCUMENT ME!
     * @param pos DOCUMENT ME!
     * @param off DOCUMENT ME!
     * @param line_length DOCUMENT ME!
     */
    public RubberLine(int orientation, int pos, Point off, int line_length) {
        if (orientation == VERTICAL) {
            this.pos.x = pos;
        } else {
            this.pos.y = pos;
        }

        this.orientation = orientation;
        this.off = off;
        this.line_length = line_length;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void moveTo(int x, int y) {
        if (orientation == VERTICAL) {
            pos.x = x;
        } else {
            pos.y = y;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g2 DOCUMENT ME!
     */
    public void paint(Graphics2D g2) {
        int x0 = 0;
        int x1 = 0;
        int y0 = 0;
        int y1 = 0;

        if (orientation == VERTICAL) {
            x0 = pos.x;
            x1 = x0;
            y0 = -off.y;
            y1 = line_length;
        } else {
            y0 = pos.y;
            y1 = y0;
            x0 = -off.x;
            x1 = line_length;
        }

        g2.setXORMode(Color.white);
        g2.setStroke(RESIZE_STROKE);
        g2.setColor(Color.DARK_GRAY);

        Line2D line = new Line2D.Float(x0, y0, x1, y1);
        g2.draw(line);
        g2.setPaintMode();
    }
}
