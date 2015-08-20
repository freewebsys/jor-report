package jatools.core.view;

import java.awt.Graphics2D;
import java.io.Serializable;


/**
 *
 *  DOCUMENT ME!
 *
 *  @version $Revision: 1.1 $
 *  @author $author$
 *
 */
public interface View extends Serializable {
    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    void paint(Graphics2D g);
    

    

//    int typeOf();

//    Object getOwner();
}
