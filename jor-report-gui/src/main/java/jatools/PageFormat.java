package jatools;

import jatools.accessor.PropertyAccessor;
import jatools.accessor.PropertyDescriptor;
import jatools.component.ComponentConstants;
import jatools.engine.export.html.HtmlExport;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.print.Paper;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PageFormat extends Insets implements PropertyAccessor {
    public static final int LANDSCAPE = 0;
    public static final int PORTRAIT = 1;
    public static final float DOTS_PER_PX = 0.75f;
    private static final long serialVersionUID = 20080100L;
    private int orientation = java.awt.print.PageFormat.PORTRAIT;
    private int width = 500;
    private int height = 800;
    private int printHeight;

    /**
     * Creates a new PageFormat object.
     */
    public PageFormat() {
        super(20, 20, 20, 20);
    }

    /**
     * Creates a new PageFormat object.
     *
     * @param size DOCUMENT ME!
     */
    public PageFormat(Dimension size) {
        super(20, 20, 20, 20);
        this.width = size.width;
        this.height = size.height;
    }

    /**
     * Creates a new PageFormat object.
     *
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     * @param top DOCUMENT ME!
     * @param left DOCUMENT ME!
     * @param bottom DOCUMENT ME!
     * @param right DOCUMENT ME!
     */
    public PageFormat(int width, int height, int top, int left, int bottom, int right) {
        super(top, left, bottom, right);
        this.width = width;
        this.height = height;
    }

    /**
     * Creates a new PageFormat object.
     *
     * @param format DOCUMENT ME!
     */
    public PageFormat(java.awt.print.PageFormat format) {
        this(asInsets(format));
        this.width = (int) format.getWidth();
        this.height = (int) format.getHeight();
        this.orientation = format.getOrientation();
    }

    /**
     * Creates a new PageFormat object.
     *
     * @param is DOCUMENT ME!
     */
    public PageFormat(Insets is) {
        super(is.top, is.left, is.bottom, is.right);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] {
            ComponentConstants._WIDTH, ComponentConstants._HEIGHT, ComponentConstants._LEFT,
            ComponentConstants._TOP, ComponentConstants._RIGHT, ComponentConstants._BOTTOM,
            ComponentConstants._ORIENTATION
        };
    }

    private static Insets asInsets(java.awt.print.PageFormat format) {
        int left = (int) format.getImageableX();
        int top = (int) format.getImageableY();
        int bottom = (int) (format.getHeight() - top - format.getImageableHeight());
        int right = (int) (format.getWidth() - left - format.getImageableWidth());

        return new Insets(top, left, bottom, right);
    }

    /**
     * DOCUMENT ME!
     *
     * @param forPrint DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public java.awt.print.PageFormat toAwtFormat(boolean forPrint) {
        java.awt.print.PageFormat format = new java.awt.print.PageFormat();
        int ori = orientation;
        int x;
        int y;
        int iw;
        int ih;
        int w;
        int h;

        if (ori == java.awt.print.PageFormat.PORTRAIT) {
            x = left;
            y = top;
            iw = width - right - left;
            ih = height - top - bottom;
            w = width;
            h = height;
        } else {
            x = top;
            y = right;
            iw = height - top - bottom;
            ih = width - left - right;
            w = height;
            h = width;
        }

        format.setOrientation(ori);

        Paper p = new Paper();

        if (forPrint) {
            p.setImageableArea(x * DOTS_PER_PX, y * DOTS_PER_PX, iw * DOTS_PER_PX, ih * DOTS_PER_PX);
            p.setSize(w * DOTS_PER_PX, h * DOTS_PER_PX);
        } else {
            p.setImageableArea(x, y, iw, ih);
            p.setSize(w, h);
        }

        format.setPaper(p);

        return format;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getOrientation() {
        return orientation;
    }

    /**
     * DOCUMENT ME!
     *
     * @param orientation DOCUMENT ME!
     */
    public void setOrientation(int orientation) {
        this.orientation = orientation;
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
     * @return DOCUMENT ME!
     */
    public int getWidth() {
        return width;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean equals(Object obj) {
        boolean eq = super.equals(obj);

        if (eq) {
            PageFormat that = (PageFormat) obj;
            eq = (that.width == this.width) && (that.height == this.height) &&
                (that.orientation == this.orientation);
        }

        return eq;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getImageableWidth() {
        return getWidth() - left - right;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getImageableHeight() {
        return getHeight() - top - bottom;
    }

    /**
     * DOCUMENT ME!
     *
     * @param height DOCUMENT ME!
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * DOCUMENT ME!
     *
     * @param width DOCUMENT ME!
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getBottom() {
        return bottom;
    }

    /**
     * DOCUMENT ME!
     *
     * @param bottom DOCUMENT ME!
     */
    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getLeft() {
        return left;
    }

    /**
     * DOCUMENT ME!
     *
     * @param left DOCUMENT ME!
     */
    public void setLeft(int left) {
        this.left = left;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRight() {
        return right;
    }

    /**
     * DOCUMENT ME!
     *
     * @param right DOCUMENT ME!
     */
    public void setRight(int right) {
        this.right = right;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getTop() {
        return top;
    }

    /**
     * DOCUMENT ME!
     *
     * @param top DOCUMENT ME!
     */
    public void setTop(int top) {
        this.top = top;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPrintHeight() {
        if (printHeight == 0) {
            return HtmlExport.toMM(this.height);
        } else {
            return printHeight;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param printHeight DOCUMENT ME!
     */
    public void setPrintHeight(int printHeight) {
        this.printHeight = printHeight;
    }
}
