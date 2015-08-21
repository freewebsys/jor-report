package jatools.imageio;

import jatools.designer.App;

import java.awt.Image;
import java.io.OutputStream;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class ImageWriter {
    /**
     * DOCUMENT ME!
     */
    public final static int PNG = 0;

    /**
     * DOCUMENT ME!
     */
    public final static int GIF = 1;

    /**
     * DOCUMENT ME!
     */
    public final static int JPEG = 2;

    /**
     * DOCUMENT ME!
     */
    public final static int FLASH = 3;

    /**
     * DOCUMENT ME!
     */
    public final static String[] FILE_EXTENSION = new String[] {
            "png",
            "gif",
            "jpg",
            "swf"
        };

    /**
     * DOCUMENT ME!
     *
     * @param os DOCUMENT ME!
     * @param image DOCUMENT ME!
     * @param type DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void write(OutputStream os, Image image, int type)
        throws Exception {
        if (type == GIF) {
            GifEncoder en = new GifEncoder(image);
            en.write(os);
        } else if (type == PNG) {
            PngEncoder en = new PngEncoder(image);
            os.write(en.pngEncode());
        } else if (type == JPEG) {
            JpegEncoder en = new JpegEncoder(image, 100, os);
            en.encode();
        } else {
            throw new Exception(App.messages.getString("res.46") + type);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String getFileExt(int type) {
        return FILE_EXTENSION[type];
    }
}
