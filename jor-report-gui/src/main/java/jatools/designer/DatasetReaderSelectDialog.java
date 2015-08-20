package jatools.designer;

import jatools.ReportDocument;
import jatools.data.reader.DatasetReader;
import jatools.designer.data.DatasetPreviewer;
import jatools.designer.data.IconTree;
import jatools.swingx.CommandPanel;
import jatools.swingx.SimpleTreeNode;
import jatools.util.Util;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultTreeModel;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.6 $
  */
public class DatasetReaderSelectDialog extends JDialog implements ChangeListener {
    /**
     * DOCUMENT ME!
     */
    public static final String CURRENT_READER = "current.reader";

    /**
     * DOCUMENT ME!
     */
    public static final String EXTERNAL_READER = "external.reader";

    /**
     * DOCUMENT ME!
     */
    public static final String CATAGORY = "catagory";
    DatasetReaderSelectPanel panel;
    JButton browseButton = new JButton(App.messages.getString("res.102"));
    JButton okButton = new JButton(CommandPanel.OK);
    ReportDocument document;
    DatasetReader reader;
    boolean externalReader;

    DatasetReaderSelectDialog(Frame owner, ReportDocument doc) {
        super(owner, App.messages.getString("res.103"), true);
        panel = new DatasetReaderSelectPanel();

        ActionListener cancel = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    cancel();
                }
            };

        CommandPanel command = CommandPanel.createPanel(CommandPanel.CANCEL, cancel);

        command.insert(okButton, 0);
        okButton.setEnabled(false);
        okButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    done();
                }
            });

        command.insert(browseButton, 100);
        browseButton.setEnabled(false);
        browseButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    browse();
                }
            });
        this.getContentPane().add(panel, BorderLayout.CENTER);
        this.getContentPane().add(command, BorderLayout.SOUTH);
        panel.addNode(Global.getFaviroteDatasetReaders(), EXTERNAL_READER);

        try {
           

            this.document = doc;
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        panel.tree.addChangeListener(this);
        this.document = doc;
    }

    protected void done() {
        
        
        
        // 
        SimpleTreeNode node = (SimpleTreeNode) panel.tree.getSelectionPath().getLastPathComponent();

        if (node != null) {
            this.reader = (DatasetReader) node.getUserObject();
            this.externalReader = node.getProperty(CATAGORY) == EXTERNAL_READER;
            hide();

            //            
            //            
            //
            //            if (cat == EXTERNAL_READER) {
            //                DatasetReader reader = (DatasetReader) panel.tree.getSelectedObject();
            //
            //                if (this.document != null) {
            //                    DocumentVariableNameChecker checker = new DocumentVariableNameChecker(this.document);
            //
            //                    try {
            //                        checker.check(reader.getName());
            //                    } catch (Exception e) {
            
            //                            JOptionPane.OK_CANCEL_OPTION);
            //                    }
            //                }
            //            }
        }
    }

    protected void cancel() {
        hide();
    }

    protected void browse() {
        DatasetReader reader = (DatasetReader) panel.tree.getSelectedObject();
        DatasetPreviewer preview = new DatasetPreviewer((Frame) getOwner());
        preview.setLocationRelativeTo(this);
        preview.setReader(reader);
        preview.show();
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        try {
            ReportDocument doc = ReportDocument.load(new File("d:/test.xml"));

            DatasetReaderSelectDialog d;
            (d = new DatasetReaderSelectDialog(null, doc)).show();

            DatasetReader _reader = d.getReader();

            if (_reader != null) {
                if (d.isExternalReader()) {
                    DocumentVariableNameChecker checker = new DocumentVariableNameChecker(doc);

                    try {
                        checker.check(_reader.getName());
                    } catch (Exception e) {
                        e.printStackTrace();
                        JOptionPane.showInputDialog(null, App.messages.getString("res.104"), App.messages.getString("res.105"),
                            JOptionPane.OK_CANCEL_OPTION);
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent e) {
        browseButton.setEnabled(panel.tree.getSelectedObject() instanceof DatasetReader);
        okButton.setEnabled(browseButton.isEnabled());
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DatasetReader getReader() {
        return reader;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isExternalReader() {
        return externalReader;
    }
}


class DatasetReaderSelectPanel extends JPanel {
    final static Icon DATASET_LIST = Util.getIcon("/jatools/icons/new.gif");
    final static Icon DATASET = Util.getIcon("/jatools/icons/open.gif");
    final static Icon ROWSET = Util.getIcon("/jatools/icons/save.gif");
    final static Icon COLUMNSET = Util.getIcon("/jatools/icons/print.gif");
    final static Icon SUMSET = Util.getIcon("/jatools/icons/print.gif");
    IconTree tree;

    DatasetReaderSelectPanel() {
        super(new BorderLayout());

        SimpleTreeNode root = new SimpleTreeNode(App.messages.getString("res.106"), null, -1);
        tree = new IconTree();

        tree.setModel(new DefaultTreeModel(root));

        add(new JScrollPane(tree));
    }

    /**
     * DOCUMENT ME!
     *
     * @param list DOCUMENT ME!
     * @param cat DOCUMENT ME!
     */
    public void addNode(DatasetReaderList list, String cat) {
        SimpleTreeNode root = (SimpleTreeNode) tree.getModel().getRoot();
        addNode(root, list, cat);
    }

    static void addNode(SimpleTreeNode parent, DatasetReaderList list, String cat) {
        SimpleTreeNode node = new SimpleTreeNode(list.getName(), DATASET_LIST, 1);
        parent.add(node);

        Iterator it = list.iterator();

        while (it.hasNext()) {
            Object next = it.next();

            if (next instanceof DatasetReaderList) {
                addNode(node, (DatasetReaderList) next, cat);
            } else if (next instanceof DatasetReader) {
                DatasetReader reader = (DatasetReader) next;
                Icon icon = null;

//                if (reader instanceof CrossPartReader) {
//                    CrossPartReader crossReader = (CrossPartReader) reader;
//
//                    switch (crossReader.getPart()) {
//                    case CrossPartReader.ROW_PART:
//                        icon = ROWSET;
//
//                        break;
//
//                    case CrossPartReader.COLUMN_PART:
//                        icon = COLUMNSET;
//
//                        break;
//
//                    case CrossPartReader.SUM_PART:
//                        icon = SUMSET;
//
//                        break;
//                    }
//                } else {
                    icon = DATASET;
//                }

                SimpleTreeNode n = new SimpleTreeNode(reader, icon, 2);
                n.setProperty(DatasetReaderSelectDialog.CATAGORY, cat);

                node.add(n);
            }
        }
    }
}
