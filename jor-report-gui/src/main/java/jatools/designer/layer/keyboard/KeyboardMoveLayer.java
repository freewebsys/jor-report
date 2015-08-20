package jatools.designer.layer.keyboard;


import jatools.designer.ReportPanel;
import jatools.designer.layer.AbstractLayer;
import jatools.designer.layer.utils.MoveWorker;
import jatools.designer.peer.ComponentPeer;
import jatools.designer.peer.PanelPeer;
import jatools.designer.undo.GroupEdit;

import java.awt.event.KeyEvent;

import javax.swing.undo.CompoundEdit;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class KeyboardMoveLayer extends AbstractLayer {
    ReportPanel owner;
    int stepX;
    int stepY;
    MoveWorker worker;

    /**
     * Creates a new KeyboardMoveLayer object.
     *
     * @param owner DOCUMENT ME!
     */
    public KeyboardMoveLayer(ReportPanel owner) {
        this.owner = owner;
    }

    /**
     * DOCUMENT ME!
     */
    public void sleep() {
        super.sleep();

        if (owner.isUndoable()) {
            CompoundEdit edit = new GroupEdit();
            worker.close(edit);
            owner.addEdit(edit);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int wakenBy() {
        return WAKEN_BY_KEY_PRESSED;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean tryWake(KeyEvent e) {
        stepX = 0;
        stepY = 0;

        ComponentPeer selectedPeer = owner.getSelection(0);

        if (selectedPeer instanceof PanelPeer) {
            return false;
        }

        switch (e.getKeyCode()) {
        case KeyEvent.VK_UP:
            stepY = -1;

            break;

        case KeyEvent.VK_DOWN:
            stepY = 1;

            break;

        case KeyEvent.VK_LEFT:
            stepX = -1;

            break;

        case KeyEvent.VK_RIGHT:
            stepX = 1;

            break;
        }

        if ((stepX + stepY) != 0) {
            worker = new MoveWorker(owner, false, ComponentPeer.CENTER, null,
                    (ComponentPeer[]) owner.getSelection());

            keyPressed(e);

            return true;
        } else {
            return false;
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
        if (isWaken()) {
            fireActionPerformed(ACTION_DONE);
        }

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
        worker.move(stepX, stepY);
        owner.repaint();

        return true;
    }

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
    public boolean tryWake(int waker, int modifier, int x, int y, int deltaX, int deltaY) {
        return false;
    }
}
