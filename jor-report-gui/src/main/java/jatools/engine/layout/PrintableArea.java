package jatools.engine.layout;

import java.awt.Dimension;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PrintableArea {
    private int x0;
    private int y0;
    private int width;
    private int height;

    /**
     * Creates a new PrintableArea object.
     *
     * @param width DOCUMENT ME!
     * @param height DOCUMENT ME!
     */
    public PrintableArea(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public Dimension getMaxSize() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getX() {
        return this.x0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getY() {
        return this.y0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void moveTo(int x, int y) {
        this.x0 = x;
        this.y0 = y;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean contains(int x, int y) {
        return (x < width) && (y < height) && (x >= 0) && (y >= 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param h DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canBottomAppend(int h) {
        return contains(x0, y0 + h);
    }
}
