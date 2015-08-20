package jatools.io;

import jatools.engine.ReportJob;
import jatools.engine.protect.ByteArrayResourceOutputFactory;

import java.io.File;

import javax.servlet.http.HttpSession;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class DefaultResourceOutputFactory {
    static boolean contextReadOnly = true;//"true".equals(System2.getProperty("contextReadOnly"));
    static File temp_dir;

    /**
     * DOCUMENT ME!
     *
     * @param session DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ResourceOutputFactory createInstance(HttpSession session) {
        if (contextReadOnly) {
        	return new ByteArrayResourceOutputFactory(session);
//            return new SessionedFileResourceOutputFactory(null,
//                "jatoolsreport?" + ReportJob.ACTION_TYPE + "=tempfile&file=", session);
        } else {
            return new SessionedFileResourceOutputFactory(getTempDirectory(), null, session);
        }
    }

    static File getTempDirectory() {
        if (temp_dir == null) {
            String ctx = ReportJob.getServletPath();

            if (!ctx.endsWith(File.separator)) {
                ctx = ctx + File.separator;
            }

            temp_dir = new File(ctx + "temp");

            if (!temp_dir.exists()) {
                temp_dir.mkdirs();
            }
        }

        return temp_dir;
    }
}
