package jatools.designer.action;

import jatools.component.Text;
import jatools.designer.App;

import java.awt.event.ActionEvent;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
  */
public class NewTextAction extends ReportAction {
    /**
     * Creates a new NewTextAction object.
     *
     * @param owner DOCUMENT ME!
     */
    public NewTextAction() {
        super(App.messages.getString("res.594"), getIcon("/jatools/icons/text.gif")); // //$NON-NLS-2$
        putValue(CLASS, Text.class);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        Text text = new Text();
        text.setWidth(110);
        text.setHeight(23);
        this.getEditor().getReportPanel().setBaby(text);
    }
}
