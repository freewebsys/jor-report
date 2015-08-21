package jatools.dom.field;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public abstract class AbstractValueField implements jatools.engine.ValueIfClosed {
    /**
     * DOCUMENT ME!
     *
     * @param nv DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object ifnull(Object nv) {
        Object vals = value();

        return (vals == null) ? nv : vals;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int getColumn();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractValueField getSelf() {
        return this;
    }
}
