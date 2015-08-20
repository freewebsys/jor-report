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
public class RepeatRule extends AbstractRule {
    /**
     * DOCUMENT ME!
     */
    public CSSValue maxCountX;

    /**
     * DOCUMENT ME!
     */
    public CSSValue maxCountY;

    /**
     * DOCUMENT ME!
     */
    public CSSValue gapX;

    /**
     * DOCUMENT ME!
     */
    public CSSValue gapY;

    /**
     * DOCUMENT ME!
     */
    public CSSValue flowFirst;

    /**
     * DOCUMENT ME!
     */
    public CSSValue overflow;

    /**
     * DOCUMENT ME!
     */
    public CSSValue modCount;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNull() {
        return (overflow != null) && (maxCountX == null) && (maxCountY == null) && (gapX == null) &&
        (gapY == null) && (modCount == null);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer result = new StringBuffer();

        if (this.maxCountX != null) {
            result.append(PrintStyle.REPEAT_COUNT_X + ":" + this.maxCountX + ";");
        }

        if (this.maxCountY != null) {
            result.append(PrintStyle.REPEAT_COUNT_Y + ":" + this.maxCountY + ";");
        }

        if (this.gapX != null) {
            result.append(PrintStyle.REPEAT_GAP_X + ":" + this.gapX + ";");
        }

        if (this.gapY != null) {
            result.append(PrintStyle.REPEAT_GAP_Y + ":" + this.gapY + ";");
        }

        if (this.flowFirst != null) {
            result.append(PrintStyle.REPEAT_FLOW_FIRST + ":" + this.flowFirst + ";");
        }

        if (this.overflow != null) {
            result.append(PrintStyle.REPEAT_OVERFLOW + ":" + this.overflow + ";");
        }

        if (this.modCount != null) {
            result.append(PrintStyle.REPEAT_MOD_COUNT + ":" + this.modCount + ";");
        }

        return result.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param script DOCUMENT ME!
     */
    public void reset(Script script) {
        if (isScript()) {
            if (this.maxCountX != null) {
                this.maxCountX.reset(script);
            }

            if (this.maxCountY != null) {
                this.maxCountY.reset(script);
            }

            if (this.gapX != null) {
                this.gapX.reset(script);
            }

            if (this.gapY != null) {
                this.gapY.reset(script);
            }

            if (this.flowFirst != null) {
                this.flowFirst.reset(script);
            }

            if (this.overflow != null) {
                this.overflow.reset(script);
            }

            if (this.modCount != null) {
                this.modCount.reset(script);
            }
        }
    }

    protected int getScriptType() {
        if ((this.maxCountX != null) && this.maxCountX.isScript()) {
            return SCRIPTED;
        }

        if ((this.maxCountY != null) && this.maxCountY.isScript()) {
            return SCRIPTED;
        }

        if ((this.gapX != null) && this.gapX.isScript()) {
            return SCRIPTED;
        }

        if ((this.gapY != null) && this.gapY.isScript()) {
            return SCRIPTED;
        }

        if ((this.flowFirst != null) && this.flowFirst.isScript()) {
            return SCRIPTED;
        }

        if ((this.overflow != null) && this.overflow.isScript()) {
            return SCRIPTED;
        }

        if ((this.modCount != null) && this.modCount.isScript()) {
            return SCRIPTED;
        } else {
            return NO_SCRIPTED;
        }
    }
}
