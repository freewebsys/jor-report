package jatools.engine.printer.table;

import jatools.component.ColumnPanel;
import jatools.component.Component;

import jatools.component.table.Cell;
import jatools.component.table.RowPanel;

import jatools.engine.printer.CellComparator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PrintablePanelBuilder {
    Component parent;
    Cell cell;

    /**
     * Creates a new PrintableListBuilder object.
     *
     * @param cc DOCUMENT ME!
     * @param cell DOCUMENT ME!
     */
    public PrintablePanelBuilder(Component cc, Cell cell) {
        this.parent = cc;
        this.cell = cell;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ColumnPanel[] buildColumnList() {
        int[] cols = new int[cell.colSpan];

        ArrayList children = new ArrayList();

        for (int i = 0; i < parent.getChildCount(); i++) {
            Component c = parent.getChild(i);

            if (c instanceof ColumnPanel) {
                Cell cel = c.getCell();
                Arrays.fill(cols, cel.column - cell.column, cel.column - cell.column + cel.colSpan,
                    1);
                children.add(c);
            }
        }

        for (int i = 0; i < cols.length; i++) {
            for (; (i < cols.length) && (cols[i] != 0); i++)
                ;

            int col = i;
            int span = 0;

            for (; (i < cols.length) && (cols[i] == 0); i++, span++)
                ;

            if (span > 0) {
                ColumnPanel logic = new ColumnPanel();
                logic.setParent(parent);
                logic.setCell(new Cell(cell.row, col + cell.column, span, cell.rowSpan));
                children.add(logic);
            }
        }

        Collections.sort(children, new CellComparator());

        return (ColumnPanel[]) children.toArray(new ColumnPanel[0]);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RowPanel[] buildRowList() {
        int[] rows = new int[cell.rowSpan];

        ArrayList children = new ArrayList();

        for (int i = 0; i < parent.getChildCount(); i++) {
            Component c = parent.getChild(i);

            if ((c != null) && (c.getClass() == RowPanel.class)) {
                Cell cel = c.getCell();
                Arrays.fill(rows, cel.row - cell.row, cel.row - cell.row + cel.rowSpan, 1);
                children.add(c);
            }
        }

        for (int i = 0; i < rows.length; i++) {
            for (; (i < rows.length) && (rows[i] != 0); i++)
                ;

            int row = i;
            int span = 0;

            for (; (i < rows.length) && (rows[i] == 0); i++, span++)
                ;

            if (span > 0) {
                RowPanel logic = new RowPanel();
                logic.setParent(parent);
                logic.setCell(new Cell(row + cell.row, cell.column, cell.colSpan, span));
                children.add(logic);
            }
        }

        Collections.sort(children, new CellComparator());

        return (RowPanel[]) children.toArray(new RowPanel[0]);
    }
}
