package jatools.engine.printer;

import jatools.util.UUID;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ReportCacher {
    String uuid;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getUUID() {
        if (this.uuid == null) {
            this.uuid = UUID.newInstance().toString();
        }

        return uuid;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isRequired() {
        return this.uuid != null;
    }
}
