package jatools.designer.export;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import javax.swing.JComponent;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ProgressFan extends JComponent implements MouseListener {
    static Area[] ticker = null;
    protected Thread animation = null;
    protected boolean started = false;
    protected int alphaLevel = 20;
    protected int rampDelay = 300;
    protected float shield = 0.70f;
    protected String text = "";
    protected int barsCount = 14;
    protected float fps = 15.0f;
    protected RenderingHints hints = null;

    /**
     * Creates a new ProgressFan object.
     */
    public ProgressFan() {
        this("");
    }

    /**
     * Creates a new ProgressFan object.
     *
     * @param text DOCUMENT ME!
     */
    public ProgressFan(String text) {
        this(text, 10);
    }

    /**
     * Creates a new ProgressFan object.
     *
     * @param text DOCUMENT ME!
     * @param barsCount DOCUMENT ME!
     */
    public ProgressFan(String text, int barsCount) {
        this(text, barsCount, 0.70f);
    }

    /**
     * Creates a new ProgressFan object.
     *
     * @param text DOCUMENT ME!
     * @param barsCount DOCUMENT ME!
     * @param shield DOCUMENT ME!
     */
    public ProgressFan(String text, int barsCount, float shield) {
        this(text, barsCount, shield, 15.0f);
    }

    /**
     * Creates a new ProgressFan object.
     *
     * @param text DOCUMENT ME!
     * @param barsCount DOCUMENT ME!
     * @param shield DOCUMENT ME!
     * @param fps DOCUMENT ME!
     */
    public ProgressFan(String text, int barsCount, float shield, float fps) {
        this(text, barsCount, shield, fps, 300);
    }

    /**
     * Creates a new ProgressFan object.
     *
     * @param text DOCUMENT ME!
     * @param barsCount DOCUMENT ME!
     * @param shield DOCUMENT ME!
     * @param fps DOCUMENT ME!
     * @param rampDelay DOCUMENT ME!
     */
    public ProgressFan(String text, int barsCount, float shield, float fps, int rampDelay) {
        this.rampDelay = (rampDelay >= 0) ? rampDelay : 0;
        this.shield = (shield >= 0.0f) ? shield : 0.0f;
        this.fps = (fps > 0.0f) ? fps : 10.0f;
        this.barsCount = (barsCount > 0) ? barsCount : 14;

        this.hints = new RenderingHints(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        this.hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        this.hints.put(RenderingHints.KEY_FRACTIONALMETRICS,
            RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        this.setPreferredSize(new Dimension(150, 150));
        start();
    }

    /**
     * DOCUMENT ME!
     */
    public void start() {
        addMouseListener(this);
        setVisible(true);

        if (ticker == null) {
            ticker = buildTicker();
        }

        animation = new Thread(new Animator(true));
        animation.start();
    }

    /**
     * DOCUMENT ME!
     */
    public void stop() {
        if (animation != null) {
            animation.interrupt();
            started = false;
        }
    }

    /**
     * DOCUMENT ME!
     */
    public void interrupt() {
        if (animation != null) {
            animation.interrupt();
            animation = null;

            removeMouseListener(this);
            setVisible(false);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     */
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.translate(getWidth() / 2, getHeight() / 2);
        g2.setRenderingHints(hints);

        for (int i = 0; i < ticker.length; i++) {
            int channel = 160 - (100 / (i + 1));
            g2.setColor(new Color(channel, channel, channel, alphaLevel));
            g2.fill(ticker[i]);
        }
    }

    private Area[] buildTicker() {
        Area[] ticker = new Area[barsCount];
        Point2D.Double center = new Point2D.Double(0, (double) 0);
        double fixedAngle = (2.0 * Math.PI) / ((double) barsCount);

        for (double i = 0.0; i < (double) barsCount; i++) {
            Area primitive = buildPrimitive();

            AffineTransform toCenter = AffineTransform.getTranslateInstance(center.getX(),
                    center.getY());
            AffineTransform toBorder = AffineTransform.getTranslateInstance(10.0, -3.0);
            AffineTransform toCircle = AffineTransform.getRotateInstance(-i * fixedAngle,
                    center.getX(), center.getY());

            AffineTransform toWheel = new AffineTransform();
            toWheel.concatenate(toCenter);
            toWheel.concatenate(toBorder);

            primitive.transform(toWheel);
            primitive.transform(toCircle);

            ticker[(int) i] = primitive;
        }

        return ticker;
    }

    private Area buildPrimitive() {
        Rectangle2D.Double body = new Rectangle2D.Double(3, 0, 15, 6);
        Ellipse2D.Double head = new Ellipse2D.Double(0, 0, 6, 6);
        Ellipse2D.Double tail = new Ellipse2D.Double(15, 0, 6, 6);

        Area tick = new Area(body);
        tick.add(new Area(head));
        tick.add(new Area(tail));

        return tick;
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mouseClicked(MouseEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mousePressed(MouseEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mouseReleased(MouseEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mouseEntered(MouseEvent e) {
    }

    /**
     * DOCUMENT ME!
     *
     * @param e DOCUMENT ME!
     */
    public void mouseExited(MouseEvent e) {
    }

    protected class Animator implements Runnable {
        private boolean rampUp = true;

        protected Animator(boolean rampUp) {
            this.rampUp = rampUp;
        }

        public void run() {
            Point2D.Double center = new Point2D.Double((double) 0, (double) 0);
            double fixedIncrement = (2.0 * Math.PI) / ((double) barsCount);
            AffineTransform toCircle = AffineTransform.getRotateInstance(fixedIncrement,
                    center.getX(), center.getY());

            long start = System.currentTimeMillis();

            if (rampDelay == 0) {
                alphaLevel = rampUp ? 255 : 0;
            }

            started = true;

            boolean inRamp = rampUp;

            while (!Thread.interrupted() && started) {
                if (!inRamp) {
                    for (int i = 0; i < ticker.length; i++)
                        ticker[i].transform(toCircle);
                }

                repaint();

                if (rampUp) {
                    if (alphaLevel < 255) {
                        alphaLevel = (int) ((255 * (System.currentTimeMillis() - start)) / rampDelay);

                        if (alphaLevel >= 255) {
                            alphaLevel = 255;
                            inRamp = false;
                        }
                    }
                } else if (alphaLevel > 0) {
                    alphaLevel = (int) (255 -
                        ((255 * (System.currentTimeMillis() - start)) / rampDelay));

                    if (alphaLevel <= 0) {
                        alphaLevel = 0;

                        break;
                    }
                }

                try {
                    Thread.sleep(inRamp ? 10 : (int) (1000 / fps));
                } catch (InterruptedException ie) {
                    break;
                }

                Thread.yield();
            }

            if (!rampUp) {
                started = false;
                repaint();

                setVisible(false);
                removeMouseListener(ProgressFan.this);
            }
        }
    }
}
