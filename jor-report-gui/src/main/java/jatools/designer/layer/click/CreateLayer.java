package jatools.designer.layer.click;

import jatools.component.Component;

import jatools.designer.ReportPanel;

import jatools.designer.layer.AbstractLayer;
import jatools.designer.layer.Layer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class CreateLayer extends AbstractLayer {
    protected ReportPanel owner;
    BabyLooker looker;

    /**
     * Creates a new CreateLayer object.
     *
     * @param owner DOCUMENT ME!
     */
    public CreateLayer(ReportPanel owner) {
        this.owner = owner;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int wakenBy() {
        return Layer.WAKEN_BY_MOUSE_PRESSED;
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
        Component baby = owner.getBaby();

        if (baby != null) {
            if (looker == null) {
                looker = new BabyLooker(owner);
            }

            looker.clickGrow(baby, x, y);
            owner.prepareChildDone();
        }

        return false;
    }
}
