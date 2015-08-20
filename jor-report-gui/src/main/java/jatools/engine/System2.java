package jatools.engine;

import jatools.util.Util;

import org.apache.log4j.PropertyConfigurator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.util.PropertyResourceBundle;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class System2 {
    public static String csd = "S";
    public static final String LOG4J_PROPERTY = "log4j.properties";
    public static final String JATOOLS_PROPERTY = "jatools.properties";
    public static String workingDirectory;
    public static PropertyResourceBundle properties;

    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getProperty(String prop) {
        if (properties == null) {
            try {
                InputStream is = null;

                String wifile = getWorkingDirectory() + "WEB-INF" + File.separator +
                    JATOOLS_PROPERTY;

                if (new File(wifile).exists()) {
                    is = new FileInputStream(wifile);
                } else {
                    is = new FileInputStream(getWorkingDirectory() + JATOOLS_PROPERTY);
                }

                properties = loadProperties(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        try {
            prop = properties.getString(prop);

            return prop;
        } catch (Exception ex) {
            return null;
        }
    }

    private static PropertyResourceBundle loadProperties(InputStream is) {
        try {
            PropertyResourceBundle en_pr = new PropertyResourceBundle(is);

            return en_pr;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param property DOCUMENT ME!
     */
    public static void setWorkingDirectory(String property) {
        if (!property.endsWith("/") && !property.endsWith("\\")) {
            property += File.separator;
        }

        workingDirectory = property;

        getProperty("company");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getWorkingDirectory() {
        if (workingDirectory == null) {
            workingDirectory = System.getProperty("user.dir") + File.separator;
        }

        return workingDirectory;
    }

    /**
     * DOCUMENT ME!
     *
     * @param path DOCUMENT ME!
     */
    public static void setUserPath(String path) {
        setWorkingDirectory(path);
        csd = "C";
    }

    /**
     * DOCUMENT ME!
     */
    public static void setLog4jProperties() {
        PropertyConfigurator.configure(Util.class.getResource("/com/jatools/resources/" +
                LOG4J_PROPERTY));
    }

    /**
     * DOCUMENT ME!
     *
     * @param path DOCUMENT ME!
     */
    public static void setServletPath2(String path) {
        setWorkingDirectory(path);
        csd = "S";
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isDesignTime() {
        return csd.equals("C");
    }
}
