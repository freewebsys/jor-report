package jatools.math;

import jatools.accessor.ProtectPublic;
import jatools.data.sum.SumField;

import java.math.BigDecimal;
import java.math.BigInteger;

import bsh.Primitive;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class Math implements ProtectPublic {
    private static final Double DEFAULTIFNULL = new Double(0);

    /**
     * DOCUMENT ME!
     *
     * @param values DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object max(Object[] values) {
        Object val = null;

        if (values != null) {
            if (values.length == 1) {
                val = values[0];
            } else {
                Comparable comparableMax = (Comparable) values[0];

                for (int i = 1; i < values.length; i++) {
                    Comparable comparableCurrent = (Comparable) values[i];

                    if (comparableCurrent.compareTo(comparableMax) > 0) {
                        comparableMax = comparableCurrent;
                    }
                }

                val = comparableMax;
            }
        }

        return val;
    }

    /**
     * DOCUMENT ME!
     *
     * @param value1 DOCUMENT ME!
     * @param value2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static Number divide(Number value1, Number value2) {
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
    private static Number convertToNumber(Class cls, int value) {
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
     * @param values DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object sum(Object[] values) {
        Object val = null;

        if ((values != null) && (values.length > 0)) {
            if (values.length == 1) {
                val = values[0];
            } else {
                Number value = (Number) values[0];

                for (int i = 1; i < values.length; i++) {
                    value = sum(value, (Number) values[i]);
                }

                val = value;
            }
        }

        if (val == null) {
            val = DEFAULTIFNULL;
        }

        return val;
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
    private static Number sum(Number value1, Number value2) {
        if ((value1 == null) && (value2 == null)) {
            return DEFAULTIFNULL;
        }

        if ((value1 == null) && (value2 != null)) {
            return value2;
        }

        if ((value2 == null) && (value1 != null)) {
            return value1;
        }

        Object[] vals = Primitive.promotePrimitives(value1, value2);
        value1 = (Number) vals[0];
        value2 = (Number) vals[1];

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

        return DEFAULTIFNULL;
    }

    /**
     * DOCUMENT ME!
     *
     * @param values DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object min(Object[] values) {
        Object val = null;

        if (values != null) {
            if (values.length == 1) {
                val = values[0];
            } else {
                Comparable comparableMin = (Comparable) values[0];

                for (int i = 1; i < values.length; i++) {
                    Comparable comparableCurrent = (Comparable) values[i];

                    if (comparableCurrent.compareTo(comparableMin) < 0) {
                        comparableMin = comparableCurrent;
                    }
                }

                val = comparableMin;
            }
        }

        return val;
    }

    /**
     * DOCUMENT ME!
     *
     * @param values DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object avg(Object[] values) {
        Object val = null;

        if (values != null) {
            if (values.length == 1) {
                val = values[0];
            } else {
                val = (Object) divide((Number) SumField.calculate(values),
                        convertToNumber(values[0].getClass(), values.length));
            }
        }

        return val;
    }

    /**
     * DOCUMENT ME!
     *
     * @param values DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object count(Object[] values) {
        if (values != null) {
            return convertToNumber(Integer.class, values.length);
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param values DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object count2(Object[] values) {
        if (values != null) {
            int c = 0;

            for (int i = 0; i < values.length; i++) {
                if (values[i] != null) {
                    c++;
                }
            }

            return new Integer(c);
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param objects DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object topOccurs(Object[] values) {
        final Object NULL = new Object();

        if ((values != null) && (values.length > 0)) {
            Object[] tmp = new Object[values.length];
            System.arraycopy(values, 0, tmp, 0, values.length);

            int maxoccurs = 0;
            Object result = null;

            for (int i = 0; i < values.length; i++) {
                if (tmp[i] == NULL) {
                    continue;
                }

                int occurs = 1;
                Object first = tmp[i];

                for (int j = i + 1; j < values.length; j++) {
                    if ((tmp[j] != NULL) && equals(tmp[j], first)) {
                        occurs++;
                        tmp[j] = NULL;
                    }
                }

                if (occurs > maxoccurs) {
                    maxoccurs = occurs;
                    result = first;
                }
            }

            return result;
        }

        return null;
    }
    
    public static void main(String[] args) {
		System.out.println(Double.class.isPrimitive());
	}

    static boolean equals(Object o1, Object o2) {
        return (o1 == null) ? (o2 == null) : o1.equals(o2);
    }
}
