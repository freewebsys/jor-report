package jatools.designer;

import jatools.component.ImageStyle;

import java.util.HashMap;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ImageCacher {
    Map images = new HashMap();

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ImageStyle getImageStyle(String key) {
        key = normalize(key);

        return (ImageStyle) images.get(key);
    }

    private String normalize(String key) {
        if (key.length() > 300) {
            key = key.substring(0, 300);
        }

        return key;
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param style DOCUMENT ME!
     */
    public void cache(String key, ImageStyle style) {
        this.images.put(normalize(key), style);
    }
}
