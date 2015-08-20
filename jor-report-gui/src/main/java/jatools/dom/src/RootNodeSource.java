package jatools.dom.src;

import jatools.accessor.PropertyDescriptor;
import jatools.component.ComponentConstants;
import jatools.dom.RootNode;
import jatools.engine.script.Script;

import org.w3c.dom.Node;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class RootNodeSource extends NodeSource {
    static final String ROOT = "root";
    private boolean cached;

    /**
    * DOCUMENT ME!
    *
    * @param script DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public Node create(Node parent, Script script) {
        RootNode result = new RootNode(parent);
        result.setSource(this);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTagName() {
        return ROOT;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param script DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node[] createNodeList(Node parent, Script script) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNodeListSource() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] {
            ComponentConstants._CHILDREN,
            new PropertyDescriptor("Cached", Boolean.TYPE, PropertyDescriptor.SERIALIZABLE),
            new PropertyDescriptor("KeyExpression", String.class, PropertyDescriptor.SERIALIZABLE)
        };
    }

    //
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCached() {
        return this.cached;
    }

    /**
     * DOCUMENT ME!
     *
     * @param cached DOCUMENT ME!
     */
    public void setCached(boolean cached) {
        this.cached = cached;
    }
}
