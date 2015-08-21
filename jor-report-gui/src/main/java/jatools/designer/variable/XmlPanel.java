package jatools.designer.variable;

import jatools.designer.App;
import jatools.designer.EditorView;
import jatools.designer.Main;
import jatools.designer.ReportEditor;

import jatools.util.Util;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class XmlPanel extends JPanel implements EditorView {
    private NodeSourceTree variableTree;
    private JTabbedPane tabbedPane;
    private JScrollPane treeScrollPanel;

    /**
     * Creates a new XmlPanel object.
     */
    public XmlPanel() {
        initUI();
        Main.getInstance().registerEditorView(this);
    }

    private void initUI() {
        setLayout(new BorderLayout());
        tabbedPane = new JTabbedPane();

        treeScrollPanel = new JScrollPane(new JLabel());

        tabbedPane.addTab(App.messages.getString("res.259"),
            Util.getIcon("/jatools/icons/srctree.gif"), treeScrollPanel);
        add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * DOCUMENT ME!
     *
     * @param editor DOCUMENT ME!
     */
    public void setEditor(ReportEditor editor) {
        if (editor != null) {
            variableTree = editor.getNodeSourceTree();
            treeScrollPanel.setViewportView(variableTree);
        } else {
            variableTree = null;
            treeScrollPanel.setViewportView(new JLabel());
        }
    }
}
