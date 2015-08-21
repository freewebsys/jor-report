package jatools.engine.printer;

import jatools.component.BarCode;
import jatools.component.ImageExportFormat;
import jatools.component.ImageStyle;
import jatools.component.painter.BarCodeImageMaker;
import jatools.engine.PrintConstants;
import jatools.engine.imgloader.ImageLoader;
import jatools.engine.script.Script;

import java.awt.Image;
import java.awt.image.BufferedImage;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class BarCodePrinter extends ImagePrinter {
    static BufferedImage img = new BufferedImage(100, 100, 1);

    protected ImageStyle getImageStyle(Script script) {
        BarCode coder = (BarCode) this.getComponent();

        ImageStyle lastcss = coder.getImageStyle();
        ImageLoader loader = (ImageLoader) script.get(PrintConstants.IMAGE_LOADER);

        String text = null;

        if (coder.getVariable() != null) {
            Object value = script.get(coder.getVariable());

            if (value != null) {
                text = value.toString().toUpperCase();
            }
        }

        if (text == null) {
            return null;
        }

        if (loader.isAcceptableExportFormat(lastcss.getExportImageFormat())) {
            if (lastcss.getExportImageFormat() == ImageExportFormat.FLASH) {
//                byte[] flash = getFlashImage((BarCode) coder, text);
//                lastcss.setFlashObject(new FlashImage(flash, null));
//                return lastcss;
            	// TODO
            	new UnsupportedOperationException("暂时不支持flash格式输出, 待统计图移植过来后再支持!");
            }
        }

        Image image = BarCodeImageMaker.getDefaults().getAwtImage((BarCode) coder, text);
        lastcss.setAwtObject(image);

        return lastcss;
    }

    /**
     * DOCUMENT ME!
     *
     * @param coder DOCUMENT ME!
     * @param text DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
//    public byte[] getFlashImage(BarCode coder, String text) {
//        String codeType = coder.getCodeType();
//
//        if (codeType != null) {
//            AbstractBarcodeBean bean = null;
//            Class clazz = null;
//
//            try {
//                clazz = BarCode.getBarcodeClassResolver().resolveBean(codeType);
//            } catch (ClassNotFoundException e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();
//            }
//
//            try {
//                bean = (AbstractBarcodeBean) clazz.newInstance();
//            } catch (InstantiationException e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();
//            } catch (IllegalAccessException e1) {
//                // TODO Auto-generated catch block
//                e1.printStackTrace();
//            }
//
//            bean.doQuietZone(true);
//
//            if ((bean == null) || (text == null)) {
//                return null;
//            }
//
//            int w = coder.getWidth();
//            int h = coder.getHeight();
//
//            java.awt.Graphics2D g = img.createGraphics();
//
//            FlashGraphics g2d = new FlashGraphics(g, w, h);
//
//            Color back = coder.getBackColor();
//
//            if (back == null) {
//                back = Color.WHITE;
//            }
//
//            g2d.setColor(back);
//            g2d.fillRect(0, 0, w, h);
//
//            try {
//                Java2DCanvasProvider canvas = new Java2DCanvasProvider(g2d, 0);
//
//                AffineTransform baktrans = g2d.getTransform();
//
//                try {
//                    BarcodeDimension bardim = bean.calcDimensions(text);
//                    float horzScale = (float) (w / bardim.getWidthPlusQuiet());
//                    float vertScale = (float) (h / bardim.getHeightPlusQuiet());
//                    float scale;
//                    double dx = 0;
//                    double dy = 0;
//
//                    if (horzScale < vertScale) {
//                        scale = horzScale;
//                        dy = ((h / scale) - bardim.getHeightPlusQuiet()) / 2;
//                    } else {
//                        scale = vertScale;
//                        dx = ((w / scale) - bardim.getWidthPlusQuiet()) / 2;
//                    }
//
//                    g2d.setScale(new Point2D.Float(scale, scale));
//                    g2d.translate(dx * scale, dy * scale);
//
//                    Color fore = coder.getForeColor();
//
//                    if (fore == null) {
//                        fore = Color.BLACK;
//                    }
//
//                    g2d.setColor(fore);
//                    bean.generateBarcode(canvas, text);
//                } finally {
//                    g2d.setTransform(baktrans);
//                }
//            } catch (Exception e) {
//                g2d.setColor(Color.red);
//                g2d.drawLine(0, 0, w, h);
//                g2d.drawLine(0, h, w, 0);
//            }
//
//            ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//            try {
//                g2d.write(out);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return out.toByteArray();
//        } else {
//            return null;
//        }
//    }
}
