package jatools.engine.printer;


import jatools.accessor.ProtectPublic;
import jatools.core.view.DisplayStyle;
import jatools.core.view.DisplayStyleManager;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class StyleClassVariable implements ProtectPublic {
    private DisplayStyleManager manager;

    /**
     * Creates a new StyleClassVariable object.
     *
     * @param manager DOCUMENT ME!
     */
    public StyleClassVariable(DisplayStyleManager manager) {
        this.manager = manager;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DisplayStyle get(String name) {
        return manager.getStockStyle(name);
    }
}
