package jatools.designer;

import jatools.accessor.ProtectPublic;
import jatools.core.view.PageView;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public interface PageProvider extends ProtectPublic {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract PageView[] getPages();
}
