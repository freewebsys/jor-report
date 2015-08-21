package jatools.util;

import jatools.designer.App;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import java.util.Date;

import javax.swing.ImageIcon;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision: 1.3 $
  */
public class CursorUtil {
    /**
     * DOCUMENT ME!
     */
    public final static Cursor CELL_SELECT_CURSOR = createCursor((ImageIcon) Util.getIcon(
                "/jatools/icons/cursor0.gif")); //
    public final static Cursor CELL_MERGE_CURSOR = createCursor((ImageIcon) Util.getIcon(
                "/jatools/icons/mergecur.gif"), 15, 17); //

    /**
     * DOCUMENT ME!
     */
    public final static Cursor RESIZE_ROW_CURSOR = Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR);

    /**
     * DOCUMENT ME!
     */
    public final static Cursor RESIZE_COL_CURSOR = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);

    /**
     * DOCUMENT ME!
     */
    public final static Cursor MOVE_CURSOR = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);

    /**
     * DOCUMENT ME!
     */
    public final static Cursor DEFAULT_CURSOR = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);

    /**
     * DOCUMENT ME!
     */
    public final static Cursor THIN_CROSS_CURSOR = createCursor((ImageIcon) Util.getIcon(
                "/jatools/icons/thincross.gif"));

    /**
    * DOCUMENT ME!
    */
    public static Cursor CLICK_PLAY_CURSOR = createCursor((ImageIcon) Util.getIcon(
                "/jatools/icons/clickplay.gif"), 5, 5);

    /**
     * DOCUMENT ME!
     */
    public static Cursor FORMAT_CURSOR = createCursor((ImageIcon) Util.getIcon(
                "/jatools/icons/formatcursor.gif"), 3, 6);

    /**
     * DOCUMENT ME!
     */
    public final static Cursor NW_RESIZE_CURSOR = Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR);

    /**
     * DOCUMENT ME!
     */
    public final static Cursor NE_RESIZE_CURSOR = Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);

    /**
     * DOCUMENT ME!
     */
    public final static Cursor SW_RESIZE_CURSOR = Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR);

    /**
     * DOCUMENT ME!
     */
    public final static Cursor SE_RESIZE_CURSOR = Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR);

    /**
     * DOCUMENT ME!
     */
    public final static Cursor E_RESIZE_CURSOR = Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR);

    /**
     * DOCUMENT ME!
     */
    public final static Cursor S_RESIZE_CURSOR = Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR);

    /**
    * DOCUMENT ME!
    *
    * @param icon
    *            DOCUMENT ME!
    *
    * @return DOCUMENT ME!
    */
    public static Cursor createCursor(ImageIcon icon) {
        return createCursor(icon, 16, 16);
    }

    /**
     * DOCUMENT ME!
     *
     * @param icon
     *            DOCUMENT ME!
     * @param x
     *            DOCUMENT ME!
     * @param y
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static Cursor createCursor(ImageIcon icon, int x, int y) {
        Toolkit tool = Toolkit.getDefaultToolkit();
        Dimension cursorSize = tool.getBestCursorSize(32, 32);
        int colors = tool.getMaximumCursorColors();

        if (cursorSize.equals(new Dimension(0, 0)) || (colors == 0)) {
            return null;
        }

        try {
            if (icon == null) {
                throw new Exception(App.messages.getString("res.8")); //
            }

            return tool.createCustomCursor(icon.getImage(), new Point(x, y),
                "cursor." + new Date().getTime()); //
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
