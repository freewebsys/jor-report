package jatools.designer.property.editor;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;


/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.1 $
 * @author $author$
 */
class FixedSizeComboBoxUI extends BasicComboBoxUI {
    /**
 * DOCUMENT ME!
 *
 * @return DOCUMENT ME!
 */
    protected Dimension getDisplaySize() {
        return getDefaultSize();
    }

    /**
 * DOCUMENT ME!
 *
 * @return DOCUMENT ME!
 */
    public JList getPopupList() {
        return listBox;
    }

    /**
 * DOCUMENT ME!
 *
 * @return DOCUMENT ME!
 */
    protected ComboPopup createPopup() {
        return new ZComboPopup(comboBox);
    }

    /**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.1 $
 * @author $author$
 */
    class ZComboPopup extends BasicComboPopup {
        /**
 * Creates a new ZComboPopup object.
 *
 * @param combo DOCUMENT ME!
 */
        public ZComboPopup(JComboBox combo) {
            super(combo);
        }

        /**
 * DOCUMENT ME!
 */
        public void show() {
            ListCellRenderer rend = comboBox.getRenderer();
            Component comp = rend.getListCellRendererComponent(list, null, 0, false, false);
            Dimension popupSize = comp.getPreferredSize();

            popupSize.setSize(popupSize.width, getPopupHeightForRowCount(comboBox.getMaximumRowCount()));
            scroller.setMaximumSize(popupSize);
            scroller.setPreferredSize(popupSize);
            scroller.setMinimumSize(popupSize);

            Rectangle popupBounds = computePopupBounds(0, comboBox.getBounds().height, popupSize.width, 
                                                       popupSize.height);
            list.invalidate();
            list.setSelectedIndex(comboBox.getSelectedIndex());
            list.ensureIndexIsVisible(list.getSelectedIndex());

            setLightWeightPopupEnabled(comboBox.isLightWeightPopupEnabled());

            show(comboBox, popupBounds.x, popupBounds.y);
        }
    }
}
