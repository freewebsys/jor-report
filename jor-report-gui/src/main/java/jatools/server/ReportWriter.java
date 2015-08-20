package jatools.server;

import jatools.designer.App;
import jatools.engine.ReportJob;
import jatools.io.DefaultResourceOutputFactory;
import jatools.io.ResourceOutputFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Enumeration;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ReportWriter extends ReportActionBase {
    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     * @param job DOCUMENT ME!
     * @param as DOCUMENT ME!
     * @param ff DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void write(HttpServletRequest request, HttpServletResponse response,
        ReportJob job, String as, ResourceOutputFactory ff)
        throws Exception {
        if (as.equals("dhtml")) {
            response.setContentType("text/html;charset=UTF-8");

            PrintWriter out = response.getWriter();

            job.printAsDHTML(ff, out);
        }else if (as.equals("xls") || as.equals("xlsn")) {
            // 打印excel报表到一个字节流
            ByteArrayOutputStream ba = new ByteArrayOutputStream();

            if (as.equals("xlsn")) {
                job.printAsXLSn(ba);
            } else {
                job.printAsXLS(ba);
            }

            // 将字节流输出到 servlet 的response
            response.setContentType("application/vnd.ms-excel");
            //application/x-msexcel
            response.setContentType("application/x-msexcel");
            response.setHeader("Content-disposition", "attachment;filename=report.xls");

            response.setContentLength(ba.size());

            ServletOutputStream sos = response.getOutputStream();
            ba.writeTo(sos);
            sos.flush();
        }  else {
            throw new Exception("\u4E0D\u652F\u6301\u8F93\u51FA\u683C\u5F0F " + as);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     *
     * @throws ServletException DOCUMENT ME!
     */
    public static void service(HttpServletRequest request, HttpServletResponse response)
        throws ServletException {
        try {
        
            ReportJob job = null;
            String jobUUID = request.getParameter(ReportJob.REQUIRED_CAHCED_JOB_UUID);

            if (jobUUID != null) {
                job = JobCacher.callJob(request.getSession(), jobUUID);

                for (Enumeration en = request.getParameterNames(); en.hasMoreElements();) {
                    String name = (String) en.nextElement();

                    job.setParameter(name, request.getParameter(name));
                }
            } else {
                final String formats = "dhtml;csv;applet;pdf;rtf;csv;xls;xlsn;";

                String as = (String) request.getParameter(ReportJob.AS_PARAM);

                if ((as == null) && (formats.indexOf(as + ";") == 1)) {
                    throw new Exception(App.messages.getString("res.45"));
                }

                job = createJob(request);
            }

            job.setParameter(ReportJob.HTTP_REQUEST, request);

            if (true || ReportJob.USE_SESSION2) {
                job.setParameter(ReportJob.HTTP_SESSION2, request.getSession());
            }

            ReportWriter.write(request, response, job, (String) job.getParameter("as"),
                DefaultResourceOutputFactory.createInstance(request.getSession()));

            if (true || ReportJob.USE_SESSION2) {
                JobCacher.doCache(request.getSession(), job);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
