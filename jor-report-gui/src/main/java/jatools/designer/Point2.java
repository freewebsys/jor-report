/*
 *   Author: John.
 *
 *   杭州杰创软件   All Copyrights Reserved.
 */


package jatools.designer;

import java.awt.Point;
import java.util.Observable;
import java.util.Observer;


/**
 * @author   java9
 */
public final class Point2 extends Observable {
    private int x;
    private int y;

    public String toString()
    {
      return "("+x+","+y+")"; // //$NON-NLS-2$ //$NON-NLS-3$
    }

    private Point2(Observer observer,
                  int x,
                  int y) {
        this.x = x;
        this.y = y;
        this.addObserver(observer);

    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Point asAwtPoint() {
        return new Point(x, y);
    }

    /**
     * DOCUMENT ME!
     *
     * @param deltaX DOCUMENT ME!
     * @param deltaY DOCUMENT ME!
     */
    public void move(int deltaX,
                     int deltaY) {
        x += deltaX;
        y += deltaY;
        notifyObservers(new Point(deltaX, deltaY));
    }

    /**
     * DOCUMENT ME!
     */
    public void notifyObservers(Point delta) {
        setChanged();

        super.notifyObservers(delta);
    }

    /**
	 * DOCUMENT ME!
	 * @return   DOCUMENT ME!
	 * @uml.property   name="x"
	 */
    public int getX() {
        return x;
    }

    /**
	 * DOCUMENT ME!
	 * @return   DOCUMENT ME!
	 * @uml.property   name="y"
	 */
    public int getY() {
        return y;
    }

    /**
	 * DOCUMENT ME!
	 * @param y   DOCUMENT ME!
	 * @uml.property   name="y"
	 */
    public void setY(int y) {
        if (this.y == y) {
            return;
        }

        int dy = y - this.y;
        this.y = y;
        notifyObservers(new Point(0, dy));
    }

    /**
	 * DOCUMENT ME!
	 * @param x   DOCUMENT ME!
	 * @uml.property   name="x"
	 */
    public void setX(int x) {
        if (this.x == x) {
            return;
        }

        int dx = x - this.x;

        this.x = x;
        notifyObservers(new Point(dx, 0));
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void set(int x,
                    int y) {
        int dx = x - this.x;
        int dy = y - this.y;
        this.x = x;
        this.y = y;
        notifyObservers(new Point(dx, dy));
    }
}
