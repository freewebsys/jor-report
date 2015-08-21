package jatools.core.view;

import jatools.designer.App;
import jatools.formatter.DecimalFormat;
import jatools.formatter.Format2;

import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jxl.biff.DisplayFormat;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.UnderlineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.DateFormat;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class DisplayStyleManager {
    public static final String NORMAL_CLASS_PREFIX = "c";
    public static final String DIV_CLASS_PREFIX = "d";
    public static final String TD_CLASS_PREFIX = "t";
    int id = 0;
    ArrayList csses = new ArrayList();
    WritableWorkbook w;
    Map thisColors = new HashMap();
    WritableCellFormat[] xlsCSS;
    Map formatsCache;
    Map stockStyles;
    ArrayList newcomers = new ArrayList();
    private int bookmark;

    /**
     * Creates a new DisplayStyleManager object.
     *
     * @param styles DOCUMENT ME!
     */
    public DisplayStyleManager(ArrayList styles) {
        this.setStockStyles(styles);
    }

    void setStockStyles(ArrayList styles) {
        if ((styles != null) && !styles.isEmpty()) {
            Iterator it = styles.iterator();
            this.stockStyles = new HashMap();

            while (it.hasNext()) {
                DisplayStyle style = new DisplayStyleWrapper(this, (DisplayStyle) it.next());

                style.setId(id++);
                this.stockStyles.put(style.getName(), style);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int generateId() {
        return id++;
    }

    /**
     * DOCUMENT ME!
     *
     * @param css DOCUMENT ME!
     */
    public void add(DisplayStyle css) {
        csses.add(css);
        css.setId(id++);
        newcomers.add(css);
    }

    /**
     * DOCUMENT ME!
     */
    public void setBookmark() {
        this.bookmark = csses.size();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DisplayStyle[] getStylesSinceBookmark() {
        return (DisplayStyle[]) this.csses.subList(this.bookmark, this.csses.size())
                                          .toArray(new DisplayStyle[0]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param css DOCUMENT ME!
     */
    public void add(DisplayStyle[] css) {
        if (css != null) {
            for (int i = 0; i < css.length; i++) {
                add(css[i]);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Colour getColour(Color color) {
        if (color == null) {
            return null;
        } else {
            Colour clr = (Colour) thisColors.get(color);

            if ((clr == null) && (thisColors.size() < 55)) {
                clr = Colour.getInternalColour(0x3f - thisColors.size());
                w.setColourRGB(clr, color.getRed(), color.getGreen(), color.getBlue());
                thisColors.put(color, clr);
            }

            return clr;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param css DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public WritableCellFormat getCellFormat(DisplayStyle css) {
        if ((csses != null) && !csses.isEmpty()) {
            generateXlsCSS();
        }

        int i = css.getId();

        if ((i > -1) && (i < xlsCSS.length)) {
            return xlsCSS[i];
        } else {
            System.out.println(App.messages.getString("res.631") + (xlsCSS.length - 1) + "] " + i);

            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param _html_report_id DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toHtmlCSS() {
        StringBuffer styles = new StringBuffer();
        Iterator it = csses.iterator();

        while (it.hasNext()) {
            DisplayStyle css = (DisplayStyle) it.next();

            styles.append(CSSBuilder.build(css, ""));
        }

        return styles.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param out DOCUMENT ME!
     */
    public void write(Writer out) {
        if (!this.newcomers.isEmpty()) {
            try {
                out.write("<style>\n");

                Iterator it = this.newcomers.iterator();

                while (it.hasNext()) {
                    DisplayStyle css = (DisplayStyle) it.next();

                    out.write(CSSBuilder.build(css, ""));
                }

                out.write("</style>");
            } catch (IOException e) {
                e.printStackTrace();
            }

            this.newcomers.clear();
        }
    }

   
    private void generateXlsCSS() {
        if ((csses == null) || csses.isEmpty()) {
            return;
        }

        if (xlsCSS == null) {
            xlsCSS = new WritableCellFormat[id + 1];

            for (int i = 0; i < csses.size(); i++) {
                DisplayStyle style = (DisplayStyle) csses.get(i);
                xlsCSS[style.getId()] = generateFormat(style);
            }
        }
    }

    private WritableCellFormat generateFormat(DisplayStyle css) {
        WritableCellFormat cf = null;

        Format2 f = css.getFormat();

        if (f != null) {
            if (formatsCache == null) {
                formatsCache = new HashMap();
            }

            DisplayFormat format = (DisplayFormat) formatsCache.get(f.toExcel());

            if (format == null) {
                if (f instanceof DecimalFormat) {
                    format = new NumberFormat(f.toExcel());
                } else {
                    format = new DateFormat(f.toExcel());
                }

                formatsCache.put(f.toExcel(), format);
            }

            cf = new WritableCellFormat(format);
        } else {
            cf = new WritableCellFormat();
        }

        Font font = css.getFont();

        WritableFont wf = new WritableFont(WritableFont.createFont(font.getFontName()),
                Math.round(font.getSize() * 0.75f),
                font.isBold() ? WritableFont.BOLD : WritableFont.NO_BOLD, font.isItalic(),
                UnderlineStyle.NO_UNDERLINE);

        try {
            Colour forecolor = getColour(css.getForeColor());

            if (forecolor != null) {
                wf.setColour(forecolor);
            }

            cf.setFont(wf);

            Alignment ha = Alignment.LEFT;

            switch (css.getHorizontalAlignment()) {
            case TextView.LEFT: {
                ha = Alignment.LEFT;

                break;
            }

            case TextView.CENTER: {
                ha = Alignment.CENTRE;

                break;
            }

            case TextView.RIGHT: {
                ha = Alignment.RIGHT;

                break;
            }
            }

            cf.setAlignment(ha);

            VerticalAlignment va = VerticalAlignment.BOTTOM;

            switch (css.getVerticalAlignment()) {
            case TextView.TOP: {
                va = VerticalAlignment.TOP;

                break;
            }

            case TextView.MIDDLE: {
                va = VerticalAlignment.CENTRE;

                break;
            }

            case TextView.BOTTOM: {
                va = VerticalAlignment.BOTTOM;

                break;
            }
            }

            cf.setVerticalAlignment(va);

            if (css.isWordwrap()) {
                cf.setWrap(true);
            }

            Colour backcolor = getColour(css.getBackColor());

            if (backcolor != null) {
                cf.setBackground(backcolor);
            }

            if (css.getBorder() != null) {
                jatools.core.view.Border border = css.getBorder();

                if (border != null) {
                    setBorder(cf, border.getTopStyle(), jxl.format.Border.TOP);
                    setBorder(cf, border.getLeftStyle(), jxl.format.Border.LEFT);
                    setBorder(cf, border.getBottomStyle(), jxl.format.Border.BOTTOM);
                    setBorder(cf, border.getRightStyle(), jxl.format.Border.RIGHT);
                }
            }
        } catch (WriteException e) {
            e.printStackTrace();
        }

        return cf;
    }

    private void setBorder(WritableCellFormat cf, BorderStyle style, Border side) {
        if (style != null) {
            BorderLineStyle linestyle = null;

            if (style.getStyle().equals("dotted")) {
                linestyle = BorderLineStyle.DOTTED;
            } else if (style.getStyle().equals("dashed")) {
                linestyle = BorderLineStyle.DASHED;
            } else if (style.getThickness() < 2) {
                linestyle = BorderLineStyle.THIN;
            } else {
                linestyle = BorderLineStyle.MEDIUM;
            }

            Colour bordercolor = getColour(style.getColor());

            try {
                cf.setBorder(side, linestyle, bordercolor);
            } catch (WriteException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param ws DOCUMENT ME!
     */
    public void setWorkbook(WritableWorkbook ws) {
        this.w = ws;
    }

    /**
     * DOCUMENT ME!
     *
     * @param styleRef DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DisplayStyle getStockStyle(String styleRef) {
        return (DisplayStyle) this.stockStyles.get(styleRef);
    }
}
