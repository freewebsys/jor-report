package jatools.resources;

import jatools.engine.ProtectClass;

import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class Messages implements ProtectClass {
    private static Logger logger = Logger.getLogger("ZMessages");
    public static ResourceBundle dictionary;

    /**
     * DOCUMENT ME!
     *
     * @param mid DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getString(String mid) {
        if (dictionary == null) {
            dictionary = loadDictionary();
        }

        return dictionary.getString(mid);
    }

    private static ResourceBundle buildEnglishResources() {
        return null;
    }

  
    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        buildEnglishResources();
    }

    private static ResourceBundle loadDictionary() {
        PropertyResourceBundle bundle = (PropertyResourceBundle) PropertyResourceBundle.getBundle("jatools.resources.resources",
                Locale.ENGLISH);

        return bundle;
    }
}
