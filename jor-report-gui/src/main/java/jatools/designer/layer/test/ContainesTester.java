package jatools.designer.layer.test;


import jatools.designer.ReportPanel;
import jatools.designer.peer.ComponentPeer;
import jatools.util.CursorUtil;

import java.awt.Cursor;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ContainesTester implements Tester {
    private ReportPanel owner;

    /**
     * Creates a new ContainesTester object.
     *
     * @param owner DOCUMENT ME!
     */
    public ContainesTester(ReportPanel owner) {
        this.owner = owner;
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Cursor test(int modifier, int x, int y) {
        ComponentPeer hitPeer = findComponentPeerAt(x, y);

        if ((hitPeer != null) && hitPeer.isMoveable() && hitPeer.isSelected()) {
            return (CursorUtil.MOVE_CURSOR);
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ComponentPeer findComponentPeerAt(int x, int y) {
        return ReportPanel.findComponentPeerAt(owner.getReportPeer(), x, y);
    }
}
