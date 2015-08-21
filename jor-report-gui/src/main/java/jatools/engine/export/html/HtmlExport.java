package jatools.engine.export.html;

import jatools.PageFormat;

import jatools.component.ImageObjectFormat;
import jatools.component.ImageStyle;
import jatools.component.Size;

import jatools.core.view.AbstractView;
import jatools.core.view.Border;
import jatools.core.view.ClipView;
import jatools.core.view.CompoundView;
import jatools.core.view.DisplayStyleManager;
import jatools.core.view.ImageView;
import jatools.core.view.PageView;
import jatools.core.view.TextLine;
import jatools.core.view.TextView;
import jatools.core.view.TransformView;
import jatools.core.view.View;

import jatools.designer.App;

import jatools.engine.ReportJob;
import jatools.engine.System2;

import jatools.engine.export.BasicExport;

import jatools.engine.layout.TableView;

import jatools.engine.protect.PageableStringBuffer;

import jatools.io.ResourceOutputFactory;

import jatools.util.StringUtil;
import jatools.util.Util;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Rectangle;

import java.io.IOException;
import java.io.Writer;

import java.text.SimpleDateFormat;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class HtmlExport extends BasicExport {
    final static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
    final static SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    static boolean NO_PAGE_BORDER = System2.getProperty("html.no.page.border") != null;
    private static final String[] ALIGN_CLASS2 = { "tl", "t", "tr", "l", "cc", "r", "bl", "b", "br" };
    public static final String CSS_TEXT_ALIGN_LEFT = "left";
    public static final String CSS_TEXT_ALIGN_RIGHT = "right";
    public static final String CSS_TEXT_ALIGN_CENTER = "center";
    public static final String CSS_TEXT_ALIGN_JUSTIFY = "justify";
    public static final String HTML_VERTICAL_ALIGN_TOP = "top";
    public static final String HTML_VERTICAL_ALIGN_MIDDLE = "middle";
    public static final String HTML_VERTICAL_ALIGN_BOTTOM = "bottom";
    private static final String title = App.messages.getString("res.63");
    public static final int colorMask = Integer.parseInt("FFFFFF", 16);
    public static final int INCH = 96;
    public static final double DOTS_PER_MM = (double) INCH / (double) 25.4;
    protected ResourceOutputFactory factory;
    protected Writer out;
    private PageableStringBuffer body;
    private int index = 0;
    private Map options;
    private DisplayStyleManager styleManager;
    private boolean first = true;
    private boolean _html_body_only = false;
    private String _html_title;
    private String _html_background = "#AAAAAA";
    private int _html_offset = 10;
    private Map borderCache = new HashMap();
    private ImageEncoder imageEncoder;
    private PageFormat pageFormat;
    private String jobSessionId;
    private String _html_report_server_path;

    /**
     * Creates a new HtmlExport object.
     *
     * @param fc
     *            DOCUMENT ME!
     * @param writer
     *            DOCUMENT ME!
     * @param styleManager
     *            DOCUMENT ME!
     */
    public HtmlExport(ResourceOutputFactory fc, Writer writer, DisplayStyleManager styleManager) {
        this.out = writer;
        this.factory = fc;
        this.styleManager = styleManager;
    }

    private void getOptions() {
        if (options != null) {
            this._html_title = (String) options.get(ReportJob.HTML_TITLE);
            this._html_body_only = Util.boolValue(options.get(ReportJob.HTML_BODY_ONLY), false);
            this.jobSessionId = (String) options.get(ReportJob.JOB_SESSION_ID);

            this._html_report_server_path = (String) options.get(ReportJob.HTML_REPORT_SERVER_PATH);

            if ((this._html_report_server_path != null) &&
                    !this._html_report_server_path.endsWith("/")) {
                this._html_report_server_path += "/";
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param string
     *            DOCUMENT ME!
     * @param default_value
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Color getColor(String string, Color default_value) {
        Color color = null;

        try {
            if (string != null) {
                color = new Color(Integer.parseInt(string.substring(1, 3), 16),
                        Integer.parseInt(string.substring(3, 5), 16),
                        Integer.parseInt(string.substring(5, 7), 16));
            }
        } catch (RuntimeException e) {
        } finally {
            if (color == null) {
                color = default_value;
            }
        }

        return color;
    }

    /**
     * DOCUMENT ME!
     *
     * @param page
     *            DOCUMENT ME!
     * @param index1
     *            DOCUMENT ME!
     *
     * @throws IOException
     *             DOCUMENT ME!
     */
    public void export(PageView page, int index1) throws IOException {
        if (first) {
            getOptions();
            pageFormat = page.getPageFormat();
            body = new PageableStringBuffer(64000);

            first = false;
        }

        index++;

        body.append(StringUtil.format("<div id='_page_#' class='pcls' ", index + ""));
        body.append(StringUtil.format(" style='margin:#;width:#;height:#;", _html_offset + "",
                "" + page.getBounds().width, page.getBounds().height + ""));

        String backcolor = null;

        if (page.getBackColor() != null) {
            backcolor = getColor(page.getBackColor());
        } else {
            backcolor = "white";
        }

        body.append("background-color:" + backcolor + ";");

        if (page.getBackgroundImageStyle() != null) {
            body.append(getImageEncoder().encode(page.getBackgroundImageStyle()));
        }

        body.append("'>");
        body.append("<div class='jpage' id=\"page" + index + "\">");

        int y0 = page.getPadding().top;
        int x0 = page.getPadding().left;

        encode(page.getElements(), x0, y0);

        body.append("</div>");
        body.append("</div>");
    }

    private void encode(List elems, int x0, int y0) throws IOException {
        View e;
        Iterator it = elems.iterator();

        while (it.hasNext()) {
            e = (View) it.next();

            if (e instanceof TransformView) {
                x0 += ((TransformView) e).x;
                y0 += ((TransformView) e).y;

                continue;
            } else if (e instanceof ClipView && ((ClipView) e).isNull()) {
                body.append("</div>");

                continue;
            }

            Rectangle rect = ((AbstractView) e).getBounds();

            rect.x += x0;
            rect.y += y0;

            encode(e);

            rect.x -= x0;
            rect.y -= y0;
        }
    }

    private void encode(View e) throws IOException {
        if (e instanceof TextView) {
            encodeText((TextView) e, true);
        } else if (e instanceof ImageView) {
            encodeImage((ImageView) e);
        } else if (e instanceof ClipView) {
            encodeClip((ClipView) e);
        } else if (e instanceof TableView) {
            encodeTable((TableView) e);
        } else if (e instanceof CompoundView) {
            encodeCompound((CompoundView) e);
        } else {
            System.out.println("unknown view can not be dhtml encode ! " + e.getClass().getName());
        }
    }

    private ImageEncoder getImageEncoder() {
        if (imageEncoder == null) {
            this.imageEncoder = new ImageEncoder(this.factory);
        }

        return imageEncoder;
    }

    private void encodeTable(TableView tableView) throws IOException {
        encodeNormalTable(tableView, false, null, null, null);
    }

    private void encodeNormalTable(TableView tableView, boolean relative, String id, String cls,
        String attr2) throws IOException {
        boolean wrapped = tableView.getBackgroundImageStyle() != null;
        Rectangle save = null;

        if (wrapped) {
            body.append("<div style='");
            body.append(getImageEncoder().encode(tableView.getBackgroundImageStyle()));
            body.append(tableView.getBounds());
            body.append("'>");

            save = tableView.getBounds();

            Rectangle clone = (Rectangle) save.clone();
            clone.x = clone.y = 0;

            tableView.setBounds(clone);
        }

        body.append("<table ");

        if (id == null) {
            id = tableView.getName();
        }

        if (id != null) {
            body.append("id='" + id + "' ");
        }

        if (cls != null) {
            body.append("class='" + cls + "' ");
        }

        if (attr2 != null) {
            body.append(attr2);
        }

        body.append(" cellpadding='0' cellspacing='0' style='border-collapse:collapse;");

        if (relative) {
            body.append("position:relative;");
        }

        if (tableView.getBackColor() != null) {
            body.append("background-color:" + getColor(tableView.getBackColor()) + ";");
        }

        if (tableView.getBorder() != null) {
            body.append(tableView.getBorder().toString());
        }

        body.appendNoHeight(tableView.getBounds(), tableView.getBorder());

        body.append("'>");

        body.append("<colgroup>");

        Size size = tableView.getColumns();

        for (int i = 0; i < size.length(); i++) {
            body.append("<col width='" + size.getSize(i) + "'>");
        }

        body.append("</colgroup>");

        size = tableView.getRows();

        int groupSpan = 0;

        for (int i = 0; i < tableView.getRows().length(); i++) {
            boolean rowtag = false;

            if (!rowtag) {
                body.append("<tr ");
            }

            body.append(StringUtil.format("height='#'>", size.getSize(i) + ""));

            for (int j = 0; j < tableView.getColumns().length(); j++) {
                AbstractView view = tableView.getViewOver(i, j);

                if (view == null) {
                    body.append("<td/>");
                } else if ((view.getCell().row == i) && (view.getCell().column == j)) {
                    body.append("<td ");

                    String cellCls = this.getCellClass(view);

                    if (cellCls != null) {
                        body.append("class='" + cellCls + "' ");
                    }

                    // else {
                    // Border border = view.getBorder();
                    //
                    // if ((border != null)) {
                    // body.append("style='");
                    //
                    // if (border != null) {
                    // body.append(border.toString());
                    // }
                    //
                    // // if (view.getCell().rowSpan > 1) {
                    // // body.append(StringUtil.format("line-height:#px;",
                    // // "" +
                    // // size.getSize(view.getCell().row,
                    // view.getCell().rowSpan)));
                    // // }
                    //
                    // body.append("' ");
                    // }
                    // }
                    if (view.getCell().rowSpan > 1) {
                        body.append(" rowSpan='" + view.getCell().rowSpan + "' ");
                    }

                    if (view.getCell().colSpan > 1) {
                        body.append("colSpan='" + view.getCell().colSpan + "' ");
                    }

                    body.append(">");

                    boolean encoded = false;

                    if (!encoded) {
                        encode(view);
                    }

                    body.append("</td>");
                }
            }

            body.append("</tr>");
        }

        body.append("</table>");

        if (wrapped) {
            body.append("</div>");
            tableView.setBounds(save);
        }
    }

    private void encodeCompound(CompoundView p) throws IOException {
        if (p.hasShapes()) {
        	p.getBounds().height -=2;
            encodeImage(p.asImageView());
            p.getBounds().height +=2;
        } else {
            Rectangle b = p.getBounds();

            body.openDiv();

            if (p.getCell() != null) {
                body.append("class='td' ");
            }

            body.openStyle();

            if (p.getBackColor() != null) {
                body.append("background-color:" + getColor(p.getBackColor()) + ";");
            }

            if ((p.getBorder() != null) && (p.getCell() == null)) {
                body.append(p.getBorder().toString());
            }

            if (p.getCell() == null) {
                body.append(b, p.getBorder());
            }

            if (p.getBackgroundImageStyle() != null) {
                body.append(getImageEncoder().encode(p.getBackgroundImageStyle()));
            }

            body.closeStyle();

            if (p.getTooltipText() != null) {
                body.append(StringUtil.format("title='#'", p.getTooltipText()));
            }

            body.append(">");

            Insets is = p.getPadding();

            try {
                encode(p.elementCache, is.left, is.top);
            } catch (IOException e) {
                e.printStackTrace();
            }

            body.closeDiv();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param hyperlink
     *            DOCUMENT ME!
     * @param Text
     *            DOCUMENT ME!
     * @param flag
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String setHyperlink(String hyperlink, String Text, boolean flag) {
        if (hyperlink != null) {
            String target = null;
            String link = null;
            int indexof = hyperlink.indexOf("url:");
            target = hyperlink.substring(0, indexof - 1);
            target = target.replaceAll(":", "='");
            link = hyperlink.substring(indexof + 4, hyperlink.length());
            Text = "<a href='" + link + "' " + target + "'>" +
                (flag ? getEncodedBlankText(Text) : Text) + "</a>";
        }

        return Text;
    }

    /**
     * DOCUMENT ME!
     *
     * @param clip
     *            DOCUMENT ME!
     *
     * @throws IOException
     *             DOCUMENT ME!
     */
    public void encodeClip(ClipView clip) throws IOException {
        Rectangle rect = (Rectangle) clip.getBounds();

        body.append("<div class=\"jnc\" STYLE=\"left:0;top:0;");

        body.append("clip:rect(" + (rect.y) + "," + (rect.width + rect.x) + "," +
            (rect.y + rect.height) + "," + (rect.x) + ");");
        body.append("\"><p>&nbsp;</p>");
    }

    /**
     * DOCUMENT ME!
     *
     * @param e
     *            DOCUMENT ME!
     *
     * @throws IOException
     *             DOCUMENT ME!
     */
    public void encodeImage(ImageView e) throws IOException {
        body.append("<div ");

        if (e.getCell() != null) {
            body.append("class='td' ");
        } else {
            body.append("class='h' ");
        }

        body.append("style='");

        if ((e.getBorder() != null) && (e.getCell() == null)) {
            body.append(e.getBorder().toString());
        }

        if (e.getCell() == null) {
            body.append(e.getBounds(), e.getBorder());
        }

        body.append("'>");

        ImageStyle style = e.getImageStyle();

        if (style != null) {
            if (style.getImageObjectFormat() == ImageObjectFormat.FLASH) {
                encodeFlashImage(e);
            } else {
                encodeNormalImage(e);
            }
        }

        body.append("</div>");
    }

    private void encodeFlashImage(ImageView e) {
        ImageStyle style = e.getImageStyle();

        String src = null;

        if (style != null) {
            src = this.getImageEncoder().encode(style);
        }

        boolean doubleImage = true;
        String klass = doubleImage ? " flash_image" : "";

        if (this._html_report_server_path != null) {
            src = this._html_report_server_path + src;
        }

        body.append(getFlashString(e.getBounds().width + "", e.getBounds().height + "", src));

        if (e.getTooltipText() != null) {
            body.append(StringUtil.format("title='#' ", e.getTooltipText()));
        }

        if (e.getHyperlink() != null) {
            body.append(encodeHyperlink(e.getHyperlink(), "", false));
        }
    }

    private void encodeNormalImage(ImageView e) {
        ImageStyle style = e.getImageStyle();

        String src = null;

        if (style != null) {
            src = this.getImageEncoder().encode(style);
        }

        int ha = 0;
        int va = 0;

        float a = e.getImageStyle().getX().floatValue();

        if (a == 0.0) {
            ha = 0;
        } else if (a == 0.5) {
            ha = 1;
        } else if (a == 1.0) {
            ha = 2;
        }

        a = e.getImageStyle().getY().floatValue();

        if (a == 0.0) {
            va = 0;
        } else if (a == 0.5) {
            va = 1;
        } else if (a == 1.0) {
            va = 2;
        }

        String map = null;

        StringBuffer img = new StringBuffer();
        img.append("<img border='0' ");

        if (map != null) {
            img.append("usemap='#" + map + "' ");
        }

        String cls = ALIGN_CLASS2[(va * 3) + ha];

        img.append("class='" + cls + "' ");

        if (src != null) {
            img.append(StringUtil.format("src='#' ", src));
        }

        boolean styled = false;
        StringBuffer styleString = new StringBuffer();
        styleString.append("style='");

        if (style.isStretches()) {
            styleString.append(StringUtil.format("width:#;height:#;", e.getBounds().width + "",
                    e.getBounds().height + ";"));
            styled = true;
        }

        if (va == 1) {
            styleString.append(StringUtil.format("margin-top:expression((#-this.height)/2);",
                    "" + e.getBounds().height));
            styled = true;
        }

        if (ha == 1) {
            styleString.append(StringUtil.format("margin-left:expression((#-this.width)/2);",
                    "" + e.getBounds().width));
            styled = true;
        }

        if (styled) {
            styleString.append("' ");
            img.append(styleString.toString());
        }

        if (e.getTooltipText() != null) {
            img.append(StringUtil.format("title='#' ", e.getTooltipText()));
        }

        img.append(">");

        if (e.getHyperlink() == null) {
            body.append(img.toString());
        } else {
            body.append(encodeHyperlink(e.getHyperlink(), img.toString(), false));
        }
    }

    static String getFlashString(String width, String height, String location) {
        String s = "\n<embed src=\"" + location + "\" quality=\"high\" width=\"" + width +
            "\" height=\"" + height + "\" name=\"movie\" \n scale=\'ExactFit\'\n" +
            "align=\"\" type=\"application/x-shockwave-flash\" pluginspage=\"http://www.macromedia.com/go/getflashplayer\"> \n" +
            "\n\n";

        return s;
    }

    /**
     * DOCUMENT ME!
     */
    public void flush() {
        try {
            if (!_html_body_only) {
                out.write("<html>");

                out.write("<head>");
                out.write("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">");
            } else {
                out.write("<div style='display:none'>&nbsp;</div>");
            }

            writeCSS();

            if (!_html_body_only) {
                out.write("<title>" + ((this._html_title == null) ? title : _html_title) +
                    "</title>");
            }

            if (!_html_body_only) {
                out.write("</head><body");
                out.write(" bgcolor=\"" + _html_background + "\"");

                out.write(">");
            }

            body.writeTo(out);

            writeSettings(out);

            if (!this._html_body_only) {
                out.write("</body>");
                out.write("</html>  ");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeSettings(Writer out2) {
        String orientation = (pageFormat.getOrientation() == PageFormat.PORTRAIT) ? "1" : "2";
        String pageWidth = toMM(pageFormat.getWidth()) + "";
        String pageHeight = pageFormat.getPrintHeight() + "";
        String jobSessionId = this.jobSessionId;

        try {
            out.write(StringUtil.format(
                    "<div id='jsettings' orientation='#' pageWidth='#' pageHeight='#' jobSessionId='#' style='display:none' />",
                    orientation, pageWidth, pageHeight, jobSessionId));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param pixes
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int toMM(double pixes) {
        return 10 * (int) Math.round(pixes / DOTS_PER_MM);
    }

    private void writeCSS() {
        try {
            out.write("<style><!--\n*{\n-moz-box-sizing: border-box;\n}\n");

            out.write(".pcls{float: left;overflow:hidden;position:relative");

            out.write(";}\n");

            out.write("div.pcls *{white-space:nowrap;}\n");
            out.write("div.pcls div{position:absolute}\n");
            out.write("div.pcls table{position:absolute}\n");
            out.write("div.pcls img{position:absolute}\n");
            // out.write("div.pcls p{overflow:hidden;position:absolute;width:100%;}\n");
            out.write("div.td {overflow:hidden;width:100%;height:100%;}\n");
            out.write("div.pcls div.h{overflow:hidden;}");

            // out.write(".nt{display:table-cell;_position:absolute}\n");
            out.write(".txd{position:static;width:100%}\n");

            out.write(".mt{display:table}\n");
            out.write(
                "div.mt div.mt2{position:static;_position:absolute;_top: 50%;_left: 0;display:table-cell;vertical-align:middle;}\n");
            out.write(".mt2 p{_position: relative;_top:-50%}\n");

            out.write("img.tl{top:0;left:0}\n");
            out.write("img.tr{top:0;right:0}\n");
            out.write("img.bl{bottom:0;left:0}\n");
            out.write("img.br{bottom:0;right:0}\n");
            out.write("img.t{top:0;}\n");
            out.write("img.l{left:0}\n");
            out.write("img.b{bottom:0;}\n");
            out.write("img.r{right:0}\n");
            out.write(".fr_lt{z-index:4;}\n");
            out.write(".fr_t{z-index:3;}\n");
            out.write(".fr_l{z-index:2;}\n");
            out.write(".fr_c{cursor:hand;}\n");
            out.write(styleManager.toHtmlCSS());

            for (Iterator it = borderCache.values().iterator(); it.hasNext();) {
                String[] values = (String[]) it.next();
                out.write(".");
                out.write(values[0]);
                out.write(values[1]);
            }

            out.write("--></style>");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param options
     *            DOCUMENT ME!
     */
    public void setOptions(Map options) {
        this.options = options;
    }

    private String getCellClass(View v) {
        String cls = null;

        if (v instanceof TextView) {
            int id = ((TextView) v).getDisplayStyle().getId2();

            if (id > -1) {
                cls = DisplayStyleManager.TD_CLASS_PREFIX + id;
            }
        }else {
			Border b = ((AbstractView) v).getBorder();

			if (b != null) {
				String[] styles = (String[]) this.borderCache.get(b);

				if (styles == null) {
					styles = new String[] { "mb" + this.borderCache.size(),
							"{" + b.toString() + "}\n" };
					this.borderCache.put(b, styles);
				}

				return styles[0];
			}
		}

        return cls;
    }

    /**
     * DOCUMENT ME!
     *
     * @param v
     *            DOCUMENT ME!
     * @param clipped
     *            DOCUMENT ME!
     */
    public void encodeText(TextView v, boolean clipped) {
        if (v.getCell() != null) {
            encodeCellText(v, clipped);
        } else {
            encodeNormalText(v);
        }
    }

    private void encodeNormalText(TextView v) {
        if (v.getDisplayStyle().isMiddleMultiline()) {
            encodeMiddleMultilineText(v);
        } else {
            // div p
            Rectangle b = v.getBounds();
            body.append("<div ");

            body.append("class='" + DisplayStyleManager.DIV_CLASS_PREFIX +
                v.getDisplayStyle().getId());

            body.append(" nt' ");

            if ((v.getCell() == null) || (v.getBackgroundImageStyle() != null)) {
                body.openStyle();

                if (v.getCell() == null) {
                    body.append(b, v.getBorder());
                }

                if (v.getBackgroundImageStyle() != null) {
                    body.append(getImageEncoder().encode(v.getBackgroundImageStyle()));
                }

                body.closeStyle();
            }

            if (v.getTooltipText() != null) {
                body.append(StringUtil.format(" title='#' ", v.getTooltipText()));
            }

            body.append(">");

            String pClass = DisplayStyleManager.NORMAL_CLASS_PREFIX + v.getDisplayStyle().getId();

            body.append("<div class='" + pClass + " txd' ");

            if (!v.getDisplayStyle().isWordwrap() &&
                    (v.getDisplayStyle().getVerticalAlignment() == TextView.MIDDLE)) {
                body.append("style='line-height:" + b.height + "px' ");
            }

            TextLine[] lines = v.getWrappedLines();

            body.append(">");

            String text = null;

            if (lines.length > 0) {
                text = getWrappedText(lines);
                text = text.replaceAll("\n", "<br>");
            } else {
                text = v.getText();
            }

            body.append(encodeHyperlink(v.getHyperlink(), text, true));
            body.append("</div>");

            body.closeDiv();
        }
    }

    private void encodeMiddleMultilineText(TextView v) {
        // <div style='display:table;height: 300px;position: relative;width:
        // 400px;border: 1px solid #596480;color: inherit;background: #ffc' >
        // <div style='_position: absolute;_top: 50%;_left:
        // 0;display:table-cell;vertical-align:middle;'><p style='_position:
        // relative;_top: -50%'>Lorem ipsum dolor sit amet, consectetuer
        // adipiscing elit. Maecenas dignissim diam eu sem. Proin nunc ante,
        // accumsan sollicitudin, sodales at, semper sed, ipsum. Etiam orci.
        // Vestibulum magna lectus, venenatis nec, tempus ac, dictum vel,
        // lorem.</p></div>
        // </div>

        // div p
        Rectangle b = v.getBounds();
        body.append("<div ");

        body.append("class='" + DisplayStyleManager.DIV_CLASS_PREFIX + v.getDisplayStyle().getId());

        body.append(" mt' ");

        if ((v.getCell() == null) || (v.getBackgroundImageStyle() != null)) {
            body.openStyle();

            if (v.getCell() == null) {
                body.append(b, v.getBorder());
            }

            if (v.getBackgroundImageStyle() != null) {
                body.append(getImageEncoder().encode(v.getBackgroundImageStyle()));
            }

            body.closeStyle();
        }

        if (v.getTooltipText() != null) {
            body.append(StringUtil.format(" title='#' ", v.getTooltipText()));
        }

        body.append(">");
        body.append("<div class='mt2'>");

        String pClass = DisplayStyleManager.NORMAL_CLASS_PREFIX + v.getDisplayStyle().getId();

        body.append("<p class='" + pClass + " txd' ");

        if (!v.getDisplayStyle().isWordwrap() &&
                (v.getDisplayStyle().getVerticalAlignment() == TextView.MIDDLE)) {
            body.append("style='line-height:" + b.height + "px' ");
        }

        TextLine[] lines = v.getWrappedLines();

        body.append(">");

        String text = null;

        if (lines.length > 0) {
            text = getWrappedText(lines);
            text = text.replaceAll("\n", "<br>");
        } else {
            text = v.getText();
        }

        body.append(encodeHyperlink(v.getHyperlink(), text, true));
        body.append("</p></div>");

        body.closeDiv();
    }

    /**
     * DOCUMENT ME!
     *
     * @param v
     *            DOCUMENT ME!
     * @param clipped
     *            DOCUMENT ME!
     */
    public void encodeCellText(TextView v, boolean clipped) {
        body.append("<p ");

        body.append("class='" + DisplayStyleManager.NORMAL_CLASS_PREFIX +
            v.getDisplayStyle().getId());

        body.append("' ");

        if (v.getTooltipText() != null) {
            body.append(StringUtil.format(" title='#' ", v.getTooltipText()));
        }

        body.append(">");

        TextLine[] lines = v.getWrappedLines();
        boolean wrapped = lines.length > 1;

        String text = null;

        if (wrapped) {
            text = getWrappedText(lines);
            text = text.replaceAll("\n", "<br>");
        } else {
            text = v.getText();
        }

        body.append(encodeHyperlink(v.getHyperlink(), text, true));
        body.append("</p>");
    }

    private String getWrappedText(TextLine[] lines) {
        StringBuffer result = new StringBuffer();

        for (int i = 0; i < lines.length; i++) {
            if (i > 0) {
                result.append('\n');
            }

            result.append(lines[i].getText());
        }

        return result.toString();
    }

    String encodeHyperlink(String hyperlink, String text, boolean flag) {
    	
    	
        if ((hyperlink != null) && (text != null) && (text.trim().length() > 0)) {
            String target = null;
            String link = null;
            int indexof = hyperlink.indexOf("url:");
            target = hyperlink.substring(0, indexof - 1);
            target = target.replaceAll(":", "='");
            link = hyperlink.substring(indexof + 4, hyperlink.length());

            if (link.startsWith("javascript")) {
            	// 如果是javascript,不需要转义了
                link = link.substring(link.indexOf(":") + 1);

                text = "<a href='javascript:' onclick='" + link + "'>" +
                     text + "</a>";
            } else {
            	// 普通的超链接，需要转义一下
            	link = StringUtil.encodeURI(link);
                text = "<a href='" + link + "' " + target + "'>" +
                    (flag ? getEncodedBlankText(text) : text) + "</a>";
            }
        }else if( (text != null) && (text.trim().length() > 0)
        		)
        {
        	text = getEncodedBlankText(text);
        }

        return (text == null) ? "" : text;
    }

    private static String getColor(Color RGBColor) {
        if (RGBColor == null) {
            return "";
        }

        String backColor = Integer.toHexString(RGBColor.getRGB() &
                jatools.engine.export.html.HtmlExport.colorMask).toUpperCase();
        backColor = ("000000" + backColor).substring(backColor.length());

        return backColor;
    }

    private static String getEncodedBlankText(String text) {
        if ((text != null) && (text.indexOf("") > -1)) {
            text = text.replaceAll(" ", "&nbsp;");
        } else if ((text == null) || "".equals(text)) {
            text = "&nbsp;";
        }

        return text;
    }
}
