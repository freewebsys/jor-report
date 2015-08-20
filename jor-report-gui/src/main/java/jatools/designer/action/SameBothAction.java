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
public class SameBothAction extends ResizeAction {
    /**
     * Creates a new SameBothAction object.
     */
    public SameBothAction() {
        super(App.messages.getString("res.596"), getIcon("/jatools/icons/sameboth.gif"),
            getIcon("/jatools/icons/sameboth2.gif"));
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
        return new Point(targetRect.width - peerRect.width, targetRect.height - peerRect.height);
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
