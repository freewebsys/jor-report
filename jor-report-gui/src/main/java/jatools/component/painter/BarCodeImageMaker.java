package jatools.component.painter;

import jatools.component.BarCode;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import org.krysalis.barcode4j.BarcodeDimension;
import org.krysalis.barcode4j.impl.AbstractBarcodeBean;
import org.krysalis.barcode4j.output.java2d.Java2DCanvasProvider;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class BarCodeImageMaker {
    static BarCodeImageMaker defaults;

    /**
     * Creates a new BarCode2Image object.
     */
    private BarCodeImageMaker() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param coder DOCUMENT ME!
     * @param text DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public java.awt.Image getAwtImage(BarCode coder, String text) {
        java.awt.Image result = null;

        String codeType = coder.getCodeType();

        if (codeType != null) {
            AbstractBarcodeBean bean = null;
            Class clazz = null;

            try {
                clazz = coder.getBarcodeClassResolver().resolveBean(codeType);
            } catch (ClassNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            try {
                bean = (AbstractBarcodeBean) clazz.newInstance();
            } catch (InstantiationException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

            bean.doQuietZone(true);

            if ((bean == null) || (text == null)) {
                return null;
            }

            int w = coder.getWidth();
            int h = coder.getHeight();

            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

            Graphics2D g2d = (Graphics2D) img.getGraphics();
            Color back = coder.getBackColor();

            if (back == null) {
                back = Color.WHITE;
            }

            g2d.setColor(back);
            g2d.fillRect(0, 0, w, h);

            try {
                Java2DCanvasProvider canvas = new Java2DCanvasProvider(g2d, 0);

                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS,
                    RenderingHints.VALUE_FRACTIONALMETRICS_ON);

                AffineTransform baktrans = g2d.getTransform();

                try {
                    BarcodeDimension bardim = bean.calcDimensions(text);
                    double horzScale = w / bardim.getWidthPlusQuiet();
                    double vertScale = h / bardim.getHeightPlusQuiet();
                    double scale;
                    double dx = 0;
                    double dy = 0;

                    if (horzScale < vertScale) {
                        scale = horzScale;
                        dy = ((h / scale) - bardim.getHeightPlusQuiet()) / 2;
                    } else {
                        scale = vertScale;
                        dx = ((w / scale) - bardim.getWidthPlusQuiet()) / 2;
                    }

                    g2d.scale(scale, scale);
                    g2d.translate(dx, dy);

                    Color fore = coder.getForeColor();

                    if (fore == null) {
                        fore = Color.BLACK;
                    }

                    g2d.setColor(fore);
                    bean.generateBarcode(canvas, text);
                } finally {
                    g2d.setTransform(baktrans);
                }
            } catch (Exception e) {
                g2d.setColor(Color.red);
                g2d.drawLine(0, 0, w, h);
                g2d.drawLine(0, h, w, 0);
            }

            result = img;
        }

        return result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static BarCodeImageMaker getDefaults() {
        if (defaults == null) {
            defaults = new BarCodeImageMaker();
        }

        return defaults;
    }

    /**
     * DOCUMENT ME!
     *
     * @param coder DOCUMENT ME!
     * @param text DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
   
}
