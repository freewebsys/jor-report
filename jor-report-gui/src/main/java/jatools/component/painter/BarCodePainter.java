package jatools.component.painter;

import jatools.component.BarCode;
import jatools.component.Component;
import jatools.component.ImageStyle;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.5 $
  */
public class BarCodePainter extends ImagePainter {
    static final String SAMPLE_CODE = "12345678901222"; //

    protected java.awt.Image loadImage(Component c, ImageStyle css) {
        java.awt.Image image = BarCodeImageMaker.getDefaults().getAwtImage((BarCode) c, SAMPLE_CODE);
        css.setAwtObject(image);

        return image;
    }
}
