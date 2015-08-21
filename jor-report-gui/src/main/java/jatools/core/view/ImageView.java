package jatools.core.view;

import jatools.component.ImageStyle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;


/**
 * @author   java9
 */
public class ImageView extends AbstractView {
    static final long serialVersionUID = 20030716004L;

    private ImageStyle imageStyle;

    /**
     * Creates a new ImageView object.
     */
    public ImageView() {
    }

   
    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics2D g) {
        Border border = getBorder();
        Rectangle b = (border != null)
            ? deflateRect((Rectangle) getBounds().clone(), border.getInsets()) : getBounds();

        Graphics2D gcopy = (Graphics2D) g.create();
        Shape copy = gcopy.getClip();
        gcopy.clip(b);

        Color color = getBackColor();

        if (color != null) {
            gcopy.setColor(getBackColor());
            gcopy.fillRect(b.x, b.y, b.width, b.height);
        }

        ImageStyle css = getImageStyle();

        if (css != null) {
            gcopy.translate(b.x, b.y);

            css.paint(gcopy, b.width, b.height);

            gcopy.translate(-b.x, -b.y);
        }

        gcopy.setClip(copy);

        if (getBorder() != null) {
            getBorder().paint(gcopy, getBounds());
        }

        gcopy.dispose();
    }

    /**
         * @return   Returns the backColor.
         * @uml.property   name="backColor"
         */

    /**
         * @return   Returns the border.
         * @uml.property   name="border"
         */

    /**
         * @return   Returns the link.
         * @uml.property   name="link"
         */

    /**
     * DOCUMENT ME!
     *
     * @param css DOCUMENT ME!
     */
    public void setImageStyle(ImageStyle css) {
        this.imageStyle = css;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ImageStyle getImageStyle() {
        return imageStyle;
    }
}
