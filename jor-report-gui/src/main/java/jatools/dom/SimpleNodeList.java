package jatools.dom;

import jatools.accessor.ProtectPublic;
import jatools.dataset.RowsService;
import jatools.engine.script.Script;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import bsh.NameSpace;
import bsh.SimpleNode;
import bsh.UtilEvalError;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class SimpleNodeList extends ArrayList implements NodeSortable, ProtectPublic {
    final static String prop = "COMP_VALUE";
    final static Object NULL = new Object();

    /**
     * Creates a new SimpleNodeList object.
     *
     * @param src DOCUMENT ME!
     */
    public SimpleNodeList(List src) {
        super(src);
    }

    /**
     * DOCUMENT ME!
     *
     * @param tag DOCUMENT ME!
     * @param command DOCUMENT ME!
     */
    public void sort(String tag, String command) {
        Iterator it = this.iterator();

        while (it.hasNext()) {
            NodeSortable sortable = (NodeSortable) it.next();
            sortable.sort(tag, command);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param expr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SimpleNodeList sort(String expr) {
        return sort(expr, true);
    }

    /**
     * DOCUMENT ME!
     *
     * @param expr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SimpleNodeList sort_desc(String expr) {
        return sort(expr, false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param expr DOCUMENT ME!
     * @param asc DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private SimpleNodeList sort(String expr, boolean asc) {
        if (!this.isEmpty() && (expr != null)) {
            ElementBase first = (ElementBase) this.get(0);
            Script script = first.getRoot().getScript();
            NameSpace ns = script.createNameSpace();

            if (!expr.startsWith("=")) {
                expr = "$." + expr;
            } else {
                expr = expr.substring(1);
            }

            SimpleNode node = first.getRoot().getScript().parse(expr);
            script.pushNameSpace(ns);
            Collections.sort(this, new ScriptComparator(script, ns, node, asc));
            script.popNameSpace();
        }

        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @param expr DOCUMENT ME!
     * @param options DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private RankList rank(String expr, String options) {
        if (!this.isEmpty() && (expr != null)) {
            ElementBase first = (ElementBase) this.get(0);
            Script script = first.getRoot().getScript();
            NameSpace ns = script.createNameSpace();

            if (!expr.startsWith("=")) {
                expr = "$." + expr;
            } else {
                expr = expr.substring(1);
            }

            SimpleNode node = first.getRoot().getScript().parse(expr);
            script.pushNameSpace(ns);

            Object[] vals = new Object[this.size()];

            for (int i = 0; i < this.size(); i++) {
                try {
                    ns.setLocalVariable("$", this.get(i));
                } catch (UtilEvalError e1) {
                    e1.printStackTrace();
                }

                vals[i] = script.eval(node);
            }

            script.popNameSpace();

            return new RankList(vals, "asc".equals(options));
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param expr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RankList rank(String expr) {
        return rank(expr, "asc");
    }

    /**
     * DOCUMENT ME!
     *
     * @param expr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public RankList rank_desc(String expr) {
        return rank(expr, "desc");
    }

    class ScriptComparator implements Comparator {
        private Script script;
        private NameSpace names;
        private SimpleNode node;
        private boolean asc;

        ScriptComparator(Script script, NameSpace ns, SimpleNode node, boolean asc) {
            this.script = script;
            this.names = ns;
            this.node = node;
            this.asc = asc;
        }

        public int compare(Object o1, Object o2) {
            ElementBase e1 = (ElementBase) o1;
            ElementBase e2 = (ElementBase) o2;

            int result = RowsService.compareComparables((Comparable) getValue(e1),
                    (Comparable) getValue(e2));

            return asc ? result : (-result);
        }

        private Object getValue(ElementBase e) {
            Object result = e.getClientProperty(prop);

            if (result != null) {
                return (result == NULL) ? null : result;
            } else {
                try {
                    this.names.setLocalVariable("$", e);
                } catch (UtilEvalError e1) {
                    e1.printStackTrace();
                }

                result = this.script.eval(node);
                e.setClientProperty(prop, (result == null) ? NULL : result);

                return result;
            }
        }
    }
}
