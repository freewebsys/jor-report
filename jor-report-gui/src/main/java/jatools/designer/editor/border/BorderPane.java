package jatools.designer.editor.border;

import jatools.core.view.Border;

import javax.swing.JComponent;
import javax.swing.event.ChangeListener;



/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.1 $
 * @author $author$
 */
public interface BorderPane {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    Border getValue();

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    void addChangeListener(ChangeListener lst);

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    void removeChangeListener(ChangeListener lst);

    /**
     * DOCUMENT ME!
     *
     * @param border DOCUMENT ME!
     */
    void setValue(Object value);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    boolean isCompatible(Object border);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    JComponent getComponent();
}
