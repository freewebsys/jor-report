package jatools.component.layout;

import jatools.component.Component;
import jatools.component.Panel;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PanelRowLayout extends FreeLayout {
    
    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void layout(Component c) {
        Iterator it = c.iterator();

        while (it.hasNext()) {
            Component panel = (Component) it.next();

            if (panel instanceof Panel) {
                panel.setX(0);
                panel.setWidth(c.getWidth() - c.getPadding().left - c.getPadding().right);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void layout2(Component c) {
        if (c.getChildCount() == 0) {
            return;
        }

        Component[] children = (Component[]) c.getChildren().toArray(new Component[0]);
        Arrays.sort(children, LocationComparator.getInstance());

        Rectangle r = new Rectangle();

        for (int i = 0; i < children.length; i++) {
            Component panel = (Component) children[i];

            if (panel instanceof Panel) {
                if (r.height > panel.getY()) {
                    int offy = (r.height - panel.getY());

                    int y = panel.getY();
                    int y2 = panel.getY2();

                    panel.setY(panel.getY() + offy);

                    if ((i < (children.length - 1)) && (children[i + 1].getY() < y2) &&
                            (children[i + 1].getY() >= y)) {
                        if (children[i + 1] instanceof Panel) {
                            offy += (y2 - children[i + 1].getY());
                        } else {
                            offy += panel.getHeight();
                        }
                    }

                    for (int j = i + 1; j < children.length; j++) {
                        children[i].setY(children[i].getY() + offy);
                    }

                    r.setBounds(0, 0, 0, 0);
                }
            } else {
                r = r.union(panel.getBounds());
            }
        }

        super.layout2(c);
    }
}


