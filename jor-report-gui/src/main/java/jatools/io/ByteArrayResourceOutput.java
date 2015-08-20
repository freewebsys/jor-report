package jatools.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class ByteArrayResourceOutput implements ResourceOutput {
    String name;
    ByteArrayOutputStream baos;

    /**
     * Creates a new ByteArrayResourceOutput object.
     */
    public ByteArrayResourceOutput(String name) {
        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public OutputStream getOutputStream() throws IOException {
        baos = new ByteArrayOutputStream();

        return baos;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getUrl() {
        return "jatoolsreport?_action_type=tempfile&file=" + name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public byte[] getBytes() {
        return baos.toByteArray();
    }

 
}
