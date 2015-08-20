package jatools.engine.printer;

import java.awt.Image;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class FlashImage {
    private byte[] flash;
    private Image image2;

    /**
     * Creates a new FlashImage object.
     *
     * @param flash DOCUMENT ME!
     * @param image2 DOCUMENT ME!
     */
    public FlashImage(byte[] flash, Image image2) {
        this.flash = flash;
        this.image2 = image2;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public byte[] getFlash() {
        return flash;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Image getImage2() {
        return image2;
    }
}
