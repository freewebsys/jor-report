package jatools.tags;

import jatools.designer.App;
import jatools.util.StringUtil;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ContainerTag extends AbstractReportTag {
    static String JAVASCRIPT_DRAG_PLACED = "_javascript_drag_placed";

    //    private String scroll;
    //    private String background;
    //    private String height;
    //    private String width;
    private String style;
    private String canDrag;
    private int renderCount;

    /**
     * DOCUMENT ME!
     * @throws IOException
     * @throws Exception
     */
    public void writeOut() throws IOException {
        JspWriter out = reporttag.getBodyContent();

        String jid = this.getJavascriptId() + "_div";

        boolean _candrag = (canDrag == null) || canDrag.equals("true");

        if (renderCount > 0) {
            jid += ("__" + renderCount);
        }

        out.print(StringUtil.format("<div id=\'#\' style=\"", jid));

        //        out.print("<div  style=\"");

        //        if ((scroll != null) && scroll.equals("true")) {
        //            out.print("overflow:scroll;");
        //        }
        if (_candrag) {
            out.print("cursor:hand;");
        }

        //
        //        if (height != null) {
        //            out.print("height:" + height + ";");
        //        }
        //
        //        if (width != null) {
        //            out.print("width:" + width + ";");
        //        }
        //
        //        if (background != null) {
        //            out.print("background:" + background + ';');
        //        }
        if (style != null) {
            out.print(style);
        }

        out.print(";\" ");

        //  	out.print("onselectstart=\'selectStart(this)\' ondblclick=\'dblClick(this)\'  onmousemove=\'drag(this,event)\' onmousedown=\'dragStart(this,event)\' onmouseup=\'dragEnd()\' mode=\'drag\' ");
        out.print(">");

        //        out.print(StringUtil.format("<div id=\'#\' >", jid));
        reporttag.writeOut(out);
        out.print("</div>");
        //        out.print("</div>");
        this.renderCount++;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws JspException DOCUMENT ME!
     */
    public int doStartTag() throws JspException {
        if (!(this.getParent() instanceof ReportTag)) {
            throw new JspException(App.messages.getString("res.9") + id + App.messages.getString("res.10"));
        }

        boolean _candrag = (canDrag == null) || canDrag.equals("true");

        if (_candrag) {
            placeDragJavascriptsLibrary();
        }

        reporttag = (ReportTag) this.getParent();
        reporttag.setContainer(this);

        this.renderCount = 0;
        pageContext.setAttribute(id, this);

        return super.doStartTag();
    }

    void placeDragJavascriptsLibrary() {
        if (pageContext.findAttribute(JAVASCRIPT_DRAG_PLACED) == null) {
            JspWriter out = pageContext.getOut();

            pageContext.setAttribute(JAVASCRIPT_DRAG_PLACED, JAVASCRIPT_DRAG_PLACED,
                pageContext.PAGE_SCOPE);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param sytle DOCUMENT ME!
     */
    public void setStyle(String sytle) {
        this.style = sytle;
    }

    /**
     * DOCUMENT ME!
     *
     * @param canDrag DOCUMENT ME!
     */
    public void setCanDrag(String canDrag) {
        this.canDrag = canDrag;
    }
}
