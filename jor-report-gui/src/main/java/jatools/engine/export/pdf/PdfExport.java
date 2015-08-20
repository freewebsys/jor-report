package jatools.engine.export.pdf;

import jatools.PageFormat;
import jatools.core.view.PageView;
import jatools.engine.System2;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.io.OutputStream;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PdfExport {
    static boolean DEBUG = false;
    static int PAGE_LIMITS = 200;
    static DefaultFontMapper mapper;
    PdfWriter writer;
    Document document;
    private OutputStream os;

    /**
     * Creates a new PdfExport object.
     *
     * @param os DOCUMENT ME!
     * @param size DOCUMENT ME!
     */
    public PdfExport(OutputStream os, Dimension size) {
        if (mapper == null) {
            mapper = new DefaultFontMapper();

            String fontDirs = System2.getProperty("font.dirs");
            String[] dirs = fontDirs.split(";");

            for (int i = 0; i < dirs.length; i++) {
                String dir = dirs[i].trim();

                if (!dir.equals("")) {
                    mapper.insertDirectory(dir);
                }
            }
        }

        document = new Document(new com.lowagie.text.Rectangle(209.97f, 296.97f), 0f, 0f, 0f, 0);

        if (DEBUG) {
            System.out.println("now document is null ? " + (document == null) + "\n");
        }

        this.os = os;
    }

    /**
     * Creates a new PdfExport object.
     *
     * @param os DOCUMENT ME!
     */
    public PdfExport(OutputStream os) {
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
        try {
            PageFormat pf = pp.getPageFormat();

            float w = pf.getWidth() * PageFormat.DOTS_PER_PX;
            float h = pf.getHeight() * PageFormat.DOTS_PER_PX;

            if (writer == null) {
                /*  21 cm / 2.54 = 8.2677 inch
                       8.2677 * 72 = 595 points
                       29.7 cm / 2.54 = 11.6929 inch
                       11.6929 * 72 = 842 points*/
                document.setPageSize(new com.lowagie.text.Rectangle(w, h));
                document.setMargins(0f, 0f, 0f, 0f);

                try {
                    writer = PdfWriter.getInstance(document, os);
                } catch (DocumentException ex) {
                    if (DEBUG) {
                        System.out.println("get writer error!\n");
                    }

                    ex.printStackTrace();
                }

                document.open();
            } else {
                document.newPage();
            }

            if (DEBUG) {
                System.out.println("get writer success!\n");
            }

            PdfContentByte cb = writer.getDirectContent();
            PdfTemplate tp = cb.createTemplate(w, h);
            Graphics2D g2 = tp.createGraphics(w, h, mapper);
            g2.scale(PageFormat.DOTS_PER_PX, PageFormat.DOTS_PER_PX);

            pp.print(g2, null, 0);

            if (DEBUG) {
                System.out.println("page render success!\n");
            }

            g2.dispose();

            tp.setWidth(w);
            tp.setHeight(h);
            cb.addTemplate(tp, 0, 0);

            writer.flush();
        } catch (DocumentException e) {
            if (DEBUG) {
                System.out.println("after page render error!\n");
            }

            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void close() {
        document.close();

        writer.close();
    }
}
