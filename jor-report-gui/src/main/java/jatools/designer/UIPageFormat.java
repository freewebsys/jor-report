package jatools.designer;

import java.awt.Toolkit;
import java.awt.print.PageFormat;
import java.awt.print.Paper;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class UIPageFormat {
    public static final int INCH = Toolkit.getDefaultToolkit().getScreenResolution();
    public static final double DOTS_PER_MM = (double) INCH / (double) 25.4;
    public boolean portrait;
    public double width;
    public double height;
    public double top;
    public double left;
    public double bottom;
    public double right;

    /**
     * Creates a new UIPageFormat object.
     *
     * @param f DOCUMENT ME!
     */
    public UIPageFormat(java.awt.print.PageFormat f) {
        Paper p = f.getPaper();

        this.left = toCms(p.getImageableX());
        this.top = toCms(p.getImageableY());
        this.right = toCms(p.getWidth() - p.getImageableWidth() - p.getImageableX());
        this.bottom = toCms(p.getHeight() - p.getImageableHeight() - p.getImageableY());

        this.width = toCms(f.getWidth());
        this.height = toCms(f.getHeight());
        this.portrait = f.getOrientation() == PageFormat.PORTRAIT;
    }

    /**
     * Creates a new UIPageFormat object.
     */
    public UIPageFormat() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public java.awt.print.PageFormat toAwtFormat() {
        PageFormat format = new PageFormat();
        int ori = portrait ? PageFormat.PORTRAIT : PageFormat.LANDSCAPE;

        double pw = toDots(this.width);
        double ph = toDots(this.height);
        double left = toDots(this.left);
        double top = toDots(this.top);
        double right = toDots(this.right);
        double bottom = toDots(this.bottom);
        double x;
        double y;
        double iw;
        double ih;

        if (ori == PageFormat.PORTRAIT) {
            x = left;
            y = top;
            iw = pw - right - left;
            ih = ph - top - bottom;
        } else {
            x = top;
            y = right;
            iw = pw - top - bottom;
            ih = ph - left - right;
        }

        format.setOrientation(ori);

        Paper p = new Paper();
        p.setImageableArea(x, y, iw, ih);
        p.setSize(pw, ph);
        format.setPaper(p);

        return format;
    }

    static int toDots(double mm) {
        try {
            return (int) ((DOTS_PER_MM * mm));
        } catch (NumberFormatException e) {
        }

        return 0;
    }

    static double toDots2(double mm) {
        try {
            return ((DOTS_PER_MM * mm));
        } catch (NumberFormatException e) {
        }

        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param dots DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int toCms(double dots) {
        return (int) Math.round(dots / DOTS_PER_MM);
    }
}
