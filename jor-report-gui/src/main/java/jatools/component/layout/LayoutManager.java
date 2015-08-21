package jatools.component.layout;

import jatools.component.Component;

import java.awt.Insets;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public interface LayoutManager {
    static final Insets NULL_INSETS = new Insets(0, 0, 0, 0);

    /**
     * DOCUMENT ME!
     *
     * @param comp DOCUMENT ME!
     */
    public void layout(Component comp);

    /**
     * DOCUMENT ME!
     *
     * @param comp DOCUMENT ME!
     */
    public void layout2(Component comp);
}
