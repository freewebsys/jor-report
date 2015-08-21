package jatools.designer.data;


import jatools.data.reader.DatasetReader;
import jatools.designer.App;
import jatools.designer.DatasetReaderConfigureTree;
import jatools.designer.wizard.BuilderContext;
import jatools.swingx.CustomTable;
import jatools.swingx.SimpleTreeNode;
import jatools.swingx.SwingUtil;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.tree.TreePath;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ReaderSelector extends javax.swing.JPanel implements ChangeListener,
    TableModelListener {
    JButton unselectCommand;
    JButton selectCommand;
    CustomTable table;
    DatasetReader usableReader;
    ArrayList listenerCache = new ArrayList();
    DatasetReaderConfigureTree datasetTree;

    /**
     * Creates a new ReaderSelector object.
     *
     * @param rootNode DOCUMENT ME!
     */
    public ReaderSelector(SimpleTreeNode rootNode) {
        super(new GridBagLayout());

        JPanel sourcePanel = getSourceTreePanel();
        JPanel commandPanel = getCommandPanel();
        JPanel targetPanel = getTargetPanel();

        SwingUtil.setSize(sourcePanel, new Dimension(200, 200));
        SwingUtil.setSize(commandPanel, new Dimension(60, 200));
        SwingUtil.setSize(targetPanel, new Dimension(200, 200));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        add(sourcePanel, gbc);
        add(commandPanel, gbc);

        add(targetPanel, gbc);
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        add(Box.createHorizontalGlue(), gbc);
        gbc.weightx = 0;
        gbc.weighty = 1.0;
        add(Box.createVerticalGlue(), gbc);

        initReaderTree(rootNode);

        SwingUtil.setBorder6(this);
    }

    private void initReaderTree(SimpleTreeNode rootNode) {
        datasetTree.setRoot(rootNode);
        datasetTree.addChangeListener(this);

        datasetTree.expandPath(new TreePath(rootNode.getPath()));
    }

    private JPanel getTargetPanel() {
        table = new CustomTable(new String[] { App.messages.getString("res.108") });

        table.getModel().addTableModelListener(this);

        JPanel result = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;

        result.add(new JLabel(App.messages.getString("res.550")), gbc);
        gbc.weighty = 1.0;

        JScrollPane scroll = new JScrollPane(table);

        table.getTableHeader().setVisible(false);
        table.getTableHeader().setPreferredSize(new Dimension(1, 1));
        result.add(scroll, gbc);

        return result;
    }

    private JPanel getCommandPanel() {
        unselectCommand = new JButton("<");
        selectCommand = new JButton(">");
        SwingUtil.setSize(unselectCommand, new Dimension(45, 27));
        SwingUtil.setSize(selectCommand, new Dimension(45, 27));

        selectCommand.setEnabled(false);
        unselectCommand.setEnabled(false);

        selectCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (usableReader != null) {
                        table.removeAllRows();
                        table.addRow(new Object[] { usableReader }, true);
                    }
                }
            });
        unselectCommand.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    table.removeAllRows();
                }
            });

        JPanel result = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets.top = 23;

        result.add(selectCommand, gbc);
        gbc.insets.top = 1;
        result.add(unselectCommand, gbc);
        gbc.weighty = 1.0;
        result.add(Box.createVerticalGlue(), gbc);

        return result;
    }

    private JPanel getSourceTreePanel() {
        datasetTree = new DatasetReaderConfigureTree();
        table = new CustomTable(new String[] { App.messages.getString("res.108") });

        JPanel result = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.weightx = 1.0;
        result.add(new JLabel(App.messages.getString("res.551")), gbc);
        gbc.weighty = 1.0;

        result.add(new JScrollPane(datasetTree), gbc);

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        showGUI();
    }

    /**
     * DOCUMENT ME!
     */
    public static void showGUI() {
        try {
            JFrame frame = new JFrame();

            ReaderSelector inst = new ReaderSelector(null);
            frame.setContentPane(inst);
            frame.pack();
            frame.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == datasetTree) {
            if (datasetTree.getSelectionPath() != null) {
                boolean hitReader = ((SimpleTreeNode) datasetTree.getSelectionPath()
                                                                 .getLastPathComponent()).getUserObject() instanceof DatasetReader;

                selectCommand.setEnabled(hitReader);

                if (hitReader) {
                    usableReader = (DatasetReader) ((SimpleTreeNode) datasetTree.getSelectionPath()
                                                                                .getLastPathComponent()).getUserObject();
                }
            } else {
                selectCommand.setEnabled(false);
                usableReader = null;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void contentsChanged(ListDataEvent e) {
        enableButtons();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void intervalAdded(ListDataEvent e) {
        enableButtons();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void intervalRemoved(ListDataEvent e) {
        enableButtons();
    }

    void enableButtons() {
        unselectCommand.setEnabled(table.getRowCount() > 0);

        this.fireChangeListener();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DatasetReader getSelectedReader() {
        if (table.getRowCount() > 0) {
            return (DatasetReader) table.getValueAt(0, 0);
        }
        else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void addChangeListener(ChangeListener lst) {
        listenerCache.add(lst);
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void removeChangeListener(ChangeListener lst) {
        listenerCache.remove(lst);
    }

    void fireChangeListener() {
        ChangeEvent e = new ChangeEvent(this);

        for (Iterator iter = listenerCache.iterator(); iter.hasNext();) {
            ChangeListener element = (ChangeListener) iter.next();

            element.stateChanged(e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void tableChanged(TableModelEvent e) {
        enableButtons();
    }

    /**
     * DOCUMENT ME!
     *
     * @param context DOCUMENT ME!
     */
    public void apply(BuilderContext context) {
        if (table.getRowCount() > 0) {
            context.setValue(BuilderContext.READER, table.getValueAt(0, 0));
        }
    }
}
