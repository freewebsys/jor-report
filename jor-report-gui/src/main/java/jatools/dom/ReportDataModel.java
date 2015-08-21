package jatools.dom;

import jatools.accessor.ProtectPublic;
import jatools.dom.src.NodeSource;
import jatools.dom.src.RootNodeSource;
import jatools.dom.src.xpath.XPath;
import jatools.engine.script.AxisNode;
import jatools.engine.script.Script;

import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Node;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ReportDataModel implements ProtectPublic {
    JatoolsDocument jdoc;

    /**
     * Creates a new ReportDataModel object.
     *
     * @param rootSrc DOCUMENT ME!
     * @param script DOCUMENT ME!
     */
    public ReportDataModel(RootNodeSource rootSrc, Script script) {
        this.jdoc = new JatoolsDocument(rootSrc, script);

        this.setScript(script);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCached() {
        return ((RootNode) this.jdoc.getFirstChild()).isCached();
    }

    /**
     * DOCUMENT ME!
     *
     * @param path DOCUMENT ME!
     * @param tag DOCUMENT ME!
     * @param command DOCUMENT ME!
     */
    public void sort(String path, String tag, String command) {
        List nodes = XPath.getDefaults().selectNodes(path, this.jdoc.getScript());

        if (nodes != null) {
            Iterator it = nodes.iterator();

            while (it.hasNext()) {
                ElementBase e = (ElementBase) it.next();
                e.sort(tag, command);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param script DOCUMENT ME!
     */
    public void setScript(Script script) {
        jdoc.setScript(script);

        Node root = jdoc.getFirstChild();
        script.set("$$root", root);
        script.getNodeStack(0).push(root);
        script.getNodeStack(1).push(root);
        script.set("$", new AxisNode(script.getNodeStack(0)));
        script.set("$$", new AxisNode(script.getNodeStack(1)));

        registerNodeVariable(((ElementBase) root).getSource(), root, script);
    }

    private void registerNodeVariable(NodeSource src, Node root, Script script) {
        script.set(src.getTagName(), new NodeProxy(src, root));

        Iterator it = src.getChildren().iterator();

        while (it.hasNext()) {
            NodeSource child = (NodeSource) it.next();
            child.setParent(src);
            registerNodeVariable(child, root, script);
        }
    }
}
