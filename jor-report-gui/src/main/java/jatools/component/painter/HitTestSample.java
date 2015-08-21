package jatools.component.painter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.font.TextAttribute;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.geom.Point2D;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JPanel;


/**
 * DOCUMENT ME!
 * 
 * @author $author$
 * @version $Revision: 1.3 $
 */
public class HitTestSample extends JPanel {
    private static final Color STRONG_CARET_COLOR = Color.red;
    private static final Color WEAK_CARET_COLOR = Color.black;
    private static final FontRenderContext DEFAULT_FRC = new FontRenderContext(null, false, false);
    private static final Hashtable map = new Hashtable();

    static {
        map.put(TextAttribute.SIZE, new Float(18.0));
    }

    private static AttributedString helloWorld = new AttributedString("Java <br>Source and Support.",
            map);
    private TextLayout textLayout;
    private int insertionIndex;

    /**
	 * Creates a new HitTestSample object.
	 */
    public HitTestSample() {
        AttributedCharacterIterator text = helloWorld.getIterator();
        // Create a new TextLayout from the given text.
        textLayout = new TextLayout(text, DEFAULT_FRC);

        // Initilize insertionIndex.
        insertionIndex = 0;

        addMouseListener(new HitTestMouseListener());
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @return DOCUMENT ME!
	 */
    public Dimension getPreferredSize() {
        return new Dimension(400, 250);
    }

    private Point2D computeLayoutOrigin() {
        Dimension size = getPreferredSize();
        Point2D.Float origin = new Point2D.Float();

        origin.x = (float) (size.width - textLayout.getAdvance()) / 2;
        origin.y = (float) (size.height - textLayout.getDescent() + textLayout.getAscent()) / 2;

        return origin;
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param g
	 *            DOCUMENT ME!
	 */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(Color.white);
        
        Font f = new Font("Arial",0,30);
       // System.out.println(g.getFontMetrics( f).getHeight()) ;
        
//        System.out.println(g.getFontMetrics( f).getAscent() ) ;
//        System.out.println(g.getFontMetrics( f).getDescent() ) ;
//        System.out.println(g.getFontMetrics( f).getLeading ()) ;
        

        Graphics2D graphics2D = (Graphics2D) g;

        Point2D origin = computeLayoutOrigin();

        graphics2D.translate(origin.getX(), origin.getY());

        // Draw textLayout.
        textLayout.draw(graphics2D, 0, 0);

        // Retrieve caret Shapes for insertionIndex.
        Shape[] carets = textLayout.getCaretShapes(insertionIndex);

        graphics2D.setColor(STRONG_CARET_COLOR);
        graphics2D.draw(carets[0]);

        if (carets[1] != null) {
            graphics2D.setColor(WEAK_CARET_COLOR);
            graphics2D.draw(carets[1]);
        }
    }

    /**
	 * DOCUMENT ME!
	 * 
	 * @param args
	 *            DOCUMENT ME!
	 */
    public static void main(String[] args) {
        JFrame f = new JFrame("HitTestSample");
        HitTestSample demo = new HitTestSample();
        f.getContentPane().add(demo, "Center");

        f.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });

        f.setSize(new Dimension(400, 250));
        f.setVisible(true);
    }

    private class HitTestMouseListener extends MouseAdapter {
        public void mouseClicked(MouseEvent e) {
            Point2D origin = computeLayoutOrigin();

            // Compute the mouse click location relative to
            // textLayout's origin.
            float clickX = (float) (e.getX() - origin.getX());
            float clickY = (float) (e.getY() - origin.getY());

            // Get the character position of the mouse click.
            TextHitInfo currentHit = textLayout.hitTestChar(clickX, clickY);
            insertionIndex = currentHit.getInsertionIndex();

            // Repaint the Component so the new caret(s) will be displayed.
            repaint();
        }
    }
}
