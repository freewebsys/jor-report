package jatools.data.reader;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class Index {
    private String key;
    private String variable;

    /**
     * Creates a new Index object.
     *
     * @param key DOCUMENT ME!
     * @param variable DOCUMENT ME!
     */
    public Index(String key, String variable) {
        this.key = key;
        this.variable = variable;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getKey() {
        return key;
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getVariable() {
        return variable;
    }

    /**
     * DOCUMENT ME!
     *
     * @param variable DOCUMENT ME!
     */
    public void setVariable(String variable) {
        this.variable = variable;
    }
}
