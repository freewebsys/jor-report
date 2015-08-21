package jatools.core.view;

import jatools.component.BackgroundImageStyle;

import jatools.component.table.Cell;

import jatools.engine.css.rule.LayoutRule;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Rectangle;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public abstract class AbstractView implements View, Cloneable {
    protected Rectangle bounds;
    protected Cell cell;
    String hyperlink;
    String tooltipText;
    private LayoutRule layoutRule;
    private BackgroundImageStyle backgroundImageStyle;
    private Border border;
    private Color backColor;
    protected String name;
    private boolean draggable;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Rectangle getBounds() {
        return bounds;
    }

    /**
     * DOCUMENT ME!
     */
    public void doLayout() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isAutoSize() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getHyperlink() {
        return hyperlink;
    }

    /**
     * DOCUMENT ME!
     *
     * @param url DOCUMENT ME!
     */
    public void setHyperlink(String url) {
        this.hyperlink = url;
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
     * @param tooltipText DOCUMENT ME!
     */
    public void setTooltipText(String tooltipText) {
        this.tooltipText = tooltipText;
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPreferedHeight(int width) {
        return -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param height DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPreferedWidth(int height) {
        return -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws CloneNotSupportedException DOCUMENT ME!
     */
    public Object clone() throws CloneNotSupportedException {
        AbstractView a = (AbstractView) super.clone();

        if (bounds != null) {
            a.bounds = (Rectangle) bounds.clone();
        }

        if (cell != null) {
            a.cell = (Cell) cell.clone();
        }

        return a;
    }

    /**
     * DOCUMENT ME!
     *
     * @param b DOCUMENT ME!
     * @param ins DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Rectangle deflateRect(Rectangle b, Insets ins) {
        b.x += ins.left;
        b.y += ins.top;
        b.width -= (ins.left + ins.right);
        b.height -= (ins.top + ins.bottom);

        if (b.width < 0) {
            b.width = 0;
        }

        if (b.height < 0) {
            b.height = 0;
        }

        return b;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bounds DOCUMENT ME!
     */
    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
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
    public BackgroundImageStyle getBackgroundImageStyle() {
        return backgroundImageStyle;
    }

    /**
     * DOCUMENT ME!
     *
     * @param backgroundImageStyle DOCUMENT ME!
     */
    public void setBackgroundImageStyle(BackgroundImageStyle backgroundImageStyle) {
        this.backgroundImageStyle = backgroundImageStyle;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public LayoutRule getLayoutRule() {
        return layoutRule;
    }

    /**
     * DOCUMENT ME!
     *
     * @param layoutRule DOCUMENT ME!
     */
    public void setLayoutRule(LayoutRule layoutRule) {
        this.layoutRule = layoutRule;
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
     * @param border DOCUMENT ME!
     */
    public void setBorder(Border border) {
        this.border = border;
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
     * @param backColor DOCUMENT ME!
     */
    public void setBackColor(Color backColor) {
        this.backColor = backColor;
    }

    /**
     * DOCUMENT ME!
     */
    public void autoResize() {
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
    public DisplayStyle getDisplayStyle() {
        return null;
    }
}
