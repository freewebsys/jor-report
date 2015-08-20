package jatools.tags;

import jatools.accessor.ProtectPublic;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;



public class AbstractReportTag extends BodyTagSupport implements ProtectPublic {
	ReportTag reporttag;
    static String JAVASCRIPT_LIB_PLACED = "_javascript_lib_placed";
    private static String contextPath;
    String id;
    
    

    void placeJavascriptsLibrary()  {
        if (pageContext.findAttribute(JAVASCRIPT_LIB_PLACED) == null ) {
            try {
				JspWriter out = pageContext.getOut();

				
				out.write("<script type=\"text/javascript\" src=\"");
				out.write(getContextPath());
				out.write("/js/tools/toolsbar.js\"></script>"); //

				pageContext.setAttribute(JAVASCRIPT_LIB_PLACED, JAVASCRIPT_LIB_PLACED,
				    pageContext.PAGE_SCOPE);
			} catch (IOException e) {
			
				e.printStackTrace();
			}
        }
    }
    
   

    String getContextPath() {
        if (contextPath == null) {
            contextPath = ((HttpServletRequest) pageContext.getRequest()).getContextPath();
        }

        return contextPath;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getId() {
        return id;
    }

    /**
     * DOCUMENT ME!
     *
     * @param id DOCUMENT ME!
     */
    public void setId(String id) {
        this.id = id;
    }

    
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getJavascriptId() {
        return  id;
    }
}
