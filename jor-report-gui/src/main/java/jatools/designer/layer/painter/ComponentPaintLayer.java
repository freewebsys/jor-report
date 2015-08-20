package jatools.designer.layer.painter;


import jatools.component.Page;
import jatools.component.painter.PainterFactory;
import jatools.designer.ReportPanel;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ComponentPaintLayer implements Painter {
    static Border b = new MatteBorder(1, 1, 2, 2, Color.black);
    Page report;
    int gridSize;
    int w;
    int h;
    private JComponent owner;
    private boolean borderVisible;
    private ReportPanel reportpanel;

    /**
     * Creates a new ComponentPaintLayer object.
     *
     * @param reportpanel DOCUMENT ME!
     * @param owner DOCUMENT ME!
     * @param borderVisible DOCUMENT ME!
     */
    public ComponentPaintLayer(ReportPanel reportpanel, JComponent owner, boolean borderVisible) {
        this.reportpanel = reportpanel;
        this.owner = owner;
        this.borderVisible = borderVisible;
    }

    /**
     * DOCUMENT ME!
     *
     * @param report DOCUMENT ME!
     */
    public void setReport(Page report) {
        this.report = report;
        owner.repaint();
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paint(Graphics2D g) {
        int rw = report.getWidth();

        int rh = report.getHeight();

        if ((rw != w) || (rh != h)) {
            w = rw;
            h = rh;
            owner.revalidate();
            owner.repaint();
        }

        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.white);
        g2.fillRect(0, 0, rw + 5, rh + 5);
        g2.drawRect(2, 2, rw, rh);

        if (report != null) {
            PainterFactory.reportpanel.set(reportpanel);
            PainterFactory.getPainter(report.getClass()).paint(g2, report);
        }

        if (borderVisible) {
            b.paintBorder(owner, g2, 0, 0, rw + 1, rh + 1);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getGridSize() {
        return gridSize;
    }

    boolean isGridsVisible() {
        return gridSize > 0;
    }

    /**
     * DOCUMENT ME!
     *
     * @param gridSize DOCUMENT ME!
     */
    public void setGridSize(int gridSize) {
        this.gridSize = gridSize;
    }
}
