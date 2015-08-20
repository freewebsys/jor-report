package jatools.engine.script.debug;

import bsh.Interpreter;

import jatools.engine.InterpreterAware;
import jatools.engine.ValueIfClosed;

import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;
import org.jdesktop.swingx.treetable.TreeTableNode;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class ScriptDebuggerModel extends DefaultTreeTableModel {
    private String[] columnNames = { "名称", "值", "类型" };
    private Class[] columnTypes = { String.class, Object.class, String.class };
    private Interpreter it;

    /**
     * Creates a new ScriptDebuggerModel object.
     *
     * @param node
     *            DOCUMENT ME!
     */
    public ScriptDebuggerModel(TreeTableNode node, Interpreter it) {
        super(node);
        this.it = it;
    }

    /**
     * DOCUMENT ME!
     *
     * @param col
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Class getColumnClass(int col) {
        return columnTypes[col];
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * DOCUMENT ME!
     *
     * @param column
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getColumnName(int column) {
        return columnNames[column];
    }

    /**
     * DOCUMENT ME!
     *
     * @param node
     *            DOCUMENT ME!
     * @param column
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValueAt(Object node, int column) {
        Object value = null;

        if (node instanceof DefaultMutableTreeTableNode) {
            DefaultMutableTreeTableNode mutableNode = (DefaultMutableTreeTableNode) node;
            Object o = mutableNode.getUserObject();

            if ((o != null) && o instanceof ScriptDebuggerBean) {
                ScriptDebuggerBean bean = (ScriptDebuggerBean) o;

                switch (column) {
                case 0:
                    value = bean.getName();

                    break;

                case 1:
                    value = bean.getValue();

                    break;

                case 2:
                    value = bean.getType();
                    if(value == null)
                    	value = "";

                    break;
                }
            }
        }

        return value;
    }

    /**
     * DOCUMENT ME!
     *
     * @param node
     *            DOCUMENT ME!
     * @param column
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCellEditable(Object node, int column) {
        return true;
    }
}
