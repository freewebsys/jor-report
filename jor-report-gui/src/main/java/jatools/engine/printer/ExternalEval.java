package jatools.engine.printer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public interface ExternalEval {
    /**
     * DOCUMENT ME!
     *
     * @param val DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public void setExternalValue(Object val);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getLastValue();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasExternalValue();
}
