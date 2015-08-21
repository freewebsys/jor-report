package jatools.designer.layer.drag;

import jatools.designer.ReportPanel;

import jatools.designer.layer.AbstractLayer;
import jatools.designer.layer.painter.SelectionFramePainter;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ResizeLayer extends AbstractLayer {
    private ArrayList resizers = new ArrayList();
    private Resizer activeResizer;

    /**
     * Creates a new ResizeLayer object.
     *
     * @param owner DOCUMENT ME!
     * @param paintLayer DOCUMENT ME!
     */
    public ResizeLayer(ReportPanel owner, SelectionFramePainter paintLayer) {
        registerResizer(new SelectedComponentsResizer(owner, paintLayer));
    }

    /**
     * DOCUMENT ME!
     *
     * @param resizer DOCUMENT ME!
     */
    public void registerResizer(Resizer resizer) {
        this.resizers.add(resizer);
    }

    /**
     * DOCUMENT ME!
     */
    public void sleep() {
        super.sleep();
        this.activeResizer.end();
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
        Iterator it = this.resizers.iterator();

        while (it.hasNext()) {
            Resizer resizer = (Resizer) it.next();
            boolean b = resizer.start(modifier, x, y, deltaX, deltaY);

            if (b) {
                this.activeResizer = resizer;
                this.setCursor(this.activeResizer.getCursor());

                return true;
            }
        }

        return false;
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
        this.activeResizer.drag(x, y, deltaX, deltaY);
    }
}
