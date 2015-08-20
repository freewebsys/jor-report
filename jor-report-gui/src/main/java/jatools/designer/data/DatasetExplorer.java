package jatools.designer.data;



import jatools.data.reader.DatasetReader;
import jatools.data.reader.sql.SqlReader;
import jatools.dataset.Dataset;
import jatools.designer.App;
import jatools.designer.config.DatasetReaderList;
import jatools.swingx.MessageBox;
import jatools.swingx.dnd.Selectable;
import jatools.util.Util;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.AbstractTableModel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class DatasetExplorer extends JPanel implements ChangeListener, NameChecker {
    public static final String NAME = App.messages.getString("res.533");
    JTextField proxySelector = new JTextField();
    JTextField descriptionText = new JTextField();
    JTable fieldsTable;
    DatasetReaderList proxyContainer;
    Dataset selection;

    /**
     * Creates a new DatasetExplorer object.
     *
     * @param showMore DOCUMENT ME!
     */
    public DatasetExplorer(boolean showMore) {
        buildUI(showMore);
    }

    /**
     * Creates a new DatasetExplorer object.
     */
    public DatasetExplorer() {
        this(true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param source DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void check(String source) throws Exception {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dataset getSelection() {
        return selection;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JTable getTable() {
        return fieldsTable;
    }

    /**
     * DOCUMENT ME!
     *
     * @param datasourceName DOCUMENT ME!
     */
    public void setSelectionByName(String datasourceName) {
        proxySelector.setText(datasourceName);
    }

    private void buildUI(boolean showMore) {
        JPanel commandsPane = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.gridwidth = 1;
        commandsPane.add(new JLabel(App.messages.getString("res.90")), gbc);

        gbc.weightx = 100.0f;

        if (!showMore) {
            gbc.gridwidth = GridBagConstraints.REMAINDER;
        }

        commandsPane.add(proxySelector, gbc);
        gbc.weightx = 0.0f;
        proxySelector.setEditable(false);

        if (showMore) {
            gbc.gridwidth = GridBagConstraints.REMAINDER;

            JButton more = new JButton("...");
            more.setFont(new Font("Arial", 0, 6));
            commandsPane.add(more, gbc);
            more.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        more();
                    }
                });
        }

        gbc.gridwidth = 1;
        commandsPane.add(new JLabel(App.messages.getString("res.534")), gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        descriptionText.setEditable(false);
        commandsPane.add(descriptionText, gbc);

        fieldsTable = new JTable(new ZFieldsTableModel());

        fieldsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        fieldsTable.setRowHeight(20);
        setLayout(new BorderLayout());
        gbc.weighty = 1.0f;
        commandsPane.add(new JScrollPane(fieldsTable), gbc);
        add(commandsPane, BorderLayout.CENTER);
    }

    /**
     * DOCUMENT ME!
     *
     * @param proxyContainer DOCUMENT ME!
     */
    public void setProxyContainer(DatasetReaderList proxyContainer) {
        if (proxyContainer == null) {
            throw new NullPointerException(App.messages.getString("res.535"));
        }

        if (this.proxyContainer != null) {
            this.proxyContainer.removeDatasetChangeListener(this);
        }

        this.proxyContainer = proxyContainer;
        this.proxyContainer.addDatasetChangeListener(this);
        refreshSelector();
    }

    private void more() {
        if (proxyContainer.getCount() > 0) {
            edit();
        } else {
            add();
        }

        refreshSelector();
    }

    void edit() {
        DatasetReader proxy = proxyContainer.getDatasetReader(0);
        DatasetReaderFactory proxyFactory = SimpleDatasetReaderFactory.getIntance(proxy.getType());
        proxyFactory.edit(proxy, this, this);
    }

    private void add() {
        String[] types = SimpleDatasetReaderFactory.getTypes();
        DatasetPropertyEditor property = new DatasetPropertyEditor(Util.getCDF(this), "",
                SqlReader.TYPE, types);
        property.setLocationRelativeTo(this);
        property.show();

        if (property.isExitOK()) {
            DatasetReaderFactory proxyFactory = SimpleDatasetReaderFactory.getIntance(property.getType());

            if (proxyFactory != null) {
                DatasetReader proxy = proxyFactory.create(this, this);

                if (proxy != null) {
                    proxy.setName(property.getName());

                    proxyContainer.add(proxy);
                }
            }
        }
    }

    private void refreshSelector() {
        if (proxyContainer.getCount() > 0) {
            refreshFields(proxyContainer.getDatasetReader(0));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent e) {
        refreshSelector();
    }

    /**
     * DOCUMENT ME!
     *
     * @param proxy DOCUMENT ME!
     */
    public void refreshFields(DatasetReader proxy) {
        try {
            proxySelector.setText(proxy.getName());
            descriptionText.setText(proxy.getDescription());
            selection = proxy.read(null, 0);
        } catch (Exception ex) {
            MessageBox.error(this, ex.getMessage());
            selection = null;
        }

        fieldsTable.revalidate();
        fieldsTable.repaint();
    }

    class ZFieldsTableModel extends AbstractTableModel implements Selectable {
        public int getRowCount() {
            return (selection == null) ? 0 : selection.getColumnCount();
        }

        public Object getSelection(int i) {
            return (selection == null) ? null : selection.getColumnName(i);
        }

        public int getColumnCount() {
            return 1;
        }

        public String getColumnName(int columnIndex) {
            return NAME;
        }

        public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            return selection.getColumnName(rowIndex);
        }
    }
}
