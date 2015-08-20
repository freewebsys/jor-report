package jatools.engine.export.runnable;

import jatools.core.view.PageView;
import jatools.engine.export.BasicExport;
import jatools.engine.export.html.HtmlExport;
import jatools.engine.imgloader.HtmlImageLoader;
import jatools.engine.printer.ReportPrinter;

import java.io.IOException;
import java.util.Iterator;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class HtmlRunnable extends ExportRunnable {
    HtmlExport exp;

    /**
     * Creates a new HtmlRunnable object.
     *
     * @param exp DOCUMENT ME!
     */
    public HtmlRunnable(HtmlExport exp) {
        this.exp = exp;
    }

    /**
     * DOCUMENT ME!
     */
    public void run() {
        try {
            if (!tryCache(view, index)) {
                exp.export(view, index);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    ExportRunnable clone(PageView page, int index1) {
        HtmlRunnable r = new HtmlRunnable(exp);
        r.view = page;
        r.index = index1;

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
                exp.export(runner.view, runner.index);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public BasicExport getExport() {
        return exp;
    }

    /**
     * DOCUMENT ME!
     *
     * @param printer DOCUMENT ME!
     */
    public void init(ReportPrinter printer) {
        printer.getScript().set(ReportPrinter.IMAGE_LOADER, new HtmlImageLoader(printer.getScript()));
    }
}
