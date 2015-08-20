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
public class PageRule extends AbstractRule {
    /**
     * DOCUMENT ME!
     */
    public CSSValue newPageX;

    /**
     * DOCUMENT ME!
     */
    public CSSValue newPageY;
    public CSSValue forceBreak;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNull() {
        return (newPageX == null) && (newPageY == null) && (forceBreak == null);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer result = new StringBuffer();

        if (this.newPageX != null) {
            result.append(PrintStyle.NEW_PAGE_X + ":" + this.newPageX + ";");
        }

        if (this.newPageY != null) {
            result.append(PrintStyle.NEW_PAGE_Y + ":" + this.newPageY + ";");
        }

        if (this.forceBreak != null) {
            result.append(PrintStyle.BREAK_PAGE_NEXT + ":" + this.forceBreak + ";");
        }

        return result.toString();
    }

    protected int getScriptType() {
        if ((this.newPageX != null) && this.newPageX.isScript()) {
            return SCRIPTED;
        }

        if ((this.forceBreak != null) && this.forceBreak.isScript()) {
            return SCRIPTED;
        }

        if ((this.newPageY != null) && this.newPageY.isScript()) {
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
            if (this.newPageX != null) {
                this.newPageX.reset(script);
            }

            if (this.newPageY != null) {
                this.newPageY.reset(script);
            }

            if (this.forceBreak != null) {
                this.forceBreak.reset(script);
            }
        }
    }
}
