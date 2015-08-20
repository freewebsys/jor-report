package jatools.server;

import jatools.designer.App;
import jatools.engine.ReportJob;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class JobCacher {
    static int i = 0;

    /**
     * DOCUMENT ME!
     *
     * @param session DOCUMENT ME!
     * @param jobSessionId DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static ReportJob callJob(HttpSession session, String jobSessionId)
        throws Exception {
        Map params = (Map) session.getAttribute(jobSessionId);

        if (params == null) {
            throw new Exception(App.messages.getString("res.43"));
        }

        return ReportJob.createJob(params);
    }

    /**
     * DOCUMENT ME!
     *
     * @param session DOCUMENT ME!
     * @param job DOCUMENT ME!
     */
    public static void doCache(HttpSession session, ReportJob job) {
        if ((ReportJob.USE_SESSION2)) {
            Map parameters = (Map) ((HashMap) job.getCachedParameters()).clone();
            
            session.setAttribute(job.getId(), parameters);
        }
    }
}
