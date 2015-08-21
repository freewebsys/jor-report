package jatools.engine;

import jatools.PageFormat;
import jatools.ReportDocument;

import jatools.core.view.DisplayStyleManager;
import jatools.core.view.PageView;

import jatools.data.reader.sql.Connection;

import jatools.designer.App;

import jatools.engine.export.doc.RtfExport;
import jatools.engine.export.html.HtmlExport;
import jatools.engine.export.pdf.PdfExport1;
import jatools.engine.export.runnable.ExportRunnable;
import jatools.engine.export.runnable.HtmlRunnable;
import jatools.engine.export.runnable.PageCollectionRunnable;
import jatools.engine.export.runnable.PdfRunnable;
import jatools.engine.export.runnable.RtfRunnable;
import jatools.engine.export.runnable.XlsRunnable;
import jatools.engine.export.xls.XlsExport;

import jatools.engine.printer.ReportCacher;
import jatools.engine.printer.ReportPrinter;

import jatools.io.ResourceOutputFactory;

import jatools.util.Util;

import org.apache.log4j.Logger;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStream;
import java.io.Writer;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpSession;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class _Job {
    private static Logger logger = Logger.getLogger("ZReportJob");

    /**
     * DOCUMENT ME!
     *
     * @param path DOCUMENT ME!
     */
    public static void setServletPath2(String path) {
        System2.setServletPath2(path);
    }

    /**
     * DOCUMENT ME!
     *
     * @param os DOCUMENT ME!
     * @param doc DOCUMENT ME!
     * @param paramValues DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void printAsPDF(OutputStream os, ReportDocument doc, Map paramValues)
        throws Exception {
        PdfExport1 exp = new PdfExport1(os);

        generatePage(doc, paramValues, new PdfRunnable(exp), null);

        exp.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @param doc DOCUMENT ME!
     * @param writer DOCUMENT ME!
     * @param factory DOCUMENT ME!
     * @param paramValues DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void printAsDHTML2(ReportDocument doc, Writer writer,
        ResourceOutputFactory factory, Map paramValues)
        throws Exception {
        if (factory == null) {
            throw new Exception(App.messages.getString("res.48"));
        }

        HtmlExport exp = null;

        DisplayStyleManager csser = new DisplayStyleManager(null);

        exp = new HtmlExport(factory, writer, csser);

        ReportCacher cacher = new ReportCacher();
        paramValues.put(ReportJob.HTML_REPORT_CACHER, cacher);

        exp.setOptions(paramValues);

        if (doc.getTitle() != null) {
            paramValues.put(ReportJob.HTML_TITLE, Connection.eval(null, doc.getTitle()));
        }

        ExportRunnable runner = new HtmlRunnable(exp);
        generatePage(doc, paramValues, runner, csser);

        exp.flush();

        if (cacher.isRequired() && (ReportJob.USE_SESSION2)) {
            HttpSession session = (HttpSession) paramValues.get(ReportJob.HTTP_SESSION2);

            if (session != null) {
                session.setAttribute(cacher.getUUID(), paramValues);
            }
        }
    }

    private static void generatePage(ReportDocument doc, Map paramValues, ExportRunnable runner,
        DisplayStyleManager styleManager) throws Exception {
        if (doc == null) {
            throw new Exception(Util.debug(logger, "doc is null"));
        }

        doc.validate();

        int count = 0;
        PageView[] pages = null;

        try {
            ReportPrinter printer = new ReportPrinter(doc, paramValues, styleManager);
            runner.init(printer);
            printer.print();

            pages = printer.getPages();

            for (int i = 0; i < pages.length; i++) {
                runner.view = pages[i];
                runner.index = i;
                runner.run();
                count++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        if (count == 0) {
            throw new Exception(App.messages.getString("res.49"));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param os DOCUMENT ME!
     * @param doc DOCUMENT ME!
     * @param paramValues DOCUMENT ME!
     * @param sheetable DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void printAsXls(OutputStream os, ReportDocument doc, Map paramValues,
        boolean sheetable) throws Exception {
        DisplayStyleManager csser = new DisplayStyleManager(null);
        XlsExport exp = new XlsExport(os, csser);

        if (!sheetable) {
            paramValues.put(ReportJob.ALL_IN_ONE_PAGE, Boolean.TRUE);
        }

        generatePage(doc, paramValues, new XlsRunnable(exp), csser);
        exp.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @param os DOCUMENT ME!
     * @param doc DOCUMENT ME!
     * @param paramValues DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void printAsRTF(OutputStream os, ReportDocument doc, Map paramValues)
        throws Exception {
        PageFormat pf = doc.getPage().getPageFormat();

        RtfExport exp = new RtfExport(os, new Dimension(pf.getWidth(), pf.getHeight()));

        generatePage(doc, paramValues, new RtfRunnable(exp), null);

        exp.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @param job DOCUMENT ME!
     * @param doc DOCUMENT ME!
     * @param paramValues DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void toPrinter(PrinterJob job, ReportDocument doc, Map paramValues)
        throws Exception {
        ArrayList pages = new ArrayList();

        generatePage(doc, paramValues, new PageCollectionRunnable(pages), null);

        if (!pages.isEmpty()) {
            if (pages.isEmpty()) {
                throw new Exception(App.messages.getString("res.50"));
            } else {
                PageView[] pps = (PageView[]) pages.toArray(new PageView[0]);
                job.setPrintable(new PagePrintable(pps), pps[0].getPageFormat().toAwtFormat(true));
                job.print();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param os DOCUMENT ME!
     * @param jgos DOCUMENT ME!
     * @param type DOCUMENT ME!
     */
    public static void exportByJgos(OutputStream os, File[] jgos, String type) {
        ArrayList pages = new ArrayList();

        for (int i = 0; i < jgos.length; i++) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(jgos[i]));
                PageView pv = (PageView) ois.readObject();
                pages.add(pv);
                ois.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getServletPath() {
        return System2.getWorkingDirectory();
    }

    static class PagePrintable implements Printable {
        private PageView[] views;

        PagePrintable(PageView[] views) {
            this.views = views;
        }

        public int print(Graphics g, java.awt.print.PageFormat pageFormat, int pi)
            throws PrinterException {
            if (pi < views.length) {
                Graphics2D g2 = (Graphics2D) g;
                g2.scale(PageFormat.DOTS_PER_PX, PageFormat.DOTS_PER_PX);
                views[pi].paint(g2);

                return PAGE_EXISTS;
            } else {
                return NO_SUCH_PAGE;
            }
        }
    }
}
