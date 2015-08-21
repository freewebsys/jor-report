package jatools.designer.layer.utils;


import jatools.designer.peer.ComponentPeer;
import jatools.util.Util;

import java.awt.Rectangle;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PositionValidator {
    int groupOffx;
    int groupOffy;
    boolean autoGrow;

    PositionValidator(boolean autoGrow, int groupOffx, int groupOffy) {
        this.autoGrow = autoGrow;
        this.groupOffx = groupOffx;
        this.groupOffy = groupOffy;
    }

    /**
     * DOCUMENT ME!
     *
     * @param frames DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean validate(SelectionFrame[] frames) {
        if (autoGrow) {
            for (int i = 0; i < frames.length; i++) {
                SelectionFrame frame = frames[i];

                int x = Math.min(frame.startPoint.x, frame.endPoint.x);
                int y = Math.min(frame.startPoint.y, frame.endPoint.y);

                if ((x < groupOffx) || (y < groupOffy)) {
                    return false;
                }
            }
        } else {
            for (int i = 0; i < frames.length; i++) {
                SelectionFrame frame = frames[i];
                ComponentPeer peer = frames[i].peer;

                Rectangle peerArea = Util.toRactangle(frame.startPoint.x, frame.startPoint.y,
                        frame.endPoint.x, frame.endPoint.y);

                peerArea.translate(-frame.parentOffScreenX, -frame.parentOffScreenY);

                int tw = peer.getParent().getWidth();
                int th = peer.getParent().getHeight();

                if ((peerArea.x < 0) || (peerArea.y < 0) || ((peerArea.width + peerArea.x) > tw) ||
                        ((peerArea.height + peerArea.y) > th)) {
                    return false;
                }
            }
        }

        return true;
    }
}
