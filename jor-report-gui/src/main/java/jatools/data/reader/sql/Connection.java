/*
 * Author: John.
 *
 * 杭州杰创软件 All Copyrights Reserved.
 */
package jatools.data.reader.sql;

import bsh.Interpreter;

import jatools.accessor.PropertyAccessor;
import jatools.accessor.PropertyDescriptor;

import jatools.component.ComponentConstants;

import jatools.engine.ValueIfClosed;

import jatools.engine.script.ReportContext;
import jatools.engine.script.Script;

import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.4 $
 * @author $author$
 */
public class Connection implements PropertyAccessor, ValueIfClosed {
    private static Logger logger = Logger.getLogger("ZConnection"); //
    private String driver;
    private String url;
    private String user;
    private String password;

    //   private String name;

    /**
     * Creates a new _Connection object.
     *
     * @param driver
     *            DOCUMENT ME!
     * @param url
     *            DOCUMENT ME!
     * @param user
     *            DOCUMENT ME!
     * @param password
     *            DOCUMENT ME!
     */
    public Connection(String driver, String url, String user, String password) {
        //  this.name = name;
        this.driver = driver;
        this.url = url;
        this.user = user;
        this.password = password;
    }

    /**
     * Creates a new ZConnection object.
     */
    public Connection() {
    }

    /* (non-Javadoc)
     * @see java.lang.Object#finalize()
     */

    //    protected void finalize() throws Throwable {
    //       
    //        poolCount--;
    //
    //        if (poolCount == 0) {
    //            Collection conns = connectionPool.values();
    //
    //            for (Iterator iter = conns.iterator(); iter.hasNext();) {
    //                Connection element = (Connection) iter.next();
    //
    //                try {
    //                    element.close();
    //                } catch (SQLException e) {
    //                    e.printStackTrace();
    //                }
    //            }
    //        }
    //        super.finalize();
    //    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] {
            new PropertyDescriptor(ComponentConstants.PROPERTY_DRIVER, String.class),
            

            //
            new PropertyDescriptor(ComponentConstants.PROPERTY_USER, String.class),
            

            //
            new PropertyDescriptor(ComponentConstants.PROPERTY_PASSWORD, String.class),
            

            //
            new PropertyDescriptor(ComponentConstants.PROPERTY_URL, String.class)
        };
    }

    /**
     * DOCUMENT ME!
     *
     * @param sql DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object select(String sql) {
        java.sql.Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet results = null;
        Object result = null;

        try {
            // System.out.println(sqlcopy);
            conn = getConnection(ReportContext.getDefaultContext());

            stmt = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_READ_ONLY);

            results = stmt.executeQuery();

            if (results.next()) {
                result = results.getObject(1);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (results != null) {
                try {
                    results.close();

                    // results = null;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            if (ConnectionPools.NO_CACHE && (conn != null)) {
                try {
                    conn.close();

                    conn = null;
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     */

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public java.sql.Connection getConnection() throws Exception {
        return getConnection(ReportContext.getDefaultContext());
    }

    /**
     * DOCUMENT ME!
     *
     * @param dataProvider DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public java.sql.Connection getConnection(Script dataProvider)
        throws Exception {
        //System.out.println("get !");
        return ConnectionPools.getDefault().getConnection(this, dataProvider);
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
            if ((str.indexOf("${") > -1)) { //

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
     * @param o
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object o) {
        if (!(o instanceof Connection)) {
            return false;
        }

        Connection that = (Connection) o;

        String thisValue = getDriver();
        String thatValue = that.getDriver();

        if (!((thisValue == null) ? (thatValue == null) : thisValue.equals(thatValue))) {
            return false;
        }

        thisValue = getUrl();
        thatValue = that.getUrl();

        if (!((thisValue == null) ? (thatValue == null) : thisValue.equals(thatValue))) {
            return false;
        }

        thisValue = getUser();
        thatValue = that.getUser();

        if (!((thisValue == null) ? (thatValue == null) : thisValue.equals(thatValue))) {
            return false;
        }

        thisValue = getPassword();
        thatValue = that.getPassword();

        if (!((thisValue == null) ? (thatValue == null) : thisValue.equals(thatValue))) {
            return false;
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDriver() {
        return driver;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPassword() {
        return password;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getUrl() {
        return url;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getUser() {
        return user;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        Connection inst = new Connection();
        inst.driver = this.driver;
        inst.url = this.url;
        inst.user = this.user;
        inst.password = this.password;

        return inst;
    }

    /**
     * @param driver
     *            The driver to set.
     */
    public void setDriver(String driver) {
        this.driver = driver;
    }

    /**
     * @param password
     *            The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @param url
     *            The url to set.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * @param user
     *            The user to set.
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Override hashCode.
     *
     * @return the Objects hashcode.
     */
    public int hashCode() {
        return ((url == null) ? 1 : url.hashCode());
    }

    /* (non-Javadoc)
     * @see bsh.DataItem#getValue()
     */

   
    /**
     * DOCUMENT ME!
     *
     * @param it DOCUMENT ME!
     */
    public void setInterpreter(Interpreter it) {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object value() {
        try {
            return getConnection();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
}
