package jatools.core.view;

import jatools.accessor.PropertyAccessor;
import jatools.accessor.PropertyDescriptor;
import jatools.component.ComponentConstants;
import jatools.formatter.Format2;
import jatools.formatter.FormatUtil;

import java.awt.Color;
import java.awt.Font;
import java.io.Serializable;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class DisplayStyle implements PropertyAccessor, Cloneable, Serializable {
    static final long serialVersionUID = 200512203L;
    private final static String STYLE_NAME = "name";
    private final static String STYLE_BACKCOLOR = "backcolor";
    private final static String STYLE_FORECOLOR = "forecolor";
    private final static String STYLE_FONT = "font";
    private final static String STYLE_BORDER = "border";
    private final static String STYLE_HALIGN = "h-align";
    private final static String STYLE_VALIGN = "v-align";
    private final static String STYLE_FORMAT = "format";
    private final static String STYLE_WORDWRAP = "wordwrap";
    public final static int BACKCOLOR = 1;
    public final static int WORDWRAP = 2;
    int enables = WORDWRAP | BACKCOLOR;
    private int id;
    Color backColor;
    Color foreColor;
    Font font;
    Border border;
    int horizontalAlignment = -1;
    int verticalAlignment = -1;
    transient Format2 format;
    boolean wordwrap;
    String name;
    private boolean parsed = true;
    private String text;
    private boolean forCell;

    /**
     * Creates a new DisplayStyle object.
     *
     * @param backColor DOCUMENT ME!
     * @param foreColor DOCUMENT ME!
     * @param font DOCUMENT ME!
     * @param border DOCUMENT ME!
     * @param horizontalAlignment DOCUMENT ME!
     * @param verticalAlignment DOCUMENT ME!
     * @param format DOCUMENT ME!
     * @param wordwrap DOCUMENT ME!
     */
    public DisplayStyle(Color backColor, Color foreColor, Font font, Border border,
        int horizontalAlignment, int verticalAlignment, Format2 format, boolean wordwrap) {
        this.backColor = backColor;
        this.foreColor = foreColor;
        this.font = font;
        this.border = border;
        this.horizontalAlignment = horizontalAlignment;
        this.verticalAlignment = verticalAlignment;
        this.format = format;
        this.wordwrap = wordwrap;
    }

    /**
     * Creates a new DisplayStyle object.
     *
     * @param name DOCUMENT ME!
     */
    public DisplayStyle(String name) {
        this.name = name;
    }

    /**
     * Creates a new DisplayStyle object.
     */
    public DisplayStyle() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isWordwrapEnabled() {
        return (this.enables & WORDWRAP) != 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        if (!isParsed()) {
            parse();
        }

        int c = 0;

        if (backColor != null) {
            c ^= backColor.hashCode();
        }

        if (foreColor != null) {
            c ^= foreColor.hashCode();
        }

        if (font != null) {
            c ^= font.hashCode();
        }

        if (border != null) {
            c += border.hashCode();
        }

        c = horizontalAlignment + verticalAlignment;

        if (format != null) {
            c += format.hashCode();
        }

        if (wordwrap) {
            c++;
        }

        return c;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isBlank() {
        if (!isParsed()) {
            parse();
        }

        return (backColor == null) && (border == null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (!isParsed()) {
            parse();
        }

        if (obj == this) {
            return true;
        }

        if (obj instanceof DisplayStyle) {
            DisplayStyle css = (DisplayStyle) obj;
            Color _backColor = css.getBackColor();

            if (!equals(_backColor, backColor)) {
                return false;
            }

            Color _foreColor = css.getForeColor();

            if (!equals(_foreColor, foreColor)) {
                return false;
            }

            Font _font = css.getFont();

            if (!equals(_font, font)) {
                return false;
            }

            Border _border = css.getBorder();

            if (!equals(_border, border)) {
                return false;
            }

            int _horizontalAlignment = css.getHorizontalAlignment();

            if (_horizontalAlignment != horizontalAlignment) {
                return false;
            }

            int _verticalAlignment = css.getVerticalAlignment();

            if (_verticalAlignment != verticalAlignment) {
                return false;
            }

            boolean _wordwrap = css.isWordwrap();

            if (_wordwrap != wordwrap) {
                return false;
            }

            Format2 _format = css.getFormat();

            if (!equals(_format, format)) {
                return false;
            }

            return true;
        } else {
            return false;
        }
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
    public Color getBackColor() {
        if (!isParsed()) {
            parse();
        }

        return backColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Border getBorder() {
        if (!isParsed()) {
            parse();
        }

        return border;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Font getFont() {
        if (!isParsed()) {
            parse();
        }

        return font;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getForeColor() {
        if (!isParsed()) {
            parse();
        }

        return foreColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHorizontalAlignment() {
        if (!isParsed()) {
            parse();
        }

        return horizontalAlignment;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getVerticalAlignment() {
        if (!isParsed()) {
            parse();
        }

        return verticalAlignment;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isMiddleMultiline() {
        return (isWordwrap() && (getVerticalAlignment() == TextView.MIDDLE));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getId() {
        return id;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getId2() {
        this.forCell = true;

        return ((getBorder() != null) || (this.verticalAlignment > -1)) ? id : (-1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param id DOCUMENT ME!
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Format2 getFormat() {
        if (!isParsed()) {
            parse();
        }

        return format;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isWordwrap() {
        if (!isParsed()) {
            parse();
        }

        return wordwrap;
    }

    boolean equals(Object x0, Object x1) {
        return (x0 == null) ? (x1 == null) : x0.equals(x1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param aStyle DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isDifferent(DisplayStyle aStyle) {
        if (!isParsed()) {
            parse();
        }

        if (!aStyle.isParsed()) {
            aStyle.parse();
        }

        if ((this.font != null) && !equals(this.font, aStyle.font)) {
            return true;
        }

        if (this.isBackColorEnabled() && !equals(this.backColor, aStyle.backColor)) {
            return true;
        }

        if ((this.foreColor != null) && !equals(this.foreColor, aStyle.foreColor)) {
            return true;
        }

        if ((this.format != null) && !equals(this.format, aStyle.format)) {
            return true;
        }

        if ((this.border != null) && !equals(this.border, aStyle.border)) {
            return true;
        }

        if ((this.horizontalAlignment != -1) &&
                (this.horizontalAlignment != aStyle.horizontalAlignment)) {
            return true;
        }

        if ((this.verticalAlignment != -1) && (this.verticalAlignment != aStyle.verticalAlignment)) {
            return true;
        }

        if (this.isWordwrapEnabled() && (this.wordwrap != aStyle.wordwrap)) {
            return true;
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param aStyle DOCUMENT ME!
     * @param depend DOCUMENT ME!
     */
    public void apply(DisplayStyle aStyle, DisplayStyle depend) {
        if (!isParsed()) {
            parse();
        }

        if (depend.font != null) {
            this.font = aStyle.font;
        }

        if (depend.isBackColorEnabled()) {
            this.backColor = aStyle.backColor;
        }

        if (depend.foreColor != null) {
            this.foreColor = aStyle.foreColor;
        }

        if (depend.format != null) {
            this.format = aStyle.format;
        }

        if (depend.border != null) {
            this.border = aStyle.border;
        }

        if (depend.horizontalAlignment != -1) {
            this.horizontalAlignment = aStyle.horizontalAlignment;
        }

        if (depend.verticalAlignment != -1) {
            this.verticalAlignment = aStyle.verticalAlignment;
        }

        if (depend.isWordwrapEnabled()) {
            this.wordwrap = aStyle.wordwrap;
        }

        this.text = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isBackColorEnabled() {
        return (this.enables & BACKCOLOR) != 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        if (!isParsed()) {
            parse();
        }

        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void setName(String name) {
        if (!isParsed()) {
            parse();
        }

        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return this.getName();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getText() {
        if (text != null) {
            return text;
        } else {
            this.text = createText();

            return text;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String createText() {
        StringBuffer result = new StringBuffer();

        if (this.name != null) {
            result.append(STYLE_NAME);
            result.append(":");
            result.append(this.name);
            result.append(";");
        }

        if (this.font != null) {
            result.append(STYLE_FONT);
            result.append(":");
            result.append(StyleAttributes.toString(this.font));
            result.append(";");
        }

        if (isBackColorEnabled()) {
            result.append(STYLE_BACKCOLOR);
            result.append(":");
            result.append(StyleAttributes.toString(this.backColor));
            result.append(";");
        }

        if (this.foreColor != null) {
            result.append(STYLE_FORECOLOR);
            result.append(":");
            result.append(StyleAttributes.toString(this.foreColor));
            result.append(";");
        }

        if (this.border != null) {
            result.append(border.toString());
            result.append(";");
        }

        if (horizontalAlignment != -1) {
            result.append(STYLE_HALIGN);
            result.append(":");
            result.append(horizontalAlignment);
            result.append(";");
        }

        if (verticalAlignment != -1) {
            result.append(STYLE_VALIGN);
            result.append(":");
            result.append(verticalAlignment);
            result.append(";");
        }

        if (this.format != null) {
            result.append(STYLE_FORMAT);
            result.append(":");
            result.append(this.format.toPattern());
            result.append(";");
        }

        if (this.isWordwrapEnabled()) {
            result.append(STYLE_WORDWRAP);
            result.append(":");
            result.append(this.wordwrap ? "true" : "false");
            result.append(";");
        }

        return result.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param csstext DOCUMENT ME!
     */
    public void setText(String csstext) {
        this.text = csstext;
        this.parsed = false;
    }

    private void parse() {
        if (!this.parsed) {
            StyleAttributes styles = new StyleAttributes(this.text);

            this.name = styles.getString(STYLE_NAME, null);
            this.backColor = styles.getColor(STYLE_BACKCOLOR, null);
            enable2(BACKCOLOR, styles.hasAttribute(STYLE_BACKCOLOR));
            this.foreColor = styles.getColor(STYLE_FORECOLOR, null);
            this.font = styles.getFont(STYLE_FONT, null);
            this.border = Border.create(this.text);

            String pattern = styles.getString(STYLE_FORMAT, null);

            if (pattern != null) {
                this.format = FormatUtil.getInstance(pattern);
            }

            this.horizontalAlignment = styles.getInt(STYLE_HALIGN, -1);
            this.verticalAlignment = styles.getInt(STYLE_VALIGN, -1);

            this.wordwrap = styles.getBoolean(STYLE_WORDWRAP, false);
            enable2(WORDWRAP, styles.hasAttribute(STYLE_WORDWRAP));

            this.parsed = true;
        }
    }

    private boolean isParsed() {
        return this.parsed;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] { ComponentConstants._TEXT };
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isForCell() {
        return this.forCell;
    }

    /**
     * DOCUMENT ME!
     *
     * @param field DOCUMENT ME!
     * @param b DOCUMENT ME!
     */
    public void enable2(int field, boolean b) {
        if (b) {
            this.enables |= field;
        } else {
            this.enables &= (field ^ 0xFFFF);
        }
    }
}
