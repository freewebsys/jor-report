package jatools.data.reader.sql;

import jatools.accessor.PropertyDescriptor;

import jatools.component.ComponentConstants;

import jatools.data.reader.AbstractDatasetReader;

import jatools.dataset.Dataset;
import jatools.dataset.DatasetException;
import jatools.dataset.Row;
import jatools.dataset.RowMeta;
import jatools.dataset.StreamService;

import jatools.designer.App;

import jatools.engine.printer.ReportPrinter;

import jatools.engine.script.ReportContext;
import jatools.engine.script.Script;

import jatools.util.Util;

import org.apache.log4j.Logger;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.ArrayList;
import java.util.List;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class SqlReader extends AbstractDatasetReader {
    private static Logger logger = Logger.getLogger("ZSqlReader");
    public static final String TYPE = App.messages.getString("res.619");
    public static final int QUERY = 0;
    public static final int STORED_PROCEDURE = 1;
    public final static int DONE = -1;
    private static ReportContext context;
    private Connection connection;
    private int jdbcType;
    private String sql;
    protected java.sql.Connection conn = null;
    private PreparedStatement stmt = null;
    ResultSet results = null;
    private int rowCount = 0;
    StreamService streamService;
    private Dataset lastDataset;
    private RowMeta lastRowMeta;

    /**
     * Creates a new SqlReader object.
     */
    public SqlReader() {
    }

    /**
     * Creates a new SqlReader object.
     *
     * @param connection DOCUMENT ME!
     * @param sql DOCUMENT ME!
     */
    public SqlReader(Connection connection, String sql) {
        setConnection(connection);
        setSql(sql);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getJdbcType() {
        return jdbcType;
    }
    
    public static void main(String[] args) {
	//	String a = "220.191.211.182";
		System.out.println(reverse("220.191.211.182"));
		System.out.println(reverse("www.hedasafety.gov.cn"));
	}
    
    private static String reverse(String s)
    {
    	String result = "";
    	for (int i = 0; i < s.length(); i++) {
			result +=s.charAt(s.length()-1-i);
		}
    	
    	return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param jdbcType DOCUMENT ME!
     */
    public void setJdbcType(int jdbcType) {
        this.jdbcType = jdbcType;
        invalidate();
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
    public StreamService getStreamService() {
        if (streamService == null) {
            streamService = new SqlStreamService(results);
        }

        return streamService;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] {
            new PropertyDescriptor(ComponentConstants.PROPERTY_NAME, String.class),
            
            new PropertyDescriptor(ComponentConstants.PROPERTY_SQL, String.class),
            new PropertyDescriptor(ComponentConstants.PROPERTY_DESCRIPTION, String.class),
            new PropertyDescriptor("JdbcType", Integer.TYPE),
            ComponentConstants._USER_DEFINED_COLUMNS,
            new PropertyDescriptor(ComponentConstants.PROPERTY_CONNECTION, Connection.class)
        };
    }

    /**
     * DOCUMENT ME!
     *
     * @param sql DOCUMENT ME!
     */
    public void setSql(String sql) {
        if (sql != null) {
            this.sql = sql;
        }

        invalidate();
    }

    /**
     * DOCUMENT ME!
     *
     * @param connection DOCUMENT ME!
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
        invalidate();
    }

    private void invalidate() {
        this.lastDataset = null;
        this.lastRowMeta = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param script DOCUMENT ME!
     * @param withdata DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws . DOCUMENT ME!
     */
    public RowMeta readStart(Script script, boolean withdata)
        throws jatools.dataset.DatasetException {
        destroy();
        rowCount = 0;

        try {
            if (sql == null) {
                throw new IllegalArgumentException(Util.debug(logger,
                        App.messages.getString("res.620")));
            }

            String sqlcopy = sql;

            if ((sql.indexOf("${") > -1)) {
                if (script instanceof ReportContext) {
                    sqlcopy = (String) ((ReportContext) script).evalTemplate(sql);
                } else {
                    if (context == null) {
                        context = ReportContext.getDefaultContext();
                    }

                    sqlcopy = (String) context.evalTemplate(sql);
                }
            }
            
            Util.debug(logger,sqlcopy);  // 合并后的sql输出到控制台

            conn = getConnection2(script);

            if (isQuery(sqlcopy)) {
            	  while ((sqlcopy = sqlcopy.trim()).endsWith(";")) {
                      sqlcopy = sqlcopy.substring(0, sqlcopy.length() - 1);
                  }
                if (!withdata) {
                    // 修正bug,当输入的sql以分号结束时
                  

                 //   sqlcopy = "select * from (" + sqlcopy + ") a where 1=0";
                    
                   sqlcopy = SqlUtil.toNodataSql(sqlcopy);
                }

                stmt = conn.prepareStatement(sqlcopy, ResultSet.TYPE_SCROLL_INSENSITIVE,
                        ResultSet.CONCUR_READ_ONLY);
                results = stmt.executeQuery();
            } else {
                if (sqlcopy.startsWith("=")) {
                    Statement stmt2 = conn.createStatement();
                    stmt2.execute(sqlcopy.substring(1));

                    conn.commit();
                    stmt2.close();
                    stmt2 = null;
                } else {
                    stmt = conn.prepareCall(sqlcopy);

                    StoredProcResultsFactory factory = getResultsFactory(conn.getClass());

                    results = factory.createResultSet((CallableStatement) stmt);
                }
            }

            ResultSetMetaData metaData = results.getMetaData();

            int columnCount = metaData.getColumnCount();

            if (columnCount > 0) {
                SqlColumnInfo[] columnInfos = new SqlColumnInfo[columnCount];

                for (int i = 1; i <= columnCount; i++) {
                    String className = null;

                    try {
                        className = metaData.getColumnClassName(i);
                    } catch (Exception e1) {
                        className = "unknown";
                    }

                    columnInfos[i - 1] = new SqlColumnInfo(metaData.getColumnName(i), className,
                            metaData.getColumnType(i));
                }

                return new SqlRowInfo(columnInfos);
            } else {
                throw new jatools.dataset.DatasetException(Util.debug(logger,
                        App.messages.getString("res.621") + "\n" + sqlcopy));
            }
        } catch (Exception e) {
            throw new jatools.dataset.DatasetException(Util.debug(logger,
                    App.messages.getString("res.622") + Util.toString(e)), e);
        }
    }

    private StoredProcResultsFactory getResultsFactory(Class cls) {
        if (this.connection.getDriver().toLowerCase().indexOf("oracle.") > -1) {
            return new OracleStoredProcResultsFactory();
        } else if (cls.getName().indexOf("microsoft.") > -1) {
            return new MSSqlStoredProcResultsFactory();
        } else {
            return new OtherStoredProcResultsFactory();
        }
    }

    private boolean isQuery(String sql) {
        if (sql != null) {
            return sql.toLowerCase().indexOf("select ") > -1;
        } else {
            return false;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRowCount() {
        try {
            results.last();

            int rc = results.getRow();
            results.beforeFirst();

            return rc;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param paraProvider DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     * @throws . DOCUMENT ME!
     */
    public java.sql.Connection getConnection2(Script paraProvider)
        throws Exception {
        if (connection == null) {
            throw new jatools.dataset.DatasetException(Util.debug(logger,
                    App.messages.getString("res.623")));
        }

        return connection.getConnection(paraProvider);
    }

    /**
     * DOCUMENT ME!
     *
     * @param rowInfo DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws . DOCUMENT ME!
     */
    public Row readRow(RowMeta rowInfo) throws jatools.dataset.DatasetException {
        if (results == null) {
            throw new jatools.dataset.DatasetException(Util.debug(logger,
                    App.messages.getString("res.624")));
        }

        try {
            if (rowInfo != null) {
                if (results.next()) {
                    Object[] values;
                    values = new Object[rowInfo.getColumnCount()];

                    for (int i = 1; i <= values.length; i++) {
                        values[i - 1] = results.getObject(i);
                    }

                    rowCount++;

                    return new Row(values);
                } else {
                    return Row.NO_MORE_ROWS;
                }
            } else {
                throw new jatools.dataset.DatasetException(App.messages.getString("res.612"));
            }
        } catch (SQLException e) {
            throw new jatools.dataset.DatasetException(App.messages.getString("res.613"), e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     */
    public int readEnd() throws jatools.dataset.DatasetException {
        streamService = new SqlStreamService(results);

        release();

        return rowCount;
    }

    protected void finalize() throws Throwable {
        destroy();
        super.finalize();
    }

    void release() {
        List readers = ReportPrinter.getLocalReaders();

        if (!readers.contains(this)) {
            readers.add(this);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void destroy() {
        if (results != null) {
            try {
                results.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            results = null;
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            stmt = null;
        }

        if (ConnectionPools.NO_CACHE && (conn != null)) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            conn = null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getSql() {
        return sql;
    }

    /**
     * DOCUMENT ME!
     *
     * @param that DOCUMENT ME!
     */
    public void copy(SqlReader that) {
        that.name = name;
        that.description = description;
        that.sql = sql;
        that.type = type;

        if (connection != null) {
            that.setConnection((Connection) connection.clone());
        }
    }

    protected Dataset doRead(Script script, int requestRows)
        throws DatasetException {
        if (requestRows == 0) {
            return getEmptyDataset(script);
        } else {
            return getDataset(script, requestRows);
        }
    }

    private Dataset getDataset(Script script, int requestRows)
        throws DatasetException {
        int count = 0;

        boolean withData = requestRows != 0;
        lastRowMeta = readStart(script, withData);

        Dataset ds = new Dataset(lastRowMeta);

        if (requestRows == -1) {
            requestRows = Integer.MAX_VALUE;
        }

        if (filterSet != null) {
            filterSet.initDataset(ds);
        }

        Row row = null;

        while ((count < requestRows) && ((row = readRow(lastRowMeta)) != Row.NO_MORE_ROWS)) {
            if ((filterSet == null) || filterSet.accept(row)) {
                ds.addRow(row);
                count++;
            }
        }

        readEnd();

        if (this.userDefinedColumns != null) {
            addUserColumns(ds, this.userDefinedColumns, script);
        }

        ds.setReaderSrc(this);

        return ds;
    }

    private Dataset getEmptyDataset(Script script) throws DatasetException {
        if (lastDataset != null) {
            return lastDataset;
        } else {
            if (this.lastRowMeta == null) {
                this.lastDataset = new Dataset(readStart(script, false));
            } else {
                this.lastDataset = new Dataset(lastRowMeta);
            }

            if (this.userDefinedColumns != null) {
                addUserColumns(lastDataset, this.userDefinedColumns, script);
            }

            lastDataset.setReaderSrc(this);

            return lastDataset;
        }
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
        SqlReader inst = new SqlReader();
        inst.connection = (this.connection == null) ? null : (Connection) this.connection.clone();
        inst.sql = this.sql;
        inst.jdbcType = this.jdbcType;
        inst.name = name + "_copy";
        inst.description = this.description;
        inst.type = this.type;

        return inst;
    }

    /**
     * DOCUMENT ME!
     *
     * @param script DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dataset load(Script script) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param script DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RowMeta getRowMeta(Script script) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param userDefinedColumns DOCUMENT ME!
     */
    public void setUserDefinedColumns(ArrayList userDefinedColumns) {
        super.setUserDefinedColumns(userDefinedColumns);
        this.invalidate();
    }
    
    
    public String getDescription() {
        return this.sql;
    }
}
