package jatools.swingx.treetable;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreeModel;


/**
 * This example shows how to create a simple JTreeTable component, by using a
 * JTree as a renderer (and editor) for the cells in a particular column in the
 * JTable.
 *
 * @version %I% %G%
 *
 * @author Philip Milne
 * @author Scott Violet
 */
public class JTreeTable extends JTable {
    protected TreeTableCellRenderer tree;
    TreeTableModel ttm;

    /**
     * Creates a new JTreeTable object.
     *
     * @param ttm DOCUMENT ME!
     */
    public JTreeTable(TreeTableModel ttm) {
        super();

        // Make the tree and table row heights the same.
        setShowGrid(false);
        setIntercellSpacing(new Dimension(0, 0));

        if (ttm != null) {
            setModel(ttm);
        }
    }

    /**
     * Creates a new JTreeTable object.
     */
    public JTreeTable() {
        this(null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ttm DOCUMENT ME!
     */
    public void setModel(TreeTableModel ttm) {
        // Create the tree. It will be used as a renderer and editor.
        tree = new TreeTableCellRenderer(ttm);

        // Install a tableModel representing the visible rows in the tree.
        super.setModel(new TreeTableModelAdapter(ttm, tree));

        // Force the JTable and JTree to share their row selection models.
        tree.setSelectionModel(new DefaultTreeSelectionModel() {

                {
                    setSelectionModel(listSelectionModel);
                }
            });

        tree.setRowHeight(getRowHeight());

        // Install the tree editor renderer and editor.
        setDefaultRenderer(TreeTableModel.class, tree);
        setDefaultEditor(TreeTableModel.class, new TreeTableCellEditor());

        this.ttm = ttm;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TreeTableModel getTTM() {
        return this.ttm;
    }

    /*
     * Workaround for BasicTableUI anomaly. Make sure the UI never tries to
     * paint the editor. The UI currently uses different techniques to paint the
     * renderers and editors and overriding setBounds() below is not the right
     * thing to do for an editor. Returning -1 for the editing row in this case,
     * ensures the editor is never painted.
     */
    public int getEditingRow() {
        return (getColumnClass(editingColumn) == TreeTableModel.class) ? (-1) : editingRow;
    }

    /**
     * @param x
     */
    public void setCellRenderer(TreeCellRenderer render) {
        tree.setCellRenderer(render);
    
    }

    // 
    // The renderer used to display the tree nodes, a JTree.
    //
    public class TreeTableCellRenderer extends JTree implements TableCellRenderer {
        protected int visibleRow;

        public TreeTableCellRenderer(TreeModel model) {
            super(model);
        }

        public void setBounds(int x, int y, int w, int h) {
            super.setBounds(x, 0, w, JTreeTable.this.getHeight());
        }

        public void paint(Graphics g) {
            g.translate(0, -visibleRow * getRowHeight());
            super.paint(g);
        }

        public Component getTableCellRendererComponent(JTable table, Object value,
            boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }

            visibleRow = row;

            return this;
        }
    }

    // 
    // The editor used to interact with tree nodes, a JTree.
    //
    public class TreeTableCellEditor extends AbstractCellEditor implements TableCellEditor {
        public Component getTableCellEditorComponent(JTable table, Object value,
            boolean isSelected, int r, int c) {
            return tree;
        }
    }
}
