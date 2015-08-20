package jatools.engine.imgloader;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
 */
public interface ImageLoader {
    /**
     * DOCUMENT ME!
     *
     * @param src DOCUMENT ME!
     */
    public Object load(ImageSource src);

    /**
     * DOCUMENT ME!
     *
     * @param format DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isAcceptableExportFormat(int format);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
//   / public boolean isDoubleFlashImage();
}
