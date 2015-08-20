package jatools.designer.peer;

import jatools.component.Component;
import jatools.component.Panel;
import jatools.designer.ReportPanel;

import java.awt.Point;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class PanelPeer extends ComponentPeer {
    PanelPeer(ReportPanel owner, Component target) {
        super(owner, target);
    }

    /**
     * DOCUMENT ME!
     *
     * @param focusedBoxes DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getFocusedBoxes(Point[] focusedBoxes) {
        if (((Panel) this.getComponent()).getType() > 0) {
            return super.getFocusedBoxes4(focusedBoxes);
        } else {
            return super.getFocusedBoxes(focusedBoxes);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isMoveable() {
        return ((Panel) this.getComponent()).getType() == 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isResizable() {
        return ((Panel) this.getComponent()).getType() == 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isRemoveable() {
        return ((Panel) this.getComponent()).getType() == 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCopiable() {
        return ((Panel) this.getComponent()).getType() == 0;
    }
}
