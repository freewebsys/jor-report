package jatools.dom.src;

import jatools.accessor.PropertyDescriptor;
import jatools.component.ComponentConstants;
import jatools.engine.script.Script;

import org.w3c.dom.Node;



/**
 * <p>Title: XmlNodeSource</p>
 * <p>Description:xml node  </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: jatools</p>
 * @author Jiang
 * @version 1.0
 */
public class XmlNodeSource extends NodeSource {
    private String tagName;
    private String url;
    private String xpath;

    /**
     * Creates a new XmlNodeSource object.
     */
    public XmlNodeSource() {
    }

    /**
     * Creates a new XmlNodeSource object.
     *
     * @param tagName DOCUMENT ME!
     * @param url DOCUMENT ME!
     * @param xpath DOCUMENT ME!
     */
    public XmlNodeSource(String tagName, String url, String xpath) {
        super();
        this.tagName = tagName;
        this.url = url;
        this.xpath = xpath;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTagName() {
        return tagName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getUrl() {
        return url;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getXPath() {
        return xpath;
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     * @param script DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node create(Node parent, Script script) {
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
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] {
            ComponentConstants._CHILDREN,
            ComponentConstants._URL,
            new PropertyDescriptor("TagName", String.class, PropertyDescriptor.SERIALIZABLE),
            new PropertyDescriptor("XPath", String.class, PropertyDescriptor.SERIALIZABLE), new PropertyDescriptor("KeyExpression", String.class, PropertyDescriptor.SERIALIZABLE)
        };
    }

    /**
     * DOCUMENT ME!
     *
     * @param xpath DOCUMENT ME!
     */
    public void setXPath(String xpath) {
        this.xpath = xpath;
    }

    /**
     * DOCUMENT ME!
     *
     * @param url DOCUMENT ME!
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tagName DOCUMENT ME!
     */
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
}
