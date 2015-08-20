package jatools.designer.ruler;

import javax.swing.event.ChangeListener;


public interface Sizer {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWidth();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHeight();

    /**
     * DOCUMENT ME!
     *
     * @param cl DOCUMENT ME!
     */
    public void addChangeListener(ChangeListener cl);
}
