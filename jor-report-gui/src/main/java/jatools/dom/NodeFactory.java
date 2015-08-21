package jatools.dom;

import jatools.dom.src.NodeSource;

import jatools.engine.script.Script;

import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class NodeFactory {
    /**
     * DOCUMENT ME!
     *
     * @param n DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ElementBase[] createChildNodes(Script script, ElementBase node) {
        ArrayList list = new ArrayList();

        NodeSource src = node.getSource();
        NodeSource[] sources = src.getChildSources();

        if ((sources != null) && (sources.length > 0)) {
            script.getNodeStack(0).push(node);

            for (int i = 0; i < sources.length; i++) {
                if (sources[i].isNodeListSource()) {
                    Node[] c = (Node[]) sources[i].createNodeList(node, node.getRoot().getScript());

                    if (c != null) {
                        list.addAll(Arrays.asList(c));
                    }
                } else {
                    ElementBase c = (ElementBase) sources[i].create(node, node.getRoot().getScript());

                    if (c != null) {
                        list.add(c);
                    }
                }

                sources[i].setLatestParentNode(node);
            }

            script.getNodeStack(0).pop();
        }

        return (ElementBase[]) list.toArray(new ElementBase[0]);
    }

    /**
     * DOCUMENT ME!
     *
     * @param script DOCUMENT ME!
     * @param node DOCUMENT ME!
     * @param childSrc DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static List createChildNodes(Script script, ElementBase node, NodeSource childSrc) {
        ArrayList list = new ArrayList();

        script.getNodeStack(0).push(node);

        if (childSrc.isNodeListSource()) {
            Node[] c = (Node[]) childSrc.createNodeList(node, node.getRoot().getScript());

            if (c != null) {
                list.addAll(Arrays.asList(c));
            }
        } else {
            ElementBase c = (ElementBase) childSrc.create(node, node.getRoot().getScript());

            if (c != null) {
                list.add(c);
            }
        }

        script.getNodeStack(0).pop();

        return list;
    }
}
