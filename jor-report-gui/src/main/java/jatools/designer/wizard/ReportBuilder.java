package jatools.designer.wizard;

import jatools.ReportDocument;

import java.awt.Frame;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public interface ReportBuilder {
    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     * @param context DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ReportDocument build(Frame owner, BuilderContext context);
}
