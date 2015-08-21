package jatools.designer.data;

import jatools.data.reader.sql.Connection;
import jatools.designer.App;
import jatools.swingx.wizard.WizardCellEditor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.3 $
 * @author $author$
 */
public class ConnectionPanel extends JPanel implements WizardCellEditor {

    private JComboBox suggestionCList;
    JTextField driverText = new JTextField();
    JTextField urlText = new JTextField();
    private JTextField userText = new JTextField();
    private JTextField passwordText = new JPasswordField();
    Connection lastInfo;
    transient private ArrayList changeListeners;

    /**
     * Creates a new JDBCConfigurePane object.
     */
    public ConnectionPanel() {
        buildUI();
    }

    /**
     * DOCUMENT ME!
     *
     * @param driver
     *            DOCUMENT ME!
     */
    public void setDriver(String driver) {
        driverText.setText(driver);

//        for (int i = 0; i < suggestionCList.getItemCount(); i++) {
//            if (((ZJdbcDriver) suggestionCList.getItemAt(i)).getDriver().equals(driver)) {
//                suggestionCList.setSelectedIndex(i);
//
//                break;
//            }
//        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param url
     *            DOCUMENT ME!
     */
    public void setUrl(String url) {
        urlText.setText(url);
    }

    /**
     * DOCUMENT ME!
     *
     * @param password
     *            DOCUMENT ME!
     */
    public void setPassword(String password) {
        passwordText.setText(password);
    }

    /**
     * DOCUMENT ME!
     *
     * @param user
     *            DOCUMENT ME!
     */
    public void setUser(String user) {
        userText.setText(user);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getEditorComponent() {
        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    public boolean isChanged() {
        Connection info = new Connection( driverText.getText(), urlText.getText(),
                userText.getText(), passwordText.getText());

        return !info.equals(lastInfo);
    }

    /**
     * DOCUMENT ME!
     */
    public void applyChange() {
        lastInfo = new Connection( driverText.getText(), urlText.getText(),
                userText.getText(), passwordText.getText());
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    public void checkAvailable() throws Exception {
        Connection info = new Connection( driverText.getText(), urlText.getText(),
                userText.getText(), passwordText.getText());

        info.getConnection();
    }

    /**
     * DOCUMENT ME!
     *
     * @throws IllegalStateException
     *             DOCUMENT ME!
     */
    public void validateInput() throws IllegalStateException {
        if (driverText.getText().trim().equals("")) { //
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    public Object getContent() throws Exception {
        Connection info = new Connection( driverText.getText(), urlText.getText(),
                userText.getText(), passwordText.getText());

        return info;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDriver() {
        return driverText.getText();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getURL() {
        return urlText.getText();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getUser() {
        return userText.getText();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPassword() {
        return passwordText.getText();
    }

    /**
     * DOCUMENT ME!
     */
    private void buildUI() {
        JLabel suggestionLabel = new JLabel(App.messages.getString("res.73")); //
        JLabel driverLabel = new JLabel("Driver:"); //
        JLabel urlLabel = new JLabel("URL:"); //
        JLabel userLabel = new JLabel(App.messages.getString("res.74")); //
        JLabel passwordLabel = new JLabel(App.messages.getString("res.75")); //

        //  driverLabel.setPreferredSize(new Dimension(526, 20));
        //  driverLabel.setMaximumSize(driverLabel.getPreferredSize() );
        setLayout(new GridBagLayout());
        setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        //Driver Suggestions:
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = 1;

        Insets old = gbc.insets;

        gbc.insets = new Insets(0, 0, 0, 14);
        add(suggestionLabel, gbc);
        gbc.gridwidth = 1;
        gbc.insets = old;
        suggestionCList = new JComboBox();
//        suggestionCList.addItemListener(new ItemListener() {
////                public void itemStateChanged(ItemEvent e) {
////                    ZJdbcDriver driver = (ZJdbcDriver) e.getItem();
////                    driverText.setText(driver.getDriver());
////                    urlText.setText(driver.getUrl());
////                    userText.setText(driver.getUser());
////                    passwordText.setText(driver.getPassword());
////                    urlText.requestFocus();
////                }
//            });
        gbc.weightx = 1.0f;
        add(suggestionCList, gbc);
        gbc.weightx = 0f;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        JButton prelist = new JButton("...");
        prelist.setToolTipText(App.messages.getString("res.453"));
        prelist.setPreferredSize(new Dimension(24, 24));
        add(prelist, gbc);

        prelist.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                }
            });

        //JDBC Driver:
        gbc.gridwidth = 1;
        add(driverLabel, gbc);
        gbc.weightx = 1.0f;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(driverText, gbc);
        gbc.weightx = 0.0f;

        //Database URL:
        gbc.gridwidth = 1;
        add(urlLabel, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(urlText, gbc);

        //User Name:
        gbc.gridwidth = 1;
        add(userLabel, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(userText, gbc);

        //Password:
        gbc.gridwidth = 1;
        add(passwordLabel, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(passwordText, gbc);

        Connection databaseInfo = new Connection();
        driverText.setText(databaseInfo.getDriver());
        urlText.setText(databaseInfo.getUrl());
        userText.setText(databaseInfo.getUser());
        passwordText.setText(databaseInfo.getPassword());

        // this.setPreferredSize(new Dimension(400, 300));
    }

    /**
     * DOCUMENT ME!
     *
     * @param l
     *            DOCUMENT ME!
     */
    public synchronized void removeChangeListener(ChangeListener l) {
        if ((changeListeners != null) && changeListeners.contains(l)) {
            ArrayList v = (ArrayList) changeListeners.clone();
            v.remove(l);
            changeListeners = v;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param driver
     *            DOCUMENT ME!
     */
    public void add(ArrayList drivers) {
//        String def = ZUserX.getInstance().getDefaultJdbcDriver();
//        int index = -1;
//
//        for (int i = 0; i < drivers.size(); i++) {
//            suggestionCList.addItem(drivers.get(i));
//
//            if (((ZJdbcDriver) drivers.get(i)).getName().equals(def)) {
//                index = i;
//            }
//        }
//
//        if (index != -1) {
//            suggestionCList.setSelectedIndex(index);
//        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param l
     *            DOCUMENT ME!
     */
    public synchronized void addChangeListener(ChangeListener l) {
        ArrayList v = (changeListeners == null) ? new ArrayList(2) : (ArrayList) changeListeners.clone();

        if (!v.contains(l)) {
            v.add(l);
            changeListeners = v;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e
     *            DOCUMENT ME!
     */
    protected void fireStateChanged(ChangeEvent e) {
        if (changeListeners != null) {
            ArrayList listeners = changeListeners;
            int count = listeners.size();

            for (int i = 0; i < count; i++) {
                ((ChangeListener) listeners.get(i)).stateChanged(e);
            }
        }
    }
}
