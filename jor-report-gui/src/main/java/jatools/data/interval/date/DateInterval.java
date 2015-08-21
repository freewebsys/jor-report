package jatools.data.interval.date;

import bsh.Interpreter;

import jatools.engine.ValueIfClosed;

import jatools.formatter.DateFormat;

import java.util.Date;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class DateInterval implements Comparable, ValueIfClosed {
    public static final int EVERY_YEAR = 0;
    public static final int EVERY_HALF_YEAR = 1;
    public static final int EVERY_QUARTER = 2;
    public static final int EVERY_MONTH = 3;
    public static final int EVERY_WEEK = 4;
    public static final int EVERY_DAY = 5;
    public static final int EVERY_HALF_DAY = 6;
    public static final int EVERY_HOUR = 7;
    public static final int EVERY_MINITUES = 8;
    public static final int EVERY_SECOND = 9;
    long start;
    long end;

    /**
     * Creates a new DateGroup object.
     *
     * @param start DOCUMENT ME!
     * @param end DOCUMENT ME!
     */
    public DateInterval(long start, long end) {
        this.start = start;
        this.end = end;
    }

    /**
     * DOCUMENT ME!
     *
     * @param d DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean includes(Date d) {
        return (d.getTime() >= start) && (d.getTime() < end);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return new DateFormat("yyyy/MM/dd").format(new Date(start));
    }

    /**
     * DOCUMENT ME!
     *
     * @param o DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compareTo(Object o) {
        DateInterval d = (DateInterval) o;

        if (this.start == d.start) {
            return 0;
        } else if (this.start > d.start) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object value() {
        return new Date(start);
    }

}
