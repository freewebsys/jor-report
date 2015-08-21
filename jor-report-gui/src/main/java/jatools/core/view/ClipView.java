package jatools.core.view;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Stack;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ClipView extends AbstractView {
    static final long serialVersionUID = 20030716003L;
    static Stack cache = new Stack();

    /**
     * Creates a new ClipView object.
     */
    public ClipView() {
        bounds = null;
    }

    /**
     * Creates a new ClipView object.
     *
     * @param clip DOCUMENT ME!
     */
    public ClipView(Shape clip) {
        this.bounds = (Rectangle) clip;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isNull() {
        return bounds == null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics2D g) {
        if (getBounds() != null) {
            cache.push(g.getClip());
            g.clip(getBounds());
        } else {
            try {
                Shape c = (Shape) cache.pop();

                if (c != null) {
                    g.setClip(c);
                }
            } catch (Exception ex) {
            }
        }
    }
}
