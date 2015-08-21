package jatools.designer.wizard.table;


import jatools.ReportDocument;
import jatools.designer.wizard.BuilderContext;
import jatools.designer.wizard.ReportBuilder;
import jatools.designer.wizard.ReportStyler;

import java.awt.Frame;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class TableReportBuilder implements ReportBuilder {
    private static TableReportBuilder shared;
    ReportStyler styler;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static TableReportBuilder getInstance() {
        if (shared == null) {
            shared = new TableReportBuilder();
        }

        return shared;
    }

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     * @param context DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ReportDocument build(Frame owner, BuilderContext context) {
        TableBuilder editor = new TableBuilder(owner, context);
        editor.setLocationRelativeTo(owner);
        editor.show();

        if (editor.isExitOK()) {
            ReportDocument doc = new ReportDocument();

            if (styler != null) {
                styler.format(doc, context);
            }

            return doc;
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param styler DOCUMENT ME!
     */
    public void setStyler(ReportStyler styler) {
        this.styler = styler;
    }
}
