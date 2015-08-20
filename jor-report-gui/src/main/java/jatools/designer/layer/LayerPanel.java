/*
 *   Author: John.
 *
 *   杭州杰创软件   All Copyrights Reserved.
 */
package jatools.designer.layer;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;


/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.4 $
 * @author $author$
 */
public class LayerPanel extends JPanel implements Layer {
    private ArrayList actionListeners = new ArrayList();
    private boolean waken = false;

    LayerPanel() {
        setOpaque(false);
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
     * @return DOCUMENT ME!
     */
    public boolean isWaken() {
        return waken;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public boolean keyTyped(KeyEvent e) {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public boolean keyPressed(KeyEvent e) {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public boolean keyReleased(KeyEvent e) {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int wakenBy() {
        return WAKEN_BY_NOTHING;
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param deltaX DOCUMENT ME!
     * @param deltaY DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean tryWake(int waker, int modifier, int x, int y, int deltaX, int deltaY) {
        return false;
    }

    /**
       * DOCUMENT ME!
       *
       * @param cursor DOCUMENT ME!
       */
    public void setCursor(Cursor cursor) {
        if (getCursor() != cursor) {
            super.setCursor(cursor);
        }

        this.fireActionPerformed(ACTION_UPDATE_CURSOR);
    }

    /**
     * DOCUMENT ME!
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
      * @param modifier DOCUMENT ME!
      * @param x DOCUMENT ME!
      * @param y DOCUMENT ME!
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

    /* (non-Javadoc)
     * @see com.jatools.designer.layer.ZLayerable#getWakenLayer()
     */
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Layer getWakenLayer() {
        return this;
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
    public boolean hasTooltip() {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTooltip(int x,int y) {
        return null;
    }

	public void setFocused(boolean focused) {
		// TODO Auto-generated method stub
		
	}
}
