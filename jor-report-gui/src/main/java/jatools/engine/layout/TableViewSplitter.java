package jatools.engine.layout;

import jatools.component.Size;
import jatools.component.table.Cell;
import jatools.core.view.AbstractView;
import jatools.core.view.View;
import jatools.engine.css.rule.CrossTabRule;

import java.awt.Rectangle;
import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class TableViewSplitter {
    private TableView grid;
    AbstractView[][] viewstore;
    private CrossTabRule rule;

    /**
     * Creates a new TableViewSplitter object.
     *
     * @param grid DOCUMENT ME!
     * @param rule DOCUMENT ME!
     */
    public TableViewSplitter(TableView grid, CrossTabRule rule) {
        this.grid = grid;
        this.rule = rule;
    }

    /**
     * DOCUMENT ME!
     *
     * @param sel DOCUMENT ME!
     * @param copyleft DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TableView selectColumn(Rectangle sel, int copyleft) {
        int lastx = sel.x + sel.width;

        if (grid.isLeftOf(lastx) || !rule.isTrue(rule.pageWrap, true)) {
            return grid;
        } else {
            if (!rule.is(rule.leftHeaderVisible, "everypage", true)) {
                copyleft = 0;
            }

            Size columns = grid.getColumns();

            int lastcol = columns.length() - 1;

            while ((lastcol >= 0) && (columns.getPosition(lastcol) > lastx)) {
                lastcol--;
            }

            lastcol--;

            int[] oldcols = columns.getSizes();
            int[] oldrows = this.grid.getRows().getSizes();
            int[] newcols = new int[lastcol + 1];

            System.arraycopy(oldcols, 0, newcols, 0, newcols.length);

            TableView newgrid = new TableView(oldrows, newcols,grid.getName());
            newgrid.setBounds((Rectangle) grid.getBounds().clone());

            Iterator it = grid.elementCache.iterator();

            while (it.hasNext()) {
                AbstractView a = (AbstractView) it.next();

                Cell cell = a.getCell();

                if (cell.column2() <= lastcol) {
                    if (cell.column2() < copyleft) {
                        try {
                            newgrid.add((View) a.clone());
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        newgrid.add(a);
                        it.remove();
                    }
                } else if (a.getCell().column > lastcol) {
                    a.getCell().column -= (lastcol + 1);
                    a.getCell().column += copyleft;
                } else {
                    try {
                        AbstractView newview = (AbstractView) a.clone();
                        Cell r = newview.getCell();

                        r.colSpan = (lastcol + 1) - r.column;

                        newgrid.add(newview);

                        r = a.getCell();
                        r.colSpan = r.column2() - lastcol;
                        r.column = copyleft;
                    } catch (CloneNotSupportedException e) {
                        e.printStackTrace();
                    }
                }
            }

            if (copyleft > 0) {
                newcols = new int[oldcols.length - newcols.length + copyleft];

                System.arraycopy(oldcols, 0, newcols, 0, copyleft);

                System.arraycopy(oldcols, lastcol + 1, newcols, copyleft, newcols.length -
                    copyleft);
            } else {
                newcols = new int[oldcols.length - newcols.length];
                System.arraycopy(oldcols, lastcol + 1, newcols, 0, newcols.length);
            }

            grid.getColumns().setSizes(newcols);

            newgrid.setBackColor(grid.getBackColor());
            newgrid.setBackgroundImageStyle(grid.getBackgroundImageStyle());
            newgrid.setBorder(grid.getBorder());
            newgrid.setLayoutRule(grid.getLayoutRule());

            return newgrid;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param col DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractView getComponent(int row, int col) {
        AbstractView c = this.getComponents()[row][col];

        if ((c != null) && (c.getBounds().y == row) && (c.getBounds().x == col)) {
            return c;
        } else {
            return null;
        }
    }

    private AbstractView[][] getComponents() {
        if (this.viewstore == null) {
            this.viewstore = populateComponent();
        }

        return viewstore;
    }

    private AbstractView[][] populateComponent() {
        AbstractView[][] to = new AbstractView[this.grid.getRows().length()][this.grid.getColumns()
                                                                                      .length()];

        Iterator it = grid.getElements().iterator();

        while (it.hasNext()) {
            AbstractView a = (AbstractView) it.next();
            Rectangle b = a.getBounds();

            if (b != null) {
                for (int j = b.y; j < (b.y + b.height); j++) {
                    for (int k = b.x; k < (b.x + b.width); k++) {
                        to[j][k] = a;
                    }
                }
            }
        }

        return to;
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public AbstractView getComponentOver(int row, int column) {
        return getComponents()[row][column];
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TableView copyRowsBefore(int row) {
        int[] oldcols = this.grid.getColumns().getSizes();

        int[] oldrows = this.grid.getRows().getSizes();

        if (row == -1) {
            row = oldrows.length;
        }

        int[] newrows = new int[row];
        System.arraycopy(oldrows, 0, newrows, 0, newrows.length);

        TableView newgrid = new TableView(newrows, oldcols,this.grid.getName());
        newgrid.setBounds((Rectangle) grid.getBounds().clone());

        Iterator it = grid.elementCache.iterator();

        while (it.hasNext()) {
            AbstractView a = (AbstractView) it.next();
            Cell cell = a.getCell();

            if (cell.row2() < row) {
                try {
                    newgrid.add((View) a.clone());
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
            }
        }

        return newgrid;
    }
}
