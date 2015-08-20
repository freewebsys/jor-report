/*
 * Created on 2004-8-9
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jatools.data.reader.csv;

import jatools.accessor.PropertyDescriptor;

import jatools.component.ComponentConstants;

import jatools.data.reader.AbstractDatasetReader;
import jatools.data.reader.sql.Connection;
import jatools.data.reader.sql.WhereExpressionVisitor;

import jatools.dataset.Column;
import jatools.dataset.DatasetException;
import jatools.dataset.Row;
import jatools.dataset.RowComparator;
import jatools.dataset.RowMeta;

import jatools.db.TypeUtil;

import jatools.engine.script.Script;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;

import java.net.URL;
import java.net.URLConnection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author java
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */

/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.5 $
 * @author $author$
 */
public class CsvReader extends AbstractDatasetReader {
    private static Logger logger = Logger.getLogger("ZCsvRowsReader"); //

    /**
     * DOCUMENT ME!
     */
    public static final String TYPE = "Csv数据集"; //
    String filePath;
    int currentRow1;

    //   BufferedReader reader;
    boolean localFile;
    private String orderBy;
    private String where;
    private List<Row> rows;

    /**
     * DOCUMENT ME!
     *
     * @param readRowInfo
     *            DOCUMENT ME!
     * @param paraProvider
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws com.jatools.data.rs.DatasetException
     *             DOCUMENT ME!
     */

    /**
     * Creates a new ZBeanRowsReader object.
     */
    public CsvReader() {
    }

    /**
     * Creates a new ZBeanRowsReader object.
     *
     * @param name
     *            DOCUMENT ME!
     * @param description
     *            DOCUMENT ME!
     * @param beanClass
     *            DOCUMENT ME!
     */
    public CsvReader(String name, String description, String filePath, boolean localFile,
        String where, String orderBy) {
        this.name = name;
        this.description = description;
        this.filePath = filePath;
        this.localFile = localFile;
        this.where = where;
        this.orderBy = orderBy;
    }

    /**
     * Creates a new CsvReader object.
     *
     * @param filePath DOCUMENT ME!
     */
    public CsvReader(String filePath) {
        this.filePath = filePath;
        this.localFile = true;
    }

    /**
     * @param localFile
     *            The localFile to set.
     */
    public void setLocalFile(boolean localFile) {
        this.localFile = localFile;
    }

    /**
     * @return Returns the filePath.
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * @param filePath
     *            The filePath to set.
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getType() {
        return TYPE;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] {
            new PropertyDescriptor(ComponentConstants.PROPERTY_NAME, String.class),
            
            new PropertyDescriptor(ComponentConstants.PROPERTY_DESCRIPTION, String.class),
            new PropertyDescriptor("FilePath", //
                String.class), new PropertyDescriptor("LocalFile", Boolean.TYPE), //
            new PropertyDescriptor("Where", String.class), //
            new PropertyDescriptor("OrderBy", String.class),
            ComponentConstants._USER_DEFINED_COLUMNS
        };
    }

    private PlainSelect joinSelect(Script script) {
        if ((this.where == null) && (this.orderBy == null)) {
            return null;
        }

        String sql = "select * from temp";

        if ((where != null) && (where.trim().length() > 0)) {
            if (where.toLowerCase().indexOf("select ") != -1) {
                sql = where;
            } else {
                sql += (" where " + Connection.eval(script, where));
            }
        }

        if ((orderBy != null) && (orderBy.trim().length() > 0)) {
            sql += (" order by " + orderBy);
        }

        try {
            Statement stat = new CCJSqlParserManager().parse(new StringReader(sql));

            return (PlainSelect) ((Select) stat).getSelectBody();
        } catch (JSQLParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param script DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws . DOCUMENT ME!
     */
    public RowMeta readStart(Script script, boolean withdata)
        throws jatools.dataset.DatasetException {
        currentRow1 = 0;

        try {
            BufferedReader reader = openCsv(script);

            // classes {String,Integer,Date}
            String[] classes = CsvParser.parse(reader); // 

            if ((classes == null) || (classes.length == 0)) {
                throw new DatasetException("csv文件 " + filePath + "未定义字段类型");
            }

            String[] columnNames = CsvParser.parse(reader);

            if ((columnNames == null) || (columnNames.length == 0)) {
                throw new DatasetException("csv文件 " + filePath + "未定义字段名称");
            }

            if (classes.length != columnNames.length) {
                throw new DatasetException("csv文件 " + filePath + "字段类型与字段名称数量不一致");
            }

            int columnCount = classes.length;

            if (columnCount > 0) {
                Column[] columnInfos = new Column[columnCount];

                for (int i = 0; i < columnCount; i++) {
                    Class cls = TypeUtil.getClass(classes[i].trim().toLowerCase());

                    if (cls == null) {
                        throw new DatasetException("csv文件 " + filePath + "不支持字段类型:" + classes[i]);
                    }

                    columnInfos[i] = new Column(columnNames[i].trim() /*.toUpperCase()*/, cls);
                }

                RowMeta meta = new RowMeta(columnInfos);
                rows = load(reader, meta, withdata, script);

                reader.close();

                return meta;
            } else {
                throw new DatasetException("csv文件 " + filePath + "的列数为0"); // //$NON-NLS-2$
            }
        } catch (Exception e) {
            throw new jatools.dataset.DatasetException("初始化csv文件 " + filePath + "时出错.", e); // //$NON-NLS-2$
        }
    }

