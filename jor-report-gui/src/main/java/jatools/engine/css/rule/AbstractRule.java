package jatools.engine.css.rule;

import jatools.engine.css.CSSValue;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public abstract class AbstractRule {
    static private final int UNKOWN = 0;
    static protected final int SCRIPTED = 1;
    static protected final int NO_SCRIPTED = 2;
    int script;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isScript() {
        if (script == 0) {
            script = getScriptType();
        }

        return script == SCRIPTED;
    }

    protected abstract int getScriptType();

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     * @param defaults DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isTrue(CSSValue value, boolean defaults) {
        if (value != null) {
            return value.isTrue();
        } else {
            return defaults;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     * @param str DOCUMENT ME!
     * @param b DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean is(CSSValue value, String str, boolean b) {
        if (value != null) {
            return value.is(str);
        } else {
            return b;
        }
    }
}
