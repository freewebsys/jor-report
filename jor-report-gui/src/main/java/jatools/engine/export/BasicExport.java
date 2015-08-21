package jatools.engine.export;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class BasicExport {
    static BasicExport shared;
    private boolean cached;

    protected BasicExport() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCached() {
        return cached;
    }

    /**
     * DOCUMENT ME!
     */
    public void requireCache() {
        this.cached = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static BasicExport getInstance() {
        if (shared == null) {
            shared = new BasicExport();
        }

        return shared;
    }
}
