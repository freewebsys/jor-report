package jatools.designer.data;

import jatools.data.Formula;
import jatools.designer.App;
import jatools.designer.Main;
import jatools.designer.VariableTreeModel;
import jatools.engine.AmbiguousNameNodePattern;
import jatools.engine.PrintConstants;
import jatools.swingx.CommandPanel;
import jatools.swingx.MessageBox;
import jatools.swingx.SimpleTreeNode;
import jatools.swingx.scripteditor.ScriptEditor;
import jatools.util.Util;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;




/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class CustomFormulaDialog extends JDialog implements ActionListener {
    static final String[] FUNCTIONS = new String[] {
            App.messages.getString("res.454"), "double abs(double)", "double asin(double)", "double acos(double)",
            "double atan(double)", "double atan2(double,double)", "double ceil(double)",
            "double cos(double)", "double exp(double)", "double floor(double)",
            
            "double log(double)", "double max(double,double)", "double min(double,double)",
            "double pow(double,double)", "double random()", "double rint(double)",
            "double sin(double)", "double sqrt(double)", "double tan(double)",
            "double toDegrees(double)", "double toRadians(double)", "float abs(float)",
            "float max(float,float)", "float min(float,float)", "int abs(int)", "int max(int,int)",
            "int min(int,int)", "int round(float)", "long abs(long)", "long max(long,long)",
            "long min(long,long)", "long round(double)",
            
            App.messages.getString("res.455"), "int length(String)", "char charAt(String, int)",
            
            "boolean startsWith(String, String)", "boolean endsWith(String, String)",
            
            "int indexOf(String, String)",
            
            "int lastIndexOf(String, String)",
            
            "String substring(String, int)", "String substring(String, int,int)",
            
            "String replaceAll(String, String , String )",
            
            "String[] split(String, String)",
            
            "String toLowerCase(String)",
            
            "String toUpperCase(String )",
            
            App.messages.getString("res.456"),
            
            "int getYear(Date )", "int getMonth(Date )", "int getDate(Date )", "int getDay(Date )",
            "int getHours(Date )", "int getMinutes(Date )", "int getSeconds(Date )",
            "boolean before(Date,Date )", "boolean after(Date,Date )",
            
            App.messages.getString("res.457"),
            
            "String toRmbString(double)", "String toHZYear(int)", "String toHZMonth(int)",
            "String toHZDay(int)",
            
            "String format(float,String)", "String format(double,String)",
            "String format(Date,String)", App.messages.getString("res.458"), "String p(String )",
            "String clobString(String )"
        };
    static final HashMap tips = new HashMap(FUNCTIONS.length);
    IconTree functionTree;
    VariableTree varTree;
    JButton check;
    JButton ok;
    JButton cancel;
    ScriptEditor textArea;
    private Formula formula;
    protected boolean nullPermitted;
    JLabel tipLabel;
   

    /**
     * Creates a new CustomFormulaDialog object.
     *
     * @param owner DOCUMENT ME!
     * @param astemp DOCUMENT ME!
     */
    public CustomFormulaDialog(Frame owner, boolean astemp) {
        this(owner, true, false, astemp);
    }

    /**
     * Creates a new CustomFormulaDialog object.
     *
     * @param owner DOCUMENT ME!
     * @param showVariable DOCUMENT ME!
     * @param nullPermitted DOCUMENT ME!
     * @param astemp DOCUMENT ME!
     */
    public CustomFormulaDialog(Frame owner, boolean showVariable, boolean nullPermitted,
        boolean astemp) {
        super(owner, App.messages.getString("res.459"), true);

        varTree = new VariableTree();

        TreeModel variableModel = null;

        if ((Main.getInstance() != null) && (Main.getInstance().getActiveEditor() != null)) {
            variableModel = Main.getInstance().getActiveEditor().getVariableModel();
        } else {
            variableModel = VariableTreeModel.getDefaults();
        }

        varTree.setModel(variableModel);

        varTree.setShowPopupMenu(false);

        varTree.setDoubleClickAction(this);

        functionTree = createFunctionTree();

        JComponent first = null;

        tips.put("double abs(double)", App.messages.getString("res.460"));
        tips.put("double asin(double)", App.messages.getString("res.461"));
        tips.put("double acos(double)", App.messages.getString("res.462"));
        tips.put("double atan(double)", App.messages.getString("res.463"));
        tips.put("double atan2(double,double)", App.messages.getString("res.462"));
        tips.put("double ceil(double)", App.messages.getString("res.464"));
        tips.put("double cos(double)", App.messages.getString("res.465"));
        tips.put("double exp(double)", App.messages.getString("res.466"));
        tips.put("double floor(double)", App.messages.getString("res.467"));
        tips.put("double log(double)", App.messages.getString("res.468"));
        tips.put("double max(double,double)", App.messages.getString("res.469"));
        tips.put("double min(double,double)", App.messages.getString("res.470"));
        tips.put("double pow(double,double)", App.messages.getString("res.471"));
        tips.put("double random()", App.messages.getString("res.472"));
        tips.put("double rint(double)", App.messages.getString("res.473"));
        tips.put("double sin(double)", App.messages.getString("res.474"));
        tips.put("double sqrt(double)", App.messages.getString("res.475"));
        tips.put("double tan(double)", App.messages.getString("res.476"));
        tips.put("double toDegrees(double)", App.messages.getString("res.468"));
        tips.put("double toRadians(double)", App.messages.getString("res.477"));
        tips.put("float abs(float)", App.messages.getString("res.478"));
        tips.put("float max(float,float)", App.messages.getString("res.479"));
        tips.put("float min(float,float)", App.messages.getString("res.480"));
        tips.put("int abs(int)", App.messages.getString("res.481"));
        tips.put("int max(int,int)", App.messages.getString("res.482"));
        tips.put("int min(int,int)", App.messages.getString("res.483"));
        tips.put("int round(float)", App.messages.getString("res.484"));
        tips.put("long abs(long)", App.messages.getString("res.485"));
        tips.put("long max(long,long)", App.messages.getString("res.486"));
        tips.put("long min(long,long)", App.messages.getString("res.487"));
        tips.put("long round(double)", App.messages.getString("res.488"));

        tips.put("int length(String)", App.messages.getString("res.489"));
        tips.put("char charAt(String, int)",
            App.messages.getString("res.490"));
        tips.put("boolean startsWith(String, String)",
            App.messages.getString("res.491"));
        tips.put("boolean endsWith(String, String)",
            App.messages.getString("res.492"));
        tips.put("int indexOf(String, String)",
            App.messages.getString("res.493"));
        tips.put("int lastIndexOf(String, String)",
            App.messages.getString("res.494"));
        tips.put("String substring(String, int)",
            App.messages.getString("res.495"));
        tips.put("String substring(String, int,int)",
            App.messages.getString("res.489"));
        tips.put("String replaceAll(String, String , String )",
            App.messages.getString("res.496"));
        tips.put("String[] split(String, String)",
            App.messages.getString("res.497"));
        tips.put("String toLowerCase(String)",
            App.messages.getString("res.498"));
        tips.put("String toUpperCase(String )", App.messages.getString("res.499"));

        tips.put("int getYear(Date )", App.messages.getString("res.500"));
        tips.put("int getMonth(Date )", App.messages.getString("res.501"));
        tips.put("int getDate(Date )", App.messages.getString("res.502"));
        tips.put("int getDay(Date )", App.messages.getString("res.503"));
        tips.put("int getHours(Date )", App.messages.getString("res.504"));
        tips.put("int getMinutes(Date )", App.messages.getString("res.505"));
        tips.put("int getSeconds(Date )", App.messages.getString("res.506"));
        tips.put("boolean before(Date,Date )",
            App.messages.getString("res.507"));
        tips.put("int getYear(Date )", App.messages.getString("res.500"));
        tips.put("boolean after(Date,Date )",
            App.messages.getString("res.508"));

        tips.put("String toRmbString(double)", App.messages.getString("res.509"));
        tips.put("String toHZYear(int)", App.messages.getString("res.510"));
        tips.put("String toHZMonth(int)", App.messages.getString("res.511"));
        tips.put("String toHZDay(int)", App.messages.getString("res.512"));
        tips.put("String format(float,String)",
            App.messages.getString("res.513"));
        tips.put("String format(double,String)",
            App.messages.getString("res.514"));
        tips.put("String format(Date,String)",
            App.messages.getString("res.515"));

        tips.put("String p(String )", App.messages.getString("res.516"));
        tips.put("String clobString(String )",
            App.messages.getString("res.517"));

        JPanel scrollPanel = new JPanel(new BorderLayout());
        tipLabel = new JLabel(App.messages.getString("res.518"), JLabel.LEFT);
        tipLabel.setHorizontalAlignment(JLabel.LEFT);
        tipLabel.setVerticalAlignment(JLabel.TOP);
        tipLabel.setPreferredSize(new Dimension(20, 50));

        if (varTree != null) {
            JTabbedPane tab = new JTabbedPane();
            tab.addTab(App.messages.getString("res.205"), new JScrollPane(varTree));

            jatools.designer.variable.NodeSourceTree tree = new jatools.designer.variable.NodeSourceTree(false);
            tree.setToggleClickCount(10000);
            tree.setEnablePopup(false);

            if (((Main.getInstance() != null) && (Main.getInstance().getActiveEditor() != null)) &&
                    (Main.getInstance().getActiveEditor().getReport() != null)) {
                tree.initTreeData(Main.getInstance().getActiveEditor().getReport().getNodeSource());
            }

            tree.addPropertyChangeListener(new PropertyChangeListener() {
                    public void propertyChange(PropertyChangeEvent evt) {
                        if (evt.getPropertyName().equals("select.value")) {
                            String selectedText = evt.getNewValue().toString();
                            textArea.replaceSelection(selectedText);
                            textArea.requestFocus();
                        }
                    }
                });
            tab.addTab(App.messages.getString("res.519"), new JScrollPane(tree));

            scrollPanel.add(new JScrollPane(functionTree), "Center");
            scrollPanel.add(tipLabel, "South");
            tab.add(App.messages.getString("res.246"), scrollPanel);

            first = tab;
        } else {
            first = new JScrollPane(functionTree);
        }

        JPanel formulaTextPanel = new JPanel(new BorderLayout());

        JLabel formulaLabel = new JLabel(App.messages.getString("res.520"), JLabel.LEFT);
        formulaTextPanel.add(formulaLabel, BorderLayout.NORTH);
        textArea = new ScriptEditor(astemp);

        JScrollPane bottom = new JScrollPane(textArea);
        formulaTextPanel.add(bottom);

        JSplitPane spthird = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true);
        spthird.setDividerLocation(193);
        spthird.setTopComponent(first);
        spthird.setBottomComponent(formulaTextPanel);

        getContentPane().add(spthird);

        JPanel buttonPanel = new JPanel(new BorderLayout());
        JPanel leftButtonPanel = new JPanel(new FlowLayout());

        CommandPanel rightButtonPanel = CommandPanel.createPanel(false);

        check = new JButton(App.messages.getString("res.521"));
        ok = new JButton(App.messages.getString("res.3"));
        cancel = new JButton(App.messages.getString("res.4"));

        JButton empty = new JButton(App.messages.getString("res.23"));

        ok.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    done();
                }
            });

        cancel.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    hide();
                }
            });

        empty.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    empty();
                }
            });

        rightButtonPanel.addComponent(ok);
        rightButtonPanel.addComponent(cancel);

        if (nullPermitted) {
            rightButtonPanel.addComponent(empty);
        }

        buttonPanel.add(leftButtonPanel, BorderLayout.WEST);
        buttonPanel.add(rightButtonPanel, BorderLayout.EAST);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

        this.nullPermitted = nullPermitted;

        pack();

        Dimension size = showVariable ? new Dimension(630, 480) : new Dimension(420, 430);
        setSize(size);
        this.setLocationRelativeTo(owner);
    }

    protected void empty() {
        textArea.setText(null);
        done();
    }

    private IconTree createFunctionTree() {
        Icon icon = Util.getIcon("/jatools/icons/function.gif");

        SimpleTreeNode rootNode = new SimpleTreeNode(App.messages.getString("res.246"), icon);

        SimpleTreeNode parentNode = rootNode;

        for (int i = 0; i < FUNCTIONS.length; i++) {
            if (FUNCTIONS[i].indexOf("(") == -1) {
                SimpleTreeNode n = new SimpleTreeNode(FUNCTIONS[i], icon);
                rootNode.add(n);
                parentNode = n;
            } else {
                parentNode.add(new SimpleTreeNode(FUNCTIONS[i], icon, 1));
            }
        }

        IconTree tree = new IconTree();
        tree.setRoot(rootNode);
        tree.addTreeSelectionListener(new TreeSelectionListener() {
                String selectedNode = null;
                String value = null;

                public void valueChanged(TreeSelectionEvent e) {
                    TreePath path = e.getNewLeadSelectionPath();
                    SimpleTreeNode simpleNode = (SimpleTreeNode) path.getLastPathComponent();

                    if (simpleNode != null) {
                        selectedNode = simpleNode.getUserObject().toString();

                        if (tips.containsKey(selectedNode)) {
                            value = (String) tips.get(selectedNode);
                            tipLabel.setText("<html><p>" + value + "</p></html>");
                        } else {
                            tipLabel.setText(selectedNode);
                        }
                    }
                }
            });
        tree.setDoubleClickAction(this);

        return tree;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        String text = null;

        if ((e.getSource() == functionTree) && (functionTree.getSelectedType() == 1)) {
            text = (String) functionTree.getSelectedObject();

            if (text != null) {
                int index = text.indexOf(" ");
                int left = text.indexOf("(");

                int count = 0;
                int start = 0;

                for (int i = 0; i < text.length(); i++) {
                    int comma = text.indexOf(",", start);

                    if (comma >= 0) {
                        start = comma + 1;
                        count++;
                    }
                }

                String textShort;
                String str = "";

                if (index >= 0) {
                    textShort = text.substring(index + 1, left);

                    if (count > 0) {
                        for (int i = 0; i < count; i++) {
                            str = str + ",  ";
                        }

                        text = textShort + "(" + str + ")";
                    } else {
                        text = textShort + "()";
                    }
                } else {
                    textArea.requestFocus();
                }

                textArea.replaceSelection(text + " ");
                textArea.requestFocus();

                return;
            }
        } else if ((e.getSource() == varTree) && varTree.isSettable()) {
            text = (String) varTree.getVariable();
        } else if (e.getSource() instanceof JToggleButton) {
            text = ((JToggleButton) e.getSource()).getText() + " ";
        }

        if (text != null) {
            textArea.replaceSelection(text);
            textArea.requestFocus();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Formula start() {
        return start((String) null);
    }

    /**
     * DOCUMENT ME!
     *
     * @param formula DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Formula start(Formula formula) {
        if (formula != null) {
            textArea.setText(formula.getText());
        }

        this.formula = null;
        show();

        return this.formula;
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Formula start(String text) {
        textArea.setText(text);

        this.formula = null;
        show();

        return this.formula;
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean showChooser(Object value) {
        textArea.setText((String) value);
        this.formula = null;
        show();

        return this.formula != null;
    }

    private void done() {
        String txt = textArea.getText();

        boolean isNull = (txt == null) || txt.trim().equals("");

        if (isNull && !nullPermitted) {
            MessageBox.error(CustomFormulaDialog.this, App.messages.getString("res.522"));
            textArea.requestFocus();

            return;
        } else if ((txt.indexOf(PrintConstants.TOTAL_PAGE_NUMBER) != -1) &&
                (!txt.trim().equals(PrintConstants.TOTAL_PAGE_NUMBER))) {
            if (AmbiguousNameNodePattern.matches(txt, PrintConstants.TOTAL_PAGE_NUMBER)) {
                MessageBox.error(CustomFormulaDialog.this,
                    App.messages.getString("res.523") + PrintConstants.TOTAL_PAGE_NUMBER + App.messages.getString("res.524"));

                textArea.requestFocus();

                return;
            }
        }

        formula = new Formula(isNull ? null : txt);
        hide();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue() {
        return this.formula.getText();
    }

    /**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     */
    public static void main(String[] args) {
    }
}
