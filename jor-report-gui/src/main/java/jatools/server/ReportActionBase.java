package jatools.server;

import jatools.engine.ReportJob;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class ReportActionBase {
    private static Logger logger = Logger.getLogger("com.jatools.server.jrs");

    protected static ReportJob createJob(HttpServletRequest request)
        throws Exception {
        InputStream is = null;

        String file = (String) request.getParameter("file");
        String xmluuid = (String) request.getParameter("xmluuid");

        if (xmluuid != null) {
            String xml = (String) request.getSession().getServletContext().getAttribute(xmluuid);

            if (xml != null) {
                is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
            }
        } else if (file != null) {
            try {
                File f = new File(file);
                is = new FileInputStream(f);
            } catch (FileNotFoundException e) {
                throw new Exception("\u6253\u4E0D\u5F00\u6307\u5B9A\u6A21\u677F: ");
            }
        } else {
            throw new Exception("\u8BF7\u6307\u5B9A\u62A5\u8868\u6A21\u677F\u6587\u4EF6");
        }

        ReportJob job = new ReportJob(is);

        for (Enumeration en = request.getParameterNames(); en.hasMoreElements();) {
            String name = (String) en.nextElement();
            job.setParameter(name, request.getParameter(name));
        }

        String as = request.getParameter(ReportJob.AS_PARAM);

        if ((as == null) || as.equals("")) {
            job.setParameter(ReportJob.AS_PARAM, "applet");
        }

        return job;
    }
}
