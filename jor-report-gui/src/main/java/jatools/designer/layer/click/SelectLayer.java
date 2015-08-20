package jatools.designer.layer.click;

import jatools.designer.ReportPanel;

import jatools.designer.layer.AbstractLayer;

import jatools.designer.peer.ComponentPeer;

import jatools.util.CursorUtil;

import java.awt.Point;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class SelectLayer extends AbstractLayer {
    ReportPanel owner;
    private ComponentPeer peer;
    private boolean canPluralSelect;
    private boolean validSelect;

    /**
     * Creates a new SelectLayer object.
     *
     * @param owner
     *            DOCUMENT ME!
     */
    public SelectLayer(ReportPanel owner) {
        this.owner = owner;

        setCursor(CursorUtil.DEFAULT_CURSOR);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int wakenBy() {
        return WAKEN_BY_MOUSE_PRESSED;
    }

    /**
     * DOCUMENT ME!
     */
    public void sleep() {
        super.sleep();

        if (this.validSelect && canPluralSelect) {
            if (owner.isSelected(peer)) {
                owner.unselect(peer);
            } else {
                owner.select(peer);
            }

            owner.repaint();
        }

        this.validSelect = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param waker
     *            DOCUMENT ME!
     * @param modifier
     *            DOCUMENT ME!
     * @param x
     *            DOCUMENT ME!
     * @param y
     *            DOCUMENT ME!
     * @param deltaX
     *            DOCUMENT ME!
     * @param deltaY
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean tryWake(int waker, int modifier, int x, int y, int deltaX, int deltaY) {
        if (this.isHitHot(x, y)) {
            return false;
        }

        validSelect = false;

        peer = owner.findComponentPeerAt(x, y);
        canPluralSelect = ctrlDown(modifier);

        if (!canPluralSelect) {
            if (!owner.isSelected(peer)) {
                owner.unselectAll();
                owner.select(peer);
            } else {
                return false;
            }

            owner.repaint();
        }

        return true;
    }

    boolean isHitHot(int x, int y) {
        ComponentPeer hitPeer = owner.getSelection(0);

        if (hitPeer != null) {
            Point loc = new Point(x, y);
            owner.screenPointAsChildPoint(hitPeer, loc);

            int hit = hitPeer.hitHot(loc);

            return hit != ComponentPeer.NOT_HIT;
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier
     *            DOCUMENT ME!
     * @param x
     *            DOCUMENT ME!
     * @param y
     *            DOCUMENT ME!
     * @param deltaX
     *            DOCUMENT ME!
     * @param deltaY
     *            DOCUMENT ME!
     */
    public void mouseDragged(int modifier, int x, int y, int deltaX, int deltaY) {
        if (isWaken()) {
            this.fireActionPerformed(ACTION_DONE);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier
     *            DOCUMENT ME!
     * @param x
     *            DOCUMENT ME!
     * @param y
     *            DOCUMENT ME!
     */
    public void mouseReleased(int modifier, int x, int y) {
        if (isWaken()) {
            validSelect = true;
            this.fireActionPerformed(ACTION_DONE);
        }
    }
}
