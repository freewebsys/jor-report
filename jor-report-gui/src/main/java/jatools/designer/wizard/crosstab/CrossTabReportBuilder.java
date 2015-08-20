package jatools.designer.wizard.crosstab;

import jatools.ReportDocument;
import jatools.designer.wizard.BuilderContext;
import jatools.designer.wizard.ReportBuilder;
import jatools.designer.wizard.ReportStyler;

import java.awt.Frame;



/**
 * <p>Title:CrossTabReportBuilder </p>
 * <p>Description: 创建交叉报表</p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company:Jatools </p>
 * @author Jiang Dehua
 * @version 1.0
 */
public class CrossTabReportBuilder implements ReportBuilder {
    private static CrossTabReportBuilder shared;
    ReportStyler styler;

    /**
     * Creates a new CrossTabReportBuilder object.
     */
    public CrossTabReportBuilder() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static CrossTabReportBuilder getInstance() {
        if (shared == null) {
            shared = new CrossTabReportBuilder();
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
        CrossTableBuilder editor = new CrossTableBuilder(owner, context);
        editor.setModal(true);
        editor.setLocationRelativeTo(owner);
        editor.show();

        if (editor.isExitOk()) {
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
