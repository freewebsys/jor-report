package jatools.engine.css;

import jatools.designer.App;
import jatools.engine.script.Script;

import org.apache.commons.lang.StringUtils;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class CSSValue {
    /**
     * DOCUMENT ME!
     */
    public final static CSSValue ZERO = new CSSValue("0");

    /**
     * DOCUMENT ME!
     */
    public final static CSSValue TRUE = new CSSValue("true");

    /**
     * DOCUMENT ME!
     */
    public final static CSSValue AUTO = new CSSValue("auto");

    /**
     * DOCUMENT ME!
     */
    public static final CSSValue DEFAULT = new CSSValue();
    final int NUMERIC = 0;
    final int PERCENT = 1;
    final int STRING = 2;
    private int type;
    private float floatValue;
    private String csstext;
    private String scripts;

    /**
     * Creates a new Length object.
     *
     * @param csstext DOCUMENT ME!
     */
    public CSSValue(String csstext) {
        if ((csstext == null) || ((csstext = csstext.trim()).length() == 0)) {
            throw new IllegalArgumentException(App.messages.getString("res.64"));
        }

        if (csstext.startsWith("${") && csstext.endsWith("}")) {
            this.scripts = csstext.substring(2, csstext.length() - 1);
            this.csstext = csstext;
        } else {
            setCSSText(csstext);
        }
    }

    /**
     * Creates a new CSSValue object.
     */
    public CSSValue() {
        // TODO Auto-generated constructor stub
    }

    private void setCSSText(String csstext) {
        this.csstext = csstext;

        if (csstext.endsWith("%")) {
            type = PERCENT;
            csstext = csstext.substring(0, csstext.length() - 1);

            floatValue = Float.parseFloat(csstext) / 100.0f;
        } else if ((csstext.startsWith("-") && StringUtils.isNumeric(csstext.substring(1))) ||
                StringUtils.isNumeric(csstext)) {
            type = NUMERIC;
            floatValue = Float.parseFloat(csstext);
        } else {
            type = STRING;
        }
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public String toString() {
        if (isScript()) {
            return "${" + this.scripts + "}";
        } else {
            return stringValue();
        }
    }

    private String stringValue() {
        return this.csstext;
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public boolean isPercent() {
        return type == PERCENT;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNumeric() {
        return type == NUMERIC;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isString() {
        return type == STRING;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float floatValue() {
        return this.floatValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public float percentValue() {
        return this.floatValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int intValue() {
        return (int) this.floatValue;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isAuto() {
        return "auto".equals(csstext);
    }

    /**
     * DOCUMENT ME!
     *
     * @param script DOCUMENT ME!
     */
    public void reset(Script script) {
        if (isScript()) {
            Object val = script.eval(this.scripts);

            if (val != null) {
                setCSSText(val.toString());
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isScript() {
        return this.scripts != null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isTrue() {
        return "true".equalsIgnoreCase(csstext);
    }

    /**
     * DOCUMENT ME!
     *
     * @param string2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean is(String string2) {
        return this.csstext.equals(string2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param script DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CSSValue clone(Script script) {
        if (this.isScript()) {
            Object val = script.eval(this.scripts);

            if (val != null) {
                return new CSSValue(val.toString());
            } else {
                return null;
            }
        } else {
            return this;
        }
    }
}
