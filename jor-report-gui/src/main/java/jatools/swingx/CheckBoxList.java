package jatools.swingx;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;


/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.2 $
 * @author $author$
 */
public class CheckBoxList extends JList {
    public static final String ACTION_CHANGE_SELECTION = "clicked"; //
    public static final String ACTION_FOCUSED_INDEX_CHANGE = "focuse.change"; //
    static final int TEXT_X = 25;

    /**
 * Creates a new ZCheckBoxList object.
 *
 * @param listData DOCUMENT ME!
 */
    ArrayList actionListeners;
    private boolean textClickEnable = true;

    /**
 * DOCUMENT ME!
 *
 * @param textClickEnable DOCUMENT ME!
 */
    private int focusedIndex = 0;

    /**
 * Creates a new ZCheckBoxList object.
 *
 * @param listData DOCUMENT ME!
 */
    public CheckBoxList(Object[] listData) {
        super(createItems(listData));

        setCellRenderer(new CellRenderer());
        setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        setBorder(new EmptyBorder(0, 4, 0, 0));
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int index = locationToIndex(e.getPoint());
                setFocusedIndex(index);

                if (!textClickEnable && (e.getX() > TEXT_X)) {
                    return;
                }

                CheckableItem item = (CheckableItem) getModel().getElementAt(index);
                item.setSelected(!item.isSelected());

                Rectangle rect = getCellBounds(index, index);
                repaint(rect);
                fireActionListener(ACTION_CHANGE_SELECTION);
            }
        });
    }
    
    public void selectItem(int index,boolean selected)
	{
    	CheckableItem item = (CheckableItem) getModel().getElementAt(index);
    	item.setSelected(selected);
    	
    }
    
    

    /**
 * DOCUMENT ME!
 *
 * @param pos DOCUMENT ME!
 */
    public void setFocusedIndexAt(Point pos) {
        int i = locationToIndex(pos);
        setFocusedIndex(i);
    }

    /**
 * DOCUMENT ME!
 *
 * @param textClickEnable DOCUMENT ME!
 */
    public void setTextClickEanble(boolean textClickEnable) {
        this.textClickEnable = textClickEnable;
    }

    /**
 * DOCUMENT ME!
 *
 * @param index DOCUMENT ME!
 *
 * @return DOCUMENT ME!
 */
    public Object getItemAt(int index) {
        CheckableItem item = (CheckableItem) getModel().getElementAt(index);

        return item.getItem();
    }

    /**
 * DOCUMENT ME!
 *
 * @param lst DOCUMENT ME!
 */
    public void addActionListener(ActionListener lst) {
        if (actionListeners == null) {
            actionListeners = new ArrayList();
        }

        actionListeners.add(lst);
    }

    /**
 * DOCUMENT ME!
 *
 * @param lst DOCUMENT ME!
 */
    public void removeAcitonListener(ActionListener lst) {
        if (actionListeners == null) {
            return;
        }

        actionListeners.remove(lst);
    }

    /**
 * DOCUMENT ME!
 */
    private void fireActionListener(String command) {
        if ((actionListeners == null) || actionListeners.isEmpty()) {
            return;
        }

        ActionEvent evt = new ActionEvent(this, 0, command);

        for (int i = 0; i < actionListeners.size(); i++) {
            ActionListener lst = (ActionListener) actionListeners.get(i);
            lst.actionPerformed(evt);
        }
    }

    /**
 * DOCUMENT ME!
 *
 * @param listData DOCUMENT ME!
 *
 * @return DOCUMENT ME!
 */
    public static Object[] createItems(Object[] listData) {
        int n = listData.length;
        CheckableItem[] items = new CheckableItem[n];

        for (int i = 0; i < n; i++) {
            items[i] = new CheckableItem(listData[i]);
        }

        return items;
    }

    /**
 * DOCUMENT ME!
 *
 * @return DOCUMENT ME!
 */
    public Object[] getSelectedItems() {
        ArrayList selectedItems = new ArrayList();

        for (int i = 0; i < getModel().getSize(); i++) {
            CheckableItem item = (CheckableItem) getModel().getElementAt(i);

            if (item.isSelected()) {
                selectedItems.add(item.getItem());
            }
        }

        return selectedItems.toArray();
    }

    /**
 * DOCUMENT ME!
 *
 * @param value DOCUMENT ME!
 * @param selected DOCUMENT ME!
 */
    public void setSelectedValue(Object value, boolean selected) {
        if (value == null) {
            return;
        }

        for (int i = 0; i < getModel().getSize(); i++) {
            CheckableItem item = (CheckableItem) getModel().getElementAt(i);

            if (value.equals(item.getItem())) {
                item.setSelected(selected);

                return;
            }
        }
    }

    /**
 * DOCUMENT ME!
 *
 * @return DOCUMENT ME!
 */
    public int getFocusedIndex() {
        return focusedIndex;
    }

    /**
 * DOCUMENT ME!
 *
 * @param focusedIndex DOCUMENT ME!
 */
    public void setFocusedIndex(int focusedIndex) {
        if (this.focusedIndex == focusedIndex) {
            return;
        }

        this.focusedIndex = focusedIndex;
        fireActionListener(CheckBoxList.ACTION_FOCUSED_INDEX_CHANGE);
        repaint();
    }

    class CellRenderer extends JCheckBox implements ListCellRenderer {
        public CellRenderer() {
            setBackground(UIManager.getColor("List.textBackground")); //
            setForeground(UIManager.getColor("List.textForeground")); //
        }

        public Component getListCellRendererComponent(JList list, Object value, int index,
                                                      boolean isSelected,
                                                      boolean hasFocus) {
            setEnabled(list.isEnabled());
            setSelected(((CheckableItem) value).isSelected());
            setFont(list.getFont());
            setText(value.toString());

            if (index == focusedIndex) {
                setBackground(Color.BLUE);
                setForeground(Color.white);
            } else {
                setBackground(UIManager.getColor("List.textBackground")); //
                setForeground(UIManager.getColor("List.textForeground")); //
            }

            return this;
        }
    }
}
