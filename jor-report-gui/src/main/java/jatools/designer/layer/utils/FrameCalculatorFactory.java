package jatools.designer.layer.utils;

import jatools.designer.peer.ComponentPeer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class FrameCalculatorFactory {
    static FrameCalculator moveCalculator = new _MoveCalculator();
    static FrameCalculator resizeCalculator = new _ResizeCalculator();

    /**
     * DOCUMENT ME!
     *
     * @param direction DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static FrameCalculator getInstance(int direction) {
        if (direction == ComponentPeer.CENTER) {
            return moveCalculator;
        } else {
            return resizeCalculator;
        }
    }
}


class _MoveCalculator implements FrameCalculator {
    /**
     * DOCUMENT ME!
     *
     * @param frame DOCUMENT ME!
     * @param dx DOCUMENT ME!
     * @param dy DOCUMENT ME!
     */
    public void calculate(SelectionFrame frame, int dx, int dy) {
        frame.startPoint.translate(dx, dy);
        frame.endPoint.translate(dx, dy);
    }
}


class _ResizeCalculator implements FrameCalculator {
    /**
     * DOCUMENT ME!
     *
     * @param frame DOCUMENT ME!
     * @param dx DOCUMENT ME!
     * @param dy DOCUMENT ME!
     */
    public void calculate(SelectionFrame frame, int dx, int dy) {
        if (frame.xPoint != null) {
            frame.xPoint.x += dx;
        }

        if (frame.yPoint != null) {
            frame.yPoint.y += dy;
        }
    }
}