    private List<Row> load(BufferedReader reader, RowMeta rowInfo, boolean withdata, Script script) {
        List<Row> rows = new ArrayList<Row>();

        if (!withdata) {
            return rows;
        }

        PlainSelect select = joinSelect(script);

        WhereExpressionVisitor visitor = null;

        if ((select != null) && (select.getWhere() != null)) {
            Map<String, Integer> _columns = new HashMap<String, Integer>();

            for (int i = 0; i < rowInfo.getColumnCount(); i++) {
                _columns.put(rowInfo.getColumnName(i).toUpperCase(), i);
            }

            visitor = new WhereExpressionVisitor(_columns, select.getWhere());
        }

        String[] strValues = null;

        try {
            while ((strValues = CsvParser.parse(reader)) != null) {
                Object[] values = CsvParser.strings2Objects(strValues, rowInfo);

                if (visitor != null) {
                    Object result = visitor.eval(values);

                    if (result instanceof Boolean && ((Boolean) result).booleanValue()) {
                        rows.add(new Row(values));
                    }
                } else {
                    rows.add(new Row(values));
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if ((select != null) && (select.getOrderByElements() != null) &&
                !select.getOrderByElements().isEmpty()) {
            List orders = select.getOrderByElements();

            int[] cols = new int[1];
            boolean[] dirs = new boolean[orders.size()];

            for (int i = 0; i < cols.length; i++) {
                OrderByElement order = (OrderByElement) orders.get(i);

                if (order.getColumnReference() instanceof net.sf.jsqlparser.schema.Column) {
                    net.sf.jsqlparser.schema.Column column = (net.sf.jsqlparser.schema.Column) order.getColumnReference();
                    cols[i] = rowInfo.getIndexByColumnName(column.getColumnName());
                    dirs[i] = order.isAsc();
                } else {
                    return rows;
                }
            }

            Collections.sort(rows, new RowComparator(cols, dirs));
        }

        return rows;
    }

    /**
    * @param script
    * @return
    * @throws FileNotFoundException
    * @throws IOException
    */
    private BufferedReader openCsv(Script script) throws IOException {
        BufferedReader in = null;

        String _filePath = Connection.eval(script, filePath);

        System.out.println(new File(_filePath).getAbsolutePath());

        if (localFile) {
            in = new BufferedReader(new FileReader(_filePath));
        } else {
            URL url = new URL(_filePath);
            URLConnection conn = url.openConnection();
            conn.connect();
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        }

        return in;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rowInfo
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ZDataSetException
     *             DOCUMENT ME!
     */
    public Row readRow(RowMeta rowInfo) throws jatools.dataset.DatasetException {
        if (currentRow1 < this.rows.size()) {
            Row row = this.rows.get(currentRow1);
            currentRow1++;

            return row;
        } else {
            return Row.NO_MORE_ROWS;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws ZDataSetException
     *             DOCUMENT ME!
     */
    public int readEnd() throws jatools.dataset.DatasetException {
        return currentRow1 + 1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return this.getName();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        CsvReader inst = new CsvReader();
        inst.filePath = filePath;
        inst.name = name + "_copy"; //
        inst.description = this.description;
        inst.where = where;
        inst.orderBy = orderBy;
        inst.localFile = localFile;

        return inst;
    }

    /**
     * DOCUMENT ME!
     *
     * @param that
     *            DOCUMENT ME!
     */
    public void copy(CsvReader that) {
        that.name = name;
        that.description = description;
        that.filePath = filePath;
        that.where = where;
        that.orderBy = orderBy;
        that.localFile = localFile;
    }

    /**
     * @return
     */
    public boolean isLocalFile() {
        return localFile;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * DOCUMENT ME!
     *
     * @param orderBy DOCUMENT ME!
     */
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getWhere() {
        return where;
    }

    /**
     * DOCUMENT ME!
     *
     * @param where DOCUMENT ME!
     */
    public void setWhere(String where) {
        this.where = where;
    }
}
