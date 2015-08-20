package jatools.designer;

import jatools.data.reader.sql.Connection;
import jatools.data.reader.sql.SqlReader;
import jatools.designer.data.DatasetPreviewer;
import jatools.designer.data.NameChecker;
import jatools.designer.data.SqlTextPanel;
import jatools.swingx.CommandPanel;
import jatools.swingx.GridBagConstraints2;
import jatools.swingx.MessageBox;
import jatools.swingx.TitledSeparator;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class SqlReaderDialog extends JDialog {
    static JFileChooser chooser;
    JTextField nameText = new JTextField();
    ConnectionPanel connectionPane;
    SqlTextPanel sqltext;
    boolean exitOK;
    NameChecker checker;

    /**
     * Creates a new SqlReaderDialog object.
     *
     * @param title DOCUMENT ME!
     * @param owner DOCUMENT ME!
     * @param reader DOCUMENT ME!
     * @param checker DOCUMENT ME!
     */
    public SqlReaderDialog(String title, Frame owner, SqlReader reader, NameChecker checker) {
        super(owner, title, true);
        buildUI();
        pack();
        setSize(new Dimension(500, 580));
        setReader(reader);
        this.checker = checker;
        this.nameText.setName(reader.getName());
        this.setLocationRelativeTo(Main.getInstance());
    }

    private void setReader(SqlReader proxy) {
        nameText.setText(proxy.getName());

        Connection conn = proxy.getConnection();

        if (conn != null) {
            connectionPane.setConnection(conn);
        }

        sqltext.setText(proxy.getSql());
    }

    private void preview() {
        try {
            DatasetPreviewer preview = new DatasetPreviewer((Frame) getOwner());
            preview.setLocationRelativeTo(this);
            preview.setReader(this.getReader());
            preview.show();
        } catch (Exception ex) {
            ex.printStackTrace();
            MessageBox.error(this, ex.getMessage());
        }
    }

    private void buildUI() {
        JPanel north = new JPanel(new GridBagLayout());

        GridBagConstraints2 gbc = new GridBagConstraints2(north);

        gbc.insets = Global.GBC_INSETS;

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        north.add(new JLabel(App.messages.getString("res.90")), gbc);

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.add(nameText, 150);

        north.add(Box.createVerticalStrut(10), gbc);
        north.add(new TitledSeparator(App.messages.getString("res.185")), gbc);
        connectionPane = new ConnectionPanel(north, gbc);

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        north.add(Box.createVerticalStrut(10), gbc);
        north.add(new TitledSeparator(App.messages.getString("res.186")), gbc);
        gbc.gridwidth = 1;

        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        sqltext = new SqlTextPanel();

        JLabel label = new JLabel("SQL:");
        label.setVerticalAlignment(JLabel.TOP);
        north.add(label, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        sqltext.setPreferredSize(new Dimension(100, 150));
        gbc.weightx = 100.0f;

        north.add(sqltext, gbc);
        gbc.weightx = 0;
        gbc.weighty = 0.0;
        gbc.gridwidth = 1;
        north.add(Box.createVerticalStrut(10));

        JButton b = new JButton(App.messages.getString("res.187"));
        b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    preview();
                }
            });

        ActionListener lok = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    done();
                }
            };

        ActionListener cok = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    canel();
                }
            };

        CommandPanel cp = CommandPanel.createPanel();
        cp.addComponent(b);

        cp.addComponent(App.messages.getString("res.3"), lok);
        cp.addComponent(App.messages.getString("res.4"), cok);

        getContentPane().add(north, BorderLayout.CENTER);
        getContentPane().add(cp, BorderLayout.SOUTH);
    }

    protected void exportSQL() {
        if (chooser == null) {
            chooser = new JFileChooser();
        }

        chooser.showSaveDialog(this);

        File file = chooser.getSelectedFile();

        if (file != null) {
            try {
                FileWriter writer = new FileWriter(file);
                writer.write((String) sqltext.getContent());
                writer.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    protected void importSQL() {
        if (chooser == null) {
            chooser = new JFileChooser();
        }

        chooser.showOpenDialog(this);

        File file = chooser.getSelectedFile();

        if (file != null) {
            String record = null;

            StringBuffer sb = new StringBuffer();

            try {
                FileReader fr = new FileReader(file);
                BufferedReader br = new BufferedReader(fr);

                while ((record = br.readLine()) != null) {
                    sb.append(record + "\n");
                }

                br.close();
                fr.close();

                sqltext.setText(sb.toString());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    protected void pasteSQL() {
        Clipboard sysClipBoard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable contents = sysClipBoard.getContents(this);

        try {
            String text = (String) (contents.getTransferData(DataFlavor.stringFlavor));

            if (text != null) {
                sqltext.setText(text);
            }
        } catch (UnsupportedFlavorException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void done() {
        if (!isOriginalName() && (this.checker != null)) {
            try {
                this.checker.check(this.nameText.getText());
            } catch (Exception e) {
                MessageBox.error(this.getContentPane(), e.getMessage());
                nameText.requestFocus();
                nameText.selectAll();

                return;
            }
        }

        exitOK = true;
        hide();
    }

    boolean isOriginalName() {
        return (nameText.getName() != null) && nameText.getName().equals(nameText.getText());
    }

    protected void canel() {
        exitOK = false;
        hide();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SqlReader getReader() {
        Connection conn = connectionPane.getConnection();

        SqlReader reader = null;

        try {
            String sql = (String) sqltext.getContent();
            reader = new SqlReader(conn, sql);
            reader.setName(nameText.getText());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        reader.setConnection(conn);

        return reader;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isOK() {
        return exitOK;
    }
}
