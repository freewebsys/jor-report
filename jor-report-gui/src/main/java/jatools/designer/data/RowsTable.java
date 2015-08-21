/*
 * Created on 2003-12-30
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jatools.designer.data;

import jatools.dataset.Dataset;

import java.awt.FontMetrics;
import java.sql.SQLException;

import javax.swing.JTable;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;


/**
 * @author zhou
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class RowsTable extends JTable {
    /**
    * DOCUMENT ME!
    *
    * @param datasetWrapper DOCUMENT ME!
    */
    public void setRowSet(Dataset rows) {
        this.setModel(new _TableModel(rows));
        setAutoResizeMode(AUTO_RESIZE_OFF);

        FontMetrics fm = getFontMetrics(getFont());

        for (int i = 0; i < getColumnCount(); i++) {
            TableColumn c = getColumn(getColumnName(i));
            c.setPreferredWidth(determineColumnWidth(c, getModel(), fm));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param col DOCUMENT ME!
     * @param model DOCUMENT ME!
     * @param fm DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static int determineColumnWidth(TableColumn col, TableModel model, FontMetrics fm) {
        String value = (String) col.getHeaderValue();
        int headerWidth = fm.stringWidth("." + value.trim() + "."); // //$NON-NLS-2$
        int columnNumber = col.getModelIndex();
        int max = headerWidth;
        int columnWidth = 0;
        int nrows = model.getRowCount();

        for (int i = 0; i < nrows; i++) {
            Object obj = model.getValueAt(i, columnNumber);

            if (obj != null) {
                value = (String) obj.toString();
                columnWidth = fm.stringWidth("." + value.trim() + "."); // //$NON-NLS-2$

                if (columnWidth > max) {
                    max = columnWidth;
                }
            }
        }

        return max + 5;
    }

    /**
         * @author   java9
         */
    class _TableModel implements TableModel {
        Dataset dataset;
        int numcols;
        int numrows;

        /**
        * Creates a new ZDataSetTableModel object.
        *
        * @param datasetWrapper DOCUMENT ME!
        *
        * @throws SQLException DOCUMENT ME!
        */
        _TableModel(Dataset rows) {
            this.dataset = rows; // Save the results
            numcols = dataset.getColumnCount();
            numrows = dataset.getRowCount();
        }

        /**
        * DOCUMENT ME!
        *
        * @return DOCUMENT ME!
        */
        public int getColumnCount() {
            return numcols;
        }

        /**
        * DOCUMENT ME!
        *
        * @return DOCUMENT ME!
        */
        public int getRowCount() {
            return numrows;
        }

        /**
        * DOCUMENT ME!
        *
        * @param column DOCUMENT ME!
        *
        * @return DOCUMENT ME!
        */
        public String getColumnName(int column) {
            return dataset.getColumnName(column);
        }

        /**
        * DOCUMENT ME!
        *
        * @param column DOCUMENT ME!
        *
        * @return DOCUMENT ME!
        */
        public Class getColumnClass(int column) {
            return String.class;
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
            return dataset.getValueAt(row, column);
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
            return false;
        }

        /**
        * DOCUMENT ME!
        *
        * @param value DOCUMENT ME!
        * @param row DOCUMENT ME!
        * @param column DOCUMENT ME!
        */
        public void setValueAt(Object value, int row, int column) {
        }

        /**
        * DOCUMENT ME!
        *
        * @param l DOCUMENT ME!
        */
        public void addTableModelListener(TableModelListener l) {
        }

        /**
        * DOCUMENT ME!
        *
        * @param l DOCUMENT ME!
        */
        public void removeTableModelListener(TableModelListener l) {
        }
    }
}
