package jatools.engine.export.pdf;

import jatools.core.view.PageView;
import jatools.engine.export.BasicExport;

import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfWriter;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class PdfExport1 extends BasicExport {
    static boolean DEBUG = false;
    static int PAGE_LIMITS = 50;
    PdfExport currentExport;
    ArrayList cachedFiles;
    private OutputStream os;
    private Dimension size;
    int count = 0;
    File tempFile;
    OutputStream tempOutputStream;
    PdfExport tempExport;

    /**
     * Creates a new ZPdfExport object.
     *
     * @param os DOCUMENT ME!
     * @param size DOCUMENT ME!
     */
    public PdfExport1(OutputStream os, Dimension size) {
        this.os = os;
        this.size = size;
    }

    /**
     * Creates a new ZPdfExport object.
     *
     * @param os DOCUMENT ME!
     */
    public PdfExport1(OutputStream os) {
        this(os, null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param pp DOCUMENT ME!
     * @param i DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void export(PageView pp, int i) throws Exception {
        if (this.isCached()) {
            if (tempExport == null) {
                this.createTempExport();
            } else if (count > PAGE_LIMITS) {
                this.closeTempExport();
                this.createTempExport();
                count = 0;
            }
        } else if (currentExport == null) 
         {
            currentExport = new PdfExport(os, size);
        }

        currentExport.export(pp, i);
        count++;
    }

    /**
     * DOCUMENT ME!
     */
    public void close() {
        if (this.isCached()) {
            closeTempExport();
            mergePdfs();
        } else {
            this.currentExport.close();
        }
    }

    private void mergePdfs() {
        Document document = null;

        PdfWriter writer = null;

        for (int i = 0; i < this.cachedFiles.size(); i++) {
            try {
                PdfReader reader = new PdfReader((String) this.cachedFiles.get(i));

                if (document == null) {
                    document = new Document(reader.getPageSize(1));
                    writer = PdfWriter.getInstance(document, os);
                    document.open();
                }

                PdfContentByte cb = writer.getDirectContent();

                int pageCount = reader.getNumberOfPages();

                for (int j = 0; j < pageCount; j++) {
                    document.newPage();
                    cb.addTemplate(writer.getImportedPage(reader, j + 1), 0, 0);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        document.close();
        writer.close();
    }

    void createTempExport() {
        try {
            tempFile = File.createTempFile("tmp", ".pdf");
            tempOutputStream = new FileOutputStream(tempFile);
            tempExport = new PdfExport(tempOutputStream, size);

            if (cachedFiles == null) {
                cachedFiles = new ArrayList();
            }

            cachedFiles.add(tempFile.getAbsolutePath());
            currentExport = tempExport;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void closeTempExport() {
        if (tempExport != null) {
            tempExport.close();
            tempExport = null;
        }

        if (tempOutputStream != null) {
            try {
                tempOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            tempOutputStream = null;
        }
    }
}
