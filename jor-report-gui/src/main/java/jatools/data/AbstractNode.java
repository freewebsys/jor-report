package jatools.data;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public abstract class AbstractNode implements Node {
    /**
     * Creates a new AbstractNode object.
     */
    public AbstractNode() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNodeType() {
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getData() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node getChild(int i) {
        return this.getChildren()[i];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int size() {
        return this.getChildren().length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return null;
    }
}
