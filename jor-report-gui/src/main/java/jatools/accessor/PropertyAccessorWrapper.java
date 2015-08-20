package jatools.accessor;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public interface PropertyAccessorWrapper extends PropertyAccessor {
    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     * @param newValue DOCUMENT ME!
     * @param valueType DOCUMENT ME!
     */
    public void setValue(String prop, Object newValue, Class valueType);

    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     * @param valueType DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue(String prop, Class valueType);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Class getTargetClass();
}
