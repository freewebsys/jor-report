package jatools.designer.property.editor;

import java.awt.Color;
import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ColorCBBAsCellEditor extends AbstractCellEditor implements TableCellEditor {
    ColorComboBox colorCombo;
 

    /**
     * Creates a new ColorCBBAsCellEditor object.
     *
     * @param colorCombo DOCUMENT ME!
     */
    public ColorCBBAsCellEditor(ColorComboBox colorCombo) {
        this.colorCombo = colorCombo;
        this.colorCombo.addPropertyChangeListener(new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent evt) {
                    if (evt.getPropertyName().equals(ColorComboBox.PROPERTY_POPUP_CLOSED)) {
                        stopCellEditing();
                    }
                }
            });
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
        colorCombo.setColor((Color) value);

        return colorCombo;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getCellEditorValue() {
        return colorCombo.getColor();
    }
}
