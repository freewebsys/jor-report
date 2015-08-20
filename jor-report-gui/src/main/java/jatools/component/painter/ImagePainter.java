package jatools.component.painter;

import jatools.component.Component;
import jatools.component.Image;
import jatools.component.ImageStyle;
import jatools.designer.Main;
import jatools.engine.imgloader.ImageLoader;
import jatools.util.Util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MediaTracker;
import java.awt.Rectangle;

import javax.swing.JLabel;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
 */
public class ImagePainter extends SimplePainter {
	final static JLabel IMAGE_OBSERVER = new JLabel();
    /**
     * DOCUMENT ME!
     *
     * @param g2
     *            DOCUMENT ME!
     */
    public void paintComponent(Graphics2D g2, Component c) {
        Image image = (Image) c;

        Rectangle b = Util.deflateRect(c.getBounds(), c.getInsets());

        Graphics2D gcopy = (Graphics2D) g2.create();
        gcopy.clip(b);

        Color color = c.getBackColor();

        if (color != null) {
            gcopy.setColor(c.getBackColor());
            gcopy.fillRect(b.x, b.y, b.width, b.height);
        }

        ImageStyle css = image.getImageStyle();

        if (css != null) {
            java.awt.Image _image = loadImage(image,css);
            
            MediaTracker mt = new MediaTracker(IMAGE_OBSERVER);
            mt.addImage( _image,0);
            try {
				mt.waitForID( 0);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

            if (_image != null) {
                int imageWidth = _image.getWidth(null);
                int imageHeight = _image.getHeight(null);
                
            //    System.out.println(imageWidth+","+imageHeight);

                if (css.isStretches()) {
                    gcopy.drawImage(_image, b.x, b.y, b.width, b.height, null);
                } else {
                    int xoff = b.x;

                    if (css.getX().isPercent()) {
                        float percent = css.getX().percentValue();
                        xoff += Math.round((b.width * percent) - (imageWidth * percent));
                    } else {
                        xoff += css.getX().floatValue();
                    }

                    int yoff = b.y;

                    if (css.getY().isPercent()) {
                    	float percent = css.getY().percentValue();
                        yoff += Math.round((b.height * percent) - (imageHeight * percent));
                    } else {
                        yoff += css.getY().floatValue();
                    }

                    gcopy.drawImage(_image, xoff, yoff, null);
                }
            }
        }

        gcopy.dispose();
    }

    protected java.awt.Image loadImage(Component c,ImageStyle css) {
        java.awt.Image image = null;
        ImageLoader loader = Main.getInstance().getActiveEditor().getImageLoader();
        image = (java.awt.Image) loader.load(css);

        return image;
    }
    
    public static void main(String[] args) {
		System.out.println("\u201d");
	}
}
