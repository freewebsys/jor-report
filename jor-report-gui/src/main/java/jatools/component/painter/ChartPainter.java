package jatools.component.painter;

import jatools.component.Component;
import jatools.component.chart.Chart;
import jatools.component.chart.ChartCanvas;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.Properties;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class ChartPainter extends SimplePainter {
    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param c DOCUMENT ME!
     */
    public void paintComponent(Graphics2D g, Component c) {
    	Rectangle bounds = c.getBounds();
    	
        Chart chart = (Chart) c;
        int width = chart.getWidth();
        int height = chart.getHeight();

        Properties graphProperties = chart.getProperties();

        if (graphProperties.get("chartType") == null) {
            graphProperties.put("chartType", "1");
        }

        ChartCanvas canvas = new ChartCanvas(chart.getChart());

        if ((width > 0) && (height > 0)) {
            g = (Graphics2D) g.create();
            Shape copy = g.getClip();
        	g.clip(bounds);
            g.translate(chart.getX(), chart.getY());

            canvas.setSize(width, height);
            canvas.paintComponent(g);
            g.setClip(copy);
            g.dispose();
        }
        
       
    }
}
