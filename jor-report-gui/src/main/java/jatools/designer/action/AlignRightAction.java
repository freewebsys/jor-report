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
public class AlignRightAction extends AlignAction {
    /**
     * Creates a new AlignRightAction object.
     */
    public AlignRightAction() {
        super(App.messages.getString("res.574"), getIcon("/jatools/icons/alignright.gif"),
            getIcon("/jatools/icons/alignright2.gif"));
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
        int deltaX = (targetRect.x + targetRect.width) - (peerRect.x + peerRect.width);
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
