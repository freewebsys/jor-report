package jatools.designer.action;

import jatools.component.chart.Chart;

import java.awt.event.ActionEvent;


/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.6 $
 * @author $author$
 */
public class NewChartAction extends ReportAction {
    /**
     * Creates a new NewChartAction object.
     */
    public NewChartAction() {
        super("统计图", getIcon("/jatools/icons/chart.gif"), getIcon("/jatools/icons/chart2.gif")); // //$NON-NLS-2$
        putValue(CLASS, Chart.class);
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void actionPerformed(ActionEvent e) {
        Chart chart = new Chart();
        chart.setWidth(270);
        chart.setHeight(200);
        this.getEditor().getReportPanel().setBaby(chart);
    }
}
