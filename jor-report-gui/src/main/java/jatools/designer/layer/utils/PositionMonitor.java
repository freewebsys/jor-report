package jatools.designer.layer.utils;


import jatools.designer.peer.ComponentPeer;
import jatools.designer.undo.PositionEdit;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PositionMonitor {
    static final String OLD_BOUNDS = "old.bounds";
    ArrayList boundsCache = new ArrayList();

    /**
     * DOCUMENT ME!
     *
     * @param peer DOCUMENT ME!
     */
    public void addAsDescending(ComponentPeer peer) {
        if (peer != null) {
            addAsDescending(peer.getParent());
            add(peer);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param peer DOCUMENT ME!
     */
    public void add(ComponentPeer peer) {
        if (!boundsCache.contains(peer)) {
            boundsCache.add(peer);
            peer.setClientProperty(OLD_BOUNDS, peer.getBounds());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param edit DOCUMENT ME!
     */
    public void addEdit(CompoundEdit edit) {
        Iterator it = boundsCache.iterator();

        while (it.hasNext()) {
            ComponentPeer peer = (ComponentPeer) it.next();
            Rectangle position = (Rectangle) peer.getClientProperty(OLD_BOUNDS);

            if (!position.equals(peer.getBounds())) {
                UndoableEdit pe = new PositionEdit(peer, position, peer.getBounds());
                edit.addEdit(pe);
            }
        }
    }
}
