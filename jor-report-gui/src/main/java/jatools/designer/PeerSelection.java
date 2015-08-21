package jatools.designer;

import jatools.designer.peer.ComponentPeer;
import jatools.designer.peer.TablePeer;

import java.awt.Insets;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PeerSelection {
    ArrayList listeners = new ArrayList();
    ArrayList selection = new ArrayList();
    boolean notifierPlaced = false;
    SelectionState state = new SelectionState();
    private ArrayList save;

    /**
     * DOCUMENT ME!
     *
     * @param peer DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSelected(ComponentPeer peer) {
        return selection.contains(peer);
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ComponentPeer getSelection(int index) {
        if ((index >= 0) && (index < selection.size())) {
            return (ComponentPeer) selection.get(index);
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getCount() {
        return selection.size();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ComponentPeer[] getSelection() {
        return (ComponentPeer[]) selection.toArray(new ComponentPeer[0]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param peer DOCUMENT ME!
     * @param frame DOCUMENT ME!
     */
    public void selectAt(ComponentPeer peer, Rectangle frame) {
        unselectAll();
        doSelectAt(peer, frame);
    }

    private void doSelectAt(ComponentPeer peer, Rectangle frame) {
        int x = frame.x;
        int y = frame.y;

        Rectangle frameCopy = (Rectangle) frame.clone();

        if (!(peer instanceof TablePeer)) {
            Insets is = peer.getComponent().getPadding();

            for (int i = peer.getChildCount() - 1; i >= 0; i--) {
                ComponentPeer child = peer.getChild(i);
                frame.x -= (peer.getX() + is.left);

                frame.y -= (peer.getY() + is.top);
                doSelectAt(child, frame);
                frame.x = x;
                frame.y = y;
            }
        }

        if (peer.hit(frameCopy)) {
            select(peer);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param peer DOCUMENT ME!
     */
    public void select(ComponentPeer peer) {
        if (!selection.contains(peer)) {
            selection.add(peer);
        }

        if (selection.size() > 1) {
            ComponentPeer[] peers = (ComponentPeer[]) selection.toArray(new ComponentPeer[0]);

            for (int i = 0; i < peers.length; i++) {
                ComponentPeer _p = peers[i];

                while ((_p = _p.getParent()) != null) {
                    selection.remove(_p);
                }
            }
        }

        placeNotifier();
    }

    /**
     * DOCUMENT ME!
     */
    public void reselect() {
        placeNotifier();
    }

    /**
     * DOCUMENT ME!
     *
     * @param peer DOCUMENT ME!
     */
    public void unselect(ComponentPeer peer) {
        if (selection.contains(peer)) {
            selection.remove(peer);
            placeNotifier();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void unselectAll() {
        if (!selection.isEmpty()) {
            selection.clear();
            placeNotifier();
        }
    }

    private void placeNotifier() {
        if (listeners.isEmpty()) {
            return;
        }

        _ChangedNotifier notifier = new _ChangedNotifier();
        SwingUtilities.invokeLater(notifier);
        notifier.run();
    }

    private void fireSelectionChange() {
        state.refresh(this);

        ChangeEvent e = new ChangeEvent(this);

        for (Iterator i = listeners.iterator(); i.hasNext();) {
            ChangeListener l = (ChangeListener) i.next();
            l.stateChanged(e);
        }

        notifierPlaced = false;
    }

    /**
     * DOCUMENT ME!
     */
    public void save() {
        save = this.selection;
        this.selection = new ArrayList();
        this.fireSelectionChange();
    }

    /**
     * DOCUMENT ME!
     */
    public void rollback() {
        this.selection = save;
        this.fireSelectionChange();
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void addSelectionListener(ChangeListener lst) {
        listeners.add(lst);
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void removeSelectionListener(ChangeListener lst) {
        listeners.remove(lst);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SelectionState getState() {
        return state;
    }

    class _ChangedNotifier implements Runnable {
        public void run() {
            fireSelectionChange();
        }
    }
}
