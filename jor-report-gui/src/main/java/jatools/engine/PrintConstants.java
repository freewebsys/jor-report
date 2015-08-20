package jatools.engine;

import jatools.designer.App;

import java.awt.Font;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public interface PrintConstants {
    static final Font BIG_FONT = new Font(App.messages.getString("res.21"), Font.BOLD, 36);
    static final String CURRENT_PRINTER = "_current_printer_";
    static final String VARIABLE_MANAGER = "$$vm";
    static final String SESSION2 = "$$session";
    static final String DEFAULT_USER = "guest";
    public static final String SCRIPT = "$$script";
    public static final String DISPLAY_STYLE_MANAGER = "$$stylemanager";
    public static final String IMAGE_LOADER = "$$imageloader";
    public static final int PAGE_COUNT_FOUND = 1;
    static final String COMPANY = "$company";
    static final String USER = "$user";
    static final String TITLE = "$title";
    static final String NOW = "$now";
    static final String TODAY = "$today";
    static final String TOTAL_PAGE_NUMBER = "$totalPageNumber";
    static final String PAGE_INDEX = "$pageIndex";
    static final String MODEL = "$$model";
    static final String WORKING_DIR = "$workingDir";
    static final String HTTP_REQUEST = "$httpRequest";
    static final String HTTP_SESSION = "$httpSession";
    static final String AS2 = "$as";
    static final String PRINTER = "$printer";
    static final String STYLE_CLASSE = "$styleClass";
}
