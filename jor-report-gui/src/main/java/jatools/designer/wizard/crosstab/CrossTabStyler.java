package jatools.designer.wizard.crosstab;

import jatools.ReportDocument;
import jatools.component.ColumnPanel;
import jatools.component.Component;
import jatools.component.Label;
import jatools.component.Line;
import jatools.component.Page;
import jatools.component.PagePanel;
import jatools.component.Panel;
import jatools.component.Text;
import jatools.component.table.Cell;
import jatools.component.table.CellStore;
import jatools.component.table.PowerTable;
import jatools.component.table.RowPanel;
import jatools.component.table.Table;
import jatools.component.table.TableBase;
import jatools.core.view.Border;
import jatools.data.reader.DatasetReader;
import jatools.data.sum.Sum;
import jatools.designer.App;
import jatools.designer.wizard.BuilderContext;
import jatools.designer.wizard.ReportBuilder;
import jatools.designer.wizard.ReportStyler;
import jatools.dom.Group;
import jatools.dom.src.DatasetNodeSource;
import jatools.dom.src.GroupNodeSource;
import jatools.dom.src.RootNodeSource;
import jatools.util.Util;

import java.awt.Frame;
import java.util.ArrayList;

import javax.swing.Icon;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class CrossTabStyler implements ReportStyler {
    static final String STYLER_NAME = App.messages.getString("res.253");
    static final String ICON_URL = "/jatools/icons/stylecrosstab.gif";
    public static final String ROW_FIELDS = "ROW_FIELDS";
    public static final String COLUMN_FIELDS = "COLUMN_FIELDS";
    public static final String SUMS = "SUMS";
    public static final String ROW_FLOWLAYOUT = "ROW_FLOWLAYOUT";
    public static final String TOP_READER = "TOP_READER";
    public static final String LEFT_READER = "LEFT_READER";
    public static final String ROW_SUM_TOP = "ROW_SUM_TOP";
    public static final String COLUMN_SUM_LEFT = "COLUMN_SUM_LEFT";
    public static final String PRINTCSS = "PRINTCSS";
    ReportBuilder builder;

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        return getName();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getName() {
        return STYLER_NAME;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Icon getIcon() {
        return Util.getIcon(ICON_URL);
    }

    /**
     * DOCUMENT ME!
     *
     * @param doc DOCUMENT ME!
     * @param context DOCUMENT ME!
     */
    public void format(ReportDocument doc, BuilderContext context) {
        doc.setNodeSource(generateNodeSource(context));

        Page page = new Page();
        page.setName("page");

        PagePanel header = new PagePanel();
        header.setHeight(80);
        header.setName("header");
        page.setHeader(header);

        PagePanel body = new PagePanel();
        body.setName("body");

        PowerTable powerTable = getPowerTable(context);

        body.add(powerTable);
        page.setBody(body);

        PagePanel footer = new PagePanel();
        footer.setHeight(280);
        footer.setName("footer");

        doc.setPage(page);
    }

    private PowerTable getPowerTable(BuilderContext context) {
        String css = context.getValue(PRINTCSS).toString();

        DatasetReader reader = (DatasetReader) context.getValue(BuilderContext.READER);

        Table topHeader = getColumnTable(context);

        topHeader.setNodePath(reader.getName());

        Table leftHeader = getRowTable(context);

        leftHeader.setNodePath(reader.getName());

        PowerTable power = PowerTable.create(leftHeader, topHeader);
        power.setPrintStyle(css);

        createCrossText(power, context);
        createCrossHeader(power);

        setBorder(power);

        power.setX(20);
        power.setY(20);

        return power;
    }

    private void setBorder(TableBase table) {
        CellStore cellstore = table.getCellstore();

        for (int r = 0; r < table.getRowCount(); r++) {
            for (int c = 0; c < table.getColumnCount(); c++) {
                Component child = cellstore.getComponent(r, c);

                if (child != null) {
                    child.setBorder(new Border());
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ReportBuilder getBuilder() {
        if (builder == null) {
            builder = new CrossTabReportBuilder();
            ((CrossTabReportBuilder) builder).setStyler(this);
        }

        return builder;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getDescription() {
        return App.messages.getString("res.254");
    }

    private RootNodeSource generateNodeSource(BuilderContext context) {
        DatasetReader reader = (DatasetReader) context.getValue(BuilderContext.READER);
        RootNodeSource rootNodeSource = new RootNodeSource();
        ArrayList rowsList = (ArrayList) context.getValue(ROW_FIELDS);
        ArrayList rowsListCopy = new ArrayList();
        ArrayList columnsList = (ArrayList) context.getValue(COLUMN_FIELDS);
        ArrayList columnsListCopy = new ArrayList();

        String[] indexFields = new String[rowsList.size()];
        String[] indexFields2 = new String[columnsList.size()];

        for (int i = 0; i < indexFields.length; i++) {
            indexFields[i] = ((Group) rowsList.get(i)).getField();
            rowsListCopy.add(rowsList.get(i));
        }

        for (int i = 0; i < indexFields2.length; i++) {
            indexFields2[i] = ((Group) columnsList.get(i)).getField();
            columnsListCopy.add(columnsList.get(i));
        }

        DatasetNodeSource dataSource = new DatasetNodeSource(reader.getName(), reader);

        if (rowsListCopy.size() > 0) {
            Object o = rowsListCopy.get(0);
            Group group = (Group) o;
            GroupNodeSource rowGroupNodeSource = new GroupNodeSource(group);
            rowsListCopy.remove(o);
            configGroupSource(rowsListCopy, rowGroupNodeSource);

            dataSource.add(rowGroupNodeSource);
        }

        if (columnsListCopy.size() > 0) {
            Object o = columnsListCopy.get(0);
            Group group = (Group) o;
            GroupNodeSource columnGroupNodeSource = new GroupNodeSource(group);
            columnsListCopy.remove(o);
            configGroupSource(columnsListCopy, columnGroupNodeSource);

            dataSource.add(columnGroupNodeSource);
        }

        dataSource.setIndexFields(getUniqueIndexes(indexFields));
        dataSource.setIndexFields2(getUniqueIndexes(indexFields2));

        rootNodeSource.add(dataSource);

        return rootNodeSource;
    }

    String[] getUniqueIndexes(String[] indexFields) {
        ArrayList list = new ArrayList();

        for (int i = 0; i < indexFields.length; i++) {
            if (!list.contains(indexFields[i])) {
                list.add(indexFields[i]);
            }
        }

        String[] result = new String[list.size()];

        for (int i = 0; i < result.length; i++) {
            result[i] = list.get(i).toString();
        }

        return result;
    }

    Table getRowTable(BuilderContext context) {
        Table table = null;

        ArrayList rowsList = (ArrayList) context.getValue(ROW_FIELDS);
        DatasetReader reader = (DatasetReader) context.getValue(BuilderContext.READER);
        String dbTableName = reader.getName();

        ArrayList sums = (ArrayList) context.getValue(SUMS);

        Boolean b = (Boolean) context.getValue(ROW_FLOWLAYOUT);
        boolean multiRows = b.booleanValue();

        int[] rows = null;
        int[] columns = null;

        if (!multiRows) {
            rows = new int[rowsList.size() + 1];
            columns = new int[rowsList.size()];
        } else {
            rows = new int[(rowsList.size() + 1) * sums.size()];
            columns = new int[rowsList.size()];
        }

        java.util.Arrays.fill(rows, 20);
        java.util.Arrays.fill(columns, 80);
        table = new Table(rows, columns);

        Boolean isRowSumTop = (Boolean) context.getValue(this.ROW_SUM_TOP);
        boolean _isisRowSumTop = isRowSumTop.booleanValue();

        if (_isisRowSumTop) {
            if (multiRows) {
                table.add(new Label(App.messages.getString("res.255")), 0, 0, rowsList.size(),
                    sums.size());
            } else {
                table.add(new Label(App.messages.getString("res.255")), 0, 0, rowsList.size(), 1);
            }

            ArrayList rowPanels = new ArrayList();

            for (int i = 0; i < rowsList.size(); i++) {
                String variable = ((Group) rowsList.get(i)).getField();
                RowPanel rowPanel = new RowPanel();
                int rowSpane = multiRows ? ((rowsList.size() - i) * sums.size()) : (rowsList.size() -
                    i);
                rowPanel.setCell(new Cell(multiRows ? sums.size() : 1, 0, rowsList.size(), rowSpane));

                rowPanel.setNodePath(variable);

                if (i < (rowsList.size() - 1)) {
                    rowSpane = multiRows ? sums.size() : 1;

                    int rowPosition = multiRows ? ((rowsList.size() - 1 - i) * (sums.size()))
                                                : (rowsList.size() - 1 - i);
                    rowPanel.add(new Label(App.messages.getString("res.227")),
                        rowPosition + (multiRows ? sums.size() : 1), i + 1,
                        rowsList.size() - 1 - i, rowSpane);
                }

                if (i == (rowsList.size() - 1)) {
                    for (int k = 0; k < rowsList.size(); k++) {
                        variable = ((Group) rowsList.get(k)).getField();

                        Text text = new Text();
                        text.setVariable("=$" + variable);

                        rowSpane = multiRows ? ((rowsList.size() - k) * (sums.size()))
                                             : (rowsList.size() - k);
                        rowPanel.add(text, multiRows ? sums.size() : 1, k, 1, rowSpane);
                    }
                }

                rowPanels.add(rowPanel);
            }

            if (rowPanels.size() > 0) {
                RowPanel rootPanel = (RowPanel) rowPanels.get(0);
                rowPanels.remove(rootPanel);
                configPanel(rowPanels, rootPanel);
                table.add(rootPanel);
            }
        } else {
            ArrayList rowPanels = new ArrayList();

            for (int i = 0; i < rowsList.size(); i++) {
                String variable = ((Group) rowsList.get(i)).getField();
                RowPanel rowPanel = new RowPanel();
                int rowSpane = multiRows ? ((rowsList.size() - i) * sums.size()) : (rowsList.size() -
                    i);
                rowPanel.setCell(new Cell(0, 0, rowsList.size(), rowSpane));

                rowPanel.setNodePath(variable);

                if (i < (rowsList.size() - 1)) {
                    rowSpane = multiRows ? sums.size() : 1;

                    int rowPosition = multiRows ? ((rowsList.size() - 1 - i) * (sums.size()))
                                                : (rowsList.size() - 1 - i);
                    rowPanel.add(new Label(App.messages.getString("res.227")), rowPosition, i + 1,
                        rowsList.size() - 1 - i, rowSpane);
                }

                if (i == (rowsList.size() - 1)) {
                    for (int k = 0; k < rowsList.size(); k++) {
                        variable = ((Group) rowsList.get(k)).getField();

                        Text text = new Text();
                        text.setVariable("=$" + variable);

                        rowSpane = multiRows ? ((rowsList.size() - k) * (sums.size()))
                                             : (rowsList.size() - k);
                        rowPanel.add(text, 0, k, 1, rowSpane);
                    }
                }

                rowPanels.add(rowPanel);
            }

            if (rowPanels.size() > 0) {
                RowPanel rootPanel = (RowPanel) rowPanels.get(0);
                rowPanels.remove(rootPanel);
                configPanel(rowPanels, rootPanel);
                table.add(rootPanel);
            }

            if (multiRows) {
                table.add(new Label("合计"), rowsList.size() * sums.size(), 0, rowsList.size(),
                    sums.size());
            } else {
                table.add(new Label(App.messages.getString("res.255")), rowsList.size(), 0,
                    rowsList.size(), 1);
            }
        }

        return table;
    }

    Table getColumnTable(BuilderContext context) {
        Table table = null;

        ArrayList columnsList = (ArrayList) context.getValue(COLUMN_FIELDS);
        DatasetReader reader = (DatasetReader) context.getValue(BuilderContext.READER);
        String dbTableName = reader.getName();

        ArrayList sums = (ArrayList) context.getValue(SUMS);

        Boolean b = (Boolean) context.getValue(ROW_FLOWLAYOUT);
        boolean mutilColumns = !b.booleanValue();

        int[] rows = null;
        int[] columns = null;

        if (!mutilColumns) {
            rows = new int[columnsList.size()];
            columns = new int[columnsList.size() + 1];
        } else {
            rows = new int[columnsList.size()];
            columns = new int[(columnsList.size() + 1) * sums.size()];
        }

        java.util.Arrays.fill(rows, 20);
        java.util.Arrays.fill(columns, 80);
        table = new Table(rows, columns);
        table.setRightFlow(true);

        Boolean isColumnSumLeft = (Boolean) context.getValue(this.COLUMN_SUM_LEFT);
        boolean _isColumnSumLeft = isColumnSumLeft.booleanValue();

        if (_isColumnSumLeft) {
            if (mutilColumns) {
                table.add(new Label(App.messages.getString("res.255")), 0, 0, sums.size(),
                    columnsList.size());
            } else {
                table.add(new Label(App.messages.getString("res.255")), 0, 0, 1, columnsList.size());
            }

            ArrayList columnPanels = new ArrayList();

            for (int i = 0; i < columnsList.size(); i++) {
                String variable = ((Group) columnsList.get(i)).getField();
                ColumnPanel columnPanel = new ColumnPanel();

                int columnSpan = mutilColumns ? ((columnsList.size() - i) * sums.size())
                                              : (columnsList.size() - i);
                columnPanel.setCell(new Cell(0, mutilColumns ? sums.size() : 1, columnSpan,
                        columnsList.size()));

                columnPanel.setNodePath(variable);

                if (i < (columnsList.size() - 1)) {
                    columnSpan = mutilColumns ? sums.size() : 1;

                    int columnPosition = mutilColumns ? ((columnsList.size() - 1 - i) * sums.size())
                                                      : (columnsList.size() - 1 - i);
                    columnPanel.add(new Label(App.messages.getString("res.227")), i + 1,
                        columnPosition + (mutilColumns ? sums.size() : 1), columnSpan,
                        columnsList.size() - 1 - i);
                }

                if (i == (columnsList.size() - 1)) {
                    for (int k = 0; k < columnsList.size(); k++) {
                        variable = ((Group) columnsList.get(k)).getField();

                        Text text = new Text();
                        text.setVariable("=$" + variable);

                        columnSpan = mutilColumns ? ((columnsList.size() - k) * sums.size())
                                                  : (columnsList.size() - k);
                        columnPanel.add(text, k, mutilColumns ? sums.size() : 1, columnSpan, 1);
                    }
                }

                columnPanels.add(columnPanel);
            }

            if (columnPanels.size() > 0) {
                ColumnPanel rootPanel = (ColumnPanel) columnPanels.get(0);
                columnPanels.remove(rootPanel);
                configPanel(columnPanels, rootPanel);
                table.add(rootPanel);
            }
        } else {
            ArrayList columnPanels = new ArrayList();

            for (int i = 0; i < columnsList.size(); i++) {
                String variable = ((Group) columnsList.get(i)).getField();
                ColumnPanel columnPanel = new ColumnPanel();

                int columnSpan = mutilColumns ? ((columnsList.size() - i) * sums.size())
                                              : (columnsList.size() - i);
                columnPanel.setCell(new Cell(0, 0, columnSpan, columnsList.size()));

                columnPanel.setNodePath(variable);

                if (i < (columnsList.size() - 1)) {
                    columnSpan = mutilColumns ? sums.size() : 1;

                    int columnPosition = mutilColumns ? ((columnsList.size() - 1 - i) * sums.size())
                                                      : (columnsList.size() - 1 - i);
                    columnPanel.add(new Label(App.messages.getString("res.227")), i + 1,
                        columnPosition, columnSpan, columnsList.size() - 1 - i);
                }

                if (i == (columnsList.size() - 1)) {
                    for (int k = 0; k < columnsList.size(); k++) {
                        variable = ((Group) columnsList.get(k)).getField();

                        Text text = new Text();
                        text.setVariable("=$" + variable);

                        columnSpan = mutilColumns ? ((columnsList.size() - k) * sums.size())
                                                  : (columnsList.size() - k);
                        columnPanel.add(text, k, 0, columnSpan, 1);
                    }
                }

                columnPanels.add(columnPanel);
            }

            if (columnPanels.size() > 0) {
                ColumnPanel rootPanel = (ColumnPanel) columnPanels.get(0);
                columnPanels.remove(rootPanel);
                configPanel(columnPanels, rootPanel);
                table.add(rootPanel);
            }

            if (mutilColumns) {
                table.add(new Label("合计"), 0, columnsList.size() * sums.size(), sums.size(),
                    columnsList.size());
            } else {
                table.add(new Label(App.messages.getString("res.255")), 0, columnsList.size(), 1,
                    columnsList.size());
            }
        }

        return table;
    }

    void createCrossText(PowerTable power, BuilderContext context) {
        ArrayList sums = (ArrayList) context.getValue(SUMS);

        Boolean b = (Boolean) context.getValue(ROW_FLOWLAYOUT);
        boolean mutilRows = b.booleanValue();
        DatasetReader reader = (DatasetReader) context.getValue(BuilderContext.READER);
        String dbTableName = reader.getName();

        int r = power.getTopHeader().getRowCount();
        int c = power.getLeftHeader().getColumnCount();
        int w = power.getTopHeader().getColumnCount();
        int h = power.getLeftHeader().getRowCount();

        if (mutilRows) {
            int cellRows = sums.size();

            for (int k = 0; k < cellRows; k++) {
                for (int i = r + k; i < (r + h); i = i + cellRows) {
                    for (int j = c; j < (c + w); j++) {
                        Sum sum = (Sum) sums.get(k);
                        String temp = sum.getGroupFields()[0];
                        String calcName = temp.substring(temp.indexOf("(") + 1, temp.indexOf(")"));

                        String fieldName = sum.getCalcField();
                        Text text = new Text();

                        String nothPath = String.format("=%s.@%s.DEF2.%s()", reader.getName(),
                                fieldName, calcName);
                        text.setVariable(nothPath);

                        power.add(text, i, j);
                    }
                }
            }
        } else {
            int cellRows = sums.size();

            for (int i = r; i < (r + h); i++) {
                for (int j = c; j < (c + w); j = j + cellRows) {
                    for (int k = 0; k < cellRows; k++) {
                        Sum sum = (Sum) sums.get(k);
                        String temp = sum.getGroupFields()[0];
                        String calcName = temp.substring(temp.indexOf("(") + 1, temp.indexOf(")"));

                        String fieldName = sum.getCalcField();
                        Text text = new Text();

                        String nothPath = String.format("=%s.@%s.DEF2.%s()", reader.getName(),
                                fieldName, calcName);
                        text.setVariable(nothPath);
                        power.add(text, i, j + k);
                    }
                }
            }
        }
    }

    void createCrossHeader(PowerTable power) {
        int r = power.getTopHeader().getRowCount();
        int c = power.getLeftHeader().getColumnCount();

        Panel crossPanel = new Panel();
        Line line = new Line(0, 0, 80 * c, 20 * r);
        crossPanel.add(line);

        power.add(crossPanel, 0, 0, c, r);
    }

    void configPanel(ArrayList panels, Panel first) {
        Panel panel = null;

        for (int i = 0; i < panels.size(); i++) {
            panel = (Panel) panels.get(i);
            first.add(panel);
            panels.remove(panel);

            break;
        }

        if (panel != null) {
            configPanel(panels, panel);
        }
    }

    void configGroupSource(ArrayList list, GroupNodeSource root) {
        GroupNodeSource groupSource = null;

        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            Group g = (Group) o;
            groupSource = new GroupNodeSource(g);
            root.add(groupSource);
            list.remove(o);

            break;
        }

        if (groupSource != null) {
            configGroupSource(list, groupSource);
        }
    }

    class _CrossTabBuilder implements ReportBuilder {
        public ReportDocument build(Frame owner, BuilderContext context) {
            ReportDocument doc = new ReportDocument();

            format(doc, context);

            return doc;
        }
    }
}
