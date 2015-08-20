package jatools.designer.action;

import jatools.component.Component;
import jatools.component.table.CellStore;
import jatools.component.table.Table;
import jatools.core.view.Border;
import jatools.designer.App;
import jatools.designer.layer.table.BlankCellLoader;

import java.awt.event.ActionEvent;
import java.util.Arrays;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.6 $
  */
public class NewTableAction extends ReportAction implements NewTable {
    /**
     * Creates a new NewTableAction object.
     *
     * @param owner DOCUMENT ME!
     */
    public NewTableAction() {
        super(App.messages.getString("res.593"), getIcon("/jatools/icons/table.gif"), getIcon("/jatools/icons/table2.gif"));
        putValue(CLASS,Table.class );      
        }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        int[] rows = new int[2];
        int[] cols = new int[6];

        Arrays.fill(rows, 20);
        Arrays.fill(cols, 60);

        Table table = new Table(rows, cols);

        BlankCellLoader.load(table);
        
        setBorder(table);
        
        this.getEditor().getReportPanel().setBaby(table);
    }
    
    private void setBorder(Table table) {
        CellStore cellstore = table.getCellstore();

        for (int r = 0; r < table.getRowCount(); r++) {
            for (int c = 0; c < table.getColumnCount(); c++) {
                Component child = cellstore.getComponent(r, c);

                if (child != null) {
                    child.setBorder(new Border());
                }
            }
        }
    }
}
