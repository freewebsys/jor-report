package jatools.dom.field;

import jatools.dataset.RowSet;
import bsh.Interpreter;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class RowSetField extends AbstractField {
    int col;
    private RowSet rset;

    /**
     * Creates a new RowSetField object.
     *
     * @param rset DOCUMENT ME!
     * @param col DOCUMENT ME!
     */
    public RowSetField(RowSet rset, int col) {
        this.rset = rset;
        this.col = col;
    }

    /**
     * DOCUMENT ME!
     *
     * @param it DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object value() {
        if (rset != null) {
            return rset.valueAt(getColumn());
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] values() {
        if (rset != null) {
            return rset.valuesAt(getColumn());
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumn() {
        return col;
    }
}
