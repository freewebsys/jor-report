package jatools.designer.property.editor;

import jatools.core.view.Border;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class BorderPanel extends JPanel {
    private final static int LEFT = 0;
    private final static int RIGHT = 1;
    private final static int TOP = 2;
    private final static int BOTTOM = 3;
    Rectangle topRec = new Rectangle(0, 0, 100, 20);
    Rectangle leftRec = new Rectangle(0, 0, 20, 100);
    Rectangle bottomRec = new Rectangle(0, 80, 100, 20);
    Rectangle rightRec = new Rectangle(80, 0, 20, 100);
    private Border border;

    /**
     * Creates a new BorderPanel object.
     */
    public BorderPanel() {
        addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    Point p = (Point) e.getPoint();

                    if (topRec.contains(p)) {
                        hit(TOP);
                    } else if (leftRec.contains(p)) {
                        hit(LEFT);
                    } else if (bottomRec.contains(p)) {
                        hit(BOTTOM);
                    } else if (rightRec.contains(p)) {
                        hit(RIGHT);
                    }
                }
            });

        setBackground(Color.WHITE);
    }

    protected void hit(int side) {
        this.firePropertyChange("hitside", -1, side);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        initLine(g);

        if (this.isEnabled()) {
            if (border != null) {
                border.paint(g2, new Rectangle(10, 10, getWidth() - 20, getHeight() - 20));
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void initLine(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(new Color(210, 208, 200));

        int width = this.getWidth();
        int height = this.getHeight();

        g2.drawLine(10, 2, 10, 10);
        g2.drawLine(2, 10, 10, 10);

        g2.drawLine(width - 10, 2, width - 10, 10);
        g2.drawLine(width - 10, 10, width - 2, 10);

        g2.drawLine(2, height - 10, 10, height - 10);
        g2.drawLine(10, height - 10, 10, height - 2);

        g2.drawLine(width - 10, height - 10, width - 2, height - 10);
        g2.drawLine(width - 10, height - 10, width - 10, height - 2);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Border getBorder2() {
        return border;
    }

    /**
     * DOCUMENT ME!
     *
     * @param border DOCUMENT ME!
     */
    public void setBorder2(Border border) {
        Border old = this.border;
        this.border = border;
        this.firePropertyChange("border2", old, this.border);
        this.repaint();
    }
}
