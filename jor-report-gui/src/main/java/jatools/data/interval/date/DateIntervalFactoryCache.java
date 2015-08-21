package jatools.data.interval.date;

import java.util.Date;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class DateIntervalFactoryCache {
    static DateIntervalFactory[] caches;

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static DateIntervalFactory getInstance(int type) {
        if (caches == null) {
            createCaches();
        }

        return caches[type];
    }

    private static void createCaches() {
        caches = new DateIntervalFactory[10];

        caches[DateInterval.EVERY_DAY] = new DateIntervalFactory() {
                    public DateInterval create(Date src) {
                        return DateIntervalFactoryUtil.createDayRange(src);
                    }
                };
        caches[DateInterval.EVERY_HALF_DAY] = new DateIntervalFactory() {
                    public DateInterval create(Date src) {
                        return DateIntervalFactoryUtil.createHalfDayRange(src);
                    }
                };

        caches[DateInterval.EVERY_WEEK] = new DateIntervalFactory() {
                    public DateInterval create(Date src) {
                        return DateIntervalFactoryUtil.createWeekRange(src);
                    }
                };

        caches[DateInterval.EVERY_MONTH] = new DateIntervalFactory() {
                    public DateInterval create(Date src) {
                        return DateIntervalFactoryUtil.createMonthRange(src);
                    }
                };

        caches[DateInterval.EVERY_QUARTER] = new DateIntervalFactory() {
                    public DateInterval create(Date src) {
                        return DateIntervalFactoryUtil.createQuarterRange(src);
                    }
                };

        caches[DateInterval.EVERY_HALF_YEAR] = new DateIntervalFactory() {
                    public DateInterval create(Date src) {
                        return DateIntervalFactoryUtil.createHalfYearRange(src);
                    }
                };
        caches[DateInterval.EVERY_YEAR] = new DateIntervalFactory() {
                    public DateInterval create(Date src) {
                        return DateIntervalFactoryUtil.createYearRange(src);
                    }
                };

        caches[DateInterval.EVERY_SECOND] = new DateIntervalFactory() {
                    public DateInterval create(Date src) {
                        return DateIntervalFactoryUtil.createSecondRange(src);
                    }
                };

        caches[DateInterval.EVERY_MINITUES] = new DateIntervalFactory() {
                    public DateInterval create(Date src) {
                        return DateIntervalFactoryUtil.createMinituesRange(src);
                    }
                };

        caches[DateInterval.EVERY_HOUR] = new DateIntervalFactory() {
                    public DateInterval create(Date src) {
                        return DateIntervalFactoryUtil.createHourRange(src);
                    }
                };
    }
}
