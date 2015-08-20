package jatools.io;

import java.io.IOException;
import java.io.OutputStream;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public interface ResourceOutput {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     * @throws IOException
     */
    public OutputStream getOutputStream() throws IOException;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getUrl();

  
}
