package jatools.dom;

import jatools.engine.script.Script;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public interface Root {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Script getScript();

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param node DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object registerElement(ElementBase node);

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getElement(Object key);
}
