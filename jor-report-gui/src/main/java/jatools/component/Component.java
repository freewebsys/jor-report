package jatools.component;

import jatools.accessor.PropertyAccessor;
import jatools.accessor.PropertyDescriptor;
import jatools.component.layout.LayoutManager;
import jatools.component.table.Cell;
import jatools.component.table.PowerTable;
import jatools.component.table.Table;
import jatools.core.view.Border;
import jatools.data.reader.AbstractDatasetReader;
import jatools.designer.data.Hyperlink;
import jatools.engine.script.Script;
import jatools.xml.serializer.ListenToXmlWrite;
import jatools.xml.serializer.XmlWriteListener;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Shape;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.8 $
  */
public abstract class Component implements PropertyAccessor, ListenToXmlWrite {
    static protected final Insets NULL_INSETS = new Insets(0, 0, 0, 0);

    /**
     * DOCUMENT ME!
     */
    public final static ThreadLocal temppropertylistener = new ThreadLocal();

    /**
     * DOCUMENT ME!
     */
    public static Script defaultDataProvider;
    private int x;
    private int y;
    private int width;
    private int height;
    private Color backColor;
    private String backgroundImageStyle;
    private Color foreColor = Color.black;
    private Border border;
    private String name;
    private ArrayList children = new ArrayList();
    private Map clientProperties;
    private String initPrint;
    private String beforePrint;
    private String afterPrint;

    private String printStyle;
    private int layer;
    private Component parent;
    private Cell cell;
    private Insets padding;
    protected boolean valid;
    private LayoutManager layout;
    protected PropertyChangeSupport changeSupport;
    private String tooltipText;
    private Hyperlink hyperlink;
    private String constraints;


    protected Component() {
    }

    protected Component(int x, int y, int width, int height) {
        this.setX(x);
        this.setY(y);
        this.setWidth(width);
        this.setHeight(height);
    }
    
