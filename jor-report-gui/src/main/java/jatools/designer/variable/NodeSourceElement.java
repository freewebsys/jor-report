package jatools.designer.variable;

import jatools.dom.src.NodeSource;

import java.awt.Rectangle;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class NodeSourceElement {
    String field;
    NodeSource src;
    Rectangle rect;
    int row;

    /**
         * @param src
         * @param field
         * @param row
         */
    public NodeSourceElement(NodeSource src, String field, int row) {
        this.src = src;
        this.field = field;
        this.row = row;
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public String toString() {
        return String.format("[%d]%s@%s", row, src.getTagName(), field);
    }
}
