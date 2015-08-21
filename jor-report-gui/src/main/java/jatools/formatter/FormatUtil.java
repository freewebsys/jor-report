package jatools.formatter;

import java.util.HashMap;
import java.util.Map;


public class FormatUtil {
    static Map cachedFormat;

    public static Format2 getInstance(String pattern) {
        if (pattern == null) {
            return null;
        }

        Format2 result = null;

        if (cachedFormat != null) {
            result = (Format2) cachedFormat.get(pattern);

            if (result != null) {
                return result;
            }
        }

        if (pattern.matches(".*?(yyyy|yy|m|M|d|s|h).*")) {
            result = new DateFormat(pattern);
        } else {
            result = new DecimalFormat(pattern);
        }

        if (cachedFormat == null) {
            cachedFormat = new HashMap();
        }

        cachedFormat.put(pattern, result);

        return result;
    }
}
