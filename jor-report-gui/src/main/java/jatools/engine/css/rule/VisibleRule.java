package jatools.engine.css.rule;

import jatools.engine.css.CSSValue;
import jatools.engine.css.PrintStyle;
import jatools.engine.script.Script;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class VisibleRule extends AbstractRule {
    /**
     * everypage,followed
     */
    public CSSValue printMode;

    /**
     * DOCUMENT ME!
     */
    public CSSValue visible;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNull() {
        return (printMode == null) && (visible == null);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer result = new StringBuffer();

        if (this.printMode != null) {
            result.append(PrintStyle.PRINT_MODE + ":" + this.printMode + ";");
        }

        if (this.visible != null) {
            result.append(PrintStyle.VISIBLE + ":" + this.visible + ";");
        }

        return result.toString();
    }

    protected int getScriptType() {
        if ((this.printMode != null) && this.printMode.isScript()) {
            return SCRIPTED;
        }

        if ((this.visible != null) && this.visible.isScript()) {
            return SCRIPTED;
        } else {
            return NO_SCRIPTED;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param script DOCUMENT ME!
     */
    public void reset(Script script) {
        if (isScript()) {
            if (this.printMode != null) {
                this.printMode.reset(script);
            }

            if (this.visible != null) {
                this.visible.reset(script);
            }
        }
    }
}
