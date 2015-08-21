package jatools.swingx;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public class IconableCellRender<Icon> extends DefaultListCellRenderer  {
    /**
     * Creates a new CellRenderer object.
     */
    Icon checkedIcon;
    Icon nocheckedIcon;

    /**
     * Creates a new ZIconableCellRender object.
     *
     * @param checkedIcon   DOCUMENT ME!
     * @param nocheckedIcon DOCUMENT ME!
     */
//    public IconableCellRender(Icon checkedIcon, Icon nocheckedIcon) {
//        this.checkedIcon = checkedIcon;
//        this.nocheckedIcon = nocheckedIcon;
//
//        setBackground(UIManager.getColor("List.textBackground")); //
//        setForeground(UIManager.getColor("List.textForeground")); //
//    }

    /**
     * Creates a new ZIconableCellRender object.
     */
    public IconableCellRender() {

        this.checkedIcon = (Icon)UIManager.getIcon("Tree.leafIcon");
        this.nocheckedIcon = (Icon)UIManager.getIcon("Tree.closedIcon");

        setBackground(UIManager.getColor("List.textBackground")); //
        setForeground(UIManager.getColor("List.textForeground")); //
    }

    /**
     * DOCUMENT ME!
     *
     * @param icon DOCUMENT ME!
     */
    public void setSelectedIcon(Icon icon) {
        checkedIcon = icon;
    }

    /**
     * DOCUMENT ME!
     *
     * @param icon DOCUMENT ME!
     */
    public void setNoSelectedIcon(Icon icon) {
        nocheckedIcon = icon;
    }

    /**
     * DOCUMENT ME!
     *
     * @param list       DOCUMENT ME!
     * @param value      DOCUMENT ME!
     * @param index      DOCUMENT ME!
     * @param isSelected DOCUMENT ME!
     * @param hasFocus   DOCUMENT ME!
     * @return DOCUMENT ME!
     */

    public Component getListCellRendererComponent(JList list, Object value, int index,
                                                  boolean isSelected, boolean hasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, hasFocus);

        Icon icon = ((CheckBoxDndList) list).isSelected(index)
                ? checkedIcon : nocheckedIcon;

        //setIcon(icon);

        return this;
    }
}
