package jatools.designer.config;

import jatools.accessor.AutoAccessor;
import jatools.designer.App;
import jatools.util.Util;
import jatools.xml.XmlReader;
import jatools.xml.XmlWriter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.apache.log4j.Logger;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class UserX extends AutoAccessor {
    private static Logger logger = Logger.getLogger("ZUserX");
    private static String USERX_XML = "userx.xml";
    private static UserX instance;
    boolean showOpenDialogOnStartup;
    String defaultUIFont = App.messages.getString("res.21");
    String defaultJdbcDriver;
    String defaultLookAndFeel;
    ArrayList lookAndFeels = new ArrayList();
    ArrayList predefinedJdbcDrivers = new ArrayList();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDefaultLookAndFeel() {
        return defaultLookAndFeel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param defaultLookAndFeel DOCUMENT ME!
     */
    public void setDefaultLookAndFeel(String defaultLookAndFeel) {
        this.defaultLookAndFeel = defaultLookAndFeel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDefaultJdbcDriver() {
        return defaultJdbcDriver;
    }

    /**
     * DOCUMENT ME!
     *
     * @param defaultJdbcDriver DOCUMENT ME!
     */
    public void setDefaultJdbcDriver(String defaultJdbcDriver) {
        this.defaultJdbcDriver = defaultJdbcDriver;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDefaultUIFont() {
        return defaultUIFont;
    }

    /**
     * DOCUMENT ME!
     *
     * @param defaultUIFont DOCUMENT ME!
     */
    public void setDefaultUIFont(String defaultUIFont) {
        this.defaultUIFont = defaultUIFont;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isShowOpenDialogOnStartup() {
        return showOpenDialogOnStartup;
    }

    /**
     * DOCUMENT ME!
     *
     * @param showOpenDialogOnStartup DOCUMENT ME!
     */
    public void setShowOpenDialogOnStartup(boolean showOpenDialogOnStartup) {
        this.showOpenDialogOnStartup = showOpenDialogOnStartup;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ArrayList getLookAndFeels() {
        return lookAndFeels;
    }

    /**
     * DOCUMENT ME!
     *
     * @param lookAndFeels DOCUMENT ME!
     */
    public void setLookAndFeels(ArrayList lookAndFeels) {
        this.lookAndFeels = lookAndFeels;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ArrayList getPredefinedJdbcDrivers() {
        return predefinedJdbcDrivers;
    }

    /**
     * DOCUMENT ME!
     *
     * @param predefinedJdbcDrivers DOCUMENT ME!
     */
    public void setPredefinedJdbcDrivers(ArrayList predefinedJdbcDrivers) {
        this.predefinedJdbcDrivers = predefinedJdbcDrivers;
    }

    /**
     * DOCUMENT ME!
     *
     * @param filePath DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static UserX load(String filePath) {
        UserX ux = null;

        try {
            FileInputStream is = new FileInputStream(filePath);
            ux = (UserX) XmlReader.read(is);
            is.close();
        } catch (FileNotFoundException e) {
            Util.debug(logger, App.messages.getString("res.558") + filePath);
        } catch (Exception e) {
            Util.debug(logger, App.messages.getString("res.559") + Util.toString(e));
        }

        return ux;
    }

   

    /**
     * DOCUMENT ME!
     *
     * @param ux DOCUMENT ME!
     */
    public static void write(UserX ux) {
        try {
            FileOutputStream fo = new FileOutputStream(USERX_XML);
            XmlWriter.write(ux, fo);
            fo.close();
        } catch (Exception ex) {
            Util.debug(logger, App.messages.getString("res.560") + USERX_XML + "," + Util.toString(ex));
        }
    }


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static UserX getInstance() {
        if (instance == null) {
            instance = load(USERX_XML);
        }

        return instance;
    }
}
