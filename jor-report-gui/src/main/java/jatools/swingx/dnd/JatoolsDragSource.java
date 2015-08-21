package jatools.swingx.dnd;


import java.awt.Component;


/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.1 $
 * @author $author$
 */
public interface JatoolsDragSource {
    /**
     * DOCUMENT ME!
     */
    public void complete(boolean whole);

    /**
     * DOCUMENT ME!
     */
    public void cancel();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getObject();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] getAllObject();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getSourceComponent();
}
