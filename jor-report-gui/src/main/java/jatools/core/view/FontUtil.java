package jatools.core.view;

import jatools.engine.System2;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class FontUtil {
    static Font defaultFont;
    static List fontList;

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static java.awt.Font getFont(Font f) {
        List list = getSystemFonts();

        if (!list.contains(f.getName())) { //系统没有安装宋体，用字体文件创建一个 

            Font _f = getDefault();

            if (_f != null) {
                f = _f.deriveFont(f.getStyle(), f.getSize());
            }
        }

        return f;
    }

    static List getSystemFonts() {
        if (fontList == null) {
            GraphicsEnvironment gEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
            String[] evnfonts = gEnv.getAvailableFontFamilyNames();
            fontList = Arrays.asList(evnfonts);
        }

        return fontList;
    }

    static Font getDefault() {
        if (defaultFont == null) {
            String fontDirs = System2.getProperty("font.dirs"); //
            String[] dirs = fontDirs.split(";"); //

            for (int i = 0; i < dirs.length; i++) {
                File fontFile = new File(dirs[i].trim(), "simsun.ttc");

                if (fontFile.exists()) {
                    try {
                        defaultFont = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT,
                                new FileInputStream(fontFile));
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (FontFormatException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    break;
                }
            }
        }

        return defaultFont;
    }
}
