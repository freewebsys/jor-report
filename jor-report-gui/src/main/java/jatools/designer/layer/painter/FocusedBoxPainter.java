package jatools.designer.layer.painter;


import jatools.designer.ReportPanel;
import jatools.designer.peer.ComponentPeer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class FocusedBoxPainter implements Painter {
    static Point[] focusedBoxes = new Point[10];
    static Color LEADING_BOX_COLOR = new Color(0, 248, 0);
    static Color BOX_COLOR = new Color(0, 248, 248);
    static Color LOCKED_COLOR = new Color(255, 204, 51);

    static {
        for (int i = 0; i < focusedBoxes.length; i++) {
            focusedBoxes[i] = new Point();
        }
    }

    int focusedPointSize;
    Stroke stroke;
    Color drawColor;
    ReportPanel owner;

    /**
     * Creates a new FocusedBoxPainter object.
     *
     * @param owner DOCUMENT ME!
     * @param focusedPointSize DOCUMENT ME!
     * @param drawColor DOCUMENT ME!
     * @param stroke DOCUMENT ME!
     */
    public FocusedBoxPainter(ReportPanel owner, int focusedPointSize, Color drawColor, Stroke stroke) {
        this.owner = owner;
        this.focusedPointSize = focusedPointSize;

        this.drawColor = drawColor;
        this.stroke = stroke;
    }

    /**
     * Creates a new FocusedBoxPainter object.
     *
     * @param owner DOCUMENT ME!
     * @param focusedPointSize DOCUMENT ME!
     * @param drawColor DOCUMENT ME!
     */
    public FocusedBoxPainter(ReportPanel owner, int focusedPointSize, Color drawColor) {
        this(owner, focusedPointSize, drawColor, new BasicStroke(1));
    }

    static void fillFocusedBox(Graphics2D g2, Point p, int offset) {
        int size = offset * 2;

        g2.fillRect(p.x - offset, p.y - offset, size, size);
    }

    static void drawFocusedBox(Graphics2D g2, Point p, int offset) {
        int size = offset * 2;

        g2.drawRect(p.x - offset, p.y - offset, size, size);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g2 DOCUMENT ME!
     */
    public void paint(Graphics2D g2) {
        ComponentPeer[] peers = (ComponentPeer[]) owner.getSelection();

        g2.setStroke(stroke);
        g2.setColor(drawColor);

        int offset = focusedPointSize / 2;

        for (int i = 0; i < peers.length; i++) {
            ComponentPeer peer = peers[i];

            int len = peer.getFocusedBoxes(focusedBoxes);

            for (int j = 0; j < len; j++) {
                if (g2.getColor() != peer.getFocusColor()) {
                    g2.setColor(peer.getFocusColor());
                }

                if (!peer.isResizable()) {
                    g2.setColor(LOCKED_COLOR);
                } else {
                    g2.setColor((i == 0) ? LEADING_BOX_COLOR : BOX_COLOR);
                }

                fillFocusedBox(g2, focusedBoxes[j], offset);

                g2.setColor(Color.BLACK);
                drawFocusedBox(g2, focusedBoxes[j], offset);
            }
        }
    }
}
