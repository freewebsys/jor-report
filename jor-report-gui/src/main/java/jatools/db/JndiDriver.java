package jatools.db;

import jatools.accessor.ProtectPublic;

import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class JndiDriver implements Driver, ProtectPublic {
    final static int MAJOR = 1;
    final static int MINOR = 4;
    final static String VERSION = MAJOR + "." + MINOR;
    final static String PRODUCT = "Jndi";

    /**
     * DOCUMENT ME!
     *
     * @param url DOCUMENT ME!
     * @param info DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws SQLException DOCUMENT ME!
     */
    public Connection connect(String url, Properties info)
        throws SQLException {
        if (!acceptsURL(url)) {
            return null;
        }

        return JndiDriverUtil.getConnection(url);
    }

    /**
     * DOCUMENT ME!
     *
     * @param url DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean acceptsURL(String url) {
        return url.startsWith("FACTORY=");
    }

    /**
     * DOCUMENT ME!
     *
     * @param url DOCUMENT ME!
     * @param info DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) {
        return new DriverPropertyInfo[0];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMajorVersion() {
        return MAJOR;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMinorVersion() {
        return MINOR;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean jdbcCompliant() {
        return false;
    }

    public Logger getParentLogger() throws SQLFeatureNotSupportedException{
        return null;
    }
}
