package jatools.engine.script;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.NameSpace;
import bsh.ParseException;
import bsh.SimpleNode;
import bsh.TargetError;

import jatools.ReportDocument;

import jatools.dataset.Dataset;

import jatools.designer.App;

import jatools.dom.NodeStack;

import jatools.engine.PrintConstants;
import jatools.engine.System2;

import jatools.util.Base64Util;
import jatools.util.Util;

import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import java.security.MessageDigest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.Stack;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ReportContext implements PrintConstants, Script {
    private static Logger logger = Logger.getLogger("ZReportContext");

    // static ReportContext defaultContext;

    //    static {
    //        try {
    //            defaultContext = new ReportContext(null);
    //        } catch (Exception e) {
    //            // TODO Auto-generated catch block
    //            e.printStackTrace();
    //        }
    //    }
    private static int THROW_ERR_MAX_COUNT = 30;
    private final static String[] HIDE_ALL_ERRORS = new String[0];
    private static String[] hideErrors;
    private Stack namesCache = new Stack();

    /**
     * DOCUMENT ME!
     */
    public Interpreter it = new Interpreter();
    private ReportDocument document;
    Stack printerCache = new Stack();
    private KeyStack keyStack = new KeyStack();
    private KeyStack keyStack1 = new KeyStack();
    private NodeStack nodeStack = new NodeStack(it.getNameSpace());
    private NodeStack nodeStack1 = new NodeStack(it.getNameSpace());
    private int stackType;
    private int errors;

    /**
     * Creates a new ReportContext object.
     *
     * @param document DOCUMENT ME!
     */
    public ReportContext(ReportDocument document) throws Exception {
        this.document = document;
        pushNameSpace(it.getNameSpace());
        Interpreter.LOCALSCOPING = false;

        importFunctions();

        try {
            it.set("$$jatoolsproperties", System2.properties);
        } catch (EvalError e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new ReportContext object.
     */
    public ReportContext() throws Exception {
        this(null);
    }

    /**
     * DOCUMENT ME!
     */
    public void showError() {
        this.hideErrors = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param errors DOCUMENT ME!
     */
    public void hideErrors(String errors) {
        this.hideErrors = (errors == null) ? HIDE_ALL_ERRORS : errors.split(";");
    }

    /**
     * DOCUMENT ME!
     */
    public void hideErrors() {
        this.hideErrors(null);
    }

    private void importFunctions() {
        it.getNameSpace().importStatic(GlobalScripts.class);

        String classes = System2.getProperty("class_for_import_functions");

        if (classes != null) {
            String[] clss = classes.split(";");

            for (int i = 0; i < clss.length; i++) {
                try {
                    it.getNameSpace().importStatic(Class.forName(clss[i]));
                } catch (ClassNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    /**
    * DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public static ReportContext getDefaultContext() {
        try {
            return new ReportContext(null);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param template DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object evalTemplate(String template) {
        String copy = template;

        Stack stack = new Stack();
        ArrayList macros = new ArrayList();

        for (int j = 0; j < template.length(); j++) {
            char ch = template.charAt(j);

            if ((ch == '$') && (j < (template.length() - 1)) && (template.charAt(j + 1) == '{')) {
                stack.push(new Integer(j));
                j++;
            } else if (ch == '{') {
                stack.push(null);
            } else if (ch == '}') {
                if (stack.size() > 0) {
                    if (stack.peek() == null) {
                        stack.pop();
                    } else {
                        Integer top = (Integer) stack.peek();

                        if (top.intValue() > -1) {
                            stack.pop();

                            //    if (stack.isEmpty()) {
                            macros.add(new int[] { top.intValue(), j });

                            //  }
                        } else {
                            stack.push(new Integer(-j));
                        }
                    }
                } else {
                    System.out.println(App.messages.getString("res.56"));

                    return copy;
                }
            }
        }

        if (!stack.isEmpty()) {
            System.out.println(App.messages.getString("res.56"));

            return copy;
        } else {
            StringBuffer b = new StringBuffer();
            int headfrom = 0;
            Iterator it1 = macros.iterator();

            while (it1.hasNext()) {
                int[] m = (int[]) it1.next();
                b.append(template.substring(headfrom, m[0]));

                String macro = template.substring(m[0] + 2, m[1]);
                Object obj = eval(macro);
                b.append((obj != null) ? obj.toString() : "");

                headfrom = m[1] + 1;
            }

            b.append(template.substring(headfrom));

            return b.toString();
        }
    }

    private static byte[] loadUser() {
        try {
            InputStream is = Util.class.getResourceAsStream("/jatools/icons/new.jpg");
            byte[] _user = new byte[is.available()];
            is.read(_user);

            InputStream kis = Util.class.getResourceAsStream("/jatools/icons/new.png");
            byte[] _key = new byte[kis.available()];
            kis.read(_key);

            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            digest.update(_user);

            byte[] di = digest.digest();

            if (MessageDigest.isEqual(di, _key)) {
                return Base64Util.decode(new String(_user, "UTF-8"));
            } else {
                throw new Exception(App.messages.getString("res.57"));
            }
        } catch (Exception e) {
            e.printStackTrace();
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
    public Object eval(String expr) {
        try {
            //	it.DEBUG = true;
            return it.eval(expr);
        } catch (EvalError e) {
            errors++;

            if ((errors < THROW_ERR_MAX_COUNT) && (hideErrors != HIDE_ALL_ERRORS)) {
                try {
                    boolean match = false;

                    if (this.hideErrors != null) {
                        for (int i = 0; !match && (i < this.hideErrors.length); i++) {
                            String error = this.hideErrors[i];
                            Throwable t = e;

                            while (t != null) {
                                if (t.getClass().getName().indexOf(error) > -1) {
                                    match = true;

                                    break;
                                } else if (t instanceof TargetError &&
                                        (((TargetError) t).getTarget() != null) &&
                                        (((TargetError) t).getTarget().getClass().getName()
                                              .indexOf(error) > -1)) {
                                    match = true;

                                    break;
                                }

                                t = t.getCause();
                            }
                        }
                    }

                    if (!match) {
                        Util.debug(logger,
                            App.messages.getString("res.58") + it.get("file") + "]:\n" +
                            Util.toString(e));
                    }
                } catch (EvalError e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }

        return null;
    }

    private Object[] filtered(Dataset rows, int from, int to, String calcField, String filter)
        throws EvalError {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param thisOne DOCUMENT ME!
     * @param thatOne DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean equals(Object thisOne, Object thatOne) {
        return (thisOne == null) ? (thatOne == null) : thisOne.equals(thatOne);
    }

    /**
     * DOCUMENT ME!
     *
     * @param ns DOCUMENT ME!
     */
    public void pushNameSpace(NameSpace ns) {
        if (namesCache.size() > 0) {
            NameSpace parent = (NameSpace) namesCache.lastElement();
            ns.setParent(parent);
        }

        it.setNameSpace(ns);
        namesCache.push(ns);
    }

    /**
     * DOCUMENT ME!
     */
    public NameSpace popNameSpace() {
        Object result = namesCache.pop();

        if (namesCache.size() > 0) {
            NameSpace ns = (NameSpace) namesCache.lastElement();
            it.setNameSpace(ns);
        }

        return (NameSpace) result;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NameSpace createNameSpace() {
        return new NameSpace(it.getNameSpace(), "dummy");
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object get(String name) {
        try {
            if (name != null) {
                if (name.startsWith("=")) {
                    return eval(name.substring(1));
                } else if (name.startsWith("&")) {
                    return it.get(name.substring(1));
                } else {
                    return it.get(name);
                }
            }
        } catch (EvalError ex) {
            ex.printStackTrace();
            Util.debug(logger, App.messages.getString("res.59") + name);
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param name DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void set(String name, Object value) {
        try {
            it.set(name, value);
        } catch (EvalError ex) {
            ex.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param it DOCUMENT ME!
     * @param cols DOCUMENT ME!
     */
    public static void DEBUG_VARS(Interpreter it, int cols) {
        NameSpace names = it.getNameSpace();

        ArrayList vars = new ArrayList();

        while (names != null) {
            String[] _vars = names.getVariableNames();
            vars.addAll(Arrays.asList(_vars));
            names = names.getParent();
        }

        Collections.sort(vars);

        System.out.println(App.messages.getString("res.60"));

        for (int i = 0; i < vars.size(); i++) {
            String name = (String) vars.get(i);

            if (cols > 0) {
                System.out.print(name);

                try {
                    if (cols > 1) {
                        Object value = it.get(name);
                        System.out.print(" = " + value);

                        if ((cols > 2) && (value != null)) {
                            System.out.print("    (" + value.getClass().getName() + ")");
                        }
                    }
                } catch (EvalError ex) {
                    ex.printStackTrace();
                }

                System.out.println();
            }
        }

        System.out.println("..............");
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ReportDocument getDocument() {
        return document;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object clone() {
        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Interpreter getIt() {
        return it;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public KeyStack getKeyStack(int type) {
        return (type == KeyStack.ROW) ? keyStack : keyStack1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public NodeStack getNodeStack(int type) {
        return (type == KeyStack.ROW) ? nodeStack : nodeStack1;
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int setStackType(int type) {
        int tmp = this.stackType;
        this.stackType = type;

        return tmp;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getStackType() {
        return stackType;
    }

    /**
     * DOCUMENT ME!
     *
     * @param expr DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SimpleNode parse(String expr) {
        return it.parse(expr);
    }

    /**
     * DOCUMENT ME!
     *
     * @param node DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object eval(SimpleNode node) {
        try {
            return it.eval(node);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (EvalError e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
        System.out.println(new Date().getTime());

        try {
            Properties p = new Properties();
            p.load(new FileInputStream("d:/jatools.properties"));
            System.out.println(p.get("wgcdburl"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue2() {
        return this.it.getValue2();
    }

    /**
     * DOCUMENT ME!
     */
    public void clearValue2() {
        this.it.setValue2(null);
    }
}
