package jatools.designer.action;

/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.1 $
 * @author $author$
 */
public interface ActionConstants {
    final static int FILE_NEW = 0;
    final static int FILE_OPEN = FILE_NEW + 1;
    final static int FILE_SAVE = FILE_OPEN + 1;
    final static int FILE_SAVEAS = FILE_SAVE + 1;
    final static int FILE_UPLOAD = FILE_SAVEAS + 1;
    final static int FILE_DOWNLOAD = FILE_UPLOAD + 1;
    final static int FILE_RECENT = FILE_DOWNLOAD + 1;
    final static int FILE_EXIT = FILE_RECENT + 1;
    final static int EDIT_UNDO = FILE_EXIT + 1;
    final static int EDIT_REDO = EDIT_UNDO + 1;
    final static int EDIT_CUT = EDIT_REDO + 1;
    final static int EDIT_COPY = EDIT_CUT + 1;
    final static int EDIT_PASTE = EDIT_COPY + 1;
    final static int EDIT_DELETE = EDIT_PASTE + 1;
    final static int LAF_METAL_LOOKS = EDIT_DELETE + 1;
    final static int LAF_MOTIF_LOOKS = LAF_METAL_LOOKS + 1;
    final static int LAF_COMPIERE_LOOKS = LAF_MOTIF_LOOKS + 1;
    final static int LAF_WINDOWS_LOOKS = LAF_COMPIERE_LOOKS + 1;
    final static int INSERT_TOGGLE_SELECTION = LAF_WINDOWS_LOOKS + 1;
    final static int INSERT_LABEL = INSERT_TOGGLE_SELECTION + 1;
    final static int INSERT_TEXT = INSERT_LABEL + 1;
    final static int INSERT_TEXT_STYLED = INSERT_TEXT + 1;
    final static int INSERT_IMAGE = INSERT_TEXT_STYLED + 1;
    final static int INSERT_LINE = INSERT_IMAGE + 1;
    final static int INSERT_RECTANGLE = INSERT_LINE + 1;
    final static int INSERT_OVAL = INSERT_RECTANGLE + 1;
    final static int INSERT_TABLE = INSERT_OVAL + 1;
    final static int INSERT_CHART = INSERT_TABLE + 1;
    final static int INSERT_BAND = INSERT_CHART + 1;
    final static int INSERT_HEADER_BAND = INSERT_BAND + 1;
    final static int INSERT_FOOTER_BAND = INSERT_HEADER_BAND + 1;
    final static int INSERT_DETAIL_BAND = INSERT_FOOTER_BAND + 1;
    final static int INSERT_DATABASE = INSERT_DETAIL_BAND + 1;
    final static int INSERT_RECORDSET = INSERT_DATABASE + 1;
    final static int DATA_DATASET_MANAGER = INSERT_RECORDSET + 1;
    final static int FORMAT_BRING_FRONT = DATA_DATASET_MANAGER + 1;
    final static int FORMAT_BRING_BACK = FORMAT_BRING_FRONT + 1;
    final static int FORMAT_ALIGN = FORMAT_BRING_BACK + 1;
    final static int FORMAT_ALIGN_LEFT = FORMAT_ALIGN + 1;
    final static int FORMAT_ALIGN_RIGHT = FORMAT_ALIGN_LEFT + 1;
    final static int FORMAT_ALIGN_X_CENTER = FORMAT_ALIGN_RIGHT + 1;
    final static int FORMAT_ALIGN_TOP = FORMAT_ALIGN_X_CENTER + 1;
    final static int FORMAT_ALIGN_BOTTOM = FORMAT_ALIGN_TOP + 1;
    final static int FORMAT_ALIGN_Y_CENTER = FORMAT_ALIGN_BOTTOM + 1;
    final static int FORMAT_CENTER_ON_PARENT = FORMAT_ALIGN_Y_CENTER + 1;
    final static int FORMAT_RESIZE = FORMAT_CENTER_ON_PARENT + 1;
    final static int FORMAT_RESIZE_HEIGHT = FORMAT_RESIZE + 1;
    final static int FORMAT_RESIZE_WIDTH = FORMAT_RESIZE_HEIGHT + 1;
    final static int FORMAT_RESIZE_BOTH = FORMAT_RESIZE_WIDTH + 1;
    final static int FORMAT_GROUP_LEVEL = FORMAT_RESIZE_BOTH + 1;
    final static int FORMAT_PAGE_FORMAT = FORMAT_GROUP_LEVEL + 1;
    final static int FILE_OPEN_MRU = Integer.MAX_VALUE;
    final static int MENUITEM = 1;
    final static int MENU = 2;
    final static int RADIOMENU = 4;
    final static int CHECKBOXMENU = 8;
    final static int BUTTON = 16;
    final static int TOGGLEBUTTON = 32;
    final static int SELECTED = 64;
    final static int ACTION_IDLE = 0;
    final static int ACTION_PREPARE_DRAWING = 2;
    final static int ACTION_DRAWING = 3;
    final static int ACTION_MOVING = 4;
    final static int ACTION_MOVING_POINT = 5;
    final static int ACTION_POINT_MOVING = 6;
    final static int ACTION_FRAME_SELECTING = 7;
    final static int ACTION_ADJUST_RIGHT_MARGIN = 8;
    final static int ACTION_ADJUST_BOTTOM_MARGIN = 9;
    final static int ACTION_ADJUST_BOTH_MARGIN = 10;
    final static int ACTION_DRAG_AND_DROP_MOVING = 11;
    final static int HIT_TEST_MISTAKE = 3;
    final static int SPACING = 8;
    final static int LINE_WIDTH = 1;
    final static int WIDTH = 1;
    final static int HEIGHT = 2;
    final static int BOTH = 3;
    final static int LEFT = 1;
    final static int CENTER = 2;
    final static int RIGHT = 3;
    final static int TOP = 4;
    final static int MIDDLE = 5;
    final static int BOTTOM = 6;
}
