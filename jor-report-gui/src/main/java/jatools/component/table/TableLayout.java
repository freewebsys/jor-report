package jatools.component.table;

import jatools.component.Component;
import jatools.component.Size;
import jatools.component.layout.LayoutManager;

import java.awt.Point;
import java.awt.Rectangle;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class TableLayout implements LayoutManager {
    /**
            * DOCUMENT ME!
            *
            * @param parent DOCUMENT ME!
            */
    public void layout(Component c) {
        TableBase t = (TableBase) this.getRootTable(c);

        if (t == c) {
            calclulateGrid(t);
        }

        Point off = offset(c, t);

        for (int i = 0; i < c.getChildCount(); i++) {
            Component child = c.getChild(i);

            Cell cell = child.getCell();

            int x = t.getColumnX(cell.column) - off.x;
            int y = t.getRowY(cell.row) - off.y;

            int width = t.getWidth(cell.column, cell.colSpan);
            int height = t.getHeight(cell.row, cell.rowSpan);
            child.setBounds(new Rectangle(x, y, width, height));
        }

        if (c == t) {
            Rectangle r = t.getBounds(0, 0, t.getColumnCount(), t.getRowCount());
            t.setWidth(r.width);
            t.setHeight(r.height);
        }
    }

    private void calclulateGrid(TableBase t) {
        Size size = new Size(t.getRowHeights());
        CellStore store = t.getCellstore();

        for (int r = 0; r < t.getRowCount(); r++) {
            for (int c = 0; c < t.getColumnCount(); c++) {
                Component child = store.getComponent(r, c);

                if (child != null) {
                    child.validate();

                    if (child.isContainer() && (child.getMinHeight() > size.getSize(r))) {
                        if (child.getCell().rowSpan == 1) {
                            size.setSize(r, child.getMinHeight());
                        } else {
                            int d = size.getSize(r, child.getCell().rowSpan) -
                                child.getMinHeight();

                            if (d < 0) {
                                
                                size.setSize(r, size.getSize(r) - d);
                            }
                        }
                    }
                }
            }
        }

        t.setRowHeights(size.getSizes());
    }

    TableBase getRootTable(Component c) {
        while (c.getCell() != null) {
            c = c.getParent();
        }

        if (c instanceof TableBase) {
            return (TableBase) c;
        } else {
            return null;
        }
    }

    Point offset(Component c, TableBase t) {
        Point off = new Point();

        while (t != c) {
            off.x += c.getX();
            off.y += c.getY();

            c = c.getParent();
        }

        return off;
    }

    /**
     * DOCUMENT ME!
     *
     * @param comp DOCUMENT ME!
     */
    public void layout2(Component comp) {
        // TODO Auto-generated method stub
    }
}
