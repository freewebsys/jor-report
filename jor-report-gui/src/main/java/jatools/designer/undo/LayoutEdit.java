package jatools.designer.undo;

import java.awt.event.ActionListener;


public interface LayoutEdit {
    /**
     * DOCUMENT ME!
     */
    public static final int UNDO = 1;

    /**
     * DOCUMENT ME!
     */
    public static final int REDO = 2;

    /**
     * DOCUMENT ME!
     *
     * @param al DOCUMENT ME!
     */
    public void addActionListener(ActionListener al);
}
