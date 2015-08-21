package jatools.io;

import jatools.accessor.ProtectPublic;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public interface ResourceOutputFactory extends ProtectPublic {
    /**
     * DOCUMENT ME!
     *
     * @param pre DOCUMENT ME!
     * @param ext DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ResourceOutput createOutput(String prefix, String ext);
    
    
}
