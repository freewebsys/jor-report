package jatools.designer.action;


import jatools.component.Component;
import jatools.designer.Main;
import jatools.designer.peer.ComponentPeer;
import jatools.designer.undo.PropertyEdit;

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
public class SimplePropertyListener implements PropertyChangeListener {
    private static final SimplePropertyListener instance = new SimplePropertyListener();
    ArrayList changes = new ArrayList();

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void propertyChange(PropertyChangeEvent e) {
        Component c = (Component) e.getSource();
        ComponentPeer peer = Main.getInstance().getActiveEditor().getReportPanel()
                                 .getComponentPeer(c);

        if (peer != null) {
            PropertyEdit pe = new PropertyEdit(peer, e.getPropertyName(), e.getOldValue(),
                    e.getNewValue());

            changes.add(pe);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param edit DOCUMENT ME!
     */
    public void applyEdit(CompoundEdit edit) {
        if (!changes.isEmpty()) {
            Iterator it = changes.iterator();

            while (it.hasNext()) {
                UndoableEdit pe = (UndoableEdit) it.next();
                edit.addEdit(pe);
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void clear() {
        this.changes.clear();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static SimplePropertyListener getInstance() {
        return instance;
    }
}
