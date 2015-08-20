package jatools.designer;

import jatools.component.table.TableBase;
import jatools.designer.layer.utils.PointsMoveTracker;
import jatools.designer.peer.ComponentPeer;
import jatools.designer.peer.TablePeer;

import javax.swing.undo.CompoundEdit;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class Stretcher {
    public static final int NORTH = 1;
    public static final int SOUTH = 2;
    public static final int VECTICAL = 3;
    ComponentPeer[] peers;

    /**
     * Creates a new Stretcher object.
     *
     * @param peers DOCUMENT ME!
     */
    public Stretcher(ComponentPeer[] peers) {
        this.peers = peers;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ori DOCUMENT ME!
     * @param edit DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void strech(int ori, CompoundEdit edit) throws Exception {
        if ((peers == null) || (peers.length == 0)) {
            throw new Exception(App.messages.getString("res.188"));
        }

        if (edit != null) {
            PointsMoveTracker.getInstance().open();

            for (int i = 0; i < peers.length; i++) {
                ComponentPeer peer = peers[i];

                if (peer.getParent() instanceof TablePeer) {
                    peer = (TablePeer) peer.getParent();
                }

                if (ori == NORTH) {
                    peer.setY(0);
                } else if (ori == SOUTH) {
                    peer.setY(peer.getParent().getHeight() - peer.getHeight());
                } else if (ori == VECTICAL) {
                    peer.setY(0);

                    if (peer instanceof TablePeer) {
                        TablePeer tablePeer = (TablePeer) peer;
                        TableBase t = (TableBase) peer.getComponent();
                        int h = t.getRowHeight(t.getRowCount() - 1);
                        int dh = tablePeer.getParent().getHeight() - tablePeer.getHeight();
                        t.setRowHeight(t.getRowCount() - 1, dh + h);
                    } else {
                        peer.setHeight(peer.getParent().getHeight());
                    }
                }
            }

            if (edit != null) {
                PointsMoveTracker.getInstance().close(edit);
            }
        }
    }
}
