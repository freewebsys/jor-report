package jatools.tags;

import jatools.designer.App;
import jatools.engine.ReportJob;
import jatools.io.DefaultResourceOutputFactory;
import jatools.server.JobCacher;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

import org.apache.log4j.Logger;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ReportTag extends AbstractReportTag {
    static Logger logger = Logger.getLogger("jatools.tags.ReportTag");
    private String parameters;
    private String template;
    private ContainerTag container;
   
    private Map parametersCache;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ContainerTag getContainer() {
        return container;
    }

    /**
     * DOCUMENT ME!
     *
     * @param container DOCUMENT ME!
     */
    public void setContainer(ContainerTag container) {
        this.container = container;
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param val DOCUMENT ME!
     */
    public void setParameter(String key, Object val) {
        if (parametersCache == null) {
            parametersCache = new HashMap();
        }

        this.parametersCache.put(key, val);
    }

   

    /**
     * DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public void writeOut() throws IOException {
        writeOut(this.getBodyContent());
    }

    void writeOut(Writer out) throws IOException {
        try {
            HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
            InputStream is = null;
            String xmluuid = (String) request.getParameter("xmluuid");

            if (xmluuid != null) {
                String xml = (String) request.getSession().getServletContext().getAttribute(xmluuid);

                if (xml != null) {
                    is = new ByteArrayInputStream(xml.getBytes("UTF-8"));
                }
            } else if (template != null) {
                try {
                    File f = new File(template);
                    is = new FileInputStream(f);
                } catch (FileNotFoundException e) {
                    throw new Exception("\u6253\u4E0D\u5F00\u6307\u5B9A\u6A21\u677F: ");
                }
            }

            ReportJob job = null;

            if (is == null) {
                job = JobCacher.callJob(request.getSession(),
                        request.getParameter(ReportJob.JOB_SESSION_ID));
            } else {
                job = new ReportJob(is);
            }

            for (Enumeration en = request.getParameterNames(); en.hasMoreElements();) {
                String name = (String) en.nextElement();
                job.setParameter(name, request.getParameter(name));
            }

            if (parameters != null) {
                String[] params = parameters.split("&");

                for (int i = 0; i < params.length; i++) {
                    String[] entry = params[i].split("=");

                    if (entry.length == 2) {
                        job.setParameter(entry[0], entry[1]);
                    }
                }
            }

            if (this.parametersCache != null) {
                Iterator it = this.parametersCache.keySet().iterator();

                while (it.hasNext()) {
                    String key = (String) it.next();
                    Object val = this.parametersCache.get(key);

                    if (logger.isDebugEnabled()) {
                        logger.debug(App.messages.getString("res.11") + key + " = " + val);
                    }

                    job.setParameter(key, val);
                }
            }

            job.setParameter("file", template);
            job.setParameter("as", "dhtml");
            job.setParameter(ReportJob.HTML_BODY_ONLY, "true");
            job.setParameter(ReportJob.HTML_REPORT_ID, "j" + id);

            job.setParameter(ReportJob.HTTP_REQUEST, request);

            if (true || ReportJob.USE_SESSION2) {
                job.setParameter(ReportJob.HTTP_SESSION2, request.getSession());
            }

            job.printAsDHTML(DefaultResourceOutputFactory.createInstance(pageContext.getSession()),
                out);

            if (true || ReportJob.USE_SESSION2) {
                JobCacher.doCache(pageContext.getSession(), job);
            }

            job = null;
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException(e.getMessage());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws JspException DOCUMENT ME!
     */
    public int doEndTag() throws JspException {
        try {
           

            this.getBodyContent().writeOut(this.getPreviousOut());
        } catch (IOException e) {
            e.printStackTrace();
            throw new JspException(e);
        }

        return EVAL_PAGE;
    }

    boolean cacheRequired() {
        return (this.container != null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param template DOCUMENT ME!
     */
    public void setTemplate(String template) {
        this.template = template;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parameters DOCUMENT ME!
     */
    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws JspException DOCUMENT ME!
     */
    public int doStartTag() throws JspException {
       
        this.parametersCache = null;
        pageContext.setAttribute(id, this);

        int results = super.doStartTag();

        return results;
    }

   
}
