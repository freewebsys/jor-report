package jatools.swingx;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class CustomTable extends JTable {
    _TableModel model;
    boolean[] editableCache;
    public Object oldValue;

    /**
     * Creates a new CustomTable object.
     *
     * @param columns DOCUMENT ME!
     */
    public CustomTable(String[] columns) {
        model = new _TableModel(columns);
        setModel(model);
    }

    /**
     * DOCUMENT ME!
     *
     * @param column DOCUMENT ME!
     * @param value DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean exists(int column, Object value) {
        for (int i = 0; i < model.getRowCount(); i++) {
            Object tableVal = model.getValueAt(i, column);
            boolean equal = (tableVal == null) ? (value == null) : tableVal.equals(value);

            if (equal) {
                return true;
            }
        }

        return false;
    }

    /**
     * DOCUMENT ME!
     */
    public void upRow() {
        if (canUp()) {
            int row = this.getSelectedRow();
            model.moveRow(row, row, row - 1);

            setSelectedRow(row - 1);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param col DOCUMENT ME!
     * @param editable DOCUMENT ME!
     */
    public void setEditable(int col, boolean editable) {
        if (editableCache == null) {
            this.editableCache = new boolean[this.getColumnCount()];
        }

        editableCache[col] = editable;
    }

    /**
     * DOCUMENT ME!
     */
    public void downRow() {
        if (canDown()) {
            int row = this.getSelectedRow();
            model.moveRow(row, row, row + 1);
            setSelectedRow(row + 1);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean editCellAt(int row, int column, EventObject e) {
        oldValue = this.getValueAt(row, column);

        return super.editCellAt(row, column, e);
    }

    /**
     * DOCUMENT ME!
     */
    public void removeAllRows() {
        model.setRowCount(0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     */
    public void setSelectedRow(int row) {
        this.getSelectionModel().setSelectionInterval(row, row);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canUp() {
        return this.getSelectedRow() > 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canDown() {
        return (this.getRowCount() > 1) && (getSelectedRow() < (getRowCount() - 1));
    }

    /**
     * DOCUMENT ME!
     *
     * @param col DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isEditable(int col) {
        return (editableCache == null) ? false : editableCache[col];
    }

    /**
     * DOCUMENT ME!
     *
     * @param rowData DOCUMENT ME!
     * @param autoSelect DOCUMENT ME!
     */
    public void addRow(Object[] rowData, boolean autoSelect) {
        model.addRow(rowData);
        setSelectedRow(this.getRowCount() - 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row DOCUMENT ME!
     * @param autoSelect DOCUMENT ME!
     */
    public void removeRow(int row, boolean autoSelect) {
        model.removeRow(row);

        if (autoSelect) {
            if (row == this.getRowCount()) {
                row--;
            }

            if (row >= 0) {
                this.setSelectedRow(row);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ArrayList cloneRows() {
        return model.cloneRows();
    }

    /**
     * DOCUMENT ME!
     *
     * @param rows DOCUMENT ME!
     */
    public void loadRows(ArrayList rows) {
        model.loadRows(rows);

        if (getRowCount() > 0) {
            setSelectedRow(0);
        }
    }

    class _TableModel extends DefaultTableModel {
        _TableModel(String[] columns) {
            super(columns, 0);
        }

        public void loadRows(ArrayList rows) {
            this.setRowCount(0);

            for (Iterator iter = rows.iterator(); iter.hasNext();) {
                ArrayList element = (ArrayList) iter.next();
                this.addRow(element.toArray());
            }
        }

        public ArrayList cloneRows() {
            return new ArrayList(this.dataVector);
        }

        public boolean isCellEditable(int row, int col) {
            return isEditable(col);
        }
    }
}
