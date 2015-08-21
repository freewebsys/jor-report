package jatools.component.table;

import jatools.accessor.PropertyDescriptor;
import jatools.component.ComponentConstants;
import jatools.engine.printer.NodePathTarget;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.6 $
 */
public class Table extends TableBase implements NodePathTarget {
    /**
     * DOCUMENT ME!
     */
    final public static TableLayout GRID_LAYOUT = new TableLayout();
    boolean rightFlow;

    /**
     * Creates a new Grid object.
     */
    public Table() {
        this.setLayout(GRID_LAYOUT);
    }

    /**
     * Creates a new Grid object.
     *
     * @param columns DOCUMENT ME!
     * @param rows DOCUMENT ME!
     */
    public Table(int[] rows, int[] columns) {
        super(rows, columns);
        this.setLayout(GRID_LAYOUT);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
//    public TableBuilder getBuilder() {
//        return new TableBuilder(this);
//    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] {
            ComponentConstants._NAME,
            ComponentConstants._NODE_PATH,
            ComponentConstants._BACK_COLOR,
            ComponentConstants._BORDER,
            ComponentConstants._BACKGROUND_IMAGE,
            ComponentConstants._PRINT_STYLE,
            ComponentConstants._X,
            ComponentConstants._Y,
            ComponentConstants._WIDTH,
            ComponentConstants._HEIGHT,
            ComponentConstants._CHILDREN,
            ComponentConstants._COLUMN_WIDTHS,
            ComponentConstants._ROW_HEIGHTS,
            ComponentConstants._RIGHT_FLOW,
            ComponentConstants._CELL,
			ComponentConstants._INIT_PRINT,
            ComponentConstants._AFTERPRINT,
            ComponentConstants._BEFOREPRINT2
        };
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isRightFlow() {
        return rightFlow;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rightFlow DOCUMENT ME!
     */
    public void setRightFlow(boolean rightFlow) {
        this.rightFlow = rightFlow;
    }
}
