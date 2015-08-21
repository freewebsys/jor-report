package jatools.designer.action;

import jatools.component.Panel;
import jatools.designer.App;

import java.awt.event.ActionEvent;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.6 $
  */
public class NewPanelAction extends ReportAction {
    /**
     * Creates a new ZInsertTableAction object.
     *
     * @param owner DOCUMENT ME!
     */
    public NewPanelAction() {
        super(App.messages.getString("res.591"), getIcon("/jatools/icons/panel.gif"), getIcon("/jatools/icons/panel2.gif"));
        putValue(CLASS,Panel.class );
        
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        Panel p = new Panel();
        p.setBounds(0, 0, 300, 200);

        this.getEditor().getReportPanel().setBaby(p);
    }
}
