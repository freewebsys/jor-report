/*
 * Created on 2003-11-11
 *
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package jatools.designer.action;

import jatools.component.BarCode;

import java.awt.event.ActionEvent;


/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.5 $
 * @author $author$
 */
public class NewBarcodeAction extends ReportAction {
    /**
     * Creates a new NewBarcodeAction object.
     *
     * @param owner DOCUMENT ME!
     */
    public NewBarcodeAction() {
        super("条形码", getIcon("/jatools/icons/barcode.gif"), getIcon("/jatools/icons/barcode2.gif")); // //$NON-NLS-2$

        putValue(CLASS, BarCode.class);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e
     *            DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        BarCode code = new BarCode();
        code.setWidth(230);
        code.setHeight(45);
        this.getEditor().getReportPanel().setBaby(code);
    }
}
