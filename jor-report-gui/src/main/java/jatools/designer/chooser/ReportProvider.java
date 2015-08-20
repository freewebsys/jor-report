package jatools.designer.chooser;

import jatools.ReportDocument;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public abstract class ReportProvider extends JPanel {
    public static final String DONE = "done";
    public static final String CANCEL = "cancel";
    ArrayList actionListeners = new ArrayList();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public abstract ReportDocument getDocument();

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void addActionListener(ActionListener lst) {
        actionListeners.add(lst);
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void removeActionListener(ActionListener lst) {
        actionListeners.remove(lst);
    }

    protected void fireActionListener(String command) {
        ActionEvent e = new ActionEvent(this, 0, command);

        for (Iterator iter = actionListeners.iterator(); iter.hasNext();) {
            ActionListener element = (ActionListener) iter.next();
            element.actionPerformed(e);
        }
    }
}
