/*
 * Created on 2004-7-2
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jatools.engine.export.doc;

import jatools.PageFormat;
import jatools.component.ImageStyle;
import jatools.core.view.AbstractView;
import jatools.core.view.Border;
import jatools.core.view.CompoundView;
import jatools.core.view.ImageView;
import jatools.core.view.PageView;
import jatools.core.view.TextView;
import jatools.core.view.TransformView;
import jatools.core.view.View;
import jatools.engine.export.BasicExport;
import jatools.engine.export.TableCell;
import jatools.engine.layout.TableView;
import jatools.imageio.ImageWriter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.rtf.RtfTableCell;
import com.lowagie.text.rtf.RtfWriter;


/**
 * @author java
 *
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class RtfExport extends BasicExport {
    static DefaultFontMapper mapper;
    static HashMap baseFonts = new HashMap();
    public static final TableCell OCCUPIED_CELL = new TableCell(new TextView(), 0, 0, 1, 1);
    private static TableCell[][] grid;
    


    RtfWriter writer;
    Document document;
    private OutputStream os;
    int i = 0;

    /**
     * Creates a new ZRtfExport object.
     *
     * @param os DOCUMENT ME!
     * @param size DOCUMENT ME!
     */
    public RtfExport(OutputStream os, Dimension size) {
        if (size != null) {
            document = new Document(new com.lowagie.text.Rectangle(size.width, size.height), 0f,
                    0f, 0f, 0);
        } else {
            document = new Document();
        }

        this.os = os;
    }

    /**
     * Creates a new ZRtfExport object.
     *
     * @param os DOCUMENT ME!
     */
    public RtfExport(OutputStream os) {
        this(os, null);
    }

    /**
     * DOCUMENT ME!
     */
    public void close() {
        writer.close();
    }

    /**
     * DOCUMENT ME!
     *
     * @param pp DOCUMENT ME!
     * @param i0 DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void export(PageView pp, int i0) throws Exception {
        PageFormat pf = pp.getPageFormat();

        int w = (int) pf.getWidth();

        if (writer == null) {
            writer = RtfWriter.getInstance(document, os);

            document.open();
        }

        ArrayList xs = new ArrayList();
        ArrayList ys = new ArrayList();
        ArrayList labels = new ArrayList();
        splitView(pp, xs, ys, labels);

        Table table = new Table(xs.size() - 1, ys.size() - 1);

        int xx = 0;

        float[] widths = new float[xs.size() - 1];

        for (int j = 1; j < xs.size(); j++) {
            widths[j - 1] = ((Integer) xs.get(j)).intValue() - xx;
            xx = ((Integer) xs.get(j)).intValue();
        }

        table.setWidths(widths);

        int yy = 0;
        float[] heights = new float[ys.size() - 1];

        for (int j = 1; j < ys.size(); j++) {
            heights[j - 1] = ((Integer) ys.get(j)).intValue() - yy;
            yy = ((Integer) ys.get(j)).intValue();
        }

        table.setHeights(heights);

        //	table.setDefaultCellBorder(0 );
        table.setAutoFillEmptyCells(false);

        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[y].length; x++) {
                if (grid[y][x].element == null) {
                    // cell is empty, autofill will create an
                    // empty cell here ...
                    final Cell cell = new Cell();
                    cell.setBorderWidth(0);
                    cell.setRowspan(1);
                    cell.setColspan(1);

                    table.addCell(cell, y, x);
                } else {
                    Cell cell = null;

                    if (grid[y][x].element instanceof jatools.core.view.ImageView) {
                        String path = this.exportImage((ImageView) grid[y][x].element);
                        cell = new Cell();
                        if (path != null) {
                            Image image = Image.getInstance(path);
                            //cell = new Cell();
                            cell.addElement(image);
                        }
                    } else {
                        cell = new Cell();

                        TextView tv = (TextView) grid[y][x].element;
                        String txt = tv.getText();

                        if (txt == null) {
                            txt = ""; //
                        }

                        final Chunk chunk = new Chunk(txt);

                        setFont(chunk, tv);

                        final Paragraph paragraph = new Paragraph();
                        paragraph.add(chunk);

                        cell.setHorizontalAlignment(this.toCellHorizontalAlignment(
                                tv.getHorizontalAlignment()));
                        cell.setVerticalAlignment(this.toCellVerticalAlignment(
                                tv.getVerticalAlignment()));
                        cell.addElement(paragraph);
                        
                        //cell.setNoWrap(true);//!tv.isWordwrap());
                       
                        RtfTableCell cella ;


                        Color bkColor = tv.getBackColor();

                        if (bkColor != null) {
                            cell.setBackgroundColor(bkColor);
                        }
                    }

                    if (((AbstractView) grid[y][x].element).getBorder() != null) {
                        float border = 0;
                        Color borderColor = null;
                        Border b = ((AbstractView) grid[y][x].element).getBorder();

                        if (b.getTopStyle() != null) {
                            border = b.getTopStyle().getThickness();
                            borderColor = b.getTopStyle().getColor();
                        } else if (b.getLeftStyle() != null) {
                            border = b.getLeftStyle().getThickness();
                            borderColor = b.getLeftStyle().getColor();
                        } else if (b.getBottomStyle() != null) {
                            border = b.getBottomStyle().getThickness();
                            borderColor = b.getBottomStyle().getColor();
                        } else if (b.getRightStyle() != null) {
                            border = b.getRightStyle().getThickness();
                            borderColor = b.getRightStyle().getColor();
                        }

                        cell.setBorderWidth(border);
                        cell.setBorderColor(borderColor);
                    } else {
                        cell.setBorderWidth(0);
                    }

                    cell.setRowspan(grid[y][x].rowSpan);
                    cell.setColspan(grid[y][x].colSpan);

                    table.addCell(cell, y, x);
                }
            }
        }

        table.setWidth((100.0f * ((Integer) xs.get(xs.size() - 1)).intValue()) / w);
        table.setAlignment(Element.ALIGN_LEFT);

        if (i > 0) {
            document.newPage();
        }

        document.add(table);
        i++;
    }

    private String exportImage(ImageView view) {
        ImageStyle css = view.getImageStyle();

        if ((css != null) && (css.getImageObject() != null)) {
            BufferedImage image = new BufferedImage(view.getBounds().width,
                    view.getBounds().height, BufferedImage.TYPE_INT_RGB);

            Graphics2D g = (Graphics2D) image.getGraphics();

            g.setColor(Color.WHITE);
            g.fillRect(0, 0, view.getBounds().width, view.getBounds().height);
            css.paint(g, view.getBounds().width, view.getBounds().height);

            File f = null;

            try {
                f = File.createTempFile("jatoo", ".png");

                OutputStream fos = new FileOutputStream(f);
                ImageWriter.write(fos, image, ImageStyle.PNG);
                fos.close();

                return f.getAbsolutePath();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        } else {
            return null;
        }
    }

    /**
     * @param chunk
     * @param tv
     */
    private void setFont(Chunk chunk, TextView tv) {
        java.awt.Font tf = tv.getFont();

        if (tf != null) {
            int style = Font.NORMAL;

            if (tf.isBold()) {
                style += Font.BOLD;
            }

            if (tf.isItalic()) {
                style += Font.ITALIC;
            }

            /*
             * int family = Font.HELVETICA; if (font.isCourier()) { family =
             * Font.COURIER; } else if (font.isSerif()) { family =
             * Font.TIMES_ROMAN; }
             */
            chunk.setFont(new Font(tf.getName(), Math.round(tf.getSize() / 1.33f), style,
                    tv.getForeColor()));

            //  chunk.setFont(new Font(baseFont, tf.getSize() , style,
            // tv.getForeColor ()));
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param f DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    private static BaseFont getCachedBaseFont(java.awt.Font f) {
        synchronized (baseFonts) {
            BaseFont bf = (BaseFont) baseFonts.get(f.getFontName());

            if (bf == null) {
                bf = mapper.awtToPdf(f);
                baseFonts.put(f.getFontName(), bf);
            }

            return bf;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param tvAlign DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    int toCellHorizontalAlignment(int tvAlign) {
        int horizontalAlignment = Cell.ALIGN_LEFT;

        switch (tvAlign) {
        case TextView.LEFT: {
            horizontalAlignment = Cell.ALIGN_LEFT;

            break;
        }

        case TextView.CENTER: {
            horizontalAlignment = Cell.ALIGN_CENTER;

            break;
        }

        case TextView.RIGHT: {
            horizontalAlignment = Cell.ALIGN_RIGHT;

            break;
        }

        default:
            horizontalAlignment = Cell.ALIGN_LEFT;
        }

        return horizontalAlignment;
    }

    /**
     * DOCUMENT ME!
     *
     * @param tvAlign DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    int toCellVerticalAlignment(int tvAlign) {
        short verticalAlignment = Cell.ALIGN_TOP;

        switch (tvAlign) {
        case TextView.TOP: {
            verticalAlignment = Cell.ALIGN_TOP;

            break;
        }

        case TextView.MIDDLE: {
            verticalAlignment = Cell.ALIGN_MIDDLE;

            break;
        }

        case TextView.BOTTOM: {
            verticalAlignment = Cell.ALIGN_BOTTOM;

            break;
        }

        default:
            verticalAlignment = Cell.ALIGN_TOP;
        }

        return verticalAlignment;
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param xs DOCUMENT ME!
     * @param ys DOCUMENT ME!
     * @param ts DOCUMENT ME!
     */
    public void splitView2(PageView view, ArrayList xs, ArrayList ys, ArrayList ts) {
        int x = 0;
        int y = 0;

        xs.add(new Integer(0));
        ys.add(new Integer(0));

        split(view, x, y, xs, ys, ts);

        Collections.sort(xs);
        Collections.sort(ys);

        int xCellCount = xs.size() - 1;
        int yCellCount = ys.size() - 1;

        grid = new TableCell[yCellCount][xCellCount];

        for (int j = 0; j < yCellCount; j++) {
            for (int i = 0; i < xCellCount; i++) {
                grid[j][i] = new TableCell(null,
                        ((Integer) xs.get(i + 1)).intValue() - ((Integer) xs.get(i)).intValue(),
                        ((Integer) ys.get(j + 1)).intValue() - ((Integer) ys.get(j)).intValue(), 1,
                        1);
            }
        }

        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = 0;
        int xi = 0;
        int yi = 0;

        boolean isOverlap = false;

        for (int i = ts.size() - 1; i >= 0; i--) {
            View v = (View) ts.get(i);

            if (v instanceof TextView) {
                TextView element = (TextView) ts.get(i);

                Rectangle r = element.getBounds();

                x1 = xs.indexOf(new Integer(r.x + x));
                y1 = ys.indexOf(new Integer(r.y + y));
                x2 = xs.indexOf(new Integer(x + r.x + r.width));
                y2 = ys.indexOf(new Integer(y + r.y + r.height));

                isOverlap = false;
                yi = y1;

                while ((yi < y2) && !isOverlap) {
                    xi = x1;

                    while ((xi < x2) && !isOverlap) {
                        if (grid[yi][xi].element != null) {
                            isOverlap = true;
                        }

                        xi++;
                    }

                    yi++;
                }

                if (!isOverlap) {
                    yi = y1;

                    while (yi < y2) {
                        xi = x1;

                        while (xi < x2) {
                            grid[yi][xi] = OCCUPIED_CELL;
                            xi++;
                        }

                        yi++;
                    }

                    grid[y1][x1] = new TableCell(element, r.width, r.height, x2 - x1, y2 - y1);
                }
            } else if (v instanceof TransformView) {
                TransformView tv = (TransformView) v;
                x -= tv.x;
                y -= tv.y;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param xs DOCUMENT ME!
     * @param ys DOCUMENT ME!
     * @param ts DOCUMENT ME!
     */
    public void splitView(PageView view, ArrayList xs, ArrayList ys, ArrayList ts) {
        int x = 0;
        int y = 0;

        xs.add(new Integer(0));
        ys.add(new Integer(0));

        split(view, x, y, xs, ys, ts);

        Collections.sort(xs);
        Collections.sort(ys);

        int xCellCount = xs.size() - 1;
        int yCellCount = ys.size() - 1;

        grid = new TableCell[yCellCount][xCellCount];

        for (int j = 0; j < yCellCount; j++) {
            for (int i = 0; i < xCellCount; i++) {
                grid[j][i] = new TableCell(null,
                        ((Integer) xs.get(i + 1)).intValue() - ((Integer) xs.get(i)).intValue(),
                        ((Integer) ys.get(j + 1)).intValue() - ((Integer) ys.get(j)).intValue(), 1,
                        1);
            }
        }

        Object[] xa = xs.toArray();
        Object[] ya = ys.toArray();

        int x1 = 0;
        int y1 = 0;
        int x2 = 0;
        int y2 = 0;
        int xi = 0;
        int yi = 0;

        boolean isOverlap = false;

        for (int i = ts.size() - 1; i >= 0; i--) {
            View v = (View) ts.get(i);

            if (v instanceof TextView || v instanceof ImageView) {
                AbstractView element = (AbstractView) ts.get(i);

                Rectangle r = element.getBounds();

                x1 = Arrays.binarySearch(xa, new Integer(r.x + x));
                y1 = Arrays.binarySearch(ya, new Integer(r.y + y));
                x2 = Arrays.binarySearch(xa, new Integer(x + r.x + r.width));
                y2 = Arrays.binarySearch(ya, new Integer(y + r.y + r.height));

                //				x1 = xs.indexOf(new Integer(r.x + x));
                //				y1 = ys.indexOf(new Integer(r.y + y));
                //				x2 = xs.indexOf(new Integer(x + r.x + r.width));
                //				y2 = ys.indexOf(new Integer(y + r.y + r.height));
                isOverlap = false;
                yi = y1;

                while ((yi < y2) && !isOverlap) {
                    xi = x1;

                    while ((xi < x2) && !isOverlap) {
                        if (grid[yi][xi].element != null) {
                            isOverlap = true;
                        }

                        xi++;
                    }

                    yi++;
                }

                if (!isOverlap) {
                    yi = y1;

                    while (yi < y2) {
                        xi = x1;

                        while (xi < x2) {
                            grid[yi][xi] = OCCUPIED_CELL;
                            xi++;
                        }

                        // rows[yi] = sheet.createRow(yi);
                        yi++;
                    }

                    grid[y1][x1] = new TableCell(element, r.width, r.height, x2 - x1, y2 - y1);
                }
            } else if (v instanceof TransformView) {
                TransformView tv = (TransformView) v;
                x -= tv.x;
                y -= tv.y;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param view DOCUMENT ME!
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     * @param xs DOCUMENT ME!
     * @param ys DOCUMENT ME!
     * @param ts DOCUMENT ME!
     */
    void split(CompoundView view, int x, int y, ArrayList xs, ArrayList ys, ArrayList ts) {
        AbstractView theView;
        java.awt.Rectangle b;

        if (view instanceof TableView) {
            ((TableView) view).doLayout();
        }

        for (Iterator i = view.getElements().iterator(); i.hasNext();) {
            Object item = i.next();

//            if (item instanceof CompoundView ) {
//                item = ((CompoundView) item).asImageView();
//            }

            if ((item instanceof TextView) || item instanceof ImageView) {
                theView = (AbstractView) item;

                b = theView.getBounds();

                int bx = b.x + x;
                int by = b.y + y;

                ts.add(theView);

                Integer x0 = new Integer(bx);

                if (!xs.contains(x0)) {
                    xs.add(x0);
                }

                Integer x1 = new Integer((bx + b.width));

                if (!xs.contains(x1)) {
                    xs.add(x1);
                }

                Integer y0 = new Integer(by);

                if (!ys.contains(y0)) {
                    ys.add(y0);
                }

                Integer y1 = new Integer(by + b.height);

                if (!ys.contains(y1)) {
                    ys.add(y1);
                }
            } else if (item instanceof TransformView) {
                TransformView tv = (TransformView) item;
                x += tv.x;
                y += tv.y;
                ts.add(item);
            } else if (item instanceof CompoundView) {
                b = ((CompoundView) item).getBounds();
                ts.add(new TransformView(b.x, b.y));
                split((CompoundView) item, x + b.x, y + b.y, xs, ys, ts);

                ts.add(new TransformView(-b.x, -b.y));
            }
        }
    }
}
