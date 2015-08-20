package jatools.designer;

import jatools.data.reader.sql.Connection;
import jatools.swingx.DropDownButton;
import jatools.swingx.MessageBox;
import jatools.util.Util;
import jatools.xml.XmlReader;
import jatools.xml.XmlWriter;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToolBar;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class ConnectionPanel {
    static JFileChooser chooser;
    static final String DEFAULT_SUFFIX = App.messages.getString("res.72");
    static final String JDBC_PROVIDERS_CONFIG = "jdbcproviders.xml";
    static JdbcProvider[] jdbcProviders;
    private JComboBox suggestionsCombo;
    JTextField driverText = new JTextField();
    JTextField urlText = new JTextField();
    private JTextField userText = new JTextField();
    private JTextField passwordText = new JPasswordField();
    private JPanel owner;
    private boolean connectable;
    private ActionListener testListener;
    private boolean dirty = true;
    
    

    /**
     * Creates a new JDBCConfigurePane object.
     */
    public ConnectionPanel(JPanel owner, GridBagConstraints gbc) {
        JLabel suggestionLabel = new JLabel(App.messages.getString("res.73")); //
        JLabel driverLabel = new JLabel("Driver:"); //
        JLabel urlLabel = new JLabel("URL:"); //
        JLabel userLabel = new JLabel(App.messages.getString("res.74")); //
        JLabel passwordLabel = new JLabel(App.messages.getString("res.75")); //
        gbc.gridwidth = 1;
        owner.add(suggestionLabel, gbc);
        gbc.gridwidth = 1;

        suggestionsCombo = new JComboBox(getJdbcProviders());
        suggestionsCombo.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Connection conn = (Connection) suggestionsCombo.getSelectedItem();

                    if (conn != null) {
                        setConnection(conn);
                        urlText.requestFocus();
                    }

                    setConnectable(false);
                }
            });
        gbc.weightx = 1.0f;
        owner.add(suggestionsCombo, gbc);
        gbc.weightx = 0f;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JToolBar bar = new JToolBar();
        DropDownButton ddb = new DropDownButton(null, Util.getIcon("/jatools/icons/tree.gif"));
        ddb.setPreferredSize(new Dimension(23, 23));

        JMenuItem mi = new JMenuItem(App.messages.getString("res.76"));
        mi.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    addToFavourites();
                }
            });
        ddb.getPopupMenu().add(mi);
        mi = new JMenuItem(App.messages.getString("res.77"));
        mi.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    removeFromFavourites();
                }
            });

        ddb.getPopupMenu().add(mi);

        mi = new JMenuItem(App.messages.getString("res.78"));
        mi.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setDefault();
                }
            });
        ddb.getPopupMenu().add(mi);
        ddb.getPopupMenu().addSeparator();

        mi = new JMenuItem(App.messages.getString("res.79"));
        mi.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    exportProviders();
                }
            });
        ddb.getPopupMenu().add(mi);

        mi = new JMenuItem(App.messages.getString("res.80"));
        mi.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    importProviders();
                }
            });
        ddb.getPopupMenu().add(mi);

        bar.add(ddb);

        //        
        //        bar.add(new FixedSizeButton(Util.getIcon("/jatools/icons/additem.gif"), null, 22, 22));
        //        bar.add(new FixedSizeButton(Util.getIcon("/jatools/icons/removeitem.gif"), null, 22, 22));
        // bar.putClientProperty("JToolBar.isRollover", Boolean.TRUE);
        bar.setPreferredSize(new Dimension(30, 23));

        bar.setFloatable(false);

        Insets oi = gbc.insets;
        gbc.insets = new Insets(0, 1, 0, 1);
        owner.add(bar, gbc);
        gbc.insets = oi;
        //JDBC Driver:
        gbc.gridwidth = 1;
        owner.add(driverLabel, gbc);
        gbc.weightx = 1.0f;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        owner.add(driverText, gbc);
        gbc.weightx = 0.0f;

        //Database URL:
        gbc.gridwidth = 1;
        owner.add(urlLabel, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        owner.add(urlText, gbc);

        //User Name:
        gbc.gridwidth = 1;
        owner.add(userLabel, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.NONE;
        userText.setPreferredSize(new Dimension(100, 23));
        owner.add(userText, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //Password:
        gbc.gridwidth = 1;
        owner.add(passwordLabel, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        gbc.fill = GridBagConstraints.NONE;
        passwordText.setPreferredSize(new Dimension(100, 23));
        owner.add(passwordText, gbc);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // test connection button
        gbc.gridwidth = 1;
        owner.add(Box.createVerticalStrut(10));
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        //  gbc.fill = GridBagConstraints.VERTICAL;
        bar = new JToolBar();
        bar.addSeparator();

        JButton b = new JButton(App.messages.getString("res.81"), Util.getIcon("/jatools/icons/conn.gif"));
        b.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    testConnection();
                }
            });

        bar.add(b);

        bar.setFloatable(false);

        owner.add(bar, gbc);

        enableDefault();
        this.owner = owner;
    }

    protected void setConnectable(boolean b) {
        this.connectable = b;

        if (this.testListener != null) {
            this.testListener.actionPerformed(null);
        }
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public static ConnectionPanel getInstance() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.WEST;

        return new ConnectionPanel(p, gbc);
    }

    protected void testConnection() {
        try {
            getConnection().getConnection();
            MessageBox.show(this.urlText.getTopLevelAncestor(), App.messages.getString("res.82"), App.messages.getString("res.83"));
            this.setConnectable(true);
        } catch (Exception e) {
            //e.printStackTrace() ;
            MessageBox.error(null, App.messages.getString("res.84"), e);
            this.setConnectable(false);
        }
    }

    protected void importProviders() {
        if (chooser == null) {
            chooser = new JFileChooser();
        }

        chooser.showOpenDialog(this.passwordText.getTopLevelAncestor());

        File file = chooser.getSelectedFile();

        if (file != null) {
            JdbcProvider[] providers = null;

            try {
                providers = getJdbcProviders(file);
            } catch (Exception e) {
                MessageBox.error(this.passwordText.getTopLevelAncestor(), App.messages.getString("res.85"));
            }

            if (providers.length > 0) {
                
                for (int i = 0; i < providers.length; i++) {
                    String name = providers[i].getName();

                    if (name.endsWith(DEFAULT_SUFFIX)) {
                        providers[i].setName(name.substring(1,
                                name.length() - DEFAULT_SUFFIX.length()));
                    }
                }

                JdbcProvider[] _temp = new JdbcProvider[jdbcProviders.length + providers.length];
                System.arraycopy(jdbcProviders, 0, _temp, 0, jdbcProviders.length);
                System.arraycopy(providers, 0, _temp, jdbcProviders.length, providers.length);
                jdbcProviders = _temp;

                refreshSugguestion();
                this.suggestionsCombo.setSelectedIndex(0);
                updateProviders();
                MessageBox.show(this.passwordText.getTopLevelAncestor(), App.messages.getString("res.82"),
                    App.messages.getString("res.86") + file.getAbsolutePath() + App.messages.getString("res.87") + providers.length + App.messages.getString("res.88"));
            }
        }
    }

    protected void exportProviders() {
        if (chooser == null) {
            chooser = new JFileChooser();
        }

        chooser.showSaveDialog(this.passwordText.getTopLevelAncestor());

        File file = chooser.getSelectedFile();

        if (file != null) {
            saveProviders(file);
            MessageBox.show(this.passwordText.getTopLevelAncestor(), App.messages.getString("res.82"),
                App.messages.getString("res.89") + file.getAbsolutePath());
        }
    }

    private void enableDefault() {
        for (int i = 0; i < jdbcProviders.length; i++) {
            if (jdbcProviders[i].getName().endsWith(DEFAULT_SUFFIX)) {
                suggestionsCombo.setSelectedIndex(i);

                break;
            }
        }
    }

    protected void setDefault() {
        int index = this.suggestionsCombo.getSelectedIndex();

        if (index > -1) {
            setDefault(index);
            updateProviders();
        }
    }

    protected void removeFromFavourites() {
        int index = this.suggestionsCombo.getSelectedIndex();

        if (index > -1) {
            JdbcProvider provider = (JdbcProvider) this.suggestionsCombo.getSelectedItem();
            JdbcProvider[] providers = new JdbcProvider[jdbcProviders.length - 1];
            System.arraycopy(jdbcProviders, 0, providers, 0, index);
            System.arraycopy(jdbcProviders, index + 1, providers, index, providers.length - index);
            jdbcProviders = providers;

            if ((jdbcProviders.length > 0) && provider.getName().endsWith(DEFAULT_SUFFIX)) {
                
                setDefault(0);
            }

            refreshSugguestion();
            this.suggestionsCombo.setSelectedIndex(0);
            updateProviders();
        }
    }

    protected void addToFavourites() {
        String results = JOptionPane.showInputDialog(this.passwordText.getTopLevelAncestor(),
                App.messages.getString("res.90"), App.messages.getString("res.91"), JOptionPane.OK_CANCEL_OPTION);

        if (results != null) {
            JdbcProvider provider = new JdbcProvider(this.getConnection());
            provider.setName(results);

            JdbcProvider[] providers = new JdbcProvider[jdbcProviders.length + 1];
            System.arraycopy(jdbcProviders, 0, providers, 0, jdbcProviders.length);
            providers[providers.length - 1] = provider;
            jdbcProviders = providers;
            setDefault(jdbcProviders.length - 1);

            refreshSugguestion();
            this.suggestionsCombo.setSelectedIndex(jdbcProviders.length - 1);
            updateProviders();
        }
    }

    private void setDefault(int index) {
        for (int i = 0; i < jdbcProviders.length; i++) {
            String name = jdbcProviders[i].getName();

            if (i == index) {
                if (!name.endsWith(DEFAULT_SUFFIX)) {
                    jdbcProviders[i].setName(name + DEFAULT_SUFFIX);
                }
            } else {
                if (name.endsWith(DEFAULT_SUFFIX)) {
                    jdbcProviders[i].setName(name.substring(0,
                            name.length() - DEFAULT_SUFFIX.length()));
                }
            }
        }

        this.suggestionsCombo.repaint();
    }

    private void refreshSugguestion() {
        this.suggestionsCombo.removeAllItems();

        for (int i = 0; i < jdbcProviders.length; i++) {
            this.suggestionsCombo.addItem(jdbcProviders[i]);
        }
    }

    private void updateProviders() {
        saveProviders(new File(JDBC_PROVIDERS_CONFIG));
    }

    private static void saveProviders(File f) {
        try {
            FileOutputStream fos = new FileOutputStream(f);
            XmlWriter.write(jdbcProviders, fos);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Connection getConnection() {
        Connection conn = new Connection();
        conn.setDriver(driverText.getText());
        conn.setUrl(urlText.getText());
        conn.setUser(userText.getText());
        conn.setPassword(passwordText.getText());

        return conn;
    }

    /**
     * DOCUMENT ME!
     *
     * @param conn DOCUMENT ME!
     */
    public void setConnection(Connection conn) {
        driverText.setText(conn.getDriver());
        urlText.setText(conn.getUrl());
        userText.setText(conn.getUser());
        passwordText.setText(conn.getPassword());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static JdbcProvider[] getJdbcProviders() {
        if (jdbcProviders == null) {
            try {
                jdbcProviders = getJdbcProviders(new File(JDBC_PROVIDERS_CONFIG));
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (jdbcProviders == null) {
                jdbcProviders = new JdbcProvider[0];
            }
        }

        return jdbcProviders;
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static JdbcProvider[] getJdbcProviders(File f)
        throws Exception {
        JdbcProvider[] providers = new JdbcProvider[0];

        if (f.exists()) {
            FileInputStream fis = new FileInputStream(f);
            providers = (JdbcProvider[]) XmlReader.read(fis);
        }

        return providers;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JPanel getOwner() {
        return owner;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isConnectable() {
        return connectable;
    }

    /**
     * DOCUMENT ME!
     *
     * @param testListener DOCUMENT ME!
     */
    public void setTestListener(ActionListener testListener) {
        this.testListener = testListener;
    }

	public boolean isDirty() {
		return dirty;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}
}
