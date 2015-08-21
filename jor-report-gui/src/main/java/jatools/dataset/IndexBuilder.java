package jatools.dataset;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
  */
public class IndexBuilder {
    Row[] rows;
    int[] ids;
    Object[] groupLevels;
    Map index;
    RowSet src;

    /**
     * Creates a new IndexBuilder object.
     *
     * @param src DOCUMENT ME!
     * @param fields DOCUMENT ME!
     */
    public IndexBuilder(RowSet src, int[] fields) {
        this.rows = sort(src.toArray(), fields);

        this.ids = getIds(rows);
        this.groupLevels = getLevels(fields);
        this.src = src;
    }

    private Row[] sort(Row[] rows, int[] cols) {
        RowComparator c = new RowComparator(cols, null);
        Arrays.sort(rows, c);

        return rows;
    }

    private int[] getIds(Row[] rows2) {
        int[] result = new int[rows.length];

        for (int i = 0; i < rows2.length; i++) {
            result[i] = rows2[i].index;
        }

        return result;
    }

    private Object[] getLevels(int[] fields) {
        Object[] result = new Object[fields.length];

        
        for (int i = 0; i < fields.length; i++) {
            int[] f = new int[i + 1];
            System.arraycopy(fields, 0, f, 0, i + 1);
            result[i] = f;
        }

        return result;
    }

    /**
    * DOCUMENT ME!
    *
    * @param dataset DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public Map build() {
        this.index = new HashMap();
        build(0, 0, this.rows.length - 1);
        this.index.put(Dataset.ANY, getTotalIndex());

        return this.index;
    }

    protected Object getTotalIndex() {
        return this.src;
    }

    /**
     * DOCUMENT ME!
     *
     * @param dataset DOCUMENT ME!
     * @param groupLevels DOCUMENT ME!
     * @param from DOCUMENT ME!
     * @param to DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private void build(int level, int from, int to) {
        int[] fields = (int[]) this.groupLevels[level];

        while (from <= to) {
            Row thisRow = rows[from];

            int row = from;

            while (row <= to) {
                Row next = rows[row];

                if (!thisRow.equals(next, fields)) {
                    break;
                }

                row++;
            }

            int _from = from;
            int _to = row - 1;

            
            Object[] vals = thisRow.values(fields);
            Key key = new Key(vals);

            RowSet item = getIndex(key, _from, _to);

            
            this.index.put(key, item);

            
            if (level < (this.groupLevels.length - 1)) {
                build(level + 1, _from, _to);
            }

            from = row;
        }
    }

    protected RowSet getIndex(Key key, int _from, int _to) {
        int[] rows = new int[_to - _from + 1];
        System.arraycopy(ids, _from, rows, 0, rows.length);
        return new RandomRowSet(src.getDataset(), key, rows);
    }
}
