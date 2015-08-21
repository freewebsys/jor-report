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
public class LayoutRule extends AbstractRule {
    /**
     * DOCUMENT ME!
     */
    public CSSValue alignX;

    /**
     * DOCUMENT ME!
     */
    public CSSValue alignY;

    /**
     * DOCUMENT ME!
     */
    public CSSValue width;

    /**
     * DOCUMENT ME!
     */
    public CSSValue height;

    /**
     * DOCUMENT ME!
     */
    public CSSValue bottom;

    /**
     * DOCUMENT ME!
     */
    public CSSValue right;

    /**
     * DOCUMENT ME!
     */
    public CSSValue left;

    /**
     * DOCUMENT ME!
     */
    public CSSValue top;

  

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNull() {
        return (alignX == null) && (alignY == null) && (width == null) && (height == null) &&
        (right == null) && (bottom == null) && (left == null) && (top == null) ;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer result = new StringBuffer();

        if (this.alignX != null) {
            result.append(PrintStyle.ALIGN_X + ":" + this.alignX + ";");
        }

        if (this.alignY != null) {
            result.append(PrintStyle.ALIGN_Y + ":" + this.alignY + ";");
        }

        if (this.left != null) {
            result.append(PrintStyle.LEFT + ":" + this.left + ";");
        }

        if (this.top != null) {
            result.append(PrintStyle.TOP + ":" + this.top + ";");
        }

        if (this.bottom != null) {
            result.append(PrintStyle.BOTTOM + ":" + this.bottom + ";");
        }

        if (this.right != null) {
            result.append(PrintStyle.RIGHT + ":" + this.right + ";");
        }

        if (this.width != null) {
            result.append(PrintStyle.WIDTH + ":" + this.width + ";");
        }

        if (this.height != null) {
            result.append(PrintStyle.HEIGHT + ":" + this.height + ";");
        }

       

        return result.toString();
    }

    protected int getScriptType() {
        if ((this.alignX != null) && this.alignX.isScript()) {
            return SCRIPTED;
        }

        if ((this.alignX != null) && this.alignX.isScript()) {
            return SCRIPTED;
        }

        if ((this.bottom != null) && this.bottom.isScript()) {
            return SCRIPTED;
        }

        if ((this.right != null) && this.right.isScript()) {
            return SCRIPTED;
        }

        if ((this.left != null) && this.left.isScript()) {
            return SCRIPTED;
        }

        if ((this.top != null) && this.top.isScript()) {
            return SCRIPTED;
        }

        if ((this.width != null) && this.width.isScript()) {
            return SCRIPTED;
        }

       

        if ((this.height != null) && this.height.isScript()) {
            return SCRIPTED;
        } else {
            return NO_SCRIPTED;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param script DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public LayoutRule clone(Script script) {
        if (!isScript()) {
            return this;
        } else {
            LayoutRule rule = new LayoutRule();

            if (this.alignX != null) {
                rule.alignX = this.alignX.clone(script);
            }

            if (this.alignY != null) {
                rule.alignY = this.alignY.clone(script);
            }

            if (this.bottom != null) {
                rule.bottom = this.bottom.clone(script);
            }

            if (this.right != null) {
                rule.right = this.right.clone(script);
            }

            if (this.left != null) {
                rule.left = this.left.clone(script);
            }

            if (this.top != null) {
                rule.top = this.top.clone(script);
            }

            if (this.width != null) {
                rule.width = this.width.clone(script);
            }

            if (this.height != null) {
                rule.height = this.height.clone(script);
            }

           

            return rule;
        }
    }
}
