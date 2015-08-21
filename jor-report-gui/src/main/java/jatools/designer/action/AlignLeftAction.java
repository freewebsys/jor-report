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
public class AlignLeftAction extends AlignAction {
    /**
     * Creates a new AlignLeftAction object.
     */
    public AlignLeftAction() {
        super(App.messages.getString("res.573"), getIcon("/jatools/icons/alignleft.gif"),
            getIcon("/jatools/icons/alignleft2.gif"));
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
        int deltaX = targetRect.x - peerRect.x;
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
