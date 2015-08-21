package jatools.designer.ruler;

import jatools.designer.App;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.JComponent;


public class Ruler extends JComponent {
    /**
     * DOCUMENT ME!
     */
    public static final int INCH = Toolkit.getDefaultToolkit().getScreenResolution();

    /**
     * DOCUMENT ME!
     */
    public static final int HORIZONTAL = 0;

    /**
     * DOCUMENT ME!
     */
    public static final int VERTICAL = 1;

    /**
     * DOCUMENT ME!
     */
    public static final int SIZE = 10;

    /**
     * DOCUMENT ME!
     */
    public int orientation;

    /**
     * DOCUMENT ME!
     */
    public boolean isMetric;
    private int increment;
    private int units;

    /**
     * Creates a new Ruler object.
     *
     * @param o DOCUMENT ME!
     * @param m DOCUMENT ME!
     */
    public Ruler(int o, boolean m) {
        orientation = o;
        isMetric = m;
        setIncrementAndUnits();
        setBorder(BorderFactory.createLoweredBevelBorder());
    }

    /**
     * DOCUMENT ME!
     *
     * @param isMetric DOCUMENT ME!
     */
    public void setIsMetric(boolean isMetric) {
        this.isMetric = isMetric;
        setIncrementAndUnits();
        repaint();
    }

    private void setIncrementAndUnits() {
        if (isMetric) {
            units = (int) ((double) INCH / (double) 2.54);
            increment = units;
        } else {
            units = INCH;
            increment = units / 2;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isMetric() {
        return this.isMetric;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getIncrement() {
        return increment;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ph DOCUMENT ME!
     */
    public void setPreferredHeight(int ph) {
        setPreferredSize(new Dimension(SIZE, ph));
    }

    /**
     * DOCUMENT ME!
     *
     * @param pw DOCUMENT ME!
     */
    public void setPreferredWidth(int pw) {
        setPreferredSize(new Dimension(pw, SIZE));
    }

    protected void paintComponent(Graphics g) {
        Rectangle drawHere = g.getClipBounds();

        g.setColor(Color.white);
        g.fillRect(drawHere.x, drawHere.y, drawHere.width, drawHere.height);

        g.setFont(new Font(App.messages.getString("res.21"), Font.PLAIN, 9));
        g.setColor(Color.black);

        int end = 0;
        int start = 0;
        int tickLength = 0;
        String text = null;

        if (orientation == HORIZONTAL) {
            start = (drawHere.x / increment) * increment;
            end = (((drawHere.x + drawHere.width) / increment) + 1) * increment;
        } else {
            start = (drawHere.y / increment) * increment;
            end = (((drawHere.y + drawHere.height) / increment) + 1) * increment;
        }

        for (int i = start; i < end; i += 10) {
            if (((i - start) % 40) == 0) {
                tickLength = 4;
                text = Integer.toString((i - start) / 40);
            } else {
                tickLength = 1;
                text = null;
            }

            if (tickLength != 0) {
                if (orientation == HORIZONTAL) {
                    g.drawLine(i, SIZE - 1, i, SIZE - tickLength - 3);

                    if (text != null) {
                        g.drawString(text, i - 3, 18);
                    }
                } else {
                    g.drawLine(SIZE - 3, i, SIZE - tickLength, i);

                    if (text != null) {
                        g.drawString(text, 11, i + 3);
                    }
                }
            }
        }
    }
}
