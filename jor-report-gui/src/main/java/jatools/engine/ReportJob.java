package jatools.engine;

import jatools.ReportDocument;
import jatools.accessor.ProtectPublic;
import jatools.io.ResourceOutputFactory;
import jatools.util.Util;
import jatools.xml.XmlReader;

import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;




/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ReportJob implements ProtectPublic {
    public static boolean USE_SESSION2 = true;
    private static Logger logger = Logger.getLogger("ZReportJob");
    public static String ALL_IN_ONE_PAGE = "_all_in_one_page";
    public static String HTML_BODY_ONLY = "_html_body_only";
    public static String HTML_TITLE = "_html_title";
    public static String HTML_REPORT_ID = "_html_report_id";
    public static String HTML_PAGE_CACHE = "_html_page_cache";
    public static String HTML_REPORT_CACHER = "_html_report_cacher";
    public static String HTML_REPORT_SERVER_PATH = "_html_report_server_path";
    public static String HTML_CSS_HEAD_ONLY = "_html_css_head_only";
    
    public static String AS_PARAM = "as";
    public static String FILE_PARAM = "file";
    public static String CACHED_PARAMS = "cached_params";
    public static String DO_EXPORT_PARAM = "do_export";
    public static String EXPORT_DEFAULT_NAME = "_exp_file_name";
    public static final String REQUIRED_CAHCED_JOB_UUID = "_required_cached_job_uuid";
    public static final String HTTP_REQUEST = PrintConstants.HTTP_REQUEST;
    public static final String HTTP_SESSION2 = PrintConstants.HTTP_SESSION;
    public static final String JOB_SESSION_ID = "_job_session_id";
    public static String ACTION_TYPE = "_action_type";
    private ReportDocument doc;
    protected Map paramValues = new HashMap();
    private String id = Util.randomId();

    /**
     * Creates a new ReportJob object.
     *
     * @param url DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public ReportJob(URL url) throws Exception {
        this();
        doc = (ReportDocument) XmlReader.read(url.openStream());
    }

    /**
     * Creates a new ReportJob object.
     */
    public ReportJob() {
        System.gc();
        this.paramValues.put(JOB_SESSION_ID, this.id);
    }

    /**
     * Creates a new ReportJob object.
     *
     * @param is DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public ReportJob(InputStream is) throws Exception {
        this();
        doc = (ReportDocument) XmlReader.read(is);
    }

    /**
     * Creates a new ReportJob object.
     *
     * @param doc DOCUMENT ME!
     */
    public ReportJob(ReportDocument doc) {
        this();
        this.doc = doc;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator getParameters() {
        return ReportPrinterUtil.parameters(doc);
    }
    public void printAsPDF(OutputStream os) throws Exception {

   	 Object allinone = this.paramValues.remove(ReportJob.ALL_IN_ONE_PAGE);
       _Job.printAsPDF(os, doc, paramValues);
       
       if(allinone != null)
       	this.paramValues.put(ReportJob.ALL_IN_ONE_PAGE,allinone);
   }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Map getCachedParameters() {
        return paramValues;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ff DOCUMENT ME!
     * @param writer DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void printAsDHTML(ResourceOutputFactory ff, Writer writer)
        throws Exception {
        _Job.printAsDHTML2(doc, writer, ff, paramValues);
    }

    /**
     * DOCUMENT ME!
     *
     * @param paramName DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void setParameter(String paramName, Object value) {
        paramValues.put(paramName, value);
    }

    /**
     * DOCUMENT ME!
     *
     * @param map DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static ReportJob createJob(Map map) throws Exception {
        InputStream is = null;

        String file = (String) map.get("file");
        String xml = (String) map.get("filexml");

        if (file != null) {
            if (file.equals("_") && (xml != null)) {
                is = new ByteArrayInputStream(xml.getBytes());
            } else {
                try {
                    File f = new File(file);
                    is = new FileInputStream(f);
                } catch (FileNotFoundException e) {
                    throw new Exception("\u6253\u4E0D\u5F00\u6307\u5B9A\u6A21\u677F: ");
                }
            }
        } else {
            throw new Exception("\u8BF7\u6307\u5B9A\u62A5\u8868\u6A21\u677F\u6587\u4EF6");
        }

        ReportJob job = new ReportJob(is);

        job.paramValues = (Map) ((HashMap) map).clone();

        return job;
    }
    
    public void printAsRTF(OutputStream os) throws Exception {
        _Job.printAsRTF(os, doc, paramValues);
    }

    /**
     * DOCUMENT ME!
     *
     * @param os DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void printAsXLS(OutputStream os) throws Exception {
        printAsXLS(os, false);
    }

    private void printAsXLS(OutputStream os, boolean sheetable)
        throws Exception {
        _Job.printAsXls(os, doc, paramValues, sheetable);
    }

    /**
     * DOCUMENT ME!
     *
     * @param os DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void printAsXLSn(OutputStream os) throws Exception {
        printAsXLS(os, true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param param DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getParameter(String param) {
        return (paramValues == null) ? null : paramValues.get(param);
    }

    /**
     * DOCUMENT ME!
     *
     * @param path DOCUMENT ME!
     */
    public static void setServletPath(String path) {
        if (!path.endsWith(File.separator)) {
            path = path + File.separator;
        }

        _Job.setServletPath2(path);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getServletPath() {
        return _Job.getServletPath();
    }

    /**
     * DOCUMENT ME!
     *
     * @param job DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void toPrinter(PrinterJob job) throws Exception {
        _Job.toPrinter(job, doc, paramValues);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ReportDocument getDocument() {
        return this.doc;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getId() {
        return id;
    }
}
