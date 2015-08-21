package jatools.dataset;

import java.util.Iterator;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class CrossIndexView {
    private Map keysCache;

    /**
     * Creates a new CrossIndexView object.
     *
     * @param src DOCUMENT ME!
     * @param cols DOCUMENT ME!
     * @param cols2 DOCUMENT ME!
     */
    public CrossIndexView(RowSet src, int[] cols, int[] cols2) {
        this.keysCache = new CrossIndexBuilder(src, cols, cols2).build();
    }

    Iterator keys() {
        return this.keysCache.keySet().iterator();
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param key2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DatasetException DOCUMENT ME!
     */
    public RowSet locate(Key key, Key key2) throws DatasetException {
        CrossRowSet rowset = locate(key);

        if (rowset != null) {
            return rowset.locate(key2);
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CrossRowSet locate(Key key) {
        CrossRowSet rowset = (CrossRowSet) keysCache.get(key);

        return rowset;
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param key2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RowSet[] groups(Key key, Key key2) {
        return CrossGroupBuilder.getDefaults().build(this, key, key2);
    }
}
