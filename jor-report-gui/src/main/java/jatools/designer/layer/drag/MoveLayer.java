package jatools.designer.layer.drag;

import jatools.component.Component;

import jatools.designer.ReportPanel;

import jatools.designer.layer.AbstractLayer;
import jatools.designer.layer.painter.SelectionFramePainter;
import jatools.designer.layer.utils.MoveWorker;

import jatools.designer.peer.ComponentPeer;

import jatools.designer.undo.GroupEdit;

import jatools.swingx.MessageBox;

import jatools.util.CursorUtil;

import java.awt.event.MouseEvent;

import javax.swing.undo.CompoundEdit;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class MoveLayer extends AbstractLayer {
    ReportPanel owner;
    SelectionFramePainter paintLayer;
    MoveWorker worker;
    boolean copy;

    /**
     * Creates a new MoveLayer object.
     *
     * @param owner
     *            DOCUMENT ME!
     * @param paintLayer
     *            DOCUMENT ME!
     */
    public MoveLayer(ReportPanel owner, SelectionFramePainter paintLayer) {
        this.owner = owner;
        this.paintLayer = paintLayer;
        setCursor(CursorUtil.MOVE_CURSOR);
    }

    /**
     * DOCUMENT ME!
     */
    public void sleep() {
        super.sleep();

        CompoundEdit edit = new GroupEdit();

        if (worker != null) {
            worker.close(edit, copy);
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
    public int wakenBy() {
        return WAKEN_BY_MOUSE_DRAGGED;
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
        copy = this.ctrlDown(modifier);

        for (int i = 0; i < owner.getSelectionCount(); i++) {
            if (!owner.getSelection(i).isMoveable()) {
                return false;
            }
        }

        prepareMove((ComponentPeer[]) owner.getSelection(), modifier, x, y);

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param peers
     *            DOCUMENT ME!
     * @param modifier
     *            DOCUMENT ME!
     * @param x
     *            DOCUMENT ME!
     * @param y
     *            DOCUMENT ME!
     */
    public void prepareMove(ComponentPeer[] peers, int modifier, int x, int y) {
        boolean shiftDown = (modifier & MouseEvent.SHIFT_MASK) != 0;
        worker = new MoveWorker(owner, !shiftDown, ComponentPeer.CENTER, paintLayer,
                (ComponentPeer[]) owner.getSelection());

        if ((paintLayer != null) && !shiftDown) {
            paintLayer.setSelectedFrames(worker.getFrames());
        }

        mouseDragged(modifier, x, y, 0, 0);
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
        if (isWaken()) {
            this.fireActionPerformed(ACTION_DONE);
        }
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
        worker.move(deltaX, deltaY);
    }
}
