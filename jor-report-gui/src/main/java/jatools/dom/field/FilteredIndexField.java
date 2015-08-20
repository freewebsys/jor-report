package jatools.dom.field;

import jatools.dataset.DatasetException;
import jatools.dataset.IndexView;
import jatools.dataset.Key;
import jatools.dataset.RowSet;
import jatools.engine.InterpreterAware;
import bsh.Interpreter;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class FilteredIndexField extends AbstractValueField implements InterpreterAware {
    protected IndexView indexView;
    private int col;
    private Key key;
    private Interpreter it;

    /**
     * Creates a new FixedIndexField object.
     *
     * @param indexView DOCUMENT ME!
     * @param key DOCUMENT ME!
     * @param col DOCUMENT ME!
     */
    public FilteredIndexField(IndexView indexView, Key key, int col) {
        this.indexView = indexView;
        this.col = col;

        this.key = key;
    }

    /**
     * DOCUMENT ME!
     *
     * @param it DOCUMENT ME!
     */
    public void setInterpreter(Interpreter it) {
        this.it = it;
    }

    /**
     * DOCUMENT ME!
     *
     * @param it DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object value() {
        try {
            it.setValue2(this);

            RowSet index = this.indexView.locate(getKey());

            if (index != null) {
                return index.valueAt(getColumn());
            } else {
                return null;
            }
        } catch (DatasetException e) {
            e.printStackTrace();
        }

        return null;
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

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumn() {
        return this.col;
    }
}
