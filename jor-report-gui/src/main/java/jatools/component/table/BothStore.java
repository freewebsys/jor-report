package jatools.component.table;

import jatools.component.Component;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class BothStore {
    TableBase grid;

    /**
     * Creates a new CellStore object.
     *
     * @param grid DOCUMENT ME!
     */
    public BothStore(TableBase grid) {
        this.grid = grid;
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator iterator() {
        ArrayList v = new ArrayList();
        visit(grid, v);

        return v.iterator();
    }

    private void visit(Component c, ArrayList v) {
        for (int i = 0; i < c.getChildCount(); i++) {
            Component child = c.getChild(i);

            if (child instanceof GridComponent) {
                visit(child, v);
            }

            v.add(child);
        }
    }
}
