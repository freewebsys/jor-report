package jatools.designer.layer.utils;

import jatools.designer.ReportPanel;
import jatools.designer.peer.ComponentPeer;
import jatools.util.Util;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public final class SelectionFrame {
    public ComponentPeer peer;
    ComponentPeer newParent;
    int parentOffScreenX;
    int parentOffScreenY;
    public Point startPoint;
    public Point endPoint;
    public Point xPoint;
    public Point yPoint;
    public FrameCalculator calculator;
    public Shape paintCache;

    SelectionFrame(ComponentPeer peer, int dir) {
        this.peer = peer;
        createCalculator(dir);
    }

    SelectionFrame(ComponentPeer peer) {
        this.peer = peer;
    }

    private void createCalculator(int direction) {
        ReportPanel owner = peer.getOwner();

        Point p = new Point();

        Point _startPoint = new Point(peer.getX(), peer.getY());
        Point _endPoint = new Point(peer.getX() + peer.getWidth(), peer.getY() + peer.getHeight());

        p.x = 0;
        p.y = 0;
        owner.childPointAsScreenPoint(peer.getParent(), p);
        _startPoint.translate(p.x, p.y);
        _endPoint.translate(p.x, p.y);

        parentOffScreenX = p.x;
        parentOffScreenY = p.y;

        startPoint = _startPoint;
        endPoint = _endPoint;

        Point leftPoint = (_endPoint.x > _startPoint.x) ? _startPoint : _endPoint;
        Point rightPoint = (leftPoint == _startPoint) ? _endPoint : _startPoint;
        Point topPoint = (_endPoint.y > _startPoint.y) ? _startPoint : _endPoint;
        Point bottomPoint = (topPoint == _startPoint) ? _endPoint : _startPoint;

        switch (direction) {
        case ComponentPeer.SOUTH_WEST:
            xPoint = leftPoint;
            yPoint = bottomPoint;

            break;

        case ComponentPeer.NORTH_EAST:
            xPoint = rightPoint;
            yPoint = topPoint;

            break;

        case ComponentPeer.SOUTH_EAST:
            xPoint = rightPoint;
            yPoint = bottomPoint;

            break;

        case ComponentPeer.NORTH_WEST:
            xPoint = leftPoint;
            yPoint = topPoint;

            break;

        case ComponentPeer.EAST:
            xPoint = rightPoint;
            yPoint = null;

            break;

        case ComponentPeer.WEST:
            xPoint = leftPoint;
            yPoint = null;

            break;

        case ComponentPeer.SOUTH:
            xPoint = null;
            yPoint = bottomPoint;

            break;

        case ComponentPeer.NORTH:
            xPoint = null;
            yPoint = topPoint;

            break;

        case ComponentPeer.CENTER:
            break;
        }

        calculator = FrameCalculatorFactory.getInstance(direction);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWidth() {
        return Math.abs(endPoint.x - startPoint.x);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHeight() {
        return Math.abs(endPoint.y - startPoint.y);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Rectangle toScreenBounds() {
        return Util.toRactangle(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
    }

    /**
     * DOCUMENT ME!
     */
    public void apply() {
        peer.getComponent()
            .setBounds(Util.toRactangle(startPoint.x - parentOffScreenX,
                startPoint.y - parentOffScreenY, endPoint.x - parentOffScreenX,
                endPoint.y - parentOffScreenY));

        peer.updateBounds(startPoint.x - parentOffScreenX, startPoint.y - parentOffScreenY,
            endPoint.x - parentOffScreenX, endPoint.y - parentOffScreenY);
    }
}
