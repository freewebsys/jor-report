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
public class AlignTopAction extends AlignAction {
    /**
     * Creates a new AlignTopAction object.
     */
    public AlignTopAction() {
        super(App.messages.getString("res.575"), getIcon("/jatools/icons/aligntop.gif"),
            getIcon("/jatools/icons/aligntop2.gif"));
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
        int deltaY = targetRect.y - peerRect.y;

        return new Point(deltaX, deltaY);
    }
}
