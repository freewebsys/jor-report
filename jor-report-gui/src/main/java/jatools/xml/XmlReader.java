package jatools.xml;

import jatools.util.Util;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;



/**
 *
 *  DOCUMENT ME!
 *
 *  @version $Revision: 1.2 $
 *  @author $author$
 *
 */
public class XmlReader extends XmlBase {
    private static Logger logger = Logger.getLogger("ZXmlReader"); //
  

    /**
     * DOCUMENT ME!
     *
     * @param target
     *            DOCUMENT ME!
     * @param is
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    public static Object read(InputStream is) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

        DocumentBuilder builder = factory.newDocumentBuilder();

        Document doc = builder.parse(new BufferedInputStream(is));

        return load(doc);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static Object load(Document doc) throws Exception {
    	
        Object o = doc.getFirstChild();
        Element rootElement = (Element) o;

        try {
            Class type = Class.forName(rootElement.getAttribute(XmlBase.CLASS_ATTRIBUTE_TAG));

            XmlSerializer nodeDelegate = XmlSerializerFactory.createInstance(type);

            return nodeDelegate.read(rootElement, type);
        } catch (Exception ex) {
            throw new Exception(Util.debug(logger, ex));
        }
    }
}
