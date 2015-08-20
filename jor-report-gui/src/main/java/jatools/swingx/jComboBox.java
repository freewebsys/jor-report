package jatools.swingx;

import javax.swing.JComboBox;

import org.apache.commons.lang.ArrayUtils;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class jComboBox extends JComboBox {
    private Object[] keys;

    /**
    * Creates a new jComboBox object.
    *
    * @param key DOCUMENT ME!
    * @param prompts DOCUMENT ME!
    */
    public jComboBox(Object[] keys, String[] names) {
        super(asItems(keys, names));
        this.keys = keys;
    }

    private static Item[] asItems(Object[] keys, String[] names) {
        if (keys != null) {
            Item[] items = new Item[keys.length];

            for (int i = 0; i < keys.length; i++) {
                items[i] = new Item(keys[i], names[i]);
            }

            return items;
        } else {
            return new Item[0];
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getKey() {
        int i = this.getSelectedIndex();

        if (i > -1) {
            Item item = (Item) this.getSelectedItem();

            return item.key;
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param anObject DOCUMENT ME!
     */
    public void setKey(Object key) {
        int i = ArrayUtils.indexOf(this.keys, key);

        if (i > -1) {
            setSelectedIndex(i);
        }
    }
}


class Item {
    Object key;
    String name;

    /**
     * Creates a new Item object.
     *
     * @param key DOCUMENT ME!
     * @param name DOCUMENT ME!
     */
    public Item(Object key, String name) {
        this.key = key;
        this.name = name;
    }

    /**
     * Creates a new Item object.
     *
     * @param key DOCUMENT ME!
     */
    public Item(Object key) {
        this.key = key;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        if (obj instanceof Item) {
            return this.key.equals(((Item) obj).key);
        } else {
            return this.key.equals(obj);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return (name != null) ? name.toString() : null;
    }
}
