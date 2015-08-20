package jatools.dataset;

import bsh.CallStack;
import bsh.Interpreter;
import bsh.Primitive;
import bsh.PropertyGetter;
import bsh.PropertySetter;

import java.util.ArrayList;
import java.util.List;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class RowBuffer extends Row implements PropertyGetter, PropertySetter, ScriptableRow {
    public final static int NOP = 0;
    public final static int NEW = 1;

    // // public final static int DELETED = 2;
    // public final static int UPDATE = 3;

    // int state;
    private Dataset data;
    private int row = -1;
    private int action;

    /**
     * Creates a new RowBuffer object.
     *
     * @param values
     *            DOCUMENT ME!
     */
    public RowBuffer(Object[] values, Dataset data, int row) {
        this(values, data);
        this.row = row;

        if (data.getRow(row).isInserted()) {
            action = NEW;
        }
    }

    /**
     * Creates a new RowBuffer object.
     *
     * @param values
     *            DOCUMENT ME!
     * @param data
     *            DOCUMENT ME!
     */
    public RowBuffer(Object[] values, Dataset data) {
        super(values);
        this.data = data;
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
        if (data != null) {
            int col = data.getColumnIndex(prop);

            if (col > -1) {
                Object result = this.getValueAt(col);

                if (result == null) {
                    return Primitive.NULL;
                } else {
                    return result;
                }
            }
        }

        return Primitive.VOID;
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop
     *            DOCUMENT ME!
     * @param val
     *            DOCUMENT ME!
     */
    public void assign(String prop, Object val) {
    }

    // /**
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNew() {
        return this.action == NEW;
    }

    //
    // /**
    // * DOCUMENT ME!
    // *
    // * @return DOCUMENT ME!
    // */
    // public boolean isDeleted() {
    // return state == DELETED;
    // }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getAction2() {
        int result = NOP;

        if (action == DELETE) {
            result = DELETE;
        } else {
            if (isChanged()) {
                if (isNew()) {
                    result = RowBuffer.INSERT;
                } else {
                    result = RowBuffer.UPDATE;
                }
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param action
     *            DOCUMENT ME!
     */
    public void setAction(int action) {
        this.action = action;
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop
     *            DOCUMENT ME!
     * @param val
     *            DOCUMENT ME!
     */
    public void setProperty(String prop, Object val) {
        int col = data.getColumnIndex(prop);

        if (col > -1) {
            setValueAt(col, Primitive.unwrap(val));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isChanged() {
        if (isNew()) {
            return true;
        } else {
            for (int i = 0; i < this.getColumnCount(); i++) {
                Object newval = this.getValueAt(i);
                Object old = data.getValueAt(row, i);
                boolean eq = (old == null) ? (newval == null) : old.equals(newval);

                if (!eq) {
                    return true;
                }
            }

            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param col DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue(String col) {
        return this.getValueAt(this.data.getColumnIndex(col));
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String[] getPropertyNames() {
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
}
