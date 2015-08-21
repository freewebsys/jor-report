package jatools.engine.export.runnable;

import jatools.core.view.PageView;
import jatools.engine.export.BasicExport;
import jatools.engine.export.xls.XlsExport;

import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class XlsRunnable extends ExportRunnable {
    public XlsExport exp;

    /**
     * Creates a new XlsRunnable object.
     *
     * @param exp DOCUMENT ME!
     */
    public XlsRunnable(XlsExport exp) {
        this.exp = exp;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public BasicExport getExport() {
        return this.exp;
    }

    /**
     * DOCUMENT ME!
     */
    public void run() {
        try {
            if (!tryCache(view, index)) {
                exp.export(view, index, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    ExportRunnable clone(PageView page, int index) {
        XlsRunnable r = new XlsRunnable(exp);
        r.view = page;
        r.index = index;

        return r;
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void close() throws Exception {
        if (cache != null) {
            for (Iterator it = cache.iterator(); it.hasNext();) {
                Runner runner = (Runner) it.next();
                exp.export(runner.view, runner.index, null);
            }
        }
    }
}
