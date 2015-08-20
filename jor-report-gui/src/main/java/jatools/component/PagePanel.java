package jatools.component;

import jatools.accessor.PropertyDescriptor;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class PagePanel extends Panel {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PropertyDescriptor[] getRegistrableProperties() {
        return new PropertyDescriptor[] {
            ComponentConstants._NAME,
            ComponentConstants._BACK_COLOR,
            ComponentConstants._BORDER,
            ComponentConstants._BACKGROUND_IMAGE,
            ComponentConstants._PRINT_STYLE,
            ComponentConstants._X,
            ComponentConstants._Y,
            ComponentConstants._WIDTH,
            ComponentConstants._HEIGHT,
            ComponentConstants._CHILDREN,
            ComponentConstants._TYPE,
            ComponentConstants._CELL,
            ComponentConstants._INIT_PRINT,
            ComponentConstants._AFTERPRINT,
            ComponentConstants._BEFOREPRINT2
        };
    }
}
