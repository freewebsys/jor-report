package jatools.designer.action;


import jatools.designer.App;
import jatools.designer.SelectionState;

import java.awt.Point;
import java.awt.Rectangle;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class SameHeightAction extends ResizeAction {
    /**
     * Creates a new SameHeightAction object.
     */
    public SameHeightAction() {
        super(App.messages.getString("res.597"), getIcon("/jatools/icons/sameheight.gif"),
            getIcon("/jatools/icons/sameheight2.gif"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param targetRect DOCUMENT ME!
     * @param peerRect DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Point getSizeDelta(Rectangle targetRect, Rectangle peerRect) {
        return new Point(0, targetRect.height - peerRect.height);
    }

    /**
     * DOCUMENT ME!
     *
     * @param state DOCUMENT ME!
     */
    public void enabled(SelectionState state) {
        setEnabled((state.getCount() > 1) && state.isResizable());
    }
}
