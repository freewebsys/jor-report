package jatools.data.sum;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class AvgField extends SumField {
    /**
     * Creates a new ZGroupAvg object.
     *
     * @param groupField DOCUMENT ME!
     * @param calcField DOCUMENT ME!
     */
    public AvgField(String[] groupField, String calcField) {
        super(groupField, calcField);
    }

    /**
     * Creates a new ZGroupAvg object.
     */
    public AvgField() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param values
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue(Object[] values) {
        return calculate(values);
    }

    /**
     * DOCUMENT ME!
     *
     * @param values DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object calculate(Object[] values) {
        Object val = null;

        if (values != null) {
            if (values.length == 1) {
                val = values[0];
            } else {
                val = (Object) divide((Number) SumField.calculate(values),
                        convertToNumber(values[0].getClass(), values.length));
            }
        }

        return val;
    }
}
