package jatools.dom.field;

import jatools.math.Math;

import java.util.ArrayList;

import bsh.Interpreter;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public abstract class AbstractField implements jatools.engine.ValueIfClosed {
    /**
     * DOCUMENT ME!
     *
     * @param it
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Object value();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Object[] values();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object sum() {
        return Math.sum(values());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object max() {
        return Math.max(values());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object min() {
        return Math.min(values());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object avg() {
        return Math.avg(values());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object count() {
        return Math.count(values());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object topOccurs() {
        return Math.topOccurs(values());
    }

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

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract int getColumn();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractField getSelf() {
        return this;
    }
}
