package jatools.component.layout;

import jatools.component.Component;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class FreeLayout implements LayoutManager {
    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void layout(Component c) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void layout2(Component c) {
        Rectangle r = new Rectangle();

        ArrayList constraints = null;

        for (int i = c.getChildCount() - 1; i >= 0; i--) {
            r = r.union(c.getChild(i).getBounds());

            Component child = c.getChild(i);

            if ((child.getConstraints() != null) && child.getConstraints().equals("center")) {
                if (constraints == null) {
                    constraints = new ArrayList();
                }

                constraints.add(child);
            }
        }

        int max = c.getHeight();

        c.setMinHeight(r.height);

        if ((r.height + c.getPadding().top + c.getPadding().bottom) > c.getHeight()) {
            c.setHeight(r.height + c.getPadding().top + c.getPadding().bottom);
        }

        if (constraints != null) {
            Iterator it = constraints.iterator();

            while (it.hasNext()) {
                Component child = (Component) it.next();

                child.setBounds(0, 0, c.getWidth(), max, false);
            }
        }
    }
}
