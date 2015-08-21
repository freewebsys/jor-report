/*
 * Created on 2004-7-8
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jatools.designer.data;

import jatools.data.reader.sql.Connection;
import jatools.designer.App;
import jatools.designer.config.UserX;
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
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;



/**
 * @author java
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ConnectionEditor extends JDialog {
    private ConnectionPanel connPane;
    private boolean success;
    private JTextField nameText;
    private Connection connection;

    /**
     * Creates a new ZConnectionEditor object.
     *
     * @param owner DOCUMENT ME!
     * @param connection DOCUMENT ME!
     */
    public ConnectionEditor(Frame owner, Connection connection) {
        super(owner, (connection != null) ? App.messages.getString("res.447") : App.messages.getString("res.448"), true); // //$NON-NLS-2$

        JPanel namePane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel nameLabel = new JLabel(App.messages.getString("res.90")); //
        nameLabel.setPreferredSize(new Dimension(56, 20));
        gbc.fill = GridBagConstraints.BOTH;
        namePane.add(nameLabel, gbc);
        gbc.weightx = 100.0f;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        nameText = new JTextField();

        namePane.add(nameText, gbc);
        gbc.weightx = 0.0f;
        gbc.gridwidth = 1;
        namePane.setBorder(BorderFactory.createEmptyBorder(10, 5, 0, 5));
      //  getContentPane().add(namePane, BorderLayout.NORTH);
        connPane = new ConnectionPanel();
        connPane.add(UserX.getInstance().getPredefinedJdbcDrivers());
        getContentPane().add(connPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        JButton testButton = new JButton(App.messages.getString("res.81")); //
        testButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    test();
                }
            });
        buttonPanel.add(testButton);
        JButton okButton = new JButton(App.messages.getString("res.3")); //
        okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    done();
                }
            });

        buttonPanel.add(okButton);

        JButton cancelButton = new JButton(App.messages.getString("res.4")); //
        cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cancel();
                }
            });

        buttonPanel.add(cancelButton);

        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        if (connection != null) {
            init(connection);
        }

        pack();
        setSize(new Dimension(370, 226));
        this.setLocationRelativeTo(owner);
    }

    /**
     * Creates a new ZConnectionEditor object.
     *
     * @param owner DOCUMENT ME!
     */
    public ConnectionEditor(Frame owner) {
        this(owner, null);
    }

    /**
         *
         */
    protected void test() {
    	if(validateFields())
    	{
    		
    	   try {
			connection.getConnection() ;
			MessageBox.show( this,App.messages.getString("res.82"),App.messages.getString("res.449")); // //$NON-NLS-2$
		} catch (Exception e) {
			 MessageBox.error(this,e.getMessage() );
			 
			 
		}

     
    	}
    }

    /**
     * @param name
     * @param connection2
     */
    private void init(Connection connection2) {
      
        connPane.setDriver(connection2.getDriver());
        connPane.setUrl(connection2.getUrl());
        connPane.setPassword(connection2.getPassword());
        connPane.setUser(connection2.getUser());
    }

    /**
     *
     */
    protected void cancel() {
        hide();
    }

    /**
     *
     */
    protected void done() {
        if (validateFields()) {
            hide();
            success = true;
        }
    }

    /**
     * @return
     */
    private boolean validateFields() {
        String name = nameText.getText();

        String driver = connPane.getDriver();
        String url = connPane.getURL();
        String pwd = connPane.getPassword();
        String user = connPane.getUser();

//        if ((name == null) || name.trim().equals("")) //
//         {
//            ZMessageBox.error(this, Z"数据集名称不能为空."); //
//            nameText.requestFocus();
//
//            return false;
//        }

        if ((driver == null) || driver.trim().equals("")) //
         {
            MessageBox.error(this, App.messages.getString("res.450")); //
            connPane.driverText.requestFocus();

            return false;
        }

        if ((url == null) || url.trim().equals("")) //
         {
            MessageBox.error(this, App.messages.getString("res.451")); //
            connPane.urlText.requestFocus();

            return false;
        }

        connection = new Connection();

        connection.setDriver(driver);
        connection.setUrl(url);
        connection.setUser(user);
        connection.setPassword(pwd);

        return true;
    }

    /**
     * @return Returns the connection.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * @return Returns the success.
     */
    public boolean isSuccess() {
        return success;
    }


}
