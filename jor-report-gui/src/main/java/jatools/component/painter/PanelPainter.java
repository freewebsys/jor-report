package jatools.component.painter;

import jatools.component.Component;
import jatools.component.Panel;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class PanelPainter extends SimplePainter {
    static final int PADDING_FLAG_SIZE = 3;

    /**
     * DOCUMENT ME!
     *
     * @param g2 DOCUMENT ME!
     */
    public void paintComponent(Graphics2D g2, Component c) {
        try {
            Rectangle b = c.getBounds();

            Color color = c.getBackColor();

            if (color != null) {
                g2.setColor(color);
                g2.fill(b);
            }

            paintBackground(g2, c, b);

            //            g2.setColor( Color.BLUE );
            //
            //            int x0 = b.x + is.left;
            //            int y0 = b.y + is.top;
            //            int x1 = (b.x + b.width) - is.right;
            //            int y1 = (b.y + b.height) - is.top;
            //
            
            //            g2.drawLine(x0, y0, x0 + PADDING_FLAG_SIZE, y0);
            //            g2.drawLine(x0, y0, x0, y0 + PADDING_FLAG_SIZE);
            
            //            g2.drawLine(x0, y1, x0 + PADDING_FLAG_SIZE, y1);
            //            g2.drawLine(x0, y1, x0, y1 - PADDING_FLAG_SIZE);
            
            //            g2.drawLine(x1, y1, x1 - PADDING_FLAG_SIZE, y1);
            //            g2.drawLine(x1, y1, x1, y1 - PADDING_FLAG_SIZE);
            
            //            g2.drawLine(x1, y0, x1 - PADDING_FLAG_SIZE, y0);
            //            g2.drawLine(x1, y0, x1, y0 + PADDING_FLAG_SIZE);
            Panel p = (Panel) c;

            if (c.getClass() == Panel.class && p.getNodePath() != null ) {
                g2.setColor(new Color(200, 200, 200));
                g2.drawString("<"+p.getNodePath()+">", b.x + 3, (b.y + b.height) - 2);
            }
        } catch (Exception ex) {
        }
    }

    //    protected void paintBorder(Graphics2D g2, Component c) {
    //        if ((c.getCell() == null)) {
    //            frameBorder.paint(g2, c.getBounds());
    //        }
    //    }
}
