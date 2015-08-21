package jatools.dataset;

import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class IndexView {
    private Map keysCache;
    Dataset dataset;
    private int[] cols;

    /**
     * Creates a new GroupView object.
     *
     * @param dataset DOCUMENT ME!
     * @param rowset DOCUMENT ME!
     * @param group DOCUMENT ME!
     */
    public IndexView(Dataset ds, RowSet src, int[] cols) {
        this.keysCache = new IndexBuilder(src, cols).build();
        this.dataset = ds;
        this.cols = cols;
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws DatasetException DOCUMENT ME!
     */
    public RowSet locate(Key key) throws DatasetException {
        return (RowSet) keysCache.get(key);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dataset getDataset() {
        return dataset;
    }

	public int[] getCols() {
		return cols;
	}
}
