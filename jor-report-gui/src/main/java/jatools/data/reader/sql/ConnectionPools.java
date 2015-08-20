package jatools.data.reader.sql;

import jatools.db.JndiDriver;
import jatools.engine.script.ReportContext;
import jatools.engine.script.Script;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ConnectionPools {
    static {
        try {
            DriverManager.registerDriver(new JndiDriver());
        } catch (Exception e) {
        }
    }

    public static boolean NO_CACHE = true; // System2.getProperty("no.connection.cache") != null;
    private static ConnectionPools instance;
    private Map connectionPool2 = new HashMap();

    /**
     * DOCUMENT ME!
     *
     * @param conn2 DOCUMENT ME!
     * @param dataProvider DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public java.sql.Connection getConnection(Connection conn2, Script dataProvider)
        throws Exception {
        java.sql.Connection conn = (java.sql.Connection) connectionPool2.get(this);

        if (conn == null) {
            try {
                String driver = eval(dataProvider, conn2.getDriver());

                if (driver != null) {
                    driver = driver.trim();
                }

                String url = eval(dataProvider, conn2.getUrl());

                if (url != null) {
                    url = url.trim();
                }

                String user = eval(dataProvider, conn2.getUser());

                String password = eval(dataProvider, conn2.getPassword());

          
                Class.forName(driver);

                conn = DriverManager.getConnection(url, user, password);

                if (!NO_CACHE) {
                    connectionPool2.put(this, conn);
                }
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
                throw ex;
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw ex;
            }
        }

        return (java.sql.Connection) conn;
    }

    protected void finalize() throws Throwable {
        closePools();

        super.finalize();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String poolInfo() {
        Iterator it = connectionPool2.keySet().iterator();
        String res = "";

        while (it.hasNext()) {
            Connection conn = (Connection) it.next();

            res += "<BR>";
        }

        return res;
    }

    /**
     * DOCUMENT ME!
     */
    public void closePools() {
        Iterator it = connectionPool2.values().iterator();

        while (it.hasNext()) {
            java.sql.Connection conn = (java.sql.Connection) it.next();

            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            conn = null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param dataProvider DOCUMENT ME!
     * @param str DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String eval(Script dataProvider, String str) {
        if (str == null) {
            return str;
        } else {
            if ((str.indexOf("${") > -1)) {
                if (dataProvider != null) {
                    str = (String) ((ReportContext) dataProvider).evalTemplate(str);
                } else {
                    str = (String) ReportContext.getDefaultContext().evalTemplate(str);
                }
            }

            return str;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ConnectionPools getDefault() {
        if (instance == null) {
            instance = new ConnectionPools();
        }

        return instance;
    }
}
