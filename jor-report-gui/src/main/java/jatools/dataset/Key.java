package jatools.dataset;

import org.apache.commons.lang.ArrayUtils;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class Key {
    public static final Object ANY = Dataset.ANY;
    public static final Object UNKNOWN = new Object();
    
    public Object[] elements;

    /**
     * Creates a new Key object.
     *
     * @param keys DOCUMENT ME!
     */
    public Key(Object[] keys) {
        this.elements = keys;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLength() {
        if (elements == null) {
            return 0;
        } else {
            return this.elements.length;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Key getParent() {
        if (elements.length > 0) {
            Object[] newkey = new Object[elements.length - 1];
            System.arraycopy(elements, 0, newkey, 0, newkey.length);

            return new Key(newkey);
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object lastValue() {
        if ((this.elements != null) && (this.elements.length > 0)) {
            return this.elements[elements.length - 1];
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isChildOf(Key key) {
        return ((key.elements.length + 1) == (this.elements.length)) && this.startWith(key);
    }

    private boolean startWith(Key key) {
        if (key.elements.length > this.elements.length) {
            return false;
        }

        for (int i = 0; i < key.elements.length; i++) {
            if (!equals(key.elements[i], this.elements[i])) {
                return false;
            }
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        Key that = (Key) obj;

        if (this.elements.length != that.elements.length) {
            return false;
        } else {
            for (int i = 0; i < elements.length; i++) {
                if (!equals(this.elements[i], that.elements[i])) {
                    return false;
                }
            }

            return true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param one DOCUMENT ME!
     * @param another DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object one, Object another) {
        return (one != null) ? one.equals(another) : (another == null);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int hashCode() {
        int code = 0;

        for (int i = 0; i < this.elements.length; i++) {
            if (elements[i] != null) {
                code += this.elements[i].hashCode();
            }
        }

        return code;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return ArrayUtils.toString(this.elements);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasAll() {
        if (this.elements != null) {
            for (Object obj : this.elements) {
                if (obj == ANY) {
                    return true;
                }
            }
        }

        return false;
    }
}
