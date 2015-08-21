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
public class CrossTabRule extends AbstractRule {
    /**
     * everypage,followed
     */
    public CSSValue topHeaderVisible;

    /**
     * DOCUMENT ME!
     */
    public CSSValue leftHeaderVisible;

    /**
     * DOCUMENT ME!
     */
    public CSSValue pageWrap;

	public int headerRows = -1;

	public int headerColumns = -1;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNull() {
        return (topHeaderVisible == null) && (leftHeaderVisible == null) && (pageWrap == null);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer result = new StringBuffer();

        if (this.topHeaderVisible != null) {
            result.append(PrintStyle.CROSSTAB_TOP_HEADER_VISIBLE + ":" + this.topHeaderVisible +
                ";");
        }

        if (this.leftHeaderVisible != null) {
            result.append(PrintStyle.CROSSTAB_LEFT_HEADER_VISIBLE + ":" + this.leftHeaderVisible +
                ";");
        }

        if (this.pageWrap != null) {
            result.append(PrintStyle.CROSSTAB_PAGE_WRAP + ":" + this.pageWrap + ";");
        }

        return result.toString();
    }

    protected int getScriptType() {
        if ((this.pageWrap != null) && this.pageWrap.isScript()) {
            return SCRIPTED;
        }

        if ((this.topHeaderVisible != null) && this.topHeaderVisible.isScript()) {
            return SCRIPTED;
        }

        if ((this.leftHeaderVisible != null) && this.leftHeaderVisible.isScript()) {
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
            if (this.pageWrap != null) {
                this.pageWrap.reset(script);
            }

            if (this.topHeaderVisible != null) {
                this.topHeaderVisible.reset(script);
            }

            if (this.leftHeaderVisible != null) {
                this.leftHeaderVisible.reset(script);
            }
        }
    }

	
}
