/*
 *   Author: John.
 *
 *   杭州杰创软件   All Copyrights Reserved.
 */
package jatools.designer.peer;

import jatools.component.Component;
import jatools.designer.ReportPanel;
import jatools.util.Util;

import java.awt.Point;
import java.awt.Rectangle;


/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.3 $
 * @author $author$
 */
public class LinePeer extends ComponentPeer {
    final static int HIT_TEST_DELTA = 3;

    LinePeer(ReportPanel owner, Component target) {
        super(owner, target);
    }

    /**
    * DOCUMENT ME!
    *
    * @param x DOCUMENT ME!
    * @param y DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public int hitHot(Point loc) {
        int size = FOCUSED_POINT_SIZE;
        int off = size / 2;

        int x = loc.x;
        int y = loc.y;
        int x1 = 0;
        int y1 = 0;
        int x2 = getWidth();
        int y2 = getHeight();

        if ((x >= (x1 - off)) && (x < (x1 + off)) && (y >= (y1 - off)) && (y <= (y1 + off))) {
            return locate(this.getX(), this.getY());
        } else if ((x >= (x2 - off)) && (x < (x2 + off)) && (y >= (y2 - off)) && (y <= (y2 + off))) {
            //  击中第一点
            return locate(this.getX2(), this.getY2());
        } else {
            return NOT_HIT;
        }
    }

    private int locate(int x, int y) {
       
        int hit = 0;

        if (x == left()) {
            hit = WEST;
        } else {
            hit = EAST;
        }

        if (y == top()) {
            hit |= NORTH;
        } else {
            hit |= SOUTH;
        }

        return hit;
    }

    private int left() {
        return Math.min(getX(), getX2());
    }

    private int top() {
        return Math.min(getY(), getY2());
    }

    /**
    * DOCUMENT ME!
    *
    * @param x DOCUMENT ME!
    * @param y DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public boolean hit(int x, int y) {
        return Util.hitLineTest(getX(), getY(), getX2(), getY2(), x, y, HIT_TEST_DELTA);
    }

    /**
     * 被 selectionFrame.apply调用
     *
     * @param x1 DOCUMENT ME!
     * @param y1 DOCUMENT ME!
     * @param x2 DOCUMENT ME!
     * @param y2 DOCUMENT ME!
     */
    public void updateBounds(int x1, int y1, int x2, int y2) {
        this.getComponent().setWidth(x2 - x1);
        this.getComponent().setHeight(y2 - y1);
        this.getComponent().setX(x1);
        this.getComponent().setY(y1);
    }

    /**
    * DOCUMENT ME!
    *
    * @param focusedBoxes DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public int getFocusedBoxes(Point[] focusedBoxes) {
        Point pointCache = focusedBoxes[0];
        pointCache.setLocation(0, 0);
        getOwner().childPointAsScreenPoint(this, pointCache);

        int x0 = pointCache.x;
        int y0 = pointCache.y;

        focusedBoxes[0].x = x0;
        focusedBoxes[0].y = y0;

        focusedBoxes[1].x = (x0 + getWidth());
        focusedBoxes[1].y = (y0 + getHeight());

        return 2;
    }

    int getX2() {
        return getX() + getWidth();
    }

    int getY2() {
        return getY() + getHeight();
    }

    /**
    * DOCUMENT ME!
    *
    * @param frame DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public boolean hit(Rectangle frame) {
        return Util.hitLineTest(getX(), getY(), getX2(), getY2(), frame);
    }
}
