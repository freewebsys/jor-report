package jatools.dom.sort;

import jatools.dataset.Dataset;
import jatools.dataset.RowsService;
import jatools.dom.ElementBase;
import jatools.dom.RowNode;
import jatools.dom.src.RowNodeSource;

import java.util.Comparator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class RowNodeComparator implements Comparator {
    final static String ORDERS = "ORDERS.";
    final static int UNKOWN = 0;
    final static int ASC = 1;
    final static int DESC = 2;
    int[] fields;
    boolean[] ascs;
    String command;

    RowNodeComparator(String command) {
        this.command = command.trim();
    }

    private void compile(Dataset dataset, ElementBase parent) {
        int[] orders = getOrders(dataset, parent);

        String[] items = this.command.split(",");
        this.fields = new int[items.length];
        this.ascs = new boolean[items.length];

        for (int i = 0; i < items.length; i++) {
            String[] item = items[i].trim().split(" ");
            this.fields[i] = dataset.getColumnIndex(item[0]);

            if ((item.length == 1) || item[1].equals("toggle")) {
                if (orders[this.fields[i]] == ASC) {
                    this.ascs[i] = false;
                } else {
                    this.ascs[i] = true;
                }
            } else {
                this.ascs[i] = item[1].equals("asc");
            }
        }

        for (int i = 0; i < fields.length; i++) {
            orders[fields[i]] = this.ascs[i] ? ASC : DESC;
        }

        this.command = null;
    }

    private int[] getOrders(Dataset dataset, ElementBase parent) {
        String key = ORDERS + RowNodeSource.TAG_NAME;
        int[] result = (int[]) parent.getClientProperty(key);

        if (result == null) {
            result = new int[dataset.getColumnCount()];
            parent.setClientProperty(key, result);
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param o1 DOCUMENT ME!
     * @param o2 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int compare(Object o1, Object o2) {
        RowNode row1 = (RowNode) o1;
        RowNode row2 = (RowNode) o2;

        if (this.command != null) {
            compile(row1.getDataset(), (ElementBase) row1.getParentNode());
        }

        for (int i = 0; i < fields.length; i++) {
            if (fields[i] >= 0) {
                int value = RowsService.compareComparables((Comparable) row1.getValueAt(fields[i]),
                        (Comparable) row2.getValueAt(fields[i]));

                if (value != 0) {
                    return ascs[i] ? value : (-value);
                }
            }
        }

        return 0;
    }
}
