package jatools.component;

import jatools.accessor.PropertyDescriptor;
import jatools.component.layout.FreeLayout;
import jatools.component.layout.LayoutManager;
import jatools.component.layout.PanelColumnLayout;
import jatools.component.layout.PanelRowLayout;
import jatools.engine.printer.NodePathTarget;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.9 $
  */
public class Panel extends Component implements NodePathTarget {
    /**
     * DOCUMENT ME!
     */
    public static final int FREE_LAYOUT = 0;

    /**
     * DOCUMENT ME!
     */
    public static final int ROW_LAYOUT = 1;

    /**
     * DOCUMENT ME!
     */
    public static final int COLUMN_LAYOUT = 2;
    private static LayoutManager freelayout = new FreeLayout();
    private static LayoutManager rowlayout = new PanelRowLayout();
    private static LayoutManager columnlayout = new PanelColumnLayout();
    String nodePath;
    private int layoutType;
    private int type;
    private int minHeight;

    /**
     * Creates a new PagePanel object.
     *
     * @param layout
     *            DOCUMENT ME!
     * @param i
     *            DOCUMENT ME!
     */
    public Panel() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isContainer() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param variable DOCUMENT ME!
     */
    public void setNodePath(String variable) {
        this.nodePath = variable;
    }

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
//            ComponentConstants._DRAGGABLE,
            ComponentConstants._X,
            ComponentConstants._Y,
            ComponentConstants._WIDTH,
            ComponentConstants._HEIGHT,
            ComponentConstants._CHILDREN,
            ComponentConstants._TYPE,
            ComponentConstants._CELL,
            ComponentConstants._CONSTRAINTS,
         
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
    public LayoutManager getLayout() {
        if ("rowlayout".equals(this.getName())) {
            return rowlayout;
        } else if (getLayoutType() == COLUMN_LAYOUT) {
            return columnlayout;
        } else {
            return freelayout;
        }
    }

    protected int getLayoutType() {
        return layoutType;
    }

    protected void setLayoutType(int layoutType) {
        this.layoutType = layoutType;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getType() {
        return type;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     */
    public void setType(int type) {
        this.type = type;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMinHeight() {
        return this.minHeight;
    }

    /**
     * DOCUMENT ME!
     *
     * @param minheight DOCUMENT ME!
     */
    public void setMinHeight(int minheight) {
        this.minHeight = minheight;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getNodePath() {
        return nodePath;
    }
}
