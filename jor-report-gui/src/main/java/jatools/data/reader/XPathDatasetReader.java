package jatools.data.reader;

import jatools.accessor.PropertyDescriptor;
import jatools.dataset.Dataset;
import jatools.dataset.DatasetException;
import jatools.dataset.StreamService;
import jatools.designer.Main;
import jatools.dom.DatasetNode;
import jatools.dom.RootNode;
import jatools.dom.src.DatasetNodeSource;
import jatools.dom.src.NodeSource;
import jatools.engine.script.Script;

import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class XPathDatasetReader implements DatasetReader {
    private String XPath;
    private DatasetNodeSource nodeSrc;
    private Script script;

    /**
     * Creates a new XPathDatasetReader object.
     *
     * @param xpath
     *            DOCUMENT ME!
     */
    public XPathDatasetReader(String xpath) {
        this.XPath = xpath;
    }

    /**
     * Creates a new XPathDatasetReader object.
     */
    public XPathDatasetReader() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getXPath() {
        return XPath;
    }

    private DatasetReader getDelegate() {
        DatasetNodeSource src = getDatasetNodeSource();

        return (src == null) ? null : src.getReader();
    }

    private DatasetNodeSource getDatasetNodeSource() {
        if (this.nodeSrc == null) {
            NodeSource root = Main.getInstance().getActiveEditor().getDocument().getNodeSource();
            if (root != null) {
                this.nodeSrc = (DatasetNodeSource) root.getNodeSourceByPath(this.XPath);
            }
        }

        return this.nodeSrc;
    }
    
    private boolean isRuntime()
    {
    	return (this.script != null) && this.script.get("$$root") instanceof RootNode;
    }

    /**
     * DOCUMENT ME!
     *
     * @param xPath
     *            DOCUMENT ME!
     */
    public void setXPath(String xPath) {
        XPath = xPath;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] { new PropertyDescriptor("XPath", String.class) };
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator indexs() {
        return getDelegate().indexs();
    }

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
        throws DatasetException {
        this.script = script;
        
        if(this.isRuntime())
        {
        	Object node =  this.script.eval("!{"+XPath+"}");
        	if(node instanceof DatasetNode)
        	{
        		return ((DatasetNode)node).getDataset();
        	}else
        		return null;
        	
        	
        }else
        {
        	return getDelegate().read(script, requestedRows);
        }
        
     
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        return getDelegate().clone();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return getDelegate().getName();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDescription() {
        return getDelegate().getDescription();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getType() {
        return getDelegate().getType();
    }

    /**
     * DOCUMENT ME!
     *
     * @param name
     *            DOCUMENT ME!
     */
    public void setName(String name) {
        getDelegate().setName(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @param description
     *            DOCUMENT ME!
     */
    public void setDescription(String description) {
        getDelegate().setDescription(description);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public StreamService getStreamService() {
        return getDelegate().getStreamService();
    }
}
