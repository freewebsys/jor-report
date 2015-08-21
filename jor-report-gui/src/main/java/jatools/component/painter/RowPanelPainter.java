package jatools.component.painter;

import jatools.component.Component;

import java.awt.Graphics2D;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class RowPanelPainter extends PanelPainter {
    /**
    * DOCUMENT ME!
    *
    * @param g2 DOCUMENT ME!
    */
    public void paint(Graphics2D g2, Component c) {
        paintComponent(g2, c);
        paintChildren(g2, c);
        paintBorder(g2, c);
    }
}
