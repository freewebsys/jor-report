package jatools.designer.layer.dbclick;


import jatools.designer.ReportPanel;
import jatools.designer.componenteditor.ComponentEditor;
import jatools.designer.componenteditor.ComponentEditorFactory;
import jatools.designer.layer.AbstractLayer;
import jatools.designer.peer.ComponentPeer;
import jatools.util.CursorUtil;

import java.awt.event.InputEvent;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class DoubleClickLayer extends AbstractLayer {
    ReportPanel owner;
    ComponentPeer peer;

    /**
     * Creates a new DoubleClickLayer object.
     *
     * @param owner DOCUMENT ME!
     */
    public DoubleClickLayer(ReportPanel owner) {
        this.owner = owner;

        setCursor(CursorUtil.DEFAULT_CURSOR);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int wakenBy() {
        return WAKEN_BY_MOUSE_DOUBLE_PRESSED;
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
        peer = owner.findComponentPeerAt(x, y);

        if (!peer.isEditing()) {
            ComponentEditor editor = ComponentEditorFactory.createEditor(peer,
                    (InputEvent.CTRL_MASK & modifier) != 0);

            if (editor != null) {
                editor.show(peer);
            }
        }

        return false;
    }
}
