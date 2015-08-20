package jatools.engine.script;


import jatools.dom.NodeStack;
import bsh.NameSpace;
import bsh.SimpleNode;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class SimpleScript implements Script {
    /**
     * DOCUMENT ME!
     *
     * @param var DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object get(String var) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param var DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void set(String var, Object value) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param beforePrint DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object eval(String beforePrint) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ns DOCUMENT ME!
     */
    public void pushNameSpace(NameSpace ns) {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NameSpace popNameSpace() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NameSpace createNameSpace() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public KeyStack getKeyStack(int type) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NodeStack getNodeStack(int type) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int setStackType(int type) {
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getStackType() {
        return 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param template DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object evalTemplate(String template) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param expr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SimpleNode parse(String expr) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object eval(SimpleNode node) {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue2() {
        return null;
    }

    /**
     * DOCUMENT ME!
     */
    public void clearValue2() {
    }

	public void hideErrors(String errors) {
		// TODO Auto-generated method stub
		
	}
}
