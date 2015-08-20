package jatools.dom.field;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class SimpleValuesField extends AbstractValuesField {
    private Object[] _values;

    /**
     * Creates a new SimpleValuesField object.
     *
     * @param values DOCUMENT ME!
     */
    public SimpleValuesField(Object[] values) {
        this._values = values;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @Override
    public Object[] values() {
        // TODO Auto-generated method stub
        return _values;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @Override
    public int getColumn() {
        // TODO Auto-generated method stub
        return 0;
    }
}
