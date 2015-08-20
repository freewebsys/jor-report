package jatools.dataset;

import jatools.dom.field.AbstractValuesField;
import jatools.dom.field.SimpleValuesField;
import jatools.engine.script.KeyStack;
import bsh.BSHArguments;
import bsh.CallStack;
import bsh.EvalError;
import bsh.Filter;
import bsh.Interpreter;
import bsh.Node;
import bsh.Primitive;
import bsh.PropertyGetter;
import bsh.UtilEvalError;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
  */
public class FilteredCrossIndexField extends AbstractValuesField implements Filter, PropertyGetter {
    static final String[] properties = { "ALL", "DEF" };
    private CrossRowSet rowset;
    private KeyStack def;
    int col;
   // private Key key;

    /**
     * Creates a new CrossField2 object.
     *
     * @param rowset DOCUMENT ME!
     * @param col DOCUMENT ME!
     * @param def DOCUMENT ME!
     */
    public FilteredCrossIndexField(CrossRowSet rowset, int col, KeyStack def) {
        this.rowset = rowset;
        this.col = col;
        this.def = def;
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
        if (this.rowset != null) {
            try {
                RowSet rowset = this.rowset.locate(Dataset.ANY);

                if (rowset != null) {
                    return rowset.valuesAt(col);
                }
            } catch (DatasetException e) {
                e.printStackTrace();
            }
        }

        return EMPTY_ARRAY;
    }

    /**
     * DOCUMENT ME!
     *
     * @param _key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] locate(Key _key) {
        if (this.rowset != null) {
            try {
                RowSet rowset = this.rowset.locate(_key);

                if (rowset != null) {
                    return rowset.valuesAt(col);
                }
            } catch (DatasetException e) {
                e.printStackTrace();
            }
        }

        return EMPTY_ARRAY;
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
        if (prop.equals("ALL")) {
            return new SimpleValuesField(this.locate(Dataset.ANY));
        } else if (prop.equals("DEF")) {
            return new SimpleValuesField(this.locate(def.getKey()));
        } else {
            return Primitive.VOID;
        }
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
    	Key key = null;
        if (parameterNode instanceof BSHArguments) {
            Object[] keys = ((BSHArguments) parameterNode).getArguments(callstack, interpreter);

            for (int i = 0; i < keys.length; i++) {
                keys[i] = Primitive.unwrap(keys[i]);
            }

            key = new Key(keys);
        } else if (parameterNode == null) {
            key = Dataset.ANY;
        }

        if (key != null) {
            return new SimpleValuesField(this.locate(key));
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String[] getPropertyNames() {
        // TODO Auto-generated method stub
        return this.properties;
    }
}
