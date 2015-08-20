package jatools.dom.src;

import jatools.accessor.PropertyDescriptor;
import jatools.designer.App;
import jatools.engine.script.Script;

import org.w3c.dom.Node;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class DocumentNodeSource extends NodeSource {
    /**
     * Creates a new DatasetNodeSource object.
     *
     * @param reader DOCUMENT ME!
     */
    public DocumentNodeSource(NodeSource root) {
        this.add(root);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        throw new IllegalArgumentException(App.messages.getString("res.65"));
    }

    /**
    * DOCUMENT ME!
    *
    * @param script DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public Node create(Node parent, Script script) {
        throw new IllegalArgumentException(App.messages.getString("res.66"));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTagName() {
        return null;
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
        throw new IllegalArgumentException(App.messages.getString("res.67"));
    }
}
