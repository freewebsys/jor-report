package jatools.component;

import jatools.core.view.IndexedStyleAttributes;
import jatools.core.view.StyleAttributes;
import jatools.engine.css.CSSValue;
import jatools.engine.imgloader.ImageSource;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class BackgroundImageStyle extends ImageStyle implements ImageSource {
    public static String REPEAT_X = "repeat-x";
    public static String REPEAT_Y = "repeat-y";
    public static String REPEAT = "repeat";
    public static String REPEAT_NONE = "none";
    public static String BACKGROUND_IMAGE = "background-image";
    public static String BACKGROUND_REPEAT = "background-repeat";
    public static String BACKGROUND_POSITION = "background-position";
    private boolean repeatx;
    private boolean repeaty;

    /**
     * Creates a new BackgroundImageStyle object.
     *
     * @param styleText DOCUMENT ME!
     */
    public BackgroundImageStyle(String styleText) {
        parse(styleText);
    }

    private void parse(String styleText) {
        x = CSSValue.DEFAULT;
        y = CSSValue.DEFAULT;

        StyleAttributes parser = new StyleAttributes(styleText);

        imageSrc = parser.getString(BACKGROUND_IMAGE, null);

        IndexedStyleAttributes positionParser = parser.getIndexedStyleAttributes(BACKGROUND_POSITION);

        x = new CSSValue(positionParser.getString(0, null));
        y = new CSSValue(positionParser.getString(1, null));

        String repeat = parser.getString(BACKGROUND_REPEAT, "none");

        if (repeat.equals(REPEAT_X)) {
            repeatx = true;
        } else if (repeat.equals(REPEAT_Y)) {
            repeaty = true;
        } else if (repeat.equals(REPEAT)) {
            repeatx = true;
            repeaty = true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isRepeatx() {
        return repeatx;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isRepeaty() {
        return repeaty;
    }
}
