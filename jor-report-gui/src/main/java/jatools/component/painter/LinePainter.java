package jatools.component.painter;

import jatools.component.Component;
import jatools.component.Line;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.2 $
  */
public class LinePainter extends SimplePainter {
    /**
    * 在设计面板中,画出线.
    *
    * @param g 设计面板图形对象.
    */
    public void paintComponent(Graphics2D g, Component c) {
        Line line = (Line) c;
        Color color = line.getForeColor();

        if (color != null) {
            g.setColor(color);

            if (c.getParent() != null) {
                g.clipRect(0, 0, c.getParent().getWidth(), c.getParent().getHeight());
            }

            Stroke stroke = g.getStroke();
            line.setStroke(g);

            g.drawLine(line.getX(), line.getY(), line.getX() + line.getWidth(),
                line.getY() + line.getHeight());

            g.setStroke(stroke);
        }
    }

    protected void paintBorder(Graphics2D g2, Component c) {
    }
}
