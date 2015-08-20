package jatools.dataset;

import jatools.dom.Group;

import java.util.Arrays;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class GroupView {
    private RowSet[] rowsets;

    /**
     * Creates a new GroupView object.
     *
     * @param dataset DOCUMENT ME!
     * @param rowset DOCUMENT ME!
     * @param group DOCUMENT ME!
     */
    public GroupView(RowSet src, Group group) {
        this.rowsets = group(src, group);
    }

    /**
    * DOCUMENT ME!
    *
    * @param groups DOCUMENT ME!
    */
    public RowSet[] group(RowSet src, Group group) {
        Row[] rows = src.toArray();

        int[] cols = new int[1];
        cols[0] = src.getDataset().getColumnIndex(group.getField());

        if (group.getOrder() != Group.ORIGINAL) {
            boolean[] dir = new boolean[] {
                    group.getOrder() == Group.ASCEND
                };
            RowComparator c = new RowComparator(cols, dir);
            Arrays.sort(rows, c);
        }

        RowSetBuilder b = new RowSetBuilder(src.getDataset(), rows, cols[0]);

        return b.build();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RowSet[] getRowSets() {
        return rowsets;
    }
}
