package jatools.dom;

import jatools.accessor.ProtectPublic;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public abstract class NodeBase implements Node, ProtectPublic {
    /**
     * DOCUMENT ME!
     *
     * @param newChild DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public Node appendChild(Node newChild) throws DOMException {
        throw new DOMException(DatasetNode.NOT_IMPL_ERROR_CODE, DatasetNode.NOT_IMPL_ERROR_MSG);
    }

    /**
     * DOCUMENT ME!
     *
     * @param newChild DOCUMENT ME!
     * @param refChild DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public Node insertBefore(Node newChild, Node refChild)
        throws DOMException {
        throw new DOMException(DatasetNode.NOT_IMPL_ERROR_CODE, DatasetNode.NOT_IMPL_ERROR_MSG);
    }

    /**
     * DOCUMENT ME!
     *
     * @param feature DOCUMENT ME!
     * @param version DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSupported(String feature, String version) {
        return false;
    }

    /**
     * DOCUMENT ME!
     */
    public void normalize() {
        throw new DOMException(DatasetNode.NOT_IMPL_ERROR_CODE, DatasetNode.NOT_IMPL_ERROR_MSG);
    }

    /**
     * DOCUMENT ME!
     *
     * @param oldChild DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public Node removeChild(Node oldChild) throws DOMException {
        throw new DOMException(DatasetNode.NOT_IMPL_ERROR_CODE, DatasetNode.NOT_IMPL_ERROR_MSG);
    }

    /**
     * DOCUMENT ME!
     *
     * @param newChild DOCUMENT ME!
     * @param oldChild DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public Node replaceChild(Node newChild, Node oldChild)
        throws DOMException {
        throw new DOMException(DatasetNode.NOT_IMPL_ERROR_CODE, DatasetNode.NOT_IMPL_ERROR_MSG);
    }

    /**
     * DOCUMENT ME!
     *
     * @param nodeValue DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void setNodeValue(String nodeValue) throws DOMException {
        throw new DOMException(DatasetNode.NOT_IMPL_ERROR_CODE, DatasetNode.NOT_IMPL_ERROR_MSG);
    }

    /**
     * DOCUMENT ME!
     *
     * @param prefix DOCUMENT ME!
     *
     * @throws DOMException DOCUMENT ME!
     */
    public void setPrefix(String prefix) throws DOMException {
        throw new DOMException(DatasetNode.NOT_IMPL_ERROR_CODE, DatasetNode.NOT_IMPL_ERROR_MSG);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getNodeName() {
        return getTagName();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTagName() {
        return ((getPrefix() != null) ? (getPrefix() + ":") : "") + getLocalName();
    }
}
