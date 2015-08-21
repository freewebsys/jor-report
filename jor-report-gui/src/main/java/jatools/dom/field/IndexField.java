package jatools.dom.field;

import bsh.BSHArguments;
import bsh.CallStack;
import bsh.EvalError;
import bsh.Filter;
import bsh.Interpreter;
import bsh.Node;
import bsh.Primitive;

import jatools.dataset.Dataset;
import jatools.dataset.DatasetException;
import jatools.dataset.IndexView;
import jatools.dataset.Key;
import jatools.dataset.RowSet;

import jatools.engine.script.KeyStack;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class IndexField extends AbstractValuesField implements Filter {
    protected IndexView indexView;
    private int col;
    private KeyStack keyManager;

    /**
     * Creates a new IndexField object.
     *
     * @param indexStore DOCUMENT ME!
     * @param col DOCUMENT ME!
     * @param keyManager DOCUMENT ME!
     */
    public IndexField(IndexView indexStore, int col, KeyStack keyManager) {
        this.indexView = indexStore;
        this.col = col;

        this.keyManager = keyManager;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumn() {
        return this.col;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Key getKey() {
        return this.keyManager.getKey();
    }

    /**
     * DOCUMENT ME!
     *
     * @param callstack DOCUMENT ME!
     * @param interpreter DOCUMENT ME!
     * @param parameterNode DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws EvalError DOCUMENT ME!
     */
    public Object filter(CallStack callstack, Interpreter interpreter, Node parameterNode)
        throws EvalError {
        int col = getColumn();

        if (parameterNode instanceof BSHArguments) {
            Object[] keys = ((BSHArguments) parameterNode).getArguments(callstack, interpreter);

            for (int i = 0; i < keys.length; i++) {
                keys[i] = Primitive.unwrap(keys[i]);
            }

            Key key = null;

            if ((keys.length == 1) && (keys[0] == Dataset.ANY)) {
                key = Dataset.ANY;
            } else {
                key = new Key(keys);
            }

            Object value2 = new FilteredIndexField(this.indexView, key, col);
            interpreter.getRoot().setValue2(value2);

            return value2;
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getCols() {
        return indexView.getCols();
    }

 
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] values() {
        try {
            RowSet index = this.indexView.locate(getKey());

            if (index != null) {
                return index.valuesAt(getColumn());
            } else {
                return EMPTY_ARRAY;
            }
        } catch (DatasetException e) {
            e.printStackTrace();
        }

        return null;
    }
}
