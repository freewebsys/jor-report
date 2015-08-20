package jatools.swingx;

import javax.swing.JMenu;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
 */
public abstract class DynamicMenu extends JMenu {
    public DynamicMenu(String menuLabel) {
        super(menuLabel);

        addMenuListener(new MenuListener() {
                public void menuCanceled(MenuEvent e) {
                }

                public void menuDeselected(MenuEvent e) {
                }

                public void menuSelected(MenuEvent e) {
                    // Clean up the whole menu
                    removeAll();

                    // Rebuild the whole list of menu items on the fly
                    buildItems();
                }
            });
    }

    protected abstract void buildItems();
}
