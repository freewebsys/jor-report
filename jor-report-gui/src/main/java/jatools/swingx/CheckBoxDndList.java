package jatools.swingx;

import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;


/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.1 $
 * @author $author$
 */
public class CheckBoxDndList extends JListX {
    static final int TEXT_X = 25;
    Map selectionCache = new HashMap();
    IconableCellRender render;
    boolean change = false;
    private boolean textClickEnable = false;

    /**
     * Creates a new ZCheckBoxDndList object.
     *
     * @param listData DOCUMENT ME!
     */
    public CheckBoxDndList(Object[] listData) {
        super(listData);
        render = new IconableCellRender();
        setCellRenderer(render);
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setBorder(new EmptyBorder(0, 4, 0, 0));
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int index = locationToIndex(e.getPoint());

                if (!textClickEnable && (e.getX() > TEXT_X)) {
                    return;
                }

                if (index > -1) {
                    setSelected(index, !isSelected(index));
                    change = true;

                    Rectangle rect = getCellBounds(index, index);
                    repaint(rect);
                }
            }
        });
    }

    /**
     * DOCUMENT ME!
     *
     * @param icon DOCUMENT ME!
     */
    public void setSelectedIcon(Icon icon) {
        render.setSelectedIcon(icon);
    }

    /**
     * DOCUMENT ME!
     *
     * @param icon DOCUMENT ME!
     */
    public void setNoSelectedIcon(Icon icon) {
        render.setNoSelectedIcon(icon);
    }

    /**
     * DOCUMENT ME!
     */
    public void applyChange() {
        change = false;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isChanged() {
        return change;
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean isSelected(int index) {
        Boolean selected = (Boolean) selectionCache.get(getModel().getElementAt(index));

        return (selected == null) ? false : selected.booleanValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @param index DOCUMENT ME!
     * @param selected DOCUMENT ME!
     */
    void setSelected(int index, boolean selected) {
        selectionCache.put(getModel().getElementAt(index), new Boolean(selected));
    }

    /**
     * DOCUMENT ME!
     *
     * @param textClickEnable DOCUMENT ME!
     */
    public void setTextClickEanble(boolean textClickEnable) {
        this.textClickEnable = textClickEnable;
    }


}
