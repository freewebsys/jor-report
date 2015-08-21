package jatools.designer.action;

import jatools.designer.SelectionState;

import java.awt.event.ActionEvent;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class BorderEnableAction extends ReportAction {
    /**
     * Creates a new BorderEnableAction object.
     */
    public BorderEnableAction() {
        super(null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param state DOCUMENT ME!
     */
    public void enabled(SelectionState state) {
        this.firePropertyChange("selection", null, this);
    }
}
