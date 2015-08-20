package jatools.designer.data;


import jatools.data.reader.AbstractDatasetReader;
import jatools.data.reader.DatasetReader;
import jatools.dataset.DatasetException;
import jatools.dataset.Row;
import jatools.dataset.RowMeta;
import jatools.db.TypeUtil;
import jatools.designer.App;
import jatools.engine.script.ReportContext;
import jatools.swingx.MessageBox;

import java.awt.Component;
import java.awt.FontMetrics;
import java.io.File;
import java.io.FileWriter;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class DatasetTable extends JTable {
    public static ChangeEvent START = new ChangeEvent(Boolean.FALSE);
    public static ChangeEvent CANCELLED = new ChangeEvent(Boolean.FALSE);
    public static ChangeEvent DONE = new ChangeEvent(Boolean.FALSE);
    public static ChangeEvent ADD_ROW = new ChangeEvent(Boolean.FALSE);
    static DebugCellRenderer timeRenderer;
    private Class[] classes;
    private DatasetReader reader;
    private RowMeta rowInfo;
    private boolean cancelled;
    ChangeListener changeListener = null;

    /**
     * Creates a new DatasetTable object.
     */
    public DatasetTable() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param changeListener DOCUMENT ME!
     */
    public void setChangeListener(ChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    /**
     * DOCUMENT ME!
     *
     * @param _reader DOCUMENT ME!
     */
    public void setDataSet(DatasetReader _reader) {
        this.reader = _reader;
        cancelled = false;

        DefaultTableModel m = (DefaultTableModel) this.getModel();

        m.setRowCount(0);

        try {
            final AbstractDatasetReader reader2 = (AbstractDatasetReader) reader;
            rowInfo = reader2.readStart(ReportContext.getDefaultContext(), true);

            m.setColumnCount(rowInfo.getColumnCount());

            String[] columns = new String[rowInfo.getColumnCount()];
            classes = new Class[rowInfo.getColumnCount()];

            for (int i = 0; i < columns.length; i++) {
                columns[i] = rowInfo.getColumnName(i);
                classes[i] = rowInfo.getColumnInfo(i).getColumnClass();
            }

            m.setColumnIdentifiers(columns);

            for (int i = 0; i < columns.length; i++) {
                if ((classes[i] == Time.class) || (classes[i] == Timestamp.class)) {
                    if (timeRenderer == null) {
                        timeRenderer = new DebugCellRenderer();
                    }

                    getColumnModel().getColumn(i).setCellRenderer(timeRenderer);
                }
            }

            this.repaint();

            for (int i = 0; i < columns.length; i++) {
                columns[i] = rowInfo.getColumnName(i);
            }

            Runnable runner = new Runnable() {
                    public void run() {
                        if (changeListener != null) {
                            changeListener.stateChanged(START);
                        }

                        Row row;

                        try {
                            while ((!cancelled) &&
                                    ((row = reader2.readRow(rowInfo)) != Row.NO_MORE_ROWS)) {
                                DefaultTableModel m = (DefaultTableModel) getModel();
                                m.addRow(row.values());

                                SwingUtilities.invokeLater(new Runnable() {
                                        public void run() {
                                            if (changeListener != null) {
                                                changeListener.stateChanged(ADD_ROW);
                                            }

                                            if (getRowCount() < 30) {
                                                resizeColumns(DatasetTable.this);
                                            }

                                            DatasetTable.this.repaint();
                                        }
                                    });
                            }

                            resizeColumns(DatasetTable.this);
                            reader2.readEnd();
                        } catch (DatasetException e) {
                            MessageBox.error(DatasetTable.this, e.getMessage());
                        }

                        if (changeListener != null) {
                            changeListener.stateChanged(cancelled ? CANCELLED : DONE);
                        }
                    }
                };

            new Thread(runner).start();
        } catch (DatasetException e) {
            MessageBox.error(DatasetTable.this, e.getMessage());
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Class getColumnClass(int column) {
        if (classes != null) {
            return classes[column];
        }

        return super.getColumnClass(column);
    }

    /**
     * DOCUMENT ME!
     */
    public void cancel() {
        this.cancelled = true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param t DOCUMENT ME!
     */
    public static void resizeColumns(JTable t) {
        t.setAutoResizeMode(AUTO_RESIZE_OFF);

        FontMetrics fm = t.getFontMetrics(t.getFont());

        for (int i = 0; i < t.getColumnCount(); i++) {
            TableColumn c = t.getColumn(t.getColumnName(i));
            c.setPreferredWidth(determineColumnWidth(c, t.getModel(), fm));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param aValue DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param col DOCUMENT ME!
     */
    public void setValueAt(Object aValue, int row, int col) {
        super.setValueAt(aValue, row, col);
        resizeColumns(this);
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
        int headerWidth = fm.stringWidth("." + value.trim() + ".");
        int columnNumber = col.getModelIndex();
        int max = headerWidth;
        int columnWidth = 0;
        int nrows = model.getRowCount();

        for (int i = 0; (i < nrows) && (i < 100); i++) {
            value = model.getValueAt(i, columnNumber) + "";
            columnWidth = fm.stringWidth("." + value.trim() + ".");

            if (columnWidth > max) {
                max = columnWidth;
            }
        }
        
        columnWidth = fm.stringWidth( model.getColumnName(columnNumber) + "....");

        if (columnWidth > max) {
            max = columnWidth;
        }

        return max + 5;
    }

    /**
     * DOCUMENT ME!
     */
    public void refresh() {
        this.setDataSet(reader);
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void saveAsCsv(File f) throws Exception {
        FileWriter writer = new FileWriter(f);

        try {
            String classes = "";
            String names = "";

            for (int i = 0; i < getColumnCount(); i++) {
                if (i > 0) {
                    classes += ",";
                    names += ",";
                }

                String cls = TypeUtil.getShortName(this.getColumnClass(i));

                if (cls == null) {
                    throw new Exception(App.messages.getString("res.544") + this.getColumnClass(i) + App.messages.getString("res.545"));
                }

                classes += cls;
                names += this.getColumnName(i);
            }

            writer.write(classes);
            writer.write("\n");
            writer.write(names);
            writer.write("\n");

            for (int r = 0; r < this.getRowCount(); r++) {
                for (int c = 0; c < this.getColumnCount(); c++) {
                    if (c > 0) {
                        writer.write(",");
                    }

                    String str = this.getValueAt(r, c) + "";
                    writer.write(str);
                }

                writer.write("\n");
            }

            writer.close();
        } catch (Exception e) {
            writer.close();
            throw e;
        }
    }
}


class DebugCellRenderer extends DefaultTableCellRenderer implements TableCellRenderer {
    private DateFormat formatter = new SimpleDateFormat("yyyy-M-d h:m:s");

    /**
     * DOCUMENT ME!
     *
     * @param table DOCUMENT ME!
     * @param value DOCUMENT ME!
     * @param isSelected DOCUMENT ME!
     * @param hasFocus DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getTableCellRendererComponent(final JTable table, final Object value,
        final boolean isSelected, final boolean hasFocus, final int row, final int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
                column);

        if (value != null) {
            setText(this.formatter.format(value));
        } else {
            setText(null);
        }

        return c;
    }
}
