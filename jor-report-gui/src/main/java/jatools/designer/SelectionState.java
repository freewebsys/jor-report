package jatools.designer;

import jatools.designer.peer.ComponentPeer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class SelectionState {
    boolean removeable;
    boolean resizable;
    boolean moveable;
    boolean copiable;
    boolean sameParent;
    int count;
    private boolean tableEditting;

    /**
     * DOCUMENT ME!
     *
     * @param ps DOCUMENT ME!
     */
    public void refresh(PeerSelection ps) {
        count = ps.getCount();
        removeable = true;
        resizable = true;
        moveable = true;
        sameParent = true;
        copiable = true;
        tableEditting = true;

        ComponentPeer parent = null;

        for (int i = 0; i < count; i++) {
            ComponentPeer peer = ps.getSelection(i);

            if (i == 0) {
                parent = peer.getParent();
            } else if (parent != peer.getParent()) {
                sameParent = false;
            }

            if (peer.getComponent().getCell() == null) {
                tableEditting = false;
            }

            if (!peer.isMoveable()) {
                moveable = false;
            }

            if (!peer.isResizable()) {
                resizable = false;
            }

            if (!peer.isRemoveable()) {
                removeable = false;
            }

            if (!peer.isCopiable()) {
                copiable = false;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getCount() {
        return count;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCopiable() {
        return copiable;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isMoveable() {
        return moveable;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isRemoveable() {
        return removeable;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isResizable() {
        return resizable;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSameParent() {
        return sameParent;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isTableEditting() {
        return tableEditting;
    }
}
