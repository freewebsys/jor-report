package jatools.component.painter;

import jatools.component.BarCode;
import jatools.component.ColumnPanel;
import jatools.component.Image;
import jatools.component.Label;
import jatools.component.Line;
import jatools.component.Page;
import jatools.component.PagePanel;
import jatools.component.Panel;
import jatools.component.Text;

import jatools.component.chart.Chart;

import jatools.component.table.FillRowPanel;
import jatools.component.table.HeaderTable;
import jatools.component.table.PowerTable;
import jatools.component.table.RowPanel;
import jatools.component.table.Table;
import jatools.component.table.TableBase;

import jatools.designer.App;

import java.util.HashMap;
import java.util.Map;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class PainterFactory {
    /**
     * DOCUMENT ME!
     */
    public final static ThreadLocal reportpanel = new ThreadLocal();
    static Map painters = new HashMap();

    static {
        Painter p = new TablePainter();
        registerPainter(TableBase.class, p);
        registerPainter(Table.class, p);

        registerPainter(PowerTable.class, p);
        registerPainter(HeaderTable.class, p);

        registerPainter(Page.class, new SimplePainter());

        registerPainter(Chart.class, new ChartPainter());
        registerPainter(BarCode.class, new BarCodePainter());
        registerPainter(Image.class, new ImagePainter());
        registerPainter(Label.class, p = new LabelPainter());
        registerPainter(Text.class, p);
        registerPainter(Line.class, new LinePainter());

        p = new PanelPainter();
        registerPainter(Panel.class, p);
        registerPainter(PagePanel.class, p);

        //        registerPainter(Oval.class, new OvalPainter());
        registerPainter(RowPanel.class, new RowPanelPainter());
        registerPainter(FillRowPanel.class, new RowPanelPainter());
        registerPainter(ColumnPanel.class, new ColumnListPainter());
    }

    /**
     * DOCUMENT ME!
     *
     * @param class1 DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Painter getPainter(Class class1) {
        Painter p = (Painter) painters.get(class1);

        if (p == null) {
            throw new IllegalArgumentException(App.messages.getString("res.648") +
                class1.getName());
        }

        return p;
    }

    /**
     * DOCUMENT ME!
     *
     * @param c DOCUMENT ME!
     * @param painter DOCUMENT ME!
     */
    public static void registerPainter(Class c, Painter painter) {
        painters.put(c, painter);
    }
}
