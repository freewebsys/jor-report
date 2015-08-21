package jatools.designer.action;

import jatools.component.Component;
import jatools.component.table.CellStore;
import jatools.component.table.PowerTable;
import jatools.component.table.Table;
import jatools.component.table.TableBase;
import jatools.core.view.Border;
import jatools.designer.App;
import jatools.designer.layer.table.BlankCellLoader;

import java.awt.event.ActionEvent;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.8 $
  */
public class NewPowerTableAction extends ReportAction implements NewTable  {
    /**
     * Creates a new ZInsertTableAction object.
     *
     * @param owner DOCUMENT ME!
     */
    public NewPowerTableAction() {
        super(App.messages.getString("res.592"), getIcon("/jatools/icons/crosstab16.gif"), getIcon("/jatools/icons/crosstab2.gif")); // //$NON-NLS-2$
        putValue(CLASS,PowerTable.class );
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        Table topHeader = getTopTable();
        Table leftHeader = getLeftTable();

        //
        PowerTable power = PowerTable.create(leftHeader, topHeader);

        BlankCellLoader.load(power);

        
        setBorder(power);

        this.getEditor().getReportPanel().setBaby(power);
    }

    Table getTopTable() {
        Table table = new Table(new int[] {
                    20
                }, new int[] {
                    80,
                    80
                }); //ZBindingDatasetDialog.clickCreate(owner.getFrame() ,owner.getEditor().getReportPanel() ,owner.getEditor().getDocument()  );

        table.setRightFlow(true);

        return table;
    }

    Table getLeftTable() {
        Table table = new Table(new int[] {
                    20,
                    20
                }, new int[] {
                    80
                }); //ZBindingDatasetDialog.clickCreate(owner.getFrame() ,owner.getEditor().getReportPanel() ,owner.getEditor().getDocument()  );

        return table;
    }

    private void setBorder(TableBase table) {
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
