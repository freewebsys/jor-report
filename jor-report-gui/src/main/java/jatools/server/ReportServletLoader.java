package jatools.server;

import jatools.accessor.ProtectPublic;
import jatools.engine.ReportJob;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class ReportServletLoader implements ProtectPublic {
    public static String contextPath;

    /**
     * DOCUMENT ME!
     *
     * @param servlet DOCUMENT ME!
     *
     * @throws ServletException DOCUMENT ME!
     */
    public static void service(HttpServlet servlet) throws ServletException {
        if (contextPath == null) {
            contextPath = servlet.getServletConfig().getServletContext().getRealPath("/");
            ReportJob.setServletPath(contextPath);
        }
    }
}
