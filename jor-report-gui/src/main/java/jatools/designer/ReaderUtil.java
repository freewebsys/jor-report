package jatools.designer;


import jatools.data.reader.DatasetReader;
import jatools.dataset.Column;
import jatools.dataset.Dataset;
import jatools.dataset.DatasetException;
import jatools.engine.script.ReportContext;



public class ReaderUtil {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     * @throws DatasetException
     */
    public static Column[] getColumns(DatasetReader reader) {
        try {
            Dataset ds = reader.read(  ReportContext.getDefaultContext(), 0);
            int columnCount = ds.getColumnCount();
            Column[] columns = new Column[columnCount];

            for (int i = 0; i < columnCount; i++) {
                columns[i] = ds.getColumn(i);
            }

            return columns;
        } catch (DatasetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            return new Column[0];
        }
    }
}
