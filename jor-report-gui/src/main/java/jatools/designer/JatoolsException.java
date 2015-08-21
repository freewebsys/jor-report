package jatools.designer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class JatoolsException extends Exception {
    Object source;

    /**
     * Creates a new JatoolsException object.
     *
     * @param err DOCUMENT ME!
     */
    public JatoolsException(String err) {
        super(err);
    }
    
    public JatoolsException(Throwable e) {
        super(e);
    }

    /**
     * Creates a new JatoolsException object.
     *
     * @param err DOCUMENT ME!
     * @param src DOCUMENT ME!
     */
    public JatoolsException(String err, Object src) {
        super(err);
        this.source = src;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getSource() {
        return this.source;
    }
}
