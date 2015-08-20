package jatools.designer.wizard;

import jatools.data.sum.AvgField;
import jatools.data.sum.CountField;
import jatools.data.sum.DistinctCountField;
import jatools.data.sum.MaxField;
import jatools.data.sum.MinField;
import jatools.data.sum.Sum;
import jatools.data.sum.SumField;
import jatools.designer.App;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class CustomSummary {
    public static final String COUNT = App.messages.getString("res.211");
    public static final String SUM = App.messages.getString("res.212");
    public static final String MAX = App.messages.getString("res.213");
    public static final String MIN = App.messages.getString("res.214");
    public static final String AVERAGE = App.messages.getString("res.201");
    public static final String DISTINCT_COUNT = App.messages.getString("res.215");
    public static final String[] SUPPORT_FUNCTIONS = {
            COUNT, SUM, MAX, MIN, AVERAGE, DISTINCT_COUNT,
        };
    String groupField;
    String calcField;
    String calcType;
    private String readerVariable;
    private String filter;

    /**
     * Creates a new CustomSummary object.
     *
     * @param readerVariable DOCUMENT ME!
     * @param groupField DOCUMENT ME!
     * @param calcField DOCUMENT ME!
     * @param calcType DOCUMENT ME!
     * @param _filter DOCUMENT ME!
     */
    public CustomSummary(String readerVariable, String groupField, String calcField,
        String calcType, String _filter) {
        this.readerVariable = readerVariable;
        this.groupField = groupField;
        this.calcField = calcField;
        this.calcType = calcType;
        this.filter = _filter;
    }

    /**
     * DOCUMENT ME!
     *
     * @param sum DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Sum asGroupCalc(CustomSummary sum) {
        String ct = sum.getGroupField();
        String cf = sum.getCalcField();
        Sum calc = null;

        if (ct == SUM) {
            calc = new SumField(new String[] { ct }, cf);
        } else if (ct == COUNT) {
            calc = new CountField(new String[] { ct }, cf);
        } else if (ct == MAX) {
            calc = new MaxField(new String[] { ct }, cf);
        } else if (ct == MIN) {
            calc = new MinField(new String[] { ct }, cf);
            ;
        } else if (ct == AVERAGE) {
            calc = new AvgField(new String[] { ct }, cf);
            ;
        } else if (ct == DISTINCT_COUNT) {
            calc = new DistinctCountField(new String[] { ct }, cf);
        }

        return calc;
    }

    /**
     * DOCUMENT ME!
     *
     * @param cf DOCUMENT ME!
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Sum asGroupCalc(String cf, int i) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param calc DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String toCustomType(Sum calc) {
        String ct = SUM;

        if (calc instanceof AvgField) {
            ct = AVERAGE;
        } else if (calc instanceof CountField) {
            ct = COUNT;
        } else if (calc instanceof MaxField) {
            ct = MAX;
        } else if (calc instanceof MinField) {
            ct = MIN;
        } else if (calc instanceof DistinctCountField) {
            ct = DISTINCT_COUNT;
        }

        return ct;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ZCustomSummary:");

        buffer.append(" groupField: ");
        buffer.append(groupField);
        buffer.append(" calcField: ");
        buffer.append(calcField);
        buffer.append(" calcType: ");
        buffer.append(calcType);
        buffer.append("]");

        return buffer.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCalcField() {
        return calcField;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getCalcType() {
        return calcType;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getGroupField() {
        return groupField;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getReaderVariable() {
        return readerVariable;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getFilter() {
        return filter;
    }
}
