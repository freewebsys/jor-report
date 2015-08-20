package jatools.designer.export;

import jatools.designer.App;
import jatools.util.Util;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ProgressDialog extends JDialog {
    private ProgressFan pb;
    private JButton ok;
    private JButton openCommand;
    private JLabel prompt;
    File file;

    /**
     * Creates a new ProgressDialog object.
     *
     * @param frame DOCUMENT ME!
     * @param title DOCUMENT ME!
     * @param f DOCUMENT ME!
     */
    public ProgressDialog(Frame frame, String title, File f) {
        super(frame, App.messages.getString("res.442"), true);

        pb = new ProgressFan();

        Container contentPane = getContentPane();
        JPanel pp = new JPanel(new BorderLayout());

        pp.add(pb, BorderLayout.WEST);

        prompt = new JLabel(App.messages.getString("res.443"));

        pp.add(prompt, BorderLayout.CENTER);

        pp.setBorder(BorderFactory.createEtchedBorder());

        contentPane.add(pp, BorderLayout.CENTER);

        JPanel commands = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        openCommand = new JButton(App.messages.getString("res.444"));
        openCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    hide();

                    String path = "file:/" + file.getAbsolutePath().replace('\\', '/');
                    Util.open(path);
                }
            });
        commands.add(openCommand);
        ok = new JButton(App.messages.getString("res.3"));
        ok.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    hide();
                }
            });

        commands.add(openCommand);
        commands.add(ok);

        contentPane.add(commands, BorderLayout.SOUTH);

        setSize(350, 200);

        ok.setEnabled(false);
        openCommand.setEnabled(false);

        this.file = f;
        this.setLocationRelativeTo(frame);
    }

    /**
     * DOCUMENT ME!
     */
    public void done() {
        pb.stop();
        prompt.setText(App.messages.getString("res.445") + file.getName());
        ok.setEnabled(true);
        this.openCommand.setEnabled(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        ProgressDialog p = new ProgressDialog(null, "aaa", new File("d:/report.xls"));

        p.show();
    }
}
