/*
 * Created on 2004-10-6
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jatools.swingx;

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
public class ComboBoxEditor extends DefaultCellEditor implements TableCellRenderer{
    Object[] values;
    Object[] prompts;
    
  

    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer();
    

    /**
     * @param comboBox
     */
    public ComboBoxEditor( Object[] prompts,Object[] values) {
        super(new JComboBox(prompts));
        this.prompts = prompts;
        this.values = values;
        JComboBox com = (JComboBox) this.getComponent();
        com.setEditable( true);
    }
    public ComboBoxEditor( Object[] prompts) {
        super(new JComboBox(prompts));
        this.prompts = prompts;
        this.values = prompts;
        JComboBox com = (JComboBox) this.getComponent();
        com.setEditable( true);
        
    }
    /* (non-Javadoc)
     * @see javax.swing.CellEditor#getCellEditorValue()
     */
    public Object getCellEditorValue() {
        JComboBox com = (JComboBox) this.getComponent();

        if (com.getSelectedIndex() != -1) {
            return values[com.getSelectedIndex()];
        } else {
            return com.getSelectedItem() ;
        }
    }

    /* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		if(value != null)
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
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		if(value != null)
		for (int i = 0; i < values.length; i++) {
            if (value.equals(values[i])) {
                value = prompts[i];

                break;
            }
        }
		
		return renderer.getTableCellRendererComponent( table,  value,  isSelected,  hasFocus,  row,  column);
		
	}
}
