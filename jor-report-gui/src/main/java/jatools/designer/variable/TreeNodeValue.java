package jatools.designer.variable;

import jatools.designer.data.Variable;

import jatools.dom.src.ArrayNodeSource;
import jatools.dom.src.CrossIndexNodeSource;
import jatools.dom.src.DatasetNodeSource;
import jatools.dom.src.GroupNodeSource;
import jatools.dom.src.IndexNodeSource;
import jatools.dom.src.NodeSource;
import jatools.dom.src.RootNodeSource;
import jatools.dom.src.RowNodeSource;
import jatools.dom.src.XmlNodeSource;

import java.awt.Rectangle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class TreeNodeValue extends Variable implements SourceType {
    private NodeSource nodeSource;
    private String display;
    List<String> selectFields = new ArrayList<String>();
    Map<String, Rectangle> fieldRects = new HashMap<String, Rectangle>();
    private boolean lastPressedNodeSource = true;

    /**
    * Creates a new TreeNodeValue object.
    *
    * @param nodeSource DOCUMENT ME!
    * @param dispalyName DOCUMENT ME!
    * @param pessison DOCUMENT ME!
    * @param variableName DOCUMENT ME!
    */
    public TreeNodeValue(NodeSource nodeSource, String dispalyName, int pessison,
        String variableName) {
        super(dispalyName, pessison, variableName);
        this.nodeSource = nodeSource;
        this.setDisplay(dispalyName);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isLastPressedNodeSource() {
        return lastPressedNodeSource;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String[] getSelectedFields() {
        if (this.selectFields != null) {
            return this.selectFields.toArray(new String[0]);
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param lastPressedNodeSource DOCUMENT ME!
     */
    public void setLastPressedNodeSource(boolean lastPressedNodeSource) {
        this.lastPressedNodeSource = lastPressedNodeSource;
    }

    /**
     * DOCUMENT ME!
     *
     * @param field DOCUMENT ME!
     */
    public void select(String field) {
        this.selectFields.add(field);
    }

    /**
     * DOCUMENT ME!
     */
    public void unselectAll() {
        if (this.selectFields != null) {
            this.selectFields.clear();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFieldAt(int x, int y) {
        if (this.fieldRects != null) {
            for (Entry<String, Rectangle> entry : fieldRects.entrySet()) {
                if (entry.getValue().contains(x, y)) {
                    return entry.getKey();
                }
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param field DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSelected(String field) {
        return ((this.selectFields != null) && this.selectFields.contains(field));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return display;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getSourceType() {
        if (nodeSource instanceof CrossIndexNodeSource) {
            return CROSS_DATASET_NODE_SOURCE;
        } else if (nodeSource instanceof IndexNodeSource) {
            return INDEX_DATASET_NODE_SOURCE;
        } else if (nodeSource instanceof DatasetNodeSource) {
            return DATASET_NODE_SOURCE;
        } else if (nodeSource instanceof RootNodeSource) {
            return ROOT_NODE_SOURCE;
        } else if (nodeSource instanceof RowNodeSource) {
            return ROW_NODE_SOURCE;
        } else if (nodeSource instanceof GroupNodeSource) {
            return GROUP_NODE_SOURCE;
        } else if (nodeSource instanceof XmlNodeSource) {
            return XML_NODE_SOURCE;
        } else if (nodeSource instanceof ArrayNodeSource) {
            return ARRAY_NODE_SOURCE;
        }

        return -2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NodeSource getNodeSource() {
        return nodeSource;
    }

    /**
     * DOCUMENT ME!
     *
     * @param display DOCUMENT ME!
     */
    public void setDisplay(String display) {
        this.display = display;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rects DOCUMENT ME!
     */
    public void setFieldRects(Map<String, Rectangle> rects) {
        this.fieldRects = rects;
    }
}
