package jatools.dom.field;

import jatools.dataset.DatasetException;
import jatools.dataset.IndexView;
import jatools.dataset.Key;
import jatools.dataset.RowSet;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class FixedIndexField extends IndexField {
    Key key;

    /**
     * Creates a new FixedIndexField object.
     *
     * @param indexView DOCUMENT ME!
     * @param key DOCUMENT ME!
     * @param col DOCUMENT ME!
     */
    public FixedIndexField(IndexView indexView, Key key, int col) {
        super(indexView, col, null);
        this.key = key;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Key getKey() {
        return this.key;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean exists() {
        try {
            return this.indexView.locate(getKey()) != null;
        } catch (DatasetException e) {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRow() {
        try {
            RowSet rowset = this.indexView.locate(getKey());

            if (rowset != null) {
                return rowset.firstRow();
            }
        } catch (DatasetException e) {
        }

        return -1;
    }
}
