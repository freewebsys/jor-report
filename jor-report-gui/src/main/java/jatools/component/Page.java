package jatools.component;

import jatools.PageFormat;
import jatools.ReportDocument;
import jatools.accessor.PropertyDescriptor;
import jatools.component.layout.LayoutManager;
import jatools.designer.App;
import jatools.designer.PageFormatParser;

import java.awt.Dimension;
import java.awt.Insets;
import java.util.Iterator;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.10 $
  */
public class Page extends Component {
    /**
     * DOCUMENT ME!
     */
    final static PageLayout PAGE_LAYOUT = new PageLayout();
    public final static int HEADER = 100;
    public final static int BODY = 102;
    public final static int FOOTER = 103;
    public static final Dimension A4 = new Dimension(793, 1122);

    /**
     * Creates a new PagePanel object.
     *
     * @param filler
     *            DOCUMENT ME!
     * @param i
     *            DOCUMENT ME!
     */
    ReportDocument document1;
    private PageFormat pageFormat = new PageFormat(A4);

    /**
     * Creates a new Page object.
     */
    public Page() {
        this.setLayout(PAGE_LAYOUT);
    }

    /**
     * DOCUMENT ME!
     *
     * @param doc DOCUMENT ME!
     */
    public void setDocument(ReportDocument doc) {
        this.document1 = doc;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Insets getPadding() {
        if (this.pageFormat != null) {
            return this.pageFormat;
        } else {
            return NULL_INSETS;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Page copy() {
        Page page2 = new Page();

        //   page.setName("panel");
        PagePanel header = this.getHeader();

        if (header != null) {
            PagePanel h = new PagePanel();
            h.setHeight(header.getHeight());

            page2.setHeader(h);
        }

        PagePanel footer = this.getHeader();

        if (footer != null) {
            PagePanel f = new PagePanel();
            f.setHeight(footer.getHeight());

            page2.setHeader(f);
        }

        PagePanel body = new PagePanel();

        // body.setName("body");
        page2.setBody(body);

        return page2;
    }

    /**
     * DOCUMENT ME!
     *
     * @param padding DOCUMENT ME!
     */
    public void setPadding(Insets padding) {
        throw new UnsupportedOperationException(App.messages.getString("res.632"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param height DOCUMENT ME!
     */
    public void setHeight(int height) {
        throw new UnsupportedOperationException(App.messages.getString("res.633"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     */
    public void setWidth(int width) {
        throw new UnsupportedOperationException(App.messages.getString("res.634"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     */
    public void setX(int x) {
        throw new UnsupportedOperationException(App.messages.getString("res.635"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param y DOCUMENT ME!
     */
    public void setY(int y) {
        
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public Panel getBody() {
        Iterator it = iterator();

        while (it.hasNext()) {
            Panel p = (Panel) it.next();

            if (p.getType() == BODY) {
                return p;
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param body
     *            DOCUMENT ME!
     */
    public void setBody(PagePanel body) {
        Panel b = getBody();

        if (b != null) {
            remove(b);
        }

        body.setType(BODY);
        add(body);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PagePanel getFooter() {
        Iterator it = iterator();

        while (it.hasNext()) {
            Panel p = (Panel) it.next();

            if (p.getType() == FOOTER) {
                return (PagePanel) p;
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param footer
     *            DOCUMENT ME!
     */
    public void setFooter(PagePanel footer) {
        Panel f = getFooter();

        if (f != null) {
            remove(f);
        }

        footer.setType(FOOTER);
        add(footer);
    }

    /**
     * DOCUMENT ME!
     *
     * @param format DOCUMENT ME!
     */
    public void setPageFormat2(String format) {
        this.setPageFormat(new PageFormatParser(format).parse());
        this.validate();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PagePanel getHeader() {
        Iterator it = iterator();

        while (it.hasNext()) {
            Panel p = (Panel) it.next();

            if (p.getType() == HEADER) {
                return (PagePanel) p;
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param header
     *            DOCUMENT ME!
     */
    public void setHeader(PagePanel header) {
        Panel h = getHeader();

        if (h != null) {
            remove(h);
        }

        header.setType(HEADER);
        insert(header, 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHeight() {
        return this.pageFormat.getHeight();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWidth() {
        return this.pageFormat.getWidth();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] {
            ComponentConstants._NAME, ComponentConstants._BACK_COLOR, ComponentConstants._FORE_COLOR,
            ComponentConstants._BACKGROUND_IMAGE, ComponentConstants._PRINT_STYLE,
            ComponentConstants._X, ComponentConstants._Y, ComponentConstants._CHILDREN,
            ComponentConstants._PAGE_FORMAT, ComponentConstants._INIT_PRINT,
            ComponentConstants._AFTERPRINT, ComponentConstants._BEFOREPRINT2
        };
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isContainer() {
        return true;
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public boolean isValid() {
        // TODO Auto-generated method stub
        return super.isValid();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PageFormat getPageFormat() {
        return pageFormat;
    }

    /**
     * DOCUMENT ME!
     */
    public void validate() {
        if (!this.valid) {
            this.doLayout();

            //            for (int i = 0; i < this.getChildCount(); i++) {
            //                this.getChild(i).validate();
            //            }
            this.doLayout2();
        }

        this.valid = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param pageFormat DOCUMENT ME!
     */
    public void setPageFormat(PageFormat pageFormat) {
        this.pageFormat = pageFormat;
        this.invalid();
    }
}


class PageLayout implements LayoutManager {
    /**
       * DOCUMENT ME!
       *
       * @param parent DOCUMENT ME!
       */
    public void layout(Component c) {
        Page p = (Page) c;
        Insets is = p.getPadding();

        Component header = p.getHeader();
        Component body = p.getBody();
        Component footer = p.getFooter();
        int y = 0;
        int bodyWidth = p.getWidth() - is.left - is.right;
        int bodyHeight = p.getHeight() - is.top - is.bottom;

        if (header != null) {
            header.setBounds(0, 0, bodyWidth, header.getHeight());

            header.validate();

            if (header.getMinHeight() > header.getHeight()) {
                header.setHeight(header.getMinHeight());
            }

            y += header.getHeight();
        }

        if (body != null) {
            if (footer != null) {
                body.setHeight(bodyHeight - y - footer.getHeight());
            } else {
                body.setHeight(bodyHeight - y);
            }

            body.setWidth(bodyWidth);
            //   y += body.getHeight();
            body.validate();

            int bh = body.getHeight();

            if (body.getMinHeight() > body.getHeight()) {
                bh = body.getMinHeight();
            }

            body.setBounds(0, y, bodyWidth, bh, false);
            y += body.getHeight();
        }

        if (footer != null) {
            footer.setBounds(0, y, bodyWidth, bodyHeight - y);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param comp DOCUMENT ME!
     */
    public void layout2(Component comp) {
    }
}
