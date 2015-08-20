package jatools.designer.data;

import jatools.accessor.AutoAccessor;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class Hyperlink extends AutoAccessor {
    String url;
    String target;

    /**
     * Creates a new Hyperlink object.
     *
     * @param url DOCUMENT ME!
     * @param target DOCUMENT ME!
     */
    public Hyperlink(String url, String target) {
        this.url = url;
        this.target = target;
    }

    /**
     * Creates a new Hyperlink object.
     */
    public Hyperlink() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getTarget() {
        return target;
    }

    /**
     * DOCUMENT ME!
     *
     * @param target DOCUMENT ME!
     */
    public void setTarget(String target) {
        this.target = target;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getUrl() {
        return url;
    }

    /**
     * DOCUMENT ME!
     *
     * @param url DOCUMENT ME!
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        String result = "";

        if (target != null) {
            result = "target:" + target + ";";
        }

        if (url != null) {
            result += ("url:" + url);
        }

        return result;
    }
}
