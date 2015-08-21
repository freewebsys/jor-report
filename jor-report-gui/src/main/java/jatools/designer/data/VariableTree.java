package jatools.designer.data;

import jatools.VariableContext;

import jatools.data.Formula;
import jatools.data.Parameter;

import jatools.data.reader.DatasetReader;

import jatools.designer.App;
import jatools.designer.DataTreeUtil;
import jatools.designer.DocumentVariableNameChecker;
import jatools.designer.DocumentVariableNamePicker;
import jatools.designer.EditorView;
import jatools.designer.Main;
import jatools.designer.ReportEditor;
import jatools.designer.VariableTreeModel;

import jatools.swingx.MessageBox;
import jatools.swingx.SimpleTreeNode;

import jatools.util.CursorUtil;

import java.awt.Frame;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class VariableTree extends IconTree implements ActionListener, ChangeListener, NameChecker,
    DragSourceListener, DragGestureListener, EditorView {
    public static final int ROWS_READER_CATAGORY = 6;
    public static final int ROWS_READER_FIELD = 7;
    public static final int PARAMETER_CATAGORY = 11;
    public static final int FORMULA_CATAGORY = 13;
    public static final int COMMAND_CATAGORY = 15;
    public static final int COMMAND = 16;
    public static final int FORMULA = 14;
    public static final int COMPONENT_CATAGORY = 21;
    public static final int SYSTEM_CATAGORY = 23;
    static final String FORMULA_PREFIX = "$formula_";
    static final String DATADICTIONARY_PREFIX = "$dict_";
    public static String COMMAND_PREFIX = "$command_";
    public final static int CANCEL = 0;
    public final static int OK = 1;
    public static int NULL = 2;
    public static final int EDITABLE = 1;
    public static final int REMOVABLE = 2;
    public static final int INSERTABLE = 4;
    public static final int SETTABLE = 8;
    int xok = CANCEL;
    Action addAction;
    Action deleteAction;
    Action editAction;
    private JPopupMenu popup;
    Frame owner0;
    boolean showPopupMenu = true;
    private VariableTreeModel vm;

    /**
     * Creates a new VariableTree object.
     */
    public VariableTree() {
        addAction = new _AddAction();
        deleteAction = new _DeleteAction();
        editAction = new _EditAction();

        addAction.setEnabled(false);
        deleteAction.setEnabled(false);
        deleteAction.setEnabled(false);
        buildUI();

        DragSource dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);

        if (Main.getInstance() != null) {
            Main.getInstance().registerEditorView(this);
        }

        this.setShowsRootHandles(true);
        this.setRootVisible(false);
    }

    /**
     * DOCUMENT ME!
     *
     * @param owner DOCUMENT ME!
     */
    public void setOwner(Frame owner) {
        this.owner0 = owner;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Action getAddAction() {
        return addAction;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Action getDeleteAction() {
        return deleteAction;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Action getEditAction() {
        return editAction;
    }

    private void buildUI() {
        popup = new JPopupMenu();

        popup.add(addAction);
        popup.add(deleteAction);
        popup.add(editAction);

        popup.setOpaque(true);
        popup.setLightWeightPopupEnabled(true);

        setPopupActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e0) {
                    if (showPopupMenu) {
                        MouseEvent e = (MouseEvent) e0.getSource();

                        popup.show((JComponent) e.getSource(), e.getX(), e.getY());
                    }
                }
            });

        addChangeListener(this);
    }

    void enabledActions(int permission) {
        addAction.setEnabled((permission & DataTreeUtil.INSERTABLE) > 0);
        deleteAction.setEnabled((permission & DataTreeUtil.REMOVABLE) > 0);
        editAction.setEnabled((permission & DataTreeUtil.EDITABLE) > 0);

        int type = this.getSelectedType();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSettable() {
        Variable var = (Variable) this.getSelectedObject();

        if (var != null) {
            return (var.permission & VariableTree.SETTABLE) != 0;
        } else {
            return false;
        }
    }

    protected void delete() {
        if (MessageBox.show(this.getTopLevelAncestor(), App.messages.getString("res.437"),
                    App.messages.getString("res.555"), MessageBox.OK_CANCEL) == MessageBox.OK) {
            SimpleTreeNode selected = (SimpleTreeNode) this.getSelectionModel().getSelectionPath()
                                                           .getLastPathComponent();

            DefaultTreeModel model = (DefaultTreeModel) this.getModel();
            model.removeNodeFromParent(selected);
        }
    }

    protected Object getVariableValue() {
        if (vm != null) {
            return vm.getVariable(this.getVariable());
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     */
    public void setToolTipText(String text) {
        setToolTipText(text);
    }

    protected void edit() {
        SimpleTreeNode selected = (SimpleTreeNode) this.getSelectionPath().getLastPathComponent();

        if (selected.getType() == VariableContext.PARAMETER) {
            Parameter para = (Parameter) getVariableValue();

            if (para != null) {
                String old = para.getName();

                CustomParameterDialog d = new CustomParameterDialog();
                para = d.start(para,
                        new DocumentVariableNameChecker(
                            Main.getInstance().getActiveEditor().getDocument()));

                if (para != null) {
                    vm.replaceVariable(selected, old, para.getName(), para);
                    repaint();
                }
            }
        } else if (selected.getType() == VariableContext.FORMULA) {
            Formula formula = (Formula) getVariableValue();

            if (formula != null) {
                CustomFormulaDialog d = new CustomFormulaDialog(owner0, false);
                formula = d.start(formula);

                if (formula != null) {
                    String var = (String) getVariable();

                    vm.replaceVariable(selected, var, var, formula);
                    repaint();
                }
            }
        }
    }

    boolean isSelected(int i) {
        return false;
    }

    protected void add() {
        DocumentVariableNamePicker picker = new DocumentVariableNamePicker(Main.getInstance()
                                                                               .getActiveEditor()
                                                                               .getDocument());
        DocumentVariableNameChecker checker = new DocumentVariableNameChecker(Main.getInstance()
                                                                                  .getActiveEditor()
                                                                                  .getDocument());

        SimpleTreeNode selected = (SimpleTreeNode) this.getSelectionModel().getSelectionPath()
                                                       .getLastPathComponent();

        if (selected.getType() == VariableTree.FORMULA_CATAGORY) {
            String var = picker.newName(FORMULA_PREFIX);
            var = new NameDialog(owner0).start(checker, var);

            if (var != null) {
                CustomFormulaDialog d = new CustomFormulaDialog(owner0, false);

                Formula formula = d.start();

                if (formula != null) {
                    DefaultTreeModel model = (DefaultTreeModel) this.getModel();

                    SimpleTreeNode newNode = VariableTreeModel.createVariableNode(formula,
                            selected.getIcon(), VariableContext.FORMULA,
                            REMOVABLE | EDITABLE | SETTABLE, var);

                    model.insertNodeInto(newNode, selected, selected.getChildCount());
                    expandPath(getSelectionPath());

                    setSelectionPath(this.getSelectionPath().pathByAddingChild(newNode));
                }
            }
        } else if (selected.getType() == VariableTree.PARAMETER_CATAGORY) {
            CustomParameterDialog d = new CustomParameterDialog();

            Parameter para = new Parameter();

            String var = picker.newName(VariableContext.PARAMETER_PREFIX);

            para.setName(var);

            para = d.start(para, checker);

            if (para != null) {
                DefaultTreeModel model = (DefaultTreeModel) this.getModel();

                SimpleTreeNode newNode = VariableTreeModel.createVariableNode(para,
                        selected.getIcon(), VariableContext.PARAMETER,
                        REMOVABLE | EDITABLE | SETTABLE, para.getName());

                model.insertNodeInto(newNode, selected, selected.getChildCount());
                expandPath(getSelectionPath());

                setSelectionPath(this.getSelectionPath().pathByAddingChild(newNode));
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param newModel DOCUMENT ME!
     */
    public void setModel(TreeModel newModel) {
        super.setModel(newModel);

        expandAll();
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand() == ReportEditor.OPEN) {
            this.setModel(((ReportEditor) e.getSource()).getVariableModel());

            setOwner((Frame) ((ReportEditor) e.getSource()).getTopLevelAncestor());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param source DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void check(String source) throws Exception {
        DocumentVariableNameChecker checker = new DocumentVariableNameChecker(Main.getInstance()
                                                                                  .getActiveEditor()
                                                                                  .getDocument());

        checker.check(source);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent e) {
        Variable var = (Variable) this.getSelectedObject();

        if (var != null) {
            enabledActions(var.permission);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getVariable() {
        Variable var = (Variable) getSelectedObject();

        if (var != null) {
            return var.variableName;
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param var DOCUMENT ME!
     */
    public void setVariable(String var) {
        if (var != null) {
            Variable v = new Variable(var, 0, var);
            selectObject(v);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param showPopupMenu DOCUMENT ME!
     */
    public void setShowPopupMenu(boolean showPopupMenu) {
        this.showPopupMenu = showPopupMenu;
    }

    /**
     * DOCUMENT ME!
     *
     * @param dsde DOCUMENT ME!
     */
    public void dragEnter(DragSourceDragEvent dsde) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param dsde DOCUMENT ME!
     */
    public void dragOver(DragSourceDragEvent dsde) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param dsde DOCUMENT ME!
     */
    public void dropActionChanged(DragSourceDragEvent dsde) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param dsde DOCUMENT ME!
     */
    public void dragDropEnd(DragSourceDropEvent dsde) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param dse DOCUMENT ME!
     */
    public void dragExit(DragSourceEvent dse) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param dge DOCUMENT ME!
     */
    public void dragGestureRecognized(DragGestureEvent dge) {
        Variable var = (Variable) this.getSelectedObject();

        if ((var != null) && var.isSettable()) {
            VariableTransferable trans = new VariableTransferable(var.getVariableName());

            dge.startDrag(CursorUtil.CLICK_PLAY_CURSOR, trans);
        }
    }

    private void preview() {
        DatasetReader reader = (DatasetReader) this.getVariableValue();

        if (reader != null) {
            try {
                DatasetPreviewer preview = new DatasetPreviewer(Main.getInstance());
                preview.setLocationRelativeTo(this);
                preview.setReader(reader);
                preview.show();
            } catch (Exception ex) {
                ex.printStackTrace();

                MessageBox.error(this.getTopLevelAncestor(), ex.getMessage());
            }
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void scrollSelectedRowToVisible() {
        int[] rows = getSelectionRows();

        if ((rows != null) && (rows.length > 0)) {
            scrollRowToVisible(rows[0]);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param editor DOCUMENT ME!
     */
    public void setEditor(ReportEditor editor) {
        if (editor != null) {
            this.vm = (VariableTreeModel) editor.getVariableModel();
            this.setModel(vm);
        }
    }

    class _AddAction extends AbstractAction {
        _AddAction() {
            super(App.messages.getString("res.93"));
        }

        public void actionPerformed(ActionEvent e) {
            add();
        }
    }

    class _DeleteAction extends AbstractAction {
        _DeleteAction() {
            super(App.messages.getString("res.96"));
        }

        public void actionPerformed(ActionEvent e) {
            delete();
        }
    }

    class _EditAction extends AbstractAction {
        _EditAction() {
            super(App.messages.getString("res.95"));
        }

        public void actionPerformed(ActionEvent e) {
            edit();
        }
    }

    class _PreviewAction extends AbstractAction {
        _PreviewAction() {
            super(App.messages.getString("res.97"));
        }

        public void actionPerformed(ActionEvent e) {
            preview();
        }
    }

    class _ChangeListener implements ChangeListener {
        SimpleTreeNode owner;
        JTree _tree;

        public _ChangeListener(SimpleTreeNode owner, JTree tree) {
            this.owner = owner;
            this._tree = tree;
        }

        public void stateChanged(ChangeEvent e) {
        }
    }
}
