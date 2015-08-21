package jatools.designer.action;

import jatools.designer.App;

import java.awt.event.ActionEvent;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class SelectAction extends ReportAction {
    /**
     * Creates a new SelectAction object.
     */
    public SelectAction() {
        super(App.messages.getString("res.600"), getIcon("/jatools/icons/arrow.gif"),
            getIcon("/jatools/icons/arrow2.gif"));
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        getEditor().getReportPanel().prepareChildDone();
    }
}
