/*
 *   Author: John.
 *
 *   杭州杰创软件   All Copyrights Reserved.
 */


package jatools.designer.action;

import jatools.component.Label;
import jatools.designer.App;

import java.awt.event.ActionEvent;


/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.5 $
 * @author $author$
 */
public class NewLabelAction extends ReportAction {
    public NewLabelAction() {
        super(App.messages.getString("res.590"), getIcon("/jatools/icons/label.gif"), getIcon("/jatools/icons/label2.gif")); // //$NON-NLS-2$
        putValue(CLASS,Label.class );
      }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
    
    	
    	Label text = new Label();
        text.setWidth(110);
        text.setHeight(23);
        this.getEditor().getReportPanel().setBaby(text);
    }
}
