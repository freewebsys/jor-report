package jatools.designer.variable;

import jatools.dataset.Dataset;
import jatools.dom.src.DatasetNodeSource;
import jatools.dom.src.NodeSource;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class DatasetTreeNodeValue extends TreeNodeValue {
    String[] columns = null;

    /**
     * Creates a new DatasetTreeNodeValue object.
     *
     * @param nodeSource DOCUMENT ME!
     * @param dispalyName DOCUMENT ME!
     * @param pessison DOCUMENT ME!
     * @param variableName DOCUMENT ME!
     */
    public DatasetTreeNodeValue(NodeSource nodeSource, String dispalyName, int pessison,
        String variableName) {
        super(nodeSource, dispalyName, pessison, variableName);
    }

    /**
     * DOCUMENT ME!
     */
    public void invalidate() {
        this.columns = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String[] getColumns() {
        if (this.columns == null) {
            this.columns = Dataset.getFieldNames(((DatasetNodeSource) this.getNodeSource()).getReader());
        }

        return columns;
    }
}
