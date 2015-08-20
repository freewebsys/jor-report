package jatools.designer;

import jatools.VariableContext;
import jatools.component.Component;
import jatools.designer.data.VariableTree;
import jatools.designer.data.Variable;
import jatools.swingx.SimpleTreeNode;
import jatools.util.Util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Enumeration;
import java.util.Iterator;

import javax.swing.Icon;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeModel;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.9 $
  */
public class VariableTreeModel extends DefaultTreeModel implements TreeModelListener,
    PropertyChangeListener {
    public static final int EDITABLE = 1;
    public static final int REMOVABLE = 2;
    public static final int INSERTABLE = 4;
    public static final int SETTABLE = 8;
    static final Icon variableIcon = Util.getIcon("/jatools/icons/variable.gif");
    static final Icon fieldIcon = Util.getIcon("/jatools/icons/field.gif");
    static final Icon parameterIcon = Util.getIcon("/jatools/icons/parameter.gif");
    static final Icon formulaIcon = Util.getIcon("/jatools/icons/formula.gif");
    static final Icon commandIcon = Util.getIcon("/jatools/icons/command.gif");
    static final Icon sqlIcon = Util.getIcon("/jatools/icons/dataset.gif");
    static final Icon sysIcon = Util.getIcon("/jatools/icons/varsystem.gif");
    static final Icon componentIcon = Util.getIcon("/jatools/icons/component.gif");
    private static final int SYSTEM_CATAGORY = 19;
    private static VariableTreeModel defaults;
    private static SimpleTreeNode commandNode;
    VariableContext vm;

    /**
     * Creates a new VariableTreeModel object.
     *
     * @param vm DOCUMENT ME!
     */
    public VariableTreeModel(VariableContext vm) {
        super(asTree1(vm));
        this.vm = vm;

        this.addTreeModelListener(this);
        this.vm.addChangeListener(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static VariableTreeModel getDefaults() {
        if (defaults == null) {
            defaults = new VariableTreeModel(new VariableContext());
        }

        return defaults;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public SimpleTreeNode getCommandNode() {
        return commandNode;
    }

    private static SimpleTreeNode asTree1(VariableContext context) {
        SimpleTreeNode rootNode = new SimpleTreeNode(new Variable(App.messages.getString("res.205"), 0), variableIcon);

        SimpleTreeNode catagory = new SimpleTreeNode(new Variable(App.messages.getString("res.206"), INSERTABLE),
                parameterIcon, VariableTree.PARAMETER_CATAGORY);

        addVariableNodes(catagory, context, parameterIcon, VariableContext.PARAMETER,
            REMOVABLE | EDITABLE | SETTABLE);

        rootNode.add(catagory);
        catagory = new SimpleTreeNode(new Variable(App.messages.getString("res.207"), INSERTABLE), formulaIcon,
                VariableTree.FORMULA_CATAGORY);

        addVariableNodes(catagory, context, formulaIcon, VariableContext.FORMULA,
            REMOVABLE | EDITABLE | SETTABLE);

        rootNode.add(catagory);

        commandNode = new SimpleTreeNode(new Variable(App.messages.getString("res.208"), INSERTABLE), commandIcon,
                VariableTree.COMMAND_CATAGORY);

        addVariableNodes(commandNode, context, commandIcon, VariableContext.COMMAND,
            REMOVABLE | EDITABLE | SETTABLE);

        rootNode.add(commandNode);

        catagory = new SimpleTreeNode(new Variable(App.messages.getString("res.209"), 0), componentIcon,
                VariableTree.COMPONENT_CATAGORY);

        addVariableNodes(catagory, context, componentIcon, VariableContext.COMPONENT, SETTABLE);
        rootNode.add(catagory);

        SimpleTreeNode sytemCatagory = new SimpleTreeNode(new Variable(App.messages.getString("res.210"), 0), sysIcon,
                SYSTEM_CATAGORY);
        String[] vars = VariableContext.getSystemVariables();

        for (int i = 0; i < vars.length; i++) {
            sytemCatagory.add(new SimpleTreeNode(new Variable(vars[i], SETTABLE, vars[i]),
                    sysIcon, VariableContext.SYSTEM0));
        }

        rootNode.add(sytemCatagory);

        return rootNode;
    }

    /**
     * DOCUMENT ME!
     */
    public void resetComponentVariables() {
    	SimpleTreeNode cat = find(VariableTree.COMPONENT_CATAGORY);
        addVariableNodes(cat, vm, componentIcon,
            VariableContext.COMPONENT, SETTABLE);
        
        this.nodeStructureChanged(cat);
    }

    private SimpleTreeNode find(int type) {
        SimpleTreeNode r = (SimpleTreeNode) this.getRoot();
        Enumeration e = r.breadthFirstEnumeration();

        while (e.hasMoreElements()) {
            SimpleTreeNode n = (SimpleTreeNode) e.nextElement() ;

            if (n.getType() == type) {
                return n;
            }
        }

        return null;
    }

    private static void addVariableNodes(SimpleTreeNode owner, VariableContext m, Icon icon,
        int nodetype, int permission) {
        owner.removeAllChildren();

        Iterator it = m.names(nodetype);

        while (it.hasNext()) {
            String element = (String) it.next();

            SimpleTreeNode node = createVariableNode(m.getVariable(element), icon, nodetype,
                    permission, element);

            owner.add(node);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param variable DOCUMENT ME!
     * @param icon DOCUMENT ME!
     * @param nodetype DOCUMENT ME!
     * @param permission DOCUMENT ME!
     * @param element DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static SimpleTreeNode createVariableNode(Object variable, Icon icon, int nodetype,
        int permission, String element) {
        SimpleTreeNode node = new SimpleTreeNode(new Variable(element, permission, element,
                    variable), icon, nodetype);

        return node;
    }

    /**
     * DOCUMENT ME!
     */
    public void clear() {
        vm.clear();
    }

    /**
     * DOCUMENT ME!
     *
     * @param varName DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getVariable(String varName) {
        return vm.getVariable(varName);
    }

    /**
     * DOCUMENT ME!
     *
     * @param oldKey DOCUMENT ME!
     * @param newKey DOCUMENT ME!
     * @param newVal DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean replace(String oldKey, String newKey, Object newVal) {
        return vm.replace(oldKey, newKey, newVal);
    }

    /**
     * DOCUMENT ME!
     *
     * @param selected DOCUMENT ME!
     * @param oldKey DOCUMENT ME!
     * @param newKey DOCUMENT ME!
     * @param newVal DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean replaceVariable(SimpleTreeNode selected, String oldKey, String newKey,
        Object newVal) {
        vm.replaceVariable(oldKey, newKey, newVal);

        Variable var = (Variable) selected.getUserObject();
        var.setVariable(newVal);
        var.setDisplayName(newKey);
        var.setVariableName(newKey);

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator variables() {
        return vm.variables();
    }

    /**
     * DOCUMENT ME!
     *
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Iterator variables(int type) {
        return vm.variables(type);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void treeNodesChanged(TreeModelEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void treeNodesInserted(TreeModelEvent e) {
        Object[] inserts = e.getChildren();

        for (int i = 0; i < inserts.length; i++) {
            Variable var = (Variable) ((SimpleTreeNode) inserts[i]).getUserObject();
            this.vm.declareVariable(var.getVariableName(), var.getVariable());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void treeNodesRemoved(TreeModelEvent e) {
        Object[] inserts = e.getChildren();

        for (int i = 0; i < inserts.length; i++) {
            Variable var = (Variable) ((SimpleTreeNode) inserts[i]).getUserObject();
            this.vm.undeclareVariable(var.getVariableName());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void treeStructureChanged(TreeModelEvent e) {
    	
    }

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue() instanceof Component) {
            this.resetComponentVariables();
        }
    }
}
