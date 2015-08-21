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
public class AlignVerticalCenterAction extends AlignAction {
    /**
     * Creates a new AlignVerticalCenterAction object.
     */
    public AlignVerticalCenterAction() {
        super(App.messages.getString("res.576"), getIcon("/jatools/icons/alignvcenter.gif"),
            getIcon("/jatools/icons/alignvcenter2.gif"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param state DOCUMENT ME!
     */
    public void enabled(SelectionState state) {
        setEnabled((state.getCount() > 1) && state.isMoveable());
    }

    /**
     * DOCUMENT ME!
     *
     * @param targetRect DOCUMENT ME!
     * @param peerRect DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Point getDelta(Rectangle targetRect, Rectangle peerRect) {
        int deltaX = 0;
        int deltaY = (targetRect.y + (targetRect.height / 2)) -
            (peerRect.y + (peerRect.height / 2));

        return new Point(deltaX, deltaY);
    }
}
