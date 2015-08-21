package jatools.dataset;

import java.util.HashMap;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class DatasetFilter {
    int fieldIndex = -1;
    Map hashs = new HashMap();
    private String field;
    private boolean select;

    /**
     * Creates a new DatasetFilter object.
     *
     * @param dataset DOCUMENT ME!
     * @param field DOCUMENT ME!
     * @param strVals DOCUMENT ME!
     */
    public DatasetFilter(String field, String[] strVals, boolean select) {
        this.field = field;

        for (int i = 0; i < strVals.length; i++) {
            hashs.put(strVals[i], strVals[i]);
        }

        this.select = select;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ds DOCUMENT ME!
     */
    public void initDataset(Dataset ds) {
        this.fieldIndex = ds.getColumnIndex(this.field);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean accept(Row row) {
        Object val = row.getValueAt(fieldIndex);
        boolean has = ((val != null) && hashs.containsKey(val.toString()));

        if (has) {
            return select;
        } else {
            return true;
        }
    }
}
