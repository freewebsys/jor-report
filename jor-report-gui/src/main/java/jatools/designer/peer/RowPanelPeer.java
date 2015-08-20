package jatools.designer.peer;

import jatools.component.Component;

import jatools.designer.ReportPanel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class RowPanelPeer extends LockedComponentPeer {
    RowPanelPeer(ReportPanel owner, Component target) {
        super(owner, target);
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isAcceptableChild(Component child) {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCopiable() {
        return false;
    }
}
