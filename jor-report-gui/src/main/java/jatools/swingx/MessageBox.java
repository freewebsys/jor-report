package jatools.swingx;

import jatools.designer.App;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class MessageBox {
    public static int OK = JOptionPane.OK_OPTION;
    public static int CANCEL = JOptionPane.CANCEL_OPTION;
    public static int YES = JOptionPane.YES_OPTION;
    public static int NO = JOptionPane.NO_OPTION;
    public static int PLAINT = JOptionPane.PLAIN_MESSAGE;
    public static int ERROR = JOptionPane.ERROR_MESSAGE;
    public static int YES_NO = JOptionPane.YES_NO_OPTION;
    public static int YES_NO_CANCEL = JOptionPane.YES_NO_CANCEL_OPTION;
    public static int OK_CANCEL = JOptionPane.OK_CANCEL_OPTION;

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     * @param error DOCUMENT ME!
     */
    public static void error(Component owner, String error) {
      //  JPanel p = new JPanel(new FlowLayout() );

        JTextPane errText = new JTextPane(){public Dimension getPreferredSize()
        {
        	 Dimension dim = super.getPreferredSize();
             dim.width = Math.min(dim.width, 500);
             dim.height = Math.min(dim.height, 200);

             return dim;
        	
        }
        
        }
        ;
        errText.setText(error);
        errText.setMaximumSize( new Dimension(500,200));
        errText.setOpaque(false);
 //       p.add(new JScrollPane(errText));

        JOptionPane.showMessageDialog(SwingUtilities.getWindowAncestor(owner), errText, App.messages.getString("res.26"),
            JOptionPane.ERROR_MESSAGE);
    }

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     * @param title DOCUMENT ME!
     * @param message DOCUMENT ME!
     */
    public static void show(Component owner, String title, String message) {
        JOptionPane.showMessageDialog(owner, new JLabel(message), title,
            JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     * @param error DOCUMENT ME!
     * @param e DOCUMENT ME!
     */
    public static void error(JComponent owner, String error, Exception e) {
        final JTextArea textArea = new JTextArea();
        final JLabel label = new JLabel();
        label.setBorder(BorderFactory.createEmptyBorder(10, 0, 12, 0));

        final JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(textArea), BorderLayout.CENTER);
        p.add(label, BorderLayout.NORTH);
        label.setText(error);

        textArea.setEditable(false);

        StringWriter writer = new StringWriter();
        e.printStackTrace(new PrintWriter(writer));
        textArea.setText(writer.toString());
        textArea.setOpaque(false);

        p.setPreferredSize(new Dimension(350, 150));

        JOptionPane.showMessageDialog(owner, p, App.messages.getString("res.26"), JOptionPane.ERROR_MESSAGE);
    }

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     * @param title DOCUMENT ME!
     * @param message DOCUMENT ME!
     * @param option DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int show(Component owner, String title, String message, int option) {
        return JOptionPane.showConfirmDialog(owner, new JLabel(message), title, option,
            JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     * @param title DOCUMENT ME!
     * @param message DOCUMENT ME!
     * @param option DOCUMENT ME!
     * @param messageType DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int show(Component owner, String title, String message, int option,
        int messageType) {
        return JOptionPane.showConfirmDialog(owner, new JLabel(message), title, option, messageType);
    }
}
