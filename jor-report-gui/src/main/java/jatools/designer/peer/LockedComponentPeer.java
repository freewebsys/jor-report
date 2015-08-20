package jatools.designer.peer;

import jatools.component.Component;
import jatools.designer.ReportPanel;

import java.awt.Point;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class LockedComponentPeer extends ComponentPeer {
    LockedComponentPeer(ReportPanel owner, Component component) {
        super(owner, component);
    }

    /**
     * DOCUMENT ME!
     *
     * @param focusedBoxes DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getFocusedBoxes(Point[] focusedBoxes) {
        return super.getFocusedBoxes4(focusedBoxes);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isResizable() {
        return false;
    }
}
