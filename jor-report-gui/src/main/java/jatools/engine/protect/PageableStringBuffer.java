package jatools.engine.protect;

import jatools.accessor.ProtectPublic;
import jatools.core.view.Border;
import jatools.core.view.BorderStyle;
import jatools.util.StringUtil;

import java.awt.Rectangle;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class PageableStringBuffer implements ProtectPublic {
    static int MAX_CAP = 2000000;
    private StringBuffer buffer;
    private int pageChars;
    private Writer writer;
    private File pageFile;
    boolean closed = false;

    /**
     * Creates a new PageableStringBuffer object.
     *
     * @param pageChars DOCUMENT ME!
     */
    public PageableStringBuffer(int pageChars) {
        buffer = new StringBuffer((pageChars > MAX_CAP) ? MAX_CAP : pageChars);
        this.pageChars = pageChars;
    }

    /**
     * Creates a new PageableStringBuffer object.
     *
     * @param pageChars DOCUMENT ME!
     * @param writer DOCUMENT ME!
     */

    //    public PageableStringBuffer(int pageChars, Writer writer) {
    //        buffer = new StringBuffer((pageChars > MAX_CAP) ? MAX_CAP : pageChars);
    //        this.pageChars = pageChars;
    //        this.writer = writer;
    //    }

    /**
     * DOCUMENT ME!
     *
     * @param string DOCUMENT ME!
     */
    public void append(String string) {
        if (buffer.length() > pageChars) {
            
            if (writer == null) {
                try {
                    writer = new FileWriter2(pageFile = FileUtil.createTempFile("htm", ".ch"));
                    
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            try {
                writer.write(buffer.toString());
                buffer = new StringBuffer(pageChars);

                
            } catch (IOException e) {
                // MYDO Auto-generated catch block
                e.printStackTrace();
            }
        }

        buffer.append(string);
    }

    /**
     * DOCUMENT ME!
     *
     * @param body1 DOCUMENT ME!
     */
    public void append(StringBuffer body1) {
        append(body1.toString());
    }

    /**
     * DOCUMENT ME!
     *
     * @param _writer DOCUMENT ME!
     */
    public void writeTo(Writer _writer) {
        if (writer != null) {
            try {
                writer.close();

                FileReader2 reader = new FileReader2(pageFile);
                char[] b = new char[4096];
                int size = 0;

                while ((size = reader.read(b)) != -1) {
                    _writer.write(b, 0, size);
                }

                
            } catch (Exception e) {
                // MYDO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try {
            _writer.write(buffer.toString());
        } catch (IOException e) {
            // MYDO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void flush() {
        try {
            writer.write(buffer.toString());
            buffer = new StringBuffer(pageChars);
        } catch (IOException e) {
            // MYDO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void openDiv() {
        append("<div ");
    }

    /**
     * DOCUMENT ME!
     */
    public void closeDiv() {
        append("</div>");
    }

    /**
     * DOCUMENT ME!
     */
    public void openStyle() {
        append(" style='");
    }

    /**
     * DOCUMENT ME!
     */
    public void closeStyle() {
        append("' ");
    }

    /**
     * DOCUMENT ME!
     */
    public void openClass() {
        append(" class='");
    }

    /**
     * DOCUMENT ME!
     */
    public void closeClass() {
        append("' ");
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    public void append(Rectangle b) {
        append(StringUtil.format("left:#;top:#;width:#;height:#;", b.x + "", b.y + "",
                b.width + "", b.height + ""));
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    public void appendNoHeight(Rectangle b) {
        append(StringUtil.format("left:#;top:#;width:#;", b.x + "", b.y + "", b.width + ""));
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     * @param border DOCUMENT ME!
     */
    public void appendNoHeight(Rectangle b, Border border) {
        appendNoHeight(getRectangle(b, border));
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     * @param border DOCUMENT ME!
     */
    public void append(Rectangle b, Border border) {
        append(getRectangle(b, border));
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     * @param border DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTLStyle(Rectangle b, Border border) {
        b = getRectangle(b, border);

        return StringUtil.format("left:#;top:#;", b.x + "", b.y + "");
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     * @param border DOCUMENT ME!
     */
    public void appendWH(Rectangle b, Border border) {
        appendWH(getRectangle(b, border));
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     */
    public void appendWH(Rectangle b) {
        append(StringUtil.format("width:#;height:#;", b.width + "", b.height + ""));
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     * @param border DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Rectangle getRectangle(Rectangle b, Border border) {
        if (border != null) {
            b = (Rectangle) b.clone();

            BorderStyle left = border.getLeftStyle();

            if (left != null) {
                b.x -= left.getThickness();
                b.width += left.getThickness();
            }

            BorderStyle top = border.getTopStyle();

            if (top != null) {
                b.y -= top.getThickness();
                b.height += top.getThickness();
            }
        }

        return b;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public StringBuffer getBuffer() {
        return buffer;
    }
}


class FileReader2 extends InputStreamReader {
    /**
     * Creates a new FileReader2 object.
     *
     * @param file DOCUMENT ME!
     *
     * @throws FileNotFoundException DOCUMENT ME!
     * @throws UnsupportedEncodingException DOCUMENT ME!
     */
    public FileReader2(File file) throws FileNotFoundException, UnsupportedEncodingException {
        super(new FileInputStream(file), "UTF-8");
    }
}


class FileWriter2 extends OutputStreamWriter {
    /**
     * Creates a new FileReader2 object.
     *
     * @param file DOCUMENT ME!
     *
     * @throws FileNotFoundException DOCUMENT ME!
     * @throws UnsupportedEncodingException DOCUMENT ME!
     */
    public FileWriter2(File file) throws FileNotFoundException, UnsupportedEncodingException {
        super(new FileOutputStream(file), "UTF-8");
    }
}
