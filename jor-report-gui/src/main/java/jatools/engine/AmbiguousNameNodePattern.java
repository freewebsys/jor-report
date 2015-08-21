package jatools.engine;

import java.io.StringReader;

import bsh.BSHAmbiguousName;
import bsh.BSHPrimaryExpression;
import bsh.ParseException;
import bsh.Parser;
import bsh.SimpleNode;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.1 $
  */
public class AmbiguousNameNodePattern {
    /**
     * DOCUMENT ME!
     *
     * @param formula DOCUMENT ME!
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean matches(String src, String name) {
        if (src.indexOf(name) == -1) {
            return false;
        }

        StringReader reader = null;

        if (src.endsWith(";")) {
            reader = new StringReader(src);
        } else {
            reader = new StringReader(src + ";");
        }

        Parser parser = new Parser(reader);
        boolean eof = false;

        while (!eof) {
            try {
                eof = parser.Line();

                SimpleNode node = (SimpleNode) parser.getRootNode();

                if (!isSimpleName(node,name) && matches(node, name)) {
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    private static boolean isSimpleName(SimpleNode node, String name) {
        if (node instanceof BSHPrimaryExpression && (node.children != null) &&
                (node.children.length == 1) && node.children[0] instanceof BSHAmbiguousName) {
            BSHAmbiguousName nameNode = (BSHAmbiguousName) node.children[0];

            if (name.equals(nameNode.text)) {
                return true;
            }
        }

        return false;
    }

    private static boolean matches(SimpleNode node, String name) {
        if (node instanceof BSHAmbiguousName) {
            BSHAmbiguousName nameNode = (BSHAmbiguousName) node;

            if (name.equals(nameNode.text)) {
                return true;
            }
        }

        if (node.children != null) {
            for (int i = 0; i < node.children.length; i++) {
                if (matches((SimpleNode) node.children[i], name)) {
                    return true;
                }
            }
        }

        return false;
    }
}
