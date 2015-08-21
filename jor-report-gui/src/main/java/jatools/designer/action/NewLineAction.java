/*
 *   Author: John.
 *
 *   杭州杰创软件   All Copyrights Reserved.
 */
package jatools.designer.action;

import jatools.component.Line;

import java.awt.event.ActionEvent;


/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.6 $
 * @author $author$
 */
public class NewLineAction extends ReportAction {
    /**
     * Creates a new NewLineAction object.
     */
    public NewLineAction() {
        super("线", getIcon("/com/jatools/icons/line.gif")); // //$NON-NLS-2$
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        Line line = new Line();
        line.setBounds(0, 0, 300, 200);

        this.getEditor().getReportPanel().setBaby(line);
    }
}
