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
public class AlignCenterAction extends AlignAction {
    /**
     * Creates a new AlignCenterAction object.
     */
    public AlignCenterAction() {
        super(App.messages.getString("res.31"), getIcon("/jatools/icons/alignhcenter.gif"),
            getIcon("/jatools/icons/alignhcenter2.gif"));
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
        int deltaX = (targetRect.x + (targetRect.width / 2)) - (peerRect.x + (peerRect.width / 2));
        int deltaY = 0;

        return new Point(deltaX, deltaY);
    }

    /**
     * DOCUMENT ME!
     *
     * @param state DOCUMENT ME!
     */
    public void enabled(SelectionState state) {
        setEnabled((state.getCount() > 1) && state.isMoveable());
    }
}
