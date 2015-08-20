package jatools.designer.layer.page;


import jatools.designer.ReportPanel;
import jatools.designer.layer.AbstractLayer;
import jatools.designer.layer.painter.Painter;
import jatools.util.CursorUtil;

import java.awt.Graphics2D;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PageLayer extends AbstractLayer implements Painter {
    private PageResizer resizer;

    /**
     * Creates a new PageLayer object.
     *
     * @param owner DOCUMENT ME!
     */
    public PageLayer(ReportPanel owner) {
        this.resizer = new PageResizer(owner);

        setCursor(CursorUtil.DEFAULT_CURSOR);
    }

    /**
     * DOCUMENT ME!
     */
    public void sleep() {
        super.sleep();
        resizer.end();
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
        boolean waken = this.resizer.start(modifier, x, y, deltaX, deltaY);

        if (waken) {
            this.setCursor(this.resizer.getCursor());
        }

        return waken;
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
        this.resizer.drag(x, y, deltaX, deltaY);
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
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics2D g) {
        this.resizer.paint(g);
    }
}
