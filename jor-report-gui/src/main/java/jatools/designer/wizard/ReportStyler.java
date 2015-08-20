package jatools.designer.wizard;

import jatools.ReportDocument;

import javax.swing.Icon;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public interface ReportStyler {
    void format(ReportDocument doc, BuilderContext context);

    String getDescription();

    String getName();

    Icon getIcon();

    ReportBuilder getBuilder();
}
