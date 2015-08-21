package jatools.engine.imgloader;

import jatools.component.ImageExportFormat;
import jatools.component.ImageObjectFormat;
import jatools.engine.script.Script;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class PainterImageLoader extends AbstractImageLoader {
    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object load(ImageSource src) {
        if ((src.getImageObjectFormat() != ImageObjectFormat.AWT)) {
            String path = src.getImageFileSrc();

            if (path != null) {
                java.awt.Image result = loadImage(path);
                src.setAwtObject(result);
            }
        }

        return src.getImageObject();
    }

    protected Script getScript() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param format DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isAcceptableExportFormat(int format) {
        return format == ImageExportFormat.AWT;
    }
}
