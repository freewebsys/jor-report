package jatools.designer.layer.page;


import jatools.component.Page;
import jatools.component.Panel;
import jatools.designer.ReportPanel;
import jatools.designer.layer.test.Tester;
import jatools.designer.peer.ComponentPeer;
import jatools.util.CursorUtil;

import java.awt.Cursor;
import java.awt.Insets;
import java.awt.Point;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PageTester implements Tester {
    final static int UNKNOWN = -1;
    final static int TOP = 1;
    final static int BOTTOM = 2;
    final static int TOP_MARGIN = 3;
    final static int LEFT_MARGIN = 4;
    final static int RIGHT_MARGIN = 5;
    final static int BOTTOM_MARGIN = 6;
    final static int PAGE_WIDTH = 7;
    final static int PAGE_HEIGHT = 8;
    final static int EDGE_SIZE = 2;
    ReportPanel owner;

    /**
     * Creates a new PageTester object.
     *
     * @param owner DOCUMENT ME!
     */
    public PageTester(ReportPanel owner) {
        this.owner = owner;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int testSide(int x, int y) {
        Page page = this.owner.getPage();

        if (hit(page.getWidth(), x)) {
            return PAGE_WIDTH;
        }

        if (hit(page.getHeight(), y)) {
            return PAGE_HEIGHT;
        }

        Insets is = page.getPadding();

        if (hit(is.top, y)) {
            return TOP_MARGIN;
        }

        if (hit(page.getHeight() - is.bottom, y)) {
            return BOTTOM_MARGIN;
        }

        if (hit(is.left, x)) {
            return LEFT_MARGIN;
        }

        if (hit(page.getWidth() - is.right, x)) {
            return RIGHT_MARGIN;
        }

        Panel header = page.getHeader();

        if (header != null) {
            ComponentPeer headerPeer = owner.getComponentPeer(header);
            Point loc = new Point(x, y);
            headerPeer.getOwner().screenPointAsChildPoint(headerPeer, loc);

            if (hit(header.getHeight(), loc.y)) {
                return TOP;
            }
        }

        Panel footer = page.getFooter();

        if (footer != null) {
            ComponentPeer footerPeer = owner.getComponentPeer(footer);
            Point loc = new Point(x, y);
            footerPeer.getOwner().screenPointAsChildPoint(footerPeer, loc);

            if (hit(0, loc.y)) {
                return BOTTOM;
            }
        }

        return UNKNOWN;
    }

    boolean hit(int target, int p) {
        return (p >= (target - EDGE_SIZE)) && (p <= (target + EDGE_SIZE));
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
        int where = testSide(x, y);

        if (where != UNKNOWN) {
            if ((where == LEFT_MARGIN) || (where == RIGHT_MARGIN) || (where == PAGE_WIDTH)) {
                return CursorUtil.E_RESIZE_CURSOR;
            } else {
                return CursorUtil.S_RESIZE_CURSOR;
            }
        } else {
            return null;
        }
    }
}
