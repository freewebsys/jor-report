package jatools.designer.undo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.undo.AbstractUndoableEdit;


public class AbstractLayoutEdit extends AbstractUndoableEdit implements LayoutEdit {
    ArrayList listeners;

    /**
     * DOCUMENT ME!
     *
     * @param al DOCUMENT ME!
     */
    public void addActionListener(ActionListener al) {
        if (this.listeners == null) {
            this.listeners = new ArrayList();
        }

        this.listeners.add(al);
    }

    /**
     * DOCUMENT ME!
     *
     * @param action DOCUMENT ME!
     */
    public void fireListener(int action) {
        if (this.listeners != null) {
            ActionEvent e = new ActionEvent(this, action, null);
            Iterator it = this.listeners.iterator();

            while (it.hasNext()) {
                ActionListener al = (ActionListener) it.next();
                al.actionPerformed(e);
            }
        }
    }
}
