package jatools.core.view;

import java.awt.Color;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class IndexedStyleAttributes {
    private String styleText;
    private String[] styles;

    /**
     * Creates a new IndexedStyleAttributes object.
     *
     * @param styleText DOCUMENT ME!
     */
    public IndexedStyleAttributes(String styleText) {
        this.styleText = styleText;
        parse();
    }

    /**
     * DOCUMENT ME!
     */
    public void parse() {
        if (this.styleText != null) {
            styles = this.styleText.split(" ");
        } else {
            styles = new String[0];
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @param defaults DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getColor(int index, Color defaults) {
        Color result = defaults;

        if ((index >= 0) && (index < this.styles.length)) {
            result = StyleAttributes.asColor(this.styles[index], defaults);
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @param defaults DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getInt(int index, int defaults) {
        int result = defaults;

        if ((index >= 0) && (index < this.styles.length)) {
            result = Integer.parseInt(this.styles[index]);
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @param defaults DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getPx(int index, int defaults) {
        float result = defaults;

        if ((index >= 0) && (index < this.styles.length)) {
            String style = this.styles[index].toLowerCase();

            if (style.endsWith("px") || style.endsWith("pt") ) {
                style = style.substring(0, style.lastIndexOf("p"));
            }

            result = Float.parseFloat(style);
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @param defaults DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getFloat(int index, float defaults) {
        float result = defaults;

        if ((index >= 0) && (index < this.styles.length)) {
            result = Float.parseFloat(this.styles[index]);
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @param defaults DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getBoolean(int index, boolean defaults) {
        boolean result = defaults;

        if ((index >= 0) && (index < this.styles.length)) {
            result = Boolean.getBoolean(this.styles[index]);
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @param defaults DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getString(int index, String defaults) {
        String result = defaults;

        if ((index >= 0) && (index < this.styles.length)) {
            result = this.styles[index];
        }

        return result;
    }
}
