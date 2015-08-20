package jatools.component.painter;

import jatools.component.BackgroundImageStyle;
import jatools.component.Component;
import jatools.component.ComponentComparator;
import jatools.core.view.Border;
import jatools.designer.Main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;



/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class SimplePainter implements Painter {
    static Border frameBorder = new Border(1, Color.LIGHT_GRAY);

    /**
     * DOCUMENT ME!
     *
     * @param g2 DOCUMENT ME!
     * @param c DOCUMENT ME!
     */
    public void paintChildren(Graphics2D g2, Component c) {
        if ((c.getChildren() == null) || c.getChildren().isEmpty()) {
            return;
        }

        Rectangle bounds = c.getBounds();
        Graphics2D gcopy = (Graphics2D) g2.create();

        gcopy.translate(bounds.x, bounds.y);

        Insets padding = c.getPadding();

        if (padding != null) {
            gcopy.translate(padding.left, padding.top);
        }

        ArrayList childrenCopy = (ArrayList) c.getChildren();

        Comparator comparator = ComponentComparator.getInstance();
        Collections.sort(childrenCopy, comparator);

        Iterator iterator = childrenCopy.iterator();

       
        while (iterator.hasNext()) {
            Component component = (Component) iterator.next();
            paintChild(gcopy, component);
        }

        gcopy.dispose();
    }

    protected void paintChild(Graphics2D g2, Component child) {
        Painter painter = PainterFactory.getPainter(child.getClass());
        painter.paint(g2, child);
    }

    protected void paintBackground(Graphics2D g, Component c, Rectangle b) {
        String backgroundStyle = c.getBackgroundImageStyle();

        if (backgroundStyle != null) {
            BackgroundImageStyle bi = (BackgroundImageStyle) Main.getInstance().getActiveEditor()
                                                                 .getImageCacher()
                                                                 .getImageStyle(backgroundStyle);

            if (bi == null) {
                bi = new BackgroundImageStyle(backgroundStyle);

                Main.getInstance().getActiveEditor().getImageLoader().load(bi);
                BackgroudImagePainter.getDefaults().paint(g, bi, b);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param g2 DOCUMENT ME!
     * @param c DOCUMENT ME!
     */
    public void paint(Graphics2D g2, Component c) {
        if (!c.isValid()) {
            c.validate();
        }

        try {
			paintComponent(g2, c);
			paintChildren(g2, c);
			paintBorder(g2, c);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    protected void paintBorder(Graphics2D g2, Component c) {
        if ((c.getBorder() != null)) {
            c.getBorder().paint(g2, c.getBounds());
        } else if ((c.getCell() == null) && isFrameBorderRequired()) {
            frameBorder.paint(g2, c.getBounds());
        }
    }

    protected boolean isFrameBorderRequired() {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g2 DOCUMENT ME!
     * @param c DOCUMENT ME!
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
        } catch (Exception ex) {
        }
    }
}
