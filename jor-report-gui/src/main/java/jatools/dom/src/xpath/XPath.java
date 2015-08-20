package jatools.dom.src.xpath;

import bsh.CallStack;
import bsh.UtilEvalError;
import bsh.XPathProcessor;

import jatools.dom.ElementBase;
import jatools.dom.NodeStack;
import jatools.dom.SimpleNodeList;

import jatools.engine.script.Script;

import org.jaxen.JaxenException;

import org.jaxen.dom.DOMXPath;

import org.w3c.dom.Node;

import java.util.List;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.7 $
  */
public class XPath implements XPathProcessor {
    static private XPath defaults;

    /**
     * DOCUMENT ME!
     *
     * @param path DOCUMENT ME!
     * @param callstack DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object selectNode(String path, CallStack callstack) {
        try {
            NodeStack nodestack = (NodeStack) callstack.top().getVariable("$$nodestack");

            if (path.startsWith("@")) {
                return new DOMXPath(path.substring(1)).selectNodes(nodestack.getNode()).toArray();
            }

            if (path.startsWith("#")) {
                List list = new DOMXPath(path.substring(1)).selectNodes(nodestack.getNode());

                if (list.isEmpty()) {
                    return null;
                }

                Object node = list.get(0);

                if (node instanceof ElementBase) {
                    return ((ElementBase) node).getSource();
                } else {
                    return null;
                }
            }

            return selectNode(path, nodestack.getNode());
        } catch (JaxenException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UtilEvalError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param path DOCUMENT ME!
     * @param context DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws JaxenException DOCUMENT ME!
     */
    public Object selectNode(String path, Node context)
        throws JaxenException {
        List list = new DOMXPath(path).selectNodes(context);

        if (list.isEmpty()) {
            return null;
        } else if (list.size() == 1) {
            return list.get(0);
        } else {
            return new SimpleNodeList(list);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param path DOCUMENT ME!
     * @param context DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public List selectNodes(String path, Script script) {
        return selectNodes(path, script.getNodeStack(0).getNode());
    }

    /**
     * DOCUMENT ME!
     *
     * @param path DOCUMENT ME!
     * @param script DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public List selectNodes2(String path, Script script) {
        return selectNodes(path, script.getNodeStack(1).getNode());
    }

    /**
     * DOCUMENT ME!
     *
     * @param path DOCUMENT ME!
     * @param node DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public List selectNodes(String path, Node node) {
        try {
            //            if (path.startsWith("/") && !path.startsWith("//")) {
            //                DOMXPath xpath = new DOMXPath(path);
            //
            //                return xpath.selectNodes(nodestack.getRoot());
            //            } else {
            return new DOMXPath(path).selectNodes(node);

            //            }
        } catch (JaxenException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static XPath getDefaults() {
        if (defaults == null) {
            defaults = new XPath();
        }

        return defaults;
    }
}
