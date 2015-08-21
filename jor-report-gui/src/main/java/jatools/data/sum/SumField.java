package jatools.data.sum;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public class SumField extends Sum {
    /**
     * Creates a new ZGroupSum object.
     *
     * @param groupField DOCUMENT ME!
     * @param calcField DOCUMENT ME!
     */
    public SumField(String[] groupField, String calcField) {
        super(groupField, calcField);
    }
    public SumField(String name,String[] groupField, String calcField) {
        this(groupField, calcField);
        this.setName( name);
    }
    /**
     * Creates a new ZGroupSum object.
     */
    public SumField() {
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
        return (calculate(values));
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

        if ((values != null) && (values.length > 0) && values[0] instanceof Number) {
            if (values.length == 1) {
                val = values[0];
            } else {
                Number value = (Number) values[0];

                for (int i = 1; i < values.length; i++) {
                    value = sum(value, (Number) values[i]);
                }

                val = value;
            }
        }

        return val;
    }
}
