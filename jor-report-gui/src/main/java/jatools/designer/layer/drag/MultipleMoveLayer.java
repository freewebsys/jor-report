package jatools.designer.layer.drag;


import jatools.designer.Point2;
import jatools.designer.ReportPanel;
import jatools.designer.layer.AbstractLayer;
import jatools.designer.peer.ComponentPeer;
import jatools.designer.peer.PanelPeer;
import jatools.designer.undo.PointsMoveEdit;
import jatools.util.CursorUtil;

import java.awt.Insets;
import java.util.ArrayList;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class MultipleMoveLayer extends AbstractLayer {
    ReportPanel owner;
    Insets limitInsets = new Insets(0, 0, 0, 0);
    int deltaX;
    int deltaY;

    /**
     * Creates a new MultipleMoveLayer object.
     *
     * @param owner DOCUMENT ME!
     */
    public MultipleMoveLayer(ReportPanel owner) {
        this.owner = owner;

        setCursor(CursorUtil.MOVE_CURSOR);
    }

    /**
     * DOCUMENT ME!
     */
    public void sleep() {
        super.sleep();

        if (owner.isUndoable()) {
            ComponentPeer[] peers = (ComponentPeer[]) owner.getSelection();
            Point2[] points = collectPoints(peers);
            owner.addEdit(new PointsMoveEdit(points, deltaX, deltaY));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param peers DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Point2[] collectPoints(ComponentPeer[] peers) {
        ArrayList points = new ArrayList();

        return (Point2[]) points.toArray(new Point2[0]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int wakenBy() {
        return WAKEN_BY_MOUSE_DRAGGED;
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
        this.deltaX = 0;
        this.deltaY = 0;

        ComponentPeer peer = owner.findComponentPeerAt(x, y);

        if (owner.isSelected(peer) && (!(peer instanceof PanelPeer))) {
            calculateLimitInsets((ComponentPeer[]) owner.getSelection(), limitInsets);
            mouseDragged(modifier, x, y, deltaX, deltaY);

            return true;
        } else {
            return false;
        }
    }

    private static Insets calculateLimitInsets(ComponentPeer[] peers, Insets limits) {
        ComponentPeer peer = peers[0];
        limits.left = peer.getX();
        limits.top = peer.getY();
        limits.right = peer.getParent().getWidth() - (limits.left + peer.getWidth());
        limits.bottom = peer.getParent().getHeight() - (limits.top + peer.getHeight());

        for (int i = 1; i < peers.length; i++) {
            peer = peers[i];

            int left = peer.getX();
            int top = peer.getY();
            int right = peer.getParent().getWidth() - (left + peer.getWidth());
            int bottom = peer.getParent().getHeight() - (top + peer.getHeight());

            if (left < limits.left) {
                limits.left = left;
            }

            if (right < limits.right) {
                limits.right = right;
            }

            if (top < limits.top) {
                limits.top = top;
            }

            if (bottom < limits.bottom) {
                limits.bottom = bottom;
            }
        }

        return limits;
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void mouseReleased(int modifier, int x, int y) {
        if (isWaken()) {
            this.fireActionPerformed(ACTION_DONE);
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
        ComponentPeer[] selection = (ComponentPeer[]) owner.getSelection();

        if (!willBreakOut(this.deltaX + deltaX, this.deltaY + deltaY)) {
            this.deltaX += deltaX;
            this.deltaY += deltaY;

            for (int i = 0; i < selection.length; i++) {
                selection[i].move(deltaX, deltaY);
            }

            owner.repaint();
        }
    }

    boolean willBreakOut(int dx, int dy) {
        boolean breakOut = false;

        if (dx > 0) {
            breakOut = dx > limitInsets.right;
        } else {
            breakOut = (-dx > limitInsets.left);
        }

        if (!breakOut) {
            if (dy > 0) {
                breakOut = dy > limitInsets.bottom;
            } else {
                breakOut = (-dy > limitInsets.top);
            }
        }

        return breakOut;
    }
}
