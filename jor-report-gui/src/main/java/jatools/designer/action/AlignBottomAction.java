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
public class AlignBottomAction extends AlignAction {
    /**
     * Creates a new AlignBottomAction object.
     */
    public AlignBottomAction() {
        super(App.messages.getString("res.572"), getIcon("/jatools/icons/alignbottom.gif"),
            getIcon("/jatools/icons/alignbottom2.gif"));
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
        int deltaY = (targetRect.y + targetRect.height) - (peerRect.y + peerRect.height);

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
