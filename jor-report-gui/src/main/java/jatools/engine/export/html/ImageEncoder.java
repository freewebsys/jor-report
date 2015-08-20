package jatools.engine.export.html;

import jatools.component.BackgroundImageStyle;
import jatools.component.ImageObjectFormat;
import jatools.component.ImageStyle;
import jatools.engine.printer.FlashImage;
import jatools.imageio.ImageWriter;
import jatools.io.ResourceOutput;
import jatools.io.ResourceOutputFactory;

import java.awt.Image;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class ImageEncoder {
    private Map caches = new HashMap();
    private ResourceOutputFactory fileFactory;

    ImageEncoder(ResourceOutputFactory fileFactory) {
        this.fileFactory = fileFactory;
    }

    boolean isHttpImage(ImageStyle style) {
        return style.getImageObjectFormat() == ImageObjectFormat.URL;
    }

    String encode(BackgroundImageStyle style) {
        if (isHttpImage(style)) {
            return style.getImageFileSrc();
        }

        String cache = (String) caches.get(style);

        if (cache != null) {
            return cache;
        } else {
            String url = getImageUrl(style);

            if (url == null) {
                return null;
            } else {
                StringBuffer result = new StringBuffer();

                String repeat = "no-repeat";
                String position = "";
                boolean isRepeatx = style.isRepeatx();
                boolean isRepeaty = style.isRepeaty();

                if (isRepeatx && !isRepeaty) {
                    repeat = "repeat-x";
                } else if (!isRepeatx && isRepeaty) {
                    repeat = "repeat-y";
                } else if (isRepeatx && isRepeaty) {
                    repeat = "repeat";
                } else {
                    float getX = style.getX().floatValue() * 100;
                    float getY = style.getY().floatValue() * 100;
                    position = "background-position:" + getX + "% " + getY + "%;";
                }

                result.append("background-image:url(" + url + ");background-repeat:" + repeat +
                    ";" + position);

                String html = result.toString();
                caches.put(style, html);

                return html;
            }
        }
    }

    String encode(ImageStyle style) {
        if (isHttpImage(style)) {
            return style.getImageFileSrc();
        }

        String cache = (String) caches.get(style);

        if (cache != null) {
            return cache;
        } else {
            String url = getImageUrl(style);

            if (url != null) {
                caches.put(style, url);
            }

            return url;
        }
    }

    private String getImageUrl(ImageStyle style) {
        String result = null;

        if (style.getImageObjectFormat() == ImageObjectFormat.AWT) {
            try {
                int exportFormat = style.getExportImageFormat();

                if (exportFormat == ImageStyle.FLASH) {
                    exportFormat = ImageStyle.PNG;
                }

                ResourceOutput f = fileFactory.createOutput("jatools",
                        "." + ImageWriter.getFileExt(exportFormat));

                OutputStream fos = f.getOutputStream();

                ImageWriter.write(fos, (Image) style.getImageObject(), exportFormat);
                fos.close();
                result = f.getUrl();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }else if (style.getImageObjectFormat() == ImageObjectFormat.FLASH) {
            try {
                ResourceOutput f = fileFactory.createOutput("jatools", ".swf");

                OutputStream fos = f.getOutputStream();
                FlashImage img = (FlashImage) style.getImageObject();

                fos.write(img.getFlash());
                fos.close();
                result = f.getUrl();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }


        return result;
    }
}
