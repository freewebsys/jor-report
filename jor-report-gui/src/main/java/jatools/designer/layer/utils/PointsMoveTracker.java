package jatools.designer.layer.utils;


import jatools.designer.Point2;
import jatools.designer.undo.PointMoveEdit;

import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public final class PointsMoveTracker {
    private static PointsMoveTracker instance = new PointsMoveTracker();
    private Map pointsCache = new HashMap();
    private boolean open;

    private PointsMoveTracker() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static PointsMoveTracker getInstance() {
        return instance;
    }

    /**
     * DOCUMENT ME!
     */
    public void open() {
        pointsCache.clear();
        open = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param point DOCUMENT ME!
     * @param delta DOCUMENT ME!
     */
    public void addPoint(Point2 point, Point delta) {
        if (!pointsCache.containsKey(point)) {
            Point oldPosition = delta;
            oldPosition.x = point.getX() - oldPosition.x;
            oldPosition.y = point.getY() - oldPosition.y;

            pointsCache.put(point, oldPosition);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param edit DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public UndoableEdit close(CompoundEdit edit) {
        if (edit != null) {
            Set keys = pointsCache.keySet();

            for (Iterator i = keys.iterator(); i.hasNext();) {
                Point2 zpoint = (Point2) i.next();
                Point apoint = (Point) pointsCache.get(zpoint);

                int dx = zpoint.getX() - apoint.x;
                int dy = zpoint.getY() - apoint.y;

                edit.addEdit(new PointMoveEdit(zpoint, dx, dy));
            }
        }

        open = false;

        return edit;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isOpen() {
        return open;
    }
}
