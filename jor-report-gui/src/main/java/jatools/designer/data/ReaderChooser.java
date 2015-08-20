package jatools.designer.data;

import jatools.data.reader.DatasetReader;
import jatools.data.reader.XPathDatasetReader;

import jatools.designer.App;
import jatools.designer.DatasetReaderConfigureTree;
import jatools.designer.Main;

import jatools.designer.config.DatasetReaderList;

import jatools.designer.variable.DatasetTreeNodeValue;
import jatools.designer.variable.NodeSourceTree;

import jatools.dom.src.DatasetNodeSource;

import jatools.swingx.CommandPanel;
import jatools.swingx.MessageBox;
import jatools.swingx.SimpleTreeNode;
import jatools.swingx.SwingUtil;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class ReaderChooser extends JDialog implements ChangeListener {
    private JButton previewButton;
    private JButton addButton;
    DatasetReaderConfigureTree datasetTree;
    private NodeSourceTree nodeSourceTree;
    private DatasetReader reader0; // 预定义数据源
    private DatasetReader reader1; // 来自本报表数据集节点
    private JTabbedPane tab;
    boolean cancel;

    /**
    * Creates a new ReaderChooser object.
    *
    * @param owner
    *            DOCUMENT ME!
    * @param proxyContainer
    *            DOCUMENT ME!
    */
    public ReaderChooser(Frame owner, DatasetReaderList proxyContainer) {
        super(owner, App.messages.getString("res.549"), true);

        datasetTree = new DatasetReaderConfigureTree();

        datasetTree.addChangeListener(this);
        datasetTree.setDoubleClickAction(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    stateChanged(new ChangeEvent(datasetTree));

                    if (reader0 != null) {
                        setVisible(false);
                    }
                }
            });

        nodeSourceTree = new jatools.designer.variable.NodeSourceTree(false);
        nodeSourceTree.setToggleClickCount(10000);
        nodeSourceTree.setEnablePopup(false);

        if (((Main.getInstance() != null) && (Main.getInstance().getActiveEditor() != null)) &&
                (Main.getInstance().getActiveEditor().getReport() != null)) {
            nodeSourceTree.initTreeData(Main.getInstance().getActiveEditor().getReport()
                                            .getNodeSource());
        }

        nodeSourceTree.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    reader1 = null;

                    if (evt.getPropertyName().equals("select.value") ||
                            evt.getPropertyName().equals("select.click")) {
                        Object sel = nodeSourceTree.getSeclectedObject();

                        if (sel instanceof DatasetTreeNodeValue) {
                            DatasetTreeNodeValue nv = (DatasetTreeNodeValue) sel;

                            if (nv.getNodeSource() instanceof DatasetNodeSource) {
                                String path = nv.getNodeSource().getFullPath();
                                reader1 = new XPathDatasetReader(path);

                                if (evt.getPropertyName().equals("select.value")) {
                                    setVisible(false);

                                    return;
                                }
                            }
                        }
                    }

                    enableButtons();
                }
            });

        tab = new JTabbedPane();
        tab.addTab("系统预定义数据集", new JScrollPane(datasetTree));
        tab.addTab("当前报表数据集", new JScrollPane(nodeSourceTree));
        tab.addChangeListener(new ChangeListener() {
                public void stateChanged(ChangeEvent e) {
                    enableButtons();
                }
            });

        nodeSourceTree.expandTree();

        this.getContentPane().add(tab);

        previewButton = new JButton(App.messages.getString("res.187"));
        previewButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    preview();
                }
            });

        addButton = new JButton(App.messages.getString("res.3"));
        addButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    setVisible(false);
                }
            });

        JButton okButton = new JButton(App.messages.getString("res.4"));
        okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    reader0 = reader1 = null;
                    cancel = true;
                    setVisible(false);
                }
            });

        CommandPanel commandsPane = CommandPanel.createPanel(false);

        commandsPane.addComponent(previewButton);
        commandsPane.addComponent(addButton);
        commandsPane.addComponent(okButton);

        datasetTree.expandAll();

        getContentPane().add(commandsPane, BorderLayout.SOUTH);
        SwingUtil.setBorder6((JComponent) getContentPane());
        pack();

        setSize(350, 400);
        this.setLocationRelativeTo(owner);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCancel() {
        return cancel;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e
     *            DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent e) {
        if (e.getSource() == datasetTree) {
            this.reader0 = null;

            if (((SimpleTreeNode) datasetTree.getSelectionPath().getLastPathComponent()).getUserObject() instanceof DatasetReader) {
                this.reader0 = (DatasetReader) ((SimpleTreeNode) datasetTree.getSelectionPath()
                                                                            .getLastPathComponent()).getUserObject();
            }

            enableButtons();
        }
    }

    private void enableButtons() {
        DatasetReader reader = this.getReader();

        previewButton.setEnabled(reader != null);
        addButton.setEnabled(reader != null);
    }

    private void preview() {
        try {
            DatasetReader proxy = (DatasetReader) ((SimpleTreeNode) datasetTree.getSelectionPath()
                                                                               .getLastPathComponent()).getUserObject();

            DatasetPreviewer preview = new DatasetPreviewer((Frame) getOwner());
            preview.setLocationRelativeTo(this);
            preview.setReader(proxy);
            preview.show();
        } catch (Exception ex) {
            MessageBox.error(this, ex.getMessage());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DatasetReader getReader() {
        return (this.tab.getSelectedIndex() == 0) ? reader0 : reader1;
    }
}
