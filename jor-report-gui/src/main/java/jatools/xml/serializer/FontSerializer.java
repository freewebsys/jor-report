package jatools.xml.serializer;

import java.awt.Font;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


public class FontSerializer extends AbstractSerializer {
    private static final String FACE = "Face";
    private static final String STYLE = "Style";
    private static final String SIZE = "Size";

    public void write(Element e, Object object) {
        Font f = (Font) object;

        Element textNode = createTextFieldNode(e, FACE, f.getName());
        e.appendChild(textNode);

        textNode = createTextFieldNode(e, STYLE, String.valueOf(f.getStyle()));
        e.appendChild(textNode);

        textNode = createTextFieldNode(e, SIZE, String.valueOf(f.getSize()));
        e.appendChild(textNode);
    }

    public Object read(Element e, Class type) {
        NodeList nodeList = getFieldNodeList(e);

        String face = "";
        int style = 0;
        int size = 12;

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element fieldNode = (Element) nodeList.item(i);
            String fieldName = fieldNode.getNodeName();

            if (fieldName.equals(FACE)) {
                face = getText(fieldNode);
            } else if (fieldName.equals(STYLE)) {
                style = intValue(fieldNode);
            } else if (fieldName.equals(SIZE)) {
                size = intValue(fieldNode);
            }
        }

        return new Font(face, style, size);
    }
}
