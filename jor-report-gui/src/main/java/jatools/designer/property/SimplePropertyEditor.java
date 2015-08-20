package jatools.designer.property;



import jatools.accessor.PropertyDescriptor;
import jatools.designer.EditorView;
import jatools.designer.Main;
import jatools.designer.ReportEditor;
import jatools.designer.property.event.EventEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class SimplePropertyEditor extends JTable implements ChangeListener, EditorView {
    private Map renderers = new HashMap();
    private Map editors = new HashMap();
    private ArrayList actionListeners = new ArrayList();
    EventEditor eventEditor;
    private ReportEditor editor;
    private Object[] selection;

    /**
     * Creates a new SimplePropertyEditor object.
     *
     * @param register DOCUMENT ME!
     */
    public SimplePropertyEditor(boolean register) {
        super(new PropertyTableModel());
        setRowHeight(20);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        if (register && (Main.getInstance() != null)) {
            Main.getInstance().registerEditorView(this);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param filter DOCUMENT ME!
     */
    public void addPropertyFilter(PropertyFilter filter) {
        ((PropertyTableModel) getModel()).addPropertyFilter(filter);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param editor DOCUMENT ME!
     */
    public void registerPropertyEditor(Object key, TableCellEditor editor) {
        editors.put(key, editor);
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param renderer DOCUMENT ME!
     */
    public void registerPropertyRenderer(Object key, TableCellRenderer renderer) {
        renderers.put(key, renderer);
    }

    /**
     * DOCUMENT ME!
     *
     * @param evt DOCUMENT ME!
     */
    public void stateChanged(ChangeEvent evt) {
        resetPropertyEntries();

        if (this.editor.getReportPanel().getSelection().length > 0) {
            eventEditor.setSelection(this.editor.getReportPanel().getSelection()[0]);
        } else {
            eventEditor.setSelection(null);
        }

        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void addActionListener(ActionListener lst) {
        actionListeners.add(lst);
    }

    /**
     * DOCUMENT ME!
     *
     * @param lst DOCUMENT ME!
     */
    public void removeActionListener(ActionListener lst) {
        actionListeners.remove(lst);
    }

    /**
     * DOCUMENT ME!
     *
     * @param actionCommand DOCUMENT ME!
     */
    public void fireActionListener(String actionCommand) {
        ActionEvent evt = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, actionCommand);

        for (int i = 0; i < actionListeners.size(); i++) {
            ((ActionListener) actionListeners.get(i)).actionPerformed(evt);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TableCellEditor getCellEditor(int row, int column) {
        PropertyTableModel model = (PropertyTableModel) getModel();

        PropertyDescriptor property = model.getPropertyEntry(row);

        TableCellEditor editor = (TableCellEditor) editors.get(property.getPropertyName());

        if (editor == null) {
            editor = (TableCellEditor) editors.get(model.getClassAt(row));

            if (editor == null) {
                editor = super.getCellEditor(row, column);
            }
        }

        this.selection = ((PropertyTableModel) getModel()).getSelection();

        return editor;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void editingStopped(ChangeEvent e) {
        TableCellEditor editor = getCellEditor();

        if (editor != null) {
            Object value = editor.getCellEditorValue();
            setValueAt(value, editingRow, editingColumn, this.selection);
            removeEditor();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TableCellRenderer getCellRenderer(int row, int column) {
        PropertyTableModel model = (PropertyTableModel) getModel();
        PropertyDescriptor property = model.getPropertyEntry(row);

        if (column == 1) {
            TableCellRenderer renderer = (TableCellRenderer) renderers.get(property.getPropertyName());

            if (renderer != null) {
                return renderer;
            }

            renderer = (TableCellRenderer) renderers.get(model.getClassAt(row));

            if (renderer != null) {
                return renderer;
            }
        }

        return super.getCellRenderer(row, column);
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj DOCUMENT ME!
     */
    public void select(Object obj) {
        PropertyTableModel model = (PropertyTableModel) getModel();
        model.select(obj);
    }

    /**
     * DOCUMENT ME!
     *
     * @param columnClass DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TableCellRenderer getDefaultRenderer(Class columnClass) {
        return new PropertyTableCellRenderer();
    }

    /**
     * DOCUMENT ME!
     *
     * @param aValue DOCUMENT ME!
     * @param fontProp DOCUMENT ME!
     */
    public void setFontAt(Object aValue, String fontProp) {
        try {
            fireActionListener(PropertyEditor.START_EDIT);
            ((PropertyTableModel) getModel()).setFontAt(aValue, fontProp);
        } catch (Exception ex) {
            fireActionListener(PropertyEditor.CANCEL_EDIT);

            return;
        }

        fireActionListener(PropertyEditor.COMMIT_EDIT);
    }

    /**
     * DOCUMENT ME!
     *
     * @param border DOCUMENT ME!
     */
    public void setBorder(String border) {
        try {
            fireActionListener(PropertyEditor.START_EDIT);
            ((PropertyTableModel) getModel()).setBorder(border);
        } catch (Exception ex) {
            fireActionListener(PropertyEditor.CANCEL_EDIT);

            return;
        }

        fireActionListener(PropertyEditor.COMMIT_EDIT);
    }

    /**
     * DOCUMENT ME!
     */
    public void resetPropertyEntries() {
        if (this.isEditing()) {
            this.getCellEditor().stopCellEditing();
        }

        ((PropertyTableModel) getModel()).resetPropertyEntries();
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     * @param type DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getPropertyValue(String prop, Class type) {
        return ((PropertyTableModel) getModel()).getPropertyValue(prop, type);
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean existProperty(String prop) {
        return ((PropertyTableModel) getModel()).existProperty(prop);
    }

    /**
     * DOCUMENT ME!
     *
     * @param editor DOCUMENT ME!
     */
    public void setEditor(ReportEditor editor) {
        if (this.editor != null) {
            this.editor.removeSelectionChangeListener(this);
            this.removeActionListener(this.editor.getReportPanel());
        }

        this.editor = editor;

        if (this.editor != null) {
            this.editor.addSelectionChangeListener(this);
            this.addActionListener(this.editor.getReportPanel());
        }

        this.resetPropertyEntries();
        this.invalidate();
        this.repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @param aValue DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     * @param selection DOCUMENT ME!
     */
    public void setValueAt(Object aValue, int row, int column, Object[] selection) {
        try {
            fireActionListener(PropertyEditor.START_EDIT);
            ((PropertyTableModel) getModel()).setValueAtX(aValue, row,
                convertColumnIndexToModel(column), selection);
        } catch (Exception ex) {
            fireActionListener(PropertyEditor.CANCEL_EDIT);

            return;
        }

        fireActionListener(PropertyEditor.COMMIT_EDIT);
    }
}
