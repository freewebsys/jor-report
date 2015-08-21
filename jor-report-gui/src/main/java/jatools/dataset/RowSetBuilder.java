package jatools.dataset;

import java.util.ArrayList;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class RowSetBuilder {
    Dataset data;
    Row[] rows;
    int[] ids;
    int col;

    RowSetBuilder(Dataset data, Row[] rows, int col) {
        this.data = data;
        this.rows = rows;
        this.ids = getIds(rows);
        this.col = col;
    }

    private int[] getIds(Row[] rows2) {
        int[] result = new int[rows.length];

        for (int i = 0; i < rows2.length; i++) {
            result[i] = rows2[i].index;
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
    public RowSet[] build() {
        ArrayList result = new ArrayList();

        int from = 0;
        int to = this.rows.length - 1;

        int[] fields = new int[] {
                col
            };

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

            
            result.add(item);

            from = row;
        }

        return (RowSet[]) result.toArray(new RowSet[0]);
    }

    private RowSet getIndex(Key key, int _from, int _to) {
        int[] rows = new int[_to - _from + 1];
        System.arraycopy(ids, _from, rows, 0, rows.length);

        return new RandomRowSet(data, key, rows);
    }
}
