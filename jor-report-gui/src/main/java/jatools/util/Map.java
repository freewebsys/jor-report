package jatools.util;

import jatools.accessor.ProtectPublic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class Map implements ProtectPublic {
    protected java.util.Map me = new HashMap();
    protected ArrayList keyCache = new ArrayList();

    /**
     * DOCUMENT ME!
     */
    public void clear() {
        me.clear();
        keyCache.clear();
    }
    
    public int size()
    {
    	return keyCache.size() ;
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object get(Object key) {
        return me.get(key);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object remove(Object key) {
        Object r = me.remove(key);

        if (r != null) {
            keyCache.remove(key);
        }

        return r;
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object put(Object key, Object value) {
        Object r = me.put(key, value);

        if (!keyCache.contains(key)) {
            keyCache.add(key);
        }

        return r;
    }

    /**
     * DOCUMENT ME!
     *
     * @param oldKey DOCUMENT ME!
     * @param newKey DOCUMENT ME!
     * @param newVal DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean replace(String oldKey, String newKey, Object newVal) {
        if (keyCache.contains(oldKey)) {
            int i = keyCache.indexOf(oldKey);
            keyCache.set(i,newKey);
            me.remove(oldKey);
            me.put(newKey, newVal);

            return true;
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator names() {
        return keyCache.iterator();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator variables() {
        return me.values().iterator();
    }
}
