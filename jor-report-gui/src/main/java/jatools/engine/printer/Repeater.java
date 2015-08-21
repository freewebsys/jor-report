package jatools.engine.printer;

import jatools.component.Component;
import jatools.engine.css.rule.RepeatRule;
import jatools.engine.script.Script;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class Repeater {
    

    
    int maxwidth = 0;

    
    int maxheight = 0;

    
    int x0 = 0;
    int y0 = 0;

    
    int row = 0;
    int col = 0;
    private RepeatRule rule;

    Repeater(RepeatRule rule) {
        this.rule = rule;
    }

    Repeater(int i) {
    }

    /**
     * DOCUMENT ME!
     */
    public void resetY() {
    }

    void reset2(Component c, Script script) {
        this.x0 = c.getX();
        this.y0 = c.getY();

        this.maxheight = 0;
        this.maxwidth = 0;

        this.row = 0;
        this.col = 0;

        if (this.rule != null) {
            this.rule.reset(script);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getGapX() {
        if ((rule != null) && (rule.gapX != null)) {
            return rule.gapX.intValue();
        } else {
            return 0;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getGapY() {
        if ((rule != null) && (rule.gapY != null)) {
            return rule.gapY.intValue();
        } else {
            return 0;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMaxCol() {
        int maxCol = 1;

        if ((rule != null) && (rule.maxCountX != null)) {
            if (rule.maxCountX.isAuto()) {
                maxCol = Integer.MAX_VALUE;
            } else {
                maxCol = rule.maxCountX.intValue();
            }
        }

        return maxCol;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMaxheight() {
        return maxheight;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMaxRow() {
        int maxRow = Integer.MAX_VALUE;

        if ((rule != null) && (rule.maxCountY != null)) {
            if (rule.maxCountY.isAuto()) {
                maxRow = Integer.MAX_VALUE;
            } else {
                maxRow = rule.maxCountY.intValue();
            }
        }

        return maxRow;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getModCount() {
        int result = -1;

        if ((rule != null) && (rule.modCount != null)) {
            result = rule.modCount.intValue();
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMaxwidth() {
        return maxwidth;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isOverhidden() {
        boolean overhidden = false;

        if ((rule != null) && (rule.overflow != null)) {
            overhidden = rule.overflow.is("hidden");
        }

        return overhidden;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isRowFirst() {
        boolean rowFirst = false;

        if ((rule != null) && (rule.flowFirst != null)) {
            rowFirst = rule.flowFirst.is("row");
        }

        return rowFirst;
    }
}
