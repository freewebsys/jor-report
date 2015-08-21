package jatools.xml.serializer;

import jatools.ReportDocument;
import jatools.component.BarCode;
import jatools.component.ColumnPanel;
import jatools.component.Image;
import jatools.component.Label;
import jatools.component.PagePanel;
import jatools.component.Panel;
import jatools.component.Text;
import jatools.component.chart.Chart;
import jatools.component.chart.PlotData;
import jatools.component.table.HeaderTable;
import jatools.component.table.PowerTable;
import jatools.component.table.RowPanel;
import jatools.component.table.Table;
import jatools.core.view.Border;
import jatools.core.view.DisplayStyle;
import jatools.data.Parameter;
import jatools.data.interval.formula.FormulaIntervalColumn;
import jatools.data.interval.formula.IntervalFormula;
import jatools.data.reader.XPathDatasetReader;
import jatools.data.reader.csv.CsvReader;
import jatools.data.reader.sql.SqlReader;
import jatools.dom.src.ArrayNodeSource;
import jatools.dom.src.CrossIndexNodeSource;
import jatools.dom.src.DatasetNodeSource;
import jatools.dom.src.GroupNodeSource;
import jatools.dom.src.IndexNodeSource;
import jatools.dom.src.RowNodeSource;
import jatools.formatter.DateFormat;
import jatools.formatter.DecimalFormat;
import jatools.xml.XmlBase;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.8 $
  */
public abstract class ContainerSerializer extends AbstractSerializer {
    static Map name2class = new HashMap();
    static Map class2name = new HashMap();
    static final String ITEM_CLASS = "ItemClass";
    static final String LENGTH = "Len";
    static final String ITEM = "Item";

    static {
        registerClassShortName(Border.class, "Border");
        registerClassShortName(Label.class, "Label");
        registerClassShortName(Text.class, "Text");
        registerClassShortName(DatasetNodeSource.class, "DatasetNodeSource");
        registerClassShortName(GroupNodeSource.class, "GroupNodeSource");
        registerClassShortName(IndexNodeSource.class, "IndexNodeSource");
        registerClassShortName(ArrayNodeSource.class, "ArrayNodeSource");
        registerClassShortName(Table.class, "Table");
        registerClassShortName(RowPanel.class, "RowPanel");
        registerClassShortName(RowNodeSource.class, "RowNodeSource");
        registerClassShortName(ColumnPanel.class, "ColumnPanel");
        registerClassShortName(HeaderTable.class, "HeaderTable");
        registerClassShortName(PowerTable.class, "PowerTable");

        registerClassShortName(SqlReader.class, "SqlReader");
        registerClassShortName(CsvReader.class, "CsvReader");
        registerClassShortName(Panel.class, "Panel");
        registerClassShortName(PagePanel.class, "PagePanel");
        registerClassShortName(Image.class, "Image");

        registerClassShortName(CrossIndexNodeSource.class, "CrossIndexNodeSource");

        registerClassShortName(DisplayStyle.class, "DisplayStyle");
        registerClassShortName(DecimalFormat.class, "DecimalFormat");
        registerClassShortName(DateFormat.class, "DateFormat");
        registerClassShortName(ReportDocument.class, "ReportDocument");
        registerClassShortName(Chart.class, "Chart");
        registerClassShortName(BarCode.class, "BarCode");
        registerClassShortName(PlotData.class, "PlotData");
        registerClassShortName(XPathDatasetReader.class, "XPathDatasetReader");
        registerClassShortName(IntervalFormula.class, "IntervalFormula");
        registerClassShortName(FormulaIntervalColumn.class, "FormulaIntervalColumn");
        registerClassShortName(String.class, "String");
        registerClassShortName(Parameter.class, "Parameter");
    }

    /**
     * DOCUMENT ME!
     *
     * @param cls DOCUMENT ME!
     * @param shortName DOCUMENT ME!
     */
    public static void registerClassShortName(Class cls, String shortName) {
        name2class.put(shortName, cls);
        class2name.put(cls, shortName);
    }

    protected static void setClass(Element e, Object object) {
        setClass(e, object, XmlBase.CLASS_ATTRIBUTE_TAG);
    }

    protected static Class getClass(Element e) throws ClassNotFoundException {
        return getClass(e, XmlBase.CLASS_ATTRIBUTE_TAG);
    }

    protected static void setClass(Element e, Object object, String attr) {
        String name = (String) class2name.get(object.getClass());

        if (name == null) {
            name = object.getClass().getName();
        }

        e.setAttribute(attr, name);
    }

    protected static Class getClass(Element e, String attr)
        throws ClassNotFoundException {
        String name = e.getAttribute(attr);
        Class cls = (Class) name2class.get(name);

        if (cls == null) {
            cls = Class.forName(name);
        }

        return cls;
    }
}
