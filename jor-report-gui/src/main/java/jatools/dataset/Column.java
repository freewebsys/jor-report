package jatools.dataset;

import jatools.accessor.ProtectPublic;

import java.util.HashMap;
import java.util.Map;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class Column implements ProtectPublic{
    private String name;
    private Class cls;
    private Map props;

    /**
     * Creates a new Column object.
     *
     * @param name DOCUMENT ME!
     * @param className DOCUMENT ME!
     *
     * @throws ClassNotFoundException DOCUMENT ME!
     */
    public Column(String name, String className) throws ClassNotFoundException {
        setName(name);
        setClassName(className);
    }

    /**
     * Creates a new Column object.
     *
     * @param name DOCUMENT ME!
     * @param cls DOCUMENT ME!
     */
    public Column(String name, Class cls) {
        setName(name);
        setClass(cls);
    }

    /**
     * Creates a new Column object.
     *
     * @param name DOCUMENT ME!
     */
    public Column(String name) {
        setName(name);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getClassName() {
        return cls.getName();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Class getColumnClass() {
        return cls;
    }

    /**
     * DOCUMENT ME!
     *
     * @param cls DOCUMENT ME!
     */
    protected void setClass(Class cls) {
        //        if (cls == null) {
        //            throw new IllegalArgumentException(Messages.getString(
        //                    "ZColumnInfo.0")); //
        //        }
        this.cls = cls;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNumber() {
        if (this.cls != null) {
            return Number.class.isAssignableFrom(cls);
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param className DOCUMENT ME!
     *
     * @throws ClassNotFoundException DOCUMENT ME!
     */
    protected void setClassName(String className) throws ClassNotFoundException {
        setClass(Class.forName(className));
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void setName(String name) {
        if (name == null) {
            name = ""; //
        }

        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param fieldInfo DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Column fieldInfo) {
        return cls.getName().equals(fieldInfo.getColumnClass().getName());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        return new Column(name, cls);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return getName();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString1() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ZFieldInfo:"); //
        buffer.append(" name: "); //
        buffer.append(name);
        buffer.append(" cls: "); //
        buffer.append(cls);
        buffer.append("]"); //

        return buffer.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param string DOCUMENT ME!
     * @param object DOCUMENT ME!
     */
    public void set(String string, Object object) {
        if (props == null) {
            props = new HashMap();
        }

        props.put(string, object);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object get(String key) {
        return (props == null) ? null : props.get(key);
    }
}
