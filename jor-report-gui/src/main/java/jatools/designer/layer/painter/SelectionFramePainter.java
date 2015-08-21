package jatools.designer.layer.painter;

import jatools.component.Component;
import jatools.component.Line;

import jatools.designer.ReportPanel;

import jatools.designer.layer.utils.SelectionFrame;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;

import javax.swing.JComponent;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class SelectionFramePainter {
    private static final Stroke stroke = new BasicStroke(1);
    Color drawColor;
    SelectionFrame[] selectedFrames;
    JComponent canvas;
    private ReportPanel owner;
    boolean dirty = false;

    /**
     * Creates a new SelectionFramePainter object.
     *
     * @param owner DOCUMENT ME!
     * @param canvas DOCUMENT ME!
     * @param drawColor DOCUMENT ME!
     */
    public SelectionFramePainter(ReportPanel owner, JComponent canvas, Color drawColor) {
        this.owner = owner;
        this.drawColor = drawColor;
        this.canvas = canvas;
    }

    /**
     * DOCUMENT ME!
     */
    public void paintComponent() {
        if (selectedFrames != null) {
            Graphics2D g2 = (Graphics2D) canvas.getGraphics();

            if (owner.getScale() != 1.0f) {
                g2.scale(owner.getScale(), owner.getScale());
            }

            g2.setColor(drawColor.darker());
            g2.setStroke(stroke);
            g2.setXORMode(Color.WHITE);

            if (dirty) {
                for (int i = 0; i < selectedFrames.length; i++) {
                    SelectionFrame frame = selectedFrames[i];
                    g2.draw(frame.paintCache);
                }
            }

            for (int i = 0; i < selectedFrames.length; i++) {
                SelectionFrame frame = selectedFrames[i];

                if (frame.paintCache instanceof Line2D) {
                    ((Line2D) frame.paintCache).setLine(frame.startPoint.x, frame.startPoint.y,
                        frame.endPoint.x, frame.endPoint.y);
                } else {
                    int w = Math.abs(frame.startPoint.x - frame.endPoint.x);
                    int h = Math.abs(frame.startPoint.y - frame.endPoint.y);
                    int x = Math.min(frame.startPoint.x, frame.endPoint.x);
                    int y = Math.min(frame.startPoint.y, frame.endPoint.y);
                    ((RectangularShape) frame.paintCache).setFrame(x, y, w, h);
                }

                g2.draw(frame.paintCache);
            }

            dirty = true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SelectionFrame[] getSelectedFrames() {
        return selectedFrames;
    }

    /**
     * DOCUMENT ME!
     *
     * @param selectedFrames DOCUMENT ME!
     */
    public void setSelectedFrames(SelectionFrame[] selectedFrames) {
        this.selectedFrames = selectedFrames;
        dirty = false;

        for (int i = 0; i < selectedFrames.length; i++) {
            SelectionFrame frame = selectedFrames[i];
            Component target = frame.peer.getComponent();
            Shape s = null;

            if (target instanceof Line) {
                s = new Line2D.Float();
            } else {
                s = new Rectangle2D.Float();
            }

            frame.paintCache = s;
        }
    }
}
