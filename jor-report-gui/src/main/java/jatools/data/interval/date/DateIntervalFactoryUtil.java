package jatools.data.interval.date;

import java.util.Calendar;
import java.util.Date;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class DateIntervalFactoryUtil {
    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static DateInterval createDayRange(Date src) {
        Calendar c = Calendar.getInstance();
        c.setTime(src);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        long start = c.getTimeInMillis();
        c.add(Calendar.DAY_OF_YEAR, 1);

        long end = c.getTimeInMillis();

        return new DateInterval(start, end);
    }

    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static DateInterval createHalfDayRange(Date src) {
        Calendar c = Calendar.getInstance();
        c.setTime(src);

        if (c.get(Calendar.AM_PM) == Calendar.AM) {
            c.set(Calendar.HOUR_OF_DAY, 0);
        } else {
            c.set(Calendar.HOUR_OF_DAY, 12);
        }

        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        long start = c.getTimeInMillis();
        c.add(Calendar.HOUR, 12);

        long end = c.getTimeInMillis();

        return new DateInterval(start, end);
    }

    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static DateInterval createWeekRange(Date src) {
        Calendar c = Calendar.getInstance();
        c.setTime(src);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        while (c.get(Calendar.DAY_OF_WEEK) != c.getFirstDayOfWeek()) {
            c.add(Calendar.DAY_OF_WEEK, -1);
        }

        long start = c.getTimeInMillis();
        c.add(Calendar.DAY_OF_YEAR, 7);

        long end = c.getTimeInMillis();

        return new DateInterval(start, end);
    }

    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static DateInterval createMonthRange(Date src) {
        Calendar c = Calendar.getInstance();
        c.setTime(src);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);

        long start = c.getTimeInMillis();
        c.add(Calendar.MONTH, 1);

        long end = c.getTimeInMillis();

        return new DateInterval(start, end);
    }

    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static DateInterval createQuarterRange(Date src) {
        Calendar c = Calendar.getInstance();
        c.setTime(src);

        int m = ((c.get(Calendar.MONTH) / 3) * 3) ;
        c.set(Calendar.MONTH, m);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        long start = c.getTimeInMillis();
        c.add(Calendar.MONTH, 3);

        long end = c.getTimeInMillis();

        return new DateInterval(start, end);
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        System.out.println(1 % 3);
    }

    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static DateInterval createHalfYearRange(Date src) {
        Calendar c = Calendar.getInstance();
        c.setTime(src);

        int m = c.get(Calendar.MONTH);

        if (m < 6) {
            c.set(Calendar.MONTH, 0);
        } else {
            c.set(Calendar.MONTH, 6);
        }

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);

        long start = c.getTimeInMillis();
        c.add(Calendar.MONTH, 6);

        long end = c.getTimeInMillis();

        return new DateInterval(start, end);
    }

    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static DateInterval createYearRange(Date src) {
        Calendar c = Calendar.getInstance();
        c.setTime(src);

        c.set(Calendar.HOUR_OF_DAY, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.MONTH, 0);

        long start = c.getTimeInMillis();
        c.add(Calendar.YEAR, 1);

        long end = c.getTimeInMillis();

        return new DateInterval(start, end);
    }

    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static DateInterval createSecondRange(Date src) {
        Calendar c = Calendar.getInstance();
        c.setTime(src);
        c.set(Calendar.MILLISECOND, 0);

        long start = c.getTimeInMillis();
        c.add(Calendar.SECOND, 1);

        long end = c.getTimeInMillis();

        return new DateInterval(start, end);
    }

    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static DateInterval createMinituesRange(Date src) {
        Calendar c = Calendar.getInstance();
        c.setTime(src);

        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        long start = c.getTimeInMillis();
        c.add(Calendar.MINUTE, 1);

        long end = c.getTimeInMillis();

        return new DateInterval(start, end);
    }

    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static DateInterval createHourRange(Date src) {
        Calendar c = Calendar.getInstance();
        c.setTime(src);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        long start = c.getTimeInMillis();
        c.add(Calendar.HOUR, 1);

        long end = c.getTimeInMillis();

        return new DateInterval(start, end);
    }
}
