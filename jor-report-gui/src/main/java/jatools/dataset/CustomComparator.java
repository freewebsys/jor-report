package jatools.dataset;

import java.util.Comparator;


public class CustomComparator implements Comparator {
    private int[] columns;
    private Comparator[] comparators;

    /**
     * Creates a new CustomComparator object.
     *
     * @param columns DOCUMENT ME!
     * @param comparators DOCUMENT ME!
     */
    public CustomComparator(int[] columns, Comparator[] comparators) {
        this.columns = columns;
        this.comparators = comparators;
    }

    /**
     * DOCUMENT ME!
     *
     * @param row1
     *            DOCUMENT ME!
     * @param row2
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final int compare(Object row1, Object row2) {
        return compare((Row) row1, (Row) row2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row1
     *            DOCUMENT ME!
     * @param row2
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compare(Row row1, Row row2) {
        int columnCount = row1.getColumnCount();

        for (int i = 0; i < columns.length; i++) {
            
            if ((columns[i] >= 0) && (columns[i] <= (columnCount - 1))) {
                int value = this.comparators[i].compare(row1.getValueAt(columns[i]),
                        row2.getValueAt(columns[i]));

                if (value != 0) {
                    return value;
                }
            }
        }

        return 0;
    }
}
