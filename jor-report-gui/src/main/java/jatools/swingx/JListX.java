package jatools.swingx;

import jatools.swingx.dnd.JatoolsDragSource;
import jatools.swingx.dnd.JatoolsDropTarget;
import jatools.swingx.dnd.Moveable;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;



/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.1 $
 * @author $author$
 */
public class JListX extends JList implements Moveable {
    public static final int ACTION_CUT = 0;
    public static final int ACTION_COPY = 1;
    protected int completeAction = ACTION_CUT;
    protected DefaultListModel model;
    JatoolsDropTarget clickTarget;
    boolean singleElement;

    /**
     * Creates a new ZList object.
     *
     * @param data
     *            DOCUMENT ME!
     */
    public JListX(Object[] data) {
        init(Arrays.asList(data));
    }

    /**
     * Creates a new ZList object.
     */
    public JListX() {
        init(new ArrayList());
    }

    /**
     * Creates a new ZList object.
     *
     * @param dataList
     *            DOCUMENT ME!
     */
    public JListX(ArrayList dataList) {
        init(dataList);
    }

    /**
     * @return Returns the singleElement.
     */
    public boolean isSingleElement() {
        return singleElement;
    }

    /**
     * @param singleElement
     *            The singleElement to set.
     */
    public void setSingleElement(boolean singleElement) {
        this.singleElement = singleElement;
    }

    /**
     * DOCUMENT ME!
     *
     * @param item
     *            DOCUMENT ME!
     */
    public void addItem(Object item) {
        model.addElement(item);
    }

    /**
     * DOCUMENT ME!
     *
     * @param i DOCUMENT ME!
     */
    public void removeItem(int i) {
        model.remove(i);
    }

    /**
     * DOCUMENT ME!
     *
     * @param dataList
     *            DOCUMENT ME!
     */
    private void init(List dataList) {
        setItems(dataList);
        this.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent e) {
                    if (e.getClickCount() > 1) {
                        doubleClick();
                    }
                }
            });
    }

    /**
     * DOCUMENT ME!
     *
     * @param evt
     *            DOCUMENT ME!
     */
    public void doubleClick() {
        if (clickTarget != null) {
            clickTarget.drop(this);
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param listData
     *            DOCUMENT ME!
     */
    public void setItems(List listData) {
        model = new DefaultListModel();

        for (int i = 0; i < listData.size(); i++) {
            model.add(i, listData.get(i));
        }

        setModel(model);
    }

    /**
     * DOCUMENT ME!
     */
    public void clearItems() {
        model.removeAllElements();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public ArrayList getItems() {
        return new ArrayList(Arrays.asList(model.toArray()));
    }



    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public DefaultListModel getDefaultModel() {
        return this.model;
    }

    /**
     * DOCUMENT ME!
     *
     * @param clickTarget
     *            DOCUMENT ME!
     */
    public void setDoublClickTo(JatoolsDropTarget clickTarget) {
        this.clickTarget = clickTarget;
    }

    /**
     * DOCUMENT ME!
     */
    public void complete(boolean whole) {
        if (completeAction == ACTION_CUT) {
            if (whole) {
                model.removeAllElements();
            } else {
                int i = this.getSelectedIndex();

                if (i >= 0) {
                    model.remove(i);
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param action
     *            DOCUMENT ME!
     */
    public void setCompleteAction(int action) {
        completeAction = action;
    }

    /**
     * DOCUMENT ME!
     */
    public void cancel() {
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object getObject() {
        return getSelectedValue();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Object[] getAllObject() {
        return getItems().toArray();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getSourceComponent() {
        return this;
    }

    /**
     * DOCUMENT ME!
     *
     * @param obj
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean canDrop(JatoolsDragSource obj) {
        return true;
    }

    /**
     * DOCUMENT ME!
     *
     * @param source
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean drop(JatoolsDragSource source) {
        Object item = source.getObject();

        if ((source.getSourceComponent() == this) || (item == null)) {
            return false;
        } else {
            if (this.completeAction == ACTION_CUT) {
                if (singleElement) {
                    model.clear();
                }

                model.add(model.size(), item);
            }

            source.complete(false);

            return true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param source
     *            DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public boolean dropAll(JatoolsDragSource source) {
        Object[] items = source.getAllObject();

        if (source.getSourceComponent() == this) {
            return false;
        } else {
            if (this.completeAction == ACTION_CUT) {
                for (int i = 0; i < items.length; i++) {
                    model.add(model.size(), items[i]);
                }
            }

            source.complete(true);

            return true;
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Component getTargetComponent() {
        return this;
    }
}
