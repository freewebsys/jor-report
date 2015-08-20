package jatools.component.table;

import jatools.accessor.PropertyDescriptor;
import jatools.component.Component;
import jatools.component.ComponentConstants;

import java.util.ArrayList;
import java.util.Iterator;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
  */
public class PowerTable extends TableBase implements PowerTableBase, GLayout {
    public final static Object CURRENT_ROW_KEY = new Object();
    final static TableLayout GRID_LAYOUT = new TableLayout();
    String variable;
    HeaderTable leftHeader;
    HeaderTable topHeader;
   // boolean expandable;


    /**
     * Creates a new PowerTable object.
     */
    public PowerTable() {
        this.setLayout(GRID_LAYOUT);
    }

    /**
    * Creates a new PowerTable object.
    *
    * @param columns DOCUMENT ME!
    * @param rows DOCUMENT ME!
    */
    private PowerTable(int[] rows, int[] columns) {
        super(rows, columns);
        this.setLayout(GRID_LAYOUT);
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     */
    public void add(Component child) {
        super.add(child);

        setHeaderTable(child);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLeftHeaderColumns() {
        return getLeftHeader().getColumnCount();
    }

    private void setHeaderTable(Component child) {
        if (child instanceof HeaderTable) {
            if (((HeaderTable) child).isRightFlow()) {
                topHeader = (HeaderTable) child;
            } else {
                leftHeader = (HeaderTable) child;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param children DOCUMENT ME!
     */
    public void setChildren(ArrayList children) {
        super.setChildren(children);

        if (children != null) {
            Iterator it = children.iterator();

            while (it.hasNext()) {
                setHeaderTable((Component) it.next());
            }
        }
    }

    /**
    * DOCUMENT ME!
    *
    * @param leftTable DOCUMENT ME!
    * @param topTable DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public static PowerTable create(Table leftTable, Table topTable) {
        
        
        int[] leftRows = leftTable.getRowHeights();
        int[] leftColumns = leftTable.getColumnWidths();
        int[] topRows = topTable.getRowHeights();
        int[] topColumns = topTable.getColumnWidths();

        int[] rows = new int[leftRows.length + topRows.length];
        System.arraycopy(topRows, 0, rows, 0, topRows.length);
        System.arraycopy(leftRows, 0, rows, topRows.length, leftRows.length);

        int[] columns = new int[leftColumns.length + topColumns.length];
        System.arraycopy(leftColumns, 0, columns, 0, leftColumns.length);
        System.arraycopy(topColumns, 0, columns, leftColumns.length, topColumns.length);

        PowerTable p = new PowerTable(rows, columns);

        leftTable.translate(topRows.length, 0);

        HeaderTable leftHeader = HeaderTable.create(leftTable);

        leftHeader.setCell(new Cell(topRows.length, 0, leftColumns.length, leftRows.length));
        p.add(leftHeader);

        HeaderTable topHeader = HeaderTable.create(topTable);
        topHeader.translate(0, leftColumns.length);
        topHeader.setCell(new Cell(0, leftColumns.length, topColumns.length, topRows.length));
        topHeader.setRightFlow(true);
        p.add(topHeader);

        return p;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] {
            ComponentConstants._NAME, ComponentConstants._BACK_COLOR, ComponentConstants._BORDER,
            ComponentConstants._BACKGROUND_IMAGE, ComponentConstants._PRINT_STYLE,
            ComponentConstants._X, ComponentConstants._Y, ComponentConstants._WIDTH,
            ComponentConstants._HEIGHT, ComponentConstants._CHILDREN,
            ComponentConstants._COLUMN_WIDTHS, ComponentConstants._ROW_HEIGHTS,
            ComponentConstants._CELL, ComponentConstants._INIT_PRINT, ComponentConstants._AFTERPRINT,
            ComponentConstants._BEFOREPRINT2
        };
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getVariable() {
        return variable;
    }

    /**
     * DOCUMENT ME!
     *
     * @param variable DOCUMENT ME!
     */
    public void setVariable(String variable) {
        this.variable = variable;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public HeaderTable getLeftHeader() {
        return leftHeader;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public HeaderTable getTopHeader() {
        return topHeader;
    }

    /**
     * DOCUMENT ME!
     *
     * @param groups DOCUMENT ME!
     */
    public void layoutGroup(String[] groups) {
        try {
            PowerTableBuilder.layoutLeftHeaderGroup(this, groups);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

//	public boolean isExpandable() {
//		return expandable;
//	}
//
//	public void setExpandable(boolean expandable) {
//		this.expandable = expandable;
//	}
}
