package jatools.designer.action;


import jatools.designer.App;
import jatools.designer.Main;
import jatools.util.Util;

import java.awt.event.ActionEvent;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ShowPreviewerAction extends ReportAction {
    /**
     * Creates a new ShowPreviewerAction object.
     */
    public ShowPreviewerAction() {
        super(App.messages.getString("res.187"), Util.getIcon("/jatools/icons/preview.gif"),
            Util.getIcon("/jatools/icons/preview2.gif"));
        caches.remove(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        Main.getInstance().showPreviewer();
    }
}
