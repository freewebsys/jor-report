package jatools.designer.componenteditor;

import jatools.component.chart.Chart;

import jatools.data.reader.DatasetReader;

import jatools.designer.Main;

import jatools.designer.peer.ComponentPeer;

import jatools.designer.undo.ChartSetEdit;

import java.awt.Frame;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import java.util.ArrayList;
import java.util.Properties;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
 */
public class ChartEditor implements ComponentEditor {
    /*
     * (non-Javadoc)
     *
     * @see com.jatools.designer.layer.ZComponentEditor#show(com.jatools.designer.ZComponentPeer,
     *      javax.swing.undo.CompoundEdit)
     */
    private Chart chart;
    private Properties props;
    private DatasetReader reader;
    private String label;
    private ArrayList data;

    /**
    * DOCUMENT ME!
    *
    * @param peer DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public void show(final ComponentPeer peer) {
        chart = (Chart) peer.getComponent();

        props = (Properties) chart.getProperties();

        if (props != null) {
            props = (Properties) props.clone();
        }

        reader = chart.getReader();

        label = chart.getLabelField();

        data = (ArrayList) chart.getPlotData();

        if (data != null) {
            data = (ArrayList) data.clone();
        }

        final jatools.component.chart.customizer.KChartEditor editor = new jatools.component.chart.customizer.KChartEditor((Frame) peer.getOwner()
                                                                                                                                       .getTopLevelAncestor(),
                (Chart) peer.getComponent());

        editor.show();
        editor.addComponentListener(new java.awt.event.ComponentAdapter() {
                public void componentHidden(ComponentEvent e) {
                    if (editor.done) {
                        ChartSetEdit edit = new ChartSetEdit(peer, props, reader, label, data);

                        peer.getOwner().addEdit(edit);
                    } else {
                        chart.setProperties(props);
                        chart.setReader(reader);
                        chart.setLabelField(label);
                        chart.setPlotData(data);
                        chart.chart = null;

                        peer.getOwner().repaint();
                    }

                    peer.setEditing(false);
                }
            });
    }
}
