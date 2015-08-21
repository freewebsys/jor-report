package jatools.dataset;

import bsh.NameSpace;
import bsh.UtilEvalError;

import jatools.accessor.ProtectPublic;

import jatools.data.reader.DatasetCursor;
import jatools.data.reader.DatasetReader;
import jatools.data.reader.ScrollableField;

import jatools.data.sum.Sum;

import jatools.designer.App;

import jatools.engine.script.DebugOff;
import jatools.engine.script.ReportContext;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.Reader;

import java.math.BigDecimal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
  */
public class Dataset implements DatasetBase, ProtectPublic {
    public static final int ASC = 0;
    public static final int DESC = 1;
    public static final Key ANY = new Key(new Object[0]);
    public static Dataset nulls;
    public final static int NO_MORE_ROWS = -1;
    public final static int ERROR = 0;
    private static Map<String, Object> defaults;
    protected RowsVector rows;
    protected RowMeta rowInfo;
    private DatasetReader readerSrc = null;
    private RowSet rowset;
    private String globalID;
    private FilterSet filterSet;
    private ArrayList series;
    private ArrayList uniquData;
    private Map uniqueMaps;

    /**
     * Creates a new Dataset object.
     */
    public Dataset() {
        rows = new RowsVector();
    }

    /**
     * Creates a new Dataset object.
     *
     * @param info DOCUMENT ME!
     */
    public Dataset(RowMeta info) {
        this();
        this.rowInfo = info;
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ArrayList getUniqueList(String f) {
        if (this.uniqueMaps == null) {
            this.uniqueMaps = new HashMap();
        }

        ArrayList result = (ArrayList) this.uniqueMaps.get(f);

        if (result == null) {
            result = new ArrayList();

            getUniqueMaps(result, this.getColumnIndex(f));

            this.uniqueMaps.put(f, result);
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param field DOCUMENT ME!
     * @param strVals DOCUMENT ME!
     */

    //    public void filter(String field, String[] strVals) {
    //        if (filterSet == null) {
    //            filterSet = new FilterSet();
    //        }
    //
    //        filterSet.filter(field, strVals);
    //    }

    /**
     * DOCUMENT ME!
     */

    //    public void doFilter() {
    //        if (filterSet != null) {
    //            filterSet.doFilter(this);
    //        }
    //    }
    private void getUniqueMaps(ArrayList result, int col) {
        if (this.rows.size() > 0) {
            Map valMaps = new HashMap();

            for (int i = 0; i < this.rows.size(); i++) {
                Object val2 = rows.elementData[i].getValueAt(col);

                if (!valMaps.containsKey(val2)) {
                    result.add(val2);
                    valMaps.put(val2, val2);
                }
            }
        }
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    @DebugOff
    public RowSet getRowSet() {
        if (this.rowset == null) {
            this.rowset = new RangeRowSet(this, ANY, 0, this.getRowCount() - 1);
        }

        return this.rowset;
    }

    /**
     * DOCUMENT ME!
     *
     * @param colName DOCUMENT ME!
     * @param vals DOCUMENT ME!
     */
    public void addColumn(String colName, Object[] vals) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param cols DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] getColumns(int row, int[] cols) {
        Row r = this.getReferenceToRow(row);
        Object[] res = new Object[cols.length];

        for (int i = 0; i < res.length; i++) {
            res[i] = r.getValueAt(cols[i]);
        }

        return res;
    }

    /**
     * DOCUMENT ME!
     */
    public void print1() {
        new DatasetPrinter(this).printBuffers11(System.out);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dataset copy() {
        Dataset ds = new Dataset((RowMeta) this.rowInfo.clone());
        ds.rows = new RowsVector();
        ds.rows.addAll(this.rows);

        return ds;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Dataset nullSet() {
        if (nulls == null) {
            nulls = new Dataset();

            Column ci = new Column("$null_column", String.class);

            RowMeta ri = new RowMeta(new Column[] { ci });
            nulls.setRowInfo(ri);
            nulls.addRow(new Row(new String[] { "" }));
        }

        return nulls;
    }

    /**
     * DOCUMENT ME!
     *
     * @param colName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumnIndex(String colName) {
        return rowInfo.getIndexByColumnName(colName);
    }

    /**
     * DOCUMENT ME!
     *
     * @param info DOCUMENT ME!
     */
    public void addColumn(Column info) {
        addColumn(new Object[1], info);
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     */
    public void removeColumnAt(int index) {
        rowInfo.remove(index);

        for (int i = 0; i < rows.size(); i++) {
            Row row = (Row) rows.get(i);
            row.removeColumn(index);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param columnsInfo DOCUMENT ME!
     */
    public void setRowInfo(RowMeta columnsInfo) {
        rowInfo = columnsInfo;
    }

    /**
     * DOCUMENT ME!
     *
     * @param object DOCUMENT ME!
     * @param rowIndex DOCUMENT ME!
     * @param columnIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final boolean setValueAt(Object object, int rowIndex, int columnIndex) {
        if (doSetValueAt(object, rowIndex, columnIndex) > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public final DatasetReader getReadEngine() {
        return readerSrc;
    }

    /**
     * DOCUMENT ME!
     *
     * @param object DOCUMENT ME!
     * @param rowIndex DOCUMENT ME!
     * @param columnName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final boolean setValueAt(Object object, int rowIndex, String columnName) {
        int columnIndex;

        columnIndex = rowInfo.getIndexByColumnName(columnName);

        return setValueAt(object, rowIndex, columnIndex);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final boolean hasReadEngine() {
        return (readerSrc != null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param rowIndex DOCUMENT ME!
     * @param columnIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Object getValueAt(int rowIndex, int columnIndex) {
        Object object = null;

        if ((rowIndex >= 0) && (rowIndex < rows.size())) {
            Row row = (Row) rows.get(rowIndex);
            object = row.getValueAt(columnIndex);
        }

        return object;
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final InputStream getBinaryStream(int row, int column) {
        StreamService service = readerSrc.getStreamService();

        return (service != null) ? service.getBinaryStream(row, column) : null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Reader getReader(int row, int column) {
        StreamService service = readerSrc.getStreamService();

        return (service != null) ? service.getReader(row, column) : null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rowIndex DOCUMENT ME!
     * @param columnName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Object getValueAt(int rowIndex, String columnName) {
        int columnIndex;

        columnIndex = rowInfo.getIndexByColumnName(columnName);

        return getValueAt(rowIndex, columnIndex);
    }

    /**
     * DOCUMENT ME!
     *
     * @param columnIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getColumnClassName(int columnIndex) {
        return rowInfo.getColumnClassName(columnIndex);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public RowMeta getRowInfo() {
        return rowInfo;
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Column getColumn(int index) {
        return rowInfo.getColumnInfo(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumnCount() {
        if (rowInfo == null) {
            return 0;
        } else {
            return rowInfo.getColumnCount();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getColumnName(int index) {
        if (rowInfo == null) {
            throw new ArrayIndexOutOfBoundsException(App.messages.getString("res.604"));
        }

        return rowInfo.getColumnName(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRowCount() {
        return rows.size();
    }

    //
    //    /**
    //     * DOCUMENT ME!
    //     *
    //     * @param printStream DOCUMENT ME!
    //     */
    //    public void printBuffers11(PrintStream printStream) {
    //        printBuffers(printStream, true);
    //    }
    //
    //    /**
    //     * DOCUMENT ME!
    //     *
    //     * @param printStream DOCUMENT ME!
    //     * @param data DOCUMENT ME!
    //     */
    //    public void printBuffers(PrintStream printStream, boolean data) {
    //        StringBuffer sep = new StringBuffer(18 * getColumnCount());
    //        sep.append("*");
    //
    //        for (int i = 0; i < getColumnCount(); i++)
    //            sep.append("******************");
    //
    //        String sep1 = sep.toString();
    //        sep = new StringBuffer(18 * getColumnCount());
    //        sep.append("+");
    //
    //        for (int i = 0; i < getColumnCount(); i++)
    //            sep.append("-----------------+");
    //
    //        String sep2 = sep.toString();
    //
    //        if (data) {
    //            printStream.println(sep1);
    //            printStream.println("Databuffer");
    //            printColumnNames(printStream);
    //            printStream.println(sep2);
    //            printBuffer(rows, printStream);
    //            printStream.println(sep2);
    //        }
    //
    //        printStream.println(sep1);
    //    }
    //
    //    private void printBuffer(Vector buffer, PrintStream printStream) {
    //        int countRows;
    //        int countColumns;
    //        Row row;
    //        countRows = buffer.size();
    //        countColumns = getColumnCount();
    //
    //        for (int i = 0; i < countRows; i++) {
    //            StringBuffer stringBuffer = createSpaceFilledStringBuffer(countColumns * 18);
    //            row = (Row) buffer.get(i);
    //            RowsService.setStringToStringBuffer(stringBuffer, "|", 0);
    //
    //            for (int j = 0; j < countColumns; j++) {
    //                String string = "" + row.getValueAt(j);
    //
    //                if (string.length() > 17) {
    //                    string = string.substring(0, 17);
    //                }
    //
    //                RowsService.setStringToStringBuffer(stringBuffer, string, (j * 18) + 1);
    //                RowsService.setStringToStringBuffer(stringBuffer, "|", j * 18);
    //            }
    //
    //            RowsService.setStringToStringBuffer(stringBuffer, " ", (countColumns * 18) - 1);
    //            printStream.print(stringBuffer.toString());
    //            printStream.println();
    //        }
    //    }
    //
    //    private void printColumnNames(PrintStream printStream) {
    //        int countColumns;
    //
    //        countColumns = getColumnCount();
    //
    //        StringBuffer stringBuffer = createSpaceFilledStringBuffer(countColumns * 18);
    //
    //        for (int i = 0; i < countColumns; i++) {
    //            RowsService.setStringToStringBuffer(stringBuffer, " " + rowInfo.getColumnName(i) + " ",
    //                i * 18);
    //        }
    //
    //        printStream.print(stringBuffer.toString());
    //        printStream.println();
    //    }
    //
    //    private StringBuffer createSpaceFilledStringBuffer(int size) {
    //        StringBuffer stringBuffer = new StringBuffer(size);
    //
    //        for (int i = 0; i < size; i++) {
    //            stringBuffer.append(" ");
    //        }
    //
    //        return stringBuffer;
    //    }
    private final int doSetValueAt(Object object, int rowIndex, int columnIndex) {
        if ((rowIndex >= 0) && (rowIndex < rows.size())) {
            Row row = (Row) rows.get(rowIndex);

            if (row.setValueAt(columnIndex, object) > 0) {
                return 1;
            } else {
                return -2;
            }
        } else {
            return -1;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Row getRow(int index) {
        Row row = getReferenceToRow(index);

        if (row != null) {
            return (Row) row.clone();
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final Row getReferenceToRow(int index) {
        if ((index >= 0) && (index < getRowCount())) {
            return (Row) rows.get(index);
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param startRow DOCUMENT ME!
     * @param endRow DOCUMENT ME!
     * @param columnId DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] getValuesAt(int startRow, int endRow, String columnId) {
        int columnIndex = getColumnIndex(columnId);

        return getValuesAt(startRow, endRow, columnIndex);
    }

    /**
     * DOCUMENT ME!
     *
     * @param startRow DOCUMENT ME!
     * @param endRow DOCUMENT ME!
     * @param columnIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] getValuesAt(int startRow, int endRow, int columnIndex) {
        if ((startRow > endRow) || (startRow < 0) || (endRow >= getRowCount())) {
            return null;
        } else {
            Object[] objects = new Object[endRow - startRow + 1];

            int counter = 0;

            for (int i = startRow; i <= endRow; i++) {
                objects[counter] = getValueAt(i, columnIndex);
                counter++;
            }

            return objects;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param columnId DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] getValuesAt(String columnId) {
        int columnIndex = getColumnIndex(columnId);

        return getValuesAt(columnIndex);
    }

    /**
     * DOCUMENT ME!
     *
     * @param col DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] getValuesAt(int col) {
        return getValuesAt(0, getRowCount() - 1, col);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        Dataset copy = new Dataset();
        copy.setRowInfo((RowMeta) this.getRowInfo().clone());

        for (int i = 0; i < rows.size(); i++) {
            Row row = (Row) ((Row) rows.get(i)).clone();
            copy.addRow(row);
        }

        return copy;
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     */
    public void addRow(Row row) {
        if ((this.filterSet == null) || this.filterSet.accept(row)) {
            rows.add(row);
            row.index = rows.size() - 1;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param index DOCUMENT ME!
     */
    public void addRow(Row row, int index) {
        if ((this.filterSet == null) || this.filterSet.accept(row)) {
            rows.add(index, row);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param from DOCUMENT ME!
     * @param groupfields DOCUMENT ME!
     * @param size DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getGroupRowsCount(int from, int[] groupfields, int size) {
        int rc = 0;

        if ((from >= 0) && (from < getRowCount())) {
            Row fromRow = getReferenceToRow(from);

            for (int i = from;
                    (i < getRowCount()) &&
                    this.getReferenceToRow(i).equals(fromRow, groupfields, size); i++, rc++)
                ;
        }

        return rc;
    }

    /**
     * DOCUMENT ME!
     *
     * @param groupCalcs DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dataset groupBy(Sum[] groupCalcs) {
        Dataset rows = new Dataset();
        rows.rowInfo = createRowInfo(new int[0], groupCalcs);

        int startRow = 0;
        int endRow = this.getRowCount() - 1;

        if (startRow <= endRow) {
            if (groupCalcs != null) {
                Object[] values = new Object[groupCalcs.length];

                for (int i = 0; i < groupCalcs.length; i++) {
                    int column = getColumnIndex(groupCalcs[i].getCalcField());

                    if (column != -1) {
                        Object[] objects = getValuesAt(startRow, endRow, column);

                        values[i] = groupCalcs[i].getValue(objects);
                    }
                }

                rows.addRow(new Row(values));
            }
        }

        return rows;
    }

    /**
     * DOCUMENT ME!
     *
     * @param idexpr DOCUMENT ME!
     * @param idas DOCUMENT ME!
     * @param xfield DOCUMENT ME!
     * @param dfield DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public Dataset crossGroup(String idexpr, String idas, String xfield, String dfield)
        throws Exception {
        return crossGroup(idexpr, idas, xfield, dfield, null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param idexpr DOCUMENT ME!
     * @param idas DOCUMENT ME!
     * @param xfield DOCUMENT ME!
     * @param dfield DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public Dataset crossGroup(String idexpr, String idas, String xfield, String dfield,
        String cProps) throws Exception {
        long before = System.currentTimeMillis();

        ReportContext script = new ReportContext(null);

        NameSpace localNameSpace = script.createNameSpace();
        script.pushNameSpace(localNameSpace);

        ScrollableField _xfield = null;
        ScrollableField _dfield = null;
        DatasetCursor rowsCursor = new DatasetCursor(this);

        Map m = new HashMap();

        for (int i = 0; i < this.getColumnCount(); i++) {
            ScrollableField field = new ScrollableField(i, rowsCursor);

            try {
                String f = getColumnName(i);

                m.put(f, field);

                if (f.equals(xfield)) {
                    _xfield = field;
                } else if (dfield.equals(f)) {
                    _dfield = field;
                }

                localNameSpace.setLocalVariable(f, field);
            } catch (UtilEvalError e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        Map groups = new HashMap();
        List columns = new ArrayList();

        columns.add(new Column(idas, "java.lang.String"));

        String[] props = (cProps != null) ? cProps.split(",") : null;

        while (rowsCursor.hasNext()) {
            rowsCursor.next();

            Object o = _xfield.value();

            if (!groups.containsKey(o)) {
                try {
                    Column c = new Column(o + "", "java.lang.Double");

                    if (props != null) {
                        for (int i = 0; i < props.length; i++) {
                            c.set(props[i], script.get(props[i]));
                        }
                    }

                    columns.add(c);
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            groups.put(o, o);
        }

        Dataset ds = new Dataset(new RowMeta((Column[]) columns.toArray(new Column[0])));

        rowsCursor.reset();

        Map rows = new HashMap();

        while (rowsCursor.hasNext()) {
            rowsCursor.next();

            Object rid = script.eval(idexpr);

            Row row = (Row) rows.get(rid);

            if (row == null) {
                Object[] vals = new Object[ds.getColumnCount()];
                Arrays.fill(vals, new Double(0.0));

                row = new Row(vals);
                row.setValueAt(0, rid);

                rows.put(rid, row);
                ds.addRow(row);
            }

            String cid = _xfield.value() + "";
            Object val = _dfield.value();

            if (val != null) {
                row.setValueAt(ds.getColumnIndex(cid), val);
            }
        }

        System.out.println(System.currentTimeMillis() - before);

        return ds;
    }

    /**
     * DOCUMENT ME!
     *
     * @param idexpr DOCUMENT ME!
     * @param idas DOCUMENT ME!
     * @param xfield DOCUMENT ME!
     * @param dfield DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    protected RowMeta createRowInfo(int[] is, Sum[] groupCalcs) {
        Column[] infos = new Column[is.length + groupCalcs.length];

        for (int i = 0; i < is.length; i++) {
            infos[i] = rowInfo.getColumnInfo(is[i]);
        }

        for (int i = 0; i < groupCalcs.length; i++) {
            Column info = (Column) getColumn(getColumnIndex(groupCalcs[i].getCalcField())).clone();

            String columnName = groupCalcs[i].getName();

            info.setName(columnName);
            infos[i + is.length] = info;
        }

        return new RowMeta(infos);
    }

    /**
     * DOCUMENT ME!
     *
     * @param is DOCUMENT ME!
     * @param groupCalcs DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dataset copyStructure(int[] is, Sum[] groupCalcs) {
        RowMeta info = createRowInfo(is, groupCalcs);

        return new Dataset(info);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEmpty() {
        return this.getRowCount() == 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final int removeRow(int index) {
        if ((index >= 0) && (index < rows.size())) {
            rows.remove(index);

            return index;
        } else {
            return -1;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param reader DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String[] getFieldNames(DatasetReader reader) {
        final String[] empty = new String[0];
        String[] columns = empty;

        try {
            Dataset rows = reader.read(ReportContext.getDefaultContext(), 0);
            columns = new String[rows.getColumnCount()];

            for (int i = 0; i < columns.length; i++) {
                columns[i] = rows.getColumnName(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return columns;
    }

    /**
     * DOCUMENT ME!
     */
    public void toPrint1() {
        for (int i = 0; i < getColumnCount(); i++) {
            System.out.print(getColumnName(i) + "\t\t");
        }

        System.out.println("");

        for (int r = 0; r < getRowCount(); r++) {
            for (int c = 0; c < getColumnCount(); c++) {
                System.out.print(getValueAt(r, c) + "\t\t");
            }

            System.out.println("");
        }

        System.out.println("");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(bao);
        //  printBuffers11(ps);
        ps.close();

        try {
            bao.close();

            return new String(bao.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    //    private void fillID(int columnIndex) {
    //        for (int i = 0; i < this.getRowCount(); i++) {
    //            this.setValueAt(new Integer(i), i, columnIndex);
    //        }
    //    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getRowPath(int i) {
        return new RowPath(this.rowInfo, this.getReferenceToRow(i));
    }

    /**
     * DOCUMENT ME!
     *
     * @param vals DOCUMENT ME!
     * @param column DOCUMENT ME!
     */
    public void addColumn(Object[] vals, Column column) {
        rowInfo.addColumn(column);

        if ((vals != null) && (vals.length > 0)) {
            int src = 0;

            for (int i = 0; i < rows.size(); i++) {
                Row row = (Row) rows.get(i);
                row.addColumn(vals[src]);

                src++;

                if (src == vals.length) {
                    src = 0;
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param readerSrc DOCUMENT ME!
     */
    public void setReaderSrc(DatasetReader readerSrc) {
        this.readerSrc = readerSrc;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public Row[] getRowsArray() {
        return (Row[]) this.rows.toArray(new Row[0]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public boolean isPaddingFirst() {
        return rows.isPaddingFirst();
    }

    /**
     * DOCUMENT ME!
     *
     * @param paddingFirst DOCUMENT ME!
     */
    public void setPaddingFirst(boolean paddingFirst) {
        rows.setPaddingFirst(paddingFirst);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    @DebugOff
    public String getGlobalID() {
        return globalID;
    }

    /**
     * DOCUMENT ME!
     *
     * @param globalID DOCUMENT ME!
     */
    public void setGlobalID(String globalID) {
        this.globalID = globalID;
    }

    /**
     * DOCUMENT ME!
     *
     * @param labelField DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Map getUniquMap(String labelField) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rowComparator DOCUMENT ME!
     */
    public void sort(RowComparator rowComparator) {
        Arrays.sort(this.rows.elementData, rowComparator);
    }

    /**
     * DOCUMENT ME!
     *
     * @param col DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getDefaultValue(int col) {
        if (defaults == null) {
            defaults = new HashMap<String, Object>();
            defaults.put(BigDecimal.class.getName(), new BigDecimal(0));
            defaults.put(Integer.class.getName(), new Integer(0));
            defaults.put(Double.class.getName(), new Double(0));
            defaults.put(Float.class.getName(), new Float(0));
        }

        return defaults.get(this.getColumnClassName(col));
    }
}
