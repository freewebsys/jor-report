package jatools.swingx.dnd;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: EZSoft.</p>
 * @author 周文军
 * @version 1.0
 */
import java.awt.Component;


/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.1 $
 * @author $author$
 */
public interface JatoolsDropTarget {
    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canDrop(JatoolsDragSource obj);

    /**
     * DOCUMENT ME!
     *
     * @param source DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean drop(JatoolsDragSource source);

    /**
     * DOCUMENT ME!
     *
     * @param source DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean dropAll(JatoolsDragSource source);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getTargetComponent();
}
