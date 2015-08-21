package jatools.engine.protect;

import jatools.io.ByteArrayResourceOutput;
import jatools.io.ResourceOutput;
import jatools.io.ResourceOutputFactory;
import jatools.util.UUID;

import javax.servlet.http.HttpSession;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ByteArrayResourceOutputFactory implements ResourceOutputFactory {
    private HttpSession session;

    /**
     * Creates a new ByteArrayResourceOutputFactory object.
     */
    public ByteArrayResourceOutputFactory(HttpSession session) {
        this.session = session;
    }

    /**
     * DOCUMENT ME!
     *
     * @param prefix DOCUMENT ME!
     * @param ext DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ResourceOutput createOutput(String prefix, String ext) {
        String name = UUID.randomUUID().toString();

        ResourceOutput result = new ByteArrayResourceOutput(name);
        session.setAttribute(name, result);

        return result;
    }

 
}
