package jatools.engine.imgloader;

import jatools.component.ImageObjectFormat;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public interface ImageSource {
    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getImageFileSrc();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getImageObject();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ImageObjectFormat getImageObjectFormat();

    /**
     * DOCUMENT ME!
     *
     * @param image DOCUMENT ME!
     */
    public void setUrlObject(Object image);

    /**
     * DOCUMENT ME!
     *
     * @param image DOCUMENT ME!
     */
    public void setAwtObject(Object image);


    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getExportImageFormat();
}
