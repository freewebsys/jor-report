package jatools.engine.printer;

import jatools.component.ImageStyle;
import jatools.component.chart.Chart;
import jatools.component.chart.ChartFactory;
import jatools.component.chart.servlet.Bean;
import jatools.engine.PrintConstants;
import jatools.engine.imgloader.ImageLoader;
import jatools.engine.script.Script;
import jatools.util.Util;

import java.awt.image.BufferedImage;
import java.util.ArrayList;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ChartPrinter extends ImagePrinter {
    protected ImageStyle getImageStyle(Script script) {
        Chart chart = (Chart) this.getComponent();

        ImageStyle lastcss = chart.getImageCSS();

        chart.getProperties().put("width", chart.getWidth() + "");
        chart.getProperties().put("height", chart.getHeight() + "");

        Bean bean = ChartFactory.createBeanInstance(chart, script);
        ImageLoader loader = (ImageLoader) script.get(PrintConstants.IMAGE_LOADER);

        if (loader.isAcceptableExportFormat(lastcss.getExportImageFormat())) {
            if (lastcss.getExportImageFormat() == ImageStyle.FLASH) {
                bean.setProperty("imageType", "swf");

                bean.isChartServlet = true;

                byte[] flash = null;

                try {
                    flash = bean.getImageBytes();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                lastcss.setFlashObject(new FlashImage(flash, null));
            } else {
                try {
                    ArrayList v = bean.getToolTips();

                    if ((v != null) && (v.size() > 0)) {
                        lastcss.setLinkMap(v);
                    }
                } catch (Exception e1) {
                }

                BufferedImage image = Util.asBufferedImage(bean.getImage());
                lastcss.setAwtObject(image);

                //                switch (lastcss.getRequiredHtmlImageFormat()) {
                //                case ImageStyle.JPG:
                //                    lastcss.setJPGImage(image);
                //
                //                    break;
                //
                //                case ImageStyle.GIF:
                //                    lastcss.setGIFImage(image);
                //
                //                    break;
                //
                //                case ImageStyle.PNG:
                //                    lastcss.setPNGImage(image);
                //
                //                    break;
                //                }
            }
        } else if (loader.isAcceptableExportFormat(ImageStyle.AWT)) {
            try {
              //  bean.generate(false);
            	try {
                    ArrayList v = bean.getToolTips();

                    if ((v != null) && (v.size() > 0)) {
                        lastcss.setLinkMap(v);
                    }
                } catch (Exception e1) {
                }
                BufferedImage image = Util.asBufferedImage(bean.getImage());
                lastcss.setAwtObject(image);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return lastcss;
    }
}
