package jatools.core.view;

import java.awt.Insets;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public interface BorderBase {
    /**
     * DOCUMENT ME!
     */
    public static final String BORDER_STYLE_SOLID = "solid";

    /**
     * DOCUMENT ME!
     */
    public static final String BORDER_STYLE_DASHED = "dashed";

    /**
     * DOCUMENT ME!
     */
    public static final String BORDER_STYLE_DOTTED = "dotted";

    /**
     * DOCUMENT ME!
     */
    public static final BorderStyle DEFAULT_STYLE = new BorderStyle();

    /**
     * DOCUMENT ME!
     */
    public static final String DEFAULT_STYLE_TEXT = "border:1px solid #000000";

   

    /**
     * DOCUMENT ME!
     */
    public static final int LEFT = 1;

    /**
     * DOCUMENT ME!
     */
    public static final int RIGHT = 2;

    /**
     * DOCUMENT ME!
     */
    public static final int TOP = 4;

    /**
     * DOCUMENT ME!
     */
    public static final int BOTTOM = 8;

    /**
     * DOCUMENT ME!
     */
    public static final int ALL = LEFT | RIGHT | TOP | BOTTOM;

    /**
     * DOCUMENT ME!
     */
    public final Insets NULL_INSETS = new Insets(0, 0, 0, 0);
}
