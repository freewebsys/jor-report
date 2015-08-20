package jatools.xml.serializer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public interface XmlWriteListener {
    /**
     * DOCUMENT ME!
     */
    public void beforeWrite();

    /**
     * DOCUMENT ME!
     */
    public void afterWrite();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isWritable(String prop);
}
