package jatools.designer;

import java.beans.PropertyChangeListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public interface SelectionManager {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] getSelection();

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void addSelectionPropertyListener(PropertyChangeListener lst);

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void removeSelectionPropertyListener(PropertyChangeListener lst);
}
