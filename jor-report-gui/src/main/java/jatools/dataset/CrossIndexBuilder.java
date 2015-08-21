package jatools.dataset;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class CrossIndexBuilder extends IndexBuilder {
    int[] cols2;

    CrossIndexBuilder(RowSet src, int[] fields, int[] cols2) {
        super(src, fields);
        this.cols2 = cols2;
    }

    protected Object getTotalIndex() {
        return new CrossRowSet(src.getDataset(), Dataset.ANY, ids, cols2);
    }

    protected RowSet getIndex(Key key, int _from, int _to) {
        int[] rows = new int[_to - _from + 1];
        System.arraycopy(ids, _from, rows, 0, rows.length);

        return new CrossRowSet(src.getDataset(), key, rows, cols2);
    }
}
