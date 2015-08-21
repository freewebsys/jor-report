package jatools.core.view;

import jatools.PageFormat;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.print.Printable;
import java.util.Iterator;


/**
 * @author   java9
 */
public class PageView extends CompoundView implements Printable {
    static final long serialVersionUID = 20030716008L;
    PageFormat pageFormat;

    /**
     * DOCUMENT ME!
     */
    public int status;

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void remove(View e) {
        elementCache.remove(e);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */

    //    public void paint(Graphics2D g) {
    //        for (int i = 0; i < elementCache.size(); i++) {
    //            View e = (View) elementCache.get(i);
    //            e.paint(g);
    //        }
    //    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param pf DOCUMENT ME!
     * @param pageIndex DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int print(Graphics g, java.awt.print.PageFormat pf, int pageIndex) {
        paint((Graphics2D) g);

        return Printable.PAGE_EXISTS;
    }

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
     *
     * @param bounds DOCUMENT ME!
     */
    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    /**
     * @return   Returns the pageFormat.
     * @uml.property   name="pageFormat"
     */
    public PageFormat getPageFormat() {
        return pageFormat;
    }

    /**
     * @param pageFormat   The pageFormat to set.
     * @uml.property   name="pageFormat"
     */
    public void setPageFormat(PageFormat pageFormat) {
        this.pageFormat = pageFormat;
    }

    /**
     * DOCUMENT ME!
     */
    public void autoAdjustHeight() {
        int maxy = 0;

        
        //            it.next();
        Insets is = this.getPadding();
        Iterator it = this.elementCache.iterator();

        while (it.hasNext()) {
            AbstractView av = (AbstractView) it.next();
            int lasty = av.bounds.y + av.bounds.height;

            if (lasty > maxy) {
                maxy = lasty;
            }
        }

        this.bounds.height = maxy + is.top + is.bottom;
    }
}
