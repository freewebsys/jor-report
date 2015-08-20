package jatools.designer;



import jatools.ReportDocument;
import jatools.accessor.ProtectPublic;
import jatools.component.Component;
import jatools.designer.action.OpenMruAction;
import jatools.designer.config.Configuration;
import jatools.designer.config.MruManager;
import jatools.designer.config.SplashWindow;
import jatools.engine.System2;
import jatools.engine.script.SimpleScript;
import jatools.resources.Messages;
import jatools.util.Util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.plaf.ColorUIResource;
import javax.swing.plaf.FontUIResource;

import org.apache.log4j.Logger;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class App implements ProtectPublic {
    private static Logger logger = Logger.getLogger("A");
    public static MruManager mruManager = new MruManager(5, new OpenMruAction());
    public static Configuration configuration;
    private static SplashWindow splash;
    public static Messages messages;

    static {
        String jre = System.getProperty("java.version");

        if ("1.4.1".compareTo(jre) > 0) {
            JOptionPane.showMessageDialog(null,
                App.messages.getString("res.69") + jre + App.messages.getString("res.70"), App.messages.getString("res.71"),
                JOptionPane.ERROR_MESSAGE);

            throw new RuntimeException("invalid jre version");
        }

        Component.defaultDataProvider = new SimpleScript() {
                    public Object get(String dataName) {
                        return Main.getInstance().getActiveEditor().getDocument()
                                   .getVariable(dataName);
                    }

                    public void set(String data, Object value) {
                    }

                    public Object eval(String beforePrint) {
                        return null;
                    }
                };
    }

    /**
     * DOCUMENT ME!
     *
     * @param working_dir DOCUMENT ME!
     */
    public static void init(String working_dir) {
        System2.setWorkingDirectory(working_dir);
        System2.setLog4jProperties();

        loadMru();
    }

    private static void loadMru() {
        MruManager mruManager = getMruManager();

        Configuration configuration = Configuration.getInstance();

        ArrayList mru = configuration.getMru();

        for (int i = mru.size() - 1; i >= 0; i--) {
            mruManager.open((String) mru.get(i));
        }

        mruManager.addChangeListener(configuration);
        App.setConfiguration(configuration);
    }

    /**
     * DOCUMENT ME!
     *
     * @param msg DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String localize(String msg) {
        String lm = Messages.getString(msg);

        return (lm == null) ? msg : lm;
    }

    /**
     * DOCUMENT ME!
     *
     * @param font DOCUMENT ME!
     */
    public static void setUIFont(javax.swing.plaf.FontUIResource font) {
        java.util.Enumeration keys = UIManager.getDefaults().keys();

        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);

            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        String working_dir = System.getProperty("user.dir");
        boolean nosplash = false;
        boolean nolookandfeel = false;

        String doc = null;

        if ((args != null) && (args.length > 0)) {
            for (int i = 0; i < args.length; i++) {
                if (args[i].startsWith("-nosplash")) {
                    nosplash = true;
                } else if (args[i].startsWith("-nolookandfeel")) {
                    nolookandfeel = true;
                } else if (args[i].startsWith("-working_dir:")) {
                    working_dir = args[i].substring("-working_dir:".length());
                } else if (args[i].startsWith("-doc:")) {
                    doc = args[i].substring("-doc:".length());
                }
            }
        }

        System2.setUserPath(working_dir);

        if (!nolookandfeel) {
           // setLookAndFeel();
        }

        loadMru();

        if (!nosplash) {
            splash = new SplashWindow(Util.getIcon("/jatools/icons/splash.jpg"));
        }

        JDialog chooser = null;
        Main m = null;

        if (doc != null) {
            try {
                m = (Main) newFrame(ReportDocument.load(new File(doc)), true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            m = (Main) newFrame(null, true);
        }

        m.show();

        if (!nosplash) {
            splash.setVisible(false);
        }

        if (chooser != null) {
            chooser.show();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param doc DOCUMENT ME!
     * @param queryAsClosed DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Frame newFrame(ReportDocument doc, boolean queryAsClosed) {
        Main mainFrame = new Main();
  

        mainFrame.pack();
        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);

        if (doc != null) {
            mainFrame.createEditor(doc, ReportDocument.getCachedFile(doc).getName(),
                ReportDocument.getCachedFile(doc).getAbsolutePath(),true);
        }

        return mainFrame;
    }

    private static void setLookAndFeel() {
        Font font = new Font("Dialog", 0, 12);
        setUIFont(new FontUIResource(font));

        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");

            Object menuBackground = UIManager.get("Button.background");
            UIManager.put("Menu.background", menuBackground);
            UIManager.put("MenuItem.background", menuBackground);
            UIManager.put("MenuBar.background", menuBackground);
            UIManager.put("CheckBoxMenuItem.background", menuBackground);
            UIManager.put("ToolTip.background", new ColorUIResource(new Color(217, 225, 247)));
            UIManager.put("ToolTip.font", new Font("DialogInput", 0, 12));
            UIManager.put("OptionPane.messageFont", font);
            UIManager.put("OptionPane.buttonFont", font);
        } catch (Exception e) {
            Util.debug(logger, e.getMessage());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Configuration getConfiguration() {
        return configuration;
    }

    /**
     * DOCUMENT ME!
     *
     * @param configuration DOCUMENT ME!
     */
    public static void setConfiguration(Configuration configuration) {
        App.configuration = configuration;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static MruManager getMruManager() {
        return mruManager;
    }
}
