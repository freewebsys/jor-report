package jatools.engine.export.runnable;

import jatools.core.view.PageView;
import jatools.engine.export.BasicExport;
import jatools.engine.printer.ReportPrinter;

import java.util.ArrayList;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public abstract class ExportRunnable implements Runnable, Comparable {
    public int index;
    public PageView view;
    ArrayList cache;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract BasicExport getExport();

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public abstract void close() throws Exception;

    protected boolean tryCache(PageView p, int index) {
        return cache != null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPageIndex() {
        return index;
    }

    abstract ExportRunnable clone(PageView page, int index1);

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compareTo(Object o) {
        ExportRunnable r = (ExportRunnable) o;

        if (getPageIndex() > r.getPageIndex()) {
            return 1;
        } else if (getPageIndex() < r.getPageIndex()) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param printer DOCUMENT ME!
     */
    public void init(ReportPrinter printer) {
    }
}


class Runner {
    int index;
    PageView view;

    /**
     * Creates a new Runner object.
     *
     * @param index DOCUMENT ME!
     * @param view DOCUMENT ME!
     */
    public Runner(int index, PageView view) {
        this.index = index;
        this.view = view;
    }
}
