package jatools.dom.field;

import jatools.math.Math;

import java.util.ArrayList;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public abstract class AbstractValuesField implements ValuesField {
    protected static Object[] EMPTY_ARRAY = new Object[0];

    /*
     * (non-Javadoc)
     *
     * @see jatools.dom.field.ValuesField#values()
     */

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLength() {
        return this.values().length;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Object value() {
        return this.values();
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object get(int i) {
        Object[] vals = this.values();

        if (i < vals.length) {

            return vals[i];
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param nullIf DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object ifnull(Object nv) {
        Object vals = this.values();

        return (vals == null) ? nv : vals;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Object[] values();

    /*
     * (non-Javadoc)
     *
     * @see jatools.dom.field.ValuesField#sum()
     */

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object sum() {
        return Math.sum(values());
    }

    /*
     * (non-Javadoc)
     *
     * @see jatools.dom.field.ValuesField#max()
     */

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object max() {
        return Math.max(values());
    }

    /*
     * (non-Javadoc)
     *
     * @see jatools.dom.field.ValuesField#min()
     */

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object min() {
        return Math.min(values());
    }

    /*
     * (non-Javadoc)
     *
     * @see jatools.dom.field.ValuesField#avg()
     */

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object avg() {
        return Math.avg(values());
    }

    /*
     * (non-Javadoc)
     *
     * @see jatools.dom.field.ValuesField#count()
     */

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object count() {
        return Math.count(values());
    }

    /*
     * (non-Javadoc)
     *
     * @see jatools.dom.field.ValuesField#topOccurs()
     */

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object topOccurs() {
        return Math.topOccurs(values());
    }

    /*
     * (non-Javadoc)
     *
     * @see jatools.dom.field.ValuesField#distinctCount()
     */

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object distinctCount() {
        Object[] vals = values();

        if (vals != null) {
            ArrayList result = new ArrayList();

            for (int i = 0; i < vals.length; i++) {
                if (!result.contains(vals[i])) {
                    result.add(vals[i]);
                }
            }

            return new Integer(result.size());
        } else {
            return new Integer(0);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see jatools.dom.field.ValuesField#getColumn()
     */

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int getColumn();
}
