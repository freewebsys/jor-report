package jatools.dom;

import bsh.BSHArguments;
import bsh.CallStack;
import bsh.EvalError;
import bsh.Filter;
import bsh.Interpreter;
import bsh.NameSpace;
import bsh.Node;
import bsh.Primitive;
import bsh.PropertyGetter;
import bsh.SimpleNode;
import bsh.UtilEvalError;

import jatools.dataset.Dataset;
import jatools.dataset.Key;
import jatools.dataset.RowSet;

import jatools.dom.field.AbstractField;

import jatools.engine.script.Script;

import java.util.ArrayList;
import java.util.List;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class CrossGroupList implements Filter, PropertyGetter {
    CrossIndexNode crossNode;
    Key key;
    Key key2;
    private RowSet rset;

    /**
     * Creates a new CrossGroupList object.
     *
     * @param crossNode
     *            DOCUMENT ME!
     */
    public CrossGroupList(CrossIndexNode crossNode) {
        this.crossNode = crossNode;
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
        if (parameterNode instanceof BSHArguments) {
            Object[] keys = ((BSHArguments) parameterNode).getArguments(callstack, interpreter);

            for (int i = 0; i < keys.length; i++) {
                keys[i] = Primitive.unwrap(keys[i]);
            }

            if (key == null) {
                key = new Key(keys);
            } else {
                key2 = new Key(keys);
            }
        }

        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @param expr
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RankList rank(String expr) {
        return rank(expr, "asc");
    }

    /**
     * DOCUMENT ME!
     *
     * @param expr
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RankList rank_desc(String expr) {
        return rank(expr, "desc");
    }

    private RankList rank(String expr, String options) {
        if ((key == null) || (key2 == null)) {
            return null;
        }

        RowSet[] sets = this.crossNode.getIndexView().groups(key, key2);

        if ((sets.length > 0) && (expr != null)) {
            Script script = crossNode.getRoot().getScript();
            NameSpace ns = script.createNameSpace();

            if (!expr.startsWith("=")) {
                expr = "$." + expr;
            }

            SimpleNode node = script.parse(expr);
            script.pushNameSpace(ns);

            try {
                ns.setLocalVariable("$", this);
            } catch (UtilEvalError e1) {
                e1.printStackTrace();
            }

            Object[] vals = new Object[sets.length];

            for (int i = 0; i < vals.length; i++) {
                rset = sets[i];
                vals[i] = script.eval(node);
            }

            script.popNameSpace();

            return new RankList(vals, "asc".equals(options));
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop
     *            DOCUMENT ME!
     * @param callstack
     *            DOCUMENT ME!
     * @param interpreter
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getProperty(String prop, CallStack callstack, Interpreter interpreter) {
        int col = crossNode.getDataset().getColumnIndex(prop);

        if (col > -1) {
            return new RowSetField(col);
        } else {
            return Primitive.VOID;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String[] getPropertyNames() {
        Dataset data = this.crossNode.getDataset();

        if (data != null) {
            String[] result = new String[data.getColumnCount()];

            for (int i = 0; i < data.getColumnCount(); i++) {
                result[i] = data.getColumnName(i);
            }

            return result;
        } else {
            return null;
        }
    }

    class RowSetField extends AbstractField {
        int col;

        public RowSetField(int col) {
            this.col = col;
        }

        public Object value() {
            if (rset != null) {
                return rset.valueAt(getColumn());
            } else {
                return null;
            }
        }

        public Object[] values() {
            if (rset != null) {
                return rset.valuesAt(getColumn());
            } else {
                return null;
            }
        }

        public int getColumn() {
            return col;
        }
    }
}
