package jatools.designer.layer;

import jatools.util.CursorUtil;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import java.util.ArrayList;
import java.util.Iterator;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public abstract class AbstractLayer implements Layer {
    Cursor cursor = CursorUtil.DEFAULT_CURSOR;
    private ArrayList actionListeners = new ArrayList();
    private boolean waken = false;

    /**
     * DOCUMENT ME!
     *
     * @param focused DOCUMENT ME!
     */
    public void setFocused(boolean focused) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTooltip(int x, int y) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasTooltip() {
        return false;
    }

    /**
     * DOCUMENT ME!
     */
    public void wake() {
        waken = true;
    }

    /**
     * DOCUMENT ME!
     */
    public void sleep() {
        waken = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean tryWake(KeyEvent e) {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasPopupMenu() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isWaken() {
        return waken;
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void addActionListener(ActionListener lst) {
        actionListeners.add(lst);
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void removeActionListener(ActionListener lst) {
        actionListeners.remove(lst);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Cursor getCursor() {
        return cursor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param cursor DOCUMENT ME!
     */
    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
        this.fireActionPerformed(ACTION_UPDATE_CURSOR);
    }

    /**
     * DOCUMENT ME!
     *
     * @param actionId DOCUMENT ME!
     */
    public void fireActionPerformed(int actionId) {
        ActionEvent e = new ActionEvent(this, actionId, null);

        for (Iterator i = actionListeners.iterator(); i.hasNext();) {
            ActionListener lst = (ActionListener) i.next();
            lst.actionPerformed(e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param deltaX DOCUMENT ME!
     * @param deltaY DOCUMENT ME!
     */
    public void mouseDragged(int modifier, int x, int y, int deltaX, int deltaY) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param deltaX DOCUMENT ME!
     * @param deltaY DOCUMENT ME!
     */
    public void mouseMoved(int modifier, int x, int y, int deltaX, int deltaY) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void mouseDoublePressed(int modifier, int x, int y) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void mousePressed(int modifier, int x, int y) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void mouseReleased(int modifier, int x, int y) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean keyTyped(KeyEvent e) {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean keyPressed(KeyEvent e) {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean keyReleased(KeyEvent e) {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean shiftDown(int modifier) {
        return (modifier & MouseEvent.SHIFT_MASK) != 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean ctrlDown(int modifier) {
        return (modifier & MouseEvent.CTRL_MASK) != 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean altDown(int modifier) {
        return (modifier & MouseEvent.ALT_MASK) != 0;
    }
}
