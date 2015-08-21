package jatools.designer.property;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PropertyTableCellRenderer extends DefaultTableCellRenderer {
    public static final String PROPERTY_VALUE = "value";

    /**
     * Creates a new PropertyTableCellRenderer object.
     */
    public PropertyTableCellRenderer() {
    }

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
        Component c = super.getTableCellRendererComponent(table, value,
                isSelected && (column == 0), false, row, column);

        setFont(new Font("DialogInput", 0, 12));

        return c;
    }
}
