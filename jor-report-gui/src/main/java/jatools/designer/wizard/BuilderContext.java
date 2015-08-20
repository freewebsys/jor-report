package jatools.designer.wizard;

import jatools.designer.App;

import java.awt.Font;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
  */
public class BuilderContext {
    public static final String READER = "reader";
    public static final String TEXT_FOR_DESIGNER = "XXXXXXX";
    public static final String SUM_FIELD_PREFIX = "$sum_";
    public static final String PAGE_SIZE = "page.size";
    public static final String COLUMN_COUNT = "column.count";
    public static final String COLUMN_SPACING = "column.spacing";
    public static final String PAGE_FORMAT = "page.format";
    public static final String DISPLAY_ITEMS = "display.items";
    public static final String GROUP_ITEMS = "group.items";
    public static final String SUMMARY_ITEMS = "summary.items";
    public static final String TITLE = "title";
    public static final String WHERE_PAGE_NUMBER = "where.page.number";
    public static final String TABLE_MODE = "table.mode";
    public static final int ITEM_LEFT_MARGIN = 100;
    public static final int REPORT_TITLE_MARGIN = 15;
    public static final int ITEM_VERTICAL_MARGIN = 8;
    public static final int ITEM_HORIZONTAL_MARGIN = 4;
    public static final String STATIC_TABLE = "static.table";
    public static final Font TITLE_FONT = new Font(App.messages.getString("res.21"), Font.BOLD, 26);
    public static final int ITEM_HEIGHT = 20;
    public static final int ITEM_WIDTH = 90;
    public static final String ALIAS_LOOKER = "alias_looker";
    Map valueCache = new HashMap();

    /**
     * Creates a new BuilderContext object.
     */
    public BuilderContext() {
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getValue(String key) {
        return valueCache.get(key);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("{ZBuilderContext:");

        Iterator i = valueCache.entrySet().iterator();
        boolean hasNext = i.hasNext();

        while (hasNext) {
            Entry e = (Entry) (i.next());
            Object key = e.getKey();
            Object value = e.getValue();

            if (value instanceof Object[]) {
                buf.append(((key == this) ? "(this Map)" : key) + "=");

                Object[] values = (Object[]) value;

                for (int i0 = 0; i0 < values.length; i0++) {
                    buf.append("[" + i0 + "]: ");
                    buf.append(values[i0]);
                    buf.append("\n");
                }
            } else {
                buf.append(((key == this) ? "(this Map)" : key) + "=" +
                    ((value == this) ? "(this Map)" : value));
            }

            hasNext = i.hasNext();

            if (hasNext) {
                buf.append("\n");
            }
        }

        buf.append("}");

        return buf.toString();
    }

    /**
     * DOCUMENT ME!
     *
     * @param key DOCUMENT ME!
     * @param value DOCUMENT ME!
     */
    public void setValue(String key, Object value) {
        valueCache.put(key, value);
    }
}
