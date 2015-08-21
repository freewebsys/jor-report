package jatools.designer.export;

import jatools.PageFormat;
import jatools.ReportDocument;
import jatools.core.view.DisplayStyleManager;
import jatools.core.view.PageView;
import jatools.engine.PrintConstants;
import jatools.engine.ReportJob;
import jatools.engine.export.doc.RtfExport;
import jatools.engine.export.html.HtmlExport;
import jatools.engine.export.pdf.PdfExport1;
import jatools.engine.export.xls.XlsExport;
import jatools.engine.imgloader.HtmlImageLoader;
import jatools.engine.printer.ReportPrinter;
import jatools.io.FileResourceOutputFactory;

import java.awt.Dimension;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Map;







/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.12 $
  */
public class LocalReportJob {
    private ReportDocument doc;
    private Map parameters;
    private File file;

    /**
    * Creates a new LocalReportJob object.
    *
    * @param doc DOCUMENT ME!
    * @param file DOCUMENT ME!
    */
    public LocalReportJob(ReportDocument doc, Map parameters, File file) {
        this.doc = doc;
        this.parameters = parameters;
        this.file = file;
        this.doc.validate();
    }

    /**
     * DOCUMENT ME!
     * @throws Exception
     */
    public void printAsDHTML() throws Exception {
    	parameters.put( "as","dhtml");
        ReportPrinter printer = new ReportPrinter(doc, parameters);
        printer.getScript().set(ReportPrinter.IMAGE_LOADER, new HtmlImageLoader(printer.getScript()));
        printer.print();

        PageView[] pages = printer.getPages();
        DisplayStyleManager styleManager = (DisplayStyleManager) printer.getScript()
                                                                        .get(PrintConstants.DISPLAY_STYLE_MANAGER);
        File tmpDir = new File(file.getParentFile().getAbsoluteFile(), file.getName() + ".tmp"); //
        FileResourceOutputFactory factory = new FileResourceOutputFactory(tmpDir);

        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), "UTF-8"); //
            HtmlExport exp = new HtmlExport(factory, writer, styleManager); //

            for (int i = 0; i < pages.length; i++) {
                exp.export(pages[i], i);
            }

            exp.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void printAsPDF() throws Exception {
       	parameters.put( "as","pdf");
        ReportPrinter printer = new ReportPrinter(doc, parameters);

        printer.print();

        PageView[] pages = printer.getPages();

        try {
            Dimension size = null;

            if (pages.length > 0) {
                PageFormat pf = pages[0].getPageFormat();
                size = new Dimension(pf.getWidth(), pf.getHeight());
            }

            PdfExport1 exp = new PdfExport1(new FileOutputStream(file), size);

            for (int i = 0; i < pages.length; i++) {
                exp.export(pages[i], i);
            }

            exp.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     * @throws Exception
     */
    public void printAsXLS() throws Exception {
       	parameters.put( "as","xls");
        parameters.put(ReportJob.ALL_IN_ONE_PAGE, new Boolean(true));

        ReportPrinter printer = new ReportPrinter(doc, parameters);
        printer.print();

        DisplayStyleManager styleManager = (DisplayStyleManager) printer.getScript()
                                                                        .get(PrintConstants.DISPLAY_STYLE_MANAGER);
        PageView[] pages = printer.getPages();
        XlsExport exp = new XlsExport(new FileOutputStream(file), styleManager);

        for (int i = 0; i < pages.length; i++) {
            exp.export(pages[i], i, null);
        }

        exp.close();
    }

    /**
     * DOCUMENT ME!
     * @throws Exception
     */
    public void printAsXLS1() throws Exception {
       	parameters.put( "as","xls1");
        ReportPrinter printer = new ReportPrinter(doc, parameters);
        printer.print();

        DisplayStyleManager cssmanager = (DisplayStyleManager) printer.getScript()
                                                                      .get(PrintConstants.DISPLAY_STYLE_MANAGER);
        PageView[] pages = printer.getPages();
        XlsExport exp = new XlsExport(new FileOutputStream(file), cssmanager);

        for (int i = 0; i < pages.length; i++) {
            exp.export(pages[i], i, null);
        }

        exp.close();
    }
    
    public void printAsRTF() throws Exception {
       	parameters.put( "as","rtf");
        ReportPrinter printer = new ReportPrinter(doc, parameters);
        printer.print();

        PageView[] pages = printer.getPages();
        Dimension size = null;

        if (pages.length > 0) {
            PageFormat pf = pages[0].getPageFormat();
            size = new Dimension(pf.getWidth(), pf.getHeight());
        }

        RtfExport exp = new RtfExport(new FileOutputStream(file), size);

        for (int i = 0; i < pages.length; i++) {
            exp.export(pages[i], i);
        }

        exp.close();
    }


    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
 
    /**
     * DOCUMENT ME!
     *
     * @param file DOCUMENT ME!
     * @throws Exception
     */


}
