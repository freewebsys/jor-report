package jatools.designer.config;

import jatools.designer.App;
import jatools.util.Util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class SplashWindow extends JWindow {
    /**
     * Creates a new SplashWindow object.
     *
     * @param icon DOCUMENT ME!
     */
    public SplashWindow(Icon icon) {
        super(getDummyFrame());//, App.messages.getString("res.63"));

        JLabel l = new JLabel(icon);
        getContentPane().add(l, BorderLayout.CENTER);
        pack();

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension labelSize = l.getPreferredSize();
        setLocation((screenSize.width / 2) - (labelSize.width / 2),
            (screenSize.height / 2) - (labelSize.height / 2));

        setVisible(true);
    }

    static JFrame getDummyFrame() {
        JFrame dummy = new JFrame();
        dummy.setIconImage(Util.getIcon("/jatools/icons/logo.png").getImage());

        return dummy;
    }
}
