package jatools.designer.layer;

import jatools.component.Component;
import jatools.component.Text;
import jatools.component.Var;

import jatools.designer.ReportPanel;

import jatools.designer.data.Variable;

import jatools.designer.layer.click.BabyLooker;
import jatools.designer.layer.drag.MoveLayer;
import jatools.designer.layer.drop.DropComponentFactory;
import jatools.designer.layer.drop.ReportPanelDropHandler;

import jatools.designer.peer.ComponentPeer;

import jatools.util.CursorUtil;

import java.awt.AWTEvent;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPopupMenu;
import javax.swing.JViewport;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class LayerContainer extends LayerPanel implements ActionListener /*, DropTargetListener*/ {
    private static final int DOUBLE_PRESS = 1000;
    private Layer activeLayer;
    private ArrayList mouseDraggedLayers = new ArrayList();
    private ArrayList mouseMoveLayers = new ArrayList();
    private ArrayList mousePressedLayers = new ArrayList();
    private ArrayList mouseDoublePressedLayers = new ArrayList();
    private ArrayList mouseReleasedLayers = new ArrayList();
    private ArrayList mouseReleasedListeners;
    private ArrayList keyTypedListeners;
    private ArrayList keyPressedLayers = new ArrayList();
    private ArrayList keyReleasedLayers = new ArrayList();
    private ArrayList all = new ArrayList();
    int lastX;
    int lastY;
    int rawX;
    int rawY;
    ReportPanel owner;
    boolean cursorLocked = false;
    BabyLooker looker;
    private Cursor lockedCursor;
    Object lastLayer;
    private final javax.swing.Timer scroller;
    private Point last = new Point();

    /**
     * Creates a new LayerContainer object.
     *
     * @param owner DOCUMENT ME!
     */
    public LayerContainer(ReportPanel owner) {
        this.owner = owner;
        this.setLayout(null);

        enableEvents(AWTEvent.KEY_EVENT_MASK);
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);

        this.setToolTipText("tip");
        this.scroller = new javax.swing.Timer(5,
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        // TODO Auto-generated method stub
                        autoScroll();
                    }
                });

        new ReportPanelDropHandler(owner, this);
    }

    protected void autoScroll() {
        JViewport vport = this.owner.getScrollPanel().getViewport();

        int w = vport.getWidth();
        int h = vport.getHeight();

        // System.out.println(last);
        if (last.x < 0) {
            last.x -= 3;
        } else if (last.x > w) {
            last.x += 3;
        }

        if (last.y < 0) {
            last.y -= 3;
        } else if (last.y > h) {
            last.y += 3;
        }

        this.scrollRectToVisible(new Rectangle(last.x, last.y, 10, 10));
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     */
    public void setCursor(Cursor c) {
        if (!cursorLocked) {
            super.setCursor(c);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        switch (e.getID()) {
        case Layer.ACTION_UPDATE_CURSOR:

            Layer layerable = (Layer) e.getSource();
            setCursor(layerable.getCursor());

            break;

        case MoveLayer.ACTION_DONE:

            if (activeLayer != null) {
                activeLayer.sleep();
                activeLayer = null;
            }

            setCursor(CursorUtil.DEFAULT_CURSOR);

            break;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param layer DOCUMENT ME!
     */
    public void addMouseReleaseListener(Layer layer) {
        if (layer != null) {
            if (this.mouseReleasedListeners == null) {
                this.mouseReleasedListeners = new ArrayList();
            }

            this.mouseReleasedListeners.add(layer);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param layer DOCUMENT ME!
     */
    public void removeMouseReleaseListener(Layer layer) {
        if (this.mouseReleasedListeners != null) {
            this.mouseReleasedListeners.remove(layer);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param layerable DOCUMENT ME!
     */
    public void addLayer(Layer layerable) {
        int wakenby = layerable.wakenBy();

        switch (wakenby) {
        case Layer.WAKEN_BY_MOUSE_DRAGGED:
            mouseDraggedLayers.add(layerable);

            break;

        case Layer.WAKEN_BY_MOUSE_MOVE:
            mouseMoveLayers.add(layerable);

            break;

        case Layer.WAKEN_BY_MOUSE_PRESSED:
            mousePressedLayers.add(layerable);

            break;

        case Layer.WAKEN_BY_MOUSE_DOUBLE_PRESSED:
            mouseDoublePressedLayers.add(layerable);

            break;

        case Layer.WAKEN_BY_MOUSE_RELEASED:
            mouseReleasedLayers.add(layerable);

            break;

        case Layer.WAKEN_BY_KEY_PRESSED:
            keyPressedLayers.add(layerable);

            break;

        case Layer.WAKEN_BY_MOUSE_PRESSED | Layer.WAKEN_BY_MOUSE_DRAGGED:
            mouseDraggedLayers.add(layerable);
            mousePressedLayers.add(0, layerable);

            break;
        }

        all.add(layerable);

        layerable.addActionListener(this);
    }

    private void _processMouseEvent(MouseEvent evt) {
        lastLayer = this.activeLayer;

        int id = (evt.getClickCount() > 1) ? DOUBLE_PRESS : evt.getID();
        int modifier = evt.getModifiers();

        if (owner.getScale() != 1.0) {
            rawX = (int) (evt.getX() / owner.getScale());
            rawY = (int) (evt.getY() / owner.getScale());
        } else {
            rawX = evt.getX();
            rawY = evt.getY();
        }

        int x;
        int y;

        if (true) {
            int gridSize = 3;
            int top = 0;
            int left = 0;

            x = snap(rawX, left, gridSize);
            y = snap(rawY, top, gridSize);
        } else {
            x = rawX;
            y = rawY;
        }

        int deltaX = x - lastX;
        int deltaY = y - lastY;

        switch (id) {
        case MouseEvent.MOUSE_PRESSED:
            mousePressed(modifier, x, y);

            break;

        case MouseEvent.MOUSE_RELEASED:
            this.scroller.stop();
            mouseReleased(modifier, x, y);

            break;

        case MouseEvent.MOUSE_DRAGGED:
            scroller.stop();
            last.setLocation(evt.getPoint());
            scroller.start();
            mouseDragged(modifier, x, y, deltaX, deltaY);

            break;

        case MouseEvent.MOUSE_MOVED:
            mouseMoved(modifier, x, y, deltaX, deltaY);

            break;

        case DOUBLE_PRESS:
            mouseDoublePressed(modifier, x, y);

        case MouseEvent.MOUSE_CLICKED:
            mouseClicked(modifier, x, y, deltaX, deltaY);

            break;
        }

        lastX = x;
        lastY = y;
    }

    private void mouseClicked(int modifier, int x, int y, int deltaX, int deltaY) {
        if (((activeLayer == null) || (!(activeLayer.hasPopupMenu()))) &&
                ((MouseEvent.META_MASK & modifier) != 0)) {
            jatools.designer.Main.getInstance().getPopupMenu().show(this, x, y);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param p DOCUMENT ME!
     * @param offset DOCUMENT ME!
     * @param gridSize DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int snap(int p, int offset, int gridSize) {
        p -= offset;

        int half = gridSize / 2;
        int nums = p / gridSize;

        return offset + (((p % gridSize) >= half) ? ((nums + 1) * gridSize) : (nums * gridSize));
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void processKeyEvent(KeyEvent e) {
        boolean processed = false;

        switch (e.getID()) {
        case KeyEvent.KEY_PRESSED:
            processed = keyPressed(e);

            break;

        case KeyEvent.KEY_RELEASED:
            processed = keyReleased(e);

            break;

        case KeyEvent.KEY_TYPED:
            processed = keyTyped(e);

            break;
        }

        if (!processed) {
            super.processKeyEvent(e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    public void processMouseMotionEvent(MouseEvent evt) {
        super.processMouseMotionEvent(evt);
        _processMouseEvent(evt);
    }

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    public void processMouseEvent(MouseEvent evt) {
        super.processMouseEvent(evt);
        _processMouseEvent(evt);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getToolTipText(MouseEvent e) {
        String tooltip = NO_TOOLTIP;

        float scale = owner.getScale();
        int x = (int) (e.getX() / scale);
        int y = (int) (e.getY() / scale);

        for (Iterator i = all.iterator(); i.hasNext() && (tooltip == NO_TOOLTIP);) {
            Layer layerable = (Layer) i.next();

            if (layerable.hasTooltip()) {
                tooltip = layerable.getTooltip(x, y);
            }
        }

        if (tooltip == NO_TOOLTIP) {
            ComponentPeer peer = owner.findComponentPeerAt(x, y);

            if (peer.getComponent() instanceof Var) {
                Var var = (Var) peer.getComponent();

                return var.getVariable();
            }

            return null;
        } else {
            return tooltip;
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void fireUpdateCursor() {
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
        if (activeLayer != null) {
            activeLayer.mouseDragged(modifier, x, y, deltaX, deltaY);
        } else {
            doWake(WAKEN_BY_MOUSE_DRAGGED, mouseDraggedLayers, modifier, x, y, deltaX, deltaY);
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
    public void mouseMoved(int modifier, int x, int y, int deltaX, int deltaY) {
        if (activeLayer != null) {
            activeLayer.mouseMoved(modifier, x, y, deltaX, deltaY);
        } else {
            doWake(WAKEN_BY_MOUSE_MOVE, mouseMoveLayers, modifier, x, y, deltaX, deltaY);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void mouseDoublePressed(int modifier, int x, int y) {
        if (activeLayer != null) {
            activeLayer.mouseDoublePressed(modifier, x, y);
        } else {
            doWake(WAKEN_BY_MOUSE_DOUBLE_PRESSED, mouseDoublePressedLayers, modifier, x, y, 0, 0);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param waker DOCUMENT ME!
     * @param layers DOCUMENT ME!
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param deltaX DOCUMENT ME!
     * @param deltaY DOCUMENT ME!
     */
    public void doWake(int waker, ArrayList layers, int modifier, int x, int y, int deltaX,
        int deltaY) {
        if (layers.isEmpty()) {
            return;
        }

        for (Iterator i = layers.iterator(); i.hasNext();) {
            Layer layerable = (Layer) i.next();

            if (layerable.tryWake(waker, modifier, x, y, deltaX, deltaY)) {
                if (activeLayer != null) {
                    activeLayer.sleep();
                }

                activeLayer = layerable;

                activeLayer.wake();

                setCursor(layerable.getCursor());

                break;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param cursor DOCUMENT ME!
     */
    public void lockCursor(Cursor cursor) {
        this.lockedCursor = cursor;

        super.setCursor((lockedCursor == null) ? CursorUtil.DEFAULT_CURSOR : lockedCursor);
    }

    /**
     * DOCUMENT ME!
     *
     * @param waker DOCUMENT ME!
     * @param layers DOCUMENT ME!
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean loop(int waker, ArrayList layers, KeyEvent e) {
        if (layers.isEmpty()) {
            return false;
        }

        for (Iterator i = layers.iterator(); i.hasNext();) {
            Layer layerable = (Layer) i.next();

            if (layerable.tryWake(e)) {
                if (activeLayer != null) {
                    activeLayer.sleep();
                }

                activeLayer = layerable;
                layerable.wake();

                return true;
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void mousePressed(int modifier, int x, int y) {
        JPopupMenu ppm = jatools.designer.Main.getInstance().getPopupMenu();

        if ((ppm != null) && ppm.isVisible()) {
            ppm.setVisible(false);
        }

        requestFocus();

        if (activeLayer != null) {
            activeLayer.mousePressed(modifier, x, y);
        } else {
            doWake(WAKEN_BY_MOUSE_PRESSED, mousePressedLayers, modifier, x, y, 0, 0);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param modifier DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public void mouseReleased(int modifier, int x, int y) {
        if (activeLayer != null) {
            activeLayer.mouseReleased(modifier, x, y);
        } else {
            doWake(WAKEN_BY_MOUSE_RELEASED, mouseReleasedLayers, modifier, x, y, 0, 0);
        }

        if ((this.mouseReleasedListeners != null) && !this.mouseReleasedListeners.isEmpty()) {
            fireMouseReleaseListeners(modifier, x, y);
        }
    }

    private void fireMouseReleaseListeners(int modifier, int x, int y) {
        for (Iterator i = this.mouseReleasedListeners.iterator(); i.hasNext();) {
            Layer layerable = (Layer) i.next();
            layerable.mouseReleased(modifier, x, y);
        }
    }

    private void fireKeyTypedListeners(KeyEvent e) {
        for (int i = this.keyTypedListeners.size() - 1; i >= 0; i--) {
            Layer layerable = (Layer) keyTypedListeners.get(i);
            layerable.keyTyped(e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean keyPressed(KeyEvent e) {
        if (activeLayer != null) {
            return activeLayer.keyPressed(e);
        } else {
            return loop(WAKEN_BY_KEY_PRESSED, keyPressedLayers, e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean keyReleased(KeyEvent e) {
        if (activeLayer != null) {
            return activeLayer.keyReleased(e);
        } else {
            return loop(0, keyReleasedLayers, e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean keyTyped(KeyEvent e) {
        if ((this.keyTypedListeners != null) && !this.keyTypedListeners.isEmpty()) {
            fireKeyTypedListeners(e);
        }

        if (activeLayer != null) {
            return activeLayer.keyTyped(e);
        } else {
            return loop(0, keyReleasedLayers, e);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param cursorLocked DOCUMENT ME!
     */
    public void setCursorLocked(boolean cursorLocked) {
        this.cursorLocked = cursorLocked;

        if (!this.cursorLocked && (this.getActiveLayer() != null)) {
            setCursor(this.getActiveLayer().getCursor());
        } else {
            setCursor(CursorUtil.DEFAULT_CURSOR);
        }
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public Layer getActiveLayer() {
        return this.activeLayer;
    }

    /**
     * DOCUMENT ME!
     *
     * @param layer DOCUMENT ME!
     */
    public void addKeyTypedListener(Layer layer) {
        if (layer != null) {
            if (this.keyTypedListeners == null) {
                this.keyTypedListeners = new ArrayList();
            }

            this.keyTypedListeners.add(layer);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param layer DOCUMENT ME!
     */
    public void removeKeyTypedListener(Layer layer) {
        if (this.keyTypedListeners != null) {
            this.keyTypedListeners.remove(layer);
        }
    }
}
