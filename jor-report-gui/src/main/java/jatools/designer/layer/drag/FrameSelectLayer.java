package jatools.designer.layer.drag;


import jatools.designer.ReportPanel;
import jatools.designer.layer.AbstractLayer;
import jatools.designer.layer.Layer;
import jatools.designer.layer.painter.Painter;
import jatools.designer.peer.ComponentPeer;
import jatools.util.Util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class FrameSelectLayer extends AbstractLayer implements Painter {
    ReportPanel owner;
    int fromX;
    int fromY;
    int toX;
    int toY;

    /**
     * Creates a new FrameSelectLayer object.
     *
     * @param owner DOCUMENT ME!
     */
    public FrameSelectLayer(ReportPanel owner) {
        this.owner = owner;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int wakenBy() {
        return Layer.WAKEN_BY_MOUSE_DRAGGED;
    }

    /**
     * DOCUMENT ME!
     *
     * @param waker DOCUMENT ME!
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param deltaX DOCUMENT ME!
     * @param deltaY DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean tryWake(int waker, int modifier, int x, int y, int deltaX, int deltaY) {
        ComponentPeer peer = owner.findComponentPeerAt(x, y);

        if (!peer.isMoveable()) {
            mouseDragged(modifier, x, y, deltaX, deltaY);

            return true;
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param deltaX DOCUMENT ME!
     * @param deltaY DOCUMENT ME!
     */
    public void mouseDragged(int modifier, int x, int y, int deltaX, int deltaY) {
        toX = x;
        toY = y;

        if (!isWaken()) {
            fromX = x;
            fromY = y;
        } else {
            Rectangle frame = Util.toRactangle(fromX, fromY, toX, toY);

            owner.selectAt(frame);
        }

        owner.repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void mouseReleased(int modifier, int x, int y) {
        fireActionPerformed(ACTION_DONE);
        owner.repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics2D g) {
        if (this.isWaken()) {
            g.setColor(Color.blue);

            Rectangle rect = Util.toRactangle(fromX, fromY, toX, toY);
            ((Graphics2D) g).draw(rect);
        }
    }
}
