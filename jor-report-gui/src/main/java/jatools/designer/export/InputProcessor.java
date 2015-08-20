package jatools.designer.export;

/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.1 $
 * @author $author$
 */
public interface InputProcessor {
    /**
    * DOCUMENT ME!
    *
    * @param id DOCUMENT ME!
    * @param modifier DOCUMENT ME!
    * @param x DOCUMENT ME!
    * @param y DOCUMENT ME!
    */
    void processMouseEvent(int id,
                           int modifier,
                           int x,
                           int y);

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    boolean isIdle();

    /**
    * DOCUMENT ME!
    */
    void started();

    /**
    * DOCUMENT ME!
    */
    void stopped();
}
