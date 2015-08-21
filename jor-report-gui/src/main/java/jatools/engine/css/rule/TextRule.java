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
public class TextRule extends AbstractRule {
    /**
     * DOCUMENT ME!
     */
    public CSSValue unitedLevel;

    /**
     * DOCUMENT ME!
     */
    public CSSValue autoSize;

    /**
     * DOCUMENT ME!
     */
    public CSSValue maxWidth;


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNull() {
        return (this.unitedLevel == null) && (this.autoSize == null) && (this.maxWidth == null);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer result = new StringBuffer();

        if (this.unitedLevel != null) {
            result.append(PrintStyle.UNITED_LEVEL + ":" + this.unitedLevel.toString() + ";");
        }

        if (this.autoSize != null) {
            result.append(PrintStyle.AUTO_SIZE + ":" + this.autoSize.toString() + ";");
        }

        if (this.maxWidth != null) {
            result.append(PrintStyle.MAX_WIDTH + ":" + this.maxWidth.toString() + ";");
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
            if (this.unitedLevel != null) {
                this.unitedLevel.reset(script);
            }

            if (this.autoSize != null) {
                this.autoSize.reset(script);
            }

            if (this.maxWidth != null) {
                this.maxWidth.reset(script);
            }

         
        }
    }

    protected int getScriptType() {
        if ((this.unitedLevel != null) && this.unitedLevel.isScript()) {
            return SCRIPTED;
        }

        if ((this.maxWidth != null) && this.maxWidth.isScript()) {
            return SCRIPTED;
        }

       
        if ((this.autoSize != null) && this.autoSize.isScript()) {
            return SCRIPTED;
        } else {
            return NO_SCRIPTED;
        }
    }
}
