package jatools.designer;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class Resource extends java.util.ListResourceBundle {
    static final Object[][] contents = new String[][] {
            { "BackColor", App.messages.getString("res.166") },
            { "Border", App.messages.getString("res.167") },
            { "Caption", App.messages.getString("res.168") },
            { "Font", App.messages.getString("res.24") },
            { "ForeColor", App.messages.getString("res.169") },
            { "Format", App.messages.getString("res.170") },
            { "Height", App.messages.getString("res.171") },
            { "X", "X" },
            { "LinePattern", App.messages.getString("res.172") },
            { "LineSize", App.messages.getString("res.173") },
            { "Name", App.messages.getString("res.108") },
            { "BreakPage", App.messages.getString("res.174") },
            { "VerticalAlignment", App.messages.getString("res.175") },
            { "HorizontalAlignment", App.messages.getString("res.176") },
            { "SizeMode", App.messages.getString("res.177") },
            { "Source", App.messages.getString("res.107") },
            { "Y", "Y" },
            { "Width", App.messages.getString("res.178") },
            { "LeftMargin", App.messages.getString("res.179") },
            { "TopMargin", App.messages.getString("res.180") },
            { "RightMargin", App.messages.getString("res.181") },
            { "BottomMargin", App.messages.getString("res.182") },
            { "HeaderVisible", App.messages.getString("res.183") },
            { "FooterVisible", App.messages.getString("res.184") }
        };

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[][] getContents() {
        return contents;
    }
}
