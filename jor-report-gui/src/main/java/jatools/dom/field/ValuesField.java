package jatools.dom.field;

import bsh.Index;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public interface ValuesField extends jatools.engine.ValueIfClosed,Index {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] values();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLength();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object sum();
    
    
    

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object max();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object min();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object avg();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object count();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object topOccurs();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object distinctCount();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumn();
}