    public void delete()
    {
    	if(this.parent != null)
    	{
    		this.parent.remove(this);
    	}
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getComponentByName(String name) {
        if (name.equals(this.getName())) {
            return this;
        } else {
            for (int i = 0; i < this.getChildCount(); i++) {
                Component c = this.getChild(i);

                if (c != null) {
                    c = c.getComponentByName(name);

                    if (c != null) {
                        return c;
                    }
                }
            }
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param results DOCUMENT ME!
     */
    public void getComponentsBySharedName(String name, java.util.List results) {
        if ((name != null) && (name.indexOf(name) > -1)) {
            results.add(this);
        }

        for (int i = 0; i < this.getChildCount(); i++) {
            Component c = this.getChild(i);

            if (c != null) {
                c.getComponentsBySharedName(name, results);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param results DOCUMENT ME!
     */
    public void getComponentsByName(String name, java.util.List results) {
        if (name.equals(this.getName())) {
            results.add(this);
        }

        for (int i = 0; i < this.getChildCount(); i++) {
            Component c = this.getChild(i);

            if (c != null) {
                c.getComponentsByName(name, results);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTooltipText() {
        return tooltipText;
    }

    /**
     * DOCUMENT ME!
     *
     * @param app DOCUMENT ME!
     */
    public void appendPrintStyle(String style) {
        String result = this.printStyle;

        if (result != null) {
            if (!result.endsWith(";")) {
                result += ";";
            }

            result += style;
        } else {
            result = style;
        }

        this.setPrintStyle(result);
    }

    /**
     * DOCUMENT ME!
     *
     * @param tooltipText DOCUMENT ME!
     */
    public void setTooltipText(String tooltipText) {
        this.tooltipText = tooltipText;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator iterator() {
        return getChildren().iterator();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getX2() {
        return getWidth() + getX();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isContainer() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getY2() {
        return getHeight() + getY();
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getClientProperty(String prop) {
        if (this.clientProperties == null) {
            return null;
        } else {
            return this.clientProperties.get(prop);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     * @param val DOCUMENT ME!
     */
    public void setClientProperty(String prop, Object val) {
        if (this.clientProperties == null) {
            this.clientProperties = new HashMap();
        }

        this.clientProperties.put(prop, val);
    }

    /**
     * DOCUMENT ME!
     *
     * @param var DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Object defaultVariableValue(String var) {
        return (defaultDataProvider != null) ? defaultDataProvider.get(var) : null;
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        if (changeSupport != null) {
            changeSupport.firePropertyChange(propertyName, oldValue, newValue);
        } else if (temppropertylistener.get() != null) {
            PropertyChangeListener listener = (PropertyChangeListener) temppropertylistener.get();
            listener.propertyChange(new PropertyChangeEvent(this, propertyName, oldValue, newValue));
        }
    }

    protected boolean hasPropertyChangeListener() {
        return (changeSupport != null) || (temppropertylistener.get() != null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param listener DOCUMENT ME!
     */
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport == null) {
            changeSupport = new PropertyChangeSupport(this);
        }

        changeSupport.addPropertyChangeListener(listener);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getChildCount() {
        return getChildren().size();
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getChild(int index) {
        return (Component) getChildren().get(index);
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     */
    public void add(Component child) {
        if ((child == null) || getChildren().contains(child)) {
            return;
        }

        getChildren().add(child);
        child.setParent(this);

        invalid();
    }

    /**
     * DOCUMENT ME!
     */
    public void invalid() {
        this.valid = false;

        if ((parent != null) && parent.valid) {
            parent.invalid();
        }
    }

    protected void doLayout() {
        LayoutManager l = this.getLayout();

        if (l != null) {
            l.layout(this);
        }
    }

    protected void doLayout2() {
        LayoutManager l = this.getLayout();

        if (l != null) {
            l.layout2(this);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void validate() {
        if (!this.valid) {
            this.doLayout();

            for (int i = 0; i < this.getChildCount(); i++) {
                this.getChild(i).validate();
            }

            this.doLayout2();
        }

        this.valid = true;
    }

    /**
     * DOCUMENT ME!
     */
    public void alwaysValidate() {
        this.doLayout();

        for (int i = 0; i < this.getChildCount(); i++) {
            this.getChild(i).alwaysValidate();
        }

        this.valid = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     * @param index DOCUMENT ME!
     */
    public void insert(Component child, int index) {
        if ((child == null) || getChildren().contains(child)) {
            return;
        }

        getChildren().add(index,child);
        child.setParent(this);
        invalid();
    }

    /**
     * DOCUMENT ME!
     */
    public void clear() {
        getChildren().clear();
    }

    /**
     * DOCUMENT ME!
     *
     * @param comp DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int indexOf(Component comp) {
        return getChildren().indexOf(comp);
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     */
    public void insert(Component child) {
        insert(child, 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     */
    public void remove(Component child) {
        getChildren().remove(child);
    }

    /**
     * DOCUMENT ME!
     *
     * @param child DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(Component child) {
        return getChildren().contains(child);
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public void setBounds(int x, int y, int width, int height, boolean invalid) {
        this.setX(x);
        this.setY(y);
        this.setWidth(width);
        this.setHeight(height);

        if (invalid) {
            this.invalid();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public void setBounds(int x, int y, int width, int height) {
        setBounds(x, y, width, height, true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param backColor DOCUMENT ME!
     */
    public void setBackColor(Color backColor) {
        if (this.backColor != backColor) {
            Color _backColor = this.backColor;
            this.backColor = backColor;

            if (hasPropertyChangeListener()) {
                this.firePropertyChange(ComponentConstants.PROPERTY_BACK_COLOR, _backColor,
                    backColor);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getBackColor() {
        return backColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param border DOCUMENT ME!
     */
    public void setBorder(Border border) {
        if (this.border != border) {
            Border _border = this.border;

            this.border = border;

            if (hasPropertyChangeListener()) {
                this.firePropertyChange(ComponentConstants.PROPERTY_BORDER, _border, border);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Border getBorder() {
        return border;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Rectangle getBounds() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     */
    public void getBounds(Rectangle r) {
        if (r != null) {
            r.setBounds(getX(), getY(), getWidth(), getHeight());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     */
    public void setBounds(Rectangle r) {
        this.setBounds(r, true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param r DOCUMENT ME!
     * @param invalid DOCUMENT ME!
     */
    public void setBounds(Rectangle r, boolean invalid) {
        if (r != null) {
            setX(r.x);
            setY(r.y);
            setWidth(r.width);
            setHeight(r.height);

            if (invalid) {
                this.invalid();
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param foreColor DOCUMENT ME!
     */
    public void setForeColor(Color foreColor) {
        if (this.foreColor != foreColor) {
            Color _foreColor = this.foreColor;
            this.foreColor = foreColor;

            if (hasPropertyChangeListener()) {
                this.firePropertyChange(ComponentConstants.PROPERTY_FORE_COLOR, _foreColor,
                    foreColor);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Color getForeColor() {
        return foreColor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param height DOCUMENT ME!
     */
    public void setHeight(int height) {
        if (this.height != height) {
            this.invalid();

            if (hasPropertyChangeListener()) {
                this.firePropertyChange(ComponentConstants.PROPERTY_HEIGHT,
                    new Integer(this.height), new Integer(height));
            }
        }

        this.height = height;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHeight() {
        return height;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     */
    public void setX(int x) {
        if (this.x != x) {
            this.invalid();

            if (hasPropertyChangeListener()) {
                this.firePropertyChange(ComponentConstants.PROPERTY_X, new Integer(this.x),
                    new Integer(x));
            }
        }

        this.x = x;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getX() {
        return x;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return name;
    }

    /**
     * DOCUMENT ME!
     *
     * @param y DOCUMENT ME!
     */
    public void setY(int y) {
        if (this.y != y) {
            this.invalid();

            if (hasPropertyChangeListener()) {
                this.firePropertyChange(ComponentConstants.PROPERTY_Y, new Integer(this.y),
                    new Integer(y));
            }
        }

        this.y = y;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getY() {
        return y;
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     */
    public void setWidth(int width) {
        if (this.width != width) {
            this.invalid();

            if (hasPropertyChangeListener()) {
                this.firePropertyChange(ComponentConstants.PROPERTY_WIDTH, new Integer(this.width),
                    new Integer(width));
            }
        }

        this.width = width;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWidth() {
        return width;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Insets getInsets() {
        if (border != null) {
            return border.getInsets();
        } else {
            return NULL_INSETS;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param parent DOCUMENT ME!
     */
    public void setParent(Component parent) {
        this.parent = parent;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getParent() {
        return parent;
    }

    protected void fill(Graphics2D g, Shape s) {
        if (getBackColor() != null) {
            g.setColor(getBackColor());
            g.fill(s);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLayer() {
        return layer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param layer DOCUMENT ME!
     */
    public void setLayer(int layer) {
        this.layer = layer;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public final ArrayList getChildren() {
        return children;
    }

    /**
     * DOCUMENT ME!
     *
     * @param children DOCUMENT ME!
     */
    public void setChildren(ArrayList children) {
        this.children = children;

        if (children != null) {
            for (int i = 0; i < children.size(); i++) {
                ((Component) children.get(i)).setParent(this);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Cell getCell() {
        return cell;
    }

    /**
     * DOCUMENT ME!
     *
     * @param cell DOCUMENT ME!
     */
    public void setCell(Cell cell) {
        this.cell = cell;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAfterPrint() {
        return afterPrint;
    }

    /**
     * DOCUMENT ME!
     *
     * @param afterPrint DOCUMENT ME!
     */
    public void setAfterPrint(String afterPrint) {
        this.afterPrint = afterPrint;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getBeforePrint() {
        return beforePrint;
    }

    /**
     * DOCUMENT ME!
     *
     * @param beforePrint DOCUMENT ME!
     */
    public void setBeforePrint(String beforePrint) {
        this.beforePrint = beforePrint;
    }

    /**
     * DOCUMENT ME!
     */
    public final void startLoad() {
    }

    /**
     * DOCUMENT ME!
     */
    public final void endLoad() {
    }

    /**
     * DOCUMENT ME!
     */
    public final void startSave() {
    }

    /**
     * DOCUMENT ME!
     */
    public final void endSave() {
    }

    

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Insets getPadding() {
        return (this.padding == null) ? NULL_INSETS : this.padding;
    }

    /**
     * DOCUMENT ME!
     *
     * @param padding DOCUMENT ME!
     */
    public void setPadding(Insets padding) {
        this.padding = padding;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public LayoutManager getLayout() {
        return layout;
    }

    /**
     * DOCUMENT ME!
     *
     * @param layout DOCUMENT ME!
     */
    public void setLayout(LayoutManager layout) {
        this.layout = layout;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public XmlWriteListener getXmlWriteListener() {
        return new ComponentXmlWriteListener(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    public void firePropertyChange(PropertyChangeEvent evt) {
        if (this.changeSupport != null) {
            changeSupport.firePropertyChange(evt);
        } else if (temppropertylistener.get() != null) {
            PropertyChangeListener listener = (PropertyChangeListener) temppropertylistener.get();
            listener.propertyChange(evt);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param listener DOCUMENT ME!
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (this.changeSupport != null) {
            changeSupport.removePropertyChangeListener(listener);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isValid() {
        return valid;
    }

    //
    //    /**
    //     * DOCUMENT ME!
    //     *
    //     * @return DOCUMENT ME!
    //     */
    //    public String getPrintIf() {
    //        return printIf;
    //    }
    //
    //    /**
    //     * DOCUMENT ME!
    //     *
    //     * @param printIf DOCUMENT ME!
    //     */
    //    public void setPrintIf(String printIf) {
    //        this.printIf = printIf;
    //    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getPrintStyle() {
        return printStyle;
    }

    /**
     * DOCUMENT ME!
     *
     * @param printCSS DOCUMENT ME!
     */
    public void setPrintStyle(String printCSS) {
        if (printCSS != this.printStyle) {
            String tmp = this.printStyle;
            this.printStyle = printCSS;
            this.firePropertyChange(ComponentConstants.PROPERTY_PRINT_STYLE, tmp, printCSS);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getMinHeight() {
        return getHeight();
    }

    /**
     * DOCUMENT ME!
     *
     * @param minHeight DOCUMENT ME!
     */
    public void setMinHeight(int minHeight) {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Hyperlink getHyperlink() {
        return hyperlink;
    }

    /**
     * DOCUMENT ME!
     *
     * @param hyperlink DOCUMENT ME!
     */
    public void setHyperlink(Hyperlink hyperlink) {
        this.hyperlink = hyperlink;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getBackgroundImageStyle() {
        return backgroundImageStyle;
    }

    /**
     * DOCUMENT ME!
     *
     * @param backgroundImage DOCUMENT ME!
     */
    public void setBackgroundImageStyle(String backgroundImage) {
        if (backgroundImage != this.backgroundImageStyle) {
            String tmp = this.backgroundImageStyle;
            this.backgroundImageStyle = backgroundImage;
            this.firePropertyChange(ComponentConstants.PROPERTY_PRINT_STYLE, tmp, backgroundImage);
        }
    }

    //    void sortProperties() {
    //        String[][] src = {
    //                new String[] {
    //                    "Name",
    //                    "_NAME"
    //                },
    //                new String[] {
    //                    "Variable",
    //                    "_VARIABLE"
    //                },
    //                new String[] {
    //                    "ImageSrc",
    //                    "_IMAGE_SRC"
    //                },
    //                new String[] {
    //                    "NodePath",
    //                    "_NODE_PATH"
    //                },
    //                new String[] {
    //                    "Text",
    //                    "_TEXT"
    //                },
    //                new String[] {
    //                    "BackColor",
    //                    "_BACK_COLOR"
    //                },
    //                new String[] {
    //                    "ForeColor",
    //                    "_FORE_COLOR"
    //                },
    //                new String[] {
    //                    "Border",
    //                    "_BORDER"
    //                },
    //                new String[] {
    //                    "Font",
    //                    "_FONT"
    //                },
    //                new String[] {
    //                    "HorizontalAlignment",
    //                    "_HORIZONTAL_ALIGNMENT"
    //                },
    //                new String[] {
    //                    "VerticalAlignment",
    //                    "_VERTICAL_ALIGNMENT"
    //                },
    //                new String[] {
    //                    "Format",
    //                    "_FORMAT"
    //                },
    //                new String[] {
    //                    "Wordwrap",
    //                    "_WORDWRAP"
    //                },
    //                new String[] {
    //                    "CharWidth",
    //                    "_CHAR_WIDTH"
    //                },
    //                new String[] {
    //                    "CodeType",
    //                    "_CODE_TYPE"
    //                },
    //                new String[] {
    //                    "Stretches",
    //                    "_STRECTHES"
    //                },
    //                new String[] {
    //                    "LinePattern",
    //                    "_LINE_PATTERN"
    //                },
    //                new String[] {
    //                    "LineSize",
    //                    "_LINE_SIZE"
    //                },
    //                new String[] {
    //                    "RequiredHtmlImageFormat",
    //                    "_REQUIRED_HTML_IMAGE_FORMAT"
    //                },
    //                new String[] {
    //                    "BackgroundImageStyle",
    //                    "_BACKGROUND_IMAGE"
    //                },
    //                new String[] {
    //                    "Hyperlink",
    //                    "_HYPERLINK"
    //                },
    //                new String[] {
    //                    "TooltipText",
    //                    "_TOOLTIP_TEXT"
    //                },
    //                new String[] {
    //                    "PrintStyle",
    //                    "_PRINT_STYLE"
    //                },
    //                new String[] {
    //                    "StyleClass",
    //                    "_STYLE_CLASS"
    //                },
    //                new String[] {
    //                    "X",
    //                    "_X"
    //                },
    //                new String[] {
    //                    "Y",
    //                    "_Y"
    //                },
    //                new String[] {
    //                    "Width",
    //                    "_WIDTH"
    //                },
    //                new String[] {
    //                    "Height",
    //                    "_HEIGHT"
    //                },
    //                new String[] {
    //                    "Children",
    //                    "_CHILDREN"
    //                },
    //                new String[] {
    //                    "ColumnWidths",
    //                    "_COLUMN_WIDTHS"
    //                },
    //                new String[] {
    //                    "RowHeights",
    //                    "_ROW_HEIGHTS"
    //                },
    //                new String[] {
    //                    "RightFlow",
    //                    "_RIGHT_FLOW"
    //                },
    //                new String[] {
    //                    "Type",
    //                    "_TYPE"
    //                },
    //                new String[] {
    //                    "RowkeyExpressions",
    //                    "_ROW_KEY_EXPRESSIONS"
    //                },
    //                new String[] {
    //                    "ColumnkeyExpressions",
    //                    "_COLUMN_KEY_EXPRESSIONS"
    //                },
    //                new String[] {
    //                    "PageFormat",
    //                    "_PAGE_FORMAT"
    //                },
    //                new String[] {
    //                    "Properties",
    //                    "_PROPERTIES"
    //                },
    //                new String[] {
    //                    "Reader",
    //                    "_READER"
    //                },
    //                new String[] {
    //                    "LabelField",
    //                    "_LABEL_FIELD"
    //                },
    //                new String[] {
    //                    "PlotData",
    //                    "_PLOT_DATA"
    //                },
    //                new String[] {
    //                    "Cell",
    //                    "_CELL"
    //                },
    //                new String[] {
    //                    "AfterPrint",
    //                    "_AFTERPRINT"
    //                },
    //                new String[] {
    //                    "BeforePrint",
    //                    "_BEFOREPRINT"
    //                }
    //            };
    //        String[] names = new String[src.length];
    //
    //        for (int i = 0; i < src.length; i++) {
    //            names[i] = src[i][0];
    //        }
    //
    //        String[] copy = new String[names.length];
    //
    //        PropertyDescriptor[] props = getRegistrableProperties();
    //
    //        for (int j = 0; j < props.length; j++) {
    //            //	System.out.println(props[j].getPropertyName());
    //            int i = ArrayUtils.indexOf(names, props[j].getPropertyName());
    //
    //            if (i == -1) {
    //                System.err.println(props[j].getPropertyName());
    //            }
    //
    //            copy[i] = "ComponentConstants." + src[i][1];
    //        }
    //
    //        System.out.println("<<<<===============" + this.getClass().getName());
    //
    //        System.out.println("return new PropertyDescriptor[] {");
    //
    //        for (int i = 0; i < copy.length; i++) {
    //            if (copy[i] != null) {
    //                if (i > 0) {
    //                    System.out.print(",");
    //                }
    //
    //                System.out.println(copy[i]);
    //            }
    //        }
    //
    //        System.out.println("};");
    //
    //        System.out.println("===============>>>>>");
    //    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main2(String[] args) {
        //new String[]{
        //    	Name
        //
        
        //
        //    	Variable
        //    	ImageSrc
        //    	NodePath
        //    	Text
        //
        //
        
        //    	BackColor
        //    	ForeColor
        //    	Border
        //
        
        //
        //    	Font
        //    	HorizontalAlignment
        //    	VerticalAlignment
        //    	Format
        //    	Wordwrap
        //    	CharWidth
        //
        //
        
        //    	CodeType
        //
        
        //    	Stretches
        //
        
        //    	LinePattern
        //    	LineSize
        //
        //
        
        //
        //    	RequiredHtmlImageFormat
        //    	BackgroundImageStyle
        //    	Hyperlink
        //    	TooltipText
        //
        
        //    	PrintStyle
        //    	DisplayStyleRef
        //
        //
        
        //    	X
        //    	Y
        //    	Width
        //    	Height
        //
        
        //    	AfterPrint
        //    	BeforePrint
        //
        //
        
        //    	Children
        //
        //    	ColumnWidths
        //    	RowHeights
        //    	RightFlow
        //    	Type
        //    	RowkeyExpressions
        //    	ColumnkeyExpressions
        //    	PageFormat
        //    	GraphProperties
        //    	GraphReader
        //    	GraphLabelField
        //    	GraphShowData
        String[] names = new String[] {
                "Name", "Variable", "ImageSrc", "NodePath", "Text", "BackColor", "ForeColor",
                "Border", "Font", "HorizontalAlignment", "VerticalAlignment", "Format", "Wordwrap",
                "CharWidth", "CodeType", "Stretches", "LinePattern", "LineSize",
                "RequiredHtmlImageFormat", "BackgroundImageStyle", "Hyperlink", "TooltipText",
                "PrintStyle", "DisplayStyleRef", "X", "Y", "Width", "Height", "AfterPrint",
                "BeforePrint", "Children", "ColumnWidths", "RowHeights", "RightFlow", "Type",
                "RowkeyExpressions", "ColumnkeyExpressions", "PageFormat", "GraphProperties",
                "GraphReader", "GraphLabelField", "GraphShowData"
            };
        Component[] comps = new Component[] {
                  new Image(), new Label(), 
                new Panel(), new PowerTable(),
                
                 new Table(), new Text(), new Page()
            };

        ArrayList list = new ArrayList();

        for (int i = 0; i < comps.length; i++) {
            PropertyDescriptor[] props = comps[i].getRegistrableProperties();

            for (int j = 0; j < props.length; j++) {
                if (!list.contains(props[j].getPropertyName())) {
                    list.add(props[j].getPropertyName());
                }
            }
        }

        Iterator it = list.iterator();

        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        //        Component[] comps = new Component[] {
        //                new BarCode(),
        //                new Chart(),
        //                new GridText(),
        //                new Image(),
        //                new Label(),
        //                new Line(),
        //                new List(),
        //                new Panel(),
        //                new PowerTable(),
        //                
        //                new StaticTable(),
        //                new Table(),
        //                new Text(),
        //                new Page()
        //            };
        //
        //        for (int i = 0; i < comps.length; i++) {
        //            comps[i].sortProperties();
        //        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getInitPrint() {
        return initPrint;
    }

    /**
     * DOCUMENT ME!
     *
     * @param initPrint DOCUMENT ME!
     */
    public void setInitPrint(String initPrint) {
        this.initPrint = initPrint;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getConstraints() {
        return constraints;
    }

    /**
     * DOCUMENT ME!
     *
     * @param constraints DOCUMENT ME!
     */
    public void setConstraints(String constraints) {
        this.constraints = constraints;
    }

//	public AbstractDatasetReader getReader() {
////		Connection conn = new Connection();
////		
////		new SqlReader(conn,"select name,area from country ");
//		
//		return null;
//	}

    
}
