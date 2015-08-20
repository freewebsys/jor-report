/*
 * Created on 2004-10-6
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jatools.designer.property.editor;

import java.awt.Component;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;


/**
 * @author java
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ComboBoxEditor extends DefaultCellEditor implements TableCellRenderer {
    Object[] values;
    Object[] prompts;
    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();

    /**
     * @param comboBox
     */
    public ComboBoxEditor(Object[] prompts, Object[] values) {
        super(new JComboBox(prompts));
        this.prompts = prompts;
        this.values = values;
    }

    /* (non-Javadoc)
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getCellEditorValue() {
        JComboBox com = (JComboBox) this.getComponent();

        if (com.getSelectedIndex() != -1) {
            return values[com.getSelectedIndex()];
        } else {
            return null;
        }
    }

    /* (non-Javadoc)
         * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
         */
    /**
     * DOCUMENT ME!
     *
     * @param table DOCUMENT ME!
     * @param value DOCUMENT ME!
     * @param isSelected DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
        int row, int column) {
        for (int i = 0; i < values.length; i++) {
            if (value.equals(values[i])) {
                value = prompts[i];

                break;
            }
        }

        return super.getTableCellEditorComponent(table, value, isSelected, row, column);
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
     */
    /**
     * DOCUMENT ME!
     *
     * @param table DOCUMENT ME!
     * @param value DOCUMENT ME!
     * @param isSelected DOCUMENT ME!
     * @param hasFocus DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
        boolean hasFocus, int row, int column) {
        if (value != null) {
            for (int i = 0; i < values.length; i++) {
                if (value.equals(values[i])) {
                    value = prompts[i];

                    break;
                }
            }
        }

        return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
            column);
    }
}
