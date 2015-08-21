package jatools.dataset;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;


public class RowsService {
	public final static Object MAX_VALUE = new Object();
	public final static Object MIN_VALUE = new Object();
    /**
     * DOCUMENT ME!
     *
     * @param value1 DOCUMENT ME!
     * @param value2 DOCUMENT ME!
     * 
     * @return DOCUMENT ME!
     */
    public static Number sum(Number value1,
                             Number value2) {
    	
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
        } else if (value1 instanceof BigInteger) {
            BigInteger big1 = (BigInteger) value1;
            BigInteger big2 = (BigInteger) value2;

            return big1.add(big2);
        } else if (value1 instanceof BigDecimal) {
            BigDecimal big1 = (BigDecimal) value1;
            BigDecimal big2 = (BigDecimal) value2;

            return big1.add(big2);
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param size DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static StringBuffer createSpaceFilledStringBuffer(int size) {
        StringBuffer stringBuffer = new StringBuffer(size);

        for (int i = 0; i < size; i++) {
            stringBuffer.append(" "); //
        }

        return stringBuffer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param stringBuffer DOCUMENT ME!
     * @param string DOCUMENT ME!
     * @param pos DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean setStringToStringBuffer(StringBuffer stringBuffer,
                                                  String string,
                                                  int pos) {
        int size = stringBuffer.length();

        if (string == null) {
            return false;
        }

        if (size <= pos) {
            return false;
        }

        int end = pos + string.length();

        if (end > size) {
            end = size;
        }

        for (int i = pos; i < end; i++) {
            stringBuffer.setCharAt(i, string.charAt(i - pos));
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param stringBuffer DOCUMENT ME!
     * @param string DOCUMENT ME!
     * @param start DOCUMENT ME!
     * @param end DOCUMENT ME!
     * @param leftToRight DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean setStringToStringBuffer(StringBuffer stringBuffer,
                                                  String string,
                                                  int start,
                                                  int end,
                                                  boolean leftToRight) {
        int size = stringBuffer.length();

        if (string == null) {
            return false;
        }

        if (size <= start) {
            return false;
        }

        if (end > size) {
            end = size;
        }

        if ((end - start) > string.length()) {
        }

        int stringLength = string.length();

        if (leftToRight) {
            for (int i = start; (i < end) && ((i - start) < stringLength); i++) {
                stringBuffer.setCharAt(i, string.charAt(i - start));
            }
        } else {
            for (int i = end; (i >= start) && (stringLength > 0); i--) {
                stringBuffer.setCharAt(i, string.charAt(--stringLength));
            }
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param object DOCUMENT ME!
     * @param className DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object StringToSpecifiedObject(String object,
                                                 String className) {
        if (object == null) {
            return null;
        }

        if (className.equals("java.lang.Boolean")) { //
            return new Boolean(Boolean.getBoolean((String) object));
        }

        if (className.equals("java.lang.Byte")) { //
            return new Byte(Byte.parseByte((String) object));
        }

        if (className.equals("java.math.BigInteger")) { //
            return new BigInteger((String) object);
        }

        if (className.equals("java.math.BigDecimal")) { //
            return new BigDecimal((String) object);
        }

        if (className.equals("java.sql.Date") || className.equals("java.util.Date")) { // //$NON-NLS-2$
            return java.sql.Date.valueOf((String) object);
        }

        if (className.equals("java.sql.Time")) { //
            return Time.valueOf((String) object);
        }

        if (className.equals("java.sql.Timestamp")) { //
            return Timestamp.valueOf((String) object);
        }

        if (className.equals("java.lang.Double")) { //
            return new Double(Double.parseDouble((String) object));
        }

        if (className.equals("java.lang.Float")) { //
            return new Float(Float.parseFloat((String) object));
        }

        if (className.equals("java.lang.Integer")) { //
            return new Integer(Integer.parseInt((String) object));
        }

        if (className.equals("java.lang.String")) { //
            return object.toString();
        }

        throw new IllegalArgumentException(object.getClass().getName() + " is not supported class type"); //
    }

    /**
     * DOCUMENT ME!
     *
     * @param time DOCUMENT ME!
     * @param format DOCUMENT ME!
     * @param nullValue DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String timestampToString(Timestamp time,
                                           String format,
                                           String nullValue) {
        if (time == null) {
            return nullValue;
        }

        if (format == null) {
            return time.toString();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        return simpleDateFormat.format(time);
    }

    /**
     * DOCUMENT ME!
     *
     * @param date DOCUMENT ME!
     * @param format DOCUMENT ME!
     * @param nullValue DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String dateToString(java.util.Date date,
                                      String format,
                                      String nullValue) {
        if (date == null) {
            return nullValue;
        }

        if (format == null) {
            return date.toString();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        return simpleDateFormat.format(date);
    }

    /**
     * DOCUMENT ME!
     *
     * @param time DOCUMENT ME!
     * @param format DOCUMENT ME!
     * @param nullValue DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String timeToString(Time time,
                                      String format,
                                      String nullValue) {
        if (time == null) {
            return nullValue;
        }

        if (format == null) {
            return time.toString();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);

        return simpleDateFormat.format(time);
    }

    /**
     * DOCUMENT ME!
     *
     * @param compareable1 DOCUMENT ME!
     * @param compareable2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int compareComparables(Comparable compareable1,
                                         Comparable compareable2) {
  
        if ((compareable1 == null) && (compareable2 == null)) {
            return 0;
        } else if ((compareable1 == null) && (compareable2 != null)) {
            return -1;
        } else if ((compareable1 != null) && (compareable2 == null)) {
            return 1;
        } else {
            return compareable1.compareTo(compareable2);
        }
    }
    
    

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     * @param format DOCUMENT ME!
     * @param defaultTime DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static java.sql.Timestamp stringToTimestamp(String value,
                                                       String format,
                                                       Timestamp defaultTime) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            java.util.Date date = simpleDateFormat.parse(value);

            return new java.sql.Timestamp(date.getTime());
        } catch (ParseException parseException) {
            return defaultTime;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     * @param format DOCUMENT ME!
     * @param defaultDate DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static java.sql.Date stringToDate(String value,
                                             String format,
                                             java.sql.Date defaultDate) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            java.util.Date date = simpleDateFormat.parse(value);

            return new java.sql.Date(date.getTime());
        } catch (ParseException parseException) {
            return defaultDate;
        }
    }
}
