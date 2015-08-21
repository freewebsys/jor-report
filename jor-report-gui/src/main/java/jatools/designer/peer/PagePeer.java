package jatools.designer.peer;

import jatools.component.Component;
import jatools.designer.ReportPanel;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class PagePeer extends LockedComponentPeer {
    /**
     * Creates a new PagePeer object.
     *
     * @param owner DOCUMENT ME!
     * @param target DOCUMENT ME!
     */
    public PagePeer(ReportPanel owner, Component target) {
        super(owner, target);
    }

//    //    /**
//    /**
//     * DOCUMENT ME!
//     *
//     * @return DOCUMENT ME!
//     */
//    public boolean isResizable() {
//        return false;
//    }

    public boolean isRemoveable() {
        return false;
    }
    
    public boolean isCopiable() {
        return false;
    }
}
