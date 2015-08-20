package jatools.designer.variable.popup;


import jatools.designer.variable.SourceType;

import java.awt.Component;

import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class TreePopupFactory implements SourceType {
    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param type DOCUMENT ME!
     * @param defaultMutableTreeNode DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static JPopupMenu createPopup(Component c, int type,
        DefaultMutableTreeNode defaultMutableTreeNode) {
        return new NodeSourceTreePopup(c, defaultMutableTreeNode, type);
    }
}
