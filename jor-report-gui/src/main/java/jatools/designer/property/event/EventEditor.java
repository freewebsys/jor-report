/*
 * Created on 2004-2-28
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jatools.designer.property.event;

import jatools.accessor.PropertyAccessorWrapper;
import jatools.designer.App;
import jatools.designer.EditorView;
import jatools.designer.ReportEditor;
import jatools.designer.property.PropertyEditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;



/**
 * @author java
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class EventEditor extends JTable implements EditorView {
    TableCellEditor celleditor;
    private ArrayList actionListeners = new ArrayList();
    _TableModel model = new _TableModel();

    //   private ReportEditor editor;

    /**
     * Creates a new ZEventEditor object.
     */
    public EventEditor() {
        setModel(model);
        setRowHeight(20);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    /**
     * DOCUMENT ME!
     *
     * @param editor DOCUMENT ME!
     */
    public void setEditor(TableCellEditor editor) {
        this.celleditor = editor;
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
        if (column == 1) {
            return celleditor;
        } else {
            return super.getCellEditor(row, column);
        }
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
     * @param lst DOCUMENT ME!
     */
    public void addActionListener(ActionListener lst) {
        actionListeners.add(lst);
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
     * @param object
     */
    public void setSelection(Object object) {
        // TODO Auto-generated method stub
        model.selection = object;
        model.setRowCount((object == null) ? 0 : 3);
        repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @param aValue DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     */
    public void setValueAt(Object aValue, int row, int column) {
        if (column == 0) {
            return;
        }

        try {
            fireActionListener(PropertyEditor.START_EDIT);
            model.setValueAtX(aValue, row, column);
        } catch (Exception ex) {
            fireActionListener(PropertyEditor.CANCEL_EDIT);

            return;
        }

        fireActionListener(PropertyEditor.COMMIT_EDIT);
    }

    /**
     * DOCUMENT ME!
     *
     * @param editor DOCUMENT ME!
     */
    public void setEditor(ReportEditor editor) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
}


/**
 * Creates a new ZPropertyTableModel object.
 */
class _TableModel extends DefaultTableModel {
    static final String[] eventNames = {
            "initPrint",
            "beforePrint",
            "afterPrint"
        };
    static final String[] upperEventNames = {
            "InitPrint",
            "BeforePrint",
            "AfterPrint"
        };
    Object selection;

    _TableModel() {
        super(new Object[] {
                App.messages.getString("res.307"),
                App.messages.getString("res.304")
            }, 3); // //$NON-NLS-2$

        //    this.setRowCount(50);
    }

    /**
     * DOCUMENT ME!
     *
     * @param value DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void setValueAtX(Object value, int row, int column)
        throws Exception {
        if (column == 0) {
            return;
        }

        PropertyAccessorWrapper comp = (PropertyAccessorWrapper) selection;
        comp.setValue(upperEventNames[row], value, String.class); // //$NON-NLS-2$
    }

    /**
    * DOCUMENT ME!
    *
    * @param row DOCUMENT ME!
    * @param column DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public boolean isCellEditable(int row, int column) {
        if (column == 0) {
            return false;
        } else {
            return super.isCellEditable(row, column);
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
    public Object getValueAt(int row, int column) {
        if (column == 0) {
            return eventNames[row];
        } else {
            if (selection != null) {
                PropertyAccessorWrapper comp = (PropertyAccessorWrapper) selection;

                return comp.getValue(upperEventNames[row], String.class); // //$NON-NLS-2$
            } else {
                return null;
            }
        }
    }
}
