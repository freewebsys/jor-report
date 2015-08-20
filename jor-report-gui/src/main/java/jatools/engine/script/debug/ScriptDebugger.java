package jatools.engine.script.debug;

import jatools.designer.App;
import jatools.designer.Main;
import jatools.engine.InterpreterAware;
import jatools.engine.System2;
import jatools.engine.ValueIfClosed;
import jatools.engine.script.DebugOff;
import jatools.swingx.Icon25x25Button;
import jatools.swingx.scripteditor.ScriptEditor;
import jatools.util.Util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultTreeCellRenderer;

import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.decorator.AbstractHighlighter;
import org.jdesktop.swingx.decorator.ComponentAdapter;
import org.jdesktop.swingx.treetable.DefaultMutableTreeTableNode;
import org.jdesktop.swingx.treetable.DefaultTreeTableModel;

import bsh.CallStack;
import bsh.EvalError;
import bsh.Interpreter;
import bsh.PropertyGetter;
import bsh.UtilEvalError;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class ScriptDebugger extends JDialog {
    private final static String STEPOUT = "退出代码";
    private final static String STOP = "停止调试";
    private final static String WATCH = "即时查看值,请先在当前脚本中选择要查看的表达式,\n或在此输入表达式";
    private final static Object[] PROPERTY_GET_PARAMS = new Object[0];
    private JXTreeTable treeTable = null;
    private final Interpreter it;
    private ScriptEditor editor;
    private JTextField watchText;
    private JLabel watchResult;

    /**
     * Creates a new ScriptDebugger object.
     *
     * @param it
     *            DOCUMENT ME!
     */
    public ScriptDebugger(final Interpreter it) {
        super(Main.getInstance(), App.messages.getString("res.61"), true);

        this.it = it;
        this.it.getRoot().setDebugOff(true);

        ArrayList vars = new ArrayList();
        it.getNameSpace().getVariableNames(vars);

        Map root = new HashMap();
        Collections.sort(vars);

        Iterator i = vars.iterator();

        while (i.hasNext()) {
            String name = (String) i.next();

            final String filters = ",bsh,ALL,$styleClass,$printer,";

            if ((filters.indexOf("," + name + ",") > -1) ||
                    (name.startsWith("$$") && !name.equals("$$"))) {
                continue;
            }

            try {
                root.put(name, it.get(name));
            } catch (EvalError e) {
                e.printStackTrace();
            }
        }

        treeTable = new JXTreeTable(new ScriptDebuggerModel(generateTreeTableModel(root), it));
        treeTable.setTreeCellRenderer(new DefaultTreeCellRenderer() {
                public java.awt.Component getTreeCellRendererComponent(javax.swing.JTree tree,
                    Object value, boolean sel, boolean expanded, boolean leaf, int row,
                    boolean hasFocus) {
                    super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row,
                        hasFocus);
                    setIcon(null);

                    return this;
                }
                ;
            });

        treeTable.addTreeExpansionListener(new TreeExpansionListener() {
                public void treeCollapsed(TreeExpansionEvent event) {
                }

                public void treeExpanded(TreeExpansionEvent event) {
                    DefaultMutableTreeTableNode n = (DefaultMutableTreeTableNode) event.getPath()
                                                                                       .getLastPathComponent();

                    if (n instanceof VariableNode) {
                        VariableNode mynode = (VariableNode) n;
                        DefaultMutableTreeTableNode node = null;

                        DefaultTreeTableModel mode = (DefaultTreeTableModel) treeTable.getTreeTableModel();

                        if (!mynode.explored) {
                            ScriptDebuggerBean sb = (ScriptDebuggerBean) mynode.getUserObject();
                            List nodes = new ArrayList();

                            if ((sb.getVal() != null) && sb.getVal().getClass().isArray()) {
                                Object[] values = (Object[]) sb.getVal();

                                if (values != null) {
                                    for (int j = 0; j < values.length; j++) {
                                        nodes.add(asNode(String.format("[%d]", j), values[j]));
                                    }
                                }
                            } else {
                                Class cls = sb.getVal().getClass();

                                getFieldNodes(sb.getVal(), cls, nodes);

                                Object val = sb.getVal();

                                Collections.sort(nodes, new NodeComparator());

                                if (val instanceof PropertyGetter) {
                                    String[] names = ((PropertyGetter) val).getPropertyNames();

                                    if (names != null) {
                                        for (int j = names.length - 1; j >= 0; j--) {
                                            String name = names[j];

                                            try {
                                                Object v = ((PropertyGetter) val).getProperty(name,
                                                        new CallStack(it.getNameSpace()), it);

                                                nodes.add(0, asNode(name, v));
                                            } catch (UtilEvalError e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }

                                if (val instanceof ValueIfClosed) {
                                    ValueIfClosed vic = (ValueIfClosed) val;

                                    if (vic instanceof InterpreterAware) {
                                        ((InterpreterAware) vic).setInterpreter(it);
                                    }

                                    Object v = vic.value();

                                    if ((v != null) && v.getClass().isArray()) {
                                        Object[] values = (Object[]) v;

                                        for (int j = values.length - 1; j >= 0; j--) {
                                            nodes.add(0, asNode(String.format("[%d]", j), values[j]));
                                        }
                                    }
                                }
                            }

                            for (int i = 0; i < nodes.size(); i++) {
                                node = (DefaultMutableTreeTableNode) nodes.get(i);
                                mode.insertNodeInto(node, n, n.getChildCount());
                                mynode.explored = true;
                            }
                        }
                    }
                }
            });

        JToolBar toolsbar = new JToolBar();
        toolsbar.add(new Icon25x25Button(
                new DebugAction(STOP, Util.getIcon("/jatools/icons/stop2.gif"), STOP)));
        toolsbar.add(new Icon25x25Button(
                new DebugAction(STEPOUT, Util.getIcon("/jatools/icons/stepout.gif"), STEPOUT)));
        toolsbar.add(Box.createHorizontalStrut(20));
        toolsbar.add(new Icon25x25Button(
                new DebugAction(WATCH, Util.getIcon("/jatools/icons/watch.gif"), WATCH)));

        watchText = new JTextField();
        watchText.addKeyListener(new KeyListener() {
                public void keyTyped(KeyEvent e) {
                    // TODO Auto-generated method stub
                }

                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == '\n') {
                        watch(watchText.getText());
                    }
                }

                public void keyReleased(KeyEvent e) {
                }
            });

        watchText.setPreferredSize(new Dimension(300, 0));
        watchText.setMaximumSize(new Dimension(300, 25));

        toolsbar.add(watchText);

        toolsbar.add(new JLabel("="));
        watchResult = new JLabel();

        // watchResult.setPreferredSize(new Dimension(300, 0));
        // watchResult.setMaximumSize(new Dimension(300, 25));
        toolsbar.add(watchResult);

        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        gbc.weighty = 1.0;
        p.add(new JScrollPane(treeTable), gbc);

        gbc.weighty = 0.0;
        p.add(Box.createVerticalStrut(30), gbc);
        p.add(new JLabel("当前脚本:"), gbc);
        gbc.weighty = 0.5;

        this.editor = new ScriptEditor();
        this.editor.setPreferredSize(new Dimension(2, 200));
        p.add(new JScrollPane(editor), gbc);

        this.getContentPane().add(p);
        this.getContentPane().add(toolsbar, BorderLayout.SOUTH);
        this.setSize(new Dimension(800, 600));

        this.setLocationRelativeTo(null);
        this.editor.setText(it.getSourceFileInfo());

        this.setVisible(true);
    }

    protected void watch(String text) {
        String result = null;

        try {
            Object val = it.eval(text);

            if (val == null) {
                result = "null";
            } else if (val instanceof String) {
                result = "\"" + val + "\"";
            } else {
                result = val.toString();
            }
        } catch (EvalError e) {
            setError("出错:" + e.getMessage());

            return;
        }

        setResult(result);
    }

    private void setResult(String result) {
        this.watchResult.setText(result);
        this.watchResult.setForeground(Color.black);
    }

    /**
     * DOCUMENT ME!
     *
     * @param it
     *            DOCUMENT ME!
     */
    public static void debug(Interpreter it) {
        if (System2.isDesignTime() && !it.getRoot().isDebugOff()) {
            new ScriptDebugger(it);
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void watch() {
        String text = this.editor.getSelectedText();

        if ((text == null) || (text.trim().length() == 0)) {
            text = watchText.getText();

            if ((text == null) || (text.trim().length() == 0)) {
                setError("请先在当前脚本中选择要查看的表达式,或在文本框中输入表达式.");
            }
        } else {
            watchText.setText(text);
            watch(text);
            watchText.requestFocus();
        }
    }

    private void setError(String err) {
        this.watchResult.setText(err);
        this.watchResult.setForeground(Color.red);
    }

    /**
     * DOCUMENT ME!
     */
    public void stepOut() {
        this.it.getRoot().setDebugOff(false);
        this.setVisible(false);
    }

    /**
     * DOCUMENT ME!
     */
    public void stop() {
        this.setVisible(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param val
     *            DOCUMENT ME!
     * @param cls
     *            DOCUMENT ME!
     * @param list
     *            DOCUMENT ME!
     */
    public void getFieldNodes2(Object val, Class cls, List list) {
        // DefaultMutableTreeTableNode node = null;
        // Object value = null;
        // Field[] f = cls.getDeclaredFields();
        //
        // for (int i = 0; i < f.length; i++) {
        // f[i].setAccessible(true);
        //
        // try {
        // value = f[i].get(val);
        // } catch (IllegalArgumentException e) {
        // e.printStackTrace();
        // } catch (IllegalAccessException e) {
        // e.printStackTrace();
        // }
        //
        // ScriptDebuggerBean bean = new ScriptDebuggerBean(f[i].getName(),
        // value,);
        //
        // if ((value == null) || value instanceof String
        // || value instanceof Integer || value instanceof Boolean) {
        // node = new DefaultMutableTreeTableNode(bean);
        // } else {
        // node = new VariableNode(bean);
        // }
        //
        // list.add(node);
        // f[i].setAccessible(false);
        // }
        //
        // cls = cls.getSuperclass();
        //
        // if (cls != null) {
        // getFieldNodes(val, cls, list);
        // }
    }

    /**
     * DOCUMENT ME!
     *
     * @param val DOCUMENT ME!
     * @param cls DOCUMENT ME!
     * @param list DOCUMENT ME!
     */
    public void getFieldNodes(Object val, Class cls, List list) {
        try {
            BeanInfo info = Introspector.getBeanInfo(cls);

            for (PropertyDescriptor descriptor : info.getPropertyDescriptors()) {
                String name = descriptor.getName();

                if ("class".equals(name)) {
                    continue;
                }

                Method method = descriptor.getReadMethod();

                if (method != null) {
                    if (isAnnotationPresent(method, DebugOff.class)) {
                        continue;
                    }

                    DefaultMutableTreeTableNode node = null;
                    Object value = null;

                    try {
                        value = method.invoke(val);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    node = this.asNode(name, value);
                    list.add(node);
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private DefaultMutableTreeTableNode asNode(String name, Object val) {
        DefaultMutableTreeTableNode node = null;

        ScriptDebuggerBean bean = ScriptDebuggerBean.create(name, val, it);

        if ((val == null) || val instanceof String || val instanceof Number ||
                val instanceof Boolean) {
            node = new DefaultMutableTreeTableNode(bean);
        } else {
            node = new VariableNode(bean);
        }

        return node;
    }

    private boolean isAnnotationPresent(Method m, Class an) {
        if (m.isAnnotationPresent(an)) {
            return true;
        }

        Class<?> parent = m.getDeclaringClass().getSuperclass();

        while (parent != null) {
            Method superMethod = getConformingMethod(m.getName(), parent);

            if ((superMethod != null) && isAnnotationPresent(superMethod, an)) {
                return true;
            }

            parent = parent.getSuperclass();
        }

        return false;
    }

    protected Method getConformingMethod(String methodName, Class<?> cls) {
        Method[] publicMethods = cls.getMethods();
        Method m = null;
        int idxMethod = 0;

        while ((m == null) && (idxMethod < publicMethods.length)) {
            m = publicMethods[idxMethod];

            if (m.getName().equals(methodName)) {
                Class<?>[] formalParameters = m.getParameterTypes();

                if (formalParameters.length > 0) {
                    m = null;
                }
            } else {
                m = null;
            }

            idxMethod++;
        }

        return m;
    }

    private DefaultMutableTreeTableNode generateTreeTableModel(Map c) {
        DefaultMutableTreeTableNode root = new DefaultMutableTreeTableNode(new ScriptDebuggerBean(
                    "root", "root", null, null));
        DefaultMutableTreeTableNode node = null;
        Object[] names = c.keySet().toArray();
        Arrays.sort(names);

        for (int i = 0; i < names.length; i++) {
            String name = (String) names[i];
            Object val = null;

            try {
                val = it.eval(name);
            } catch (EvalError e) {
            }

            ScriptDebuggerBean bean = ScriptDebuggerBean.create(name, val, it);

            if ((val == null) || val instanceof String || val instanceof Number ||
                    val instanceof Boolean) {
                node = new DefaultMutableTreeTableNode(bean);
            } else {
                node = new VariableNode(bean);
            }

            root.add(node);
        }

        return root;
    }

    class DebugAction extends AbstractAction {
        DebugAction(String name, Icon icon, String tooltip) {
            super(name, icon);
            putValue(SHORT_DESCRIPTION, tooltip);
            putValue(ACTION_COMMAND_KEY, name);
        }

        DebugAction(String name, Icon icon) {
            this(name, icon, null);
        }

        public void actionPerformed(ActionEvent e) {
            if (STOP == e.getActionCommand()) {
                stop();
            } else if (STEPOUT == e.getActionCommand()) {
                stepOut();
            } else if (WATCH == e.getActionCommand()) {
                watch();
            }
        }
    }

    class NodeComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            DefaultMutableTreeTableNode n1 = (DefaultMutableTreeTableNode) o1;
            DefaultMutableTreeTableNode n2 = (DefaultMutableTreeTableNode) o2;
            ScriptDebuggerBean b1 = (ScriptDebuggerBean) n1.getUserObject();
            ScriptDebuggerBean b2 = (ScriptDebuggerBean) n2.getUserObject();

            return b1.getName().compareTo(b2.getName());
        }
    }

    class MyHighlighter extends AbstractHighlighter {
        protected Component doHighlight(Component component, ComponentAdapter adapter) {
            System.err.println("Calling doHighlight with component" +
                component.getClass().getName() + " adapter " + adapter.getClass().getName());

            return null;
        }
    }

    class VariableNode extends DefaultMutableTreeTableNode {
        boolean explored;

        public VariableNode(Object node) {
            super(node);
        }

        public boolean isLeaf() {
            return false;
        }
    }
}
