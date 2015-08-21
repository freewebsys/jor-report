package jatools.data.sum;

import jatools.accessor.PropertyAccessor;
import jatools.accessor.PropertyDescriptor;
import jatools.component.ComponentConstants;

import java.math.BigDecimal;
import java.math.BigInteger;



/**
 * @author   java9
 */
public abstract class Sum implements PropertyAccessor, Cloneable {
    private String calcField;
    private String[] groupFields;
    private String name;

    /**
     * Creates a new ZGroupCalc object.
     *
     * @param groupField DOCUMENT ME!
     * @param calcField DOCUMENT ME!
     */
    public Sum(String[] groupFields, String calcField) {
        this.groupFields = groupFields;
        this.calcField = calcField;
    }

    /**
     * Creates a new ZGroupCalc object.
     */
    public Sum() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException ex) {
            return null;
        }
    }

    /*
    * (non-Javadoc)
    *
    * @see com.jatools.core.accessor.ZPropertyAccessor#getRegistrableProperties()
    */

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] {
            ComponentConstants._NAME,
            ComponentConstants._READER_VARIABLE,
            new PropertyDescriptor("CalcField", String.class, //
                PropertyDescriptor.SERIALIZABLE),
            new PropertyDescriptor("GroupField", String.class, //
                PropertyDescriptor.SERIALIZABLE),
            new PropertyDescriptor("Filter", String.class, //
                PropertyDescriptor.SERIALIZABLE)
        };
    }

    /**
     * DOCUMENT ME!
     *
     * @param values
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract Object getValue(Object[] values);

    /**
     * DOCUMENT ME!
     *
     * @param value1
     *            DOCUMENT ME!
     * @param value2
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Number divide(Number value1, Number value2) {
        if ((value1 == null) && (value2 != null)) {
            return value2;
        }

        if ((value2 == null) && (value1 != null)) {
            return value1;
        }

        if (value1 instanceof Integer) {
            return new Integer(((Integer) value1).intValue() / ((Integer) value2).intValue());
        } else if (value1 instanceof Long) {
            return new Long(((Long) value1).longValue() / ((Long) value2).longValue());
        } else if (value1 instanceof Double) {
            return new Double(((Double) value1).doubleValue() / ((Double) value2).doubleValue());
        } else if (value1 instanceof Float) {
            return new Float(((Float) value1).floatValue() / ((Float) value2).floatValue());
        } else if (value1 instanceof Short) {
            short shortValue1 = ((Short) value1).shortValue();
            short shortValue2 = ((Short) value1).shortValue();
            short shortValue = (short) (shortValue1 + shortValue2);

            return new Short(shortValue);
        } else if (value1 instanceof Byte) {
            byte byteValue1 = ((Byte) value1).byteValue();
            byte shortValue2 = ((Byte) value1).byteValue();
            byte shortValue = (byte) (byteValue1 + shortValue2);

            return new Byte(shortValue);
        } else if (value1 instanceof BigInteger) {
            BigInteger bigValue1 = (BigInteger) value1;
            BigInteger bigValue2 = (BigInteger) value2;

            return bigValue1.divide(bigValue2);
        } else if (value1 instanceof BigDecimal) {
            BigDecimal bigDecimal1 = (BigDecimal) value1;
            BigDecimal bigDecimal2 = (BigDecimal) value2;

            return bigDecimal1.divide(bigDecimal2, BigDecimal.ROUND_CEILING);
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param cls
     *            DOCUMENT ME!
     * @param value
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Number convertToNumber(Class cls, int value) {
        if (cls.equals(Integer.class)) {
            return new Integer(value);
        } else if (cls.equals(Long.class)) {
            return new Long(value);
        } else if (cls.equals(Double.class)) {
            return new Double(value);
        } else if (cls.equals(Float.class)) {
            return new Float(value);
        } else if (cls.equals(Short.class)) {
            short shortValue = (new Integer(value)).shortValue();

            return new Short(shortValue);
        } else if (cls.equals(Byte.class)) {
            byte byteValue = (new Integer(value)).byteValue();

            return new Byte(byteValue);
        } else if (cls.equals(BigInteger.class)) {
            return new BigInteger(String.valueOf(value));
        } else if (cls.equals(BigDecimal.class)) {
            return new BigDecimal(value);
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param value1
     *            DOCUMENT ME!
     * @param value2
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Number sum(Number value1, Number value2) {
        if ((value1 == null) && (value2 != null)) {
            return value2;
        }

        if ((value2 == null) && (value1 != null)) {
            return value1;
        }

        if (value1 instanceof Integer) {
            return new Integer(((Integer) value1).intValue() + ((Integer) value2).intValue());
        } else if (value1 instanceof Long) {
            return new Long(((Long) value1).longValue() + ((Long) value2).longValue());
        } else if (value1 instanceof Double) {
            return new Double(((Double) value1).doubleValue() + ((Double) value2).doubleValue());
        } else if (value1 instanceof Float) {
            return new Float(((Float) value1).floatValue() + ((Float) value2).floatValue());
        } else if (value1 instanceof Short) {
            short shortValue1 = ((Short) value1).shortValue();
            short shortValue2 = ((Short) value1).shortValue();
            short shortValue = (short) (shortValue1 + shortValue2);

            return new Short(shortValue);
        } else if (value1 instanceof Short) {
            byte byteValue1 = ((Byte) value1).byteValue();
            byte byteValue2 = ((Byte) value1).byteValue();
            byte byteValue = (byte) (byteValue1 + byteValue2);

            return new Byte(byteValue);
        } else if (value1 instanceof BigInteger) {
            BigInteger bigValue1 = (BigInteger) value1;
            BigInteger bigValue2 = (BigInteger) value2;

            return bigValue1.add(bigValue2);
        } else if (value1 instanceof BigDecimal) {
            BigDecimal bigDecimal1 = (BigDecimal) value1;
            BigDecimal bigDecimal2 = (BigDecimal) value2;

            return bigDecimal1.add(bigDecimal2);
        }

        return null;
    }

    /**
         * @return   Returns the calcField.
         * @uml.property   name="calcField"
         */
    public String getCalcField() {
        return calcField;
    }

    /**
         * @return   Returns the groupField.
         * @uml.property   name="groupFields"
         */
    public String[] getGroupFields() {
        return groupFields;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[" + getClass().getName() + ":"); // //$NON-NLS-2$
        buffer.append(" calcField: "); //
        buffer.append(calcField);
        buffer.append(" groupField: "); //
        buffer.append(groupFields);
        buffer.append("]"); //

        return buffer.toString();
    }

    /**
         * @param calcField   The calcField to set.
         * @uml.property   name="calcField"
         */
    public void setCalcField(String calcField) {
        this.calcField = calcField;
    }

    /**
         * @return   Returns the name.
         * @uml.property   name="name"
         */
    public String getName() {
        return (name == null) ? "s0" : name;
    }

    /**
         * @param name   The name to set.
         * @uml.property   name="name"
         */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String[] getVariables() {
        // TODO Auto-generated method stub
        return null;
    }
}
