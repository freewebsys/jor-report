package jatools.data;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public interface Node {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getNodeType();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getData();

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node getChild(int i);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node[] getChildren();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int size();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName();
}
