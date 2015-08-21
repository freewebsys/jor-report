package jatools.server;

import jatools.accessor.ProtectPublic;
import jatools.data.reader.sql.ModelsQuery;
import jatools.designer.App;
import jatools.engine.ReportJob;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class JatoolsReportServlet extends HttpServlet implements ProtectPublic {
    /**
     * Creates a new JatoolsReportServlet object.
     */
    public JatoolsReportServlet() {
    }

    /**
     * DOCUMENT ME!
     *
     * @throws ServletException DOCUMENT ME!
     */
    public void init() throws ServletException {
        ReportServletLoader.service(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws ServletException DOCUMENT ME!
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        jrservice(request, response);
    }

    private void jrservice(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        String action = request.getParameter(ReportJob.ACTION_TYPE);

        if ("export".equals(action)) {
            ReportExporter.service(request, response);
        }else if ("querymodel".equals(action)) {
        	try {
				ModelsQuery.service(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        } else if (action == null) {
            ReportWriter.service(request, response);
        } else if ("tempfile".equals(action)) {
            FileFinder.service(request, response);
        } else {
            throw new ServletException(App.messages.getString("res.42") + action + ".");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     * @throws ServletException DOCUMENT ME!
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
        System.out.println(request.getCharacterEncoding());

        request.setCharacterEncoding("UTF-8");
        jrservice(request, response);
    }

    /**
     * DOCUMENT ME!
     */
    public void destroy() {
    }
}
