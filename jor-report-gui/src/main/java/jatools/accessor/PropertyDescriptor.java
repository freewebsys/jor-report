package jatools.accessor;

/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PropertyDescriptor {
    public static int EDITABLE = 1;
    public static int RESOLVABLE = 2;
    public static int SERIALIZABLE = 4;
    public static int NODEFAULT = 16;
    public static int READONLY = 32;
    public static int DEFAULT = EDITABLE | SERIALIZABLE;
    private String propertyName;
    private Class propertyType;
    private int flag = DEFAULT;

    /**
     * Creates a new PropertyDescriptor object.
     *
     * @param propertyName DOCUMENT ME!
     * @param propertyType DOCUMENT ME!
     * @param flag DOCUMENT ME!
     */
    public PropertyDescriptor(String propertyName, Class propertyType, int flag) {
        this.propertyName = propertyName;
        this.propertyType = propertyType;
        this.flag = flag;
    }

    /**
     * Creates a new PropertyDescriptor object.
     *
     * @param propertyName DOCUMENT ME!
     * @param propertyType DOCUMENT ME!
     */
    public PropertyDescriptor(String propertyName, Class propertyType) {
        this(propertyName, propertyType, DEFAULT);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEditable() {
        return (flag & EDITABLE) != 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isReadonly() {
        return (flag & READONLY) != 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSerializable() {
        return (flag & SERIALIZABLE) != 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isResolvable() {
        return (flag & RESOLVABLE) != 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNodefault() {
        return (flag & NODEFAULT) != 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param that DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(PropertyDescriptor that) {
        if (!(that instanceof PropertyDescriptor)) {
            return false;
        }

        if (!((propertyName == null) ? (that.propertyName == null)
                                         : propertyName.equals(that.propertyName))) {
            return false;
        }

        if (!((propertyType == null) ? (that.propertyType == null)
                                         : propertyType.equals(that.propertyType))) {
            return false;
        }

        return flag == that.flag;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Class getPropertyType() {
        return propertyType;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ZPropertyDescriptor:");

        buffer.append("\n propertyName: ");
        buffer.append(propertyName);
        buffer.append("\n propertyType: ");
        buffer.append(propertyType.getName());

        buffer.append("\neditable: ");
        buffer.append((flag & EDITABLE) != 0);

        buffer.append("\nresolvable: ");
        buffer.append((flag & RESOLVABLE) != 0);

        buffer.append("\nseriable: ");
        buffer.append((flag & SERIALIZABLE) != 0);

        buffer.append("]");

        return buffer.toString();
    }
}
