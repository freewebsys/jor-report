package jatools.designer.data;

import jatools.data.reader.sql.Connection;
import jatools.designer.App;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ConnectionManager extends JDialog {
    JTable proxyTable;
    AbstractTableModel tableModel;
    ArrayList proxyContainer;
    private JButton editButton;
    private JButton removeButton;

    /**
     * Creates a new ConnectionManager object.
     *
     * @param owner DOCUMENT ME!
     * @param conns DOCUMENT ME!
     */
    public ConnectionManager(Frame owner, ArrayList conns) {
        super(owner, App.messages.getString("res.452"), true);
        this.proxyContainer = conns;
        buildUI();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent e) {
        repaint();
    }

    private void buildUI() {
        JPanel datasourcesPane = new JPanel(new BorderLayout());
        tableModel = (AbstractTableModel) this.prepareSourcesTableModel();
        proxyTable = new JTable(tableModel);
        proxyTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        proxyTable.setRowHeight(20);

        proxyTable.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (e.getClickCount() > 1) {
                        edit();
                    }
                }
            });

        JScrollPane scrollPane = new JScrollPane(proxyTable);
        datasourcesPane.add(scrollPane, BorderLayout.CENTER);

        JButton addButton = new JButton(App.messages.getString("res.93"));
        addButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if (add()) {
                        tableModel.fireTableStructureChanged();

                        int last = proxyTable.getRowCount() - 1;
                        proxyTable.getSelectionModel().setSelectionInterval(last, last);
                    }
                }
            });

        removeButton = new JButton(App.messages.getString("res.96"));
        removeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    remove();
                    tableModel.fireTableStructureChanged();
                }
            });

        editButton = new JButton(App.messages.getString("res.95"));
        editButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    edit();
                }
            });

        JButton ok = new JButton(App.messages.getString("res.3"));
        ok.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    hide();
                }
            });

        JPanel commandsPane = new JPanel(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 5, 2, 5);

        commandsPane.add(addButton, gbc);
        commandsPane.add(removeButton, gbc);
        commandsPane.add(editButton, gbc);

        gbc.insets = new Insets(20, 5, 2, 5);

        commandsPane.add(ok, gbc);

        JPanel p = new JPanel(new FlowLayout());
        p.add(commandsPane);

        datasourcesPane.add(p, BorderLayout.EAST);

        JPanel outPanel = new JPanel(new BorderLayout());
        outPanel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
        outPanel.add(datasourcesPane, BorderLayout.CENTER);

        this.getContentPane().add(outPanel);
        pack();
        setSize(400, 400);
    }

    private TableModel prepareSourcesTableModel() {
        TableModel tableModel = new AbstractTableModel() {
                public Object getValueAt(int row, int column) {
                    Connection proxy = (Connection) proxyContainer.get(row);

                    return null;
                }

                public int getRowCount() {
                    return proxyContainer.size();
                }

                public int getColumnCount() {
                    return 1;
                }

                public String getColumnName(int index) {
                    return ((index == 0) ? App.messages.getString("res.108") : null);
                }
            };

        return tableModel;
    }

    private boolean add() {
        ConnectionEditor editor = new ConnectionEditor((Frame) this.getOwner());
        editor.show();

        if (editor.isSuccess()) {
            proxyContainer.add(editor.getConnection());
        }

        return true;
    }

    private void edit() {
        int i = proxyTable.getSelectedRow();

        if (i >= 0) {
            Connection proxy = (Connection) proxyContainer.get(i);
            ConnectionEditor editor = new ConnectionEditor((Frame) this.getOwner(), proxy);
            editor.show();

            if (editor.isSuccess()) {
                proxyContainer.set(i, editor.getConnection());
            }
        }
    }

    private void remove() {
        int i = proxyTable.getSelectedRow();
        proxyContainer.remove(i);
    }
}
