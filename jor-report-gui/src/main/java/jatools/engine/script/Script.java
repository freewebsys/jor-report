package jatools.engine.script;

import bsh.NameSpace;
import bsh.SimpleNode;

import jatools.dom.NodeStack;


/**
 *
 *  在组件打印时，会要求容器解析相应变量值
 *
 *  参见 ZComponent.print()
 *
 *  @version $Revision: 1.1 $
 *  @author $author$
 *
 */
public interface Script {
    /**
     * DOCUMENT ME!
     *
     * @param var DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object get(String var);

    /**
     * DOCUMENT ME!
     *
     * @param errors DOCUMENT ME!
     */
    public void hideErrors(String errors);

    /**
     * DOCUMENT ME!
     *
     * @param var DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void set(String var, Object value);

    /**
     * DOCUMENT ME!
     *
     * @param beforePrint DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object eval(String expre);

    /**
     * DOCUMENT ME!
     *
     * @param template DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object evalTemplate(String template);

    /**
     * DOCUMENT ME!
     *
     * @param ns
     *            DOCUMENT ME!
     */
    public void pushNameSpace(NameSpace ns);

    /**
     * DOCUMENT ME!
     *
     * @param ns
     *            DOCUMENT ME!
     */
    public NameSpace popNameSpace();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NameSpace createNameSpace();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public KeyStack getKeyStack(int type);

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NodeStack getNodeStack(int type);

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     */
    public int setStackType(int type);

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     */
    public int getStackType();

    /**
     * DOCUMENT ME!
     *
     * @param expr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SimpleNode parse(String expr);

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object eval(SimpleNode node);

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue2();

    /**
     * DOCUMENT ME!
     */
    public void clearValue2();
}
