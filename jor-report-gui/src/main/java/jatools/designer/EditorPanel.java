package jatools.designer;

import jatools.ReportDocument;

import jatools.designer.property.PropertyPanel;

import java.awt.Component;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;


/**
 * 报表设计面板
 *
 *
 * @author $author$
 * @version $Revision$
  */
public class EditorPanel extends JSplitPane {
    EditorTabPanel editorTabs; // 编辑器tab窗口,每一个打开的报表用其中一个tab显示
    PropertyPanel propPanel; // 属性面板，包括属性，事件，变量三个tab
    private JTabbedPane propTab; // 左面的tab,包括设计、示例报表两个tab
    private double lastProportion;

    /**
     * Creates a new EditorPanel object.
     */
    public EditorPanel() {
        super(HORIZONTAL_SPLIT, true);

        editorTabs = new EditorTabPanel();

        propPanel = new PropertyPanel();

        setDividerLocation(300);
        setOneTouchExpandable(true);

        propTab = new JTabbedPane();

        DefaultReportsTreePanel dr = new DefaultReportsTreePanel();

        CurrentReportsTreePanel cr = new CurrentReportsTreePanel();

        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, cr, dr);
        sp.setDividerLocation(200);
        sp.setOneTouchExpandable(true);

        propTab.addTab(App.messages.getString("res.117"), propPanel);
        propTab.addTab(App.messages.getString("res.118"), sp);

        setLeftComponent(propTab);
        setRightComponent(editorTabs);

        new PropertyEditorAndRendererSupport(propPanel, propPanel.getTable());

        this.setName("EditorPanel");
        propTab.setSelectedIndex(1);
    }

    /**
     * DOCUMENT ME!
     */
    public void toggleSplit() {
        double proportionalLocation = ((double) this.getDividerLocation()) / (getWidth() -
            getDividerSize());

        if (proportionalLocation == 1.0) {
            setDividerLocation(lastProportion);
        } else {
            setDividerLocation(1.0);
            this.lastProportion = proportionalLocation;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param aa DOCUMENT ME!
     */
    public void addEditorChangeListener(ChangeListener aa) {
        this.editorTabs.addChangeListener(aa);
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param tip DOCUMENT ME!
     * @param doc DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ReportEditor createEditor(String name, String tip, ReportDocument doc) {
        return editorTabs.createEditor(name, tip, doc);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ReportEditor getActiveEditor() {
        return editorTabs.getActiveEditor();
    }

    /**
     * DOCUMENT ME!
     *
     * @param editor DOCUMENT ME!
     */
    public void setActiveEditor(ReportEditor editor) {
        editorTabs.setActiveEditor(editor);
    }

    /**
     * DOCUMENT ME!
     */
    public void selectPropertyPanel() {
        if (this.propTab.getSelectedIndex() != 0) {
            this.propTab.setSelectedIndex(0);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param editor DOCUMENT ME!
     * @param name DOCUMENT ME!
     * @param tip DOCUMENT ME!
     */
    public void setPrompt(ReportEditor editor, String name, String tip) {
        this.editorTabs.setPrompt(editor, name, tip);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyPanel getPropertTable() {
        return propPanel;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator iterator() {
        ArrayList editors = new ArrayList();

        for (int i = 0; i < this.editorTabs.getTabCount(); i++) {
            Component c = this.editorTabs.getComponentAt(i);
            editors.add(c);
        }

        return editors.iterator();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public EditorTabPanel getTabbedPanel() {
        return editorTabs;
    }

    /**
     * DOCUMENT ME!
     */
    public void resetTitle() {
        editorTabs.resetTitle();
    }
}
