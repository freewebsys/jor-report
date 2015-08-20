package jatools.designer.action;

import jatools.component.Component;
import jatools.designer.App;
import jatools.designer.ReportEditor;
import jatools.designer.SelectionState;
import jatools.designer.peer.ComponentPeer;
import jatools.designer.undo.GroupEdit;

import java.awt.event.ActionEvent;

import javax.swing.undo.CompoundEdit;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class CenterToParentAction extends ReportAction {
    /**
     * Creates a new CenterToParentAction object.
     */
    public CenterToParentAction() {
        super(App.messages.getString("res.31"), getIcon("/jatools/icons/center.gif"),
            getIcon("/jatools/icons/center2.gif"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param state DOCUMENT ME!
     */
    public void enabled(SelectionState state) {
        setEnabled((state.getCount() > 0) && state.isMoveable());
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        ReportEditor editor = getEditor();
        ComponentPeer[] peers = (ComponentPeer[]) editor.getReportPanel().getSelection();

        SimplePropertyListener propertyChangeListener = SimplePropertyListener.getInstance();
        propertyChangeListener.clear();

        Component.temppropertylistener.set(propertyChangeListener);

        boolean happen = false;

        for (int i = 0; i < peers.length; i++) {
            ComponentPeer peer = peers[i];

            ComponentPeer parentPeer = peer.getParent();

            int pw = parentPeer.getWidth();
            int cw = peer.getWidth();

            int dx = ((pw - cw) / 2) - peer.getX();

            if (dx != 0) {
                Component c = peer.getComponent();
                c.setX(c.getX() + dx);
                happen = true;
            }
        }

        if (happen) {
            CompoundEdit edit = new GroupEdit();
            propertyChangeListener.applyEdit(edit);
            Component.temppropertylistener.set(null);
            getEditor().getUndoManager().addEdit(edit);

            editor.repaint();
        }
    }
}
