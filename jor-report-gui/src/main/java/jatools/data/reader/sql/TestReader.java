package jatools.data.reader.sql;

import jatools.data.reader.DatasetReader;


public class TestReader {
    /**
     * DOCUMENT ME!
     *
     * @param sql DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static DatasetReader getReader(String sql) {
        Connection con = new Connection("org.hsqldb.jdbcDriver", "jdbc:hsqldb:demosdb/mydb", "sa",
                null);
        SqlReader reader = new SqlReader(con, sql);

        return reader;
    }
}
