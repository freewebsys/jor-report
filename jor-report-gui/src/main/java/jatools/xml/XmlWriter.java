package jatools.xml;

import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.xerces.dom3.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSParser;


public class XmlWriter extends XmlBase {
    public static void write(Object target, OutputStream os)
        throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.newDocument();

        save(document, target);

        TransformerFactory tFactory = TransformerFactory.newInstance();
        Transformer transformer = tFactory.newTransformer();
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(os);
        transformer.transform(source, result);
    }

    private static void save(Document doc, Object target)
        throws Exception {
        XmlSerializer nodeDelegate = XmlSerializerFactory.createInstance(target.getClass());
        Element rootElement = doc.createElement("jatools");
        rootElement.setAttribute(XmlBase.NAME_ATTRIBUTE_TAG, XmlBase.REPORT_TITLE);
        doc.appendChild(rootElement);
        nodeDelegate.write(rootElement, target);
        rootElement.setAttribute(XmlBase.CLASS_ATTRIBUTE_TAG, target.getClass().getName());
    }

    public static void main(String[] args) {
        try {
            System.setProperty(DOMImplementationRegistry.PROPERTY,
                "org.apache.xerces.dom.DOMImplementationSourceImpl");

            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
            DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");

            LSParser builder = impl.createLSParser(DOMImplementationLS.MODE_SYNCHRONOUS, null);

            Document document = builder.parseURI("http://www.nicholaschase.com/personal.xml");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
}
