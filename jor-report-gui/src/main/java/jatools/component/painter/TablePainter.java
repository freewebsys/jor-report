package jatools.component.painter;

import jatools.component.Component;

import jatools.component.table.Cell;
import jatools.component.table.PowerTable;
import jatools.component.table.TableBase;

import jatools.designer.ReportPanel;

import jatools.designer.peer.TablePeer;

import jatools.util.Util;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.4 $
  */
public class TablePainter extends SimplePainter {
    private static final int CROSS_SIZE = 6;

    /**
    * DOCUMENT ME!
    */
    public static final Color SELECTION_COLOR = new Color(192, 200, 240);

    /**
     * DOCUMENT ME!
     */
    static protected final int SELECTION_FRAME_SIZE = 3;

    /**
     * DOCUMENT ME!
     */
    static protected final Color SELECTION_FRAME_COLOR = Color.BLACK;

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

        paintComponent(g2, c);
        paintChildren(g2, c);

        if ((c.getBorder() != null)) {
            c.getBorder().paint(g2, c.getBounds());
        }

        TablePeer p = getPeer(c);

        if (p.isFocused()) {
            paintSelection(g2, p.getSelection(), (TableBase) c);

            if (c instanceof PowerTable) {
                paintCrossPoint(g2, (PowerTable) c);
            }
        }
    }

    private void paintCrossPoint(Graphics2D g, PowerTable power) {
        Cell cell = power.getLeftHeader().getCell();

        int x = power.getColumnX(cell.colSpan);
        int y = power.getRowY(cell.row);
        Rectangle b = Util.deflateRect(power.getBounds(), power.getInsets());

        Graphics2D gcopy = (Graphics2D) g.create();
        gcopy.translate(b.x, b.y);

        gcopy.setStroke(new BasicStroke(3));

        //  gcopy.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.55f));
        int size = CROSS_SIZE;

        gcopy.setColor(Color.WHITE);
        gcopy.drawLine(x - size, y, x + size, y);
        gcopy.drawLine(x, y - size, x, y + size);

        size -= 2;
        gcopy.setColor(new Color(0, 153, 0));
        gcopy.drawLine(x - size, y, x + size, y);
        gcopy.drawLine(x, y - size, x, y + size);

        gcopy.dispose();
    }

    protected void paintChild(Graphics2D g2, Component child) {
        Painter painter = PainterFactory.getPainter(child.getClass());
        painter.paint(g2, child);
    }

    /**
     * DOCUMENT ME!
     *
     * @param g2 DOCUMENT ME!
     * @param frame DOCUMENT ME!
     */
    public static void paintSelectionFrame(Graphics2D g2, Rectangle frame) {
        g2.setStroke(new BasicStroke(SELECTION_FRAME_SIZE));
        g2.setColor(SELECTION_FRAME_COLOR);
        g2.draw(frame);

        g2.setStroke(new BasicStroke(1));

        int size = SELECTION_FRAME_SIZE + 1;
        int half_size = size / 2;

        int x0 = frame.x + (frame.width / 2);
        int y0 = frame.y + frame.height;
        g2.setColor(SELECTION_FRAME_COLOR);
        g2.fillRect(x0 - half_size, y0 - half_size, size, size);
        g2.setColor(Color.WHITE);
        g2.drawRect(x0 - half_size, y0 - half_size, size, size);

        x0 = frame.x + frame.width;
        y0 = frame.y + (frame.height / 2);
        g2.setColor(SELECTION_FRAME_COLOR);
        g2.fillRect(x0 - half_size, y0 - half_size, size, size);
        g2.setColor(Color.WHITE);
        g2.drawRect(x0 - half_size, y0 - half_size, size, size);

        x0 = frame.x + frame.width;
        y0 = frame.y + frame.height;
        g2.setColor(SELECTION_FRAME_COLOR);
        g2.fillRect(x0 - half_size, y0 - half_size, size, size);
        g2.setColor(Color.WHITE);
        g2.drawRect(x0 - half_size, y0 - half_size, size, size);
    }

    /**
         * DOCUMENT ME!
         *
         * @param g DOCUMENT ME!
         * @param c DOCUMENT ME!
         */
    public void paintSelection(Graphics2D g, Rectangle selection, TableBase c) {
        Rectangle b = Util.deflateRect(c.getBounds(), c.getInsets());

        Graphics2D gcopy = (Graphics2D) g.create();
        gcopy.translate(b.x + c.getPadding().left, b.y + c.getPadding().top);

        Rectangle area = new Rectangle(0, 0, c.getColumnCount(), c.getRowCount());

        if (!area.contains(selection)) {
            selection.setBounds(0, 0, 1, 1);
        }

        Rectangle sel = c.getBounds(selection.y, selection.x, selection.width, selection.height);

        paintSelectionFrame(gcopy, sel);
        gcopy.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.55f));

        sel.grow(-2, -2);
        sel.x++;
        sel.y++;
        sel.width--;
        sel.height--;
        gcopy.setColor(SELECTION_COLOR);
        gcopy.fill(sel);
        gcopy.dispose();
    }

    private TablePeer getPeer(Component c) {
        ReportPanel panel = (ReportPanel) PainterFactory.reportpanel.get();
        TablePeer p = (TablePeer) panel.getComponentPeer(c);

        return p;
    }

    /**
     * DOCUMENT ME!
     *
     * @param g DOCUMENT ME!
     * @param c DOCUMENT ME!
     */
    public void paintComponent(Graphics2D g, Component c) {
        TableBase table = (TableBase) c;

        if (table.isRootTable() && getPeer(c).isGridVisible()) {
            Rectangle b = Util.deflateRect(c.getBounds(), c.getInsets());

            Graphics2D gcopy = (Graphics2D) g.create();

            Color color = c.getBackColor();

            if (color != null) {
                gcopy.setColor(c.getBackColor());
                gcopy.fillRect(b.x, b.y, b.width, b.height);
            }

            paintBackground(g, c, b);

            gcopy.translate(b.x + c.getPadding().left, b.y + c.getPadding().top);

            gcopy.setColor(Color.lightGray);
            gcopy.setStroke(new BasicStroke(1));

            for (int row = 0; row < table.getRowCount(); row++) {
                for (int col = 0; col < table.getColumnCount(); col++) {
                    Component co = table.getCellstore().getComponentOver(row, col);

                    Rectangle r = null;

                    if (co == null) {
                        r = table.getBounds(row, col, 1, 1);
                    } else {
                        Cell cell = co.getCell();

                        if ((cell.row == row) && (cell.column == col)) {
                            r = table.getBounds(co.getCell());
                        }
                    }

                    if (r != null) {
                        gcopy.draw(r);
                    }
                }
            }

            gcopy.dispose();
        }
    }

    //    protected void paintBorder(Graphics2D g2, Component c) {
    //    }
}
