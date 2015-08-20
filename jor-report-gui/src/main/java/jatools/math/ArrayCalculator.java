package jatools.math;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class ArrayCalculator implements GroupCalc {
    Object[] values;

    public ArrayCalculator(Object[] values) {
        this.values = values;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getAVG() {
        return Math.avg(values);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getCOUNT() {
        return Math.count(values);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getMAX() {
        return Math.max(values);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getMIN() {
        return Math.min(values);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getSUM() {
        return Math.sum(values);
    }

	public Object[] getArray() {
		// TODO Auto-generated method stub
		return values;
	}
}
