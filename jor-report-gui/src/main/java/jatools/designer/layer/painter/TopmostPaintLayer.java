package jatools.designer.layer.painter;


import jatools.designer.ReportPanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JPanel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class TopmostPaintLayer extends JPanel {
    ArrayList cache = new ArrayList();
    private ReportPanel owner;

    /**
     * Creates a new TopmostPaintLayer object.
     *
     * @param owner DOCUMENT ME!
     */
    public TopmostPaintLayer(ReportPanel owner) {
        setOpaque(false);
        this.owner = owner;
    }

    /**
     * DOCUMENT ME!
     *
     * @param painter DOCUMENT ME!
     */
    public void registerPainter(Painter painter) {
        cache.add(painter);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paintComponent(Graphics g) {
        if (owner.getScale() != 1.0f) {
            ((Graphics2D) g).scale(owner.getScale(), owner.getScale());
        }

        for (Iterator it = cache.iterator(); it.hasNext();) {
            Painter painter = (Painter) it.next();
            Graphics2D g2 = (Graphics2D) g.create();

            painter.paint(g2);
            g2.dispose();
        }
    }
}
