package jatools.core.view;

import java.awt.Graphics2D;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class TransformView implements View {
    static final long serialVersionUID = 20030716013L;
    public int x;
    public int y;

    /**
     * Creates a new TransformView object.
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     */
    public TransformView(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics2D g) {
        g.translate(x, y);
    }
}
