package jatools.engine.imgloader;

import jatools.component.ImageExportFormat;
import jatools.component.ImageObjectFormat;
import jatools.engine.script.Script;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class AwtImageLoader extends AbstractImageLoader {
    Script script;

    /**
     * Creates a new Graphics2DImageLoader object.
     *
     * @param script DOCUMENT ME!
     */
    public AwtImageLoader(Script script) {
        this.script = script;
    }

    /**
     * DOCUMENT ME!
     *
     * @param imagePath
     *            DOCUMENT ME!
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
        return this.script;
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
