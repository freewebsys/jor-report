package jatools.component;

import jatools.xml.serializer.XmlWriteListener;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class ComponentXmlWriteListener implements XmlWriteListener {
    private Component c;

    /**
    * Creates a new ComponentXmlWriteListener object.
    */
    public ComponentXmlWriteListener(Component c) {
        this.c = c;
    }

    /**
     * DOCUMENT ME!
     */
    public void beforeWrite() {
    }

    /**
     * DOCUMENT ME!
     */
    public void afterWrite() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isWritable(String prop) {
        if ((this.c.getCell() != null) && ("Width,Height,X,Y".indexOf(prop) > -1)) {
            return false;
        } else {
            return true;
        }
    }
}
