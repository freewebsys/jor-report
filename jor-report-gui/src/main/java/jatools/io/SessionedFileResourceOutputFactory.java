package jatools.io;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class SessionedFileResourceOutputFactory implements ResourceOutputFactory {
    private File parent;
    private String url;
    private HttpSession session;

    /**
    * Creates a new OutputFactory object.
    */
    public SessionedFileResourceOutputFactory(File parent, String url, HttpSession session) {
        this.parent = parent;
        this.url = url;
        this.session = session;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pre DOCUMENT ME!
     * @param ext DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ResourceOutput createOutput(String prefix, String ext) {
        File file = null;

        try {
        	
            file = File.createTempFile(prefix, ext, parent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        session.setAttribute(file.getName(), new TempFile(file));

        return new FileResourceOutput(file, getUrl(file));
    }

    private String getUrl(File file) {
        if (this.url != null) {
            return this.url + file.getName();
        } else {
            return this.parent.getName() + "/" + file.getName();
        }
    }

    public static class TempFile implements HttpSessionBindingListener {
        File file;

        TempFile(File file) {
            this.file = file;
        }

        public File getFile() {
            return this.file;
        }

        public void valueBound(HttpSessionBindingEvent httpSessionBindingEvent) {
        }

        public void valueUnbound(HttpSessionBindingEvent httpSessionBindingEvent) {
            file.delete();
        }
    }
}
