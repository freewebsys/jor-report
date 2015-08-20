package jatools.component;

import jatools.PageFormat;
import jatools.VariableContext;

import jatools.accessor.PropertyDescriptor;

import jatools.component.table.Cell;

import jatools.core.view.Border;

import jatools.data.reader.DatasetReader;

import jatools.designer.data.Hyperlink;

import jatools.formatter.Format2;

import java.awt.Color;
import java.awt.Font;

import java.util.ArrayList;
import java.util.Properties;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class ComponentConstants {
    public static final String PROPERTY_PAGE_SELECTION = "selection";
    public static final String PROPERTY_PAGE = "page";
    public static final String PROPERTY_START_CHANGE_FAMILY = "start.change";
    public static final String PROPERTY_STOP_CHANGE_FAMILY = "stop.change";
    public static final String PROPERTY_CANCEL_CHANGE_FAMILY = "cancel.chage";
    public static final String PROPERTY_CHILDREN_ADD = "children.add";
    public static final String PROPERTY_CHILDREN_REMOVE = "children.remove";
    public static final String PROPERTY_PAGE_LAYOUT = "page.layout";
    public static final String PROPERTY_PAGE_IMAGEABLE_WIDTH = "imageable.width";
    public static final String PROPERTY_CHILDREN = "Children";
    public static final String PROPERTY_VARIABLE_CONTEXT = "VariableContext";
    public static final String PROPERTY_PERMISSION = "Permission";
    public static final String PROPERTY_POINTS = "Points";
    public static final String PROPERTY_TABLE_ROW = "TableRow";
    public static final String PROPERTY_TABLE_COLUMN = "TableColumn";
    public static final String RROPERTY_TABLE_CELLS = "TableCells";
    public static final String RROPERTY_STRING_VALUE = "StringValue";
    public static final String PROPERTY_LIMIT_ROWS = "LimitRows";
    public static final String PROPERTY_PRINT_CONTINUED = "PrintContinued";
    public static final String PROPERTY_BACK_COLOR = "BackColor";
    public static final String PROPERTY_VARIABLE = "Variable";
    public static final String PROPERTY_BORDER = "Border";
    public static final String PROPERTY_CAPTION = "Caption";
    public static final String PROPERTY_TEXT = "Text";
    public static final String PROPERTY_FONT = "Font";
    public static final String PROPERTY_FONT_NAME = "Font";
    public static final String PROPERTY_PRINT_STYLE = "PrintStyle";
    public static final String PROPERTY_FONT_ITALIC = "Font.Italic";
    public static final String PROPERTY_FONT_BOLD = "Font.Bold";
    public static final String PROPERTY_FORE_COLOR = "ForeColor";
    public static final String PROPERTY_FORMAT = "Format";
    public static final String PROPERTY_HEIGHT = "Height";
    public static final String PROPERTY_X = "X";
    public static final String PROPERTY_LINE_PATTERN = "LinePattern";
    public static final String PROPERTY_LINE_SIZE = "LineSize";
    public static final String PROPERTY_AFTERPRINT = "AfterPrint";
    public static final String PROPERTY_BEFOREPRINT2 = "BeforePrint";
    public static final String PROPERTY_INIT_PRINT = "InitPrint";
    public static final String PROPERTY_NAME = "Name";
    public static final String PROPERTY_BREAK_PAGE = "BreakPage";
    public static final String PROPERTY_VERTICAL_ALIGNMENT = "VerticalAlignment";
    public static final String PROPERTY_HORIZONTAL_ALIGNMENT = "HorizontalAlignment";
    public static final String PROPERTY_WORDWRAP = "Wordwrap";
    public static final String PROPERTY_WORDWRAP2 = "Wordwrap2";
    public static final String PROPERTY_SOURCE = "Source";
    public static final String PROPERTY_Y = "Y";
    public static final String PROPERTY_READER_VARIABLE = "ReaderVariable";
    public static final String PROPERTY_WIDTH = "Width";
    public static final String PROPERTY_LEFT = "Left";
    public static final String PROPERTY_TOP = "Top";
    public static final String PROPERTY_RIGHT = "Right";
    public static final String PROPERTY_BOTTOM = "Bottom";
    public static final String PROPERTY_DATASETS = "DataSets";
    public static final String PROPERTY_INDEX = "Index";
    public static final String PROPERTY_ORIENTATION = "Orientation";
    public static final String PROPERTY_SHOW_CASE = "ShowCase";
    public static final String PROPERTY_FILEPATH = "FilePath";
    public static final String RROPERTY_CELL_COMPONENT = "cell.component";
    public static final String PROPERTY_CELL_COLUMN = "cell.column";
    public static final String RROPERTY_CELL_ROW = "cell.row";
    public static final String RROPERTY_CELL_WIDTH = "cell.width";
    public static final String RROPERTY_CELL_HEIGHT = "cell.height";
    public static final String RROPERTY_CELL_POINT_SEAT = "cell.point.seat";
    public static final String PROPERTY_DRIVER = "Driver";
    public static final String PROPERTY_USER = "User";
    public static final String PROPERTY_PASSWORD = "Password";
    public static final String PROPERTY_URL = "Url";
    public static final String PROPERTY_SQL = "Sql";
    public static final String PROPERTY_DATASET_TYPE = "ReaderType";
    public static final String PROPERTY_CONNECTION = "Connection";
    public static final String PROPERTY_DESCRIPTION = "Description";
    public static final String PROPERTY_TYPE = "Type";
    public static final String PROPERTY_CELL = "Cell";
    public static final String PROPERTY_DELETE = "..DELETE";
    public static final PropertyDescriptor _CHILDREN = new PropertyDescriptor(PROPERTY_CHILDREN,
            ArrayList.class, PropertyDescriptor.SERIALIZABLE);
    public static final PropertyDescriptor _SINGLE_CHILD = new PropertyDescriptor("SingleChild",
            Boolean.TYPE, PropertyDescriptor.SERIALIZABLE);
    private static final String PROPERTY_FILL_HEIGHT = "FillHeight";
    public static PropertyDescriptor _FILL_HEIGHT = new PropertyDescriptor(PROPERTY_FILL_HEIGHT,
            Boolean.TYPE, PropertyDescriptor.SERIALIZABLE);
    public static final PropertyDescriptor _FILEPATH = new PropertyDescriptor(PROPERTY_FILEPATH,
            String.class, PropertyDescriptor.SERIALIZABLE);
    public static final PropertyDescriptor _VARIABLE_CONTEXT = new PropertyDescriptor(PROPERTY_VARIABLE_CONTEXT,
            VariableContext.class, PropertyDescriptor.SERIALIZABLE);
    public static final PropertyDescriptor _TYPE = new PropertyDescriptor(PROPERTY_TYPE,
            Integer.TYPE, PropertyDescriptor.SERIALIZABLE);
    public static final String PROPERTY_GROUP_BY = "GroupBy";
    public static final PropertyDescriptor _GROUP_BY = new PropertyDescriptor(PROPERTY_GROUP_BY,
            String.class, PropertyDescriptor.SERIALIZABLE);
    public static final PropertyDescriptor _POINTS1 = new PropertyDescriptor(PROPERTY_POINTS,
            ArrayList.class, PropertyDescriptor.SERIALIZABLE);
    public static final PropertyDescriptor _FOOTER = new PropertyDescriptor("Footer", Panel.class,
            PropertyDescriptor.SERIALIZABLE);
    public static PropertyDescriptor _LIMIT_ROWS = new PropertyDescriptor(PROPERTY_LIMIT_ROWS,
            Integer.TYPE);
    public static PropertyDescriptor _PRINT_CONTINUED = new PropertyDescriptor(PROPERTY_PRINT_CONTINUED,
            Boolean.TYPE);
    public static final PropertyDescriptor _HEADER = new PropertyDescriptor("Header", Panel.class,
            PropertyDescriptor.SERIALIZABLE);
    public static final PropertyDescriptor _BODY = new PropertyDescriptor("Body", Panel.class,
            PropertyDescriptor.SERIALIZABLE);
    public static final PropertyDescriptor _BACK_COLOR = new PropertyDescriptor(PROPERTY_BACK_COLOR,
            Color.class);
    public static final PropertyDescriptor _VARIABLE = new PropertyDescriptor(PROPERTY_VARIABLE,
            String.class);
    public static final String PROPERTY_NODE_PATH = "NodePath";
    public static final PropertyDescriptor _NODE_PATH = new PropertyDescriptor(PROPERTY_NODE_PATH,
            String.class);
    public static final String PROPERTY_INSERTABLE = "Insertable";
    public static final PropertyDescriptor _INSERTABLE = new PropertyDescriptor(PROPERTY_INSERTABLE,
            Boolean.TYPE);
    public static final PropertyDescriptor _READER_VARIABLE = new PropertyDescriptor(PROPERTY_READER_VARIABLE,
            String.class, PropertyDescriptor.SERIALIZABLE);
    public static final PropertyDescriptor _BORDER = new PropertyDescriptor(PROPERTY_BORDER,
            Border.class);
    public static final PropertyDescriptor _CELL = new PropertyDescriptor(PROPERTY_CELL,
            Cell.class, PropertyDescriptor.SERIALIZABLE);
    public static final PropertyDescriptor _CAPTION = new PropertyDescriptor(PROPERTY_CAPTION,
            String.class);
    public static final PropertyDescriptor _TEXT = new PropertyDescriptor(PROPERTY_TEXT,
            String.class);
    public static final PropertyDescriptor _FONT = new PropertyDescriptor(PROPERTY_FONT, Font.class);
    public static final PropertyDescriptor _FORE_COLOR = new PropertyDescriptor(PROPERTY_FORE_COLOR,
            Color.class);
    public static final PropertyDescriptor _FORMAT = new PropertyDescriptor(PROPERTY_FORMAT,
            Format2.class, PropertyDescriptor.DEFAULT | PropertyDescriptor.RESOLVABLE);
    public static final String PROPERTY_CODE_TYPE = "CodeType";
    public static final PropertyDescriptor _CODE_TYPE = new PropertyDescriptor(PROPERTY_CODE_TYPE,
            String.class);
    public static final String PROPERTY_TITLE = "Title";
    public static final PropertyDescriptor _TITLE = new PropertyDescriptor(PROPERTY_TITLE,
            String.class);
    public static final PropertyDescriptor _STRING_VALUE = new PropertyDescriptor(RROPERTY_STRING_VALUE,
            String.class);
    public static final PropertyDescriptor _HEIGHT = new PropertyDescriptor(PROPERTY_HEIGHT,
            Integer.TYPE, PropertyDescriptor.NODEFAULT | PropertyDescriptor.DEFAULT);
    public static PropertyDescriptor _PRINT_STYLE = new PropertyDescriptor(PROPERTY_PRINT_STYLE,
            String.class);
    public static final String PROPERTY_NO_PRINT = "NoPrint";
    public static PropertyDescriptor _NO_PRINT = new PropertyDescriptor(PROPERTY_NO_PRINT,
            Boolean.TYPE);
    private static final String PROPERTY_VERSION = "Version";
    public static final PropertyDescriptor _SHOW_CASE = new PropertyDescriptor(PROPERTY_SHOW_CASE,
            Integer.TYPE);
    public static final PropertyDescriptor _VERSION = new PropertyDescriptor(PROPERTY_VERSION,
            Double.TYPE, PropertyDescriptor.SERIALIZABLE);
    public static final PropertyDescriptor _ID = new PropertyDescriptor("Id", String.class,
            PropertyDescriptor.SERIALIZABLE);
    public static final PropertyDescriptor _X = new PropertyDescriptor(PROPERTY_X, Integer.TYPE);
    public static final PropertyDescriptor _LINE_PATTERN = new PropertyDescriptor(PROPERTY_LINE_PATTERN,
            Integer.TYPE);
    public static final PropertyDescriptor _LINE_SIZE = new PropertyDescriptor(PROPERTY_LINE_SIZE,
            Float.TYPE);
    public static final PropertyDescriptor _NAME = new PropertyDescriptor(PROPERTY_NAME,
            String.class);
    public static final String PROPERTY_FONT_SIZE = "Font.Size";
    public static final PropertyDescriptor _BREAK_PAGE = new PropertyDescriptor(PROPERTY_BREAK_PAGE,
            Boolean.TYPE);
    public static final PropertyDescriptor _HORIZONTAL_ALIGNMENT = new PropertyDescriptor(PROPERTY_HORIZONTAL_ALIGNMENT,
            Integer.TYPE);
    public static final PropertyDescriptor _VERTICAL_ALIGNMENT = new PropertyDescriptor(PROPERTY_VERTICAL_ALIGNMENT,
            Integer.TYPE);
    public static final PropertyDescriptor _SOURCE = new PropertyDescriptor(PROPERTY_SOURCE,
            String.class);
    public static final PropertyDescriptor _Y = new PropertyDescriptor(PROPERTY_Y, Integer.TYPE);
    public static final PropertyDescriptor _WIDTH = new PropertyDescriptor(PROPERTY_WIDTH,
            Integer.TYPE, PropertyDescriptor.NODEFAULT | PropertyDescriptor.DEFAULT);
    public static final PropertyDescriptor _LEFT = new PropertyDescriptor(PROPERTY_LEFT,
            Integer.TYPE);
    public static final String PROPERTY_CHAR_WIDTH = "CharWidth";
    public static final PropertyDescriptor _CHAR_WIDTH = new PropertyDescriptor(PROPERTY_CHAR_WIDTH,
            Integer.TYPE);
    public static final PropertyDescriptor _TOP = new PropertyDescriptor(PROPERTY_TOP, Integer.TYPE);
    public static final PropertyDescriptor _RIGHT = new PropertyDescriptor(PROPERTY_RIGHT,
            Integer.TYPE);
    public static final PropertyDescriptor _BOTTOM = new PropertyDescriptor(PROPERTY_BOTTOM,
            Integer.TYPE);
    public static final PropertyDescriptor _DATASETS = new PropertyDescriptor(PROPERTY_DATASETS,
            ArrayList.class, PropertyDescriptor.SERIALIZABLE);
    public static final String PROPERTY_READER = "Reader";
    public static final PropertyDescriptor _READER = new PropertyDescriptor(PROPERTY_READER,
            DatasetReader.class, PropertyDescriptor.SERIALIZABLE | PropertyDescriptor.RESOLVABLE);
    public static final PropertyDescriptor _INDEX = new PropertyDescriptor(PROPERTY_INDEX,
            Integer.TYPE, PropertyDescriptor.SERIALIZABLE);
    public static final PropertyDescriptor _ORIENTATION = new PropertyDescriptor(PROPERTY_ORIENTATION,
            Integer.TYPE, PropertyDescriptor.SERIALIZABLE);
    public static final String PROPERTY_COLUMN_WIDTHS = "ColumnWidths";
    public static final String PROPERTY_ROW_HEIGHTS = "RowHeights";
    public static final PropertyDescriptor _URL = new PropertyDescriptor(PROPERTY_URL, String.class);
    private static final String PROPERTY_HYPERLINK = "Hyperlink";
    public static final PropertyDescriptor _HYPERLINK = new PropertyDescriptor(PROPERTY_HYPERLINK,
            Hyperlink.class);
    public static final PropertyDescriptor _COLUMN_WIDTHS = new PropertyDescriptor(PROPERTY_COLUMN_WIDTHS,
            int[].class, PropertyDescriptor.SERIALIZABLE);
    public static final PropertyDescriptor _ROW_HEIGHTS = new PropertyDescriptor(PROPERTY_ROW_HEIGHTS,
            int[].class, PropertyDescriptor.SERIALIZABLE);
    public static final PropertyDescriptor _AFTERPRINT = new PropertyDescriptor(PROPERTY_AFTERPRINT,
            String.class, PropertyDescriptor.SERIALIZABLE);
    public static PropertyDescriptor _BEFOREPRINT2 = new PropertyDescriptor(PROPERTY_BEFOREPRINT2,
            String.class, PropertyDescriptor.SERIALIZABLE);
    public static PropertyDescriptor _INIT_PRINT = new PropertyDescriptor(PROPERTY_INIT_PRINT,
            String.class, PropertyDescriptor.SERIALIZABLE);
    public static PropertyDescriptor _FORM_SETTINGS = new PropertyDescriptor("FormSettings",
            Properties.class, PropertyDescriptor.SERIALIZABLE);
    public static final String PROPERTY_TOOLTIP_TEXT = "TooltipText";
    public static PropertyDescriptor _TOOLTIP_TEXT = new PropertyDescriptor(PROPERTY_TOOLTIP_TEXT,
            String.class);
    public static final String PROPERTY_IMAGE_SRC = "ImageSrc";
    public static PropertyDescriptor _IMAGE_SRC = new PropertyDescriptor("ImageSrc", String.class);
    public static final String PROPERTY_BACKGROUND_IMAGE = "BackgroundImageStyle";
    public static PropertyDescriptor _BACKGROUND_IMAGE = new PropertyDescriptor(PROPERTY_BACKGROUND_IMAGE,
            String.class);
    public static final String PROPERTY_PAGE_FORMAT = "PageFormat";
    public static PropertyDescriptor _PAGE_FORMAT = new PropertyDescriptor(PROPERTY_PAGE_FORMAT,
            PageFormat.class, PropertyDescriptor.SERIALIZABLE);
    public static PropertyDescriptor _RIGHT_FLOW = new PropertyDescriptor("RightFlow",
            Boolean.TYPE, PropertyDescriptor.SERIALIZABLE);
    public static PropertyDescriptor _ROW_KEY_EXPRESSIONS = new PropertyDescriptor("RowkeyExpressions",
            Properties.class, PropertyDescriptor.SERIALIZABLE);
    public static PropertyDescriptor _COLUMN_KEY_EXPRESSIONS = new PropertyDescriptor("ColumnkeyExpressions",
            Properties.class, PropertyDescriptor.SERIALIZABLE);
    public static final PropertyDescriptor _CONSTRAINTS = new PropertyDescriptor("Constraints",
            String.class);
    public static final String PROPERTY_REQUIRED_HTML_IMAGE_FORMAT = "ExportImageFormat";
    public static final PropertyDescriptor _REQUIRED_HTML_IMAGE_FORMAT2 = new PropertyDescriptor(PROPERTY_REQUIRED_HTML_IMAGE_FORMAT,
            Integer.TYPE);
    public static final String PROPERTY_STRECTHES = "Stretches";
    public static final PropertyDescriptor _STRECTHES = new PropertyDescriptor(PROPERTY_STRECTHES,
            Boolean.TYPE);
    public static final PropertyDescriptor _WORDWRAP = new PropertyDescriptor(PROPERTY_WORDWRAP,
            Boolean.TYPE, PropertyDescriptor.DEFAULT);
    public static final PropertyDescriptor _WORDWRAP2 = new PropertyDescriptor(PROPERTY_WORDWRAP2,
            Integer.TYPE, PropertyDescriptor.DEFAULT);
    public static final PropertyDescriptor _RIGHT_PADDING = new PropertyDescriptor("RightPadding",
            Integer.TYPE, PropertyDescriptor.DEFAULT);
    public static final String PROPERTY_ADD = "..ADD";
    public static final String PROPERTY_PARENT_CHANGE = "..parent.change";
    public static final PropertyDescriptor _UNITED_LEVEL = new PropertyDescriptor("UnitedLevel",
            Integer.TYPE);
    public static final PropertyDescriptor _PROPERTIES = new PropertyDescriptor("Properties",
            Properties.class, PropertyDescriptor.SERIALIZABLE);
    public static final PropertyDescriptor _LABEL_FIELD = new PropertyDescriptor("LabelField",
            String.class, PropertyDescriptor.SERIALIZABLE);
    public static final PropertyDescriptor _PLOT_DATA = new PropertyDescriptor("PlotData",
            ArrayList.class, PropertyDescriptor.SERIALIZABLE);
    public static final String PROPERTY_USER_DEFINED_COLUMNS = "UserDefinedColumns";
    public static final PropertyDescriptor _USER_DEFINED_COLUMNS = new PropertyDescriptor(PROPERTY_USER_DEFINED_COLUMNS,
            ArrayList.class, PropertyDescriptor.SERIALIZABLE);
}
