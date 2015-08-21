package jatools.designer.layer;

import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public interface Layer {
    final static String CANCEL_TOOLTIP = "cancel.tooltip";
    final static String NO_TOOLTIP = "no.tooltip";
    final static int ACTION_DONE = 100;
    final static int ACTION_UPDATE_CURSOR = -1;
    final static int WAKEN_BY_NOTHING = 0;
    final static int WAKEN_BY_MOUSE_DRAGGED = 1;
    final static int WAKEN_BY_MOUSE_MOVE = 2;
    final static int WAKEN_BY_MOUSE_PRESSED = 4;
    final static int WAKEN_BY_MOUSE_DOUBLE_PRESSED = 8;
    final static int WAKEN_BY_MOUSE_RELEASED = 16;
    final static int WAKEN_BY_KEY_PRESSED = 32;

    /**
     * DOCUMENT ME!
     */
    public void sleep();

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void addActionListener(ActionListener lst);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasTooltip();

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTooltip(int x, int y);

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void removeActionListener(ActionListener lst);

    /**
     * DOCUMENT ME!
     */
    public void wake();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isWaken();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Cursor getCursor();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int wakenBy();

    /**
     * DOCUMENT ME!
     *
     * @param waker DOCUMENT ME!
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param deltaX DOCUMENT ME!
     * @param deltaY DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean tryWake(int waker, int modifier, int x, int y, int deltaX, int deltaY);

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean tryWake(KeyEvent e);

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param deltaX DOCUMENT ME!
     * @param deltaY DOCUMENT ME!
     */
    public void mouseDragged(int modifier, int x, int y, int deltaX, int deltaY);

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param deltaX DOCUMENT ME!
     * @param deltaY DOCUMENT ME!
     */
    public void mouseMoved(int modifier, int x, int y, int deltaX, int deltaY);

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void mouseDoublePressed(int modifier, int x, int y);

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void mousePressed(int modifier, int x, int y);

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void mouseReleased(int modifier, int x, int y);

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean keyTyped(KeyEvent e);

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean keyPressed(KeyEvent e);

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean keyReleased(KeyEvent e);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasPopupMenu();

    /**
     * DOCUMENT ME!
     *
     * @param focused DOCUMENT ME!
     */
    public void setFocused(boolean focused);
}
