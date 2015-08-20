package jatools.dataset;

import jatools.designer.App;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
 */
public class Row implements Cloneable {
    /**
     * DOCUMENT ME!
     */
    public final static Row NO_MORE_ROWS = null;
    ArrayList valueCache;

    /**
     * DOCUMENT ME!
     */
    public int index;
    private boolean inserted;

    /**
     * Creates a new ZRow object.
     *
     * @param values
     *            DOCUMENT ME!
     */
    public Row(Object[] values) {
        this(Arrays.asList(values));
    }

    /**
     * Creates a new ZRow object.
     *
     * @param values
     *            DOCUMENT ME!
     */
    public Row(Collection values) {
        this.valueCache = new ArrayList(values);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object lastElement() {
        return valueCache.get(valueCache.size()-1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param initVal
     *            DOCUMENT ME!
     */
    public void addColumn(Object initVal) {
        valueCache.add(initVal);
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     */
    public void removeColumn(int index) {
        valueCache.remove(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] values() {
        return valueCache.toArray();
    }

    /**
     * DOCUMENT ME!
     *
     * @param cols DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] values(int[] cols) {
        Object[] vals = new Object[cols.length];

        for (int i = 0; i < vals.length; i++) {
            vals[i] = this.valueCache.get(cols[i]);
        }

        return vals;
    }

    /**
     * DOCUMENT ME!
     *
     * @param i
     *            DOCUMENT ME!
     * @param value
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int setValueAt(int i, Object value) {
        if ((i >= 0) && (i < valueCache.size())) {
            valueCache.set(i, value);

            return i;
        } else {
            throw new ArrayIndexOutOfBoundsException(App.messages.getString("res.605") + i + App.messages.getString("res.606")); // //$NON-NLS-2$
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param i
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Object getValueAt(int i) {
        if ((i >= 0) && (i < valueCache.size())) {
            return valueCache.get(i);
        } else {
            throw new ArrayIndexOutOfBoundsException(App.messages.getString("res.605") + i + App.messages.getString("res.606")); // //$NON-NLS-2$
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param i
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final InputStream getInputStream(int i) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param row
     *            DOCUMENT ME!
     */
    public final void copyFromOtherRow(Row row) {
        int count = 0;
        count = row.getColumnCount();

        for (int i = 0; i < count; i++) {
            setValueAt(i, row.getValueAt(i));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    final public Object[] getColumns() {
        return valueCache.toArray();
    }

    /**
     * DOCUMENT ME!
     *
     * @param columns
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */

    // final public Object[] getColumns1(int[] columns) {
    // if (columns.length > valueCache.size()) {
    // return null;
    // }
    //
    // Object[] value = new Object[columns.length];
    //
    // for (int i = 0; i < columns.length; i++) {
    // if (columns[i] < valueCache.length) {
    // value[i] = valueCache[columns[i]];
    // } else {
    // return null;
    // }
    // }
    //
    // return value;
    // }
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumnCount() {
        return valueCache.size();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        try {
            Row inst = (Row) super.clone();
            inst.valueCache = (ArrayList) this.valueCache.clone();

            return inst;
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param columns DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Row clone(int[] columns) {
        Object[] values = new Object[columns.length];

        for (int i = 0; i < columns.length; i++) {
            values[i] = valueCache.get(columns[i]);
        }

        return new Row(values);
    }

    /**
     * DOCUMENT ME!
     */
    public void setAllNulls() {
        if (valueCache != null) {
            for (int i = 0; i < valueCache.size(); i++) {
                valueCache.set(i, null);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param another
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Row another) {
        int count = 0;

        for (int i = 0; i < getColumnCount(); i++) {
            if ((getValueAt(i) != null) && (another.getValueAt(i) != null)) {
                if (getValueAt(i).equals(another.getValueAt(i))) {
                    count++;
                }
            } else if ((getValueAt(i) == null) && (another.getValueAt(i) == null)) {
                count++;
            }
        }

        return count == getColumnCount();
    }

    /**
     * DOCUMENT ME!
     *
     * @param row
     *            DOCUMENT ME!
     * @param columns
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Row row, int[] columns) {
        return equals(row, columns, columns.length);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param columns DOCUMENT ME!
     * @param size DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Row row, int[] columns, int size) {
        int count = 0;

        for (int i = 0; i < size; i++) {
            if ((getValueAt(columns[i]) != null) && (row.getValueAt(columns[i]) != null)) {
                if (getValueAt(columns[i]).equals(row.getValueAt(columns[i]))) {
                    count++;
                }
            } else if ((getValueAt(columns[i]) == null) && (row.getValueAt(columns[i]) == null)) {
                count++;
            }
        }

        return count == size;
    }

    /**
     * DOCUMENT ME!
     *
     * @param vals DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object[] vals) {
        for (int i = 0; i < vals.length; i++) {
            if (!equals(getValueAt(i), vals[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param vals DOCUMENT ME!
     * @param cols DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object[] vals, int[] cols) {
        for (int i = 0; i < vals.length; i++) {
            if (!equals(getValueAt(cols[i]), vals[i])) {
                return false;
            }
        }

        return true;
    }

    private static boolean equals(Object val1, Object val2) {
        return (val1 != null) ? val1.equals(val2) : (val2 == null);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(" : "); //
        stringBuffer.append(valueCache.toString());

        return stringBuffer.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isInserted() {
        return inserted;
    }

    /**
     * DOCUMENT ME!
     *
     * @param inserted DOCUMENT ME!
     */
    public void setInserted(boolean inserted) {
        this.inserted = inserted;
    }
}
