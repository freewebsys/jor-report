package jatools.designer.property.editor;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;


/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.1 $
 * @author $author$
 */
public class IconListRenderer extends JLabel implements ListCellRenderer {
    /**
 * Creates a new ZIconListRenderer object.
 *
 * @param icon DOCUMENT ME!
 */
    IconListRenderer(Icon icon) {
        super(icon);
        this.setPreferredSize(new Dimension(200, 20));
    }

    /**
 * DOCUMENT ME!
 *
 * @param list DOCUMENT ME!
 * @param value DOCUMENT ME!
 * @param index DOCUMENT ME!
 * @param isSelected DOCUMENT ME!
 * @param cellHasFocus DOCUMENT ME!
 *
 * @return DOCUMENT ME!
 */
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, 
                                                  boolean cellHasFocus) {
        putClientProperty(IconListCellEditorRX.PROPERTY_VALUE, value);

        if (isSelected || cellHasFocus) {
            this.setBackground(list.getSelectionBackground());
            this.setForeground(list.getSelectionForeground());
        } else {
            this.setBackground(Color.white);
            this.setForeground(Color.black);
        }

        return this;
    }
}
