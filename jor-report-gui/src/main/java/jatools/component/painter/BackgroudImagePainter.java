package jatools.component.painter;

import jatools.component.BackgroundImageStyle;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class BackgroudImagePainter {
    static BackgroudImagePainter defaults;

    private BackgroudImagePainter() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static BackgroudImagePainter getDefaults() {
        if (defaults == null) {
            defaults = new BackgroudImagePainter();
        }

        return defaults;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param css DOCUMENT ME!
     * @param ownerBounds DOCUMENT ME!
     */
    public void paint(Graphics2D g, BackgroundImageStyle css, Rectangle ownerBounds) {
        Image image = getImage(css);

        Shape clip = g.getClip();
        g.clipRect(ownerBounds.x, ownerBounds.y, ownerBounds.width, ownerBounds.height);

        int imageWidth = image.getWidth(null);
        int imageHeight = image.getHeight(null);

        if (image != null) {
            int xoff = ownerBounds.x;

            if (css.getX().isPercent()) {
                float percent = css.getX().percentValue();
                xoff += Math.round((ownerBounds.width * percent) - (imageWidth * percent));
            } else {
                xoff += css.getX().floatValue();
            }

            int yoff = ownerBounds.y;

            if (css.getY().isPercent()) {
                float percent = css.getY().percentValue();
                yoff += Math.round((ownerBounds.height * percent) - (imageHeight * percent));
            } else {
                yoff += css.getY().floatValue();
            }

            if (!css.isRepeatx() && !css.isRepeaty()) {
                g.drawImage(image, xoff, yoff, null);
            } else if (css.isRepeatx() && css.isRepeaty()) {
                repeatPaint(g, image, adjustTo(ownerBounds.x, xoff, (int) imageWidth),
                    adjustTo(ownerBounds.y, yoff, (int) imageHeight),
                    ownerBounds.x + ownerBounds.width, ownerBounds.y + ownerBounds.height);
            } else if (css.isRepeatx()) {
                xoff = adjustTo(ownerBounds.x, xoff, (int) imageWidth);

                repeatxPaint(g, image, xoff, yoff, ownerBounds.x + ownerBounds.width);
            } else if (css.isRepeaty()) {
                yoff = adjustTo(ownerBounds.y, yoff, (int) imageHeight);

                repeatyPaint(g, image, xoff, yoff, ownerBounds.y + ownerBounds.height);
            }
        }

        g.setClip(clip);
    }

    private Image getImage(BackgroundImageStyle css) {
        return (Image) css.getImageObject();
    }

    private int adjustTo(int target, int current, int imageDim) {
        int result = current;

        //   System.out.println(imageDim);
        if (result > target) {
            while (result > target) {
                result -= imageDim;
            }
        } else if (result < target) {
            while (result < target) {
                result += imageDim;
            }

            if (result != target) {
                result -= imageDim;
            }
        }

        return result;
    }

    private void repeatPaint(Graphics2D g, Image image, int left, int top, int right, int bottom) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);

        for (int x = left; x < right; x += width) {
            for (int y = top; y < bottom; y += height) {
                g.drawImage(image, x, y, null);
            }
        }
    }

    private void repeatyPaint(Graphics2D g, Image image, int left, int top, int bottom) {
        int height = image.getHeight(null);

        for (int y = top; y < bottom; y += height) {
            g.drawImage(image, left, y, null);
        }
    }

    private void repeatxPaint(Graphics2D g, Image image, int left, int top, int right) {
        int width = image.getWidth(null);

        for (int x = left; x < right; x += width) {
            g.drawImage(image, x, top, null);
        }
    }
}
