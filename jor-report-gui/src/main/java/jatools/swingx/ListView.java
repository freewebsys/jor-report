/*
 *   Author: John.
 *
 *   杭州杰创软件   All Copyrights Reserved.
 */
package jatools.swingx;

import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionListener;


/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.2 $
 * @author $author$
 */
public class ListView extends JPanel {
    public static final int VERTICAL = JList.VERTICAL;
    public static final int VERTICAL_WRAP = JList.VERTICAL_WRAP;
    public static final int HORIZONTAL_WRAP = JList.HORIZONTAL_WRAP;
    public static final String USER_OBJECT = "user.object"; //
    protected JScrollPane scroll;
    protected _ScrollableList list;
    Object selectedUserObject;
    


    /**
     * Creates a new ZListView object.
     *
     * @param items
     *            DOCUMENT ME!
     */
    public ListView(
        JLabel[] items,
        int listLayout) {
        setLayout(new GridLayout());

        //add(BorderLayout.NORTH, toolbar = new ListViewBar());
        list = new _ScrollableList(items);
        scroll = new JScrollPane(list);
        add(scroll);
        scroll.getViewport().setBackground(list.getBackground());
        setLayoutOrientation(listLayout);
    }
    public void addListSelectionListener(ListSelectionListener x)
    {
    	if(list != null)
    	{
    	   list.addListSelectionListener( x);
    	}
    }
    

    /**
     * DOCUMENT ME!
     *
     * @param event
     *            DOCUMENT ME!
     */
    public void setLayoutOrientation(int listLayout) {
        list.setLayoutOrientation(listLayout);

        if (listLayout == JList.VERTICAL) {
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        }

        if (listLayout == JList.VERTICAL_WRAP) {
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
            scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        }

        if (listLayout == JList.HORIZONTAL_WRAP) {
            scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        }

        scroll.revalidate();
    }

    /**
     * @return Returns the list.
     */
    public JList getList() {
        return list;
    }

    /**
     * @return Returns the selectedUserObject.
     */
    public Object getSelectedUserObject() {
        JComponent obj = (JComponent) list.getSelectedValue();

        return (obj != null) ? obj.getClientProperty(USER_OBJECT) : null;
    }

    /**
     * @param selectedUserObject
     *            The selectedUserObject to set.
     */
    public void setSelectedUserObject(Object selectedUserObject) {
        for (int i = 0; i < list.getModel().getSize(); i++) {
            JComponent obj = (JComponent) list.getModel().getElementAt(i);
            Object value = obj.getClientProperty(USER_OBJECT);
            boolean equal = (value != null) ? value.equals(selectedUserObject) : (selectedUserObject == null);

            if (equal) {
                list.setSelectedIndex(i);

                break;
            }
        }
    }

    class _ScrollableList extends JList {
        protected boolean trackWidth = true;
        protected boolean trackHeight = false;

        public _ScrollableList(Object[] list) {
            super(list);
        }

        public int getVisibleRowCount() {
            return 0;
        }

        public boolean getScrollableTracksViewportWidth() {
            return trackWidth;
        }

        public void setScrollableTracksViewportWidth(boolean trackWidth) {
            this.trackWidth = trackWidth;
        }

        public boolean getScrollableTracksViewportHeight() {
            return trackHeight;
        }

        public void setScrollableTracksViewportHeight(boolean trackHeight) {
            this.trackHeight = trackHeight;
        }

        public void setLayoutOrientation(int orientation) {
            super.setLayoutOrientation(orientation);

            if (orientation == JList.VERTICAL) {
                setScrollableTracksViewportWidth(true);
                setScrollableTracksViewportHeight(false);
            }

            if (orientation == JList.VERTICAL_WRAP) {
                setScrollableTracksViewportWidth(false);
                setScrollableTracksViewportHeight(true);
            }

            if (orientation == JList.HORIZONTAL_WRAP) {
                setScrollableTracksViewportWidth(true);
                setScrollableTracksViewportHeight(false);
            }

            revalidate();
        }
    }

	/**
	 * @param i
	 */
	public void setSelectedIndex(int i) {
		list.setSelectedIndex( 0);
	}
}
