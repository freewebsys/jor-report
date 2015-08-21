package jatools.designer.property.editor;

import jatools.designer.toolbox.ComboColorChooser;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class ColorCellEditor extends AbstractCellEditor implements TableCellEditor {
	boolean enabledTransparent;
	
    ComboColorChooser colorchooser;

	private boolean empty;

    public ColorCellEditor(boolean enabledTransparent,boolean empty) {
	
		this.enabledTransparent = enabledTransparent;
		this.empty = empty;
	}
    
    public ColorCellEditor(boolean enabledTransparent) {
    	this(enabledTransparent,false);
 
	}

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
        if (this.colorchooser == null) {
            this.colorchooser = new ComboColorChooser(enabledTransparent);
            this.colorchooser.setEmpty( this.empty);
            this.colorchooser.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        stopCellEditing();
                    }
                });
        }

        this.colorchooser.setColor((Color) value);

        return this.colorchooser;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getCellEditorValue() {
        return this.colorchooser.getColor();
    }
}
