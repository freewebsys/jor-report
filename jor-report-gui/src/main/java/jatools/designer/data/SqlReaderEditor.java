package jatools.designer.data;

import jatools.data.reader.sql.Connection;
import jatools.data.reader.sql.SqlReader;
import jatools.designer.App;
import jatools.swingx.Chooser;
import jatools.swingx.MessageBox;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class SqlReaderEditor extends JDialog implements Chooser {
    JTextField nameText;
    JTextField descriptionText;
    ConnectionPanel connectionPane;
    SqlTextPanel sqltext;
    boolean exitOK;
    NameChecker nameChecker;
    String oldName;

    private SqlReaderEditor(Frame owner) {
        super(owner, App.messages.getString("res.552"), true);
        buildUI(false);
        pack();
        setSize(new Dimension(545, 500));
    }

    /**
     * Creates a new SqlReaderEditor object.
     *
     * @param owner DOCUMENT ME!
     * @param proxy DOCUMENT ME!
     */
    public SqlReaderEditor(Frame owner, SqlReader proxy) {
        super(owner, App.messages.getString("res.552"), true);
        buildUI(true);
        pack();
        setSize(new Dimension(545, 500));
        setReader(proxy);
    }

    /**
     * DOCUMENT ME!
     *
     * @param proxy DOCUMENT ME!
     */
    public void setReader(SqlReader proxy) {
        if (proxy != null) {
            nameText.setText(proxy.getName());

            oldName = nameText.getText();
            descriptionText.setText(proxy.getDescription());

            Connection conn = proxy.getConnection();

            connectionPane.setDriver(conn.getDriver());
            connectionPane.setUrl(conn.getUrl());
            connectionPane.setUser(conn.getUser());
            connectionPane.setPassword(conn.getPassword());
            sqltext.setText(proxy.getSql());
        } else {
            nameText.setText(null);

            oldName = nameText.getText();
            descriptionText.setText(null);

            connectionPane.setDriver(null);
            connectionPane.setUrl(null);
            connectionPane.setUser(null);
            connectionPane.setPassword(null);
            sqltext.setText(null);
        }
    }

    private void dataPreview() {
        try {
            DatasetPreviewer preview = new DatasetPreviewer((Frame) getOwner());
            preview.setLocationRelativeTo(this);
            preview.setReader(this.getSqlReader());
            preview.show();
        } catch (Exception ex) {
            MessageBox.error(this, ex.getMessage());
        }
    }

    void setNameChecker(NameChecker nameChecker) {
        this.nameChecker = nameChecker;
    }

    private void buildUI(boolean needProperty) {
        connectionPane = new ConnectionPanel();

        JPanel north = new JPanel(new BorderLayout());

        if (needProperty) {
            JPanel propertyPane = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            JLabel nameLabel = new JLabel(App.messages.getString("res.90"));
            nameLabel.setPreferredSize(new Dimension(56, 20));
            gbc.fill = GridBagConstraints.BOTH;
            propertyPane.add(nameLabel, gbc);
            gbc.weightx = 100.0f;
            gbc.gridwidth = GridBagConstraints.REMAINDER;

            nameText = new JTextField();

            propertyPane.add(nameText, gbc);
            gbc.weightx = 0.0f;
            gbc.gridwidth = 1;

            propertyPane.add(new JLabel(App.messages.getString("res.534")), gbc);
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            descriptionText = new JTextField();
            propertyPane.add(descriptionText, gbc);
            propertyPane.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 5));
            north.add(propertyPane, BorderLayout.NORTH);
        }

        north.add(connectionPane, BorderLayout.CENTER);

        JPanel sqltextPane = new JPanel(new BorderLayout());

        sqltext = new SqlTextPanel();

        JLabel sqlLabel = new JLabel("SQL:");
        sqlLabel.setPreferredSize(new Dimension(56, 25));
        sqltextPane.add(sqlLabel, BorderLayout.WEST);
        sqltextPane.add(sqltext, BorderLayout.CENTER);
        sqltextPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 5));

        JPanel commandPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton b = new JButton(App.messages.getString("res.187"));
        b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dataPreview();
                }
            });

        commandPane.add(b);

        b = new JButton(App.messages.getString("res.352"));
        b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        if ((nameChecker != null) && !oldName.equals(nameText.getText())) {
                            nameChecker.check(nameText.getText());
                        }
                    } catch (Exception ex) {
                        MessageBox.error(SqlReaderEditor.this, ex.getMessage());
                        nameText.selectAll();
                        nameText.requestFocus();

                        return;
                    }

                    exitOK = true;
                    hide();
                }
            });

        commandPane.add(b);

        b = new JButton(App.messages.getString("res.553"));
        b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    exitOK = false;
                    hide();
                }
            });

        commandPane.add(b);
        commandPane.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        JPanel p = new JPanel(new BorderLayout());
        p.add(north, BorderLayout.NORTH);
        p.add(sqltextPane, BorderLayout.CENTER);

        getContentPane().add(p, BorderLayout.CENTER);
        getContentPane().add(commandPane, BorderLayout.SOUTH);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isExitOK() {
        return exitOK;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SqlReader getSqlReader() {
        String driver = (connectionPane.getDriver());
        String url = (connectionPane.getURL());
        String user = (connectionPane.getUser());
        String password = (connectionPane.getPassword());

        Connection connProxy = new Connection(url, user, password, null);

        SqlReader proxy = null;

        try {
            String sql = (String) sqltext.getContent();
            proxy = new SqlReader(connProxy, sql);
        } catch (Exception ex) {
        }

        if (nameText != null) {
            proxy.setName(nameText.getText());
        }

        if (descriptionText != null) {
            proxy.setDescription(descriptionText.getText());
        }

        proxy.setConnection(connProxy);

        return proxy;
    }

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean showChooser(JComponent owner, Object value) {
        exitOK = false;

        this.setReader((SqlReader) value);

        show();

        return exitOK;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue() {
        return getSqlReader();
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        new SqlReaderEditor(null).show();
    }
}
