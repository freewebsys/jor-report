package jatools.data.sum;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class DistinctCountField extends Sum {
    /**
     * Creates a new DistinctCountField object.
     *
     * @param groupField DOCUMENT ME!
     * @param calcField DOCUMENT ME!
     */
    public DistinctCountField(String[] groupField, String calcField) {
        super(groupField, calcField);
    }

    /**
     * Creates a new DistinctCountField object.
     */
    public DistinctCountField() {
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
        if (values != null) {
            return convertToNumber(Integer.class, values.length);
        }

        return null;
    }
}
