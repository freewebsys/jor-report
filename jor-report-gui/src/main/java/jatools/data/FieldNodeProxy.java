package jatools.data;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class FieldNodeProxy implements NodeProxy {
    Node target;

    /**
     * Creates a new FieldNodeProxy object.
     */
    public FieldNodeProxy() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Node getTarget() {
        return target;
    }

    /**
     * DOCUMENT ME!
     *
     * @param target DOCUMENT ME!
     */
    public void setTarget(Node target) {
        this.target = target;
    }
}
