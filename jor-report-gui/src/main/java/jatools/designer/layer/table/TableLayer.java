package jatools.designer.layer.table;

import jatools.component.Component;

import jatools.component.table.TableBase;

import jatools.designer.ReportPanel;

import jatools.designer.layer.AbstractLayer;
import jatools.designer.layer.painter.Painter;

import jatools.designer.peer.ComponentPeer;
import jatools.designer.peer.TablePeer;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyEvent;

import javax.swing.Icon;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class TableLayer extends AbstractLayer implements Painter {
    static Icon ANCHOR;
    final static int BODY_MARK_SIZE = 20;
    final static int BODY_OFF = 90;
    final static int BODY_CX = -(BODY_MARK_SIZE - 1) / 2;
    final static int BODY_MARK_HEIGHT = 3;
    protected Point locationBaseChild = new Point();
    protected ReportPanel owner;
    ComponentPeer hitPeer;
    private TableEditKit editKit = new TableEditKit();
    private TableEditKit tableKit = new TableEditKit();
    private FocusRequest quitRequest;

    /**
     * Creates a new TableLayer object.
     *
     * @param owner
     *            DOCUMENT ME!
     */
    public TableLayer(ReportPanel owner) {
        this.owner = owner;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TableEditKit getEditKit() {
        return editKit;
    }

    /**
     * DOCUMENT ME!
     *
     * @param menuProvider DOCUMENT ME!
     */
    public void setMenuProvider(MenuProvider menuProvider) {
        this.editKit.setMenuProvider(menuProvider);
        this.tableKit.setMenuProvider(menuProvider);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean hasPopupMenu() {
        return true;
    }

    /**
     * DOCUMENT ME!
     */
    public void sleep() {
        super.sleep();

        editKit.hidePopup();
        editKit.setFocused(false);
        owner.repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int wakenBy() {
        return WAKEN_BY_MOUSE_PRESSED;
    }

    /**
     * DOCUMENT ME!
     *
     * @param waker
     *            DOCUMENT ME!
     * @param modifier
     *            DOCUMENT ME!
     * @param x
     *            DOCUMENT ME!
     * @param y
     *            DOCUMENT ME!
     * @param deltaX
     *            DOCUMENT ME!
     * @param deltaY
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean tryWake(int waker, int modifier, int x, int y, int deltaX, int deltaY) {
        if (owner.getBaby() != null) {
            return false;
        }

        if (this.isHitHot(x, y)) {
            return false;
        }

        hitPeer = owner.findComponentPeerAt(x, y);

        if (((hitPeer != null) && (hitPeer.getComponent().getCell() != null)) ||
                hitPeer instanceof TablePeer) {
            hitPeer = getRootTablePeer(hitPeer);

            Point off = new Point();

            off.setLocation(0, 0);

            owner.childPointAsScreenPoint(hitPeer, off);

            editKit = tableKit;

            editKit.setTablePeer((TablePeer) hitPeer, off.x, off.y);
            mousePressed(modifier, x, y);

            return true;
        } else {
            return false;
        }
    }

    boolean isHitHot(int x, int y) {
        ComponentPeer hitPeer = owner.getSelection(0);

        if (hitPeer != null) {
            Point loc = new Point(x, y);
            owner.screenPointAsChildPoint(hitPeer, loc);

            int hit = hitPeer.hitHot(loc);

            return hit != ComponentPeer.NOT_HIT;
        }

        return false;
    }

    private TablePeer getRootTablePeer(ComponentPeer hit) {
        Component c = hit.getComponent();

        while (c != null) {
            if (c instanceof TableBase && (c.getCell() == null)) {
                TablePeer peer = (TablePeer) hit.getOwner().getComponentPeer(c);

                return peer;
            }

            c = c.getParent();
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier
     *            DOCUMENT ME!
     * @param x
     *            DOCUMENT ME!
     * @param y
     *            DOCUMENT ME!
     */
    public void mouseReleased(int modifier, int x, int y) {
        Point off = editKit.getOff();
        editKit.mouseReleased(modifier, x - off.x, y - off.y);
        setCursor(editKit.getCursor());
        owner.repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier
     *            DOCUMENT ME!
     * @param x
     *            DOCUMENT ME!
     * @param y
     *            DOCUMENT ME!
     */
    public void mouseDoublePressed(int modifier, int x, int y) {
        Point off = editKit.getOff();
        editKit.mouseDoublePressed(modifier, x - off.x, y - off.y);
        setCursor(editKit.getCursor());
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier
     *            DOCUMENT ME!
     * @param x
     *            DOCUMENT ME!
     * @param y
     *            DOCUMENT ME!
     * @param deltaX
     *            DOCUMENT ME!
     * @param deltaY
     *            DOCUMENT ME!
     */
    public void mouseDragged(int modifier, int x, int y, int deltaX, int deltaY) {
        Point off = editKit.getOff();
        editKit.mouseDragged(modifier, x - off.x, y - off.y, deltaX, deltaY);
        setCursor(editKit.getCursor());
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier
     *            DOCUMENT ME!
     * @param x
     *            DOCUMENT ME!
     * @param y
     *            DOCUMENT ME!
     * @param deltaX
     *            DOCUMENT ME!
     * @param deltaY
     *            DOCUMENT ME!
     */
    public void mouseMoved(int modifier, int x, int y, int deltaX, int deltaY) {
        Point off = editKit.getOff();
        editKit.mouseMoved(modifier, x - off.x, y - off.y, deltaX, deltaY);
        setCursor(editKit.getCursor());
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier
     *            DOCUMENT ME!
     * @param x
     *            DOCUMENT ME!
     * @param y
     *            DOCUMENT ME!
     */
    public void mousePressed(int modifier, int x, int y) {
        Point off = editKit.getOff();
        x -= off.x;
        y -= off.y;

        boolean done = false;

        if (isWaken()) {
            if ((owner.getBaby() != null) || this.quitRequest.request(x, y)) {
                done = true;
            } else if (editKit.info.subState != TableEditKit.SUBSTATE_IDLE) {
                done = false;
            } else {
                int area = editKit.hitArea(x, y);

                if (area == TableEditKit.OUTSIDE) {
                    done = true;
                } else {
                    ComponentPeer peer = owner.findComponentPeerAt(x + off.x, y + off.y);

                    if ((peer.getComponent().getCell() == null) ||
                            (getRootTable(peer.getComponent()) != this.editKit.getTable())) {
                        done = true;
                    }
                }
            }
        }

        if (done) {
            this.fireActionPerformed(ACTION_DONE);
            owner.getGroupPanel().mousePressed(modifier, x + off.x, y + off.y);
        } else {
            ((TablePeer) hitPeer).setFocused(true);
            editKit.mousePressed(modifier, x, y);
            setCursor(editKit.getCursor());
        }

        owner.repaint();
    }

    TableBase getRootTable(Component c) {
        while (c.getCell() != null) {
            c = c.getParent();
        }

        if (c instanceof TableBase) {
            return (TableBase) c;
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param cursor
     *            DOCUMENT ME!
     */
    public void setCursor(Cursor cursor) {
        if (getCursor() != cursor) {
            super.setCursor(cursor);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g
     *            DOCUMENT ME!
     */
    public void paint(Graphics2D g) {
        Point off = editKit.getOff();
        g.translate(off.x, off.y);

        editKit.paint(g);
        g.translate(-off.x, -off.y);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean keyTyped(KeyEvent e) {
        return editKit.keyTyped(e);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean keyPressed(KeyEvent e) {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean keyReleased(KeyEvent e) {
        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param focused
     *            DOCUMENT ME!
     */
    public void setFocused(boolean focused) {
        if (this.editKit.tablePeer != null) {
            this.editKit.tablePeer.setFocused(focused);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TablePeer getActiveTable() {
        return editKit.tablePeer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param quitRequest DOCUMENT ME!
     */
    public void setQuitRequest(FocusRequest quitRequest) {
        this.quitRequest = quitRequest;
    }
}
