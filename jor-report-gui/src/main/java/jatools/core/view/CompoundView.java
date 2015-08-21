package jatools.core.view;

import jatools.component.painter.BackgroudImagePainter;

import jatools.engine.css.rule.LayoutRule;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class CompoundView extends AbstractView {
    static public final Insets NULL_INSETS = new Insets(0, 0, 0, 0);
    static final int PADDING_FLAG_SIZE = 3;
    static final long serialVersionUID = 2003071601220L;
    Insets padding;
    protected boolean layoutdirty = true;
    public ArrayList elementCache = new ArrayList();
    transient TransformView lastTrans;
    private ImageView asImage;
    private boolean shapes;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isLazy() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param form DOCUMENT ME!
     */
    public void prepareTransform(TransformView form) {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ImageView asImageView() {
        if (this.asImage == null) {
            this.asImage = ImageViewConverter.getDefaults().toImageView(this);
        }

        return this.asImage;
    }

    /**
     * DOCUMENT ME!
     *
     * @param form DOCUMENT ME!
     */
    public void doTransform(TransformView form) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void translate(int x, int y) {
        if ((x != 0) || (y != 0)) {
            if (lastTrans != null) {
                lastTrans.x += x;
                lastTrans.y += y;

                if ((lastTrans.x == 0) && (lastTrans.y == 0)) {
                    elementCache.remove(lastTrans);
                }
            } else {
                elementCache.add(new TransformView(x, y));
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public java.util.List getElements() {
        return elementCache;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isLazyContainer() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics2D g) {
        Rectangle b = (Rectangle) getBounds().clone();
        Graphics2D gcopy = (Graphics2D) g.create();

        Color color = getBackColor();

        if (color != null) {
            Rectangle r = (getBorder() != null) ? deflateRect(b, getBorder().getInsets()) : b;
            gcopy.setColor(getBackColor());
            gcopy.fillRect(r.x, r.y, r.width, r.height);
        }

        if (getBackgroundImageStyle() != null) {
            BackgroudImagePainter.getDefaults().paint(g, getBackgroundImageStyle(), b);
        }

        Insets padding = this.getPadding();

        gcopy.translate(b.x + padding.left, b.y + padding.top);

        for (int i = 0; i < elementCache.size(); i++) {
            View e = (View) elementCache.get(i);
            e.paint(gcopy);
        }

        gcopy.dispose();

        if (getBorder() != null) {
            getBorder().paint(g, getBounds());
        }
    }

    String toString(Rectangle r) {
        return "[x=" + r.x + ",y=" + r.y + ",width=" + r.width + ",height=" + r.height;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Insets getPadding() {
        return (padding == null) ? NULL_INSETS : padding;
    }

    /**
     * DOCUMENT ME!
     *
     * @param thise DOCUMENT ME!
     */
    public void add(View thise) {
        this.lastTrans = null;
        elementCache.add(thise);

        if (thise instanceof LineView) {
            this.shapes = true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasShapes() {
        return shapes;
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param bottom DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int distanceBetweenBottoms(TextView view, int bottom) {
        int index = elementCache.indexOf(view);

        if (index == -1) {
            return 0;
        }

        int deltay = 0;

        for (int i = index + 1; i < elementCache.size(); i++) {
            Object o = elementCache.get(i);

            if (o instanceof TransformView) {
                deltay += ((TransformView) o).y;
            }
        }

        return (deltay + bottom) - view.getBounds().y - view.getBounds().height;
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param right DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int distanceBetweenRights(TextView view, int right) {
        int index = elementCache.indexOf(view);

        if (index == -1) {
            return 0;
        }

        int deltay = 0;

        for (int i = index + 1; i < elementCache.size(); i++) {
            Object o = elementCache.get(i);

            if (o instanceof TransformView) {
                deltay += ((TransformView) o).x;
            }
        }

        return (deltay + right) - view.getBounds().x - view.getBounds().width;
    }

    /**
     * DOCUMENT ME!
     */
    public void clear() {
        this.elementCache.clear();
    }

    /**
     * DOCUMENT ME!
     */
    public void pack() {
        this.elementCache = pack(new ArrayList());
    }

    protected ArrayList pack(ArrayList to) {
        Iterator it = this.elementCache.iterator();

        while (it.hasNext()) {
            Object o = it.next();

            if (o instanceof CompoundView) {
                ((CompoundView) o).pack(to);
            } else {
                to.add(o);
            }
        }

        return to;
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
     */
    public void doLayout() {
        if (layoutdirty) {
            Iterator it = this.elementCache.iterator();

            int offx = 0;
            int offy = 0;

            while (it.hasNext()) {
                View obj = (View) it.next();

                if (obj instanceof TransformView) {
                    TransformView trans = (TransformView) obj;

                    offx += trans.x;
                    offy += trans.y;

                    continue;
                }

                AbstractView view = (AbstractView) obj;
                LayoutRule layoutRule = view.getLayoutRule();

                if (layoutRule != null) {
                    Insets is = this.getPadding();
                    Rectangle parentRect = getBounds();
                    Rectangle childRect = view.getBounds();

                    if (layoutRule.width != null) {
                        int width = parentRect.width - is.left - is.right;

                        if (layoutRule.width.isPercent()) {
                            float percent = layoutRule.width.percentValue();
                            width = Math.round(width * percent);
                        } else {
                            width = (int) layoutRule.width.floatValue();
                        }

                        childRect.width = width;
                    }

                    if (layoutRule.height != null) {
                        int height = parentRect.height - is.top - is.bottom;

                        if (layoutRule.height.isPercent()) {
                            float percent = layoutRule.height.percentValue();
                            height = Math.round(height * percent);
                        } else {
                            height = (int) layoutRule.height.floatValue();
                        }

                        childRect.height = height;
                    }

                    if (layoutRule.left != null) {
                        childRect.x = layoutRule.left.intValue();
                    } else if (layoutRule.right != null) {
                        int xoff = is.left;
                        int width = parentRect.width - is.left - is.right;

                        childRect.x = (xoff + width) - layoutRule.right.intValue() -
                            childRect.width;
                    } else if (layoutRule.alignX != null) {
                        int xoff = is.left;
                        int width = parentRect.width - is.left - is.right;

                        if (layoutRule.alignX.isPercent()) {
                            float percent = layoutRule.alignX.percentValue();
                            xoff += Math.round((width * percent) - (childRect.width * percent));
                        } else {
                            xoff += (int) layoutRule.alignX.floatValue();
                        }

                        childRect.x = xoff;
                    }

                    if (layoutRule.top != null) {
                        childRect.y = layoutRule.top.intValue();
                    } else if (layoutRule.bottom != null) {
                        int yoff = is.top;
                        int height = parentRect.height - is.top - is.bottom;

                        childRect.y = (yoff + height) - layoutRule.bottom.intValue() -
                            childRect.height;
                    } else if (layoutRule.alignY != null) {
                        int yoff = is.top;
                        int height = parentRect.height - is.top - is.bottom;

                        if (layoutRule.alignY.isPercent()) {
                            float percent = layoutRule.alignY.percentValue();
                            yoff += Math.round((height * percent) - (childRect.height * percent));
                        } else {
                            yoff += (int) layoutRule.alignY.floatValue();
                        }

                        childRect.y = yoff;
                    }
                }

                if (view instanceof CompoundView) {
                    ((CompoundView) view).doLayout();
                }
            }

            this.layoutdirty = false;
        }
    }
}
