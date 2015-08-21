package jatools.core.view;

import jatools.engine.css.CSSValue;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class StyleAttributes extends Properties {
    final static String TRANSPARENT = "transparent";

    /**
    * DOCUMENT ME!
    */
    public static final Map colors = new HashMap();

    static {
        colors.put("black", Color.black);
        colors.put("red", Color.red);
        colors.put("yellow", Color.yellow);
        colors.put("green", Color.green);
        colors.put("blue", Color.blue);
        colors.put("orange", Color.orange);
        colors.put("gray", Color.gray);
    }

    /**
     * Creates a new StyleAttributes object.
     */
    public StyleAttributes(String styleText) {
        StyleParser.getDefaults().parse(styleText, this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     * @param defaults DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getColor(String prop, Color defaults) {
        Color result = defaults;
        String val = getProperty(prop);

        if (val != null) {
            if (val.equals(TRANSPARENT)) {
                result = null;
            } else {
                result = asColor(val, defaults);
            }
        }

        return result;
    }

    static Color asColor(final String value, Color defaults) {
        if (value.startsWith("#")) {
            return Color.decode("0x" + value.substring(1));
        }

        final Color color = (Color) colors.get(value);

        return (color == null) ? defaults : color;
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     * @param defaults DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getInt(String prop, int defaults) {
        int result = defaults;
        String val = getProperty(prop);

        if (val != null) {
            result = Integer.parseInt(val);
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     * @param defaults DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float getFloat(String prop, float defaults) {
        float result = defaults;
        String val = getProperty(prop);

        if (val != null) {
            result = Float.parseFloat(val);
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     * @param defaults DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean getBoolean(String prop, boolean defaults) {
        boolean result = defaults;
        String val = getProperty(prop);

        if (val != null) {
            result = "true".equalsIgnoreCase(val);
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasAttribute(String prop) {
        return getProperty(prop) != null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     * @param defaults DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getString(String prop, String defaults) {
        String result = defaults;
        String val = getProperty(prop);

        if (val != null) {
            result = val;
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CSSValue getCSSValue(String prop) {
        String val = getString(prop, null);

        if (val == null) {
            return null;
        } else {
            return new CSSValue(val);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public IndexedStyleAttributes getIndexedStyleAttributes(String prop) {
        String _styleText = getProperty(prop);

        if (_styleText != null) {
            return new IndexedStyleAttributes(_styleText);
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     * @param defaults DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Font getFont(String prop, Font defaults) {
        Font result = defaults;
        String val = getProperty(prop);

        if (val != null) {
            IndexedStyleAttributes att = new IndexedStyleAttributes(val);
            result = new Font(att.getString(0, null), att.getInt(1, 0), att.getInt(2, 8));
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toString(Font f) {
        if (f != null) {
            return f.getName() + " " + f.getStyle() + " " + f.getSize();
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param color DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toString(Color color) {
        if (color == null) {
            return TRANSPARENT;
        } else {
            final int mask = Integer.parseInt("FFFFFF", 16);
            String result = Integer.toHexString(color.getRGB() & mask).toUpperCase();
            result = ("000000" + result).substring(result.length());

            return "#" + result;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     * @param defaults DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Border getBorder(String prop, Border defaults) {
        Border result = defaults;
        String val = getProperty(prop);

        if (val != null) {
            result = new Border(val);
        }

        return result;
    }
}
