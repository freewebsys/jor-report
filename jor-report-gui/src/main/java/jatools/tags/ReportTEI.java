package jatools.tags;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.VariableInfo;


public class ReportTEI extends AbstractTEI {
    /**
     * DOCUMENT ME!
     *
     * @param arg0 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public VariableInfo[] getVariableInfo(TagData arg0) {
        VariableInfo[] vi = new VariableInfo[] {
                this.createVariableInfo(arg0.getAttributeString( "id"), "jatools.tags.ReportTag")
            };

        return vi;
    }
}
