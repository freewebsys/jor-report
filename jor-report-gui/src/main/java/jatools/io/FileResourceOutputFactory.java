package jatools.io;

import java.io.File;
import java.io.IOException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class FileResourceOutputFactory implements ResourceOutputFactory {
    private File parent;
    private String url;

    /**
    * Creates a new OutputFactory object.
    */
    private FileResourceOutputFactory(File parent, String url) {
        this.parent = parent;
        this.url = url;

        if ((this.url != null) && !this.url.endsWith("/")) {
            this.url += "/";
        }
    }

    /**
     * Creates a new FileResourceOutputFactory object.
     *
     * @param parent DOCUMENT ME!
     */
    public FileResourceOutputFactory(File parent) {
        this(parent, null);
    }

    /* (non-Javadoc)
         * @see com.jatools.core.ResourceOutputFactory#createOutput(java.lang.String, java.lang.String)
         */

    /**
     * DOCUMENT ME!
     *
     * @param prefix DOCUMENT ME!
     * @param ext DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ResourceOutput createOutput(String prefix, String ext) {
        File file = null;

        try {
            if (!parent.exists()) {
                parent.mkdirs();
            }

            file = File.createTempFile(prefix, ext, parent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new FileResourceOutput(file, getUrl(file));
    }

    private String getUrl(File file) {
        if (this.url != null) {
            return this.url + file.getName();
        } else {
            return this.parent.getName() + "/" + file.getName();
        }
    }
}
