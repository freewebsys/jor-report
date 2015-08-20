package jatools.designer.layer.utils;

import jatools.component.Component;
import jatools.component.Page;
import jatools.designer.ClipBoard;
import jatools.designer.ReportPanel;
import jatools.designer.layer.painter.SelectionFramePainter;
import jatools.designer.peer.ComponentPeer;
import jatools.designer.peer.ComponentPeerFactory;
import jatools.designer.peer.TablePeer;
import jatools.designer.undo.ParentChangeEdit;
import jatools.designer.undo.PropertyEdit;

import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class MoveWorker {
    private PositionValidator positionValidator;
    private SelectionFrame[] frames;
    private SelectionFramePainter painter;
    boolean autoGrow;
    ReportPanel owner;
    boolean reparentAllowed = true;
    ComponentPeer[] peers;
    _PropertyListener propertyChangeListener;

    MoveWorker(ReportPanel owner, boolean autoGrow, int hit, SelectionFrame[] frames,
        SelectionFramePainter painter, ComponentPeer[] peers) {
        this.owner = owner;
        this.autoGrow = autoGrow;
        this.painter = painter;
        this.peers = peers;

        if (frames == null) {
            frames = createSelectedFrames(peers, hit);
        }

        this.frames = frames;

        Page report = (Page) owner.getReportPeer().getComponent();
        positionValidator = new PositionValidator(autoGrow, report.getPadding().left,
                report.getPadding().top);
    }

    /**
     * Creates a new MoveWorker object.
     *
     * @param owner
     *            DOCUMENT ME!
     * @param autoGrow
     *            DOCUMENT ME!
     * @param hit
     *            DOCUMENT ME!
     * @param painter
     *            DOCUMENT ME!
     * @param peers
     *            DOCUMENT ME!
     */
    public MoveWorker(ReportPanel owner, boolean autoGrow, int hit, SelectionFramePainter painter,
        ComponentPeer[] peers) {
        this(owner, autoGrow, hit, null, painter, peers);
    }

    /**
     * DOCUMENT ME!
     *
     * @param child
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int willApplyedWidth(ComponentPeer child) {
        for (int i = 0; i < frames.length; i++) {
            if (frames[i].peer == child) {
                return frames[i].getWidth();
            }
        }

        return -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param child
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int willApplyedHeight(ComponentPeer child) {
        for (int i = 0; i < frames.length; i++) {
            if (frames[i].peer == child) {
                return frames[i].getHeight();
            }
        }

        return -1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param peers
     *            DOCUMENT ME!
     * @param dir
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static SelectionFrame[] createSelectedFrames(ComponentPeer[] peers, int dir) {
        SelectionFrame[] frames = new SelectionFrame[peers.length];

        for (int i = 0; i < frames.length; i++) {
            ComponentPeer peer = peers[i];

            frames[i] = new SelectionFrame(peer, dir);
        }

        return frames;
    }

    /**
     * DOCUMENT ME!
     *
     * @param peer
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static SelectionFrame createSelectedFrame(ComponentPeer peer) {
        return new SelectionFrame(peer);
    }

    /**
     * DOCUMENT ME!
     *
     * @param dx
     *            DOCUMENT ME!
     * @param dy
     *            DOCUMENT ME!
     */
    public void move(int dx, int dy) {
        for (int i = 0; i < frames.length; i++) {
            frames[i].calculator.calculate(frames[i], dx, dy);
        }

        if (!positionValidator.validate(frames)) {
            for (int i = 0; i < frames.length; i++) {
                frames[i].calculator.calculate(frames[i], -dx, -dy);
            }
        } else if (!autoGrow) {
            apply();
        }

        if (autoGrow && (painter != null)) {
            painter.paintComponent();
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void apply() {
        for (int i = 0; i < frames.length; i++) {
            frames[i].apply();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param child
     *            DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static void validParent(Component child) throws Exception {
        Component parent = child.getParent();
    }

    /**
     * DOCUMENT ME!
     *
     * @param edit DOCUMENT ME!
     */
    public void close(CompoundEdit edit) {
        close(edit, false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param edit
     *            DOCUMENT ME!
     */
    public void close(CompoundEdit edit, boolean copy) {
        if (autoGrow) {
            Component[] newcomps = null;

            if (copy) {
                newcomps = copy(edit);
            }

            if (isReparentAllowed()) {
                reparent();
            }

            for (int i = 0; i < frames.length; i++) {
                SelectionFrame frame = frames[i];
                ComponentPeer peer = frame.peer;
                ComponentPeer newParent = frame.newParent;
                ComponentPeer oldParent = peer.getParent();

                if ((newParent != oldParent) && (newParent != null)) {
                    int oldIndex = peer.getParent().indexOf(peer);
                    peer.getParent().remove(peer);
                    newParent.add(peer);

                    if (edit != null) {
                        edit.addEdit(new ParentChangeEdit(peer, oldParent, oldIndex));
                    }

                    frame.apply();
                } else {
                    frame.apply();
                }
            }

            if (this.propertyChangeListener == null) {
                this.propertyChangeListener = new _PropertyListener();
            }

            this.propertyChangeListener.clear();

            Component.temppropertylistener.set(this.propertyChangeListener);

            owner.getPage().validate();

            if (edit != null) {
                this.propertyChangeListener.applyEdit(edit);
            }

            Component.temppropertylistener.set(null);

            if (newcomps != null) {
                owner.unselectAll();

                for (Component c : newcomps)
                    owner.select(c);
            }
        }
    }

    private Component[] copy(CompoundEdit edit) {
        Component[] comps = new Component[frames.length];

        for (int i = 0; i < frames.length; i++) {
            comps[i] = frames[i].peer.getComponent();
        }

        comps = ClipBoard.clone(comps);

        for (int i = 0; i < frames.length; i++) {
            Component c = comps[i];
            ComponentPeer peer = ComponentPeerFactory.createPeerDeeply(owner, comps[i]);
            frames[i].peer.getParent().add(peer);
            frames[i].peer = peer;
        }

        return comps;
    }

    private void reparent() {
        Point p = new Point();

        for (int i = 0; i < frames.length; i++) {
            SelectionFrame frame = frames[i];

            ComponentPeer oldParent = frame.peer.getParent();

            int x = Math.min(frame.startPoint.x, frame.endPoint.x);
            int y = Math.min(frame.startPoint.y, frame.endPoint.y);

            ComponentPeer newParent = findNearestContainter(owner, frame.peer.getComponent(), x, y);

            if (((newParent != null) && (newParent != oldParent)) &&
                    newParent.isAcceptableChild(frame.peer.getComponent()) &&
                    (!(newParent instanceof TablePeer))) {
                p.x = 0;
                p.y = 0;
                owner.childPointAsScreenPoint(newParent, p);
                frame.parentOffScreenX = p.x;
                frame.parentOffScreenY = p.y;
                frame.newParent = newParent;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param owner
     *            DOCUMENT ME!
     * @param comp
     *            DOCUMENT ME!
     * @param x
     *            DOCUMENT ME!
     * @param y
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ComponentPeer findNearestContainter(ReportPanel owner, Component comp, int x,
        int y) {
        ComponentPeer hitPeer = owner.findComponentPeerAt(x, y);

        while (hitPeer != null) {
            if ((hitPeer.getComponent() != comp) && (!isAncestor(comp, hitPeer.getComponent())) &&
                    (hitPeer.isAcceptableChild(comp))) {
                return hitPeer;
            }

            hitPeer = hitPeer.getParent();
        }

        return null;
    }

    static boolean isAncestor(Component ancestor, Component des) {
        while (des != null) {
            if (ancestor == des.getParent()) {
                return true;
            }

            des = des.getParent();
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SelectionFrame[] getFrames() {
        return frames;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isReparentAllowed() {
        return reparentAllowed;
    }

    /**
     * DOCUMENT ME!
     *
     * @param reparentAllowed
     *            DOCUMENT ME!
     */
    public void setReparentAllowed(boolean reparentAllowed) {
        this.reparentAllowed = reparentAllowed;
    }

    class _PropertyListener implements PropertyChangeListener {
        ArrayList changes = new ArrayList();

        public void propertyChange(PropertyChangeEvent e) {
            Component c = (Component) e.getSource();
            ComponentPeer peer = owner.getComponentPeer(c);

            if (peer != null) {
                PropertyEdit pe = new PropertyEdit(peer, e.getPropertyName(), e.getOldValue(),
                        e.getNewValue());

                changes.add(pe);
            }
        }

        public void applyEdit(CompoundEdit edit) {
            if (!changes.isEmpty()) {
                Iterator it = changes.iterator();

                while (it.hasNext()) {
                    UndoableEdit pe = (UndoableEdit) it.next();
                    edit.addEdit(pe);
                }
            }
        }

        public void clear() {
            this.changes.clear();
        }
    }
}
