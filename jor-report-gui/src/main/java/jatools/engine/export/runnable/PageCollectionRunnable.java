package jatools.engine.export.runnable;

import jatools.core.view.PageView;
import jatools.engine.export.BasicExport;

import java.util.ArrayList;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PageCollectionRunnable extends ExportRunnable {
    ArrayList pages;

    /**
     * Creates a new PageCollectionRunnable object.
     *
     * @param pages DOCUMENT ME!
     */
    public PageCollectionRunnable(ArrayList pages) {
        this.pages = pages;
    }

    /**
     * DOCUMENT ME!
     */
    public void run() {
        pages.add(view);
    }

    ExportRunnable clone(PageView page, int index) {
        PageCollectionRunnable r = new PageCollectionRunnable(pages);
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
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public BasicExport getExport() {
        return BasicExport.getInstance();
    }
}
