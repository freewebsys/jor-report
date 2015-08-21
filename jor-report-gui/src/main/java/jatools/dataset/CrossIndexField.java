package jatools.dataset;

import bsh.BSHArguments;
import bsh.CallStack;
import bsh.EvalError;
import bsh.Filter;
import bsh.Interpreter;
import bsh.Node;
import bsh.Primitive;
import bsh.PropertyGetter;
import bsh.UtilEvalError;

import jatools.component.table.PowerTable;

import jatools.dom.field.AbstractValuesField;
import jatools.dom.field.SimpleValuesField;

import jatools.engine.script.KeyStack;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.7 $
 */
public class CrossIndexField extends AbstractValuesField implements Filter, PropertyGetter {
    static final String[] properties = { "ALL", "ALL2", "DEF", "DEF2" };
    private CrossIndexView indexView;
    private KeyStack key;
    private KeyStack key2;
    int col;

    //private Object def;
    private Object def2;
    private Object all;
    private Object all2;

    /**
     * Creates a new CrossField object.
     *
     * @param indexView
     *            DOCUMENT ME!
     * @param col
     *            DOCUMENT ME!
     * @param key
     *            DOCUMENT ME!
     * @param key2
     *            DOCUMENT ME!
     */
    public CrossIndexField(CrossIndexView indexView, int col, KeyStack key, KeyStack key2) {
        this.indexView = indexView;

        this.col = col;

        this.key = key;
        this.key2 = key2;
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
    public Object[] values() {
        try {
            RowSet rowset = this.indexView.locate(Dataset.ANY, Dataset.ANY);

            if (rowset != null) {
                return rowset.valuesAt(col);
            }
        } catch (DatasetException e) {
            e.printStackTrace();
        }

        return EMPTY_ARRAY;
    }

    /**
     * DOCUMENT ME!
     *
     * @param _key DOCUMENT ME!
     * @param _key2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] locate(Key _key, Key _key2) {
        try {
            RowSet rowset = this.indexView.locate(_key, _key2);

            if (rowset != null) {
                return rowset.valuesAt(col);
            }
        } catch (DatasetException e) {
            e.printStackTrace();
        }

        return EMPTY_ARRAY;
    }

    /**
     * DOCUMENT ME!
     *
     * @param callstack
     *            DOCUMENT ME!
     * @param interpreter
     *            DOCUMENT ME!
     * @param parameterNode
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws EvalError
     *             DOCUMENT ME!
     */
    public Object filter(CallStack callstack, Interpreter interpreter, Node parameterNode)
        throws EvalError {
        int col = getColumn();
        Key key = null;

        if (parameterNode instanceof BSHArguments) {
            Object[] keys = ((BSHArguments) parameterNode).getArguments(callstack, interpreter);

            for (int i = 0; i < keys.length; i++) {
                keys[i] = Primitive.unwrap(keys[i]);
            }

            if ((keys.length == 1) && (keys[0] == PowerTable.CURRENT_ROW_KEY)) {
                key = this.key.getKey();
            } else {
                key = new Key(keys);
            }
        } else if (parameterNode == null) {
            key = Dataset.ANY;
        }

        if (key != null) {
            return new FilteredCrossIndexField(this.indexView.locate(key), col, key2);
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     * @param callstack DOCUMENT ME!
     * @param interpreter DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws UtilEvalError DOCUMENT ME!
     */
    public Object getProperty(String prop, CallStack callstack, Interpreter interpreter)
        throws UtilEvalError {
        if (prop.equals("DEF2")) {
            if (def2 == null) {
                def2 = new DEF2();
            }

            return def2;
        } else {
            if (prop.equals("ALL")) {
                if (all == null) {
                    all = new FilteredCrossIndexField(this.indexView.locate(Dataset.ANY), col, key2);
                }

                return all;
            } else if (prop.equals("ALL2")) {
                if (all2 == null) {
                    all2 = new SimpleValuesField(this.locate(Dataset.ANY, Dataset.ANY));
                }

                return all2;
            } else if (prop.equals("DEF")) {
                return new FilteredCrossIndexField(this.indexView.locate(key.getKey()), col, key2);
            } else {
                return Primitive.VOID;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String[] getPropertyNames() {
        return this.properties;
    }

    class DEF2 extends AbstractValuesField {
        @Override
        public Object[] values() {
            return locate(key.getKey(), key2.getKey());
        }

        @Override
        public int getColumn() {
            // TODO Auto-generated method stub
            return 0;
        }
    }
}
