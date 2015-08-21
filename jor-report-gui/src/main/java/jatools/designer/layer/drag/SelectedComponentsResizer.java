package jatools.designer.layer.drag;



import jatools.component.Component;
import jatools.designer.ReportPanel;
import jatools.designer.layer.painter.SelectionFramePainter;
import jatools.designer.layer.utils.MoveWorker;
import jatools.designer.peer.ComponentPeer;
import jatools.designer.undo.GroupEdit;
import jatools.swingx.MessageBox;
import jatools.util.CursorUtil;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.undo.CompoundEdit;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class SelectedComponentsResizer implements Resizer {
    ReportPanel owner;
    SelectionFramePainter paintLayer;
    MoveWorker worker;
    private Cursor cursor;

    /**
     * Creates a new SelectedComponentsResizer object.
     *
     * @param owner DOCUMENT ME!
     * @param paintLayer DOCUMENT ME!
     */
    public SelectedComponentsResizer(ReportPanel owner, SelectionFramePainter paintLayer) {
        this.owner = owner;
        this.paintLayer = paintLayer;
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
    public boolean start(int modifier, int x, int y, int deltaX, int deltaY) {
        ComponentPeer hitPeer = owner.getSelection(0);

        if ((hitPeer == null) || !hitPeer.isResizable()) {
            return false;
        }

        Point loc = new Point(x, y);
        owner.screenPointAsChildPoint(hitPeer, loc);

        int hit = hitPeer.hitHot(loc);
        boolean selected = (hit != ComponentPeer.NOT_HIT);

        if (selected) {
            boolean shiftDown = (modifier & MouseEvent.SHIFT_MASK) != 0;
            worker = new MoveWorker(owner, !shiftDown, hit, paintLayer,
                    (ComponentPeer[]) owner.getSelection());

            if ((paintLayer != null) && !shiftDown) {
                paintLayer.setSelectedFrames(worker.getFrames());
            }

            this.cursor = getCursor(hit);
            drag(x, y, deltaX, deltaY);

            return true;
        } else {
            return false;
        }
    }

    protected Cursor getCursor(int hitPosition) {
        switch (hitPosition) {
        case ComponentPeer.SOUTH_WEST:
        case ComponentPeer.NORTH_EAST:
            return CursorUtil.NE_RESIZE_CURSOR;

        case ComponentPeer.SOUTH_EAST:
        case ComponentPeer.NORTH_WEST:
            return CursorUtil.NW_RESIZE_CURSOR;

        case ComponentPeer.EAST:
        case ComponentPeer.WEST:
            return CursorUtil.E_RESIZE_CURSOR;

        case ComponentPeer.SOUTH:
        case ComponentPeer.NORTH:
            return CursorUtil.S_RESIZE_CURSOR;
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param deltaX DOCUMENT ME!
     * @param deltaY DOCUMENT ME!
     */
    public void drag(int x, int y, int deltaX, int deltaY) {
        worker.move(deltaX, deltaY);
    }

    /**
     * DOCUMENT ME!
     */
    public void end() {
        CompoundEdit edit = new GroupEdit();

        if (worker != null) {
            worker.close(edit);
            owner.addEdit(edit);
        }

        ComponentPeer[] peers = (ComponentPeer[]) owner.getSelection();

        for (int i = 0; i < peers.length; i++) {
            Component child = peers[i].getComponent();

            try {
                MoveWorker.validParent(child);
                owner.reselect();
            } catch (Exception e) {
                MessageBox.error(owner, e.getMessage());
                owner.getUndoManager().undo();
                owner.getUndoManager().discardEdit(edit);

                break;
            }
        }

        owner.repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Cursor getCursor() {
        return cursor;
    }
}
