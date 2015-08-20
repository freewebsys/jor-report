package jatools.data.interval.formula;

import jatools.accessor.AutoAccessor;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class IntervalFormula extends AutoAccessor {
    String expression;
    String as;

    /**
     * Creates a new ComputedItem object.
     *
     * @param expression DOCUMENT ME!
     * @param as DOCUMENT ME!
     */
    public IntervalFormula(String expression, String as) {
        this.expression = expression;
        this.as = as;
    }

    /**
     * Creates a new ComputedItem object.
     */
    public IntervalFormula() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getExpression() {
        return expression;
    }

    /**
     * DOCUMENT ME!
     *
     * @param expression DOCUMENT ME!
     */
    public void setExpression(String expression) {
        this.expression = expression;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAs() {
        return as;
    }

    /**
     * DOCUMENT ME!
     *
     * @param as DOCUMENT ME!
     */
    public void setAs(String as) {
        this.as = as;
    }
}
