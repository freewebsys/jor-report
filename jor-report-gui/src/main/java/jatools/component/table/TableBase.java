package jatools.component.table;

import jatools.component.Component;
import jatools.component.ComponentConstants;
import jatools.component.Label;
import jatools.component.Line;

import jatools.designer.App;
import jatools.designer.Main;

import jatools.xml.serializer.XmlWriteListener;

import java.awt.Dimension;
import java.awt.Rectangle;

import java.beans.PropertyChangeEvent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.7 $
 */
public abstract class TableBase extends Component implements GridComponent {
    protected GridSpec gridSpec = new GridSpec();
    private CellStore cellstore;
    private PanelStore panelstore;
    private BothStore bothstore;
    private Properties rowkeyExpressions;
    private Properties columnkeyExpressions;
    private String nodePath;

    /**
     * Creates a new TableBase object.
     */
    public TableBase() {
    }

    protected TableBase(int[] rows, int[] columns) {
        setColumnWidths(columns);
        setRowHeights(rows);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isInline() {
        return false;
    }

    /**
     * DOCUMENT ME!
     */
    public void validate() {
        super.validate();
    }

    /**
     * DOCUMENT ME!
     *
     * @param row
     *            DOCUMENT ME!
     * @param col
     *            DOCUMENT ME!
     */
    public void translate(int row, int col) {
        Iterator it = this.getBothstore().iterator();

        while (it.hasNext()) {
            Cell cell = ((Component) it.next()).getCell();

            if (cell != null) {
                cell.row += row;
                cell.column += col;
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Properties getColumnkeyExpressions() {
        return columnkeyExpressions;
    }

    /**
     * DOCUMENT ME!
     *
     * @param columnkeyExpressions
     *            DOCUMENT ME!
     */
    public void setColumnkeyExpressions(Properties columnkeyExpressions) {
        this.columnkeyExpressions = columnkeyExpressions;
    }

    /**
     * DOCUMENT ME!
     *
     * @param row
     *            DOCUMENT ME!
     * @param col
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getComponent(int row, int col) {
        return this.getCellstore().getComponent(row, col);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Properties getRowkeyExpressions() {
        return rowkeyExpressions;
    }

    /**
     * DOCUMENT ME!
     *
     * @param rowkeyExpressions
     *            DOCUMENT ME!
     */
    public void setRowkeyExpressions(Properties rowkeyExpressions) {
        this.rowkeyExpressions = rowkeyExpressions;
    }

    /**
     * DOCUMENT ME!
     *
     * @param row
     *            DOCUMENT ME!
     * @param newheight
     *            DOCUMENT ME!
     */
    public void insertRowBefore(int row, int newheight) {
        int[] oldVal = getRowHeights();

        gridSpec.insertRow(row, newheight);

        int[] newVal = getRowHeights();
        firePropertyChange(ComponentConstants.PROPERTY_ROW_HEIGHTS, oldVal, newVal);

        _insertRowBefore(row);
        _insertRowExpressions(row);
    }

    private void _insertRowBefore(int row) {
        Iterator it = this.getBothstore().iterator();

        while (it.hasNext()) {
            Component child = (Component) it.next();
            Cell oldCell = (Cell) child.getCell();

            int bottom = oldCell.row2();

            if (bottom >= row) {
                Cell newCell = (Cell) oldCell.clone();

                if ((newCell.row == row) && child instanceof GridComponent) {
                    newCell.rowSpan++;
                } else if ((newCell.rowSpan > 1) && (newCell.row < row)) {
                    newCell.rowSpan++;
                } else {
                    newCell.row++;
                }

                child.setCell(newCell);

                this.firePropertyChange(new PropertyChangeEvent(child,
                        ComponentConstants.PROPERTY_CELL, oldCell, newCell));
            }
        }

        setDirty();
    }

    /**
     * DOCUMENT ME!
     *
     * @param r
     *            DOCUMENT ME!
     * @param c
     *            DOCUMENT ME!
     * @param w
     *            DOCUMENT ME!
     * @param h
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean mergeCell(int r, int c, int w, int h) {
        if ((w == 1) && (h == 1)) {
            return false;
        }

        Component child = getCellstore().getComponent(r, c);

        if (child != null) {
            Cell cell = (Cell) child.getCell();

            if ((cell.colSpan == w) && (cell.rowSpan == h)) {
                return false;
            }
        }

        CellStore store = this.getCellstore();

        for (int i = r; i < (r + h); i++) {
            for (int j = c; j < (c + w); j++) {
                Component com = store.getComponent(i, j);

                if ((com != null) && (com != child)) {
                    Component p = com.getParent();

                    com.getParent().remove(com);

                    PropertyChangeEvent e = new PropertyChangeEvent(p,
                            ComponentConstants.PROPERTY_DELETE, com, null);

                    this.firePropertyChange(e);
                }
            }
        }

        if (child == null) {
            child = new Label();

            Cell cell = new Cell(r, c, w, h);
            child.setCell(cell);

            Component p = getPanelstore().getComponentOver(r, c);

            p.add(child);

            PropertyChangeEvent e = new PropertyChangeEvent(p, ComponentConstants.PROPERTY_ADD,
                    child, null);

            this.firePropertyChange(e);
        } else {
            Cell oldCell = child.getCell();

            Cell newCell = new Cell(r, c, w, h);
            child.setCell(newCell);

            this.firePropertyChange(new PropertyChangeEvent(child,
                    ComponentConstants.PROPERTY_CELL, oldCell, newCell));
        }

        setDirty();

        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getPreferredWidth() {
        int w = 0;

        for (int i = 0; i < getColumnCount(); i++) {
            w += this.getColumnWidth(i);
        }

        return w;
    }

    // protected AbstractView getBackgroundView(Rectangle b) {
    // TextView e = new TextView();
    //
    // e.setBacklayer(true);
    // e.setBounds(b);
    //
    // return e;
    // }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getTotalRowHeights() {
        int count = this.getRowCount();
        int h = 0;

        for (int i = 0; i < count; i++) {
            h += this.getRowHeight(i);
        }

        return h;
    }

    /**
     * DOCUMENT ME!
     *
     * @param cc
     *            DOCUMENT ME!
     */
    public void setColumnCount(int cc) {
        boolean equal = cc == this.getColumnCount();
        this.gridSpec.setColumnCount(cc);

        if (!equal) {
            setDirty();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param rc
     *            DOCUMENT ME!
     */
    public void setRowCount(int rc) {
        boolean equal = rc == this.getRowCount();
        gridSpec.setRowCount(rc);

        if (!equal) {
            setDirty();
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param row
     *            DOCUMENT ME!
     * @param height
     *            DOCUMENT ME!
     */
    public void setRowHeight(int row, int height) {
        this.gridSpec.setRowHeight(row, height);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRowCount() {
        return this.gridSpec.getRowCount();
    }

    protected void validateCell(int row, int column) {
        if (!((row < getRowCount()) && (column < getColumnCount()) && (row >= 0) && (column >= 0))) {
            throw new java.lang.ArrayIndexOutOfBoundsException("No such cell: [" + row + "," +
                column + "]");
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumnCount() {
        return this.gridSpec.getColumnCount();
    }

    /**
     * DOCUMENT ME!
     */
    public void setDirty() {
        this.cellstore = null;
        this.panelstore = null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param row
     *            DOCUMENT ME!
     */
    public void insertRowBefore(int row) {
        insertRowBefore(row, 20);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row
     *            DOCUMENT ME!
     */
    public void insertRowAfter(int row) {
        insertRowAfter(row, 20);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row
     *            DOCUMENT ME!
     * @param h
     *            DOCUMENT ME!
     */
    public void insertRowAfter(int row, int h) {
        int[] oldVal = getRowHeights();

        gridSpec.insertRow(row + 1, h);

        int[] newVal = getRowHeights();
        firePropertyChange(ComponentConstants.PROPERTY_ROW_HEIGHTS, oldVal, newVal);

        _insertRowAfter(row);
        _insertRowExpressions(row + 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param col
     *            DOCUMENT ME!
     */
    public void insertColumnAfter(int col) {
        insertColumnAfter(col, 80);
    }

    /**
     * DOCUMENT ME!
     *
     * @param col
     *            DOCUMENT ME!
     * @param w
     *            DOCUMENT ME!
     */
    public void insertColumnAfter(int col, int w) {
        int[] oldVal = getColumnWidths();

        gridSpec.insertColumn(col + 1, w);

        int[] newVal = getColumnWidths();
        firePropertyChange(ComponentConstants.PROPERTY_COLUMN_WIDTHS, oldVal, newVal);

        _insertColumnAfter(col);
    }

    private void _insertRowAfter(int row) {
        int _row = row;
        row++;

        Iterator it = this.getBothstore().iterator();

        while (it.hasNext()) {
            Component child = (Component) it.next();
            Cell oldCell = (Cell) child.getCell();

            Cell newCell = null;

            if (oldCell.contains(_row, oldCell.column) && child instanceof GridComponent) {
                newCell = (Cell) oldCell.clone();
                newCell.rowSpan++;
            } else if (oldCell.row2() >= row) {
                newCell = (Cell) oldCell.clone();

                if ((newCell.rowSpan > 1) && (newCell.row < row)) {
                    newCell.rowSpan++;
                } else {
                    newCell.row++;
                }
            }

            if (newCell != null) {
                child.setCell(newCell);

                this.firePropertyChange(new PropertyChangeEvent(child,
                        ComponentConstants.PROPERTY_CELL, oldCell, newCell));
            }
        }

        setDirty();
    }

    private void _insertColumnAfter(int col) {
        int _col = col;
        col++;

        Iterator it = this.getBothstore().iterator();

        while (it.hasNext()) {
            Component child = (Component) it.next();
            Cell oldCell = (Cell) child.getCell();

            Cell newCell = null;

            if (oldCell.contains(oldCell.row, _col) && child instanceof GridComponent) {
                newCell = (Cell) oldCell.clone();
                newCell.colSpan++;
            } else if (oldCell.column2() >= col) {
                newCell = (Cell) oldCell.clone();

                if ((newCell.colSpan > 1) && (newCell.column < col)) {
                    newCell.colSpan++;
                } else {
                    newCell.column++;
                }
            }

            if (newCell != null) {
                child.setCell(newCell);

                this.firePropertyChange(new PropertyChangeEvent(child,
                        ComponentConstants.PROPERTY_CELL, oldCell, newCell));
            }
        }

        setDirty();
    }

    /**
     * DOCUMENT ME!
     *
     * @param row
     *            DOCUMENT ME!
     */
    public void removeRow(int row) {
        removeRow(row, 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row
     *            DOCUMENT ME!
     * @param count
     *            DOCUMENT ME!
     */
    public void removeRow(int row, int count) {
        int[] oldVal = getRowHeights();

        for (int _i = 0; _i < count; _i++) {
            _removeRowExpressions(row);
            _removeRow(row, this);
            gridSpec.removeRow(row);
        }

        int[] newVal = getRowHeights();

        firePropertyChange(ComponentConstants.PROPERTY_ROW_HEIGHTS, oldVal, newVal);
    }

    /**
     * DOCUMENT ME!
     *
     * @param col
     *            DOCUMENT ME!
     * @param count
     *            DOCUMENT ME!
     */
    public void removeColumn(int col, int count) {
        int[] oldVal = getColumnWidths();

        for (int _i = 0; _i < count; _i++) {
            _removeColumn(col, this);
            gridSpec.removeColumn(col);
            _removeColumnExpressions(col);
        }

        int[] newVal = getColumnWidths();

        firePropertyChange(ComponentConstants.PROPERTY_COLUMN_WIDTHS, oldVal, newVal);
    }

    /**
     * DOCUMENT ME!
     *
     * @param col
     *            DOCUMENT ME!
     */
    public void removeColumn(int col) {
        removeColumn(col, 1);
    }

    private void _removeRowExpressions(int row) {
        if ((this.rowkeyExpressions != null) && !this.rowkeyExpressions.isEmpty()) {
            Properties old = (Properties) this.rowkeyExpressions.clone();

            this.rowkeyExpressions.remove("E" + row);

            for (int r = row + 1; r < this.getRowCount(); r++) {
                String rowid = "E" + r;

                String expr = this.rowkeyExpressions.getProperty(rowid);

                if (expr != null) {
                    this.rowkeyExpressions.remove(rowid);
                    this.rowkeyExpressions.setProperty("E" + (r - 1), expr);
                }
            }

            firePropertyChange("RowkeyExpressions", old, this.rowkeyExpressions);
        }
    }

    private void _removeColumnExpressions(int col) {
        if ((this.columnkeyExpressions != null) && !this.columnkeyExpressions.isEmpty()) {
            Properties old = (Properties) this.columnkeyExpressions.clone();

            this.columnkeyExpressions.remove("E" + col);

            for (int c = col + 1; c < this.getRowCount(); c++) {
                String colid = "E" + c;

                String expr = this.columnkeyExpressions.getProperty(colid);

                if (expr != null) {
                    this.columnkeyExpressions.remove(colid);
                    this.columnkeyExpressions.setProperty("E" + (c - 1), expr);
                }
            }

            firePropertyChange("ColumnkeyExpressions", old, this.columnkeyExpressions);
        }
    }

    private void _removeColumn(int col, Component c) {
        for (int i = c.getChildCount() - 1; i >= 0; i--) {
            Component child = c.getChild(i);
            Cell oldCell = (Cell) child.getCell();

            boolean delete = false;

            if (oldCell.column > col) {
                Cell newVal = (Cell) oldCell.clone();
                newVal.column--;
                child.setCell(newVal);

                this.firePropertyChange(new PropertyChangeEvent(child,
                        ComponentConstants.PROPERTY_CELL, oldCell, newVal));
            } else if (oldCell.column == col) {
                if (child instanceof GridComponent && (oldCell.colSpan > 1)) {
                    Cell newVal = (Cell) oldCell.clone();
                    newVal.colSpan--;
                    child.setCell(newVal);

                    this.firePropertyChange(new PropertyChangeEvent(child,
                            ComponentConstants.PROPERTY_CELL, oldCell, newVal));
                } else {
                    c.remove(child);

                    PropertyChangeEvent e = new PropertyChangeEvent(c,
                            ComponentConstants.PROPERTY_DELETE, child, null);

                    this.firePropertyChange(e);

                    delete = true;
                }
            } else if (((oldCell.column + oldCell.colSpan) - 1) >= col) {
                Cell newVal = (Cell) oldCell.clone();
                newVal.colSpan--;
                child.setCell(newVal);

                this.firePropertyChange(new PropertyChangeEvent(child,
                        ComponentConstants.PROPERTY_CELL, oldCell, newVal));
            }

            if (!delete && child instanceof GridComponent) {
                _removeColumn(col, child);
            }
        }
    }

    private void _removeRow(int row, Component c) {
        for (int i = c.getChildCount() - 1; i >= 0; i--) {
            Component child = c.getChild(i);
            Cell oldCell = (Cell) child.getCell();

            boolean delete = false;

            if (oldCell.row > row) {
                Cell newVal = (Cell) oldCell.clone();
                newVal.row--;
                child.setCell(newVal);

                this.firePropertyChange(new PropertyChangeEvent(child,
                        ComponentConstants.PROPERTY_CELL, oldCell, newVal));
            } else if (oldCell.row == row) {
                if (child instanceof GridComponent && (oldCell.rowSpan > 1)) {
                    Cell newVal = (Cell) oldCell.clone();
                    newVal.rowSpan--;
                    child.setCell(newVal);

                    this.firePropertyChange(new PropertyChangeEvent(child,
                            ComponentConstants.PROPERTY_CELL, oldCell, newVal));
                } else {
                    c.remove(child);

                    PropertyChangeEvent e = new PropertyChangeEvent(c,
                            ComponentConstants.PROPERTY_DELETE, child, null);

                    this.firePropertyChange(e);

                    delete = true;
                }
            } else if (((oldCell.row + oldCell.rowSpan) - 1) >= row) {
                Cell newVal = (Cell) oldCell.clone();
                newVal.rowSpan--;
                child.setCell(newVal);

                this.firePropertyChange(new PropertyChangeEvent(child,
                        ComponentConstants.PROPERTY_CELL, oldCell, newVal));
            }

            if (!delete && child instanceof GridComponent) {
                _removeRow(row, child);
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param row
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRowHeight(int row) {
        return this.gridSpec.getRowHeight(row);
    }

    /**
     * DOCUMENT ME!
     *
     * @param col
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumnWidth(int col) {
        return this.gridSpec.getColumnWidth(col);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row
     *            DOCUMENT ME!
     * @param column
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Dimension getCellSize(int row, int column) {
        return new Dimension(getColumnWidth(column), getRowHeight(row));
    }

    /**
     * DOCUMENT ME!
     *
     * @param col
     *            DOCUMENT ME!
     * @param width
     *            DOCUMENT ME!
     */
    public void setColumnWidth(int col, int width) {
        this.gridSpec.setColumnWidth(col, width);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getColumnWidths() {
        return this.gridSpec.getColumnWidths();
    }

    /**
     * DOCUMENT ME!
     *
     * @param columnWidths
     *            DOCUMENT ME!
     */
    public void setColumnWidths(int[] columnWidths) {
        setColumnCount(columnWidths.length);

        for (int i = 0; i < columnWidths.length; i++) {
            setColumnWidth(i, columnWidths[i]);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int[] getRowHeights() {
        return this.gridSpec.getRowHeights();
    }

    /**
     * DOCUMENT ME!
     *
     * @param from
     *            DOCUMENT ME!
     * @param to
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRowHeight(int from, int to) {
        return this.gridSpec.getRowY(to + 1) - this.gridSpec.getRowY(from);
    }

    /**
     * DOCUMENT ME!
     *
     * @param from
     *            DOCUMENT ME!
     * @param to
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumnWidth(int from, int to) {
        return this.gridSpec.getColumnX(to + 1) - this.gridSpec.getColumnX(from);
    }

    /**
     * DOCUMENT ME!
     *
     * @param rowHeights
     *            DOCUMENT ME!
     */
    public void setRowHeights(int[] rowHeights) {
        if (hasPropertyChangeListener()) {
            this.firePropertyChange(ComponentConstants.PROPERTY_ROW_HEIGHTS, this.getRowHeights(),
                rowHeights);
        }

        setRowCount(rowHeights.length);

        for (int i = 0; i < rowHeights.length; i++) {
            setRowHeight(i, rowHeights[i]);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param column
     *            DOCUMENT ME!
     */
    public void insertColumnBefore(int column) {
        insertColumnBefore(column, 80);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row
     *            DOCUMENT ME!
     * @param col
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getChildren(int row, int col) {
        throw new UnsupportedOperationException("getChildren");
    }

    /**
     * DOCUMENT ME!
     *
     * @param child
     *            DOCUMENT ME!
     * @param row
     *            DOCUMENT ME!
     * @param column
     *            DOCUMENT ME!
     */
    public void add(Component child, int row, int column) {
        add(child, row, column, 1, 1);
    }

    /**
     * DOCUMENT ME!
     *
     * @param child
     *            DOCUMENT ME!
     * @param row
     *            DOCUMENT ME!
     * @param column
     *            DOCUMENT ME!
     * @param width
     *            DOCUMENT ME!
     * @param height
     *            DOCUMENT ME!
     */
    public void add(Component child, int row, int column, int width, int height) {
        if (child == null) {
            throw new NullPointerException(App.messages.getString("res.645"));
        }

        if ((row < 0) || (row > getRowCount())) {
            throw new IllegalArgumentException(App.messages.getString("res.646"));
        }

        if ((column < 0) || (column > getColumnCount())) {
            throw new IllegalArgumentException(App.messages.getString("res.647"));
        }

        child.setCell(new Cell(row, column, width, height));
        add(child);
    }

    /**
     * DOCUMENT ME!
     *
     * @param child
     *            DOCUMENT ME!
     */
    public void remove(Component child) {
        super.remove(child);
        setDirty();
    }

    /**
     * DOCUMENT ME!
     *
     * @param r
     *            DOCUMENT ME!
     * @param c
     *            DOCUMENT ME!
     * @param w
     *            DOCUMENT ME!
     * @param h
     *            DOCUMENT ME!
     */
    public void removeCell(int r, int c, int w, int h) {
        CellStore store = this.getCellstore();

        for (int i = r; i < (r + h); i++) {
            for (int j = c; j < (c + w); j++) {
                Component com = store.getComponent(i, j);

                if ((com != null)) {
                    Component p = com.getParent();

                    com.getParent().remove(com);

                    PropertyChangeEvent e = new PropertyChangeEvent(p,
                            ComponentConstants.PROPERTY_DELETE, com, null);

                    this.firePropertyChange(e);
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param child
     *            DOCUMENT ME!
     */
    public void add(Component child) {
        super.add(child);

        setDirty();
    }

    /**
     * DOCUMENT ME!
     *
     * @param child
     *            DOCUMENT ME!
     * @param index
     *            DOCUMENT ME!
     */
    public void insert(Component child, int index) {
        add(child);
    }

    /**
     * DOCUMENT ME!
     *
     * @param child
     *            DOCUMENT ME!
     */
    public void insert(Component child) {
        insert(child, 0);
    }

    /**
     * DOCUMENT ME!
     *
     * @param comp
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isLazyContainer(Component comp) {
        return (comp.getClass() == Line.class);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row
     *            DOCUMENT ME!
     * @param column
     *            DOCUMENT ME!
     * @param width
     *            DOCUMENT ME!
     * @param height
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Rectangle getBounds(int row, int column, int width, int height) {
        Rectangle ret = new Rectangle();

        for (int i = 0; i < row; i++) {
            ret.y += getRowHeight(i);
        }

        for (int i = 0; i < column; i++) {
            ret.x += getColumnWidth(i);
        }

        int right = column + width;

        for (int i = column; i < right; i++) {
            ret.width += getColumnWidth(i);
        }

        int bottom = row + height;

        for (int i = row; i < bottom; i++) {
            ret.height += getRowHeight(i);
        }

        return ret;
    }

    /**
     * DOCUMENT ME!
     *
     * @param cell
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Rectangle getBounds(Cell cell) {
        return getBounds(cell.row, cell.column, cell.colSpan, cell.rowSpan);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public XmlWriteListener getXmlWriteListener() {
        return new _RemoveNullCell(this);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public GridSpec getGridSpec() {
        return gridSpec;
    }

    /**
     * 取得指定位置的单元格，有可能是合并单元格
     *
     * @param x
     *            DOCUMENT ME!
     * @param y
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Cell getCellAt(int x, int y) {
        Cell cell = this.getGrowStandardCellAt(x, y);
        Cell cell_ = getCellstore().getCell(cell.row, cell.column);

        return (cell_ == null) ? cell : cell_;
    }

    // 可能值 [-1,-1,rows,columns]
    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Cell getGrowStandardCellAt(int x, int y) {
        int column = -1;
        int row = -1;
        int x1 = 0;

        if (x > -1) {

            do {
                column++;

                if (column >= this.getColumnCount()) {
                    break;
                }

                x1 += getColumnWidth(column);
            } while (x1 < x);
        }

        int y1 = 0;

        if (y > -1) {
            do {
                row++;

                if (row >= this.getRowCount()) {
                    break;
                }

                y1 += getRowHeight(row);
            } while (y1 < y);
        }

        return new Cell(row, column);
    }

    // 可能值 [0,0,rows-1,columns-1]
    /**
     * DOCUMENT ME!
     *
     * @param x DOCUMENT ME!
     * @param y DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Cell getValidStandardCellAt(int x, int y) {
        int column = -1;
        int row = -1;
        int x1 = 0;

        do {
            column++;

            x1 += getColumnWidth(column);
        } while ((x1 < x) && (column < (this.getColumnCount() - 1)));

        int y1 = 0;

        do {
            row++;

            y1 += getRowHeight(row);
        } while ((y1 < y) && (row < (this.getRowCount() - 1)));

        return new Cell(row, column);
    }

    /**
     * DOCUMENT ME!
     *
     * @param col
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getColumnX(int col) {
        return gridSpec.getColumnX(col);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row
     *            DOCUMENT ME!
     * @param span
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getHeight(int row, int span) {
        return gridSpec.getHeight(row, span);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getRowY(int row) {
        return gridSpec.getRowY(row);
    }

    /**
     * DOCUMENT ME!
     *
     * @param col
     *            DOCUMENT ME!
     * @param span
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public int getWidth(int col, int span) {
        return gridSpec.getWidth(col, span);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public CellStore getCellstore() {
        if (cellstore == null) {
            cellstore = new CellStore(this);
        }

        return cellstore;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public BothStore getBothstore() {
        if (bothstore == null) {
            bothstore = new BothStore(this);
        }

        return bothstore;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public PanelStore getPanelstore() {
        if (panelstore == null) {
            panelstore = new PanelStore(this);
        }

        return panelstore;
    }

    /**
     * DOCUMENT ME!
     *
     * @param r
     *            DOCUMENT ME!
     * @param c
     *            DOCUMENT ME!
     * @param w
     *            DOCUMENT ME!
     * @param h
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean unmergeCell(int r, int c, int w, int h) {
        boolean done = false;

        CellStore store = getCellstore();

        for (int i = r; i < (r + h); i++) {
            for (int j = c; j < (c + w); j++) {
                Component child = store.getComponent(i, j);

                if ((child != null) && child.getCell().isMerged()) {
                    Cell oldVal = (Cell) child.getCell();
                    Cell newVal = (Cell) oldVal.clone();
                    newVal.colSpan = 1;
                    newVal.rowSpan = 1;
                    child.setCell(newVal);
                    done = true;

                    this.firePropertyChange(new PropertyChangeEvent(child,
                            ComponentConstants.PROPERTY_CELL, oldVal, newVal));
                }
            }
        }

        if (done) {
            setDirty();
        }

        return done;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TableBase getRootTable() {
        Component c = this;

        while (c != null) {
            if (c instanceof TableBase && (c.getCell() == null)) {
                return (TableBase) c;
            }

            c = c.getParent();
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isRootTable() {
        return this.getRootTable() == this;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TableBase getNearestTable() {
        Component c = this;

        while (c != null) {
            if (c instanceof TableBase) {
                return (TableBase) c;
            }

            c = c.getParent();
        }

        return null;
    }

    /**
     * DOCUMENT ME!
     *
     * @param col
     *            DOCUMENT ME!
     * @param w
     *            DOCUMENT ME!
     */
    public void insertColumnBefore(int col, int w) {
        int[] oldVal = getColumnWidths();

        gridSpec.insertColumn(col, w);

        int[] newVal = getColumnWidths();
        firePropertyChange(ComponentConstants.PROPERTY_COLUMN_WIDTHS, oldVal, newVal);

        _insertColumnBefore(col);
        _insertColumnExpressions(col);
    }

    private void _insertColumnExpressions(int col) {
        if ((this.columnkeyExpressions != null) && !this.columnkeyExpressions.isEmpty()) {
            Properties old = (Properties) this.columnkeyExpressions.clone();

            Iterator it = old.keySet().iterator();

            while (it.hasNext()) {
                String colid = (String) it.next();

                int _col = Integer.parseInt(colid.substring(1));

                if (_col >= col) {
                    String expr = this.columnkeyExpressions.getProperty(colid);
                    this.columnkeyExpressions.remove(colid);
                    this.columnkeyExpressions.setProperty("E" + (_col + 1), expr);
                }
            }

            firePropertyChange("ColumnkeyExpressions", old, this.columnkeyExpressions);
        }
    }

    private void _insertRowExpressions(int row) {
        if ((this.rowkeyExpressions != null) && !this.rowkeyExpressions.isEmpty()) {
            Properties old = (Properties) this.rowkeyExpressions.clone();

            Iterator it = old.keySet().iterator();

            while (it.hasNext()) {
                String rowid = (String) it.next();

                int _row = Integer.parseInt(rowid.substring(1));

                if (_row >= row) {
                    String expr = this.rowkeyExpressions.getProperty(rowid);
                    this.rowkeyExpressions.remove(rowid);
                    this.rowkeyExpressions.setProperty("E" + (_row + 1), expr);
                }
            }

            firePropertyChange("RowkeyExpressions", old, this.rowkeyExpressions);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param col
     *            DOCUMENT ME!
     * @param expr
     *            DOCUMENT ME!
     */
    public void setColumnKeyExpression(int col, String expr) {
        if (this.columnkeyExpressions == null) {
            this.columnkeyExpressions = new Properties();
        }

        this.columnkeyExpressions.put("E" + col, expr);
    }

    /**
     * DOCUMENT ME!
     *
     * @param row
     *            DOCUMENT ME!
     * @param expr
     *            DOCUMENT ME!
     */
    public void setRowKeyExpression(int row, String expr) {
        if (this.rowkeyExpressions == null) {
            this.rowkeyExpressions = new Properties();
        }

        this.rowkeyExpressions.put("E" + row, expr);
    }

    /**
     * DOCUMENT ME!
     *
     * @param col
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getColumnkeyExpression(int col) {
        if (this.columnkeyExpressions != null) {
            return this.columnkeyExpressions.getProperty("E" + col);
        } else {
            return null;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param row
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getRowkeyExpression(int row) {
        if (this.rowkeyExpressions != null) {
            return this.rowkeyExpressions.getProperty("E" + row);
        } else {
            return null;
        }
    }

    private void _insertColumnBefore(int col) {
        Iterator it = this.getBothstore().iterator();

        while (it.hasNext()) {
            Component child = (Component) it.next();
            Cell oldCell = (Cell) child.getCell();

            int right = oldCell.column2();

            if (right >= col) {
                Cell newCell = (Cell) oldCell.clone();

                if ((newCell.colSpan > 1) && (newCell.column < col)) {
                    newCell.colSpan++;
                } else if ((newCell.column == col) && child instanceof GridComponent) {
                    newCell.colSpan++;
                } else {
                    newCell.column++;
                }

                child.setCell(newCell);

                this.firePropertyChange(new PropertyChangeEvent(child,
                        ComponentConstants.PROPERTY_CELL, oldCell, newCell));
            }
        }

        setDirty();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getNodePath() {
        return nodePath;
    }

    /**
     * DOCUMENT ME!
     *
     * @param nodePath
     *            DOCUMENT ME!
     */
    public void setNodePath(String nodePath) {
        this.nodePath = nodePath;
    }
}


class _RemoveNullCell implements XmlWriteListener {
    private TableBase table;
    private ArrayList _children;

    _RemoveNullCell(TableBase table) {
        this.table = table;
    }

    /**
     * DOCUMENT ME!
     */
    public void beforeWrite() {
        _children = (ArrayList) this.table.getChildren().clone();
        removeEmptyCell(this.table.getChildren());
    }

    /**
     * DOCUMENT ME!
     */
    public void afterWrite() {
        this.table.setChildren(this._children);
    }

    private void removeEmptyCell(ArrayList children) {
        for (Iterator iter = children.iterator(); iter.hasNext();) {
            Component com = (Component) iter.next();

            if (com instanceof Label) {
                Label label = (Label) com;

                if ((Main.getInstance() != null) && nullString(label.getText()) &&
                        nullString(label.getName()) && (label.getBorder() == null) &&
                        (label.getBackColor() == null) && nullString(label.getBeforePrint()) &&
                        nullString(label.getAfterPrint())) {
                    iter.remove();
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param label
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static boolean isNull(Label label) {
        return nullString(label.getText()) && nullString(label.getName()) &&
        (label.getBorder() == null) && (label.getBackColor() == null) &&
        nullString(label.getBeforePrint()) && nullString(label.getAfterPrint());
    }

    static boolean nullString(String str) {
        return ((str == null) || (str.trim().length() == 0));
    }

    /**
     * DOCUMENT ME!
     *
     * @param prop
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isWritable(String prop) {
        if ((this.table.getCell() != null) &&
                ("Width,Height,X,Y,ColumnWidths,RowHeights".indexOf(prop) > -1)) {
            return false;
        } else if (("Width,Height,".indexOf(prop) > -1)) {
            return false;
        } else {
            return true;
        }
    }
}
