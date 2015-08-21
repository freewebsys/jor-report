package jatools.swingx;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class SwingUtil {
    static Border border6 = new EmptyBorder(6, 6, 6, 6);

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param size DOCUMENT ME!
     */
    public static void setSize(JComponent c, Dimension size) {
        c.setPreferredSize(size);
        c.setMinimumSize(size);
        c.setMaximumSize(size);
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public static void setBorder6(JComponent c) {
        c.setBorder(border6);
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param b DOCUMENT ME!
     */
    public static void enabled(JComponent c, boolean b) {
        c.setEnabled(b);

        for (int i = 0; i < c.getComponentCount(); i++) {
            Component cc = c.getComponent(i);
            enabled((JComponent) cc, b);
        }
    }
}
