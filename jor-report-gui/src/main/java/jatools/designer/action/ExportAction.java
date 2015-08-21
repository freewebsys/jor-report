package jatools.designer.action;

import jatools.designer.App;
import jatools.designer.Main;
import jatools.designer.PageProvider;
import jatools.designer.export.ExportPanel;

import java.awt.event.ActionEvent;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ExportAction extends ReportAction {
    private PageProvider provider;

    /**
     * Creates a new ExportAction object.
     *
     * @param provider DOCUMENT ME!
     */
    public ExportAction(PageProvider provider) {
        super(App.messages.getString("res.583"), getIcon("/jatools/icons/export.gif"));
        this.provider = provider;

        caches.remove(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        ExportPanel exp = ExportPanel.getInstance();
        exp.showDialog(Main.getInstance(), provider);
    }
}
