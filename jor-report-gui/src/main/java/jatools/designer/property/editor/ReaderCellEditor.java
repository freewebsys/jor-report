package jatools.designer.property.editor;



import jatools.data.reader.DatasetReader;
import jatools.designer.App;
import jatools.designer.config.DatasetReaderList;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ReaderCellEditor extends AbstractCellEditor implements TableCellEditor {
    JComboBox combobox;
    protected DatasetReader reader;

    /**
     * Creates a new ReaderCellEditor object.
     *
     * @param combobox DOCUMENT ME!
     */
    public ReaderCellEditor(JComboBox combobox) {
        this.combobox = combobox;
        this.combobox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    reader = (DatasetReader) ReaderCellEditor.this.combobox.getSelectedItem();

                    if (reader != null) {
                        reader = (DatasetReader) reader.clone();
                    }

                    stopCellEditing();
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @param table DOCUMENT ME!
     * @param value DOCUMENT ME!
     * @param isSelected DOCUMENT ME!
     * @param row DOCUMENT ME!
     * @param column DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected,
        int row, int column) {
        return combobox;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isCellEditable(EventObject e) {
        combobox.removeAllItems();

        DatasetReaderList dsp = App.getConfiguration();

        for (int i = 0; i < dsp.getCount(); i++) {
            combobox.addItem(dsp.getDatasetReader(i));
        }

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getCellEditorValue() {
        return reader;
    }
}
