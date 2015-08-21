package jatools.data.reader;

import jatools.accessor.PropertyAccessor;

import jatools.data.InvisibleVariable;

import jatools.dataset.Dataset;
import jatools.dataset.DatasetException;
import jatools.dataset.StreamService;

import jatools.engine.DynamicProperty;

import jatools.engine.script.Script;

import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public interface DatasetReader extends PropertyAccessor, Cloneable, InvisibleVariable,
    DynamicProperty {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator indexs();
    
   
    /**
     * DOCUMENT ME!
     *
     * @param script
     *            DOCUMENT ME!
     * @param requestedRows
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DatasetException
     *             DOCUMENT ME!
     */
    public Dataset read(Script script, int requestedRows)
        throws DatasetException;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDescription();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getType();

    /**
     * DOCUMENT ME!
     *
     * @param name
     *            DOCUMENT ME!
     */
    public void setName(String name);

    /**
     * DOCUMENT ME!
     *
     * @param description
     *            DOCUMENT ME!
     */
    public void setDescription(String description);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public StreamService getStreamService();
    
    
}
