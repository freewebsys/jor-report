package jatools.engine.export.xls;

import jatools.component.ImageStyle;
import jatools.core.view.AbstractView;
import jatools.core.view.CompoundView;
import jatools.core.view.DisplayStyleManager;
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
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Blank;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFeatures;
import jxl.write.WritableCellFormat;
import jxl.write.WritableImage;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class XlsExport extends BasicExport {
    public static final TableCell OCCUPIED_CELL = new TableCell(new TextView(), 0, 0, 1, 1);
    private static TableCell[][] grid;
    public final static float scale_x = 36.57f;
    public final static float scale_y = 15.25f;
    static Map formatsCache = new HashMap();
    boolean onepagemode = false;
    DisplayStyleManager csser;
    private OutputStream os;
    WritableWorkbook w;
    private int lastRow = 0;
    int frozenRow;
    int frozenColumn;
    private WritableSheet sheet;

    /**
     * Creates a new XlsExport object.
     *
     * @param os DOCUMENT ME!
     * @param csser DOCUMENT ME!
     */
    public XlsExport(OutputStream os, DisplayStyleManager csser) {
        WorkbookSettings ws = new WorkbookSettings();

        try {
            w = Workbook.createWorkbook(os, ws);
            this.csser = csser;
            this.csser.setWorkbook(w);
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.os = os;
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

    void split(CompoundView view, int x, int y, ArrayList xs, ArrayList ys, ArrayList ts) {
        AbstractView theView;
        java.awt.Rectangle b;

        if (view instanceof TableView) {
            ((TableView) view).doLayout();
        }

        for (Iterator i = view.getElements().iterator(); i.hasNext();) {
            Object item = i.next();
            if (item instanceof CompoundView && ((CompoundView) item).hasShapes()) {
                item = ((CompoundView) item).asImageView();
            }
            
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

    /**
     * DOCUMENT ME!
     *
     * @param pp DOCUMENT ME!
     * @param pi DOCUMENT ME!
     * @param sheetName DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void export(PageView pp, int pi, String sheetName)
        throws Exception {
        ArrayList xs = new ArrayList();
        ArrayList ys = new ArrayList();
        ArrayList labels = new ArrayList();

        frozenRow = frozenColumn = -1;

        if (sheetName == null) {
            sheetName = "Page " + (pi);
        }

        splitView(pp, xs, ys, labels);

        if (!(this.onepagemode && (this.sheet != null) && ((this.lastRow + ys.size()) < 60000))) {
            sheet = w.createSheet(sheetName, pi);

            sheet.getSettings().setShowGridLines(false);
            sheet.getSettings().setBottomMargin(0);
            sheet.getSettings().setTopMargin(0);
            sheet.getSettings().setLeftMargin(0);
            sheet.getSettings().setRightMargin(0);

            for (int i = 1; i < xs.size(); i++) {
                int width = ((Integer) xs.get(i)).intValue() -
                    ((Integer) xs.get(i - 1)).intValue();

                CellView cv = new CellView();
                cv.setSize(Math.round(scale_x * width));

                sheet.setColumnView(i - 1, cv);
            }

            this.lastRow = 0;
        }

        for (int i = 1; i < ys.size(); i++) {
            int height = ((Integer) ys.get(i)).intValue() - ((Integer) ys.get(i - 1)).intValue();

            sheet.addCell(new Blank(0, i - 1 + lastRow));

            sheet.setRowView(i - 1 + lastRow, Math.round(scale_y * height));
        }

        for (int y = 0; y < grid.length; y++) {
            int x = 0;

            for (x = 0; x < grid[y].length; x++) {
                if (grid[y][x].element != null) {
                    View element = grid[y][x].element;

                    if (element instanceof TextView) {
                        try {
                            exportText(sheet, (TextView) element, grid[y][x], x, y + lastRow);
                        } catch (WriteException e) {
                            e.printStackTrace();
                        }
                    } else {
                        exportImage(sheet, (ImageView) element, grid[y][x], x, y + lastRow);
                    }

                    x += (grid[y][x].colSpan - 1);
                }
            }
        }

        if (frozenRow > -1) {
            sheet.getSettings().setHorizontalFreeze(frozenColumn);
            sheet.getSettings().setVerticalFreeze(frozenRow);
        }

        if (onepagemode) {
            this.lastRow += (ys.size() - 2);
        }
    }

    private void exportImage(WritableSheet sheet, ImageView view, TableCell cell, int x, int y) {
        try {
            ImageStyle css = view.getImageStyle();

            if ((css != null) && (css.getImageObject() != null)) {
                BufferedImage image = new BufferedImage(view.getBounds().width,
                        view.getBounds().height, BufferedImage.TYPE_INT_RGB);

                Graphics2D g = (Graphics2D) image.getGraphics();

                g.setColor(Color.WHITE);
                g.fillRect(0, 0, view.getBounds().width, view.getBounds().height);
                css.paint(g, view.getBounds().width, view.getBounds().height);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageWriter.write(baos, image, ImageWriter.PNG);
                baos.close();

                byte[] bytes = baos.toByteArray();

                WritableImage wi = new WritableImage(x, y, cell.colSpan, cell.rowSpan, bytes);
                sheet.addImage(wi);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void exportText(WritableSheet sheet, TextView text, TableCell gridCell, int x, int y)
        throws WriteException {
        if (gridCell == OCCUPIED_CELL) {
            return;
        }

        WritableCellFormat wcf = csser.getCellFormat(text.getDisplayStyle());

        if ((gridCell.colSpan > 1) || (gridCell.rowSpan > 1)) {
            sheet.addCell(createCell(x, y, text, wcf));

            sheet.mergeCells(x, y, ((x + gridCell.colSpan) - 1), ((y + gridCell.rowSpan) - 1));
        } else if ((text.getText() != null) || !(text.getDisplayStyle()).isBlank()) {
            sheet.addCell(createCell(x, y, text, wcf));
        }

        if (text.getTooltipText() != null) {
            WritableCell cell = sheet.getWritableCell(x, y);
            WritableCellFeatures _wcf = new WritableCellFeatures();

            _wcf.setComment(text.getTooltipText());
            cell.setCellFeatures(_wcf);
        }
    }

    WritableCell createCell(int x, int y, TextView text, WritableCellFormat wcf) {
        Object value = text.getData();

        if (value instanceof Number) {
            return new jxl.write.Number(x, y, ((Number) value).doubleValue(), wcf);
        } else if (value instanceof Date) {
            return new jxl.write.DateTime(x, y, (Date) value, wcf);
        } else {
            return new Label(x, y, text.getText(), wcf);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void close() throws Exception {
        w.write();
        w.close();
        os.close();
    }
}
