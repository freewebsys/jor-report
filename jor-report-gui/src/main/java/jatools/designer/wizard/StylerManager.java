package jatools.designer.wizard;


import jatools.designer.wizard.blank.BlankStyler;
import jatools.designer.wizard.crosstab.CrossTabStyler;
import jatools.designer.wizard.table.TableStyler;

import java.util.ArrayList;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class StylerManager {
    static private ArrayList stylerCache;

    /**
     * DOCUMENT ME!
     *
     * @param styler DOCUMENT ME!
     */
    public static void registerStyler(ReportStyler styler) {
        if (stylerCache == null) {
            stylerCache = new ArrayList();
        }

        stylerCache.add(styler);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ReportStyler[] getStylers() {
        if (stylerCache == null) {
            registerStyler(new BlankStyler());
            registerStyler(new TableStyler());

            registerStyler(new CrossTabStyler());
        }

        return (ReportStyler[]) stylerCache.toArray(new ReportStyler[0]);
    }

    /**
     * DOCUMENT ME!
     */
    public static void unregisterAllStyler() {
        stylerCache.clear();
    }

    /**
     * DOCUMENT ME!
     *
     * @param styler DOCUMENT ME!
     */
    public static void unregisterStyler(ReportStyler styler) {
        stylerCache.remove(styler);
    }
}
