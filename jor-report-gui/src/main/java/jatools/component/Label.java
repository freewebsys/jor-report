package jatools.component;



import jatools.accessor.PropertyDescriptor;
import jatools.core.view.DisplayStyle;
import jatools.core.view.TextView;
import jatools.designer.App;
import jatools.engine.script.Script;
import jatools.formatter.Format2;
import jatools.util.Util;

import java.awt.Font;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class Label extends Component {
    public static final int TOP = TextView.TOP;
    public static final int BOTTOM = TextView.BOTTOM;
    public static final int CENTER = TextView.CENTER;
    public static final int LEFT = TextView.LEFT;
    public static final int RIGHT = TextView.RIGHT;
    public static Font DEFAULT_FONT = null;

    static {
        try {
            DEFAULT_FONT = new Font(App.messages.getString("res.21"), 0, 12);
        } catch (Exception e) {
            Util.debug("ZLabel", e);
        } catch (Error e) {
            e.printStackTrace();
        }
    }

    private int horizontalAlignment;
    private int verticalAlignment = CENTER;
    private Font font = DEFAULT_FONT;
    private String text;
    private boolean wordwrap;

    /**
     * Creates a new Label object.
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public Label(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    /**
     * Creates a new Label object.
     */
    public Label() {
    }

    /**
     * Creates a new Label object.
     *
     * @param text DOCUMENT ME!
     */
    public Label(String text) {
        this.text = text;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isWordwrap() {
        return wordwrap;
    }

    /**
     * DOCUMENT ME!
     *
     * @param wordwrap DOCUMENT ME!
     */
    public void setWordwrap(boolean wordwrap) {
        if (this.wordwrap != wordwrap) {
            boolean _worldwrap = this.wordwrap;

            this.wordwrap = wordwrap;

            if (hasPropertyChangeListener()) {
                this.firePropertyChange(ComponentConstants.PROPERTY_WORDWRAP,
                    new Boolean(_worldwrap), new Boolean(wordwrap));
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param font DOCUMENT ME!
     */
    public void setFont(Font font) {
        if (this.font != font) {
            Font _font = this.font;
            this.font = font;

            if (hasPropertyChangeListener()) {
                this.firePropertyChange(ComponentConstants.PROPERTY_FONT, _font, font);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Font getFont() {
        return font;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] {
            ComponentConstants._NAME, ComponentConstants._TEXT, ComponentConstants._BACK_COLOR,
            ComponentConstants._FORE_COLOR, ComponentConstants._BORDER, ComponentConstants._FONT,
            ComponentConstants._HORIZONTAL_ALIGNMENT, ComponentConstants._VERTICAL_ALIGNMENT,
            ComponentConstants._WORDWRAP, ComponentConstants._BACKGROUND_IMAGE,
            ComponentConstants._HYPERLINK, ComponentConstants._TOOLTIP_TEXT,
            ComponentConstants._PRINT_STYLE,  ComponentConstants._X,
            ComponentConstants._Y, ComponentConstants._WIDTH, ComponentConstants._HEIGHT,
            ComponentConstants._CELL, ComponentConstants._INIT_PRINT, ComponentConstants._AFTERPRINT,
            ComponentConstants._BEFOREPRINT2
        };
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getVerticalAlignment() {
        return verticalAlignment;
    }

    /**
     * DOCUMENT ME!
     *
     * @param verticalAlignment DOCUMENT ME!
     */
    public void setVerticalAlignment(int verticalAlignment) {
        if (this.verticalAlignment != verticalAlignment) {
            int _verticalAlignment = this.verticalAlignment;
            this.verticalAlignment = verticalAlignment;

            if (hasPropertyChangeListener()) {
                this.firePropertyChange(ComponentConstants.PROPERTY_VERTICAL_ALIGNMENT,
                    new Integer(_verticalAlignment), new Integer(verticalAlignment));
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }

    /**
     * DOCUMENT ME!
     *
     * @param horizontalAlignment DOCUMENT ME!
     */
    public void setHorizontalAlignment(int horizontalAlignment) {
        if (this.horizontalAlignment != horizontalAlignment) {
            int _horizontalAlignment = this.horizontalAlignment;
            this.horizontalAlignment = horizontalAlignment;

            if (hasPropertyChangeListener()) {
                this.firePropertyChange(ComponentConstants.PROPERTY_HORIZONTAL_ALIGNMENT,
                    new Integer(_horizontalAlignment), new Integer(horizontalAlignment));
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getText() {
        return text;
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * DOCUMENT ME!
     *
     * @param dataProvider DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getText(Script dataProvider) {
        return getText();
    }

    TextView asView(String _text, DisplayStyle css) {
        TextView e = new TextView(css);

        e.setBounds(getBounds());

        e.setText(_text);

        return e;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Format2 getFormat() {
        return null;
    }
}
