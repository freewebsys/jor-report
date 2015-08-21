package jatools.designer.layer.test;


import jatools.designer.ReportPanel;
import jatools.designer.peer.ComponentPeer;
import jatools.util.CursorUtil;

import java.awt.Cursor;
import java.awt.Point;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class BorderTester implements Tester {
    ReportPanel owner;

    /**
     * Creates a new BorderTester object.
     *
     * @param owner DOCUMENT ME!
     */
    public BorderTester(ReportPanel owner) {
        this.owner = owner;
    }

    protected Cursor getCursor(int hitPosition) {
        switch (hitPosition) {
        case ComponentPeer.SOUTH_WEST:
        case ComponentPeer.NORTH_EAST:
            return CursorUtil.NE_RESIZE_CURSOR;

        case ComponentPeer.SOUTH_EAST:
        case ComponentPeer.NORTH_WEST:
            return CursorUtil.NW_RESIZE_CURSOR;

        case ComponentPeer.EAST:
        case ComponentPeer.WEST:
            return CursorUtil.E_RESIZE_CURSOR;

        case ComponentPeer.SOUTH:
        case ComponentPeer.NORTH:
            return CursorUtil.S_RESIZE_CURSOR;
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Cursor test(int modifier, int x, int y) {
        if (owner.getSelectionCount() == 0) {
            return null;
        }

        for (int i = 0; i < owner.getSelectionCount(); i++) {
            ComponentPeer sel = owner.getSelection(i);

            if ((sel != null) && !sel.isResizable()) {
                return null;
            }
        }

        ComponentPeer hitPeer = owner.getSelection(0);

        Point loc = new Point(x, y);
        owner.screenPointAsChildPoint(hitPeer, loc);

        int hitPosition = hitPeer.hitHot(loc);

        boolean selected = hitPosition != ComponentPeer.NOT_HIT;

        if (selected) {
            return getCursor(hitPosition);
        } else {
            return null;
        }
    }
}
