package jatools.dataset;

import java.util.Comparator;


public class RowComparator implements Comparator {
    private int[] sortOrder;
    private boolean[] directions;

    /**
     * Creates a new RowComparator object.
     *
     * @param sortOrder DOCUMENT ME!
     */
    public RowComparator(int[] sortOrder) {
        this.sortOrder = sortOrder;
    }

    /**
     * Creates a new RowComparator object.
     *
     * @param sortOrder DOCUMENT ME!
     * @param directions DOCUMENT ME!
     */
    public RowComparator(int[] sortOrder, boolean[] directions) {
        this.sortOrder = sortOrder;
        this.directions = directions;
    }

    /**
     * Creates a new RowComparator object.
     */
    public RowComparator() {
        sortOrder = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param row1 DOCUMENT ME!
     * @param row2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final int compare(Object row1, Object row2) {
        return compare((Row) row1, (Row) row2);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row1 DOCUMENT ME!
     * @param row2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compare(Row row1, Row row2) {
        if (sortOrder == null) {
            int columnCount = row1.getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                int value = RowsService.compareComparables((Comparable) row1.getValueAt(i),
                        (Comparable) row2.getValueAt(i));

                if (value > 0) {
                    return value;
                } else if (value < 0) {
                    return value;
                }
            }
        } else {
            int columnCount = row1.getColumnCount();

            for (int i = 0; i < sortOrder.length; i++) {
                
                if ((sortOrder[i] >= 0) && (sortOrder[i] <= (columnCount - 1))) {
                    int value = RowsService.compareComparables((Comparable) row1.getValueAt(
                                sortOrder[i]), 
                                (Comparable) row2.getValueAt(sortOrder[i]));

                    if (value != 0) {
                        return ((directions == null) || directions[i]) ? value : (-value);
                    }
                }
            }
        }

        return 0;
    }
}
