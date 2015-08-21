package jatools.server;

import jatools.ReportDocument;
import jatools.VariableContext;

import jatools.data.Formula;
import jatools.data.Parameter;

import jatools.designer.App;

import jatools.engine.ReportJob;
import jatools.engine.System2;

import jatools.engine.printer.ReportPrinter;

import jatools.engine.script.ReportContext;
import jatools.engine.script.Script;

import jatools.formatter.DateFormat;

import jatools.io.DefaultResourceOutputFactory;
import jatools.io.ResourceOutputFactory;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;

import java.net.URLEncoder;

import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

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
public class ReportExporter extends ReportActionBase {
    /**
     * DOCUMENT ME!
     *
     * @param request DOCUMENT ME!
     * @param response DOCUMENT ME!
     * @param job DOCUMENT ME!
     * @param as DOCUMENT ME!
     * @param imp DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void export(HttpServletRequest request, HttpServletResponse response,
        ReportJob job, String as, ResourceOutputFactory imp)
        throws Exception {
        String ext = as;

        if (as.equals("dhtml")) {
            ext = "htm";
        } else if (as.equals("csv")) {
            ext = "txt";
        }

        String file = getFileName(job, ext);

        response.setContentType("application/x-filler");
        response.setHeader("Content-Disposition",
            "attachment;filename=" + URLEncoder.encode(file, "UTF-8"));

        ServletOutputStream os = response.getOutputStream();

        if (as.equals("dhtml")) {
            OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");

            job.printAsDHTML(imp, writer);
        } else if (as.equals("pdf")) {
            job.printAsPDF(os);
        } else if (as.equals("xls") || as.equals("xlsn")) {
            ByteArrayOutputStream ba = new ByteArrayOutputStream();

            if (as.equals("xlsn")) {
                job.printAsXLSn(ba);
            } else {
                job.printAsXLS(ba);
            }

            ba.writeTo(os);
        } else if (as.equals("rtf")) {
            job.printAsRTF(os);
        } else {
            throw new Exception(App.messages.getString("res.44") + as);
        }

        os.close();
    }

    private static String getFileName(ReportJob job, String ext) {
        String result = (String) job.getParameter(ReportJob.EXPORT_DEFAULT_NAME);

        if ((result == null)) {
            String expName = job.getDocument().getProperty(ReportDocument.EXPORT_FILE_NAME);

            if ((expName != null) && (expName.trim().length() > 0)) {
                Script script = prepareScriptEngine(job);
                result = (String) script.evalTemplate(expName);
            }
        }

        if (result == null) {
            result = "untitled." + ext;
        }

        if (result.indexOf('.') == -1) {
            result += ("." + ext);
        }

        return result;
    }

    private static Script prepareScriptEngine(ReportJob job) {
        try {
            ReportContext script = new ReportContext(null);
            script.set(ReportContext.SCRIPT, script);

            declareDocumentVariables(script, job.getDocument());

            if (job.getCachedParameters() != null) {
                declareExternalVariables(job.getDocument(), script, job.getCachedParameters());
            }

            declareSystemVariables(script);

            return script;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static void declareDocumentVariables(Script script, ReportDocument doc) {
        doc.getComponent(null);

        VariableContext vc = doc.getVariableContext();
        Iterator it = vc.names();

        while (it.hasNext()) {
            String var = (String) it.next();
            Object value = vc.getVariable(var);
            script.set(var, value);

            if (value instanceof Formula) {
                ((Formula) value).setCalculator(script);
            }
        }
    }

    private static void declareExternalVariables(ReportDocument doc, Script script, Map parameters) {
        Iterator it = parameters.keySet().iterator();

        while (it.hasNext()) {
            String key = (String) it.next();
            Object val = parameters.get(key);

            script.set(key, val);
        }

        it = doc.getVariableContext().variables(VariableContext.PARAMETER);

        while (it.hasNext()) {
            Parameter p = (Parameter) it.next();
            Object value = null;

            value = parameters.get(p.getName());

            if (value != null) {
                if (value instanceof String && (p.type() != String.class)) {
                    try {
                        value = p.castValue(p.getType1(), (String) value);
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            }

            if (value == null) {
                value = p;
            }

            script.set(p.getName(), value);
        }
    }

    private static void declareSystemVariables(Script script) {
        script.set(ReportPrinter.NOW,
            DateFormat.format(new Date(), System2.getProperty("timeformat")));
        script.set(ReportPrinter.TODAY,
            DateFormat.format(new Date(), System2.getProperty("dayformat")));
        script.set(ReportPrinter.COMPANY, System2.getProperty("company"));
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
            final String formats = ";dhtml;pdf;xls;";
            String as = (String) request.getParameter(ReportJob.AS_PARAM);

            if ((as == null) && (formats.indexOf(as + ";") == 1)) {
                throw new Exception(App.messages.getString("res.45"));
            }

            ReportJob job = null;

            job = JobCacher.callJob(request.getSession(),
                    request.getParameter(ReportJob.JOB_SESSION_ID));

            for (Enumeration en = request.getParameterNames(); en.hasMoreElements();) {
                String name = (String) en.nextElement();
                job.setParameter(name, request.getParameter(name));
            }

            job.setParameter(ReportJob.HTTP_REQUEST, request);

            if (true || ReportJob.USE_SESSION2) {
                job.setParameter(ReportJob.HTTP_SESSION2, request.getSession());
            }

            ReportExporter.export(request, response, job, as,
                DefaultResourceOutputFactory.createInstance(request.getSession()));

            JobCacher.doCache(request.getSession(), job);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }
}
