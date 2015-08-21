package jatools.swingx;

import javax.swing.Icon;


/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.1 $
 * @author $author$
 */
public abstract class AbstractIcon implements Icon {
    int height;
    int width;

    /**
 * Creates a new ZAbstractIcon object.
 *
 * @param width DOCUMENT ME!
 * @param height DOCUMENT ME!
 */
    public AbstractIcon(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
 * DOCUMENT ME!
 *
 * @return DOCUMENT ME!
 */
    public int getIconHeight() {
        return height;
    }

    /**
 * DOCUMENT ME!
 *
 * @return DOCUMENT ME!
 */
    public int getIconWidth() {
        return width;
    }
}
