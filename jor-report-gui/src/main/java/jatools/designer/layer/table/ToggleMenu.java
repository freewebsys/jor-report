package jatools.designer.layer.table;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JMenu;
import javax.swing.JMenuItem;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ToggleMenu {
    private Container parent;
    private JMenu menu;
    private JMenuItem item;

    ToggleMenu(Container parent, JMenu menu, JMenuItem item) {
        this.parent = parent;
        this.menu = menu;
        this.item = item;
    }

    /**
     * DOCUMENT ME!
     */
    public void toggleMenu() {
        replace(parent, item, menu);
    }

    /**
     * DOCUMENT ME!
     */
    public void toggleItem() {
        replace(parent, menu, item);
    }

    private int indexOf(Container parent, Component child) {
        for (int i = 0; i < parent.getComponentCount(); i++) {
            if (child == parent.getComponent(i)) {
                return i;
            }
        }

        return -1;
    }

    private void replace(Container parent, Component oldChild, Component newChild) {
        int i = indexOf(parent, oldChild);

        if (i != -1) {
            parent.remove(oldChild);
            parent.add(newChild, i);
        }
    }
}
