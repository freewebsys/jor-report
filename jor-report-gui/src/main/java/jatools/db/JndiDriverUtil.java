package jatools.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class JndiDriverUtil {
    /**
     * DOCUMENT ME!
     *
     * @param url DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Connection getConnection(String url) {
        Hashtable ht = new Hashtable();

        Properties props = new Properties();

        parseURL(props, url);

        if (props.containsKey("FACTORY")) {
            ht.put(Context.INITIAL_CONTEXT_FACTORY, props.getProperty("FACTORY"));
        }

        if (props.containsKey("URL")) {
            ht.put(Context.PROVIDER_URL, props.getProperty("URL"));
        }

        if (ht.isEmpty()) {
            ht = null;
        }

        try {
            Context ct = new InitialContext(ht);

            DataSource obj = (DataSource) ct.lookup(props.getProperty("DATASOURCE"));

            return (obj == null) ? null : obj.getConnection();
        } catch (NamingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        String a = "FACTORY=org.jnp.interfaces.NamingContextFactory;URL=jnp://localhost:1099;DATASOURCE=java:OracleDS";
        Hashtable ht = new Hashtable();

        Properties props = new Properties();

        parseURL(props, a);

        System.out.println(props.getProperty("FACTORY"));
        System.out.println(props.getProperty("URL"));
        System.out.println(props.getProperty("DATASOURCE"));
    }

    private static void parseURL(Properties props, String url) {
        if ((url != null) && (url.length() > 0)) {
            StringTokenizer QueryParams = new StringTokenizer(url, ";");

            while (QueryParams.hasMoreTokens()) {
                StringTokenizer VP = new StringTokenizer(QueryParams.nextToken(), "=");

                String Param = "";

                if (VP.hasMoreTokens()) {
                    Param = VP.nextToken();
                }

                String Value = "";

                if (VP.hasMoreTokens()) {
                    Value = VP.nextToken();
                }

                if ((Value.length() > 0) && (Param.length() > 0)) {
                    props.put(Param, Value);
                }
            }
        }
    }
}
