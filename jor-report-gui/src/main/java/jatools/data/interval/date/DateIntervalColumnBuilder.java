package jatools.data.interval.date;

import jatools.data.reader.udc.UserColumnBuilder;
import jatools.dataset.Dataset;
import jatools.dataset.Row;
import jatools.dataset.RowFieldComparator;
import jatools.dataset.UserColumn;
import jatools.engine.script.Script;

import java.util.Arrays;
import java.util.Date;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class DateIntervalColumnBuilder implements UserColumnBuilder {
    /**
     * DOCUMENT ME!
     *
     * @param rows DOCUMENT ME!
     * @param srcField DOCUMENT ME!
     * @param type DOCUMENT ME!
     */
    public void build(Dataset data, Object src,Script script) {
        DateIntervalColumn rangeCol = (DateIntervalColumn) src;
        data.getRowInfo().addColumn(new UserColumn(rangeCol.getName()));

        Row[] rows = data.getRowsArray();
        int col = data.getColumnIndex(rangeCol.getSrcField());
        DateIntervalFactory factory = DateIntervalFactoryCache.getInstance(rangeCol.getType());

        Arrays.sort(rows, new RowFieldComparator(col));

        for (int i = 0; i < rows.length; i++) {
            Date date = (Date) rows[i].getValueAt(col);

            if (date != null) {
                DateInterval dr = factory.create(date);
                rows[i].addColumn(dr);

                for (int j = i + 1; j < rows.length; j++) {
                    date = (Date) rows[j].getValueAt(col);

                    if (!dr.includes(date)) {
                        dr = factory.create(date);
                    }

                    rows[j].addColumn(dr);
                }

                break;
            } else {
                rows[i].addColumn(null);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
   
}
