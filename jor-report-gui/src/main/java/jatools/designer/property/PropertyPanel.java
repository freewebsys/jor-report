package jatools.designer.property;



import jatools.designer.App;
import jatools.designer.ReportEditor;
import jatools.designer.data.TooltipHelper;
import jatools.designer.data.VariableTree;
import jatools.designer.data.Variable;
import jatools.designer.property.editor.CellEditorWithCustomEditor;
import jatools.designer.property.event.EventChooser;
import jatools.designer.property.event.EventEditor;
import jatools.designer.variable.XmlPanel;
import jatools.swingx.Chooser;
import jatools.swingx.SimpleTreeNode;
import jatools.util.Util;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;


/**
 * 属性面板，包括属性，事件，变量三个tab
 *
 * @author $author$
 * @version $Revision$
  */
public class PropertyPanel extends JPanel implements ChangeListener, TooltipHelper {
    SimplePropertyEditor propEditor = new SimplePropertyEditor(true);
    EventEditor eventEditor = new EventEditor();
    VariableTree variableTree = new VariableTree();
    ReportEditor editor = null;
    XmlPanel xmlPanel;
    JTabbedPane tp;

    /**
     * Creates a new PropertyPanel object.
     */
    public PropertyPanel() {
        setLayout(new BorderLayout());

        tp = new JTabbedPane();
        tp.addTab(App.messages.getString("res.300"), Util.getIcon("/jatools/icons/properties.gif"),
            new JScrollPane(propEditor));
        tp.addTab(App.messages.getString("res.301"), Util.getIcon("/jatools/icons/event.gif"), new JScrollPane(eventEditor));
        tp.addTab(App.messages.getString("res.205"), Util.getIcon("/jatools/icons/variable.gif"),
            new JScrollPane(variableTree));

        xmlPanel = new XmlPanel();

        JSplitPane sp = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, xmlPanel, tp);

        add(sp);
        sp.setOneTouchExpandable(true);
        sp.setDividerLocation(200);

        propEditor.eventEditor = eventEditor;

        Chooser chooser = new EventChooser();
        eventEditor.setEditor(new CellEditorWithCustomEditor(this, chooser));
        variableTree.setTooltipHelper(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getToolTipText(Object value) {
        String text = null;
        SimpleTreeNode node = (SimpleTreeNode) value;

        Object _var = node.getUserObject();

        if (_var instanceof Variable) {
            Variable var = (Variable) _var;

            if (var.getVariableName() != null) {
                int type = node.getType();

                text = App.messages.getString("res.302");
            }
        }

        return text;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SimplePropertyEditor getTable() {
        return propEditor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public VariableTree getVariableTree() {
        return variableTree;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public JTabbedPane getTp() {
        return tp;
    }
}
