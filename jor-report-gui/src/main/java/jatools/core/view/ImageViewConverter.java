package jatools.core.view;

import jatools.component.ImageStyle;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ImageViewConverter {
    static ImageViewConverter defaults;

    private ImageViewConverter() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ImageViewConverter getDefaults() {
        if (defaults == null) {
            defaults = new ImageViewConverter();
        }

        return defaults;
    }

    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ImageView toImageView(AbstractView src) {
        ImageView e = new ImageView();
        e.setBounds(src.getBounds());
        e.setBackColor(src.getBackColor());
        e.setImageStyle(getImageStyle(src));
        e.setHyperlink(src.getHyperlink());
        e.setTooltipText(src.getTooltipText());
        e.setBorder(src.getBorder());
        e.setCell(src.getCell());

        return e;
    }

    private ImageStyle getImageStyle(AbstractView src) {
        ImageStyle imageStyle = new ImageStyle(null, 0, 0, false, ImageStyle.PNG);
        int w = src.getBounds().width;
        int h = src.getBounds().height;

        BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = (Graphics2D) img.getGraphics();
        Color back = src.getBackColor();

        if (back == null) {
            back = Color.WHITE;
        }

        g2d.setColor(back);
        g2d.fillRect(0, 0, w, h);

        Border oldborder = src.getBorder();
        src.setBorder(null);
        g2d.translate(-src.getBounds().x, -src.getBounds().y);
        src.paint(g2d);

        src.setBorder(oldborder);
        imageStyle.setAwtObject(img);

        return imageStyle;
    }
}
