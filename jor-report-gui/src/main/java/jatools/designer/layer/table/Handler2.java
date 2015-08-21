package jatools.designer.layer.table;

import jatools.component.Component;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Float;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class Handler2 {
    Line2D.Float[] lines;
    Component c;

    Handler2(Float[] lines, Component c) {
        this.lines = lines;
        this.c = c;
    }

    boolean hit(int x, int y) {
        for (int i = 0; i < lines.length; i++) {
            Float line = lines[i];

            if (line.ptSegDistSq(x, y) < 13) {
                return true;
            }
        }

        return false;
    }

    void paint(Graphics2D g) {
        for (int i = 0; i < lines.length; i++) {
            Float line = lines[i];

            g.draw(line);
        }
    }
}
